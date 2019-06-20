package com.mc.gl.objects;

import java.util.ArrayList;

import com.mc.main.CubeGame;

public class GlJobs {

	private static long MAX_EXEC_TIME = 2000l;
	private ArrayList<Runnable> jobs  = new ArrayList<Runnable>();
	
	public void queueJob(Runnable job) {
		if(job != null && !this.jobs.contains(job)) {
			this.jobs.add(job);
		}
	}
	
	public void executeJobs() {
		long start = System.currentTimeMillis();
		while(!this.jobs.isEmpty()) {
			this.jobs.remove(0).run();
			if((System.currentTimeMillis() - start) >= MAX_EXEC_TIME) {
				System.err.println("Skipping " + this.jobs.size() + " jobs, time exceeded " + MAX_EXEC_TIME + "ms!");
				break;
			}
		}
	}
	
	public static GlJobs getQueue() {
		return CubeGame.instance.renderer.jobQueue;
	}
	
}