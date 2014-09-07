package com.mythicmc.mythic.utils;

import java.util.Date;

public class Logger {

    public static void info(String format, Object...objects) {
        logf("info", format, objects);
    }

    public static void warn(String format, Object...objects) {
        logf("warn", format, objects);
    }

    public static void error(String format, Object...objects) {
        logf("error", format, objects);
    }
    
    public static void debug(String format, Object...objects) {
    	logf("debuh", format, objects);
    }
    
    public static void logf(String level, String format, Object...objects) {
    	System.out.printf(String.format("%s[%s/%s]%s", "Mythic",new Date(System.currentTimeMillis()), level.toUpperCase(), format), objects);	
    }
}