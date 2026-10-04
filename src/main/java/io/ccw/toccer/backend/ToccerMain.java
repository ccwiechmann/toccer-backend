package io.ccw.toccer.backend;

import io.ccw.toccer.backend.toc.Toc;
import io.ccw.toccer.backend.toc.ToccerSettings;
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
        Toc.create(settings).loadFinalSites().generateTocEntries().exportToOdt();
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
