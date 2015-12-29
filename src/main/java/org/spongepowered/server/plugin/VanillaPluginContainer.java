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
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.inject.Injector;
import net.minecraft.launchwrapper.Launch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.event.SpongeEventFactory;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.common.SpongeImpl;
import org.spongepowered.common.guice.SpongePluginGuiceModule;
import org.spongepowered.common.plugin.SpongePluginContainer;
import org.spongepowered.server.util.CertificateHelper;

import java.security.cert.Certificate;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;

public class VanillaPluginContainer extends SpongePluginContainer {

    private final String id;
    private final String name;
    private final String version;
    private final Optional<Object> instance;
    private final Logger logger;
    private final String expectedFingerprint;
    private final List<String> sourceFingerprints = Lists.newArrayList();
    private boolean fingerprintPresent;

    private final Injector injector;

    public VanillaPluginContainer(Class<?> pluginClass) {
        Plugin info = pluginClass.getAnnotation(Plugin.class);
        this.id = info.id();
        this.name = info.name();
        this.version = info.version();
        this.expectedFingerprint = info.certificateFingerprint();
        this.logger = LoggerFactory.getLogger(this.id);

        Boolean deobfuscatedEnvironment = (Boolean) Launch.blackboard.get("vanilla.deobfuscatedEnvironment");
        this.fingerprintPresent = deobfuscatedEnvironment || this.expectedFingerprint.isEmpty();
        @Nullable Certificate[] certificates = pluginClass.getProtectionDomain().getCodeSource().getCertificates();
        if (!this.expectedFingerprint.isEmpty() && certificates != null && !deobfuscatedEnvironment) {
            ImmutableList.Builder<String> builder = ImmutableList.builder();
            for (Certificate certificate : certificates) {
                builder.add(CertificateHelper.getFingerprint(certificate));
            }

            ImmutableList<String> sourceFingerprints = builder.build();

            if (!sourceFingerprints.contains(this.expectedFingerprint)) {
                this.logger.error("The plugin '{}' is expecting signature '{}' for source '{}', however there is no signature "
                        + "matching that description", this.getId(), expectedFingerprint, this.getName());
                this.fingerprintPresent = false;
            } else {
                this.fingerprintPresent = true;
            }
        } else if (!this.expectedFingerprint.isEmpty() && deobfuscatedEnvironment) {
            this.logger.info("The plugin '{}' is expecting signature '{}', however we are in a deobfuscated environment",
                    this.getId(), this.expectedFingerprint, this.getName());
        }

        this.injector = SpongeImpl.getInjector().createChildInjector(new SpongePluginGuiceModule(this, pluginClass));
        this.instance = Optional.of(this.injector.getInstance(pluginClass));
    }

    void firePluginFingerprintViolationEvent() {
        if (!this.fingerprintPresent) {
            SpongeImpl.postEvent(SpongeEventFactory.createPluginFingerprintViolationEvent(Cause.of(SpongeImpl.getGame()),
                    this.expectedFingerprint, ImmutableSet.copyOf(this.sourceFingerprints), this));
        }
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
