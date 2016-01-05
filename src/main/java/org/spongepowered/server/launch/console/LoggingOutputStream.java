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

import static com.google.common.base.Preconditions.checkNotNull;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class LoggingOutputStream extends ByteArrayOutputStream {

    private static final String SEPARATOR = System.getProperty("line.separator");

    private static final String[] ignoredPackages = {
            "java.",
            "kotlin.io."
    };

    private final String fqcn;
    private final Logger logger;
    private final Level level;
    boolean flush = true;

    public LoggingOutputStream(String fqcn, Logger logger, Level level) {
        this.fqcn = fqcn;
        this.logger = checkNotNull(logger, "logger");
        this.level = checkNotNull(level, "level");
    }

    @Override
    public void flush() throws IOException {
        if (!this.flush) {
            return;
        }

        String message = toString();
        reset();

        if (!message.isEmpty() && !message.equals(SEPARATOR)) {
            if (message.endsWith(SEPARATOR)) {
                message = message.substring(0, message.length() - SEPARATOR.length());
            }

            if (message.charAt(message.length() - 1) == '\n') {
                message = message.substring(0, message.length() - 1);
            }

            if (this.logger.isEnabled(level)) {
                if (this.fqcn != null) {
                    StackTraceElement location = calculateLocation(this.fqcn);
                    if (location != null) {
                        this.logger.log(this.level, "[" + location + "]: " + message);
                        return;
                    }
                }

                this.logger.log(this.level, message);
            }
        }
    }

    private static StackTraceElement calculateLocation(String fqcn) {
        // Calculate location
        StackTraceElement[] stackTrace = new Throwable().getStackTrace();
        StackTraceElement last = null;

        for (int i = stackTrace.length - 1; i > 0; i--) {
            String className = stackTrace[i].getClassName();
            if (fqcn.equals(className)) {
                return last;
            }

            // Ignore Kotlin
            boolean isIgnored = false;
            for (String ignored : ignoredPackages) {
                if (className.startsWith(ignored)) {
                    isIgnored = true;
                    break;
                }
            }

            if (!isIgnored) {
                last = stackTrace[i];
            }
        }

        return null;
    }

}
