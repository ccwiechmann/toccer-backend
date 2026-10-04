package io.ccw.toccer.backend.xpath;

import java.util.Collections;
import java.util.List;

/**
 * Immutable description of a page to fetch.
 *
 * <p>
 * The headers are stored as raw {@code "Name: value"} strings exactly as they
 * appear in the configuration files, so that this class stays free of any HTTP
 * client implementation.
 * </p>
 */
public final class PageRequest {

    private final String url;
    private final boolean post;
    private final String content;
    private final List<String> headers;

    /**
     * Creates a page request.
     *
     * @param url the absolute URL
     * @param post whether to send an HTTP POST request
     * @param content the request body for a POST request, may be {@code null}
     * @param headers raw header strings ({@code "Name: value"}), may be
     *            {@code null}
     */
    public PageRequest(String url, boolean post, String content, List<String> headers) {
        this.url = url;
        this.post = post;
        this.content = content;
        this.headers = headers == null ? Collections.emptyList() : headers;
    }

    /**
     * @return the absolute URL to fetch
     */
    public String getUrl() {
        return url;
    }

    /**
     * @return whether the request is a POST
     */
    public boolean isPost() {
        return post;
    }

    /**
     * @return the request body, may be {@code null}
     */
    public String getContent() {
        return content;
    }

    /**
     * @return raw header strings ({@code "Name: value"}), never
     *         {@code null}
     */
    public List<String> getHeaders() {
        return headers;
    }

    @Override
    public String toString() {
        return "PageRequest[" + (post ? "POST" : "GET") + " " + url + "]";
    }
}
