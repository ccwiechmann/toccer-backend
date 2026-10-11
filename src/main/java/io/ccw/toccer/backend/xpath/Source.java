package io.ccw.toccer.backend.xpath;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import javax.xml.transform.stream.StreamSource;

import io.ccw.toccer.backend.ToccerException;

import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XdmNode;

/**
 * An immutable, already-decoded HTML (or JSON converted to HTML) document that is
 * ready to be evaluated with XPath.
 *
 * <p>
 * A {@link Source} can be handed out as a fresh {@link StreamSource} any number
 * of times. This removes the old {@code mark}/{@code reset} book-keeping that
 * callers had to do after evaluating one XPath against a single-use stream:
 * every call to {@link #toStreamSource()} starts from the beginning of the
 * document again.
 * </p>
 *
 * <p>
 * The parsed Saxon node for a given {@link Processor} is lazily built and
 * cached. Since a {@link Source} is immutable, this is thread-safe to use
 * from multiple calls to {@link #getNode(Processor)}.
 * </p>
 */
public final class Source {

    private final String text;
    private final String systemId;

    // Lazily built, per-Processor Saxon document cache.
    private volatile Processor cachedProcessor;
    private volatile XdmNode cachedNode;

    private Source(String text, String systemId) {
        this.text = text;
        this.systemId = systemId;
    }

    /**
     * Creates a source from an in-memory document.
     *
     * @param text the decoded document, never {@code null}
     * @param systemId a URL or file path used for error reporting, may be
     *            {@code null}
     * @return a new {@link Source}
     */
    public static Source of(String text, String systemId) {
        return new Source(text, systemId);
    }

    /**
     * Returns a fresh, read-once {@link StreamSource} that starts at the
     * beginning of the document.
     *
     * @return a read-once {@link StreamSource}
     */
    public StreamSource toStreamSource() {
        final byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        final StreamSource source = new StreamSource(new ByteArrayInputStream(bytes));
        if (systemId != null) {
            source.setSystemId(systemId);
        }
        return source;
    }

    /**
     * Returns the Saxon {@link XdmNode} for this document, parsed through the
     * given {@code processor}. The result is cached per {@link Processor}: a
     * second call with the same processor returns the already-built node, so
     * evaluating many XPaths against the same document only parses it once.
     *
     * @param processor the Saxon {@link Processor} to parse with
     * @return a Saxon node for this document
     */
    public XdmNode getNode(final Processor processor) {
        final XdmNode local = cachedNode;
        if (local != null && cachedProcessor == processor) {
            return local;
        }
        synchronized (this) {
            if (cachedNode != null && cachedProcessor == processor) {
                return cachedNode;
            }
            try {
                cachedNode = processor.newDocumentBuilder().build(toStreamSource());
            } catch (SaxonApiException e) {
                throw new ToccerException("Failed to parse document from " + systemId, e);
            }
            cachedProcessor = processor;
            return cachedNode;
        }
    }

    /**
     * Returns the decoded document text.
     *
     * @return the document text, never {@code null}
     */
    public String getText() {
        return text;
    }

    /**
     * Returns the system id, i.e. the URL this document belongs to.
     *
     * @return the system id, or {@code null} if not available
     */
    public String getSystemId() {
        return systemId;
    }

    @Override
    public String toString() {
        return "Source[" + text.length() + " chars, systemId=" + systemId + "]";
    }
}
