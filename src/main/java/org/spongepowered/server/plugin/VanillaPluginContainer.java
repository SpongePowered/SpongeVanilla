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
import org.spongepowered.common.inject.SpongeGuice;
import org.spongepowered.common.inject.plugin.ModularPluginModule;
import org.spongepowered.common.inject.plugin.PluginModule;
import org.spongepowered.common.plugin.PluginContainerExtension;
import org.spongepowered.plugin.meta.PluginMetadata;

import java.nio.file.Path;
import java.util.Optional;

final class VanillaPluginContainer extends MetaPluginContainer implements PluginContainerExtension {

    private final Optional<?> instance;
    private final Injector injector;

    VanillaPluginContainer(Injector injector, Class<?> pluginClass, PluginMetadata metadata, Optional<Path> source) {
        super(metadata, source);

        if (SpongeGuice.isModular(pluginClass)) {
            try {
                this.instance = Optional.of(pluginClass.newInstance());
            } catch (final IllegalAccessException | InstantiationException e) {
                throw new IllegalStateException("Could not construct instance of plugin '" + pluginClass + "'.");
            }
            this.injector = injector.createChildInjector(new ModularPluginModule(this, this.instance));
        } else {
            this.injector = injector.createChildInjector(new PluginModule(this, pluginClass));
            this.instance = Optional.of(this.injector.getInstance(pluginClass));
        }
    }

    @Override
    public Optional<?> getInstance() {
        return this.instance;
    }

    @Override
    public Injector getInjector() {
        return this.injector;
    }

}
