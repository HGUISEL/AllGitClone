package edu.handong.isel.allgitclone.act;

import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.util.HashMap;

import edu.handong.isel.allgitclone.control.CmdOptions;


public class ActivityControlUnit {

	public void run(CmdOptions cmdOptions, BufferedWriter bw, PrintWriter pw) throws InterruptedException {
		HashMap<String, String> repoOpt = cmdOptions.getRepoOpt();
		HashMap<String, String> commitOpt = cmdOptions.getCommitOpt();
		double dValue;
		int iValue;
		int pages = 1;
		
		
		/*
		 * First work : search repositories
		 */
		
		RepoActivity searchRepo = new RepoActivity(cmdOptions.getAuthToken());
		
		while(!searchRepo.isCheck_blank()) {
			
			while (pages != 11 && !searchRepo.isCheck_blank()) {
				
				//random sleep time to 1~3s
				dValue = Math.random();
				iValue = (int)(dValue * 2000) + 1;
				Thread.sleep(iValue);
				
				repoOpt.replace("page", String.valueOf(pages));
				
				searchRepo.start(bw, pw, repoOpt);
				
				System.out.println("current page : " + pages);
				System.out.println(repoOpt.get("q"));

				if (!searchRepo.isCheck_over_limits())
					pages++;
				
			}
			
			cmdOptions.changeRepoUpdate(repoOpt, searchRepo.getLast_date());
			pages = 1;
		}
		
		
		System.out.println(searchRepo.getRepoResult().size() + " results are stored.\n");
		
		
		//If there is no option for commit searching query, the program is exit. 
		if (cmdOptions.getCommitOpt().get("q").isBlank()) {
			System.out.println("As there is no option for commit query, the search is end up.");
			return;
		}
		
		/*
		 * Second work : search commit
		 */
		
		CommitActivity searchCommit = new CommitActivity(searchRepo.getRepoResult(), cmdOptions.getAuthToken());
		pages = 1;
		
		while(!searchCommit.isCheck_blank()) {
			
			while (pages != 11 && !searchCommit.isCheck_blank()) {
				
				//random sleep time to 1~3s
				dValue = Math.random();
				iValue = (int)(dValue * 2000) + 1;
				Thread.sleep(iValue);
				
				commitOpt.replace("page", String.valueOf(pages));
				
				searchCommit.start(bw, pw, commitOpt);
				
				System.out.println("current page : " + pages);
				System.out.println(commitOpt.get("q"));

				if (!searchCommit.isCheck_over_limits())
					pages++;
				
			}
			
			cmdOptions.changeCommitUpdate(commitOpt, searchCommit.getLast_date());
			pages = 1;
		}
	}
}