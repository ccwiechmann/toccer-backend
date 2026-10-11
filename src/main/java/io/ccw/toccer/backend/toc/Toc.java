package io.ccw.toccer.backend.toc;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.ccw.toccer.backend.ToccerException;
import io.ccw.toccer.backend.xpath.Source;

import org.apache.commons.lang3.StringUtils;

import io.ccw.toccer.backend.configuration.XmlConfigurationReader;
import io.ccw.toccer.backend.configuration.XmlConfigurationReader.XpathResolvableResult;
import io.ccw.toccer.backend.generated.MultiDataOnOnePageCountStrategy;
import io.ccw.toccer.backend.generated.XPathExpression;
import io.ccw.toccer.backend.model.TocEntry;
import io.ccw.toccer.backend.model.ToccerSettings;
import io.ccw.toccer.backend.odt.OdtExporter;
import io.ccw.toccer.backend.xpath.XPathResolver;
import net.sf.saxon.s9api.Processor;

public final class Toc {

    private static final Logger LOG = LoggerFactory.getLogger(Toc.class);

    private final ToccerSettings settings;
    private List<XpathResolvableResult> finalPages;
    private List<TocEntry> entries;
    private XmlConfigurationReader reader;

    private Toc(ToccerSettings settings) {
        this.settings = settings;
    }

    /**
     * Creates and returns the Toc pipeline bound to the given settings.
     *
     * @param settings the command line settings (never {@code null})
     * @return the pipeline to be run with {@code .loadFinalSites()} and
     *         {@code .generateTocEntries()}
     */
    public static Toc create(ToccerSettings settings) {
        final Toc toc = new Toc(settings);
        return toc;
    }

    /**
     * Loads the configuration and resolves every final site.
     *
     * @return the same pipeline for fluent use
     */
    public Toc loadFinalSites() {
        if (settings == null) {
            throw new IllegalStateException("loadFinalSites() was called before a ToccerSettings was bound");
        }
        reader = new XmlConfigurationReader(settings);
        this.finalPages = reader.getFinalSites();
        return this;
    }

    /**
     * Evaluates all configured XPath expressions and builds the {@link TocEntry}
     * list.
     *
     * @return the same pipeline for fluent use
     */
    public Toc generateTocEntries() {
        if (finalPages == null) {
            throw new IllegalStateException("generateTocEntries() was called before loadFinalSites()");
        }

        int currentEntryNo = 0;
        final List<TocEntry> newEntries = new ArrayList<>();
        final MultiDataOnOnePageCountStrategy multi = reader.getMultiDataOnOnePageCountStrategy();
        for (final XpathResolvableResult page : finalPages) {
            final Map<String, String> xPathResultCache = page.getxPathResultCache();
            final Processor processor = XPathResolver.getProcessor();

            // The active strategy is either the generic one stored on the reader
            // or the MultiData strategy (both extend Strategy). reader.getStrategy()
            // is null for MultiData configs, so we need both.
            final io.ccw.toccer.backend.generated.Strategy activeStrategy = reader.getStrategy() != null
                    ? reader.getStrategy()
                    : (reader.getMultiDataOnOnePageCountStrategy() != null
                            ? reader.getMultiDataOnOnePageCountStrategy()
                            : null);
            final boolean isJson = activeStrategy != null && Boolean.TRUE.equals(activeStrategy.isJson());
            final boolean isPost = activeStrategy != null && Boolean.TRUE.equals(activeStrategy.isPost());
            final Source source = XPathResolver.fetchSource(page.getResolvableResult(), isJson, page.getHeaders(),
                    isPost, page.getContent());

            final FieldValues fields = new FieldValues();
            if (multi == null) {
                resolveSingleDataEntries(processor, source, xPathResultCache, fields);
            } else {
                resolveMultiDataEntries(multi, processor, source, xPathResultCache, fields);
            }

            assertConsistentSizes(page, fields);

            for (int i = 0; i < fields.volumes.size(); i++) {
                fields.volumes.set(i, reader.getVolumeReplacement(fields.volumes.get(i)));
            }

            for (int i = 0; i < fields.titles.size(); i++) {
                final String category = fields.categories.get(i);
                if (StringUtils.isEmpty(category)) {
                    final String title = fields.titles.get(i);
                    final String volume = fields.volumes.get(i);
                    fields.categories.set(i, reader.getCategoryReplacementIfAvailable(category, title, volume));
                }
            }

            for (int i = 0; i < fields.authors.size(); i++) {
                final TocEntry entry = new TocEntry(currentEntryNo);
                currentEntryNo++;
                entry.setAuthor(fields.authors.get(i));
                entry.setTitle(fields.titles.get(i));
                if (settings.getPageXpath() != null) {
                    entry.setPage(fields.pages.get(i));
                }
                if (settings.getVolumeXpath() != null) {
                    entry.setVolume(fields.volumes.get(i));
                }
                entry.setCategory(fields.categories.get(i));
                newEntries.add(entry);
                LOG.debug("Processed entry: {}", entry);
            }
        }
        this.entries = newEntries;

        return this;
    }

    private void resolveAndAddToList(XPathExpression expression, int i, Processor processor, Source source,
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

    private void resolveAndAddToListNoEmptyResult(XPathExpression expression, Processor processor, Source source,
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

    /**
     * Resolves the configured XPaths into per-field value lists for a page
     * described by a single-data configuration: each field is resolved once,
     * producing at most one value per field.
     *
     * @param processor the shared Saxon processor
     * @param source the page document
     * @param xPathResultCache the per-page cache of pre-resolved expressions
     * @param fields the per-field value lists to fill (mutated)
     */
    private void resolveSingleDataEntries(Processor processor, Source source, Map<String, String> xPathResultCache,
            FieldValues fields) {
        resolveAndAddToListNoEmptyResult(settings.getAuthorXpath(), processor, source, xPathResultCache,
                fields.authors);
        resolveAndAddToListNoEmptyResult(settings.getTitleXpath(), processor, source, xPathResultCache,
                fields.titles);
        resolveAndAddToListNoEmptyResult(settings.getPageXpath(), processor, source, xPathResultCache, fields.pages);
        resolveAndAddToListNoEmptyResult(settings.getVolumeXpath(), processor, source, xPathResultCache,
                fields.volumes);
        resolveAndAddToListNoEmptyResult(settings.getCategoryXpath(), processor, source, xPathResultCache,
                fields.categories);
    }

    /**
     * Resolves per-entry field values for a page described by a
     * {@link MultiDataOnOnePageCountStrategy}: the number of records is read
     * from {@code maxXpath} and every per-field XPath with a {@code {0}}
     * placeholder is resolved once per record index.
     *
     * @param multi the multi-data strategy (never {@code null})
     * @param processor the shared Saxon processor
     * @param source the page document
     * @param xPathResultCache the per-page cache of pre-resolved expressions
     * @param fields the per-field value lists to fill (mutated)
     */
    private void resolveMultiDataEntries(MultiDataOnOnePageCountStrategy multi, Processor processor, Source source,
            Map<String, String> xPathResultCache, FieldValues fields) {
        final boolean categoryForEachEntry = Boolean.TRUE.equals(multi.isCategoryForEachEntry());
        final boolean titleForEachEntry = Boolean.TRUE.equals(multi.isTitleForEachEntry());
        final int count = Integer.parseInt(XPathResolver.resolveXPath(processor, source, multi.getMaxXpath()).get(0));

        // A volume that is not indexed per record is resolved once and reused.
        final String volume;
        if (settings.getVolumeXpath() == null) {
            volume = null;
        } else if (!settings.getVolumeXpath().getValue().contains("{0}")) {
            final String volumeReplacement = replaceFromCachedExpression(xPathResultCache,
                    settings.getVolumeXpath().getValue());
            volume = settings.getVolumeXpath().isOnlyHandleAsReplacement()
                    ? replaceFromCachedExpression(xPathResultCache, volumeReplacement)
                    : XPathResolver.resolveXPath(processor, source, volumeReplacement).get(0);
        } else {
            volume = null;
        }

        // A category that is not indexed per record is resolved once and reused.
        String category = null;
        if (!categoryForEachEntry && !settings.getCategoryXpath().getValue().contains("{0}")) {
            final String categoryReplacement = replaceFromCachedExpression(xPathResultCache,
                    settings.getCategoryXpath().getValue());
            category = settings.getCategoryXpath().isOnlyHandleAsReplacement()
                    ? replaceFromCachedExpression(xPathResultCache, categoryReplacement)
                    : XPathResolver.resolveXPath(processor, source, categoryReplacement).get(0);
        }

        // A title that is not indexed per record is resolved once.
        if (!titleForEachEntry && !settings.getTitleXpath().getValue().contains("{0}")) {
            resolveAndAddToList(settings.getTitleXpath(), 1, processor, source, xPathResultCache, fields.titles);
        }

        for (int i = 1; i <= count; i++) {
            if (titleForEachEntry) {
                resolveAndAddToList(settings.getTitleXpath(), i, processor, source, xPathResultCache, fields.titles);
            }
            resolveAndAddToList(settings.getAuthorXpath(), i, processor, source, xPathResultCache, fields.authors);
            resolveAndAddToList(settings.getPageXpath(), i, processor, source, xPathResultCache, fields.pages);

            if (settings.getVolumeXpath() != null) {
                if (!settings.getVolumeXpath().getValue().contains("{0}")) {
                    fields.volumes.add(volume);
                } else {
                    resolveAndAddToList(settings.getVolumeXpath(), i, processor, source, xPathResultCache,
                            fields.volumes);
                }
            }

            if (categoryForEachEntry) {
                category = XPathResolver
                        .resolveXPath(processor, source, replaceFromCachedExpression(xPathResultCache,
                                settings.getCategoryXpath().getValue().replace("{0}", Integer.toString(i))))
                        .get(0);
                fields.categories.add(category);
            } else if (settings.getCategoryXpath().getValue().contains("{0}")) {
                resolveAndAddToList(settings.getCategoryXpath(), i, processor, source, xPathResultCache,
                        fields.categories);
            } else {
                fields.categories.add(category);
            }
        }
    }

    /**
     * Verifies that every resolved per-field list holds the same number of
     * values, so the entries can be zipped together consistently.
     *
     * @param page the page the values belong to (for the error message)
     * @param fields the per-field value lists to check
     * @throws ToccerException if the lists disagree on size
     */
    private void assertConsistentSizes(XpathResolvableResult page, FieldValues fields) {
        if (fields.authors.size() != fields.titles.size()
                || (fields.authors.size() != fields.pages.size() && settings.getPageXpath() != null)
                || (fields.authors.size() != fields.volumes.size() && settings.getVolumeXpath() != null)
                || fields.authors.size() != fields.categories.size()) {
            throw new ToccerException(String.format(
                    "Inconsistent XPath result sizes for page '%s': authors=%d, titles=%d, pages=%d, volumes=%d, "
                            + "categories=%d. Check that the configured XPath expressions resolve the same number"
                            + " of elements.",
                    page.getResolvableResult(), fields.authors.size(), fields.titles.size(), fields.pages.size(),
                    fields.volumes.size(), fields.categories.size()));
        }
    }

    /**
     * Bundles the per-field value lists that are resolved for a single page.
     * Keeping the five lists together lets the resolution helpers take one
     * target object instead of five parallel {@link List} parameters.
     */
    private static final class FieldValues {
        private final List<String> authors = new ArrayList<>();
        private final List<String> titles = new ArrayList<>();
        private final List<String> pages = new ArrayList<>();
        private final List<String> volumes = new ArrayList<>();
        private final List<String> categories = new ArrayList<>();
    }

    private String replaceFromCachedExpression(final Map<String, String> cachedExpressions, final String input) {
        String str = input;
        for (Entry<String, String> entry : cachedExpressions.entrySet()) {
            str = str.replace('{' + entry.getKey() + '}', entry.getValue());
        }
        return str;
    }

    /**
     * Returns the entries generated by the last call to
     * {@link #generateTocEntries()}, or {@code null} if it has not been run yet.
     *
     * @return the current list of entries, or {@code null}
     */
    public List<TocEntry> getEntries() {
        return entries;
    }

    /**
     * Writes the previously generated entries to an ODT file at the output
     * location configured in {@link ToccerSettings}.
     *
     * @return the same pipeline for fluent use
     */
    public Toc exportToOdt() {
        if (entries == null) {
            throw new IllegalStateException("exportToOdt() was called before generateTocEntries()");
        }
        final Map<String, List<TocEntry>> entriesByCategory = new LinkedHashMap<>();
        for (final TocEntry entry : entries) {
            final String category = entry.getCategoryForXml();
            if (reader.getMultiDataOnOnePageCountStrategy() == null
                    || Boolean.TRUE.equals(reader.getMultiDataOnOnePageCountStrategy().isCategoryForEachEntry())) {
                entriesByCategory.computeIfAbsent(category, k -> new ArrayList<>()).add(entry);
            } else {
                final List<TocEntry> existing = entriesByCategory.get(category);
                final String volumeOfNewEntry = entry.getVolumeForXml();
                if (existing != null && !existing.isEmpty()
                        && !java.util.Objects.equals(existing.get(0).getVolumeForXml(), volumeOfNewEntry)) {
                    entriesByCategory
                            .computeIfAbsent(category + java.util.Objects.toString(volumeOfNewEntry, ""),
                                    k -> new ArrayList<>())
                            .add(entry);
                    continue;
                }
                entriesByCategory.computeIfAbsent(category, k -> new ArrayList<>()).add(entry);
            }
        }
        new OdtExporter(settings.getOutputFile(), entriesByCategory, settings.isSortCategories(),
                settings.getLabelNameOfPublication(), settings.getLabelToc(), settings.getLabelIssuePage()).export();
        return this;
    }
}
