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

import static org.apache.logging.log4j.core.util.Booleans.parseBoolean;

import org.apache.logging.log4j.Level;
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
import org.jline.reader.LineReader;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.function.Function;

import javax.annotation.Nullable;

@Plugin(name = "TerminalConsole", category = "Core", elementType = "appender", printObject = true)
public final class TerminalConsoleAppender extends AbstractAppender {

    public static final String ANSI_RESET = "\u001B[39;0m";
    private static final String ANSI_ERROR = "\u001B[31;1m";
    private static final String ANSI_WARN = "\u001B[33;1m";

    private static final PrintStream out = System.out;

    private static boolean initialized;
    @Nullable private static Terminal terminal;
    @Nullable private static LineReader reader;

    private static Function<String, String> formatter = Function.identity();

    @Nullable
    public static Terminal getTerminal() {
        return terminal;
    }

    @Nullable
    public static LineReader getReader() {
        return reader;
    }

    public static void setReader(@Nullable LineReader newReader) {
        if (newReader != null && newReader.getTerminal() != terminal) {
            throw new IllegalArgumentException("Reader was not created with TerminalConsoleAppender.getTerminal()");
        }
        reader = newReader;
    }

    public static void setFormatter(@Nullable Function<String, String> format) {
        formatter = format != null ? format : Function.identity();
    }

    private TerminalConsoleAppender(String name, Filter filter, Layout<? extends Serializable> layout, boolean ignoreExceptions) {
        super(name, filter, layout, ignoreExceptions);
        doInitialize();
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
            layout = PatternLayout.createLayout(null, null, null, null, null, true, false, null, null);
        }

        boolean ignoreExceptions = parseBoolean(ignore, true);
        return new TerminalConsoleAppender(name, filter, layout, ignoreExceptions);
    }

    private static void doInitialize() {
        // Initialize the reader if that hasn't happened yet
        if (!initialized) {
            initialized = true;

            if (PropertiesUtil.getProperties().getBooleanProperty("jline.enable", true)
                    && System.getProperty("FORGE_FORCE_FRAME_RECALC") == null) {
                try {
                    terminal = TerminalBuilder.builder().dumb(true).build();
                } catch (IOException e) {
                    LOGGER.error("Failed to initialize terminal. Falling back to standard output", e);
                }
            } else {
                // The property is set by ForgeGradle only for Eclipse.
                // Eclipse doesn't support colors and characters like \r so enabling jline on it will
                // just cause a lot of issues with empty lines and weird characters.
                //      Also see: https://bugs.eclipse.org/bugs/show_bug.cgi?id=76936
                LOGGER.warn("Disabling terminal, you're running in an unsupported environment.");
            }
        }
    }

    @Override
    public void append(LogEvent event) {
        if (terminal != null) {
            if (reader != null) {
                // Draw the prompt line again if a reader is available
                reader.callWidget(LineReader.CLEAR);
                terminal.writer().print(formatEvent(event));
                reader.callWidget(LineReader.REDRAW_LINE);
                reader.callWidget(LineReader.REDISPLAY);
            } else {
                terminal.writer().print(formatEvent(event));
            }

            terminal.writer().flush();
        } else {
            out.print(formatEvent(event));
        }
    }

    private String formatEvent(LogEvent event) {
        String formatted = formatter.apply(getLayout().toSerializable(event).toString());
        if (terminal != null) {
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
