package edu.handong.isel.allgitclone.control;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.HashMap;

import edu.handong.isel.allgitclone.act.ActivityControlUnit;
import edu.handong.isel.allgitclone.act.CommitActivity;
import edu.handong.isel.allgitclone.act.RepoActivity;

public class RetroMain {
	private static HashMap<String, String> repoOpt, commitOpt;
	
	
	
	public static void main (String[] args) throws IOException, InterruptedException {
		
		CmdOptions cmdOptions = new CmdOptions(args);
		repoOpt = cmdOptions.getRepoOpt();
		commitOpt = cmdOptions.getCommitOpt();
		double dValue;
		int iValue;
		
		//ActivityControlUnit unit = new ActivityControlUnit();
		
		int pages = 1;
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File("CommitResult.csv"), true));
		PrintWriter pw = new PrintWriter(bw, true);
		
		RepoActivity unit = new RepoActivity();
		
		while(!unit.isCheck_blank()) {
			
			while (pages != 11 && !unit.isCheck_blank()) {
				
				//random sleep time to 1~3 sec
				dValue = Math.random();
				iValue = (int)(dValue * 2000) + 1;
				Thread.sleep(iValue);
				
				repoOpt.replace("page", String.valueOf(pages));
				
				unit.start(bw, pw, repoOpt);
				
				System.out.println("current page : " + pages);
				System.out.println(repoOpt.get("q"));

				if (!unit.isCheck_over_limits())
					pages++;
				
			}
			
			cmdOptions.changeUpdate(repoOpt, unit.getLast_date());
			pages = 1;
		}
		
		
		System.out.println("\n*******Convert to commit search*********\n");
		
		CommitActivity unit2 = new CommitActivity();
		pages = 1;
		
		while(!unit2.isCheck_blank()) {
			
			while (pages != 11 && !unit2.isCheck_blank()) {
				
				//random sleep time to 1~3 sec
				dValue = Math.random();
				iValue = (int)(dValue * 2000) + 1;
				Thread.sleep(iValue);
				
				commitOpt.replace("page", String.valueOf(pages));
				
				unit2.start(bw, pw, commitOpt);
				
				System.out.println("current page : " + pages);
				System.out.println(commitOpt.get("q"));

				if (!unit2.isCheck_over_limits())
					pages++;
				
			}
			
			cmdOptions.changeCommitDate(commitOpt, unit2.getLast_date());
			pages = 1;
		}
		
	
		return;
		
	}
	
}