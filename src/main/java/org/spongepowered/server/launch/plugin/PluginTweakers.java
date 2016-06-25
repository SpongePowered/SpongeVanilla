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

import static com.google.common.base.Strings.isNullOrEmpty;
import static org.spongepowered.asm.util.Constants.ManifestAttributes.MIXINCONFIGS;
import static org.spongepowered.asm.util.Constants.ManifestAttributes.TOKENPROVIDERS;
import static org.spongepowered.asm.util.Constants.ManifestAttributes.TWEAKER;

import com.google.common.base.Splitter;
import com.google.common.collect.Sets;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.server.launch.VanillaLaunch;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.jar.Attributes;

final class PluginTweakers {

    private static final Splitter CONFIG_SPLITTER = Splitter.on(',');
    private static final Splitter PHASE_SPLITTER = Splitter.on('@').limit(2);

    private static final String MIXIN_TWEAKER = "org.spongepowered.asm.launch.MixinTweaker";

    private PluginTweakers() {
    }

    static Set<String> findMixinConfigs(Path path, Attributes attributes) {
        String tweakClass = attributes.getValue(TWEAKER);
        String configs = attributes.getValue(MIXINCONFIGS);

        if (isNullOrEmpty(tweakClass)) {
            if (!isNullOrEmpty(configs)) {
                VanillaLaunch.getLogger().warn("Plugin {} defines mixin configs in its manifest but the tweak class is not set. "
                        + "Please add 'TweakClass' to your manifest and set it to '" + MIXIN_TWEAKER + "'.", path);
            }

            return null;
        }

        if (!tweakClass.equals(MIXIN_TWEAKER)) {
            VanillaLaunch.getLogger().warn("Skipping unsupported tweak class '{}' from plugin {}", tweakClass, path);
            return null;
        }

        if (isNullOrEmpty(configs)) {
            return null;
        }

        return Sets.newHashSet(CONFIG_SPLITTER.split(configs));
    }

    static Set<String> findTokenProviders(Attributes attributes) {
        String tokenProviders = attributes.getValue(TOKENPROVIDERS);
        if (isNullOrEmpty(tokenProviders)) {
            return null;
        }

        return Sets.newHashSet(CONFIG_SPLITTER.split(tokenProviders));
    }

    static void registerConfig(String config) {
        Mixins.addConfiguration(config);
    }

    static void registerTokenProvider(String provider) {
        if (provider.indexOf('@') != -1) {
            List<String> parts = PHASE_SPLITTER.splitToList(provider);
            MixinEnvironment.Phase phase = MixinEnvironment.Phase.forName(parts.get(1));
            if (phase != null) {
                MixinEnvironment.getEnvironment(phase).registerTokenProviderClass(parts.get(0));
            }
        } else {
            MixinEnvironment.getDefaultEnvironment().registerTokenProviderClass(provider);
        }
    }

}
