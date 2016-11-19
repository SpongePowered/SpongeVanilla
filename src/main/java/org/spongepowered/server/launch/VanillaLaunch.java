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
package org.spongepowered.server.launch;

import static com.google.common.base.Preconditions.checkState;

import net.minecraft.launchwrapper.Launch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.common.SpongeImpl;
import org.spongepowered.server.launch.transformer.deobf.SrgRemapper;

import java.io.IOException;

public final class VanillaLaunch {

    private VanillaLaunch() {
    }

    public enum Environment {
        DEVELOPMENT,
        PRODUCTION
    }

    public static final Environment ENVIRONMENT = detectEnvironment();
    private static final Logger logger = LogManager.getLogger(SpongeImpl.ECOSYSTEM_NAME);
    private static SrgRemapper remapper = SrgRemapper.NONE;

    public static Logger getLogger() {
        return logger;
    }

    public static SrgRemapper getRemapper() {
        return remapper;
    }

    public static void setRemapper(SrgRemapper newRemapper) {
        checkState(remapper == SrgRemapper.NONE, "Remapper was already set");
        VanillaLaunch.remapper = newRemapper;
    }

    private static Environment detectEnvironment() {
        try {
            // If the dedicated server class exists in the de-obfuscated name, we're likely in dev env
            if (Launch.classLoader.getClassBytes("net.minecraft.server.dedicated.DedicatedServer") != null) {
                return Environment.DEVELOPMENT;
            }
        } catch (IOException ignored) {
        }

        return Environment.PRODUCTION;
    }

}
