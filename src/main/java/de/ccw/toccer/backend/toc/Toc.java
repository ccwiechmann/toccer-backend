package de.ccw.toccer.backend.toc;

import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.stream.StreamSource;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import de.ccw.toccer.backend.configuration.XmlConfigurationReader;
import de.ccw.toccer.backend.odt.OdtExporter;
import de.ccw.toccer.backend.xpath.XPathResolver;
import net.sf.saxon.s9api.Processor;

public class Toc {

	private final ToccerSettings settings;
	private List<String> finalPages;
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

		final List<TocEntry> newEntries = new ArrayList<>();
		for (final String page : finalPages) {
			final Processor processor = XPathResolver.getProcessor();
			final StreamSource source = XPathResolver.getSource(page);

			final List<String> volumes = new ArrayList<>();
			final List<String> categories = new ArrayList<>();

			final List<String> authors = new ArrayList<>();
			final List<String> titles = new ArrayList<>();
			final List<String> pages = new ArrayList<>();
			if (reader.getMultiDataOnOnePageCountStrategy() == null) {
				authors.addAll(XPathResolver.resolveXPath(processor, source, settings.getAuthorXpath()));
				titles.addAll(XPathResolver.resolveXPath(processor, source, settings.getTitleXpath()));
				pages.addAll(XPathResolver.resolveXPath(processor, source, settings.getPageXpath()));
				volumes.addAll(XPathResolver.resolveXPath(processor, source, settings.getVolumeXpath()));
				categories.addAll(XPathResolver.resolveXPath(processor, source, settings.getCategoryXpath()));
			} else {
				final int count = Integer.parseInt(XPathResolver
						.resolveXPath(processor, source, reader.getMultiDataOnOnePageCountStrategy().getMaxXpath())
						.get(0));
				final String volume = XPathResolver.resolveXPath(processor, source, settings.getVolumeXpath()).get(0);
				final String category = XPathResolver.resolveXPath(processor, source, settings.getCategoryXpath())
						.get(0);
				for (int i = 1; i <= count; i++) {
					final String titleXpath = settings.getTitleXpath().replace("{0}", Integer.toString(i));
					titles.add(XPathResolver.resolveXPath(processor, source, titleXpath).get(0));
					final String authorXpath = settings.getAuthorXpath().replace("{0}", Integer.toString(i));
					authors.add(XPathResolver.resolveXPath(processor, source, authorXpath).get(0));
					final String pageXpath = settings.getPageXpath().replace("{0}", Integer.toString(i));
					pages.add(XPathResolver.resolveXPath(processor, source, pageXpath).get(0));
					volumes.add(volume);
					categories.add(category);
				}
			}

			if (authors.size() != titles.size() || authors.size() != pages.size() || authors.size() != volumes.size()
					|| authors.size() != categories.size()) {
				throw new IllegalStateException();
			}

			for (int i = 0; i < authors.size(); i++) {
				final TocEntry entry = new TocEntry();
				entry.setAuthor(authors.get(i));
				entry.setTitle(titles.get(i));
				entry.setPage(pages.get(i));
				entry.setVolume(volumes.get(i));
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
			final String category = entry.getCategory();
			if (reader.getMultiDataOnOnePageCountStrategy() == null) {
				entriesByCategory.put(category, entry);
			} else {
				if (entriesByCategory.containsKey(category)
						&& entriesByCategory.get(category).iterator().next().getVolume() != entry.getVolume()) {
					entriesByCategory.put(category + entry.getVolume(), entry);
					continue;
				}

				entriesByCategory.put(category, entry);
			}
		}
		new OdtExporter(settings.getOutputFile(), entriesByCategory, settings.isSortCategories()).export();
		return this;
	}
}
