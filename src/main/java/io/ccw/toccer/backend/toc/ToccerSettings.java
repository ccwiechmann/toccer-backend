package io.ccw.toccer.backend.toc;

import io.ccw.toccer.backend.generated.XPathExpression;
import picocli.CommandLine.Option;

/**
 * Command line options for the Toccer tool.
 * <p>
 * The two required parameters point to the configuration XML (typically under
 * {@code src/main/resources/config/}) and to the ODT file to generate. All
 * other settings are populated from that XML file by
 * {@link io.ccw.toccer.backend.configuration.XmlConfigurationReader}.
 */
public class ToccerSettings {

    /** Show usage information and exit. */
    @Option(names = { "-h", "--help" })
    private boolean help;

    /** Path to the input configuration XML template. */
    @Option(names = { "-i", "--input" }, paramLabel = "TEMPLATE", description = "input template", required = true)
    private String inputTemplate;

    /** Path of the ODT file to generate. */
    @Option(names = { "-o", "--output" }, paramLabel = "FILE", description = "output file", required = true)
    private String outputFile;

    private XPathExpression authorXpath;
    private XPathExpression pageXpath;
    private XPathExpression titleXpath;
    private XPathExpression volumeXpath;
    private XPathExpression categoryXpath;

    private boolean sortCategories;

    private String labelIssuePage;
    private String labelToc;
    private String labelNameOfPublication;

    /**
     * @return whether the user requested help ({@code -h/--help})
     */
    public boolean isHelp() {
        return help;
    }

    /**
     * @return the path of the input configuration template (required)
     */
    public String getInputTemplate() {
        return inputTemplate;
    }

    /**
     * @return the path of the ODT file to generate (required)
     */
    public String getOutputFile() {
        return outputFile;
    }

    /**
     * @return the configured XPath expression for authors, may be
     *         {@code null}
     */
    public XPathExpression getAuthorXpath() {
        return authorXpath;
    }

    /**
     * Stores the author XPath expression.
     *
     * <p>
     * The setter is a no-op once a value has been set so that later
     * configuration entries cannot override an already resolved value.
     *
     * @param authorXpath the XPath expression to store, ignored if already set
     */
    public void setAuthorXpath(XPathExpression authorXpath) {
        if (this.authorXpath == null) {
            this.authorXpath = authorXpath;
        }
    }

    /**
     * @return the configured XPath expression for pages, may be
     *         {@code null}
     */
    public XPathExpression getPageXpath() {
        return pageXpath;
    }

    /**
     * @param pageXpath the XPath expression; no-op if one is already set
     */
    public void setPageXpath(XPathExpression pageXpath) {
        if (this.pageXpath == null) {
            this.pageXpath = pageXpath;
        }
    }

    /**
     * @return the configured XPath expression for titles, may be
     *         {@code null}
     */
    public XPathExpression getTitleXpath() {
        return titleXpath;
    }

    /**
     * @param titleXpath the XPath expression; no-op if one is already set
     */
    public void setTitleXpath(XPathExpression titleXpath) {
        if (this.titleXpath == null) {
            this.titleXpath = titleXpath;
        }
    }

    /**
     * @return the configured XPath expression for volumes, may be
     *         {@code null}
     */
    public XPathExpression getVolumeXpath() {
        return volumeXpath;
    }

    /**
     * @param volumeXpath the XPath expression; no-op if one is already set
     */
    public void setVolumeXpath(XPathExpression volumeXpath) {
        if (this.volumeXpath == null) {
            this.volumeXpath = volumeXpath;
        }
    }

    /**
     * @return the configured XPath expression for categories, may be
     *         {@code null}
     */
    public XPathExpression getCategoryXpath() {
        return categoryXpath;
    }

    /**
     * @param categoryXpath the XPath expression; no-op if one is already set
     */
    public void setCategoryXpath(XPathExpression categoryXpath) {
        if (this.categoryXpath == null) {
            this.categoryXpath = categoryXpath;
        }
    }

    /**
     * @return whether categories are sorted alphabetically in the ODT output
     */
    public boolean isSortCategories() {
        return sortCategories;
    }

    /**
     * @param sortCategories true to sort categories alphabetically
     */
    public void setSortCategories(boolean sortCategories) {
        this.sortCategories = sortCategories;
    }

    /**
     * @return the label shown for the issue page (e.g. "Seite"), may be
     *         {@code null}
     */
    public String getLabelIssuePage() {
        return labelIssuePage;
    }

    /**
     * @param labelIssuePage the issue-page label to store
     */
    public void setLabelIssuePage(String labelIssuePage) {
        this.labelIssuePage = labelIssuePage;
    }

    /**
     * @return the label of the TOC section, may be {@code null}
     */
    public String getLabelToc() {
        return labelToc;
    }

    /**
     * @param labelToc the TOC label to store
     */
    public void setLabelToc(String labelToc) {
        this.labelToc = labelToc;
    }

    /**
     * @return the label of the publication name, may be {@code null}
     */
    public String getLabelNameOfPublication() {
        return labelNameOfPublication;
    }

    /**
     * @param labelNameOfPublication the publication name label to store
     */
    public void setLabelNameOfPublication(String labelNameOfPublication) {
        this.labelNameOfPublication = labelNameOfPublication;
    }
}
