package de.ccw.toccer.backend.xpath;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import net.sf.saxon.lib.FeatureKeys;
import net.sf.saxon.s9api.DocumentBuilder;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XPathCompiler;
import net.sf.saxon.s9api.XPathExecutable;
import net.sf.saxon.s9api.XPathSelector;
import net.sf.saxon.s9api.XdmItem;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.s9api.XdmSequenceIterator;

public final class XPathResolver {

	private XPathResolver() {
	}

	public static List<String> resolveXPath(Processor processor, StreamSource streamsrc, String xpath) {
		System.out.println("Resolving expression \"" + xpath + "\"");
		try {

			final XPathCompiler compiler = processor.newXPathCompiler();
			compiler.declareNamespace("html", "http://www.w3.org/1999/xhtml");
			final XPathExecutable executable = compiler.compile(xpath);
			final XPathSelector selector = executable.load();

			final DocumentBuilder documentbuilder = processor.newDocumentBuilder();
			final XdmNode src = documentbuilder.build(streamsrc);
			selector.setContextItem(src);

			final List<String> result = new ArrayList<>();
			final XdmSequenceIterator iterator = selector.evaluate().iterator();
			while (iterator.hasNext()) {
				final XdmItem item = iterator.next();
				if (item.getStringValue() != null) {
					result.add(StringUtils.normalizeSpace(
							StringEscapeUtils.escapeXml10(stripNonValidXMLCharacters(item.getStringValue()))));
				}
			}

			streamsrc.getInputStream().reset();
			return result;

		} catch (SaxonApiException | IOException e) {
			throw new IllegalStateException(e);
		}
	}

	public static String stripNonValidXMLCharacters(String in) {
		StringBuffer out = new StringBuffer(); // Used to hold the output.
		char current; // Used to reference the current character.

		if (in == null || ("".equals(in)))
			return ""; // vacancy test.
		for (int i = 0; i < in.length(); i++) {
			current = in.charAt(i); // NOTE: No IndexOutOfBoundsException caught
									// here; it should not happen.
			if ((current == 0x9) || (current == 0xA) || (current == 0xD) || ((current >= 0x20) && (current <= 0xD7FF))
					|| ((current >= 0xE000) && (current <= 0xFFFD)) || ((current >= 0x10000) && (current <= 0x10FFFF)))
				out.append(current);
		}
		return out.toString();
	}

	public static Processor getProcessor() {
		final Processor processor = new Processor(false);
		processor.setConfigurationProperty(FeatureKeys.SOURCE_PARSER_CLASS, "org.ccil.cowan.tagsoup.Parser");
		return processor;
	}

	public static StreamSource getSource(String site) {
		System.out.println("Resolving site \"" + site + "\"");

		try {
			final URL url = new URL(site);
			final StreamSource streamsrc = new StreamSource(
					new BufferedInputStream(new ReadFromHtmlInputStream(url.openStream())));
			streamsrc.getInputStream().mark(Integer.MAX_VALUE);
			streamsrc.setSystemId(url.toExternalForm());
			return streamsrc;
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}
}
