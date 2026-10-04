package io.ccw.toccer.backend.xpath;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class JsonToHtmlConverterTest {

	@Test
	void objectCreatesOneDivPerKey() {
		final String html = JsonToHtmlConverter.getHtmlData("{\"title\":\"T\",\"author\":\"A\"}");
		assertTrue(html.contains("<html>"));
		assertTrue(html.contains("</html>"));
		assertTrue(html.contains("class=\"title\""));
		assertTrue(html.contains("class=\"author\""));
		assertTrue(html.contains("T"));
		assertTrue(html.contains("A"));
	}

	@Test
	void arrayRendersElementsInSequence() {
		final String html = JsonToHtmlConverter.getHtmlData("{\"items\":[\"one\",\"two\"]}");
		assertTrue(html.contains("one"));
		assertTrue(html.contains("two"));
	}

	@Test
	void scalarValueIsIncluded() {
		final String html = JsonToHtmlConverter.getHtmlData("{\"value\":\"42\"}");
		assertTrue(html.contains("42"));
	}

	@Test
	void ampersandIsEscaped() {
		final String html = JsonToHtmlConverter.getHtmlData("{\"value\":\"a & b\"}");
		assertTrue(html.contains("a &amp; b"));
	}

	@Test
	void italicMarkersAreStripped() {
		final String html = JsonToHtmlConverter.getHtmlData("{\"value\":\"<italic>em</italic>phasized\"}");
		assertTrue(html.contains("emphasized"));
	}

	@Test
	void invalidJsonThrows() {
		assertThrows(IllegalArgumentException.class, () -> JsonToHtmlConverter.getHtmlData("not json at all"));
	}
}
