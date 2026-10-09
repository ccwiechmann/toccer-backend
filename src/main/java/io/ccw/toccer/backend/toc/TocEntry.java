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

    /**
     * Orders entries for stable output: volume first (when both are known),
     * then page (numeric-aware, entries without a page last), then title, then
     * the unique id. The id tiebreak makes the ordering total, so two distinct
     * entries never compare as equal and {@link java.util.Collections#sort}
     * yields a fully deterministic result.
     *
     * @param o the entry to compare against
     * @return a value &lt; 0, 0 or &gt; 0 as this entry is less than, equal to
     *         or greater than {@code o}
     */
    @Override
    public int compareTo(TocEntry o) {
        // 1) Volume (only when both entries carry one)
        if (this.volume != null && o.volume != null) {
            final int volumeComparison = compareVolumes(o);
            if (volumeComparison != 0) {
                return volumeComparison;
            }
        }
        // 2) Page (numeric-aware; an entry without a page sorts last)
        if (this.page != null || o.page != null) {
            if (this.page == null) {
                return 1;
            }
            if (o.page == null) {
                return -1;
            }
            final int pageComparison = comparePages(this.page, o.page);
            if (pageComparison != 0) {
                return pageComparison;
            }
        }
        // 3) Title (an entry without a title sorts last)
        if (this.title != null || o.title != null) {
            if (this.title == null) {
                return 1;
            }
            if (o.title == null) {
                return -1;
            }
            final int titleComparison = this.title.compareTo(o.title);
            if (titleComparison != 0) {
                return titleComparison;
            }
        }
        // 4) Id (unique within a TOC -> total order)
        return Integer.compare(this.id, o.id);
    }

    private static int comparePages(String page1, String page2) {
        final boolean numeric1 = StringUtils.isNumeric(page1);
        final boolean numeric2 = StringUtils.isNumeric(page2);
        if (numeric1 && numeric2) {
            return Integer.valueOf(page1).compareTo(Integer.valueOf(page2));
        }
        if (numeric1) {
            return -1;
        }
        if (numeric2) {
            return 1;
        }
        return page1.compareTo(page2);
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
