package io.ccw.toccer.backend.toc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import io.ccw.toccer.backend.model.TocEntry;
import io.ccw.toccer.backend.model.ToccerSettings;
import io.ccw.toccer.backend.xpath.PageFetcher;
import io.ccw.toccer.backend.xpath.XPathResolver;

class TocTest {

    @TempDir
    Path tempDir;

    private ToccerSettings settings;

    @BeforeEach
    void setUp() {
        settings = new ToccerSettings();
        XPathResolver.setFetcher(request -> "<html><head></head><body>"
                + "<h1>T1</h1><h1>T2</h1>"
                + "<p>A1</p><p>A2</p>"
                + "<i>C1</i><i>C2</i>"
                + "</body></html>");
    }

    @AfterEach
    void tearDown() {
        XPathResolver.setFetcher(null);
    }

    private void pointTo(Path config) throws Exception {
        final Field field = ToccerSettings.class.getDeclaredField("inputTemplate");
        field.setAccessible(true);
        field.set(settings, config.toString());
    }

    private Path config(String strategyXpath) throws Exception {
        final String xml = """
                <?xml version="1.0" encoding="UTF-8"?>
                <tns:inputSchema
                    xmlns:tns="http://www.ccw.io/toccer-backend/inputSchema"
                    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
                    %s
                    <tns:titleXpath>//html:h1[{0}]</tns:titleXpath>
                    <tns:authorXpath>//html:p[{0}]</tns:authorXpath>
                    <tns:categoryXpath>//html:i[{0}]</tns:categoryXpath>
                    <tns:startingHtmlPage>http://example.org/start</tns:startingHtmlPage>
                    <tns:baseUrl>http://example.org/base</tns:baseUrl>
                    <tns:labelNameOfPublication>Pub</tns:labelNameOfPublication>
                    <tns:labelToc>Contents</tns:labelToc>
                    <tns:labelIssuePage>Page</tns:labelIssuePage>
                    <tns:sortCategories>false</tns:sortCategories>
                </tns:inputSchema>
                """.formatted(strategyXpath);
        final Path file = tempDir.resolve("config.xml");
        Files.writeString(file, xml);
        return file;
    }

    @Test
    void multiDataStrategyBuildsOneEntryPerItem() throws Exception {
        // Regression: before the fix this threw NPE at reader.getStrategy().isJson()
        // because the MultiData strategy is stored separately (getStrategy() is null).
        pointTo(config("""
                <tns:xpath>
                    <tns:strategy xsi:type="tns:multiDataOnOnePageCountStrategy">
                        <tns:maxXpath>count(//html:h1)</tns:maxXpath>
                        <tns:categoryForEachEntry>true</tns:categoryForEachEntry>
                        <tns:titleForEachEntry>true</tns:titleForEachEntry>
                    </tns:strategy>
                </tns:xpath>
                """));

        final List<TocEntry> entries = Toc.create(settings).loadFinalSites().generateTocEntries().getEntries();

        assertNotNull(entries);
        assertEquals(2, entries.size());
        assertEquals("T1", entries.get(0).getTitleForXml());
        assertEquals("A1", entries.get(0).getAuthorForXml());
        assertEquals("C1", entries.get(0).getCategoryForXml());
        assertEquals("T2", entries.get(1).getTitleForXml());
        assertEquals("A2", entries.get(1).getAuthorForXml());
        assertEquals("C2", entries.get(1).getCategoryForXml());
    }

    @Test
    void exportToOdtBeforeGenerateThrowsIllegalState() throws Exception {
        pointTo(config("""
                <tns:xpath>
                    <tns:strategy xsi:type="tns:multiDataOnOnePageCountStrategy">
                        <tns:maxXpath>count(//html:h1)</tns:maxXpath>
                        <tns:titleForEachEntry>true</tns:titleForEachEntry>
                    </tns:strategy>
                </tns:xpath>
                """));
        final Toc toc = Toc.create(settings).loadFinalSites();
        org.junit.jupiter.api.Assertions.assertThrows(IllegalStateException.class, toc::exportToOdt,
                "exportToOdt() before generateTocEntries() must throw IllegalStateException, not NPE");
    }

    @Test
    void sizeMismatchThrowsInformativeError() throws Exception {
        // 2 titles vs 1 author on the same page -> sizes disagree -> ToccerException.
        XPathResolver.setFetcher(request -> "<html><head></head><body>"
                + "<h1>T1</h1><h1>T2</h1>"
                + "<p>OnlyOneAuthor</p>"
                + "</body></html>");
        pointTo(config("""
                <tns:xpath>
                    <tns:strategy xsi:type="tns:multiDataOnOnePageCountStrategy">
                        <tns:maxXpath>count(//html:h1)</tns:maxXpath>
                        <tns:titleForEachEntry>true</tns:titleForEachEntry>
                    </tns:strategy>
                </tns:xpath>
                """));

        final Toc toc = Toc.create(settings).loadFinalSites();
        org.junit.jupiter.api.Assertions.assertThrows(io.ccw.toccer.backend.ToccerException.class,
                toc::generateTocEntries);
    }
}
