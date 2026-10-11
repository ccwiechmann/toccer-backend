package io.ccw.toccer.backend;

import io.ccw.toccer.backend.model.ToccerSettings;
import io.ccw.toccer.backend.toc.Toc;
import picocli.CommandLine;
import picocli.CommandLine.ParameterException;

/**
 * Entry point of the {@code toccer-backend} command line tool.
 *
 * <p>
 * Parses the command line arguments into a {@link ToccerSettings} and runs the
 * pipeline: <em>load final sites → generate TOC entries → export to ODT</em>.
 * </p>
 */
public final class ToccerMain {

    private ToccerMain() {
    }

    /**
     * Entry point.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        final ToccerSettings settings = parseArguments(args);
        try {
            Toc.create(settings).loadFinalSites().generateTocEntries().exportToOdt();
        } catch (final ToccerException | IllegalStateException error) {
            // Expected, input-caused failures (bad or missing config, fetch
            // errors, inconsistent XPath results, pipeline steps out of order)
            // and state violations. Printed as a clean one-liner with a distinct
            // exit status (2) so shell callers can distinguish them from
            // command-line misuse (exit status 1). The underlying cause, if any,
            // is shown on the following line.
            System.err.println("Error: " + error.getMessage());
            if (error.getCause() != null) {
                System.err.println("Caused by: " + error.getCause());
            }
            System.err.println("See the README for the expected input format. Use --help for usage.");
            System.exit(2);
        } catch (final Exception error) {
            // Truly unexpected failure (a bug or a library error we did not
            // handle): keep the full stack trace for diagnosis.
            System.err.println("Error: " + error.getMessage());
            error.printStackTrace();
            System.exit(2);
        }
    }

    /**
     * Parses the command line arguments.
     * <p>
     * Exits the JVM with a non-zero status if the arguments are invalid, and with
     * status zero if the user only requested the help message.
     * </p>
     *
     * @param args raw command line arguments
     * @return the parsed settings
     */
    private static ToccerSettings parseArguments(String[] args) {
        final ToccerSettings settings = new ToccerSettings();
        final CommandLine cmd = new CommandLine(settings);

        try {
            cmd.parseArgs(args);
        } catch (final ParameterException e) {
            System.err.println(e.getMessage());
            System.err.println("Use --help for usage information.");
            System.exit(1);
        }

        if (settings.isHelp()) {
            cmd.usage(System.out);
            System.exit(0);
        }

        return settings;
    }
}
