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
package org.spongepowered.vanilla.plugin;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.spongepowered.vanilla.plugin.PluginScanner.ARCHIVE;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import net.minecraft.launchwrapper.Launch;
import org.slf4j.Logger;
import org.spongepowered.api.event.SpongeEventFactory;
import org.spongepowered.api.event.plugin.PluginLoadedEvent;
import org.spongepowered.api.event.plugin.PluginUnloadingEvent;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.plugin.PluginManager;
import org.spongepowered.api.service.event.EventManager;
import org.spongepowered.common.Sponge;

import java.io.File;
import java.io.IOException;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
public class VanillaPluginManager implements PluginManager {

    public static final String SCAN_CLASSPATH_PROPERTY = "sponge.plugins.scanClasspath";
    private static final boolean SCAN_CLASSPATH = Boolean.getBoolean(SCAN_CLASSPATH_PROPERTY);

	private final EventManager eventManager;
    private final Map<String, PluginContainer> plugins = Maps.newHashMap();
    private final Map<Object, PluginContainer> pluginInstances = Maps.newIdentityHashMap();

    @Inject
    public VanillaPluginManager(EventManager eventManager, @Named("Sponge") PluginContainer spongePlugin, @Named("Minecraft") PluginContainer minecraftPlugin) {
        this.eventManager = eventManager;
        registerPlugin(spongePlugin);
        registerPlugin(minecraftPlugin);
    }

    private void registerPlugin(PluginContainer plugin) {
        this.plugins.put(plugin.getId(), plugin);
        this.pluginInstances.put(plugin.getInstance(), plugin);
    }

    private void unregisterPlugin(PluginContainer plugin) {
        this.plugins.remove(plugin.getId());
        this.pluginInstances.remove(plugin.getInstance());    	
    }
    
    public void loadPlugins() throws IOException {
        Set<String> plugins;

        if (SCAN_CLASSPATH) {
            Sponge.getLogger().info("Scanning classpath for plugins...");

            // Find plugins on the classpath
            plugins = PluginScanner.scanClassPath(Launch.classLoader);
            if (!plugins.isEmpty()) {
                loadPlugins("classpath", plugins);
            }
        }

        for (File jar : Sponge.getPluginsDirectory().listFiles(ARCHIVE)) {
            // Search for plugins in the JAR
            plugins = PluginScanner.scanZip(jar);

            if (!plugins.isEmpty()) {
                // Add plugin to the classpath
                Launch.classLoader.addURL(jar.toURI().toURL());

                // Load the plugins
                loadPlugins(jar, plugins);
            }
        }
    }

	@Override
	public VanillaPluginContainer loadPlugin(Class<?> pluginClass, String source) {
        VanillaPluginContainer container = new VanillaPluginContainer(pluginClass);
        registerPlugin(container);
        Sponge.getGame().getEventManager().register(container, container.getInstance());
        eventManager.post(SpongeEventFactory.createPlugin(PluginLoadedEvent.class, container));
        Sponge.getLogger().info("Loaded plugin: {} {} (from {})", container.getName(), container.getVersion(), source);
		return container;
	}

    @Override
    public Set<PluginContainer> loadPlugins(URLClassLoader classLoader) {
    	String urls = Arrays.toString(classLoader.getURLs());
    	Set<String> plugins = PluginScanner.scanClassPath(classLoader);
        return loadPlugins("URLClassLoader:" + urls, plugins);
    }
    
    @Override
    public boolean unloadPlugin(PluginContainer container) { // TODO ? throws new IllegalStateException
    	if (!isLoaded(container.getId())) {
            Sponge.getLogger().error("Failed to unload plugin, as it wasn't loaded: {}", container);
            return false;
    	}
    	unregisterPlugin(container);
    	Sponge.getGame().getEventManager().unregister(container.getInstance());
        Sponge.getGame().getEventManager().unregisterPlugin(container);
        eventManager.post(SpongeEventFactory.createPlugin(PluginUnloadingEvent.class, container));
        Sponge.getLogger().info("Unloaded plugin: {} {}", container.getName(), container.getVersion());
		return true;
    }
    
    private Set<PluginContainer> loadPlugins(Object source, Iterable<String> plugins) {
        Set<PluginContainer> pluginContainers = Sets.newHashSet();
        for (String plugin : plugins) {
            try {
                Class<?> pluginClass = Class.forName(plugin);
                VanillaPluginContainer container = loadPlugin(pluginClass, source.toString());
                pluginContainers.add(container);
            } catch (Throwable e) {
                Sponge.getLogger().error("Failed to load plugin: {} (from {})", plugin, source, e);
            }
        }
        return Collections.unmodifiableSet(pluginContainers);
    }

    @Override
    public Optional<PluginContainer> fromInstance(Object instance) {
        checkNotNull(instance, "instance");

        if (instance instanceof PluginContainer) {
            return Optional.of((PluginContainer) instance);
        }

        return Optional.fromNullable(this.pluginInstances.get(instance));
    }

    @Override
    public Optional<PluginContainer> getPlugin(String id) {
        return Optional.fromNullable(this.plugins.get(id));
    }

    @Override
    public Logger getLogger(PluginContainer plugin) {
        return ((VanillaPluginContainer) plugin).getLogger();
    }

    @Override
    public Collection<PluginContainer> getPlugins() {
        return Collections.unmodifiableCollection(this.plugins.values());
    }

    @Override
    public boolean isLoaded(String id) {
        return this.plugins.containsKey(id);
    }

}
