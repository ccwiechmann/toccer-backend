package io.ccw.toccer.backend.odt;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import io.ccw.toccer.backend.toc.TocEntry;

class OdtExporterTest {

    @TempDir
    Path tempDir;

    private Path export(Map<String, List<TocEntry>> entriesByCategory) throws Exception {
        final Path output = tempDir.resolve("out.odt");
        new OdtExporter(output.toString(), entriesByCategory, false, "Pub", "Contents", "Page").export();
        return output;
    }

    private String readPart(Path odtFile, String part) throws Exception {
        try (ZipFile zip = new ZipFile(odtFile.toFile())) {
            final ZipEntry entry = zip.getEntry(part);
            try (InputStream stream = zip.getInputStream(entry)) {
                return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
            }
        }
    }

    @Test
    void rendersLabelsTitleAuthorAndPage() throws Exception {
        final TocEntry entry = new TocEntry(0);
        entry.setTitle("My Article");
        entry.setAuthor("Alice");
        entry.setPage("12");
        entry.setCategory("Science");

        final Path odt = export(Map.of("SCIENCE", List.of(entry)));
        final String content = readPart(odt, "content.xml");
        final String styles = readPart(odt, "styles.xml");

        assertTrue(styles.contains("Pub"), "publication label should be applied (styles.xml)");
        assertTrue(content.contains("Contents"), "TOC label should be applied");
        assertTrue(content.contains("Page"), "issue-page label should be applied");
        assertTrue(content.contains("SCIENCE"), "category heading should be rendered (upper-cased)");
        assertTrue(content.contains("Alice"), "author should be rendered");
        assertTrue(content.contains("My Article"), "title should be rendered");
        assertTrue(content.contains("12"), "page number should be rendered");
    }

    @Test
    void rendersEntryWithoutPageOrVolume() throws Exception {
        final TocEntry entry = new TocEntry(0);
        entry.setTitle("Bare Title");
        entry.setAuthor("Bob");
        entry.setCategory("Misc");

        final String content = readPart(export(Map.of("MISC", List.of(entry))), "content.xml");
        assertTrue(content.contains("Bare Title"), "title should be rendered");
        assertTrue(content.contains("Bob"), "author should be rendered");
    }

    @Test
    void entriesAreSortedByPageWithinCategory() throws Exception {
        final TocEntry second = new TocEntry(0);
        second.setTitle("Second");
        second.setPage("30");
        final TocEntry first = new TocEntry(1);
        first.setTitle("First");
        first.setPage("5");

        final String content = readPart(export(Map.of("CAT", List.of(second, first))), "content.xml");
        assertTrue(content.indexOf("First") < content.indexOf("Second"), "smaller page number should come first (5 before 30)");
    }
}
