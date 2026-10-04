package io.ccw.toccer.backend.xpath;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

class PageRequestTest {

	@Test
	void gettersReturnProvidedValues() {
		final PageRequest request = new PageRequest("http://example.org", true, "body", List.of("Accept: text/html"));
		assertEquals("http://example.org", request.getUrl());
		assertTrue(request.isPost());
		assertEquals("body", request.getContent());
		assertEquals(List.of("Accept: text/html"), request.getHeaders());
	}

	@Test
	void nullHeadersYieldEmptyList() {
		final PageRequest request = new PageRequest("http://example.org", false, null, null);
		assertNotNull(request.getHeaders());
		assertTrue(request.getHeaders().isEmpty());
	}

	@Test
	void contentMayBeNullForGet() {
		final PageRequest request = new PageRequest("http://example.org", false, null, null);
		assertFalse(request.isPost());
		assertNull(request.getContent());
	}

	@Test
	void toStringReflectsMethodAndUrl() {
		assertEquals("PageRequest[GET http://example.org]", new PageRequest("http://example.org", false, null, null).toString());
		assertEquals("PageRequest[POST http://example.org]", new PageRequest("http://example.org", true, null, null).toString());
	}
}
