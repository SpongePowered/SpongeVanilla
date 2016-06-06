/*
 * This file is part of SpongeVanilla, licensed under the MIT License (MIT).
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
package org.spongepowered.server.client.launch;

import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class DebugClientTweaker implements ITweaker {

    private String[] args;

    @Override
    public void acceptOptions(List<String> args, File gameDir, File assetsDir, String profile) {
        args = new ArrayList<>(args);

        Collections.addAll(args,
                "--assetsDir", assetsDir.getAbsolutePath(),
                "--version", profile,
                "--server", "127.0.0.1");

        this.args = args.toArray(new String[args.size()]);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void injectIntoClassLoader(LaunchClassLoader classLoader) {
        List<String> tweakers = (List<String>) Launch.blackboard.get("TweakClasses");
        tweakers.add("org.spongepowered.server.launch.VanillaServerTweaker");
    }

    @Override
    public String getLaunchTarget() {
        return "org.spongepowered.server.client.DebugLauncher";
    }

    @Override
    public String[] getLaunchArguments() {
        return this.args;
    }

}
