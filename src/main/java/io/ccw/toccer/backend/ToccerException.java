package io.ccw.toccer.backend;

/**
 * Domain exception for TOC pipeline failures caused by the input.
 *
 * <p>
 * Thrown by the configuration-loading and entry-generation steps when the
 * pipeline cannot continue but the cause is the data being processed rather
 * than a bug — a missing or unreadable input template, a page that could not
 * be fetched, an XPath that does not compile or returns malformed data. The
 * CLI prints {@link #getMessage()} as one clean line and exits with status
 * {@code 2}; if a cause is attached it is printed underneath in parentheses.
 * </p>
 *
 * <p>
 * Programming-state violations (e.g. running the pipeline steps out of order)
 * intentionally remain {@link IllegalStateException} instead, so the two can be
 * told apart by callers and in logs.
 * </p>
 */
public class ToccerException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a pipeline error.
     *
     * @param message a human-readable description of the failure
     */
    public ToccerException(String message) {
        super(message);
    }

    /**
     * Creates a pipeline error wrapping the underlying cause.
     *
     * @param message a human-readable description of the failure
     * @param cause the underlying error, may be {@code null}
     */
    public ToccerException(String message, Throwable cause) {
        super(message, cause);
    }
}
