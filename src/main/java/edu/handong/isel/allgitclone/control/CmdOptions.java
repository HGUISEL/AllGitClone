package edu.handong.isel.allgitclone.control;

import java.util.HashMap;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class CmdOptions {
	
	private String lang;
	private String forkCount;
	private String recentDate;
	private boolean help;
	private HashMap<String, String> refinedOpt;
	

	public CmdOptions (String[] args) {
		Options options = createOpt();
		refinedOpt = new HashMap<>();
		
		if (parseOptions(options, args)) {
			if (help) {
				printHelp(options);
				return;
			}
			
			RefiningOpt();
		}
		
	}
	
	//Consider the standard query date, how to change the value to reference all repos.
	public void changeDate(HashMap<String, String> options, String last_pushed) {
		String chg = options.get("q");
		String changed_date = last_pushed.substring(0, last_pushed.indexOf('T'));
			
		if (chg.contains("pushed")) {
			String origin_date = chg.substring(chg.length() - 10, chg.length());
			chg = chg.replace(origin_date, changed_date);
		}
			
		else
			chg += " pushed:<=" + changed_date;
			
		options.replace("q", chg);
		
	}
	
	
	public HashMap<String, String> getRefinedOpt() {
		return refinedOpt;
	}
	
	private void RefiningOpt() {
		
		String opt = "";
		
		if (!lang.isBlank())
			opt += "language:" + lang;
		
		
		if (!forkCount.isBlank())
			opt += " forks:" + forkCount;
		
		if (!recentDate.isBlank())
			opt += " pushed:" + recentDate;
		
		refinedOpt.put("q", opt);
		refinedOpt.put("sort", "updated");
		refinedOpt.put("page", String.valueOf(1));
		refinedOpt.put("per_page", String.valueOf(100));
		
		if (recentDate.contains(">="))
			refinedOpt.put("order", "asc");
	}
	

	private Options createOpt() {
		Options options = new Options();
		
		options.addOption(Option.builder("l").longOpt("language")
											.desc("Set language to search repositories.")
											.hasArg()
											.argName("language setting")
											.build());
		
		options.addOption(Option.builder("f").longOpt("fork")
											.desc("Set the count of forks to range of repositories.")
											.hasArg()
											.argName("fork counts")
											.build());
		
		options.addOption(Option.builder("d").longOpt("date")
											.desc("Set the date when the author pushed lately")
											.hasArg()
											.argName("recent push date")
											.build());
		
		return options;
	}
	
	
	private void printHelp(Options options) {
		HelpFormatter formatter = new HelpFormatter();
		String header = "All git clone CommandLine";
		String footer = "Available Options : language(l), fork(f), date(d)";
		formatter.printHelp("AllGitClone", header, options, footer, true);
	}
	
	
	private boolean parseOptions (Options options, String[] args) {
		CommandLineParser parser = new DefaultParser();
		
		try {
			CommandLine cmd = parser.parse(options, args);
			lang = cmd.getOptionValue("l", "");
			forkCount = cmd.getOptionValue("f", "");
			recentDate = cmd.getOptionValue("d", "");
			help = cmd.hasOption("h");
			
		}catch (Exception e) {
			printHelp(options);
			return false;
		}
		
		return true;
	}
}
