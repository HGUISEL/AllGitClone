package edu.handong.isel.allgitclone.control;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.HashMap;

public class RetroMain {
	private static HashMap<String, String> options;
	
	public static void main (String[] args) throws IOException, InterruptedException {
		
		MainActivity act = new MainActivity();
		InputControl set_input = new InputControl();
		options = set_input.getDefaultOption();
		int pages = 1;
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File("gitService.txt"), true));
		PrintWriter pw = new PrintWriter(bw, true);
		
		HashMap<String, String> ok = new HashMap<>();
		
		while(!act.isCheck_blank()) {
			
			while (pages != 11 && !act.isCheck_blank()) {
				
				//random sleep time to 1~3 sec
				double dValue = Math.random();
				int iValue = (int)(dValue * 2) + 1;
				Thread.sleep(iValue * 1000);
				
				options.replace("page", String.valueOf(pages));
				//act.runSyncService(bw, pw, options);
				act.runSyncService(bw, pw, options);
				
				System.out.println("current page : " + pages);
				System.out.println(options.get("q"));

				if (!act.isCheck_over_limits())
					pages++;
				
			}
			
			
			set_input.change_date(options, act.getLast_pushed());
			pages = 1;
		}
		
		pw.close();
		return;
		

	}
	
}