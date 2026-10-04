package io.ccw.toccer.backend.xpath;

/**
 * Small abstraction over "fetch the raw body of a page for the given request".
 *
 * <p>
 * The main code base uses a JDK {@code java.net.http} implementation, but this
 * indirection lets tests substitute a fake that returns canned payloads without
 * touching the network.
 * </p>
 */
@FunctionalInterface
public interface PageFetcher {

	/**
	 * Fetches the body of the requested page.
	 *
	 * @param request the page to fetch
	 * @return the decoded response body
	 * @throws Exception if the request fails (I/O, unsupported media type, ...)
	 */
	String fetch(PageRequest request) throws Exception;
}
