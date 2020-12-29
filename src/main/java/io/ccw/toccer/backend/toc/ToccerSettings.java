package io.ccw.toccer.backend.toc;

import com.beust.jcommander.Parameter;

import io.ccw.toccer.backend.generated.XPathExpression;

public class ToccerSettings {

	@Parameter(names = { "-h", "--help" }, help = true)
	private boolean help;

	@Parameter(names = { "-i", "--input" }, description = "input template", required = true)
	private String inputTemplate;

	@Parameter(names = { "-o", "--output" }, description = "output file", required = true)
	private String outputFile;

	private XPathExpression authorXpath;
	private XPathExpression pageXpath;
	private XPathExpression titleXpath;
	private XPathExpression volumeXpath;
	private XPathExpression categoryXpath;

	private boolean sortCategories;

	public boolean isHelp() {
		return help;
	}

	public String getInputTemplate() {
		return inputTemplate;
	}

	public String getOutputFile() {
		return outputFile;
	}

	public XPathExpression getAuthorXpath() {
		return authorXpath;
	}

	public void setAuthorXpath(XPathExpression authorXpath) {
		if (this.authorXpath == null) {
			this.authorXpath = authorXpath;
		}
	}

	public XPathExpression getPageXpath() {
		return pageXpath;
	}

	public void setPageXpath(XPathExpression pageXpath) {
		if (this.pageXpath == null) {
			this.pageXpath = pageXpath;
		}
	}

	public XPathExpression getTitleXpath() {
		return titleXpath;
	}

	public void setTitleXpath(XPathExpression titleXpath) {
		if (this.titleXpath == null) {
			this.titleXpath = titleXpath;
		}
	}

	public XPathExpression getVolumeXpath() {
		return volumeXpath;
	}

	public void setVolumeXpath(XPathExpression volumeXpath) {
		if (this.volumeXpath == null) {
			this.volumeXpath = volumeXpath;
		}
	}

	public XPathExpression getCategoryXpath() {
		return categoryXpath;
	}

	public void setCategoryXpath(XPathExpression categoryXpath) {
		if (this.categoryXpath == null) {
			this.categoryXpath = categoryXpath;
		}
	}

	public boolean isSortCategories() {
		return sortCategories;
	}

	public void setSortCategories(boolean sortCategories) {
		this.sortCategories = sortCategories;
	}
}
