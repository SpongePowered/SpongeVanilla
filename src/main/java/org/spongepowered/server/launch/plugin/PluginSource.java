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
package org.spongepowered.server.launch.plugin;

import net.minecraft.launchwrapper.Launch;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.Optional;

import javax.annotation.Nullable;

public final class PluginSource {

    public static final PluginSource CLASSPATH = new PluginSource();

    private final Optional<Path> source;
    private boolean onClasspath;

    private PluginSource() {
        this.source = Optional.empty();
        this.onClasspath = true;
    }

    PluginSource(Path path) {
        this.source = Optional.of(path);
    }

    public Optional<Path> getPath() {
        return this.source;
    }

    public void addToClasspath() {
        if (this.onClasspath) {
            return;
        }

        this.onClasspath = true;

        try {
            // Source should be always present at this point
            Launch.classLoader.addURL(this.source.get().toUri().toURL());
        } catch (MalformedURLException e) {
            throw new RuntimeException("Failed to add plugin " + this + " to classpath", e);
        }
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PluginSource other = (PluginSource) o;
        return this.source.equals(other.source);
    }

    @Override
    public int hashCode() {
        return this.source.hashCode();
    }

    @Override
    public String toString() {
        if (this.source.isPresent()) {
            return this.source.get().toString();
        }

        return "unknown";
    }

}
