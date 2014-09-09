package org.granitemc.granite;

import org.granitemc.granite.api.PluginLoader;
import org.granitemc.granite.utils.ClassLoader;
import org.granitemc.granite.utils.Logger;
import org.granitemc.granite.utils.ServerComposite;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.jar.JarFile;

/**
 * License (MIT)
 *
 * Copyright (c) 2014. avarisc
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
 */

public class GraniteStartupThread extends Thread {

    private String[] args;

    public GraniteStartupThread(String string, String[] args) {
        setName(string);
        this.args = args;
    }

    public void run() {
        Logger.infoc(getName(), "Starting Granite version @VERSION@");
        File mcJar = new File("minecraft_server.jar");
        Logger.info("Loading jar from %s into classpath.", mcJar.getAbsolutePath());

        if (!mcJar.exists()) {
            throw new RuntimeException("Could not locate minecraft_server.jar. Is your jar named minecraft_server.1.8.jar?");
        }

        try {
            ClassLoader.addURL(mcJar.toURI().toURL());
            Logger.info("Loaded server: " + mcJar.getAbsolutePath());
        } catch (IOException e) {
            Logger.error("Failed to load minecraft_server.jar . Please make sure it exists in the same directory.");
            e.printStackTrace();
            return;
        }

        File plugins = new File("plugins");
        plugins.mkdirs();
        for (File plugin : plugins.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File arg0, String arg1) {
                return arg0.getName().endsWith(".jar");
            }
        })) {
            try {
                ClassLoader.addFile(plugin);
                new PluginLoader(new JarFile(plugin)).run();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ServerComposite.create(args);

    }

}
