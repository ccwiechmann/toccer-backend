# AGENTS.md

Java 25 + Maven CLI tool: extracts tables of contents from websites via XML configs (schema `src/main/resources/inputSchema.xsd`) and writes ODT. Entrypoint `io.ccw.toccer.backend.ToccerMain` (`-i` config xml, `-o` output odt).

## Commands

- `mvn package` — compile + tests + Checkstyle + jar at `target/toccer-backend-0.0.1.jar`
- `mvn test` — JUnit (junit-jupiter 6); **no network** — tests install a fake `PageFetcher`
- Single test: `mvn test -Dtest=XPathResolverTest#testName`
- Style: `mvn checkstyle:check` — Sun rules at `warning` severity, failures fail the build; suppressions in `config/checkstyle-suppressions.xml`

## Gotchas

- `src/main/generated/` is JAXB code **checked in**, generated from `inputSchema.xsd`. Not part of the default Maven layout (added via build-helper plugin).
- To regenerate after a schema change: `mvn exec:exec` (manual only; deliberately not lifecycle-bound, so `mvn package` never rewrites these files). Commit the regenerated files.
- Compiler plugin forks to `/usr/bin/javac` on purpose — an in-process javac bug reports release 25 as "not supported". Don't remove `<fork>`/`<executable>`.
- ODT output is rendered from templates in `src/main/resources/odt/`.
