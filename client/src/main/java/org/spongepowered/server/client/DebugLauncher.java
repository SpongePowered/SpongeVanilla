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
package org.spongepowered.server.client;

import io.netty.handler.timeout.IdleStateHandler;
import net.minecraft.launchwrapper.Launch;
import org.spongepowered.server.SpongeVanilla;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class DebugLauncher {

    private DebugLauncher() {
    }

    public static void main(String[] args) throws Exception {
        DebugClient.disableReadTimeoutHandler(IdleStateHandler.class);

        // Start server
        SpongeVanilla.main(new String[0]);

        // Start client in separate class loader so the classes don't conflict
        List<URL> urls = new ArrayList<>(Arrays.asList(Launch.classLoader.getURLs()));
        urls.removeIf(url -> url.getFile().contains("serverSrc"));

        URLClassLoader loader = new DebugClientClassloader(urls.toArray(new URL[urls.size()]));
        Class<?> main = Class.forName("org.spongepowered.server.client.DebugClientLauncher", false, loader);
        main.getMethod("main", String[].class).invoke(null, (Object) args);
    }

}
