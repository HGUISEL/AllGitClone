package edu.handong.isel.allgitclone.control;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

public class RetroMain {
	
	
	public static void main (String[] args) throws IOException, InterruptedException {
		
		int pages = 1;
		String lit = "1000";
		HashMap<String, String> options = new HashMap<>();
		
		//Example parameters
		options.put("q", "language:java forks:200.." + lit);
		options.put("page", String.valueOf(pages));
		options.put("sort", "forks");	//default : descending sorting
		options.put("per_page", String.valueOf(100));	//essential
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File("gitService.txt"), true));
		PrintWriter pw = new PrintWriter(bw, true);
		
		// 1000 counts reference available
		while(true) {
			MainActivity act = new MainActivity();
			
			if (act.isCheck_blank())
				break;
			
			while (pages != 11) {
				
				//random sleep time to 1~3 sec
				double dValue = Math.random();
				int iValue = (int)(dValue * 2) + 1;
				Thread.sleep(iValue * 1000);
				
				options.replace("page", String.valueOf(pages));
				act.runSyncService(bw, pw, options);
				System.out.println("current page : " + pages);
				System.out.println(options.get("q"));

				
				if (!act.isCheck_over_limits())
					pages++;
				
			}
			
			lit = String.valueOf(act.getLast_forks());
			options.replace("q", "language:java forks:200.." + lit);
			pages = 1;

		}
		
		pw.close();
		return;
	}
}