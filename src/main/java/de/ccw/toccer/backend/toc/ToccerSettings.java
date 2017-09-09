package de.ccw.toccer.backend.toc;

import com.beust.jcommander.Parameter;

public class ToccerSettings {

	@Parameter(names = { "-h", "--help" }, help = true)
	private boolean help;

	@Parameter(names = { "-i", "--input" }, description = "input template", required = true)
	private String inputTemplate;

	@Parameter(names = { "-o", "--output" }, description = "output file", required = true)
	private String outputFile;

	private String authorXpath;
	private String pageXpath;
	private String titleXpath;
	private String volumeXpath;
	private String categoryXpath;

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

	public String getAuthorXpath() {
		return authorXpath;
	}

	public void setAuthorXpath(String authorXpath) {
		if (this.authorXpath == null) {
			this.authorXpath = authorXpath;
		}
	}

	public String getPageXpath() {
		return pageXpath;
	}

	public void setPageXpath(String pageXpath) {
		if (this.pageXpath == null) {
			this.pageXpath = pageXpath;
		}
	}

	public String getTitleXpath() {
		return titleXpath;
	}

	public void setTitleXpath(String titleXpath) {
		if (this.titleXpath == null) {
			this.titleXpath = titleXpath;
		}
	}

	public String getVolumeXpath() {
		return volumeXpath;
	}

	public void setVolumeXpath(String volumeXpath) {
		if (this.volumeXpath == null) {
			this.volumeXpath = volumeXpath;
		}
	}

	public String getCategoryXpath() {
		return categoryXpath;
	}

	public void setCategoryXpath(String categoryXpath) {
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
