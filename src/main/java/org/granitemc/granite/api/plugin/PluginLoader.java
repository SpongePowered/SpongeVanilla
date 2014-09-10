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

package org.granitemc.granite.api.plugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.granitemc.granite.api.GraniteAPI;
import org.granitemc.granite.api.commands.Command;
import org.granitemc.granite.api.commands.CommandContainer;
import org.granitemc.granite.api.commands.CommandInfo;
import org.granitemc.granite.utils.Logger;

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
            if (entry.getName().toLowerCase().endsWith(".class")) {
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
        List<Class<?>> ret = new ArrayList<Class<?>>();
        for (Class<?> clazz : candidates) {
            if (clazz.getAnnotations() == null || clazz.getAnnotations().length == 0) {
                continue;
            }
            try {
                for (java.lang.annotation.Annotation a : clazz.getAnnotations()) {
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

        List<Class<?>> modClasses = findPlugins(iterateJar(f, jf));
        for (Class<?> clazz : modClasses) {
            doLoading(clazz);
        }
        List<CommandContainer> commands = loadCommands(modClasses);
        for(CommandContainer command : commands) {
        	GraniteAPI.instance().addCommandContainer(command);
        }
    }

	/**
	 * @param modClasses
	 * @return
	 */
	private List<CommandContainer> loadCommands(List<Class<?>> modClasses) {
		List<CommandContainer> ret = new ArrayList<CommandContainer>();
		for(Class c : modClasses) {
			for(Method m : c.getDeclaredMethods()) {
				if(m.getAnnotation(Command.class) != null && Arrays.deepEquals(m.getParameterTypes(), new Object[]{CommandInfo.class})) {
					ret.add(new CommandContainer(m));
				}	
			}
		}
		return ret;
	}
}