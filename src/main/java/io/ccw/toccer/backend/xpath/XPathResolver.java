package io.ccw.toccer.backend.xpath;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

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

	public static List<String> resolveXPathNoEmptyResult(Processor processor, StreamSource streamsrc, String xpath) {
		final List<String> result = resolveXPath(processor, streamsrc, xpath);
		if (result.isEmpty()) {
			result.add("");
		}
		return result;
	}

	public static List<String> resolveXPath(Processor processor, StreamSource streamsrc, String xpath) {
		return resolveXPath(processor, streamsrc, xpath, true);
	}

	public static List<String> resolveXPath(Processor processor, StreamSource streamsrc, String xpath,
			boolean escapeResults) {
		System.out.println("Resolving expression \"" + xpath + "\"");
		try {

			final XPathCompiler compiler = processor.newXPathCompiler();
			compiler.setCaching(false);
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
				if (StringUtils.isNotEmpty(item.getStringValue())) {
					if (escapeResults) {
						result.add(StringUtils.normalizeSpace(StringEscapeUtils
								.escapeHtml4(stripNonValidXMLCharacters(new String(item.getStringValue())))));
					} else {
						result.add(StringUtils.normalizeSpace(new String(item.getStringValue())));
					}
				} else {
					result.add("");
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
		return getSource(site, false, new ArrayList<String>(), false, null);
	}

	public static StreamSource getSource(String site, boolean isJson, List<String> headers, boolean isPost,
			String content) {
		System.out.println("Resolving site \"" + site + "\"");

		CloseableHttpResponse response = null;
		try {

			final List<Header> headerList = new ArrayList<>();
			if (headers != null) {
				for (final String header : headers) {
					final String[] split = header.split(":", 2);
					headerList.add(new BasicHeader(StringUtils.trim(split[0]), StringUtils.trim(split[1])));
				}
			}
			CloseableHttpClient httpclient = HttpClients.custom().setDefaultHeaders(headerList).build();

			final URL url = new URL(site);
			HttpRequestBase request = isPost ? new HttpPost(site) : new HttpGet(site);

			if (StringUtils.isNotBlank(content)) {
				((HttpPost) request).setEntity(new StringEntity(content, ContentType.APPLICATION_JSON));
			}

			response = httpclient.execute(request);

			final HttpEntity entity = response.getEntity();
			final String result = EntityUtils.toString(entity);
			EntityUtils.consume(entity);

			final StreamSource streamsrc = new StreamSource(new BufferedInputStream(
					new ReadFromHtmlInputStream(new ByteArrayInputStream(result.getBytes()), isJson)));
			streamsrc.getInputStream().mark(Integer.MAX_VALUE);
			streamsrc.setSystemId(url.toExternalForm());

			if (isJson) {
				final String json = IOUtils.toString(streamsrc.getInputStream());
				final String convertedJson = JsonToHtmlConverter.getHtmlData(json);
				return new StreamSource(new ByteArrayInputStream(convertedJson.getBytes()));
			}
			return streamsrc;

		} catch (IOException e) {
			throw new IllegalStateException(e);
		} finally {
			try {
				response.close();
			} catch (IOException e) {
				throw new IllegalStateException(e);
			}
		}
	}
}
