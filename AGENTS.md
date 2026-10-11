# AGENTS.md

Java 25 + Maven CLI tool: extracts tables of contents from websites via XML configs (schema `src/main/resources/inputSchema.xsd`) and writes ODT. Entrypoint `io.ccw.toccer.backend.ToccerMain` (`-i` config xml, `-o` output odt).

## Commands

- `mvn test` — JUnit 6 (Jupiter), **no network** — tests install a fake `PageFetcher`. Includes the ArchUnit architecture rules in `src/test/java/io/ccw/toccer/backend/qa/`.
- `mvn package` — compile + tests + fat-jar at `target/toccer-backend-0.0.1.jar`. **Does not** run Checkstyle (bound to `verify` below).
- `mvn verify` — `mvn package` **plus** the Checkstyle check. This is the CI-equivalent command; use it to validate a change end-to-end.
- Single test: `mvn test -Dtest=XPathResolverTest#testName`
- Checkstyle only: `mvn checkstyle:check` — Sun ruleset at `warning` severity (every `warning` fails the build). Suppressions in `config/checkstyle-suppressions.xml`.

## Gotchas

- `src/main/generated/` is JAXB code **checked in**, generated from `inputSchema.xsd`. Not part of the default Maven layout (added via build-helper plugin).
- To regenerate after a schema change: `mvn exec:exec` (manual only; deliberately not lifecycle-bound, so `mvn package` never rewrites these files). Commit the regenerated files.
- Compiler plugin forks to `javac` (resolved via `PATH`) on purpose — the in-process compiler reports the Java 25 release target as "not supported". Don't remove `<fork>`/`<executable>`; and don't hard-code an absolute `javac` path (this host is a JRE, `java.home` has no `bin/javac`). Resolve it from `PATH` so it works locally and in CI.
- ODT output is rendered from templates in `src/main/resources/odt/`.
