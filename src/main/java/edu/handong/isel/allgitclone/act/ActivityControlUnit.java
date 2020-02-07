package edu.handong.isel.allgitclone.act;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;

import edu.handong.isel.allgitclone.control.CmdOptions;


public class ActivityControlUnit {

	public void run(CmdOptions cmdOptions, PrintWriter pw) throws InterruptedException {
		HashMap<String, String> repoOpt = cmdOptions.getRepoOpt();
		HashMap<String, String> commitOpt = cmdOptions.getCommitOpt();
		HashSet<String> finalResult = new HashSet<>();
		
		double dValue;
		int iValue;
		int pages = 1;
		
		
		/*
		 * First work : search repositories
		 */
		
		RepoActivity searchRepo = new RepoActivity(cmdOptions.getAuthToken());
		HashSet<String> repoResult = searchRepo.getRepoResult();
		
		while(!searchRepo.isCheck_blank()) {
			
			while (pages != 11 && !searchRepo.isCheck_blank()) {
				
				//random sleep time to 1~3s
				dValue = Math.random();
				iValue = (int)(dValue * 2000) + 1;
				Thread.sleep(iValue);
				
				repoOpt.replace("page", String.valueOf(pages));
				
				searchRepo.start(repoOpt);
				
				System.out.println("current page : " + pages);
				System.out.println(repoOpt.get("q"));

				if (!searchRepo.isCheck_over_limits())
					pages++;
				
			}
			
			cmdOptions.changeRepoUpdate(repoOpt, searchRepo.getLast_date());
			pages = 1;
		}
		
		
		System.out.println(repoResult.size() + " results are stored.\n");
		
		
		/*
		 * Second work : search commit
		 */
		
		CommitActivity searchCommit = new CommitActivity(cmdOptions.getAuthToken());
		
		
		pages = 1;
		
		String originQuery = "";
		
		for (String query : repoResult)  {
			
			dValue = Math.random();
			iValue = (int)(dValue * 2000) + 1;
			Thread.sleep(iValue);
			
			if (!searchCommit.isCheck_over_limits()) {
			
				if (!commitOpt.get("q").contains("repo:")) {
					commitOpt.replace("q", commitOpt.get("q") + " repo:" + query);
					originQuery = query;
				}

				else {
					String base = commitOpt.get("q");
					base = base.replace(originQuery, query);
					commitOpt.replace("q", base);
					originQuery = query;
				}
			}
			
			searchCommit.start(commitOpt, finalResult);
			  
		}
		
		
		for (String result : finalResult) {
			pw.write(result + "," + "\n");
			pw.flush();
		}
		
		System.out.println("All results are stored in CommitResult.csv");
	}
}