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

import com.google.common.collect.ImmutableList;
import com.google.inject.Injector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.asset.Asset;
import org.spongepowered.common.SpongeImpl;
import org.spongepowered.common.guice.SpongePluginGuiceModule;
import org.spongepowered.common.plugin.AbstractPluginContainer;
import org.spongepowered.common.plugin.PluginContainerExtension;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;

public final class VanillaPluginContainer extends AbstractPluginContainer implements PluginContainerExtension {

    private final String id;
    private final String unqualifiedId;

    private final Optional<String> name;
    private final Optional<String> version;
    private final Optional<String> description;
    private final Optional<String> url;
    private final Optional<Path> assets;
    private final ImmutableList<String> authors;

    private final Optional<Path> source;

    private final Optional<?> instance;
    private final Logger logger;

    private final Injector injector;

    VanillaPluginContainer(String id, Class<?> pluginClass,
            @Nullable String name, @Nullable String version, @Nullable String description, @Nullable String url, List<String> authors,
            @Nullable String assets, Optional<Path> source) {
        this.id = id;
        this.unqualifiedId = getUnqualifiedId(id);

        this.name = Optional.ofNullable(name);
        this.version = Optional.ofNullable(version);
        this.description = Optional.ofNullable(description);
        this.url = Optional.ofNullable(url);
        this.assets = assets != null ? Optional.of(Paths.get(assets)) : Optional.empty();
        this.authors = ImmutableList.copyOf(authors);
        this.source = source;
        this.logger = LoggerFactory.getLogger(this.id);

        this.injector = SpongeImpl.getInjector().createChildInjector(new SpongePluginGuiceModule(this, pluginClass));
        this.instance = Optional.of(this.injector.getInstance(pluginClass));
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getUnqualifiedId() {
        return this.unqualifiedId;
    }

    @Override
    public String getName() {
        return this.name.orElse(this.unqualifiedId);
    }

    @Override
    public Optional<String> getVersion() {
        return this.version;
    }

    @Override
    public Optional<String> getDescription() {
        return this.description;
    }

    @Override
    public Optional<String> getUrl() {
        return this.url;
    }

    @Override
    public Optional<Path> getAssetDirectory() {
        return this.assets;
    }

    @Override
    public Optional<Asset> getAsset(String name) {
        // TODO: Default method fails here likely because it is initialized
        // before the Sponge class. Workarounds?
        return Sponge.getAssetManager().getAsset(this, name);
    }

    @Override
    public List<String> getAuthors() {
        return this.authors;
    }

    @Override
    public Optional<Path> getSource() {
        return this.source;
    }

    @Override
    public Optional<?> getInstance() {
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
