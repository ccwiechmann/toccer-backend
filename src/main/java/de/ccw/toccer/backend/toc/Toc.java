package de.ccw.toccer.backend.toc;

import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.stream.StreamSource;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import de.ccw.toccer.backend.configuration.XmlConfigurationReader;
import de.ccw.toccer.backend.docx.DocxExporter;
import de.ccw.toccer.backend.xpath.XPathResolver;
import net.sf.saxon.s9api.Processor;

public class Toc {

	private final ToccerSettings settings;
	private List<String> finalPages;
	private List<TocEntry> entries;

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
		final XmlConfigurationReader reader = new XmlConfigurationReader(settings);
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

			final List<String> authors = XPathResolver.resolveXPath(processor, source, settings.getAuthorXpath());
			final List<String> titles = XPathResolver.resolveXPath(processor, source, settings.getTitleXpath());
			final List<String> pages = XPathResolver.resolveXPath(processor, source, settings.getPageXpath());
			final List<String> volumes = XPathResolver.resolveXPath(processor, source, settings.getVolumeXpath());
			final List<String> categories = XPathResolver.resolveXPath(processor, source, settings.getCategoryXpath());

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

	public Toc exportToDocx() {
		final Multimap<String, TocEntry> entriesByCategory = ArrayListMultimap.create();
		for (final TocEntry entry : entries) {
			entriesByCategory.put(entry.getCategory(), entry);
		}
		new DocxExporter(settings.getOutputFile(), entriesByCategory).export();
		return this;
	}
}
