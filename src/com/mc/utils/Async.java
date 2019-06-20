package com.mc.utils;

public class Async {

	public static Thread execute(Runnable runnable) {
		Thread t = new Thread(runnable);
		t.setName("Execute " + runnable.toString() + " (async)");
		t.start();
		return t;
	}
	
}