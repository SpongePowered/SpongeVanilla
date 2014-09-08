package org.granitemc.granite.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    public static class PluginLogger {
        private final String id;

        public PluginLogger(String id) {
            this.id = id;
        }

        public void info(String format, Object... objects) {
            logfc(id, "info", format, objects);
        }

        public void warn(String format, Object... objects) {
            logfc(id, "warn", format, objects);
        }

        public void error(String format, Object... objects) {
            logfc(id, "error", format, objects);
        }

        public void debug(String format, Object... objects) {
            logfc(id, "debug", format, objects);
        }
    }


    public static boolean showThread = true;
    private static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

    public static void info(String format, Object... objects) {
        logf("info", format, objects);
    }

    public static void warn(String format, Object... objects) {
        logf("warn", format, objects);
    }

    public static void error(String format, Object... objects) {
        logf("error", format, objects);
    }

    public static void debug(String format, Object... objects) {
        logf("debug", format, objects);
    }

    public static void logf(String level, String format, Object... objects) {
        String tName = "Mythic Main Thread";
        if (showThread)
            tName = Thread.currentThread().getName();

        System.out.printf(String.format("[%s] [%s/%s] %s\n", sdf.format(new Date(System.currentTimeMillis())), tName, level.toUpperCase(), format), objects);
    }

    public static void infoc(String caller, String format, Object... objects) {
        logfc(caller, "info", format, objects);
    }

    public static void warnc(String caller, String format, Object... objects) {
        logfc(caller, "warn", format, objects);
    }

    public static void errorc(String caller, String format, Object... objects) {
        logfc(caller, "error", format, objects);
    }

    public static void debugc(String caller, String format, Object... objects) {
        logfc(caller, "debug", format, objects);
    }

    public static void logfc(String caller, String level, String format, Object... objects) {
        System.out.printf(String.format("[%s] [%s/%s] %s\n", sdf.format(new Date(System.currentTimeMillis())), caller, level.toUpperCase(), format), objects);
    }
}
