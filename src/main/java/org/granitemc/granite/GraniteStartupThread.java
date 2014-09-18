package org.granitemc.granite;

/*****************************************************************************************
 * License (MIT)
 *
 * Copyright (c) 2014. Granite Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the
 * Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 ****************************************************************************************/

import org.granitemc.granite.api.Granite;
import org.granitemc.granite.reflect.GraniteServerComposite;
import org.granitemc.granite.utils.ClassLoader;
import org.granitemc.granite.utils.Config;
import org.granitemc.granite.utils.Mappings;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

public class GraniteStartupThread extends Thread {

    private String[] args;

    public GraniteStartupThread(String[] args) {
        setName("Granite Startup");
        this.args = args;
    }

    public void run() {
        GraniteAPI.init();

        Granite.getLogger().info("Starting Granite version @VERSION@");
        Granite.getLogger().info("Loading jar from %s into classpath.", Config.mcJar.getAbsolutePath());

        Config.initDirs();

        // Load MC
        if (!Config.mcJar.exists()) {
            throw new RuntimeException("Could not locate minecraft_server.jar. Is your jar named minecraft_server.1.8.jar?");
        }

        try {
            ClassLoader.addURL(Config.mcJar.toURI().toURL());
            Granite.getLogger().info("Loaded server: " + Config.mcJar.getAbsolutePath());
        } catch (IOException e) {
            Granite.getLogger().error("Failed to load minecraft_server.jar. Please make sure it exists in the same directory.");
            e.printStackTrace();
            return;
        }

        // Load plugins
        for (File plugin : Config.pluginsFolder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File arg0, String arg1) {
                return arg1.endsWith(".jar");
            }
        })) {
            Granite.getLogger().info("Loading jarfile plugins/%s.", plugin.getName());
            Granite.loadPluginFromJar(plugin);
        }


        // Start the server
        GraniteServerComposite.init();
    }
}
