package edu.handong.isel.allgitclone.control;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

public class RetroMain {
	
	public static void main (String[] args) throws IOException, InterruptedException {
		
		MainActivity act = new MainActivity();
		
		int pages = 1;
		
		String lit = "1000";
		
		HashMap<String, String> options = new HashMap<>();
		options.put("q", "language:java forks:200.." + lit);
		options.put("page", String.valueOf(pages));
		options.put("sort", "forks");	//default : desc sorting
		options.put("per_page", String.valueOf(100));	//essential
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File("gitService.txt"), true));
		PrintWriter pw = new PrintWriter(bw, true);
		
		// 1000 counts reference available
		while(!act.isCheck_blank()) {
			
			if (act.isCheck_over_limits()) {
				lit = String.valueOf(act.getLast_forks());
				options.replace("q", "language:java forks:200.." + lit);
				pages = 1;
				System.out.println(act.isCheck_over_limits());
				act.setCheck_over_limits(false);
				Thread.sleep(10000);
			}
			
			else {
				options.replace("page", String.valueOf(pages));
				act.runSyncService(bw, pw, options);
				System.out.println("current page : " + pages);
				System.out.println(options.get("q"));
				pages++;
				Thread.sleep(1000);
			}
			
		}
		
		
		pw.close();
		return;
	}
}