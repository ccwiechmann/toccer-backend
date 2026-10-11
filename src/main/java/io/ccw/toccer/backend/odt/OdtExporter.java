package io.ccw.toccer.backend.odt;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.ccw.toccer.backend.ToccerException;
import io.ccw.toccer.backend.model.TocEntry;

public class OdtExporter {

    private static final Logger LOG = LoggerFactory.getLogger(OdtExporter.class);

    // Templates are small, immutable classpath resources; loading them once
    // per JVM avoids re-reading (and re-decoding) the same bytes for every
    // entry when rendering large tables of contents.
    private static final Map<String, String> TEMPLATE_CACHE = new ConcurrentHashMap<>();
    private static final Map<String, byte[]> BINARY_CACHE = new ConcurrentHashMap<>();

    private String filepath;
    private Map<String, List<TocEntry>> entriesByCategory;
    private boolean isSortCategories;
    private String labelNameOfPublication;
    private String labelToc;
    private String labelIssuePage;

    /**
     * Creates an exporter with the parameters taken from the pipeline.
     *
     * @param filepath output file (absolute or relative path)
     * @param entriesByCategory TOC entries grouped by category
     * @param isSortCategories whether categories may be alphabetical
     * @param labelNameOfPublication label for the publication name
     * @param labelToc label for the TOC section
     * @param labelIssuePage label for the issue / page column
     */
    public OdtExporter(String filepath, Map<String, List<TocEntry>> entriesByCategory, boolean isSortCategories,
            String labelNameOfPublication, String labelToc, String labelIssuePage) {
        this.filepath = filepath;
        this.entriesByCategory = entriesByCategory;
        this.isSortCategories = isSortCategories;
        this.labelNameOfPublication = labelNameOfPublication;
        this.labelToc = labelToc;
        this.labelIssuePage = labelIssuePage;
    }

    /**
     * Renders the template, groups entries by category and writes the ODT file.
     */
    public void export() {
        final StringBuilder builderContentXml = new StringBuilder(100000);
        builderContentXml.append(getXmlFile("odt/odtPrefix.xml.template")
                .replace("{labelIssuePage}", labelIssuePage).replace("{labelToc}", labelToc));

        final List<String> categories = new ArrayList<>(entriesByCategory.keySet());
        if (isSortCategories) {
            Collections.sort(categories);
        } else {
            Collections.sort(categories, new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    final TocEntry entry1 = entriesByCategory.get(o1).iterator().next();
                    final TocEntry entry2 = entriesByCategory.get(o2).iterator().next();
                    final String volume1 = entry1.getVolumeForXml();
                    final String volume2 = entry2.getVolumeForXml();
                    // Fall back to the entry id if either category has no volume.
                    if (volume1 == null || volume2 == null) {
                        return entry1.getId().compareTo(entry2.getId());
                    }
                    // Numeric volumes sort numerically, everything else as string.
                    final boolean numeric1 = StringUtils.isNumeric(volume1);
                    final boolean numeric2 = StringUtils.isNumeric(volume2);
                    if (numeric1 && numeric2) {
                        return Integer.valueOf(volume1).compareTo(Integer.valueOf(volume2));
                    }
                    if (numeric1) {
                        return -1;
                    }
                    if (numeric2) {
                        return 1;
                    }
                    return volume1.compareTo(volume2);
                }
            });
        }

        int counter = 0;
        for (final String category : categories) {
            builderContentXml.append(
                    getXmlFile("odt/odtCategory.xml.template").replace("{category}", category));

            final List<TocEntry> entries = new ArrayList<>(entriesByCategory.get(category));
            Collections.sort(entries);

            for (final TocEntry entry : entries) {
                if (StringUtils.isNotEmpty(entry.getAuthorForXml())) {
                    builderContentXml.append(getXmlFile("odt/odtAuthor.xml.template")
                            .replace("{author}", entry.getAuthorForXml()));
                }

                if (StringUtils.isBlank(entry.getPageForXml()) && StringUtils.isBlank(entry.getVolumeForXml())) {
                    builderContentXml.append(getXmlFile("odt/odtEntryNoPageAndVolume.xml.template")
                            .replace("{title}", entry.getTitleForXml()));

                } else if (StringUtils.isNotBlank(entry.getPageForXml())
                        && StringUtils.isBlank(entry.getVolumeForXml())) {
                    builderContentXml.append(getXmlFile("odt/odtEntry.xml.template")
                            .replace("{title}", entry.getTitleForXml()).replace("{page}", entry.getPageForXml())
                            .replace("{issue}", ""));

                } else if (StringUtils.isBlank(entry.getPageForXml())
                        && StringUtils.isNotBlank(entry.getVolumeForXml())) {
                    builderContentXml.append(getXmlFile("odt/odtEntry.xml.template")
                            .replace("{title}", entry.getTitleForXml()).replace("{page}", "")
                            .replace("{issue}", entry.getVolumeForXml()));

                } else {
                    builderContentXml.append(getXmlFile("odt/odtEntry.xml.template")
                            .replace("{title}", entry.getTitleForXml()).replace("{page}", ", " + entry.getPageForXml())
                            .replace("{issue}", entry.getVolumeForXml()));
                }

                counter++;
            }
        }
        LOG.info("Exported {} entries to \"{}\"", counter, filepath);

        builderContentXml.append(getXmlFile("odt/odtSuffix.xml.template"));

        final byte[] zipBytes = BINARY_CACHE.computeIfAbsent(
                "odt/odtTemplate.zip", name -> {
                    try (java.io.InputStream stream = OdtExporter.class.getResourceAsStream("/" + name)) {
                        if (stream == null) {
                            throw new ToccerException("ODT template resource not found on classpath: " + name);
                        }
                        return stream.readAllBytes();
                    } catch (IOException e) {
                        throw new ToccerException("Could not read ODT template resource: " + name, e);
                    }
                });

        File tempFile = null;
        try {
            tempFile = File.createTempFile("odtTemplate", "toccer");
            tempFile.deleteOnExit();
            Files.write(tempFile.toPath(), zipBytes);
        } catch (IOException e1) {
            throw new ToccerException("Could not write the ODT template to a temporary file: "
                    + e1.getMessage(), e1);
        }

        final String stylesXml = getXmlFile("odt/styles.xml.template")
                .replace("{labelNameOfPublication}", labelNameOfPublication);

        final Map<String, String> env = new HashMap<>();
        env.put("create", "true");
        final URI uri = URI.create("jar:" + Paths.get(tempFile.getAbsolutePath()).toUri());
        try (FileSystem fs = FileSystems.newFileSystem(uri, env)) {
            Path nf = fs.getPath("content.xml");
            try (Writer writer = Files.newBufferedWriter(nf, StandardCharsets.UTF_8, StandardOpenOption.WRITE,
                    StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE)) {
                writer.write(builderContentXml.toString());
            }

            nf = fs.getPath("styles.xml");
            try (Writer writer = Files.newBufferedWriter(nf, StandardCharsets.UTF_8, StandardOpenOption.WRITE,
                    StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE)) {
                writer.write(stylesXml);
            }

        } catch (IOException e) {
            throw new ToccerException("Could not update content inside the ODT document: " + e.getMessage(), e);
        }
        final Path target = Paths.get(filepath);
        try {
            Files.deleteIfExists(target);
            Files.copy(Paths.get(tempFile.getAbsolutePath()), target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new ToccerException("Could not write the ODT output file: " + target + " (" + e.getMessage() + ")",
                    e);
        }
    }

    private String getXmlFile(String xmlFile) {
        return TEMPLATE_CACHE.computeIfAbsent(xmlFile, OdtExporter::readXmlFile);
    }

    private static String readXmlFile(String xmlFile) {
        try (java.io.InputStream stream = OdtExporter.class.getResourceAsStream("/" + xmlFile)) {
            if (stream == null) {
                throw new ToccerException("ODT template resource not found on classpath: " + xmlFile);
            }
            final byte[] encoded = stream.readAllBytes();
            return new String(encoded, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new ToccerException("Could not read ODT template resource: " + xmlFile, e);
        }
    }
}
