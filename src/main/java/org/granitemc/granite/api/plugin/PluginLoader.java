package org.granitemc.granite.api.plugin;

import java.lang.reflect.InvocationTargetException;		
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.granitemc.granite.api.GraniteAPI;
import org.granitemc.granite.utils.Logger;

/**
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
 */

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
        List<JarEntry> peeked = new ArrayList<JarEntry>();

        JarEntry entry;
        while ((entry = f.entries().nextElement()) != null) {
        	log.info("Peeking %s.", entry);
            if (entry.getName().toLowerCase().endsWith(".class")) {
                log.info("Found class file %s.", entry.getName());
                try {
                    ret.add(loader.loadClass(entry.getName().replace('/', '.').substring(0, entry.getName().lastIndexOf('.'))));
                    break;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                peeked.add(entry);
            }else if(entry.isDirectory() || entry.getName().endsWith(".")){
            	continue;
            }else {
            	continue;
            }
        }
        return ret;
    }

    @SuppressWarnings("static-method")
    private Class<?>[] findPlugins(Class<?>[] candidates) {
    	int i = 1;
    	for(Class<?> clazz : candidates) {
    		Logger.info("Candidate %s : %s", i, clazz);
    		i++;
    	}
        List<Class<?>> ret = new ArrayList<Class<?>>();
        for (Class<?> clazz : candidates) {
        	if(clazz.getInterfaces().length == 0 || clazz.getInterfaces() == null) {
        		Logger.info("Class %s does not have any interfaces present.", clazz);
        		continue;
        	}
            try {
            	Logger.info("Searching for interface IPlugin in %s", clazz);
            	for(Class<?> class1 : clazz.getInterfaces()) {
            		if(class1.equals(IPlugin.class)) {
                		ret.add(class1);
                		Logger.info("Found IPlugin in class %s", clazz);
            		}else {
            			Logger.info("Didn't find IPlugin in class %s, but I found %s", clazz, class1);
            		}
            	}
                
            } catch (Exception e) {

            }
        }
        return ret.toArray(new Class<?>[10000]);
    }

    public void run() {
    	
        log.info("Finding class candidates");
        log.info("Total candidates possible : " + f.size());
        Class<?>[] candidates = iterateJar(f).toArray(new Class[f.size()]);
        log.info("Finding actual mod classes");
        Class<?>[] modClasses = findPlugins(candidates);
        log.info("Loading actual mod classes");
        if(modClasses.length == 0 || modClasses == null) {
        	Logger.info("No plugins found in %s.", f);
        	return;
        }
        for (Class<?> clazz : modClasses) {
            doLoading(clazz);
        }
    }
}