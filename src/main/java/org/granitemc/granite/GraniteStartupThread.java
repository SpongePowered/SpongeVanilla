package org.granitemc.granite;

import org.granitemc.granite.api.plugin.PluginLoader;
import org.granitemc.granite.utils.ClassLoader;
import org.granitemc.granite.utils.Config;
import org.granitemc.granite.utils.Logger;
import org.granitemc.granite.utils.ServerComposite;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

/**
 * License (MIT)
 * <p/>
 * Copyright (c) 2014. Granite Team
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the
 * Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

public class GraniteStartupThread extends Thread {

    private String[] args;

    public GraniteStartupThread(String[] args) {
        setName("Granite Startup");
        this.args = args;
    }

    public void run() {
        Logger.infoc(getName(), "Starting Granite version @VERSION@");
        Logger.info("Loading jar from %s into classpath.", Config.mcJar.getAbsolutePath());

        Config.initDirs();

        if (!Config.mcJar.exists()) {
            throw new RuntimeException("Could not locate minecraft_server.jar. Is your jar named minecraft_server.1.8.jar?");
        }

        try {
            ClassLoader.addURL(Config.mcJar.toURI().toURL());
            Logger.info("Loaded server: " + Config.mcJar.getAbsolutePath());
        } catch (IOException e) {
            Logger.error("Failed to load minecraft_server.jar . Please make sure it exists in the same directory.");
            e.printStackTrace();
            return;
        }

        for (File plugin : Config.pluginsFolder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File arg0, String arg1) {
                return arg1.endsWith(".jar");
            }
        })) {
            Logger.info("Loading jarfile plugins/%s.", plugin.getName());
            new PluginLoader(plugin).run();
        }


        ServerComposite.create(args);
    }

}
