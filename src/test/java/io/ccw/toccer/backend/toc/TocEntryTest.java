package io.ccw.toccer.backend.toc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class TocEntryTest {

	@Test
	void idIsFixed() {
		assertEquals(7, new TocEntry(7).getId());
	}

	@Test
	void titleUnescapesOnSetAndEscapesOnGetForXml() {
		final TocEntry entry = new TocEntry(1);
		entry.setTitle("Bob &amp; Co");
		// unescapeHtml4 collapses the entity to a literal ampersand.
		assertEquals("Bob &amp; Co", entry.getTitleForXml());
	}

	@Test
	void pageAndCategoryAreUpperCased() {
		final TocEntry entry = new TocEntry(1);
		entry.setPage(" 12a ");
		entry.setCategory("essay");
		assertEquals("12A", entry.getPageForXml());
		assertEquals("ESSAY", entry.getCategoryForXml());
	}

	@Test
	void specialCharactersAreXmlEscapedOnGetForXml() {
		final TocEntry entry = new TocEntry(1);
		entry.setTitle("a < b");
		assertTrue(entry.getTitleForXml().contains("&lt;"));
	}

	@Test
	void compareToOrdersNumericPages() {
		final TocEntry a = new TocEntry(1);
		a.setPage("2");
		final TocEntry b = new TocEntry(1);
		b.setPage("10");
		assertTrue(a.compareTo(b) < 0);
		assertEquals(0, b.compareTo(b));
	}

	@Test
	void compareToPrefersNullPageLast() {
		final TocEntry withPage = new TocEntry(1);
		withPage.setPage("5");
		final TocEntry withoutPage = new TocEntry(1);
		assertTrue(withPage.compareTo(withoutPage) < 0);
		assertTrue(withoutPage.compareTo(withPage) > 0);
	}

	@Test
	void toStringJoinsFields() {
		final TocEntry entry = new TocEntry(1);
		entry.setTitle("T");
		entry.setAuthor("A");
		entry.setPage("P");
		entry.setVolume("V");
		entry.setCategory("C");
		assertEquals("T|A|P|V|C", entry.toString());
	}
}
