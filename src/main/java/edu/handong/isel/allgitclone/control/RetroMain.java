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
		CmdOptions cmdOptions = new CmdOptions(args);
		
		options = cmdOptions.getRefinedOpt();
		
		int pages = 1;
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File("gitService.csv"), true));
		PrintWriter pw = new PrintWriter(bw, true);
				
		
		pw.write("Description,forks count,Created date,Pushed date,Repo URL\n");
		pw.flush();
		
		
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
			
			cmdOptions.changeDate(options, act.getLast_pushed());
			pages = 1;
		}
		
		pw.close();
		return;
		

	}
	
}