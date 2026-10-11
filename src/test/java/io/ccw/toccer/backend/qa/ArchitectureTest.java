package io.ccw.toccer.backend.qa;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAnyPackage;
import static com.tngtech.archunit.core.domain.properties.HasName.Predicates.nameEndingWith;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

/**
 * Architecture rules that hold the {@code io.ccw.toccer.backend} packages
 * to a single, cycle-free dependency direction.
 *
 * <p>These rules run via the {@code archunit-junit6} extension and therefore
 * use the same JUnit 6 engine as the rest of the test suite. No other rule
 * in the codebase depends on the internals of this package; the tests may
 * be re-ordered, split or removed without affecting the rest of the tree.
 * </p>
 *
 * <p>The intended dependency layers are:
 * <ol>
 *   <li>{@code model} — value objects, no internal dependencies.</li>
 *   <li>{@code xpath}, {@code odt}, {@code configuration} — features built
 *       on top of {@code model} and the {@code generated} JAXB classes;
 *       they must not reference each other's upper layers.</li>
 *   <li>{@code toc} — orchestrates the pipeline using the packages above.</li>
 *   <li>{@code ToccerMain} — the CLI front door, depends on
 *       {@code toc} and {@code model}.</li>
 * </ol>
 * </p>
 */
@AnalyzeClasses(packages = "io.ccw.toccer.backend")
class ArchitectureTest {

    // -------------------------------------------------------------------
    // Cycles  (the single most important invariant)
    // -------------------------------------------------------------------

    /**
     * Top-level rule: no two top-level packages of the backend may form a
     * dependency cycle. This is the single most important invariant of the
     * code base and the one that motivated the split of the old
     * {@code toc <-> configuration} and {@code toc <-> odt} cycles.
     */
    @ArchTest
    static final ArchRule top_level_packages_are_acyclic =
            slices().matching("..backend.(*)..").namingSlices("$1")
                    .as("top-level backend packages")
                    .should().beFreeOfCycles();

    // -------------------------------------------------------------------
    // Layering  (each feature package depends only on the ones below it)
    // -------------------------------------------------------------------

    /**
     * The {@code generated} JAXB classes are a leaf: they must not call
     * back into any other backend feature package.
     */
    @ArchTest
    static final ArchRule generated_is_a_leaf =
            noClasses().that(resideInAnyPackage("io.ccw.toccer.backend.generated.."))
                    .should().dependOnClassesThat(resideInAnyPackage(
                            "io.ccw.toccer.backend.model..",
                            "io.ccw.toccer.backend.configuration..",
                            "io.ccw.toccer.backend.odt..",
                            "io.ccw.toccer.backend.toc..",
                            "io.ccw.toccer.backend.xpath.."))
                    .because("the generated JAXB code must stay a leaf in the dependency graph");

    /**
     * Value objects in {@code model} have no knowledge of feature packages.
     * They are the lowest hand-written layer in the dependency graph.
     * Dependencies on the {@code generated} JAXB DTOs are allowed because
     * {@code ToccerSettings} holds {@code XPathExpression} fields supplied
     * by the configuration parser.
     */
    @ArchTest
    static final ArchRule model_is_a_leaf =
            noClasses().that(resideInAnyPackage("io.ccw.toccer.backend.model.."))
                    .should().dependOnClassesThat(resideInAnyPackage(
                            "io.ccw.toccer.backend.configuration..",
                            "io.ccw.toccer.backend.odt..",
                            "io.ccw.toccer.backend.toc..",
                            "io.ccw.toccer.backend.xpath.."))
                    .because("model classes are plain value objects and must not depend on feature packages");

    /**
     * The {@code xpath} package talks to the network / Saxon only. It must
     * not reach out to business logic in {@code configuration}, {@code toc}
     * or {@code odt}.
     */
    @ArchTest
    static final ArchRule xpath_is_a_leaf =
            noClasses().that(resideInAnyPackage("io.ccw.toccer.backend.xpath.."))
                    .should().dependOnClassesThat(resideInAnyPackage(
                            "io.ccw.toccer.backend.configuration..",
                            "io.ccw.toccer.backend.odt..",
                            "io.ccw.toccer.backend.toc.."))
                    .because("the xpath package handles page fetching and XPath evaluation and must stay reusable by all upper layers");

    /**
     * The {@code odt} package renders output only. It reads value objects
     * from {@code model} but never reaches up into {@code configuration}
     * or {@code toc}, which are its clients.
     */
    @ArchTest
    static final ArchRule odt_does_not_reach_up_into_configuration_or_toc =
            noClasses().that(resideInAnyPackage("io.ccw.toccer.backend.odt.."))
                    .should().dependOnClassesThat(resideInAnyPackage(
                            "io.ccw.toccer.backend.configuration..",
                            "io.ccw.toccer.backend.toc.."))
                    .because("odt rendering must not know about the upstream configuration or pipeline");

    /**
     * {@code configuration} parses the input XML and knows about the
     * {@code generated} JAXB types, {@code xpath} (for page fetching) and
     * {@code model} (for the pipeline settings and result DTO). It must
     * not reach up into {@code toc}, which orchestrates it.
     */
    @ArchTest
    static final ArchRule configuration_does_not_depend_on_toc =
            noClasses().that(resideInAnyPackage("io.ccw.toccer.backend.configuration.."))
                    .should().dependOnClassesThat(resideInAnyPackage("io.ccw.toccer.backend.toc.."))
                    .because("configuration is a leaf consumed by the toc pipeline, not the other way around");

    // -------------------------------------------------------------------
    // API guarantees
    // -------------------------------------------------------------------

    /**
     * {@code TocEntry} participates in {@code Collections.sort} throughout
     * the pipeline; it must therefore be {@code Comparable}. Dropping the
     * {@code Comparable} interface would break the sort in
     * {@code Toc#generateTocEntries}.
     */
    @ArchTest
    static final ArchRule toc_entry_is_comparable =
            classes().that(resideInAnyPackage("io.ccw.toccer.backend.model..")
                    .and(nameEndingWith("TocEntry")))
                    .should().beAssignableTo(Comparable.class)
                    .because("TocEntry must remain Comparable — the pipeline sorts on it");
}
