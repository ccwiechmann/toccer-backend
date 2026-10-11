package io.ccw.toccer.backend.configuration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import io.ccw.toccer.backend.configuration.XmlConfigurationReader.XpathResolvableResult;
import io.ccw.toccer.backend.generated.NumberStrategy;
import io.ccw.toccer.backend.model.ToccerSettings;
import io.ccw.toccer.backend.xpath.XPathResolver;

class XmlConfigurationReaderTest {

    @TempDir
    Path tempDir;

    private ToccerSettings settings;

    @BeforeEach
    void setUp() {
        settings = new ToccerSettings();
        XPathResolver.setFetcher(request -> "<html><body>"
                + "<ul id=\"links\"><li><a href=\"/a/1\">1</a></li><li><a href=\"/a/2\">2</a></li><li><a href=\"/a/3\">3</a></li></ul>"
                + "</body></html>");
    }

    @AfterEach
    void tearDown() {
        XPathResolver.setFetcher(null);
    }

    private Path writeConfig(String body) throws Exception {
        return writeConfig(body, "", "");
    }

    private Path writeConfig(String body, String extraAfterCategory, String extraAtEnd) throws Exception {
        final String xml = """
                <?xml version="1.0" encoding="UTF-8"?>
                <tns:inputSchema
                    xmlns:tns="http://www.ccw.io/toccer-backend/inputSchema"
                    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
                    %s
                    <tns:titleXpath>//html:h1</tns:titleXpath>
                    <tns:authorXpath>//html:p</tns:authorXpath>
                    <tns:categoryXpath>//html:i</tns:categoryXpath>
                    %s
                    <tns:baseUrl>http://example.org/base</tns:baseUrl>
                    <tns:labelNameOfPublication>Pub</tns:labelNameOfPublication>
                    <tns:labelToc>Contents</tns:labelToc>
                    <tns:labelIssuePage>Page</tns:labelIssuePage>
                    <tns:sortCategories>false</tns:sortCategories>
                    %s
                </tns:inputSchema>
                """;
        final Path file = tempDir.resolve("config.xml");
        Files.writeString(file, xml.formatted(body.trim(), extraAfterCategory.trim(), extraAtEnd.trim()));
        return file;
    }

    private void pointSettingsTo(Path config) throws Exception {
        final Field field = ToccerSettings.class.getDeclaredField("inputTemplate");
        field.setAccessible(true);
        field.set(settings, config.toString());
    }

    @Test
    void numberStrategyExpandsFullRange() throws Exception {
        pointSettingsTo(writeConfig("""
                <tns:xpath>
                    <tns:expressionWithReplaces>//html:ul[@id='links']/html:li[{0}]/html:a/@href</tns:expressionWithReplaces>
                    <tns:strategy xsi:type="tns:numberStrategy">
                        <tns:from>1</tns:from>
                        <tns:to>3</tns:to>
                    </tns:strategy>
                </tns:xpath>
                """));
        final XmlConfigurationReader reader = new XmlConfigurationReader(settings);
        final List<XpathResolvableResult> sites = reader.getFinalSites();
        assertEquals(3, sites.size());
        assertEquals("http://example.org/base/a/1", sites.get(0).getResolvableResult());
        assertEquals("http://example.org/base/a/2", sites.get(1).getResolvableResult());
        assertEquals("http://example.org/base/a/3", sites.get(2).getResolvableResult());
        assertInstanceOf(NumberStrategy.class, reader.getStrategy());
    }

    @Test
    void countStrategyUsesXPathCountFromPage() throws Exception {
        pointSettingsTo(writeConfig("""
                <tns:xpath>
                    <tns:expressionWithReplaces>//html:ul[@id='links']/html:li[{0}]/html:a/@href</tns:expressionWithReplaces>
                    <tns:strategy xsi:type="tns:countStrategy">
                        <tns:xpath>count(//html:ul[@id='links']/html:li)</tns:xpath>
                    </tns:strategy>
                </tns:xpath>
                """));
        final XmlConfigurationReader reader = new XmlConfigurationReader(settings);
        final List<XpathResolvableResult> sites = reader.getFinalSites();
        assertEquals(3, sites.size());
        assertEquals("http://example.org/base/a/1", sites.get(0).getResolvableResult());
        assertEquals("http://example.org/base/a/3", sites.get(2).getResolvableResult());
        assertInstanceOf(io.ccw.toccer.backend.generated.CountStrategy.class, reader.getStrategy());
    }

    @Test
    void multiDataOnOnePageReturnsStartingPageUnchanged() throws Exception {
        pointSettingsTo(writeConfig("""
                <tns:xpath>
                    <tns:strategy xsi:type="tns:multiDataOnOnePageCountStrategy">
                        <tns:maxXpath>count(//html:p)</tns:maxXpath>
                        <tns:categoryForEachEntry>true</tns:categoryForEachEntry>
                        <tns:titleForEachEntry>true</tns:titleForEachEntry>
                    </tns:strategy>
                </tns:xpath>
                """, "<tns:startingHtmlPage>http://example.org/start</tns:startingHtmlPage>", ""));
        final XmlConfigurationReader reader = new XmlConfigurationReader(settings);
        final List<XpathResolvableResult> sites = reader.getFinalSites();
        assertEquals(1, sites.size());
        assertEquals("http://example.org/start", sites.get(0).getResolvableResult());
        assertNull(reader.getStrategy(), "multi-data strategy is stored separately, not as the generic strategy");
        final io.ccw.toccer.backend.generated.MultiDataOnOnePageCountStrategy strategy = reader.getMultiDataOnOnePageCountStrategy();
        assertNull(reader.getFixedPostUrlStrategy());
        assertEquals("count(//html:p)", strategy.getMaxXpath());
    }

    @Test
    void fixedPostUrlStrategyUsesConfiguredUrlsAndPostContent() throws Exception {
        pointSettingsTo(writeConfig("""
                <tns:xpath>
                    <tns:strategy xsi:type="tns:fixedPostUrlStrategy">
                        <tns:postContent>id={0}</tns:postContent>
                        <tns:headers>Content-Type: application/json</tns:headers>
                        <tns:fixedUrls>
                            <tns:url>http://example.org/api/one</tns:url>
                        </tns:fixedUrls>
                        <tns:fixedUrls>
                            <tns:url>http://example.org/api/two</tns:url>
                        </tns:fixedUrls>
                    </tns:strategy>
                </tns:xpath>
                """));
        final XmlConfigurationReader reader = new XmlConfigurationReader(settings);
        final List<XpathResolvableResult> sites = reader.getFinalSites();
        final io.ccw.toccer.backend.generated.FixedPostUrlStrategy strategy = reader.getFixedPostUrlStrategy();
        assertEquals(2, sites.size());
        assertEquals("http://example.org/api/one", sites.get(0).getResolvableResult());
        assertEquals("http://example.org/api/two", sites.get(1).getResolvableResult());
        assertEquals("Content-Type: application/json", sites.get(0).getHeaders().get(0));
        assertEquals("id={0}", sites.get(0).getContent(), "fixed strategy does not substitute the placeholder");
        assertEquals("id={0}", strategy.getPostContent());
    }

    @Test
    void cachedXpathResultWithoutMatchThrowsToccerException() throws Exception {
        final Path config = writeConfig("""
                <tns:xpath>
                    <tns:expressionWithReplaces>//html:ul[@id='links']/html:li[{0}]/html:a/@href</tns:expressionWithReplaces>
                    <tns:strategy xsi:type="tns:numberStrategy">
                        <tns:from>1</tns:from>
                        <tns:to>1</tns:to>
                    </tns:strategy>
                    <tns:cachedXpathResult>
                        <tns:name>nonexistent</tns:name>
                        <tns:xpath>//html:div[@class='this-does-not-exist']</tns:xpath>
                    </tns:cachedXpathResult>
                </tns:xpath>
                """);
        pointSettingsTo(config);
        final XmlConfigurationReader reader = new XmlConfigurationReader(settings);
        org.junit.jupiter.api.Assertions.assertThrows(io.ccw.toccer.backend.ToccerException.class,
                reader::getFinalSites,
                "a cachedXpathResult that matches nothing should surface a ToccerException, not an IndexOutOfBoundsException");
    }

    @Test
    void categoryReplacementMatchesOnTitleBeforeVolume() throws Exception {
        pointSettingsTo(writeConfig("""
                <tns:xpath>
                    <tns:expressionWithReplaces>//html:ul[@id='links']/html:li[{0}]/html:a/@href</tns:expressionWithReplaces>
                    <tns:strategy xsi:type="tns:numberStrategy">
                        <tns:from>1</tns:from>
                        <tns:to>1</tns:to>
                    </tns:strategy>
                </tns:xpath>
                """, "", """
                     <tns:emptyCategoryReplacements>
                    <tns:fallbackCategoryName>FallbackKategorie</tns:fallbackCategoryName>
                    <tns:manualCategoryReplacement>
                        <tns:categoryName>Nachrichten</tns:categoryName>
                        <tns:ifTitleContains>Update</tns:ifTitleContains>
                    </tns:manualCategoryReplacement>
                    <tns:manualCategoryReplacement>
                        <tns:categoryName>Archiv 2023</tns:categoryName>
                        <tns:ifVolumeIs>Heft 7/2023</tns:ifVolumeIs>
                    </tns:manualCategoryReplacement>
                </tns:emptyCategoryReplacements>
                """));
        final XmlConfigurationReader reader = new XmlConfigurationReader(settings);
        reader.getFinalSites();
        assertEquals("Nachrichten", reader.getCategoryReplacementIfAvailable(null, "Gro&sses Update", "Heft 7/2023"));
        assertEquals("Archiv 2023", reader.getCategoryReplacementIfAvailable(null, "Unbekannt", "Heft 7/2023"));
        assertEquals("FallbackKategorie", reader.getCategoryReplacementIfAvailable("Eigene", "Unbekannt", "Heft 9/2023"));
    }

    @Test
    void categoryReplacementWithoutConfigurationKeepsOriginalCategory() {
        final XmlConfigurationReader reader = new XmlConfigurationReader(settings);
        assertEquals("EigeneKategorie", reader.getCategoryReplacementIfAvailable("EigeneKategorie", "Titel", "Heft"));
    }

    @Test
    void volumeReplacementReplacesOnlyExactMatches() throws Exception {
        pointSettingsTo(writeConfig("""
                <tns:xpath>
                    <tns:expressionWithReplaces>//html:ul[@id='links']/html:li[{0}]/html:a/@href</tns:expressionWithReplaces>
                    <tns:strategy xsi:type="tns:numberStrategy">
                        <tns:from>1</tns:from>
                        <tns:to>1</tns:to>
                    </tns:strategy>
                </tns:xpath>
                """, "", """
                     <tns:manualVolumeReplacement>
                    <tns:VolumeName>Elektor Magazin 7/2023</tns:VolumeName>
                    <tns:ifVolumeEquals>7/2023</tns:ifVolumeEquals>
                </tns:manualVolumeReplacement>
                """));
        final XmlConfigurationReader reader = new XmlConfigurationReader(settings);
        reader.getFinalSites();
        assertEquals("Elektor Magazin 7/2023", reader.getVolumeReplacement("7/2023"));
        assertEquals("8/2023", reader.getVolumeReplacement("8/2023"));
    }

}
