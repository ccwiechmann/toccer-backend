package de.ccw.toccer.backend.toc;

import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import de.ccw.toccer.backend.configuration.XmlConfigurationReader;
import de.ccw.toccer.backend.configuration.XmlConfigurationReader.XpathResolvableResult;
import de.ccw.toccer.backend.odt.OdtExporter;
import de.ccw.toccer.backend.xpath.XPathResolver;
import net.sf.saxon.s9api.Processor;

public class Toc {

	private final ToccerSettings settings;
	private List<XpathResolvableResult> finalPages;
	private List<TocEntry> entries;
	private XmlConfigurationReader reader;

	private Toc(ToccerSettings settings) {
		this.settings = settings;
	}

	public static Toc create(ToccerSettings settings) {
		final Toc toc = new Toc(settings);
		return toc;
	}

	public Toc loadFinalSites() {
		if (settings == null) {
			throw new IllegalStateException();
		}
		reader = new XmlConfigurationReader(settings);
		this.finalPages = reader.getFinalSites();
		return this;
	}

	public Toc generateTocEntries() {
		if (finalPages == null) {
			throw new IllegalStateException();
		}

		int currentEntryNo = 0;
		final List<TocEntry> newEntries = new ArrayList<>();
		for (final XpathResolvableResult page : finalPages) {
			final Processor processor = XPathResolver.getProcessor();
			final StreamSource source;

			if (reader.getFixedPostUrlStrategy() == null) {
				source = XPathResolver.getSource(page.getResolvableResult());
			} else {
				source = XPathResolver.getSource(page.getResolvableResult(), reader.getFixedPostUrlStrategy().isJson(),
						page.getHeaders(), reader.getFixedPostUrlStrategy().isPost(), page.getContent());
			}

			final List<String> volumes = new ArrayList<>();
			final List<String> categories = new ArrayList<>();

			final List<String> authors = new ArrayList<>();
			final List<String> titles = new ArrayList<>();
			final List<String> pages = new ArrayList<>();
			if (reader.getMultiDataOnOnePageCountStrategy() == null) {
				authors.addAll(XPathResolver.resolveXPathNoEmptyResult(processor, source, settings.getAuthorXpath()));
				titles.addAll(XPathResolver.resolveXPathNoEmptyResult(processor, source, settings.getTitleXpath()));
				if (settings.getPageXpath() != null) {
					pages.addAll(XPathResolver.resolveXPathNoEmptyResult(processor, source, settings.getPageXpath()));
				}
				if (settings.getVolumeXpath() != null) {
					volumes.addAll(XPathResolver.resolveXPathNoEmptyResult(processor, source, settings.getVolumeXpath()));
				}
				categories.addAll(XPathResolver.resolveXPathNoEmptyResult(processor, source, settings.getCategoryXpath()));
			} else {
				final int count = Integer.parseInt(XPathResolver
						.resolveXPath(processor, source, reader.getMultiDataOnOnePageCountStrategy().getMaxXpath())
						.get(0));
				final String volume;
				if (settings.getVolumeXpath() == null) {
					volume = null;
				} else if (!settings.getVolumeXpath().contains("{0}")) {
					volume = XPathResolver.resolveXPath(processor, source, settings.getVolumeXpath()).get(0);
				} else {
					volume = null;
				}
				String category = null;

				if (!reader.getMultiDataOnOnePageCountStrategy().isCategoryForEachEntry()) {
					category = XPathResolver.resolveXPath(processor, source, settings.getCategoryXpath()).get(0);
				}
				for (int i = 1; i <= count; i++) {
					final String titleXpath = settings.getTitleXpath().replace("{0}", Integer.toString(i));
					titles.add(XPathResolver.resolveXPath(processor, source, titleXpath).get(0));
					final String authorXpath = settings.getAuthorXpath().replace("{0}", Integer.toString(i));
					authors.add(XPathResolver.resolveXPath(processor, source, authorXpath).get(0));
					if (settings.getPageXpath() != null) {
						final String pageXpath = settings.getPageXpath().replace("{0}", Integer.toString(i));
						pages.add(XPathResolver.resolveXPath(processor, source, pageXpath).get(0));
					}
					if (settings.getVolumeXpath() != null) {
						if (!settings.getVolumeXpath().contains("{0}")) {
							volumes.add(volume);
						} else {
							final String volumeXpath = settings.getVolumeXpath().replace("{0}", Integer.toString(i));
							volumes.add(XPathResolver.resolveXPath(processor, source, volumeXpath).get(0));
						}
					}
					if (reader.getMultiDataOnOnePageCountStrategy().isCategoryForEachEntry()) {
						category = XPathResolver.resolveXPath(processor, source,
								settings.getCategoryXpath().replace("{0}", Integer.toString(i))).get(0);
					}
					categories.add(category);
				}
			}

			if (authors.size() != titles.size() || (authors.size() != pages.size() && settings.getPageXpath() != null)
					|| (authors.size() != volumes.size() && settings.getVolumeXpath() != null)
					|| authors.size() != categories.size()) {
				throw new IllegalStateException();
			}

			for (int i = 0; i < volumes.size(); i++) {
				volumes.set(i, reader.getVolumeReplacement(volumes.get(i)));
			}

			for (int i = 0; i < titles.size(); i++) {
				final String category = categories.get(i);
				if (StringUtils.isEmpty(category)) {
					final String title = titles.get(i);
					final String volume = volumes.get(i);
					categories.set(i, reader.getCategoryReplacementIfAvailable(category, title, volume));
				}
			}

			for (int i = 0; i < authors.size(); i++) {
				final TocEntry entry = new TocEntry(currentEntryNo);
				currentEntryNo++;
				entry.setAuthor(authors.get(i));
				entry.setTitle(titles.get(i));
				if (settings.getPageXpath() != null) {
					entry.setPage(pages.get(i));
				}
				if (settings.getVolumeXpath() != null) {
					entry.setVolume(volumes.get(i));
				}
				entry.setCategory(categories.get(i));
				newEntries.add(entry);
				System.err.println(entry);
			}
		}
		this.entries = newEntries;

		return this;
	}

	public Toc exportToOdt() {
		final Multimap<String, TocEntry> entriesByCategory = ArrayListMultimap.create();
		for (final TocEntry entry : entries) {
			final String category = entry.getCategoryForXml();
			if (reader.getMultiDataOnOnePageCountStrategy() == null
					|| reader.getMultiDataOnOnePageCountStrategy().isCategoryForEachEntry()) {
				entriesByCategory.put(category, entry);
			} else {
				if (entriesByCategory.containsKey(category) && entriesByCategory.get(category).iterator().next()
						.getVolumeForXml() != entry.getVolumeForXml()) {
					entriesByCategory.put(category + entry.getVolumeForXml(), entry);
					continue;
				}

				entriesByCategory.put(category, entry);
			}
		}
		new OdtExporter(settings.getOutputFile(), entriesByCategory, settings.isSortCategories()).export();
		return this;
	}
}
