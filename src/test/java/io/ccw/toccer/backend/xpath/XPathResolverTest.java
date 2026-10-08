package io.ccw.toccer.backend.xpath;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.ccw.toccer.backend.ToccerException;
import net.sf.saxon.s9api.Processor;

class XPathResolverTest {

    private static final Processor PROCESSOR = XPathResolver.getProcessor();

    @BeforeAll
    static void installFakeFetcher() {
        XPathResolver.setFetcher(TestFetchers.DEFAULT);
    }

    @AfterAll
    static void resetFetcher() {
        XPathResolver.setFetcher(null);
    }

    // ------------------------------------------------------------------ Source

    @Test
    void fetchSourceStripsPreambleByDefault() {
        final Source source = XPathResolver.fetchSource("http://example.org");
        assertTrue(source.getText().startsWith("<html>"), source.getText());
        assertEquals("http://example.org", source.getSystemId());
    }

    @Test
    void fetchSourceConvertsJsonToHtml() {
        XPathResolver.setFetcher(request -> "{\"title\":\"T\"}");
        final Source source = XPathResolver
                .fetchSource(new PageRequest("http://example.org", false, null, null), true);
        assertTrue(source.getText().contains("converted"), source.getText());
        assertTrue(source.getText().contains("class=\"title\""), source.getText());
        XPathResolver.setFetcher(TestFetchers.DEFAULT);
    }

    @Test
    void fetchSourceFailureIsWrapped() {
        XPathResolver.setFetcher(PageRequest -> {
            throw new RuntimeException("boom");
        });
        assertThrows(ToccerException.class,
                () -> XPathResolver.fetchSource(new PageRequest("http://example.org", false, null, null), false));
    }

    // ---------------------------------------------------------------- Evaluate

    @Test
    void resolveXPathEscapesAndNormalizesSpace() {
        final Source source = Source.of("<html><body><div class=\"t\">  hello &amp;  world </div></body></html>", null);
        // The source already contains a literal "&amp;" (a real ampersand in the doc is written
        // as an entity); escapeHtml4 will re-escape the resulting "&".
        final List<String> result = XPathResolver.resolveXPath(PROCESSOR, source, "//html:div");
        assertEquals(1, result.size());
        assertEquals("hello &amp; world", result.get(0));
    }

    @Test
    void resolveXPathWithEscapeOffKeepsRawValue() {
        final Source source = Source.of("<html><body><div class=\"t\">  hi  there </div></body></html>", null);
        final List<String> escaped = XPathResolver.resolveXPath(PROCESSOR, source, "//html:div", true);
        final List<String> raw = XPathResolver.resolveXPath(PROCESSOR, source, "//html:div", false);
        assertEquals("hi there", escaped.get(0));
        assertEquals("hi there", raw.get(0));
    }

    @Test
    void resolveXPathEscapesAmpersand() {
        final Source source = Source.of("<html><body><div class=\"t\">a &amp; b</div></body></html>", null);
        final List<String> escaped = XPathResolver.resolveXPath(PROCESSOR, source, "//html:div", true);
        assertEquals("a &amp; b", escaped.get(0));
        final List<String> raw = XPathResolver.resolveXPath(PROCESSOR, source, "//html:div", false);
        assertEquals("a & b", raw.get(0));
    }

    @Test
    void resolveXPathNoEmptyResultPadsWithEmptyString() {
        final Source source = Source.of("<html><body/></html>", null);
        final List<String> result = XPathResolver.resolveXPathNoEmptyResult(PROCESSOR, source, "//html:does-not-exist");
        assertEquals(List.of(""), result);
    }

    @Test
    void invalidXPathThrowsIllegalState() {
        final Source source = Source.of("<html><body/></html>", null);
        assertThrows(ToccerException.class, () -> XPathResolver.resolveXPath(PROCESSOR, source, "this is not [ xpath"));
    }

    // ------------------------------------------------------------------ Fixture

    /**
     * A tiny in-memory fetcher that returns a fixed HTML page and ignores the
     * concrete request, so tests never touch the network.
     */
    static final class TestFetchers {
        private static final String BODY = "<html><head></head><body><h1 id=\"x\">Hello</h1></body></html>";

        static final PageFetcher DEFAULT = request -> BODY;
    }
}
