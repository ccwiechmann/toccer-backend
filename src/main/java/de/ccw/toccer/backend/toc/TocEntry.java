package de.ccw.toccer.backend.toc;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

public class TocEntry implements Comparable<TocEntry> {

	private final int id;

	private String title;
	private String author;
	private String page;
	private String volume;
	private String category;

	public TocEntry(int id) {
		this.id = id;
	}

	public String getTitleForXml() {
		return escape(title);
	}

	public void setTitle(String title) {
		this.title = unescape(title);
	}

	public String getAuthorForXml() {
		return escape(author);
	}

	public void setAuthor(String author) {
		this.author = unescape(author);
	}

	public String getPageForXml() {
		return escape(page);
	}

	public void setPage(String page) {
		this.page = unescape(page).toUpperCase();
	}

	public String getVolumeForXml() {
		return escape(volume);
	}

	public void setVolume(String volume) {
		this.volume = unescape(volume);
	}

	public String getCategoryForXml() {
		return escape(category);
	}

	public void setCategory(String category) {
		this.category = unescape(category).toUpperCase();
	}

	public Integer getId() {
		return id;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder(100);
		builder.append(title).append('|').append(author).append('|').append(page).append('|').append(volume).append('|')
				.append(category);
		return builder.toString();
	}

	@Override
	public int compareTo(TocEntry o) {
		if (this.volume != null && o.volume != null && compareVolumes(o) != 0) {
			return compareVolumes(o);
		} else if (this.page == null && o.page != null) {
			return 1;
		} else if (this.page != null && o.page == null) {
			return -1;
		} else if (this.page == null && o.page == null) {
			return 0;
		} else if (StringUtils.isNumeric(this.page) && !StringUtils.isNumeric(o.page)) {
			return -1;
		} else if (!StringUtils.isNumeric(this.page) && StringUtils.isNumeric(o.page)) {
			return 1;
		} else if (StringUtils.isNumeric(this.page) && StringUtils.isNumeric(o.page)) {
			return Integer.valueOf(this.page).compareTo(Integer.valueOf(o.page));
		} else {
			return this.page.compareTo(o.page);
		}
	}

	private int compareVolumes(TocEntry o) {
		String volume1 = this.volume;
		String volume2 = o.volume;

		if (!StringUtils.isNumeric(volume1) && volume1.contains("-")) {
			volume1 = StringUtils.substringBefore(volume1, "-");
		}
		if (!StringUtils.isNumeric(volume2) && volume2.contains("-")) {
			volume2 = StringUtils.substringBefore(volume2, "-");
		}
		if (StringUtils.isNumeric(volume1) && StringUtils.isNumeric(volume2)) {
			return Integer.valueOf(volume1).compareTo(Integer.valueOf(volume2));
		}
		return volume1.compareTo(volume2);
	}

	private String unescape(String input) {
		// triple unescape since value in HTML might already be encoded
		return StringEscapeUtils.unescapeHtml4(StringEscapeUtils.unescapeHtml4(StringEscapeUtils.unescapeHtml4(input)))
				.trim();
	}

	private String escape(String input) {
		return StringEscapeUtils.escapeXml11(input);
	}
}
