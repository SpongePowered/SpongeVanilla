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

import static jline.TerminalFactory.OFF;
import static jline.console.ConsoleReader.RESET_LINE;
import static org.apache.logging.log4j.core.helpers.Booleans.parseBoolean;
import static org.spongepowered.server.launch.VanillaCommandLine.FORCE_JLINE;
import static org.spongepowered.server.launch.VanillaCommandLine.NO_JLINE;
import static org.spongepowered.server.launch.VanillaCommandLine.NO_REDIRECT_STDOUT;

import jline.TerminalFactory;
import jline.console.ConsoleReader;
import joptsimple.OptionSet;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;
import org.spongepowered.server.launch.VanillaCommandLine;

import java.io.IOException;
import java.io.PrintStream;
import java.io.Serializable;
import java.io.Writer;
import java.util.function.Function;

import javax.annotation.Nullable;

@Plugin(name = "TerminalConsole", category = "Core", elementType = "appender", printObject = true)
public class TerminalConsoleAppender extends AbstractAppender {

    public static final String ANSI_RESET = Ansi.ansi().fgDefault().reset().toString();
    private static final String ANSI_ERROR = Ansi.ansi().fgRed().bold().toString();
    private static final String ANSI_WARN = Ansi.ansi().fgYellow().bold().toString();

    private static final boolean ENABLE_JLINE = PropertiesUtil.getProperties().getBooleanProperty("jline.enable", true);

    private static final PrintStream out = System.out;

    private static boolean initialized;
    @Nullable private static ConsoleReader reader;

    @Nullable
    public static ConsoleReader getReader() {
        return reader;
    }

    private static Function<String, String> formatter = Function.identity();

    public static void setFormatter(@Nullable Function<String, String> format) {
        formatter = format != null ? format : Function.identity();
    }

    protected TerminalConsoleAppender(String name, Filter filter, Layout<? extends Serializable> layout, boolean ignoreExceptions) {
        super(name, filter, layout, ignoreExceptions);
    }

    @PluginFactory
    public static TerminalConsoleAppender createAppender(
            @PluginAttribute("name") String name,
            @PluginElement("Filters") Filter filter,
            @PluginElement("Layout") @Nullable Layout<? extends Serializable> layout,
            @PluginAttribute("ignoreExceptions") String ignore) {

        if (name == null) {
            LOGGER.error("No name provided for TerminalConsoleAppender");
            return null;
        }
        if (layout == null) {
            layout = PatternLayout.createLayout(null, null, null, null, null);
        }

        boolean ignoreExceptions = parseBoolean(ignore, true);

        // This is handled by jline
        System.setProperty("log4j.skipJansi", "true");
        return new TerminalConsoleAppender(name, filter, layout, ignoreExceptions);
    }

    @Override
    public void start() {
        super.start();

        // We can start directly if our options are already present
        if (VanillaCommandLine.getOptions().isPresent()) {
            initialize();
        }
    }

    public static void initialize() {
        System.setProperty(ConsoleReader.JLINE_EXPAND_EVENTS, "false");

        // Initialize the reader if that hasn't happened yet
        if (!initialized) {
            initialized = true;

            OptionSet options = VanillaCommandLine.getOptions().get();

            if (ENABLE_JLINE && !options.has(NO_JLINE)) {
                final boolean hasConsole = System.console() != null;
                if (hasConsole) {
                    try {
                        AnsiConsole.systemInstall();
                        reader = new ConsoleReader();
                    } catch (Exception e) {
                        LOGGER.warn("Failed to initialize terminal. Falling back to default.", e);
                    }
                }

                if (reader == null) {
                    // Eclipse doesn't support colors and characters like \r so enabling jline2 on it will
                    // just cause a lot of issues with empty lines and weird characters.
                    // Enable jline2 only on IntelliJ IDEA to prevent that.
                    //      Also see: https://bugs.eclipse.org/bugs/show_bug.cgi?id=76936

                    if (hasConsole || options.has(FORCE_JLINE) ||  System.getProperty("java.class.path").contains("idea_rt.jar")) {
                        // Disable advanced jline features
                        TerminalFactory.configure(OFF);
                        TerminalFactory.reset();

                        try {
                            reader = new ConsoleReader();
                        } catch (Exception e) {
                            LOGGER.warn("Failed to initialize fallback terminal. Falling back to standard output console.", e);
                        }
                    } else {
                        LOGGER.warn("Disabling terminal, you're running in an unsupported environment.");
                    }
                }
            }

            if (!options.has(NO_REDIRECT_STDOUT)) {
                System.setOut(new LoggingPrintStream(System.out, LogManager.getLogger("STDOUT"), Level.INFO));
                System.setErr(new LoggingPrintStream(System.err, LogManager.getLogger("STDERR"), Level.ERROR));
            }
        }
    }

    @Override
    public void append(LogEvent event) {
        if (!initialized) {
            out.print(getLayout().toSerializable(event));
            return;
        }

        if (reader != null) {
            try {
                Writer out = reader.getOutput();
                out.write(RESET_LINE);
                out.write(formatEvent(event));

                reader.drawLine();
                reader.flush();
            } catch (IOException ignored) {
            }
        } else {
            out.print(formatEvent(event));
        }
    }

    protected String formatEvent(LogEvent event) {
        String formatted = formatter.apply(getLayout().toSerializable(event).toString());
        if (reader != null) {
            // Colorize log messages if supported
            final int level = event.getLevel().intLevel();
            if (level <= Level.ERROR.intLevel()) {
                return ANSI_ERROR + formatted + ANSI_RESET;
            } else if (level <= Level.WARN.intLevel()) {
                return ANSI_WARN + formatted + ANSI_RESET;
            }
        }
        return formatted;
    }

}
