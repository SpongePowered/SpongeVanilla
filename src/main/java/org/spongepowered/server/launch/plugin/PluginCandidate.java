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

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import com.google.common.base.Objects;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.plugin.meta.PluginMetadata;
import org.spongepowered.plugin.meta.version.DefaultArtifactVersion;
import org.spongepowered.plugin.meta.version.InvalidVersionSpecificationException;
import org.spongepowered.plugin.meta.version.VersionRange;
import org.spongepowered.server.launch.VanillaLaunch;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Nullable;

public final class PluginCandidate {

    private final String id;
    private final String pluginClass;
    private final Optional<Path> source;

    private PluginMetadata metadata;

    private boolean invalid;

    @Nullable private Set<PluginCandidate> dependencies;
    @Nullable private Set<PluginCandidate> requirements;
    @Nullable private Map<String, String> versions;
    @Nullable private Map<String, String> missingRequirements;

    PluginCandidate(String pluginClass, @Nullable Path source, PluginMetadata metadata) {
        this.pluginClass = checkNotNull(pluginClass, "pluginClass");
        this.source = Optional.ofNullable(source);
        this.metadata = checkNotNull(metadata, "metadata");
        this.id = metadata.getId();
    }

    public String getId() {
        return this.id;
    }

    public String getPluginClass() {
        return this.pluginClass;
    }

    public Optional<Path> getSource() {
        return this.source;
    }

    public String getDisplaySource() {
        if (this.source.isPresent()) {
            return this.source.get().toString();
        }

        return "unknown";
    }

    public PluginMetadata getMetadata() {
        return this.metadata;
    }

    void setMetadata(PluginMetadata metadata) {
        this.metadata = checkNotNull(metadata, "metadata");
    }

    public boolean isInvalid() {
        return this.invalid;
    }

    public boolean isLoadable() {
        return !this.invalid && getMissingRequirements().isEmpty();
    }

    public boolean dependenciesCollected() {
        return this.dependencies != null;
    }

    private void ensureState() {
        checkState(dependenciesCollected(), "Dependencies not collected yet");
    }

    public Set<PluginCandidate> getDependencies() {
        ensureState();
        return this.dependencies;
    }

    public Set<PluginCandidate> getRequirements() {
        ensureState();
        return this.requirements;
    }

    public Map<String, String> getMissingRequirements() {
        ensureState();
        return this.missingRequirements;
    }

    public String getVersion(String id) {
        ensureState();
        return this.versions.get(id);
    }

    public boolean updateRequirements() {
        ensureState();
        if (this.requirements.isEmpty()) {
            return false;
        }

        Iterator<PluginCandidate> itr = this.requirements.iterator();
        while (itr.hasNext()) {
            final PluginCandidate candidate = itr.next();
            if (!candidate.isLoadable()) {
                itr.remove();
                this.missingRequirements.put(candidate.getId(), this.versions.get(candidate.getId()));
            }
        }

        return this.invalid || !this.missingRequirements.isEmpty();
    }

    public boolean collectDependencies(Map<String, PluginContainer> loadedPlugins, Map<String, PluginCandidate> candidates) {
        checkState(this.dependencies == null, "Dependencies already collected");

        if (loadedPlugins.containsKey(this.id)) {
            this.invalid = true;
        }

        this.dependencies = new HashSet<>();
        this.requirements = new HashSet<>();
        this.versions = new HashMap<>();
        this.missingRequirements = new HashMap<>();

        for (PluginMetadata.Dependency dependency : this.metadata.getRequiredDependencies()) {
            final String id = dependency.getId();
            if (this.id.equals(id)) {
                VanillaLaunch.getLogger().warn("Plugin '{}' from {} requires itself to be loaded. "
                        + "This is redundant and can be removed from the dependencies.", this.id, getDisplaySource());
                continue;
            }

            final String version = dependency.getVersion();

            PluginContainer loaded = loadedPlugins.get(id);
            if (loaded != null) {
                if (!verifyVersionRange(id, version, loaded.getVersion().orElse(null))) {
                    this.missingRequirements.put(id, version);
                }

                continue;
            }

            PluginCandidate candidate = candidates.get(id);
            if (candidate != null && verifyVersionRange(id, version, candidate.getMetadata().getVersion())) {
                this.requirements.add(candidate);
                continue;
            }

            this.missingRequirements.put(id, version);
        }

        collectOptionalDependencies(this.metadata.getLoadAfter(), loadedPlugins, candidates);

        // TODO: Load before dependencies
        //collectOptionalDependencies(this.metadata.getLoadBefore(), loadedPlugins, candidates, false);

        if (!this.metadata.getLoadBefore().isEmpty()) {
            //this.invalid = true;
            VanillaLaunch.getLogger().error("Invalid dependency with load order BEFORE on plugin '{}' from {}. "
                    + "This is currently not supported for Sponge plugins! Requested dependencies: {}",
                    this.id, getDisplaySource(), this.metadata.getLoadBefore());
        }

        return isLoadable();
    }

    private void collectOptionalDependencies(Iterable<PluginMetadata.Dependency> dependencies,
            Map<String, PluginContainer> loadedPlugins, Map<String, PluginCandidate> candidates) {
        for (PluginMetadata.Dependency dependency : dependencies) {
            final String id = dependency.getId();
            if (this.id.equals(id)) {
                VanillaLaunch.getLogger().error("Plugin '{}' from {} cannot have a dependency on itself. This is redundant and should be "
                        + "removed.", this.id, getDisplaySource());
                this.invalid = true;
                continue;
            }

            final String version = dependency.getVersion();

            PluginContainer loaded = loadedPlugins.get(id);
            if (loaded != null) {
                if (!verifyVersionRange(id, version, loaded.getVersion().orElse(null))) {
                    this.missingRequirements.put(id, version);
                }

                //if (allowLoaded) {
                /*} else {
                    VanillaLaunch.getLogger().error("Cannot have before dependency on loaded plugin '{}' from plugin '{}'", id, this.id);
                    this.invalid = true;
                }*/

                continue;
            }

            PluginCandidate candidate = candidates.get(id);
            if (candidate != null) {
                if (verifyVersionRange(id, version, candidate.getMetadata().getVersion())) {
                    this.dependencies.add(candidate);
                } else {
                    this.missingRequirements.put(id, version);
                }
            }
        }
    }

    private boolean verifyVersionRange(String id, @Nullable String expectedRange, @Nullable String version) {
        if (expectedRange == null) {
            return true;
        }

        if (version != null) {
            try {
                VersionRange range = VersionRange.createFromVersionSpec(expectedRange);
                if (range.containsVersion(new DefaultArtifactVersion(version))) {
                    String currentRange = this.versions.get(id);
                    if (currentRange != null) {
                        if (currentRange.equals(expectedRange)) {
                            return true;
                        }

                        // This should almost never happen because it means the plugin is
                        // depending on two different versions of another plugin

                        // We need to merge the ranges
                        VersionRange otherRange;
                        try {
                            otherRange = VersionRange.createFromVersionSpec(currentRange);
                        } catch (InvalidVersionSpecificationException e) {
                            throw new AssertionError(e); // Should never happen because we already parsed it once
                        }

                        expectedRange = otherRange.restrict(range).toString();
                    }

                    this.versions.put(id, expectedRange);

                    return true;
                }
            } catch (InvalidVersionSpecificationException e) {
                VanillaLaunch.getLogger().error("Failed to parse version range {} for dependency {} of plugin {} from {}: {}",
                        version, id, this.id, getDisplaySource(), e.getMessage());
                this.invalid = true;
            }
        }

        return false;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PluginCandidate candidate = (PluginCandidate) o;
        return this.id.equals(candidate.id);

    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    @Override
    public String toString() {
        Objects.ToStringHelper helper = Objects.toStringHelper(this)
                .omitNullValues()
                .add("id", this.id)
                .add("class", this.pluginClass)
                .add("source", this.source.orElse(null));
        if (this.invalid) {
            helper.addValue("INVALID");
        } else if (this.missingRequirements != null && !this.missingRequirements.isEmpty()) {
            helper.addValue("FAILED");
        }

        return helper.toString();
    }

}
