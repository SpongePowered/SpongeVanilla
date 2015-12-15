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
package org.spongepowered.server.plugin;

import com.google.inject.Injector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.common.SpongeImpl;
import org.spongepowered.common.guice.SpongePluginGuiceModule;
import org.spongepowered.common.plugin.SpongePluginContainer;

import java.util.Optional;

public class VanillaPluginContainer extends SpongePluginContainer {

    private final String id;
    private final String name;
    private final String version;
    private final Optional<Object> instance;
    private final Logger logger;

    private final Injector injector;

    public VanillaPluginContainer(Class<?> pluginClass) {
        Plugin info = pluginClass.getAnnotation(Plugin.class);
        this.id = info.id();
        this.name = info.name();
        this.version = info.version();
        this.logger = LoggerFactory.getLogger(this.id);

        this.injector = SpongeImpl.getInjector().createChildInjector(new SpongePluginGuiceModule(this, pluginClass));
        this.instance = Optional.of(this.injector.getInstance(pluginClass));
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getVersion() {
        return this.version;
    }

    @Override
    public Optional<Object> getInstance() {
        return this.instance;
    }

    @Override
    public Logger getLogger() {
        return this.logger;
    }

    @Override
    public Injector getInjector() {
        return this.injector;
    }

}
