package io.ccw.toccer.backend.xpath;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link PageFetcher} implementation built on the JDK
 * {@link HttpClient} (replaces Apache HttpClient 4.x, which reached end of
 * life).
 *
 * <p>
 * The client is created per request, which matches the previous behaviour where
 * Apache built a fresh client on every call. A 10 minute timeout is applied so
 * a hanging host cannot block the whole run.
 * </p>
 */
public final class HttpPageFetcher implements PageFetcher {

    private static final Logger LOG = LoggerFactory.getLogger(HttpPageFetcher.class);
    private static final Duration TIMEOUT = Duration.ofMinutes(10);

    @Override
    public String fetch(PageRequest request) throws Exception {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(java.net.URI.create(request.getUrl()))
                .timeout(TIMEOUT);

        addHeaders(builder, request.getHeaders());

        if (request.isPost()) {
            final byte[] body = request.getContent() == null
                    ? new byte[0]
                    : request.getContent().getBytes(StandardCharsets.UTF_8);
            builder.POST(BodyPublishers.ofByteArray(body));
        } else {
            builder.GET();
        }

        final HttpClient client = HttpClient.newHttpClient();
        LOG.debug("Fetching {} -> {}", (request.isPost() ? "POST" : "GET"), request.getUrl());
        final HttpResponse<byte[]> response = client.send(builder.build(), BodyHandlers.ofByteArray());
        return new String(response.body(), StandardCharsets.UTF_8);
    }

    /**
     * Copies the raw {@code "Name: value"} header strings onto the request
     * builder.
     *
     * @param builder the request builder
     * @param headers the raw header strings
     */
    private static void addHeaders(HttpRequest.Builder builder, java.util.List<String> headers) {
        for (final String header : headers) {
            final int sep = header.indexOf(':');
            final String name = header.substring(0, sep).trim();
            final String value = header.substring(sep + 1).trim();
            builder.header(name, value);
        }
    }
}
