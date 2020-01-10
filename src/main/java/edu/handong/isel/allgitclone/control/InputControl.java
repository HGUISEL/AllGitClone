package edu.handong.isel.allgitclone.control;

import java.util.HashMap;
import java.util.Scanner;

public class InputControl {
	private String lang;
	private String fork_count;
	private String pushed_date;

	
	public HashMap<String, String> getDefaultOption() {
		HashMap<String, String> options = new HashMap<>();
		Scanner sc = new Scanner(System.in);
		String opt = "";
		
		System.out.println("Language : ");
		lang = sc.nextLine();
		
		if (!lang.isBlank())
			opt += "language:" + lang;
		
		
		System.out.println("Range of forks : ");
		fork_count = sc.nextLine();
		
		if (!fork_count.isBlank())
			opt += " forks:" + fork_count;
		
		
		System.out.println("Range of created date (yyyy-mm-dd) : ");
		pushed_date = sc.nextLine();
		
		if (!pushed_date.isBlank())
			opt += " pushed:" + pushed_date;
		
		
		options.put("q", opt);
		options.put("sort", "updated");
		options.put("page", String.valueOf(1));
		options.put("per_page", String.valueOf(100));
		
		if (pushed_date.contains(">="))
			options.put("order", "asc");
		
		
		sc.close();
		
		return options;
	}
	
	//Consider the standard query date, how to change the value to reference all repos.
	public void change_date(HashMap<String, String> options, String last_pushed) {
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
	
}