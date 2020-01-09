package edu.handong.isel.allgitclone.control;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

public class InputControl {
	private String lang;
	private String fork_count;
	private String created_date;

	
	public HashMap<String, String> getDefaultOption() {
		HashMap<String, String> options = new HashMap<>();
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Language : ");
		lang = sc.nextLine();
		
		System.out.println("Range of forks : ");
		fork_count = sc.nextLine();
		
		System.out.println("Range of created date (yyyy-mm-dd) : ");
		created_date = sc.nextLine();
		
		String opt = "language:" + lang +
					" forks:" + fork_count +
					" created:" + created_date;
		
		options.put("q", opt);
		options.put("sort", "forks");
		options.put("page", String.valueOf(1));
		options.put("per_page", String.valueOf(100));
		
		sc.close();
		
		return options;
	}
	
	//Consider the standard query date, how to change the value to reference all repos.
	public void change_date(HashMap<String, String> options) throws ParseException {
		String chg = options.get("q");
		int index = chg.indexOf('-');
		int month = Integer.parseInt(chg.substring(index + 1, index + 3));
		int year = Integer.parseInt(chg.substring(index - 4, index));
		int date = Integer.parseInt(chg.substring(index + 4, index + 6));
		
		String origin_date = chg.substring(index - 4, index + 6);
		
		if (month != 12)
			month++;
		
		else {
			year++;
			month = 1;
		}
		
		SimpleDateFormat dat = new SimpleDateFormat("yyyy-MM-dd");
		Date convertedDate = dat.parse(year + "-" + month + "-" + date);
		String changed_date = dat.format(convertedDate);
		
		chg = chg.replace(origin_date, changed_date);
		
		
		options.replace("q", chg);
		System.out.println(chg);
	}
	
}