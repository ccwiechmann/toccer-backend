package de.ccw.toccer.backend.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.xml.sax.SAXException;

import de.ccw.toccer.backend.toc.ToccerSettings;
import de.ccw.toccer.backend.xpath.XPathResolver;
import de.ccw.toccer.generated.FixedPostUrlStrategy;
import de.ccw.toccer.generated.FixedPostUrlStrategy.FixedUrls;
import de.ccw.toccer.generated.InputSchema;
import de.ccw.toccer.generated.ManualCategoryReplacement;
import de.ccw.toccer.generated.ManualVolumeReplacement;
import de.ccw.toccer.generated.MultiDataOnOnePageCountStrategy;
import de.ccw.toccer.generated.Xpath;
import net.sf.saxon.s9api.Processor;

public class XmlConfigurationReader {

	private final ToccerSettings settings;
	private String baseHtml;
	private String urlSuffix;
	private MultiDataOnOnePageCountStrategy multiDataOnOnePageCountStrategy;
	private FixedPostUrlStrategy fixedPostUrlStrategy;
	private InputSchema.EmptyCategoryReplacements emptyCategoryReplacements;
	private List<ManualVolumeReplacement> volumeReplacements;

	public XmlConfigurationReader(ToccerSettings settings) {
		this.settings = settings;
	}

	public List<XpathResolvableResult> getFinalSites() {
		final List<XpathResolvableResult> results = new ArrayList<>();

		try {
			final FileInputStream xmlContent = new FileInputStream(settings.getInputTemplate());
			final JAXBContext context = JAXBContext.newInstance(InputSchema.class);
			final Unmarshaller unmarshaller = context.createUnmarshaller();

			final Schema schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
					.newSchema(new File("src/main/resources/inputSchema.xsd"));
			unmarshaller.setSchema(schema);

			final InputSchema configuration = (InputSchema) unmarshaller.unmarshal(xmlContent);

			settings.setAuthorXpath(configuration.getAuthorXpath());
			settings.setCategoryXpath(configuration.getCategoryXpath());
			settings.setPageXpath(configuration.getPageXpath());
			settings.setTitleXpath(configuration.getTitleXpath());
			settings.setVolumeXpath(configuration.getVolumeXpath());
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

			results.addAll(parseDefinedXpathReplacements(configuration.getXpath(), result));
		} catch (JAXBException | SAXException | NumberFormatException | XPathExpressionException | IOException e) {
			throw new IllegalStateException(e);
		}

		return results;
	}

	private List<XpathResolvableResult> parseDefinedXpathReplacements(Xpath xpath, XpathResolvableResult site)
			throws NumberFormatException, XPathExpressionException, IOException {

		List<XpathResolvableResult> results = new ArrayList<>();
		if (xpath.getNumberStrategy() != null) {
			results = handleXpathNumberStrategy(xpath);
		} else if (xpath.getCountStrategy() != null) {
			results = handleXpathCountStrategy(xpath, site.resolvableResult);
		} else if (xpath.getMultiDataOnOnePageCountStrategy() != null) {
			multiDataOnOnePageCountStrategy = xpath.getMultiDataOnOnePageCountStrategy();
			return Arrays.asList(site);
		} else if (xpath.getFixedPostUrlStrategy() != null) {
			fixedPostUrlStrategy = xpath.getFixedPostUrlStrategy();
			results = handleFixedPostUrlStrategy(xpath);
		}

		final Processor processor = XPathResolver.getProcessor();
		StreamSource source = null;

		if (xpath.getFixedPostUrlStrategy() == null) {
			source = XPathResolver.getSource(site.resolvableResult);
		}

		final List<XpathResolvableResult> resultingPages = new ArrayList<>();
		for (final XpathResolvableResult result : results) {
			if (xpath.getFixedPostUrlStrategy() != null) {
				resultingPages.add(result);
			} else {
				final List<String> partialResults = XPathResolver.resolveXPath(processor, source,
						result.resolvableResult);
				final List<XpathResolvableResult> partialResultCompleted = new ArrayList<>();
				for (final String part : partialResults) {
					if (StringUtils.isEmpty(part)) {
						continue;
					}
					final XpathResolvableResult partialResult = new XpathResolvableResult();
					partialResult.resolvableResult = baseHtml + part + urlSuffix;
					partialResultCompleted.add(partialResult);
				}
				resultingPages.addAll(partialResultCompleted);
			}
		}

		if (xpath.getNext() == null) {
			return resultingPages;
		} else {
			final List<XpathResolvableResult> leafPages = new ArrayList<>();
			for (final XpathResolvableResult page : resultingPages) {
				leafPages.addAll(parseDefinedXpathReplacements(xpath.getNext(), page));
			}
			return leafPages;
		}
	}

	private List<XpathResolvableResult> handleXpathNumberStrategy(Xpath xpath) {
		final List<XpathResolvableResult> results = new ArrayList<>();

		final String expression = xpath.getExpressionWithReplaces();
		for (int i = xpath.getNumberStrategy().getFrom(); i <= xpath.getNumberStrategy().getTo(); i++) {
			final XpathResolvableResult result = new XpathResolvableResult();
			result.resolvableResult = expression.replace("{0}", Integer.toString(i));
			results.add(result);
		}

		return results;
	}

	private List<XpathResolvableResult> handleFixedPostUrlStrategy(Xpath xpath) {
		final List<XpathResolvableResult> results = new ArrayList<>();

		for (final FixedUrls fixedUrl : xpath.getFixedPostUrlStrategy().getFixedUrls()) {
			final XpathResolvableResult result = new XpathResolvableResult();
			result.content = fixedUrl.getContent();
			result.resolvableResult = fixedUrl.getUrl();
			result.headers = new ArrayList<>(fixedUrl.getHeaders());
			results.add(result);
		}

		return results;
	}

	private List<XpathResolvableResult> handleXpathCountStrategy(Xpath xpath, String site)
			throws NumberFormatException, XPathExpressionException, IOException {
		final List<XpathResolvableResult> results = new ArrayList<>();
		final Processor processor = XPathResolver.getProcessor();
		final StreamSource source = XPathResolver.getSource(site);

		final String expression = xpath.getExpressionWithReplaces();
		final int count = Integer
				.valueOf(XPathResolver.resolveXPath(processor, source, xpath.getCountStrategy().getXpath()).get(0));
		for (int i = 1; i <= count; i++) {
			final XpathResolvableResult result = new XpathResolvableResult();
			result.resolvableResult = expression.replace("{0}", Integer.toString(i));
			results.add(result);
		}

		return results;
	}

	public MultiDataOnOnePageCountStrategy getMultiDataOnOnePageCountStrategy() {
		return multiDataOnOnePageCountStrategy;
	}

	public FixedPostUrlStrategy getFixedPostUrlStrategy() {
		return fixedPostUrlStrategy;
	}

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

	public String getVolumeReplacement(String volume) {
		for (final ManualVolumeReplacement replacement : volumeReplacements) {
			if (StringUtils.equals(volume, replacement.getIfVolumeEquals())) {
				return replacement.getVolumeName();
			}
		}
		return volume;
	}

	public static class XpathResolvableResult {
		private String resolvableResult;
		private List<String> headers = new ArrayList<String>();
		private String content;

		public String getContent() {
			return content;
		}

		public List<String> getHeaders() {
			return Collections.unmodifiableList(headers);
		}

		public String getResolvableResult() {
			return resolvableResult;
		}
	}
}
