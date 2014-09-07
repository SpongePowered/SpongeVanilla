package com.mythicmc.mythic;

public class Logger {
	
	public static void info(String msg){
		System.out.println("[Mythic][INFO] " + msg);
	}
	public static void warn(String msg){
		System.out.println("[Mythic][WARN] " + msg);
	}
	public static void error(String msg){
		System.out.println("[Mythic][ERROR] " + msg);
	}

}