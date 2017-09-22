package de.ccw.toccer.backend.odt;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.net.URI;
import java.nio.charset.Charset;
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

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Multimap;

import de.ccw.toccer.backend.toc.TocEntry;

public class OdtExporter {

	private String filepath;
	private Multimap<String, TocEntry> entriesByCategory;
	private boolean isSortCategories;

	public OdtExporter(String filepath, Multimap<String, TocEntry> entriesByCategory, boolean isSortCategories) {
		this.filepath = filepath;
		this.entriesByCategory = entriesByCategory;
		this.isSortCategories = isSortCategories;
	}

	public void export() {
		final StringBuilder builder = new StringBuilder(100000);
		builder.append(getXmlFile("src/main/resources/odt/odtPrefix.xml"));

		final List<String> categories = new ArrayList<>(entriesByCategory.keySet());
		if (isSortCategories) {
			Collections.sort(categories);
		} else {
			Collections.sort(categories, new Comparator<String>() {
				@Override
				public int compare(String o1, String o2) {
					final TocEntry entry1 = entriesByCategory.get(o1).iterator().next();
					final TocEntry entry2 = entriesByCategory.get(o2).iterator().next();
					if (entry1.getVolumeForXml() == null || entry2.getVolumeForXml() == null) {
						return entry1.getId().compareTo(entry2.getId());
					}
					return Integer.valueOf(entry1.getVolumeForXml())
							.compareTo(Integer.valueOf(entry2.getVolumeForXml()));
				}
			});
		}

		int counter = 0;
		for (final String category : categories) {
			builder.append(getXmlFile("src/main/resources/odt/odtCategory.xml").replace("{category}", category));

			final List<TocEntry> entries = new ArrayList<>(entriesByCategory.get(category));
			Collections.sort(entries);

			for (final TocEntry entry : entries) {
				if (StringUtils.isNotEmpty(entry.getAuthorForXml())) {
					builder.append(getXmlFile("src/main/resources/odt/odtAuthor.xml").replace("{author}",
							entry.getAuthorForXml()));
				}
				if (entry.getPageForXml() == null && entry.getVolumeForXml() == null) {
					builder.append(getXmlFile("src/main/resources/odt/odtEntryNoPageAndVolume.xml").replace("{title}",
							entry.getTitleForXml()));
				} else {
					builder.append(getXmlFile("src/main/resources/odt/odtEntry.xml")
							.replace("{title}", entry.getTitleForXml())
							.replace("{page}", entry.getPageForXml() == null ? "" : (", " + entry.getPageForXml()))
							.replace("{issue}", entry.getVolumeForXml() == null ? "" : entry.getVolumeForXml()));
				}
				counter++;
			}
		}
		System.err.println("Export von " + counter + " Einträgen nach \"" + filepath + "\"");

		builder.append(getXmlFile("src/main/resources/odt/odtSuffix.xml"));

		final Map<String, String> env = new HashMap<>();
		env.put("create", "true");

		File tempFile = null;
		Path path = null;
		try {
			tempFile = File.createTempFile("odtTemplate", "toccer");
			tempFile.deleteOnExit();
			path = Paths.get("src/main/resources/odt/odtTemplate.zip");
			Files.copy(path, Paths.get(tempFile.getAbsolutePath()), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e1) {
			throw new IllegalStateException(e1);
		}

		final URI uri = URI.create("jar:" + Paths.get(tempFile.getAbsolutePath()).toUri());
		try (FileSystem fs = FileSystems.newFileSystem(uri, env)) {
			final Path nf = fs.getPath("content.xml");
			try (final Writer writer = Files.newBufferedWriter(nf, StandardCharsets.UTF_8, StandardOpenOption.WRITE,
					StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE)) {
				writer.write(builder.toString());
			}

		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
		final Path target = Paths.get(filepath);
		try {
			Files.deleteIfExists(target);
			Files.copy(Paths.get(tempFile.getAbsolutePath()), target, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	private String getXmlFile(String xmlFile) {
		try {
			final byte[] encoded = Files.readAllBytes(Paths.get(xmlFile));
			return new String(encoded, Charset.forName("UTF-8"));
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}
}
