package edu.handong.isel.allgitclone.act;

import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.util.HashMap;

public class ActivityControlUnit {
	private boolean check_blank = false;
	private boolean check_over_limits = false;		//indicate which the page is over 10 or not.
	private String last_date = null;
	
	
	public ActivityControlUnit() {
		
	}
	
	public void run(BufferedWriter bw, PrintWriter pw, HashMap<String, String> issueOption) {
		
	}

	
	public boolean isCheck_blank() {
		return check_blank;
	}

	public boolean isCheck_over_limits() {
		return check_over_limits;
	}

	public String getLast_date() {
		return last_date;
	}
	
}