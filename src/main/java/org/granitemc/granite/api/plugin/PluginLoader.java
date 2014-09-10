package org.granitemc.granite.api.plugin;

import org.granitemc.granite.api.GraniteAPI;
import org.granitemc.granite.utils.Logger;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

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

public class PluginLoader {
    Logger.PluginLogger log = new Logger.PluginLogger("PluginLoader");
    File f;

    public PluginLoader(File f) throws IllegalArgumentException {
        this.f = f;
    }

    private void doLoading(Class<?> clazz) {
        PluginContainer container = GraniteAPI.instance().loadClassPlugin(clazz);
        try {
            container.setup();
            Logger.info("Loaded %s (v%s)!", container.getName(), container.getVersion());
        } catch (IllegalAccessException | IllegalArgumentException
                | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("static-method")
    private List<Class<?>> iterateJar(File f, JarFile jf) {
        List<Class<?>> ret = new ArrayList<Class<?>>();
        //List<JarEntry> peeked = new ArrayList<JarEntry>();

        JarEntry entry;
        Enumeration<JarEntry> entries = jf.entries();

        URLClassLoader cl = null;
        try {
            cl = new URLClassLoader(new URL[]{f.toURI().toURL()});
        } catch (MalformedURLException ignored) {
        }

        while ((entry = entries.nextElement()) != null) {
            log.info("Peeking %s.", entry);
            if (entry.getName().toLowerCase().endsWith(".class")) {
                log.info("Found class file %s.", entry.getName());
                try {
                    String className = entry.getName().replaceAll("/", ".").substring(0, entry.getName().lastIndexOf("."));
                    Class<?> clazz = cl != null ? cl.loadClass(className) : null;
                    if (clazz != null) {
                        ret.add(clazz);
                    }
                    break;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                //peeked.add(entry);
            }
        }
        return ret;
    }

    @SuppressWarnings("static-method")
    private List<Class<?>> findPlugins(List<Class<?>> candidates) {
        int i = 1;
        for (Class<?> clazz : candidates) {
            Logger.info("Candidate %s : %s", i, clazz);
            i++;
        }
        List<Class<?>> ret = new ArrayList<Class<?>>();
        for (Class<?> clazz : candidates) {
            if (clazz.getAnnotations() == null || clazz.getAnnotations().length == 0) {
                Logger.info("Class %s does not have any annotations present.", clazz);
                continue;
            }
            try {
                Logger.info("Searching for annotation @Plugin in %s", clazz);
                for (java.lang.annotation.Annotation a : clazz.getAnnotations()) {
                    Logger.info("Comparing %s to %s.", a.annotationType(), Plugin.class);
                    if (a.annotationType().equals(Plugin.class)) {
                        log.info("Loading plugin class %s.", clazz.getName());
                        ret.add(clazz);
                    }
                }
            } catch (Exception ignored) {

            }
        }
        return ret;
    }

    public void run() {
        JarFile jf = null;
        try {
            jf = new JarFile(f);
        } catch (IOException ignored) {
        }

        log.info("Finding class candidates");
        log.info("Total candidates possible : " + jf.size());
        List<Class<?>> candidates = iterateJar(f, jf);
        log.info("Finding actual mod classes");
        List<Class<?>> modClasses = findPlugins(candidates);
        log.info("Loading actual mod classes");
        for (Class<?> clazz : modClasses) {
            doLoading(clazz);
        }
    }
}