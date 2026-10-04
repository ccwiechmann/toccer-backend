package io.ccw.toccer.backend.xpath;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class HtmlExtractorTest {

    @Test
    void stripsPreambleBeforeFirstHtmlTag() {
        assertEquals("<html><body>x</body></html>",
                HtmlExtractor.stripPreamble("preamble <html><body>x</body></html>"));
    }

    @Test
    void isCaseInsensitive() {
        assertEquals("<HTML><body>x</body></HTML>",
                HtmlExtractor.stripPreamble("noise <HTML><body>x</body></HTML>"));
    }

    @Test
    void returnsInputUnchangedWhenNoHtmlTag() {
        final String input = "just text, no markup";
        assertEquals(input, HtmlExtractor.stripPreamble(input));
    }

    @Test
    void htmlAlreadyAtStartIsLeftAlone() {
        final String input = "<html><body>x</body></html>";
        assertEquals(input, HtmlExtractor.stripPreamble(input));
    }

    @Test
    void handlesWhitespacePreamble() {
        assertEquals("<html>x</html>", HtmlExtractor.stripPreamble("   \n\t <html>x</html>"));
    }

    @Test
    void nullYieldsEmptyString() {
        assertEquals("", HtmlExtractor.stripPreamble(null));
    }
}
