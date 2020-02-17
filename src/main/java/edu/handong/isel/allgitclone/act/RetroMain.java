package edu.handong.isel.allgitclone.act;

import java.io.IOException;

import edu.handong.isel.allgitclone.act.ActivityControlUnit;
import edu.handong.isel.allgitclone.control.CmdOptions;

public class RetroMain {
	
	public static void main (String[] args) throws InterruptedException, IOException {
		
		long starter = System.currentTimeMillis();
		
		CmdOptions cmdOptions = new CmdOptions(args);
		ActivityControlUnit unit = new ActivityControlUnit();
		unit.run(cmdOptions);
		
		long end = System.currentTimeMillis();
		System.out.println("The time required : " + (end - starter)/1000.0);
		
		return;
	}
}