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
	private String authorDate;
	private String committerDate;
	private boolean help;
	private HashMap<String, String> repoOpt;
	private HashMap<String, String> commitOpt;
	

	public CmdOptions (String[] args) {
		Options options = createOpt();
		repoOpt = new HashMap<>();
		commitOpt = new HashMap<>();
		
		if (parseOptions(options, args)) {
			if (help) {
				printHelp(options);
				return;
			}
			
			RefiningOpt();
		}
		
	}
	
	
	
	//Consider the standard query date, how to change the value to reference all repos.
	public void changeUpdate(HashMap<String, String> options, String last_pushed) {
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
	
	
	
	public void changeCommitDate(HashMap<String, String> options, String last_date) {
		String chg = options.get("q");
		String changed_date = last_date.substring(0, last_date.indexOf('T'));
		
		if (chg.contains("author-date")) {
			String origin_date = chg.substring(chg.length() - 10, chg.length());
			
			/*
			 * if the changed date is same as right before
			 * change twice to increase day
			 * 
			 */
			if (origin_date.equals(changed_date))
				changed_date = changeDay(changed_date);
			
			
			chg = chg.replace(origin_date, changed_date);
		}
		
		else
			chg += " author-date:<=" + changed_date;
		
		options.replace("q", chg);
	}
	
	
	
	public HashMap<String, String> getRepoOpt() {
		return repoOpt;
	}
	
	
	
	public HashMap<String, String> getCommitOpt() {
		return commitOpt;
	}
	
	
	
	private String changeDay(String date) {
		int year = Integer.parseInt(date.substring(0, 4));
		int month = Integer.parseInt(date.substring(5, 7));
		String day = date.substring(8, 10);
		String returnDate;
		
		if (month == 12) {
			year++;
			month = 1;
		}
		
		else
			month++;
		
		
		if (month < 10)
			returnDate = year + "-0" + month + "-" + day;
		
		else
			returnDate = year + "-" + month + "-" + day;
		
		return returnDate;
	}
	
	
	
	
	/*	language, fork, pushed : searching repositories
	 * 	author-date, committer-date : searching commits
	 */
	
	private void RefiningOpt() {
		
		String repo_opt = "";
		String commit_opt = "";
		
		if (!lang.isBlank())
			repo_opt += "language:" + lang;
		
		
		if (!forkCount.isBlank())
			repo_opt += " forks:" + forkCount;
		
		
		if (!recentDate.isBlank())
			repo_opt += " pushed:" + recentDate;
			
		
		if (!authorDate.isBlank())
			commit_opt += "author-date:" + authorDate;
		
		
		if (!committerDate.isBlank())
			commit_opt += " committer-date:" + committerDate;
		
		
		repoOpt.put("q", repo_opt);
		repoOpt.put("sort", "updated");
		repoOpt.put("page", String.valueOf(1));
		repoOpt.put("per_page", String.valueOf(100));
		
		commitOpt.put("q", commit_opt);
		commitOpt.put("sort", "author-date");
		commitOpt.put("page", String.valueOf(1));
		commitOpt.put("per_page", String.valueOf(100));
		
		if (recentDate.contains(">="))
			repoOpt.put("order", "asc");
		
		
		if (authorDate.contains(">=") || authorDate.contains(">="))
			commitOpt.put("order", "asc");
		
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
		
		
		options.addOption(Option.builder("lb").longOpt("label")
											.desc("Search pull request & issues using labels")
											.hasArg()
											.argName("label setting")
											.build());
		
		options.addOption(Option.builder("ad").longOpt("adate")
											.desc("Set commit author-date")
											.hasArg()
											.argName("author date")
											.build());
		
		options.addOption(Option.builder("cd").longOpt("cdate")
											.desc("Set commit committer-date")
											.hasArg()
											.argName("committer date")
											.build());
		
		return options;
	}
	
	
	private void printHelp(Options options) {
		HelpFormatter formatter = new HelpFormatter();
		String header = "All git clone CommandLine";
		String footer = "Available Options : Please refer to Readme file";
		formatter.printHelp("AllGitClone", header, options, footer, true);
	}
	
	
	private boolean parseOptions (Options options, String[] args) {
		CommandLineParser parser = new DefaultParser();
		
		try {
			CommandLine cmd = parser.parse(options, args);
			lang = cmd.getOptionValue("l", "");
			forkCount = cmd.getOptionValue("f", "");
			recentDate = cmd.getOptionValue("d", "");
			authorDate = cmd.getOptionValue("ad", "");
			committerDate = cmd.getOptionValue("cd", "");
			help = cmd.hasOption("h");
			
		}catch (Exception e) {
			printHelp(options);
			return false;
		}
		
		return true;
	}
}
