package edu.handong.isel.allgitclone.act;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import edu.handong.isel.allgitclone.control.CmdOptions;


public class ActivityControlUnit {

	public void run(CmdOptions cmdOptions) throws InterruptedException, IOException {
		HashMap<String, String> repoOpt = cmdOptions.getRepoOpt();
		HashSet<String> finalResult = new HashSet<>();
		
		
		// fields related to store file
		SimpleDateFormat sd = new SimpleDateFormat( "MM-dd" );
		Date time = new Date();
		String today = sd.format(time);
		String base_github = "https://github.com/";
		
		
		double dv;
		int iv;
		int pages = 1;
		
		
		
		/*
		 * First work : search repositories
		 */
		
		RepoActivity searchRepo = new RepoActivity(cmdOptions.getAuthToken());
		HashSet<String> repoResult = searchRepo.getRepoResult();
		
		while(!searchRepo.isBlank()) {
			
			while (pages != 11 && !searchRepo.isBlank()) {
				
				//random sleep time to 1~3s
				dv = Math.random();
				iv = (int)(dv * 2000) + 100;
				Thread.sleep(iv);
				
				repoOpt.replace("page", String.valueOf(pages));
				
				searchRepo.start(repoOpt);
				
				System.out.println("current page : " + pages);
				System.out.println(repoOpt.get("q"));

				if (!searchRepo.isBlocked())
					pages++;
				
			}
			
			cmdOptions.changeRepoUpdate(repoOpt, searchRepo.lastDate());
			pages = 1;
		}
		
		
		
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(today + "_Repo_list.csv"), true));
		PrintWriter pw = new PrintWriter(bw, true);
		pw.write("REPO" + "\n");
		pw.flush();
		
		for (String result : repoResult) {
			pw.write(base_github + result + "," + "\n");
			pw.flush();
		}
		
		System.out.println(repoResult.size() + " results are stored in " + today + "_Repo_list.csv");
		bw.close();
		pw.close();
		
		
		/*
		 * Second work : search commit (optional)
		 */
		if (!cmdOptions.getCommitCountBase().isBlank()) {
			String range = cmdOptions.getCommitCountBase();
			double progress = 0, percentage = 0;
			int commitRange;
			boolean excess = false;
			
			/**
			 * The inequality sign is assumed to be at the forefront of the @param range
			 */
			if (range.contains("<") || range.contains(">")) {
				
				if (range.contains(">"))
					excess = true;
				
				
				commitRange = Integer.parseInt(range.substring(1, range.length()));
			} else {
				commitRange = Integer.parseInt(range);
			}
			
			
			CommitActivity searchCommit = new CommitActivity(cmdOptions.getAuthToken());
			
			
			for (String query : repoResult)  {
				Thread.sleep(300);
				if (!searchCommit.isBlocked()) {
					percentage = progress / repoResult.size() * 100.0;
					System.out.println(String.format("%.1f", percentage) + "% work completed.");
					progress++;
					searchCommit.start(commitRange, excess, query, finalResult);
				}
				
				else {
					while (searchCommit.isBlocked()) {
						Thread.sleep(300);
						searchCommit.start(commitRange, excess, query, finalResult);
					}
					
				}
			}
			
			
			bw = new BufferedWriter(new FileWriter(new File(today + "__list.csv"), true));
			pw = new PrintWriter(bw, true);
			pw.write("REPO" + "," + "COMMITS" + "\n");
			pw.flush();
			

			for (String result : finalResult) {
				String[] arr = result.split("#");
				pw.write(base_github + arr[0] + "," + arr[1] + "," + "\n");
				pw.flush();
			}
			
			bw.close();
			pw.close();
			
			System.out.println(finalResult.size() + " results are finally stored.\n");
		}
	}
}