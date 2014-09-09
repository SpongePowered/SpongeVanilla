package org.granitemc.granite.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * License (MIT)
 *
 * Copyright (c) 2014. avarisc
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the
 * Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

public class Logger {

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
}
