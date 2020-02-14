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
	private String forks;
	private String recentDate;
	private String authorDate;
	private String authToken;
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
			
			defineOpt();
		}
	}
	
	
	
	public void changeRepoUpdate(HashMap<String, String> options, String last_pushed) {
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
	
	
	
	public void changeCommitUpdate(HashMap<String, String> options, String last_date) {
		String chg = options.get("q");
		String changed_date = last_date.substring(0, last_date.indexOf('T'));
		  
		if (chg.contains("author-date")) {
			String origin_date = chg.substring(chg.length() - 10, chg.length());
			
			
			if (origin_date.equals(changed_date))
				changed_date = modifyIfSame(chg, changed_date);
			
			
			chg = chg.replace(origin_date, changed_date);
		}
		
		else
			chg += " author-date:<=" + changed_date;
		
		options.replace("q", chg);
	}

	
	private String modifyIfSame(String query, String date) {
		int year = Integer.parseInt(date.substring(0, 4));
		int month = Integer.parseInt(date.substring(5, 7));
		String day = date.substring(8, 10);
		String returnDate;
		
		if (query.contains(">=")) {
			if (month == 12) {
				year++;
				month = 1;
			} else
				month++;
		
		
			if (month < 10)
				returnDate = year + "-0" + month + "-" + day;
		
			else
				returnDate = year + "-" + month + "-" + day;
		}
		
		else {
			if (month == 1) {
				year--;
				month = 12;
			} else
				month--;
		
		
			if (month < 10)
				returnDate = year + "-0" + month + "-" + day;
		
			else
				returnDate = year + "-" + month + "-" + day;
		}
		
		return returnDate;
	}
	
	
	
	
	/*	
	 *  language, fork, pushed : searching repositories
	 * 	author-date, committer-date : searching commits
	 */
	
	private void defineOpt() {
		
		String repo_opt = "";
		String commit_opt = "";
		
		if (!lang.isBlank())
			repo_opt += "language:" + lang;
		
		
		if (!forks.isBlank())
			repo_opt += " forks:" + forks;
		
		
		if (!recentDate.isBlank())
			repo_opt += " pushed:" + recentDate;
			
		
		if (!authorDate.isBlank())
			commit_opt += "author-date:" + authorDate;
	
		
		repoOpt.put("q", repo_opt);
		repoOpt.put("sort", "updated");
		repoOpt.put("page", "1");
		repoOpt.put("per_page", "100");
		
		commitOpt.put("q", commit_opt);
		commitOpt.put("sort", "author-date");
		commitOpt.put("page", "1");
		commitOpt.put("per_page", "100");
		
		if (recentDate.contains(">="))
			repoOpt.put("order", "asc");
		
		
		if (authorDate.contains(">="))
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
		
		
		options.addOption(Option.builder("ad").longOpt("adate")
											.desc("Set commit author-date")
											.hasArg()
											.argName("author date")
											.build());
		
		
		options.addOption(Option.builder("auth").desc("Set authentication token")
											.hasArg()
											.argName("authentication token")
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
			forks = cmd.getOptionValue("f", "");
			recentDate = cmd.getOptionValue("d", "");
			authorDate = cmd.getOptionValue("ad", "");
			authToken = cmd.getOptionValue("auth", "");
			help = cmd.hasOption("h");
			
		}catch (Exception e) {
			printHelp(options);
			return false;
		}
		
		return true;
	}
	
	
	
	public HashMap<String, String> getRepoOpt() {
		return repoOpt;
	}
	
	
	public String getAuthToken() {
		return authToken;
	}
	

	public HashMap<String, String> getCommitOpt() {
		return commitOpt;
	}
}
