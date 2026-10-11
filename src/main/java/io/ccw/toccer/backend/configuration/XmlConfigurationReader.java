package io.ccw.toccer.backend.configuration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.ccw.toccer.backend.ToccerException;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import javax.xml.xpath.XPathExpressionException;

import io.ccw.toccer.backend.xpath.Source;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.xml.sax.SAXException;

import io.ccw.toccer.backend.model.ToccerSettings;
import io.ccw.toccer.backend.xpath.XPathResolver;
import io.ccw.toccer.backend.generated.CachedXpathResult;
import io.ccw.toccer.backend.generated.CountStrategy;
import io.ccw.toccer.backend.generated.FixedPostUrlStrategy;
import io.ccw.toccer.backend.generated.FixedPostUrlStrategy.FixedUrls;
import io.ccw.toccer.backend.generated.InputSchema;
import io.ccw.toccer.backend.generated.InputSchema.EmptyCategoryReplacements;
import io.ccw.toccer.backend.generated.ManualCategoryReplacement;
import io.ccw.toccer.backend.generated.ManualVolumeReplacement;
import io.ccw.toccer.backend.generated.MultiDataOnOnePageCountStrategy;
import io.ccw.toccer.backend.generated.NumberStrategy;
import io.ccw.toccer.backend.generated.Strategy;
import io.ccw.toccer.backend.generated.Xpath;
import net.sf.saxon.s9api.Processor;

public class XmlConfigurationReader {

    private final ToccerSettings settings;
    private String baseHtml;
    private String urlSuffix;
    private MultiDataOnOnePageCountStrategy multiDataOnOnePageCountStrategy;
    private FixedPostUrlStrategy fixedPostUrlStrategy;
    private EmptyCategoryReplacements emptyCategoryReplacements;
    private Strategy strategy;
    private List<ManualVolumeReplacement> volumeReplacements;

    /**
     * Creates a reader bound to the given command line settings.
     *
     * @param settings the ToccerSettings (never {@code null})
     */
    public XmlConfigurationReader(ToccerSettings settings) {
        this.settings = settings;
    }

    /**
     * @return the {@link Strategy} derived from the input XML, or {@code null}
     *         until {@link #getFinalSites()} has been called
     */
    public Strategy getStrategy() {
        return strategy;
    }

    /**
     * Loads and validates the input template and resolves every site listed
     * inside it.
     *
     * @return one {@link XpathResolvableResult} per final site, never
     *         {@code null}
     */
    public List<XpathResolvableResult> getFinalSites() {
        final List<XpathResolvableResult> results = new ArrayList<>();

        final java.nio.file.Path configPath = java.nio.file.Paths.get(settings.getInputTemplate());
        if (!java.nio.file.Files.isRegularFile(configPath)) {
            throw new ToccerException("Input template not found or not a regular file: " + configPath);
        }

        try (java.io.InputStream xmlContent = java.nio.file.Files.newInputStream(configPath)) {
            final JAXBContext context = JAXBContext.newInstance(InputSchema.class);
            final Unmarshaller unmarshaller = context.createUnmarshaller();

            try (java.io.InputStream xsdStream = getClass().getResourceAsStream("/inputSchema.xsd")) {
                if (xsdStream == null) {
                    throw new ToccerException("Input schema resource not found on classpath: inputSchema.xsd");
                }
                final byte[] xsdBytes = xsdStream.readAllBytes();
                unmarshaller.setSchema(
                        SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
                                .newSchema(new StreamSource(new java.io.ByteArrayInputStream(xsdBytes))));
            }

            final InputSchema configuration = (InputSchema) unmarshaller.unmarshal(xmlContent);

            settings.setAuthorXpath(configuration.getAuthorXpath());
            settings.setCategoryXpath(configuration.getCategoryXpath());
            settings.setPageXpath(configuration.getPageXpath());
            settings.setTitleXpath(configuration.getTitleXpath());
            settings.setVolumeXpath(configuration.getVolumeXpath());
            settings.setLabelIssuePage(configuration.getLabelIssuePage());
            settings.setLabelNameOfPublication(configuration.getLabelNameOfPublication());
            settings.setLabelToc(configuration.getLabelToc());
            baseHtml = configuration.getBaseUrl();
            urlSuffix = StringEscapeUtils.unescapeXml(configuration.getUrlSuffix());
            settings.setSortCategories(configuration.isSortCategories());
            emptyCategoryReplacements = configuration.getEmptyCategoryReplacements();
            volumeReplacements = configuration.getManualVolumeReplacement();
            if (urlSuffix == null) {
                urlSuffix = "";
            }

            final String startingHtmlPage = configuration.getStartingHtmlPage() == null ? ""
                    : configuration.getStartingHtmlPage();
            final XpathResolvableResult result = new XpathResolvableResult();
            result.resolvableResult = startingHtmlPage + urlSuffix;

            final Map<String, String> xPathResultCache = new HashMap<>();

            results.addAll(parseDefinedXpathReplacements(configuration.getXpath(), result, xPathResultCache));
        } catch (JAXBException | SAXException | NumberFormatException | XPathExpressionException | IOException e) {
            throw new ToccerException("Failed to load or validate the input template: " + configPath, e);
        }

        return results;
    }

    private List<XpathResolvableResult> parseDefinedXpathReplacements(Xpath xpath, XpathResolvableResult site,
            Map<String, String> xPathResultCache) throws NumberFormatException, XPathExpressionException, IOException {

        List<XpathResolvableResult> results = new ArrayList<>();
        final Strategy localStrategy = xpath.getStrategy();

        if (localStrategy instanceof NumberStrategy) {
            results = handleXpathNumberStrategy(xpath);
            strategy = localStrategy;
        } else if (localStrategy instanceof CountStrategy) {
            results = handleXpathCountStrategy(xpath, site.resolvableResult);
            strategy = localStrategy;
        } else if (localStrategy instanceof MultiDataOnOnePageCountStrategy) {
            multiDataOnOnePageCountStrategy = (MultiDataOnOnePageCountStrategy) localStrategy;
            return Arrays.asList(site);
        } else if (localStrategy instanceof FixedPostUrlStrategy) {
            fixedPostUrlStrategy = (FixedPostUrlStrategy) localStrategy;
            results = handleFixedPostUrlStrategy(xpath);
        }

        final Processor processor = XPathResolver.getProcessor();
        Source source = null;

        if (!(localStrategy instanceof FixedPostUrlStrategy)) {
            source = XPathResolver.fetchSource(site.resolvableResult);
        }

        final List<XpathResolvableResult> resultingPages = new ArrayList<>();
        for (final XpathResolvableResult result : results) {
            if (localStrategy instanceof FixedPostUrlStrategy) {
                resultingPages.add(result);
            } else {
                final String content = result.content != null
                        ? singleValue(processor, source, result.content, false,
                                "resolving POST content for " + site.resolvableResult)
                        : null;
                final List<String> partialResults = XPathResolver.resolveXPath(processor, source,
                        result.resolvableResult);
                final List<XpathResolvableResult> partialResultCompleted = new ArrayList<>();
                for (final String part : partialResults) {
                    if (StringUtils.isEmpty(part)) {
                        continue;
                    }
                    final XpathResolvableResult partialResult = new XpathResolvableResult();
                    partialResult.resolvableResult = baseHtml + part + urlSuffix;
                    partialResult.headers = result.headers;
                    partialResult.content = content;
                    partialResultCompleted.add(partialResult);
                }
                resultingPages.addAll(partialResultCompleted);
            }
        }

        final CachedXpathResult cachedXpathResult = xpath.getCachedXpathResult();
        if (cachedXpathResult != null) {
            xPathResultCache.put(cachedXpathResult.getName(),
                    singleValue(processor, source, cachedXpathResult.getXpath(), true,
                            "resolving cached XPath result '" + cachedXpathResult.getName() + "' for "
                                    + site.resolvableResult));

            resultingPages.forEach(resultingPage -> resultingPage.xPathResultCache = xPathResultCache);
        }

        if (xpath.getNext() == null) {
            return resultingPages;
        } else {
            final List<XpathResolvableResult> leafPages = new ArrayList<>();
            for (final XpathResolvableResult page : resultingPages) {
                leafPages.addAll(parseDefinedXpathReplacements(xpath.getNext(), page, new HashMap<>(xPathResultCache)));
            }
            return leafPages;
        }
    }

    /**
     * Resolves an XPath expression that is expected to yield exactly one value
     * and returns that value, throwing a descriptive {@link ToccerException}
     * when the expression matched nothing (or more) than a single result.
     *
     * @param processor the shared Saxon processor
     * @param source the document to query
     * @param xpath the expression to evaluate
     * @param escapeResults whether the value should be escaped
     * @param context human-readable context for the error message
     * @return the single matched value (possibly empty)
     */
    private static String singleValue(Processor processor, Source source, String xpath, boolean escapeResults,
            String context) {
        final List<String> values = XPathResolver.resolveXPath(processor, source, xpath, escapeResults);
        if (values.isEmpty()) {
            throw new ToccerException("XPath expression matched no result " + context + ": " + xpath);
        }
        return values.get(0);
    }

    private List<XpathResolvableResult> handleXpathNumberStrategy(Xpath xpath) {
        final List<XpathResolvableResult> results = new ArrayList<>();
        final NumberStrategy strategy = (NumberStrategy) xpath.getStrategy();

        final String expression = xpath.getExpressionWithReplaces();
        for (int i = strategy.getFrom(); i <= strategy.getTo(); i++) {
            final XpathResolvableResult result = new XpathResolvableResult();
            final String number = strategy.getFillWithPrefixedZerosToLength() != null
                    ? StringUtils.leftPad(Integer.toString(i), strategy.getFillWithPrefixedZerosToLength(), '0')
                    : Integer.toString(i);
            result.resolvableResult = expression.replace("{0}", number);
            result.headers = new ArrayList<>(strategy.getHeaders());
            if (strategy.getPostContent() != null) {
                result.content = strategy.getPostContent().replace("{0}", number);
            }
            results.add(result);
        }

        return results;
    }

    private List<XpathResolvableResult> handleFixedPostUrlStrategy(Xpath xpath) {
        final List<XpathResolvableResult> results = new ArrayList<>();
        final FixedPostUrlStrategy strategy = (FixedPostUrlStrategy) xpath.getStrategy();

        for (final FixedUrls fixedUrl : strategy.getFixedUrls()) {
            final XpathResolvableResult result = new XpathResolvableResult();
            result.content = strategy.getPostContent();
            result.resolvableResult = fixedUrl.getUrl();
            result.headers = new ArrayList<>(strategy.getHeaders());
            results.add(result);
        }

        return results;
    }

    private List<XpathResolvableResult> handleXpathCountStrategy(Xpath xpath, String site)
            throws NumberFormatException, XPathExpressionException, IOException {
        final List<XpathResolvableResult> results = new ArrayList<>();
        final Processor processor = XPathResolver.getProcessor();
        final Source source = XPathResolver.fetchSource(site);

        final String expression = xpath.getExpressionWithReplaces();
        final CountStrategy strategy = (CountStrategy) xpath.getStrategy();
        final int count = Integer.valueOf(
                singleValue(processor, source, strategy.getXpath(), true,
                        "counting items for " + site));
        for (int i = 1; i <= count; i++) {
            final XpathResolvableResult result = new XpathResolvableResult();
            result.resolvableResult = expression.replace("{0}", Integer.toString(i));
            if (strategy.getPostContent() != null) {
                result.content = strategy.getPostContent().replace("{0}", Integer.toString(i));
            }
            result.headers = new ArrayList<>(strategy.getHeaders());
            results.add(result);
        }

        return results;
    }

    /**
     * @return the count strategy for multi-data entries, or {@code null}
     */
    public MultiDataOnOnePageCountStrategy getMultiDataOnOnePageCountStrategy() {
        return multiDataOnOnePageCountStrategy;
    }

    /**
     * @return the fixed-URL strategy, or {@code null}
     */
    public FixedPostUrlStrategy getFixedPostUrlStrategy() {
        return fixedPostUrlStrategy;
    }

    /**
     * Returns the category for an entry, falling back to a manual rule from
     * the configuration when the resolved category is empty.
     *
     * @param category resolved category, may be {@code null}
     * @param title entry title, may be {@code null}
     * @param volume entry volume, may be {@code null}
     * @return the category to use (or an empty string)
     */
    public String getCategoryReplacementIfAvailable(String category, String title, String volume) {
        if (emptyCategoryReplacements == null) {
            return category;
        }

        for (final ManualCategoryReplacement replacement : emptyCategoryReplacements.getManualCategoryReplacement()) {
            if (replacement.getIfTitleContains() != null && title != null
                    && StringUtils.contains(title, replacement.getIfTitleContains())) {
                return replacement.getCategoryName();
            } else if (replacement.getIfVolumeIs() != null && volume != null
                    && StringUtils.equals(volume, replacement.getIfVolumeIs())) {
                return replacement.getCategoryName();
            }
        }

        if (StringUtils.isNotEmpty(emptyCategoryReplacements.getFallbackCategoryName())) {
            return emptyCategoryReplacements.getFallbackCategoryName();
        }

        return category;
    }

    /**
     * @param volume the raw volume from the page
     * @return the configured replacement volume or the input unchanged
     */
    public String getVolumeReplacement(String volume) {
        if (volumeReplacements == null) {
            return volume;
        }
        for (final ManualVolumeReplacement replacement : volumeReplacements) {
            if (StringUtils.equals(volume, replacement.getIfVolumeEquals())) {
                return replacement.getVolumeName();
            }
        }
        return volume;
    }

    /** A single resolved page: its URL, the request body and per-page cache. */
    public static class XpathResolvableResult {
        private String resolvableResult;
        private List<String> headers = new ArrayList<String>();
        private String content;
        private Map<String, String> xPathResultCache = new HashMap<>();

        /**
         * @return the POST request body, may be {@code null}
         */
        public String getContent() {
            return content;
        }

        /**
         * @return the raw headers, always an unmodifiable list
         */
        public List<String> getHeaders() {
            return Collections.unmodifiableList(headers);
        }

        /**
         * @return the absolute URL for this page (already suffixed)
         */
        public String getResolvableResult() {
            return resolvableResult;
        }

        /**
         * @return the cached XPath results of expressions marked with
         *         {@code cachedXpathResult}, unmodifiable
         */
        public Map<String, String> getxPathResultCache() {
            return Collections.unmodifiableMap(xPathResultCache);
        }
    }
}
