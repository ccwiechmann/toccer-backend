package io.ccw.toccer.backend.xpath;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import javax.xml.transform.stream.StreamSource;

import org.junit.jupiter.api.Test;

class SourceTest {

    @Test
    void ofRetainsTextAndSystemId() {
        final Source source = Source.of("<html><body>x</body></html>", "http://example.org/page");
        assertEquals("<html><body>x</body></html>", source.getText());
        assertEquals("http://example.org/page", source.getSystemId());
    }

    @Test
    void systemIdMayBeNull() {
        final Source source = Source.of("plain", null);
        assertNull(source.getSystemId());
    }

    @Test
    void toStreamSourceIsReproducible() throws Exception {
        final Source source = Source.of("<html><body>hello</body></html>", null);

        // First read consumes the stream.
        final String first = read(source.toStreamSource());
        // Second read must still work from the start - no mark/reset needed.
        final String second = read(source.toStreamSource());

        assertEquals("<html><body>hello</body></html>", first);
        assertEquals(first, second);
    }

    @Test
    void toStreamSourceCarriesSystemId() {
        final Source source = Source.of("body", "http://example.org");
        final StreamSource streamSource = source.toStreamSource();
        assertNotNull(streamSource);
        assertEquals("http://example.org", streamSource.getSystemId());
    }

    private static String read(StreamSource source) throws Exception {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(source.getInputStream(), StandardCharsets.UTF_8))) {
            final StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
            return sb.toString().trim();
        }
    }
}
