package de.ccw.toccer.backend.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

import de.ccw.toccer.backend.toc.ToccerSettings;
import de.ccw.toccer.backend.xpath.XPathResolver;
import de.ccw.toccer.generated.InputSchema;
import de.ccw.toccer.generated.MultiDataOnOnePageCountStrategy;
import de.ccw.toccer.generated.Xpath;
import net.sf.saxon.s9api.Processor;

public class XmlConfigurationReader {

	private final ToccerSettings settings;
	private String baseHtml;
	private String urlSuffix;
	private MultiDataOnOnePageCountStrategy multiDataOnOnePageCountStrategy;

	public XmlConfigurationReader(ToccerSettings settings) {
		this.settings = settings;
	}

	public List<String> getFinalSites() {
		final List<String> results = new ArrayList<>();

		try {
			final FileInputStream xmlContent = new FileInputStream(settings.getInputTemplate());
			final JAXBContext context = JAXBContext.newInstance(InputSchema.class);
			final Unmarshaller unmarshaller = context.createUnmarshaller();

			final Schema schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
					.newSchema(new File("src/main/resources/inputSchema.xsd"));
			unmarshaller.setSchema(schema);

			final InputSchema configuration = (InputSchema) unmarshaller.unmarshal(xmlContent);

			settings.setAuthorXpath(configuration.getAuthorXpath());
			settings.setCategoryXpath(configuration.getCategoryXpath());
			settings.setPageXpath(configuration.getPageXpath());
			settings.setTitleXpath(configuration.getTitleXpath());
			settings.setVolumeXpath(configuration.getVolumeXpath());
			baseHtml = configuration.getBaseUrl();
			urlSuffix = configuration.getUrlSuffix();
			settings.setSortCategories(configuration.getSortCategories());
			if (urlSuffix == null) {
				urlSuffix = "";
			}

			results.addAll(parseDefinedXpathReplacements(configuration.getXpath(),
					configuration.getStartingHtmlPage() + urlSuffix));
		} catch (JAXBException | SAXException | NumberFormatException | XPathExpressionException | IOException e) {
			throw new IllegalStateException(e);
		}

		return results;
	}

	private List<String> parseDefinedXpathReplacements(Xpath xpath, String site)
			throws NumberFormatException, XPathExpressionException, IOException {

		List<String> results = new ArrayList<>();
		if (xpath.getNumberStrategy() != null) {
			results = handleXpathNumberStrategy(xpath);
		} else if (xpath.getCountStrategy() != null) {
			results = handleXpathCountStrategy(xpath, site);
		} else if (xpath.getMultiDataOnOnePageCountStrategy() != null) {
			multiDataOnOnePageCountStrategy = xpath.getMultiDataOnOnePageCountStrategy();
			return Arrays.asList(site);
		}

		final Processor processor = XPathResolver.getProcessor();
		final StreamSource source = XPathResolver.getSource(site);

		final List<String> resultingPages = new ArrayList<>();
		for (final String result : results) {
			final List<String> partialResults = XPathResolver.resolveXPath(processor, source, result);
			final List<String> partialResultCompleted = new ArrayList<>();
			for (final String part : partialResults) {
				partialResultCompleted.add(baseHtml + part + urlSuffix);
			}
			resultingPages.addAll(partialResultCompleted);
		}

		if (xpath.getNext() == null) {
			return resultingPages;
		} else {
			final List<String> leafPages = new ArrayList<>();
			for (final String page : resultingPages) {
				leafPages.addAll(parseDefinedXpathReplacements(xpath.getNext(), page));
			}
			return leafPages;
		}
	}

	private List<String> handleXpathNumberStrategy(Xpath xpath) {
		final List<String> results = new ArrayList<>();

		final String expression = xpath.getExpressionWithReplaces();
		for (int i = xpath.getNumberStrategy().getFrom(); i <= xpath.getNumberStrategy().getTo(); i++) {
			results.add(expression.replace("{0}", Integer.toString(i)));
		}

		return results;
	}

	private List<String> handleXpathCountStrategy(Xpath xpath, String site)
			throws NumberFormatException, XPathExpressionException, IOException {
		final List<String> results = new ArrayList<>();
		final Processor processor = XPathResolver.getProcessor();
		final StreamSource source = XPathResolver.getSource(site);

		final String expression = xpath.getExpressionWithReplaces();
		final int count = Integer
				.valueOf(XPathResolver.resolveXPath(processor, source, xpath.getCountStrategy().getXpath()).get(0));
		for (int i = 1; i <= count; i++) {
			results.add(expression.replace("{0}", Integer.toString(i)));
		}

		return results;
	}

	public MultiDataOnOnePageCountStrategy getMultiDataOnOnePageCountStrategy() {
		return multiDataOnOnePageCountStrategy;
	}
}
