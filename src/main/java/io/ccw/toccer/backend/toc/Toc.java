package io.ccw.toccer.backend.toc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import io.ccw.toccer.backend.configuration.XmlConfigurationReader;
import io.ccw.toccer.backend.configuration.XmlConfigurationReader.XpathResolvableResult;
import io.ccw.toccer.backend.generated.XPathExpression;
import io.ccw.toccer.backend.odt.OdtExporter;
import io.ccw.toccer.backend.xpath.XPathResolver;
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
			final Map<String, String> xPathResultCache = page.getxPathResultCache();
			final Processor processor = XPathResolver.getProcessor();
			final StreamSource source;

			source = XPathResolver.getSource(page.getResolvableResult(),
					reader.getStrategy().isJson() == null ? false : reader.getStrategy().isJson(), page.getHeaders(),
					reader.getStrategy().isPost() == null ? false : reader.getStrategy().isPost(), page.getContent());

			final List<String> volumes = new ArrayList<>();
			final List<String> categories = new ArrayList<>();

			final List<String> authors = new ArrayList<>();
			final List<String> titles = new ArrayList<>();
			final List<String> pages = new ArrayList<>();
			if (reader.getMultiDataOnOnePageCountStrategy() == null) {
				resolveAndAddToListNoEmptyResult(settings.getAuthorXpath(), processor, source, xPathResultCache,
						authors);
				resolveAndAddToListNoEmptyResult(settings.getTitleXpath(), processor, source, xPathResultCache, titles);
				resolveAndAddToListNoEmptyResult(settings.getPageXpath(), processor, source, xPathResultCache, pages);
				resolveAndAddToListNoEmptyResult(settings.getVolumeXpath(), processor, source, xPathResultCache,
						volumes);
				resolveAndAddToListNoEmptyResult(settings.getCategoryXpath(), processor, source, xPathResultCache,
						categories);
			} else {
				final int count = Integer.parseInt(XPathResolver
						.resolveXPath(processor, source, reader.getMultiDataOnOnePageCountStrategy().getMaxXpath())
						.get(0));
				final String volume;
				if (settings.getVolumeXpath() == null) {
					volume = null;
				} else if (!settings.getVolumeXpath().getValue().contains("{0}")) {
					final String volumeReplacement = replaceFromCachedExpression(xPathResultCache,
							settings.getVolumeXpath().getValue());
					if (settings.getVolumeXpath().isOnlyHandleAsReplacement()) {
						volume = replaceFromCachedExpression(xPathResultCache, volumeReplacement);
					} else {
						volume = XPathResolver.resolveXPath(processor, source, volumeReplacement).get(0);
					}
				} else {
					volume = null;
				}
				String category = null;

				if ((reader.getMultiDataOnOnePageCountStrategy().isCategoryForEachEntry() == null
						|| !reader.getMultiDataOnOnePageCountStrategy().isCategoryForEachEntry())
						&& !settings.getCategoryXpath().getValue().contains("{0}")) {
					final String categoryReplacement = replaceFromCachedExpression(xPathResultCache,
							settings.getCategoryXpath().getValue());
					if (settings.getVolumeXpath().isOnlyHandleAsReplacement()) {
						category = replaceFromCachedExpression(xPathResultCache, categoryReplacement);
					} else {
						category = XPathResolver.resolveXPath(processor, source, categoryReplacement).get(0);
					}
				}

				if ((reader.getMultiDataOnOnePageCountStrategy().getTitleForEachEntry() == null
						|| !reader.getMultiDataOnOnePageCountStrategy().getTitleForEachEntry())
						&& !settings.getTitleXpath().getValue().contains("{0}")) {
					resolveAndAddToList(settings.getTitleXpath(), 1, processor, source, xPathResultCache, titles);
				}

				for (int i = 1; i <= count; i++) {
					if (reader.getMultiDataOnOnePageCountStrategy().getTitleForEachEntry() != null
							&& reader.getMultiDataOnOnePageCountStrategy().getTitleForEachEntry()) {
						resolveAndAddToList(settings.getTitleXpath(), i, processor, source, xPathResultCache, titles);
					}
					resolveAndAddToList(settings.getAuthorXpath(), i, processor, source, xPathResultCache, authors);
					resolveAndAddToList(settings.getPageXpath(), i, processor, source, xPathResultCache, pages);

					if (settings.getVolumeXpath() != null) {
						if (!settings.getVolumeXpath().getValue().contains("{0}")) {
							volumes.add(volume);
						} else {
							resolveAndAddToList(settings.getVolumeXpath(), i, processor, source, xPathResultCache,
									volumes);
						}
					}
					if (reader.getMultiDataOnOnePageCountStrategy().isCategoryForEachEntry()) {
						category = XPathResolver
								.resolveXPath(processor, source, replaceFromCachedExpression(xPathResultCache,
										settings.getCategoryXpath().getValue().replace("{0}", Integer.toString(i))))
								.get(0);
						categories.add(category);
					} else if (settings.getCategoryXpath().getValue().contains("{0}")) {
						resolveAndAddToList(settings.getCategoryXpath(), i, processor, source, xPathResultCache,
								categories);
					} else {
						categories.add(category);
					}
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

	private void resolveAndAddToList(XPathExpression expression, int i, Processor processor, StreamSource source,
			Map<String, String> xPathResultCache, List<String> resultList) {
		if (expression == null) {
			return;
		}

		if (expression.isOnlyHandleAsReplacement()) {
			resultList.add(replaceFromCachedExpression(xPathResultCache, expression.getValue()));
		} else {
			final String xPath = expression.getValue().replace("{0}", Integer.toString(i));
			resultList.addAll(XPathResolver.resolveXPath(processor, source,
					replaceFromCachedExpression(xPathResultCache, xPath)));
		}
	}

	private void resolveAndAddToListNoEmptyResult(XPathExpression expression, Processor processor, StreamSource source,
			Map<String, String> xPathResultCache, List<String> resultList) {
		if (expression == null) {
			return;
		}

		if (expression.isOnlyHandleAsReplacement()) {
			resultList.add(replaceFromCachedExpression(xPathResultCache, expression.getValue()));
		} else {
			resultList.addAll(XPathResolver.resolveXPathNoEmptyResult(processor, source,
					replaceFromCachedExpression(xPathResultCache, expression.getValue())));
		}
	}

	private String replaceFromCachedExpression(final Map<String, String> cachedExpressions, final String input) {
		String str = input;
		for (Entry<String, String> entry : cachedExpressions.entrySet()) {
			str = str.replace('{' + entry.getKey() + '}', entry.getValue());
		}
		return str;
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
		new OdtExporter(settings.getOutputFile(), entriesByCategory, settings.isSortCategories(),
				settings.getLabelNameOfPublication(), settings.getLabelToc(), settings.getLabelIssuePage()).export();
		return this;
	}
}
