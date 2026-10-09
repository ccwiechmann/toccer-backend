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
    void compareToTotalOrderUsesTitleThenId() {
        // Same volume ("2") and page ("10") but different titles/ids:
        // before the fix these two compared as equal -> non-deterministic sort.
        final TocEntry b = new TocEntry(1);
        b.setVolume("2");
        b.setPage("10");
        b.setTitle("Bravo");
        final TocEntry a = new TocEntry(0);
        a.setVolume("2");
        a.setPage("10");
        a.setTitle("Alpha");
        assertTrue(a.compareTo(b) < 0, "title breaks a volume+page tie (Alpha < Bravo)");
        assertTrue(b.compareTo(a) > 0, "symmetric: Bravo > Alpha");
        final TocEntry a2 = new TocEntry(5);
        a2.setVolume("2");
        a2.setPage("10");
        a2.setTitle("Alpha");
        assertTrue(a.compareTo(a2) < 0, "id breaks an identical volume+page+title tie");
    }

    @Test
    void compareToFallsThroughToPageWhenOneVolumeIsNull() {
        // A null volume is ignored (comparator skips it), so the page decides.
        // This is the intended "fall through" behaviour and must stay antisymmetric
        // no matter which entry carries the null.
        final TocEntry knownVolumeHigherPage = new TocEntry(0);
        knownVolumeHigherPage.setVolume("5");
        knownVolumeHigherPage.setPage("7");
        final TocEntry noVolumeLowerPage = new TocEntry(1);
        noVolumeLowerPage.setPage("3");
        // Both non-null pages, so the page (not the volume) drives the order.
        assertTrue(noVolumeLowerPage.compareTo(knownVolumeHigherPage) < 0, "page 3 before page 7");
        assertTrue(knownVolumeHigherPage.compareTo(noVolumeLowerPage) > 0, "antisymmetric: page 7 after page 3");
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
