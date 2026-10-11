# toccer-backend

A command line tool that extracts tables of contents (TOCs) from websites using
declarative XML configuration files and writes them to a uniform format (ODT).

Each supported publication is described by a configuration file that states
where the data is located (start URL, XPath expressions) and how to walk from
one page to the next. The tool fetches the pages, evaluates the configured
XPath expressions, assembles a list of TOC entries, and renders them into an
OpenDocument Text (`.odt`) file.

## Requirements

- Java 25 (LTS)
- [Maven](https://maven.apache.org/) 3.x
- Internet access (the tool fetches live web pages)

## Building

```sh
mvn package
```

This compiles the sources, runs the tests (including the ArchUnit
architecture rules) and produces an executable fat-jar at
`target/toccer-backend-0.0.1.jar`.

To also run the Checkstyle style gate before the jar is produced, use the
full verification lifecycle:

```sh
mvn verify
```

## Usage

```sh
java -jar target/toccer-backend-0.0.1.jar \
  --input src/main/resources/config/elektor.xml \
  --output my-publication-toc.odt
```

| Flag                | Required | Description                                    |
| ------------------- | -------- | ---------------------------------------------- |
| `-i`, `--input`     | yes      | Path to the input configuration XML template.  |
| `-o`, `--output`    | yes      | Path of the `.odt` file to generate.           |
| `-h`, `--help`      | no       | Print the usage information and exit.          |

Run `--help` on the jar for the full, authoritative usage text.

## Error handling

The CLI distinguishes two kinds of failure:

- **Command-line misuse** (unknown flag, missing required option) — Picocli
  prints its own message, exit status **1**.
- **Pipeline failure** (missing or unreadable input template, a page that
  could not be fetched, an XPath that does not compile or returns malformed
  data, pipeline steps run out of order) — a single `Error: …` line (plus the
  underlying cause if any) is printed to stderr, exit status **2**.

Shell wrappers can therefore test `$?` to tell a user input error apart from a
run-time failure.

## How it works

The pipeline runs three steps, each driven by the input configuration:

1. **Load final sites** — starting from the configured entry point, it follows
   the configured "next page" strategy until all pages (issues/articles) to
   extract have been discovered.
2. **Generate TOC entries** — on every collected page it evaluates the
   configured XPath expressions to resolve title, author, page, volume and
   category, producing a flat list of TOC entries.
3. **Export to ODT** — the entries are grouped by category, sorted, and
   rendered into an ODT file from the built-in templates.

The configuration schema is defined in
[`src/main/resources/inputSchema.xsd`](src/main/resources/inputSchema.xsd).
Ready-made configurations for various publications live in
[`src/main/resources/config/`](src/main/resources/config/).

## Configuration

Each configuration is an XML document conforming to `inputSchema.xsd`. The
main building blocks are:

- **`xpath`** — the entry point that enumerates the pages to process. It holds
  an `expressionWithReplaces` (the page URL, where `{0}` is replaced at
  runtime), a `strategy` that controls how many pages exist and how they are
  fetched, an optional `cachedXpathResult`, and an optional nested `next` that
  points to a sub-page (e.g. from a list page to the individual article page).
- **`titleXpath` / `authorXpath` / `pageXpath` / `volumeXpath` /
  `categoryXpath`** — the expressions that resolve the individual fields of a
  TOC entry from a page.
- **`startingHtmlPage`**, **`baseUrl`**, **`urlSuffix`** — the base URL
  handling.
- **Labels** — `labelNameOfPublication`, `labelToc`, `labelIssuePage` for the
  ODT output.
- **`sortCategories`** — whether categories are sorted alphabetically.
- **Replacements** — `emptyCategoryReplacements` and
  `manualVolumeReplacement` for correcting or back-filling extracted values.

### Strategies

The `strategy` element is abstract; a concrete type determines how pages are
counted and fetched:

- **`numberStrategy`** — a fixed numeric range (`from`/`to`, optionally
  zero-padded), e.g. issues 1–12 of a yearly publication.
- **`countStrategy`** — the number of pages is the result of a `count(...)`
  XPath on the current page, e.g. "as many article links as appear in this
  list".
- **`fixedPostUrlStrategy`** — a static list of `fixedUrls` to fetch.
- **`multiDataOnOnePageCountStrategy`** — several data records live on a single
  page; `maxXpath` gives their count and `categoryForEachEntry` /
  `titleForEachEntry` control per-entry fields.

Base strategy options shared by all types:

- **`json`** — the page body is JSON and is converted to HTML before XPath
  resolution.
- **`post`** — fetch with an HTTP POST; `postContent` holds the payload.
- **`headers`** — additional HTTP headers to send.

Cached XPath results (`cachedXpathResult`) let you compute a value once (e.g.
the issue number) and reference it by name with `{name}` from other
expressions.

## Development

- **Code style** — Checkstyle (default Sun rules, suppressions in
  [`config/checkstyle-suppressions.xml`](config/checkstyle-suppressions.xml))
  runs in the `verify` phase. Keep it green:

  ```sh
  mvn checkstyle:check
  ```

- **Tests** — JUnit 6 (Jupiter) via Surefire:

  ```sh
  mvn test
  ```

- **Architecture tests** — ArchUnit rules in `src/test/java/io/ccw/toccer/backend/qa/`
  enforce a cycle-free, one-directional package layout (`model` is the leaf
  layer that the feature packages `xpath` / `odt` / `configuration` build on,
  which in turn feed the `toc` pipeline). They run as part of `mvn test`:

  ```sh
  mvn test -Dtest=ArchitectureTest
  ```

- **JAXB generation** — the classes in `src/main/generated` are generated from
  `src/main/resources/inputSchema.xsd`. If you change the schema, regenerate
  them with:

  ```sh
  mvn exec:exec
  ```

  This is intentionally **not** bound to any lifecycle phase, so a plain
  `mvn`/`mvn package` never rewrites the checked-in generated sources. After
  regenerating, commit the updated files under `src/main/generated`.
