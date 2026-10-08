package io.ccw.toccer.backend.xpath;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.ccw.toccer.backend.ToccerException;

import net.sf.saxon.lib.FeatureKeys;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XdmItem;

/**
 * XPath / HTTP entry point for the rest of the application.
 *
 * <p>
 * Two concerns are combined because they are usually used together: fetching a
 * page (HTTP GET or POST) — converting a JSON payload to HTML when required —
 * and evaluating XPath expressions against the fetched document.
 * </p>
 *
 * <p>
 * {@link #fetchSource(PageRequest, boolean)} returns a reusable {@link Source}:
 * every call to {@link Source#toStreamSource()} starts from the beginning of
 * the document, which removes the {@code mark}/{@code reset} handling the old
 * {@code StreamSource}-based version required.
 * </p>
 *
 * <p>
 * HTTP runs on the JDK {@link java.net.http.HttpClient}. A custom
 * {@link PageFetcher} can be installed via {@link #setFetcher(PageFetcher)} to
 * avoid network I/O during tests.
 * </p>
 */
public final class XPathResolver {

    private static final Logger LOG = LoggerFactory.getLogger(XPathResolver.class);

    private static volatile PageFetcher fetcher = new HttpPageFetcher();

    private static volatile Processor processor;

    private XPathResolver() {
    }

    /**
     * Installs a custom {@link PageFetcher}. Useful in tests to return canned
     * bodies instead of performing network I/O. Passing {@code null} resets the
     * fetcher to the default {@link HttpPageFetcher}.
     *
     * @param fetcher the fetcher to use, or {@code null} for the default
     */
    public static void setFetcher(PageFetcher fetcher) {
        XPathResolver.fetcher = (fetcher != null) ? fetcher : new HttpPageFetcher();
    }

    /**
     * Returns the single shared Saxon {@link Processor} wired to the TagSoup parser so that
     * broken / truncated HTML documents can be loaded without exceptions.
     *
     * <p>
     * Saxon {@link Processor} instances are expensive to build and are thread-safe,
     * so we create exactly one per JVM and reuse it across all calls. Building one
     * per page is pure overhead with no benefit.
     * </p>
     *
     * @return the shared processor
     */
    public static Processor getProcessor() {
        if (processor == null) {
            synchronized (XPathResolver.class) {
                if (processor == null) {
                    final Processor fresh = new Processor(false);
                    fresh.setConfigurationProperty(FeatureKeys.SOURCE_PARSER_CLASS, "org.ccil.cowan.tagsoup.Parser");
                    processor = fresh;
                }
            }
        }
        return processor;
    }

    /**
     * Fetches the page described by {@code request} and returns a reusable
     * {@link Source}.
     *
     * <p>
     * If {@code isJson} is {@code true}, the raw body is treated as a JSON
     * payload and converted to a simple HTML page via
     * {@link JsonToHtmlConverter}. Otherwise the body is used as-is (after
     * stripping any preamble that some sites serve before the first
     * {@code &lt;html} tag, preserving the behaviour of the old
     * {@code ReadFromHtmlInputStream}).
     * </p>
     *
     * @param request the page to fetch
     * @param isJson whether the response body should be treated as JSON
     * @return a reusable source
     * @throws IllegalStateException if the HTTP request fails
     */
    public static Source fetchSource(PageRequest request, boolean isJson) {
        final String body;
        try {
            body = fetcher.fetch(request);
        } catch (final Exception e) {
            throw new ToccerException("Could not fetch " + request.getUrl() + ": " + e.getMessage(), e);
        }

        final String document = isJson
                ? JsonToHtmlConverter.getHtmlData(body)
                : HtmlExtractor.stripPreamble(body);

        LOG.debug("Fetched {} ({} chars)", request.getUrl(), document.length());
        return Source.of(document, request.getUrl());
    }

    /**
     * Convenience overload for a plain {@code GET}, no custom headers, body or
     * JSON conversion.
     *
     * @param url the absolute URL to fetch
     * @return a reusable source
     */
    public static Source fetchSource(String url) {
        return fetchSource(new PageRequest(url, false, null, null), false);
    }

    /**
     * Convenience overload for callers that pass the parameters individually.
     *
     * @param url the absolute URL to fetch
     * @param isJson whether the response is JSON to be converted to HTML
     * @param headers raw {@code "Name: value"} header strings, may be
     *            {@code null}
     * @param isPost whether to send a POST request
     * @param content the POST body, may be {@code null}
     * @return a reusable source
     */
    public static Source fetchSource(String url, boolean isJson, java.util.List<String> headers, boolean isPost,
            String content) {
        return fetchSource(new PageRequest(url, isPost, content, headers), isJson);
    }

    /**
     * Resolves every value matched by an XPath expression. Escapes each
     * non-empty value (XML character sanitation + HTML 4 escaping, same as the
     * historical implementation) and collapses inner whitespace.
     *
     * @param processor a Saxon processor
     * @param source the document to query
     * @param xpath the expression to evaluate
     * @return one entry per matched value, never {@code null}
     */
    public static List<String> resolveXPath(Processor processor, Source source, String xpath) {
        return resolveXPath(processor, source, xpath, true);
    }

    /**
     * Same as {@link #resolveXPath(Processor, Source, String)} but with an
     * explicit switch that controls whether the resulting values are escaped.
     *
     * @param processor a Saxon processor
     * @param source the document to query
     * @param xpath the expression to evaluate
     * @param escapeResults whether to escape and strip non-XML characters
     * @return one entry per matched value, never {@code null}
     * @throws IllegalStateException if the expression cannot be compiled or
     *         evaluated
     */
    public static List<String> resolveXPath(Processor processor, Source source, String xpath,
            boolean escapeResults) {
        LOG.debug("Resolving expression \"{}\"", xpath);
        try {
            final net.sf.saxon.s9api.XPathCompiler compiler = processor.newXPathCompiler();
            compiler.setCaching(false);
            compiler.declareNamespace("html", "http://www.w3.org/1999/xhtml");
            final net.sf.saxon.s9api.XPathSelector selector = compiler.compile(xpath).load();
            selector.setContextItem(source.getNode(processor));

            final List<String> result = new ArrayList<>();
            for (final java.util.Iterator<XdmItem> it = selector.evaluate().iterator(); it.hasNext();) {
                final String raw = it.next().getStringValue();
                if (StringUtils.isNotEmpty(raw)) {
                    result.add(escapeResults
                            ? StringUtils.normalizeSpace(StringEscapeUtils.escapeHtml4(raw))
                            : StringUtils.normalizeSpace(raw));
                } else {
                    result.add("");
                }
            }
            return result;
        } catch (final SaxonApiException e) {
            throw new ToccerException("XPath evaluation failed for: " + xpath, e);
        }
    }

    /**
     * Same as {@link #resolveXPath(Processor, Source, String)} but returns at
     * least one element — a single empty string if the expression matched
     * nothing. Useful for callers that need to index {@code [0]} without
     * checking.
     *
     * @param processor a Saxon processor
     * @param source the document to query
     * @param xpath the expression to evaluate
     * @return one entry per matched value, or {@code [""]} if none matched
     */
    public static List<String> resolveXPathNoEmptyResult(Processor processor, Source source, String xpath) {
        final List<String> result = resolveXPath(processor, source, xpath);
        if (result.isEmpty()) {
            result.add("");
        }
        return result;
    }
}
