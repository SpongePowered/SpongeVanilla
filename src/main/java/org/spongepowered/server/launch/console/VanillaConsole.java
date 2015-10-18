/*
 * This file is part of Sponge, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.spongepowered.server.launch.console;

import static com.google.common.base.Preconditions.checkState;
import static java.lang.Boolean.parseBoolean;
import static java.lang.System.getProperty;
import static jline.TerminalFactory.JLINE_TERMINAL;
import static jline.TerminalFactory.OFF;
import static jline.console.ConsoleReader.RESET_LINE;

import com.mojang.util.QueueLogAppender;
import jline.console.ConsoleReader;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.fusesource.jansi.AnsiConsole;

import java.io.IOException;

public final class VanillaConsole {

    private VanillaConsole() {
    }

    private static final boolean REDIRECT_OUT = parseBoolean(getProperty("sponge.logging.redirect", "true"));

    private static ConsoleReader reader;
    private static Formatter formatter;

    public static ConsoleReader getReader() {
        checkState(reader != null, "VanillaConsole was not initialized");
        return reader;
    }

    private static void disable() {
        System.setProperty(JLINE_TERMINAL, OFF);
    }

    public static void start() {
        if (reader != null) {
            return;
        }

        if (System.console() != null) {
            try {
                AnsiConsole.systemInstall();
                reader = new ConsoleReader();
                reader.setExpandEvents(false);
            } catch (Exception e) {
                LogManager.getLogger("Sponge").error("Failed to initialize jline terminal. Falling back to default");
            }
        }

        if (reader == null) {
            System.setProperty("log4j.skipJansi", "true");

            try {
                disable();
                reader = new ConsoleReader();
            } catch (IOException e1) {
                throw new RuntimeException("Failed to initialize console", e1);
            }
        }

        if (REDIRECT_OUT) {
            System.setOut(new LoggingPrintStream(LogManager.getLogger("System.OUT"), Level.INFO));
            System.setErr(new LoggingPrintStream(LogManager.getLogger("System.ERR"), Level.ERROR));
        }

        Thread thread = new Thread(new Writer(), "Sponge Console Thread");
        thread.setDaemon(true);
        thread.start();
    }

    public static void setFormatter(Formatter formatter) {
        VanillaConsole.formatter = formatter;
    }

    public interface Formatter {

        String format(String text);
    }

    private static class Writer implements Runnable {

        @Override
        public void run() {
            String message;

            while (true) {
                message = QueueLogAppender.getNextLogEvent("VanillaConsole");
                if (message == null) {
                    continue;
                }

                if (formatter != null) {
                    message = formatter.format(message);
                }

                try {
                    reader.print(RESET_LINE + message);
                    reader.drawLine();
                    reader.flush();
                } catch (IOException ignored) {
                }
            }
        }
    }

}
