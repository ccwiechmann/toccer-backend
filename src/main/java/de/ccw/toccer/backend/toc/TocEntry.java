package de.ccw.toccer.backend.toc;

import org.apache.commons.lang3.StringUtils;

public class TocEntry implements Comparable<TocEntry> {

	private String title;
	private String author;
	private String page;
	private String volume;
	private String category;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category.toUpperCase();
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
		final int issueCmp = Integer.valueOf(this.volume).compareTo(Integer.valueOf(o.volume));
		if (issueCmp != 0) {
			return issueCmp;
		} else if (StringUtils.isNumeric(this.page.trim()) && !StringUtils.isNumeric(o.page.trim())) {
			return -1;
		} else if (!StringUtils.isNumeric(this.page.trim()) && StringUtils.isNumeric(o.page.trim())) {
			return 1;
		} else if (StringUtils.isNumeric(this.page.trim()) && StringUtils.isNumeric(o.page.trim())) {
			return Integer.valueOf(this.page.trim()).compareTo(Integer.valueOf(o.page.trim()));
		} else {
			return this.page.compareTo(o.page);
		}
	}
}
