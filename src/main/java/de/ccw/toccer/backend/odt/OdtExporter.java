package de.ccw.toccer.backend.odt;

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
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Multimap;

import de.ccw.toccer.backend.toc.TocEntry;

public class OdtExporter {

	private String filepath;
	private Multimap<String, TocEntry> entriesByCategory;

	public OdtExporter(String filepath, Multimap<String, TocEntry> entriesByCategory) {
		this.filepath = filepath;
		this.entriesByCategory = entriesByCategory;
	}

	public void export() {
		System.err.println("Export von " + entriesByCategory.size() + " Einträgen nach \"" + filepath + "\"");
		final StringBuilder builder = new StringBuilder(100000);
		builder.append(getXmlFile("src/main/resources/odt/odtPrefix.xml"));

		final List<String> categories = new ArrayList<>(entriesByCategory.keySet());
		Collections.sort(categories);

		for (final String category : categories) {
			builder.append(getXmlFile("src/main/resources/odt/odtCategory.xml").replace("{category}", category));

			final List<TocEntry> entries = new ArrayList<>(entriesByCategory.get(category));
			Collections.sort(entries);

			for (final TocEntry entry : entries) {
				builder.append(getXmlFile("src/main/resources/odt/odtEntry.xml").replace("{author}", entry.getAuthor())
						.replace("{title}", entry.getTitle()).replace("{page}", entry.getPage())
						.replace("{issue}", entry.getVolume()));
			}
		}

		builder.append(getXmlFile("src/main/resources/odt/odtSuffix.xml"));

		final Map<String, String> env = new HashMap<>();
		env.put("create", "true");
		final Path path = Paths.get("src/main/resources/odt/odtTemplate.zip");
		final URI uri = URI.create("jar:" + path.toUri());
		try (FileSystem fs = FileSystems.newFileSystem(uri, env)) {
			final Path nf = fs.getPath("content.xml");
			try (final Writer writer = Files.newBufferedWriter(nf, StandardCharsets.UTF_8, StandardOpenOption.WRITE)) {
				writer.write(builder.toString());
			}

			final Path target = Paths.get(filepath);
			Files.deleteIfExists(target);
			Files.copy(path, target);
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
