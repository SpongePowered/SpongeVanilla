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
import org.spongepowered.plugin.meta.PluginDependency;
import org.spongepowered.plugin.meta.PluginMetadata;
import org.spongepowered.plugin.meta.version.DefaultArtifactVersion;
import org.spongepowered.plugin.meta.version.InvalidVersionSpecificationException;
import org.spongepowered.plugin.meta.version.VersionRange;
import org.spongepowered.server.launch.VanillaLaunch;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

public final class PluginCandidate {

    private String id;
    private final String pluginClass;
    private final PluginSource source;

    private PluginMetadata metadata;

    private boolean invalid;

    @Nullable private Set<PluginCandidate> dependencies;
    @Nullable private Set<PluginCandidate> requirements;
    private final Set<String> dependenciesWithUnknownVersion = new HashSet<>();
    @Nullable private Map<String, String> versions;
    @Nullable private Map<String, String> missingRequirements;

    PluginCandidate(String pluginClass, PluginSource source, PluginMetadata metadata) {
        this.pluginClass = checkNotNull(pluginClass, "pluginClass");
        this.source = checkNotNull(source, "source");
        this.metadata = checkNotNull(metadata, "metadata");
        this.id = metadata.getId();
    }

    public String getId() {
        return this.id;
    }

    void setId(String id) {
        this.id = id;
    }

    public String getPluginClass() {
        return this.pluginClass;
    }

    public PluginSource getSource() {
        return this.source;
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

        for (PluginDependency dependency : this.metadata.collectRequiredDependencies()) {
            final String id = dependency.getId();
            if (this.id.equals(id)) {
                VanillaLaunch.getLogger().warn("Plugin '{}' from {} requires itself to be loaded. "
                        + "This is redundant and can be removed from the dependencies.", this.id, this.source);
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

        Map<PluginDependency.LoadOrder, Set<PluginDependency>> dependencies = this.metadata.groupDependenciesByLoadOrder();

        collectOptionalDependencies(dependencies.get(PluginDependency.LoadOrder.BEFORE), loadedPlugins, candidates);

        // TODO: Dependencies to load after this plugin
        //collectOptionalDependencies(dependencies.get(PluginDependency.LoadOrder.AFTER), loadedPlugins, candidates);

        Set<PluginDependency> loadAfter = dependencies.get(PluginDependency.LoadOrder.AFTER);
        if (loadAfter != null && !loadAfter.isEmpty()) {
            this.invalid = true;
            VanillaLaunch.getLogger().error("Invalid dependency with load order AFTER on plugin '{}' from {}. "
                    + "This is currently not supported for Sponge plugins! Requested dependencies: {}",
                    this.id, this.source, loadAfter);
        }

        return isLoadable();
    }

    private void collectOptionalDependencies(@Nullable Iterable<PluginDependency> dependencies,
            Map<String, PluginContainer> loadedPlugins, Map<String, PluginCandidate> candidates) {
        if (dependencies == null) {
            return;
        }

        for (PluginDependency dependency : dependencies) {
            final String id = dependency.getId();
            if (this.id.equals(id)) {
                VanillaLaunch.getLogger().error("Plugin '{}' from {} cannot have a dependency on itself. This is redundant and should be "
                        + "removed.", this.id, this.source);
                this.invalid = true;
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

        // Don't check version again if it already failed
        if (expectedRange.equals(this.missingRequirements.get(id))) {
            return false;
        }

        // Don't check version again if it was already checked
        if (expectedRange.equals(this.versions.get(id))) {
            return true;
        }

        if (version != null) {
            try {
                VersionRange range = VersionRange.createFromVersionSpec(expectedRange);
                DefaultArtifactVersion installedVersion = new DefaultArtifactVersion(version);
                if (range.containsVersion(installedVersion)) {
                    String currentRange = this.versions.get(id);
                    if (currentRange != null) {
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

                    if (range.getRecommendedVersion() instanceof DefaultArtifactVersion) {
                        BigInteger majorExpected = ((DefaultArtifactVersion) range.getRecommendedVersion()).getVersion().getFirstInteger();
                        if (majorExpected != null) {
                            BigInteger majorInstalled = installedVersion.getVersion().getFirstInteger();

                            // Show a warning if the major version does not match,
                            // or if the installed version is lower than the recommended version
                            if (majorInstalled != null
                                    && (!majorExpected.equals(majorInstalled) || installedVersion.compareTo(range.getRecommendedVersion()) < 0)) {
                                VanillaLaunch.getLogger().warn("Plugin {} from {} was designed for {} {}. It may not work properly.",
                                        this.id, this.source, id, range.getRecommendedVersion());
                            }
                        }

                    }

                    return true;
                }
            } catch (InvalidVersionSpecificationException e) {
                VanillaLaunch.getLogger().error("Failed to parse version range {} for dependency {} of plugin {} from {}: {}",
                        version, id, this.id, this.source, e.getMessage());
                this.invalid = true;
            }
        } else {
            if (this.dependenciesWithUnknownVersion.add(id)) {
                VanillaLaunch.getLogger().warn("Cannot check version of dependency {} for plugin {} from {}: Version of dependency unknown",
                        id, this.id, this.source);
            }
            return true;
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
                .add("source", this.source);
        if (this.invalid) {
            helper.addValue("INVALID");
        } else if (this.missingRequirements != null && !this.missingRequirements.isEmpty()) {
            helper.addValue("FAILED");
        }

        return helper.toString();
    }

}
