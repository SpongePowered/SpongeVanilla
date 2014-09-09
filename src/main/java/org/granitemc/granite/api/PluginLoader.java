package org.granitemc.granite.api;

import org.granitemc.granite.utils.Logger;

import java.lang.reflect.InvocationTargetException;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PluginLoader {

    URLClassLoader loader = (URLClassLoader) java.lang.ClassLoader.getSystemClassLoader();
    Logger.PluginLogger log = new Logger.PluginLogger("pluginloader");
    JarFile f;

    public PluginLoader(JarFile f) throws IllegalArgumentException {
        this.f = f;
    }

    private void doLoading(Class<?> clazz) {
        PluginContainer container = GraniteAPI.instance().loadClassPlugin(clazz);
        try {
            container.setup();
        } catch (IllegalAccessException | IllegalArgumentException
                | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("static-method")
    private List<Class<?>> iterateJar(JarFile f) {
        List<Class<?>> ret = new ArrayList<Class<?>>();
        JarEntry entry;
        while ((entry = f.entries().nextElement()) != null) {
            if (entry.getName().toLowerCase().endsWith(".class")) {
                log.info("Found class file %s.", entry.getName());
                try {
                    ret.add(loader.loadClass(entry.getName().replace('/', '.').substring(0, entry.getName().lastIndexOf('.'))));
                    break;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return ret;
    }

    @SuppressWarnings("static-method")
    private Class<?>[] findPlugins(Class<?>[] candidates) {
        List<Class<?>> ret = new ArrayList<Class<?>>();
        for (Class<?> clazz : candidates) {
            try {
                if (clazz.isAnnotationPresent(Plugin.class)) {
                    log.info("Loading plugin class %s.", clazz.getName());
                    ret.add(clazz);
                }
            } catch (Exception e) {

            }
        }
        return ret.toArray(new Class<?>[10000]);
    }

    public void run() {

        log.info(" Finding class candidates");
        log.info(" Total candidates possible : " + f.size());
        Class<?>[] candidates = iterateJar(f).toArray(new Class[f.size()]);
        log.info(" Finding actual mod classes");
        Class<?>[] modClasses = findPlugins(candidates);
        log.info(" Loading actual mod classes");
        for (Class<?> clazz : modClasses) {
            doLoading(clazz);
        }
    }
}