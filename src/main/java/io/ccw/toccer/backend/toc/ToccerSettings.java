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

	/** Returns whether the user requested help output. */
	public boolean isHelp() {
		return help;
	}

	/** Returns the path of the input configuration template. */
	public String getInputTemplate() {
		return inputTemplate;
	}

	/** Returns the path of the ODT file to generate. */
	public String getOutputFile() {
		return outputFile;
	}

	/** Returns the configured XPath expression for authors. */
	public XPathExpression getAuthorXpath() {
		return authorXpath;
	}

	/**
	 * Stores the author XPath expression.
	 *
	 * <p>
	 * The setter is a no-op once a value has been set so that later
	 * configuration entries cannot override an already resolved value.
	 */
	public void setAuthorXpath(XPathExpression authorXpath) {
		if (this.authorXpath == null) {
			this.authorXpath = authorXpath;
		}
	}

	/** Returns the configured XPath expression for pages. */
	public XPathExpression getPageXpath() {
		return pageXpath;
	}

	/** Stores the page XPath expression (no-op if already set). */
	public void setPageXpath(XPathExpression pageXpath) {
		if (this.pageXpath == null) {
			this.pageXpath = pageXpath;
		}
	}

	/** Returns the configured XPath expression for titles. */
	public XPathExpression getTitleXpath() {
		return titleXpath;
	}

	/** Stores the title XPath expression (no-op if already set). */
	public void setTitleXpath(XPathExpression titleXpath) {
		if (this.titleXpath == null) {
			this.titleXpath = titleXpath;
		}
	}

	/** Returns the configured XPath expression for volumes. */
	public XPathExpression getVolumeXpath() {
		return volumeXpath;
	}

	/** Stores the volume XPath expression (no-op if already set). */
	public void setVolumeXpath(XPathExpression volumeXpath) {
		if (this.volumeXpath == null) {
			this.volumeXpath = volumeXpath;
		}
	}

	/** Returns the configured XPath expression for categories. */
	public XPathExpression getCategoryXpath() {
		return categoryXpath;
	}

	/** Stores the category XPath expression (no-op if already set). */
	public void setCategoryXpath(XPathExpression categoryXpath) {
		if (this.categoryXpath == null) {
			this.categoryXpath = categoryXpath;
		}
	}

	/** Returns whether categories should be sorted alphabetically. */
	public boolean isSortCategories() {
		return sortCategories;
	}

	/** Stores the category sorting preference. */
	public void setSortCategories(boolean sortCategories) {
		this.sortCategories = sortCategories;
	}

	/** Returns the label shown for an issue page (e.g. "Seite"). */
	public String getLabelIssuePage() {
		return labelIssuePage;
	}

	/** Stores the issue-page label. */
	public void setLabelIssuePage(String labelIssuePage) {
		this.labelIssuePage = labelIssuePage;
	}

	/** Returns the label for the table of contents section. */
	public String getLabelToc() {
		return labelToc;
	}

	/** Stores the table-of-contents label. */
	public void setLabelToc(String labelToc) {
		this.labelToc = labelToc;
	}

	/** Returns the label shown for the name of the publication. */
	public String getLabelNameOfPublication() {
		return labelNameOfPublication;
	}

	/** Stores the publication name label. */
	public void setLabelNameOfPublication(String labelNameOfPublication) {
		this.labelNameOfPublication = labelNameOfPublication;
	}
}
