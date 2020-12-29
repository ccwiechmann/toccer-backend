package io.ccw.toccer.backend;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

import io.ccw.toccer.backend.toc.Toc;
import io.ccw.toccer.backend.toc.ToccerSettings;

public class ToccerMain {

	public static void main(String[] args) {
		ToccerSettings configuration = parseArguments(args);
		Toc.create(configuration).loadFinalSites().generateTocEntries().exportToOdt();
	}

	private static ToccerSettings parseArguments(String[] args) {
		ToccerSettings arguments = new ToccerSettings();
		JCommander commander = null;
		try {
			commander = new JCommander(arguments, args);
		} catch (ParameterException e) {
			System.out.println(e.getMessage());
			System.out.println("Use -h or --help to get usage information.");
			System.exit(0);
		}

		if (arguments.isHelp()) {
			commander.usage();
			System.exit(0);
		}

		return arguments;
	}
}
