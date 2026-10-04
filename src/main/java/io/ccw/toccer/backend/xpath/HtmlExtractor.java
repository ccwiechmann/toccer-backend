package io.ccw.toccer.backend.xpath;

/**
 * Small helpers around decoded HTML documents.
 */
final class HtmlExtractor {

	private HtmlExtractor() {
	}

	/**
	 * Returns the document from the first {@code <html} tag onward, ignoring
	 * case and whitespace.
	 *
	 * <p>
	 * Some sites serve a small amount of preamble (comments, DOCTYPE, stray
	 * text) in front of the real markup. TagSoup is forgiving about this, but
	 * keeping the document anchored on {@code <html} makes the namespace
	 * declared in the XPath expressions ({@code html:http://www.w3.org/1999/xhtml})
	 * behave consistently and prevents the preamble from leaking into results.
	 * </p>
	 *
	 * <p>
	 * This is a re-implementation of the old byte-oriented
	 * {@code ReadFromHtmlInputStream} as plain string operations, which is
	 * easier to reason about and to test.
	 * </p>
	 *
	 * @param text the decoded document, may be {@code null}
	 * @return the document starting at the first {@code <html}, or the trimmed
	 *         input unchanged when no {@code <html} tag is present, or an empty
	 *         string when the input is {@code null}
	 */
	static String stripPreamble(String text) {
		if (text == null) {
			return "";
		}
		final String lower = text.toLowerCase();
		final int index = lower.indexOf("<html");
		if (index > 0) {
			return text.substring(index);
		}
		return text;
	}
}
