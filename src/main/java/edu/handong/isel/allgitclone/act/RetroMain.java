package edu.handong.isel.allgitclone.act;

import java.io.BufferedWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import edu.handong.isel.allgitclone.act.ActivityControlUnit;
import edu.handong.isel.allgitclone.control.CmdOptions;

public class RetroMain {
	
	public static void main (String[] args) throws IOException, InterruptedException {
		
		long starter = System.currentTimeMillis();
		
		CmdOptions cmdOptions = new CmdOptions(args);

		
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File("CommitResult.csv"), true));
		PrintWriter pw = new PrintWriter(bw, true);
		
		ActivityControlUnit unit = new ActivityControlUnit();
		unit.run(cmdOptions, pw);
		
		long end = System.currentTimeMillis();
		
		System.out.println("The time required : " + (end - starter)/1000.0);
		
		return;
		
	}
}