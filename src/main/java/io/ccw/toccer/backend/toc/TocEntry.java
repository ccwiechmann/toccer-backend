package io.ccw.toccer.backend.toc;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * A single entry of the generated table of contents.
 *
 * <p>Raw (HTML) values are stored unescaped; the {@code *ForXml} accessors
 * return the XML-escaped form used when exporting entries to an ODT document.
 * {@code page} and {@code category} are normalized to upper case.
 */
public class TocEntry implements Comparable<TocEntry> {

    private final int id;

    private String title;
    private String author;
    private String page;
    private String volume;
    private String category;

    /**
     * Creates an entry with the given id (zero-based within the final TOC).
     *
     * @param id unique position within the TOC, zero-based
     */
    public TocEntry(int id) {
        this.id = id;
    }

    /**
     * @return the title, XML-escaped
     */
    public String getTitleForXml() {
        return escape(title);
    }

    /**
     * @param title value to store (is unescaped and may contain HTML)
     */
    public void setTitle(String title) {
        this.title = unescape(title);
    }

    /**
     * @return the author, XML-escaped
     */
    public String getAuthorForXml() {
        return escape(author);
    }

    /**
     * @param author value to store (is unescaped and may contain HTML)
     */
    public void setAuthor(String author) {
        this.author = unescape(author);
    }

    /**
     * @return the page, XML-escaped and upper-cased
     */
    public String getPageForXml() {
        return escape(page);
    }

    /**
     * @param page value to store (upper-cased after unescaping)
     */
    public void setPage(String page) {
        this.page = unescape(page).toUpperCase();
    }

    /**
     * @return the volume, XML-escaped
     */
    public String getVolumeForXml() {
        return escape(volume);
    }

    /**
     * @param volume value to store (is unescaped)
     */
    public void setVolume(String volume) {
        this.volume = unescape(volume);
    }

    /**
     * @return the category, XML-escaped and upper-cased
     */
    public String getCategoryForXml() {
        return escape(category);
    }

    /**
     * @param category value to store (upper-cased after unescaping)
     */
    public void setCategory(String category) {
        this.category = unescape(category).toUpperCase();
    }

    /**
     * @return the unique id within the TOC
     */
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
        // Unescape three times: values coming from the source HTML can already be
        // HTML-entity encoded (even doubly), so a single pass would not be enough.
        return StringEscapeUtils.unescapeHtml4(StringEscapeUtils.unescapeHtml4(StringEscapeUtils.unescapeHtml4(input)))
                .trim();
    }

    private String escape(String input) {
        return StringEscapeUtils.escapeXml11(input);
    }
}
