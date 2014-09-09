package org.granitemc.granite.api.plugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.granitemc.granite.api.GraniteAPI;
import org.granitemc.granite.api.SetupMethod;
import org.granitemc.granite.api.events.PreloadEvent;
import org.granitemc.granite.api.plugin.IPlugin.ServerState;
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

@SuppressWarnings("ReflectionForUnavailableAnnotation")
public class PluginContainer {

    private String name;
    private String id;
    private String version;
    private Class<?> setupClass;
    private Class<?> pluginClass;

    public void loadFromPlugin(Class<?> pluginClass) {
        IPlugin classPlugin;
        try {
        	classPlugin = GraniteAPI.instance().getClassPlugin(pluginClass);
            setName(classPlugin.getName());
            setID(classPlugin.getID());
            setVersion(classPlugin.getVersion());
            setSetupClass(classPlugin.getSetupClass());
        }catch(NullPointerException e) {
        	Logger.error("A derp in the Plugin loader has caused the class %s to be mistakenly identified as a plugin. Please remain calm. ", pluginClass);
        } catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
        setPluginClass(pluginClass);

       
    }

    private void setID(String id2) {
        id = id2;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    private void setName(String name2) {
        name = name2;
    }

    /**
     * @return the version
     */
    public String getVersion() {
        return version;
    }


    private void setVersion(String version) { 
        this.version = version;
    }

    /**
     * @return the setupClass
     */
    public Class<?> getSetupClass() {
        return setupClass;
    }

    private void setSetupClass(Class<?> setupClass) {
        this.setupClass = setupClass;
    }

    public void setup() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
        if (getSetupClass() != null) {
            runSetupFunctions(getSetupClass());
        }
        for(Method m : pluginClass.getDeclaredMethods()) {
        	if(m.isAnnotationPresent(IPlugin.On.class)
        	&& Arrays.deepEquals(m.getParameterTypes(), new Object[]{PreloadEvent.class})
        	&& ((IPlugin.On)m.getAnnotation(IPlugin.On.class)).value() == ServerState.PRESTART) {
        		
        		m.invoke(new PreloadEvent(this));
        		
        	}
        }

    }

    private void runSetupFunctions(Class<?> setupClass2) {
        List<Method> methods = Arrays.asList(setupClass2.getDeclaredMethods());
        List<Method> setupfuncs = new ArrayList<Method>();
        for (Method method : methods) {
            if (method.getAnnotation(SetupMethod.class) != null) {
                setupfuncs.add(method);
            }
        }

        for (Method method : setupfuncs) {
            Class<?>[] params = method.getParameterTypes();
            if (!Arrays.deepEquals(params, new Object[]{GraniteAPI.class})) {
                Logger.info("Found method %s that does not use a single GraniteAPI parameter, instead it uses %s.", method, Arrays.deepToString(params));
                Logger.info("Skipping method %s.", method);
            } else {
                try {
                    method.invoke(setupClass2.newInstance(), GraniteAPI.instance());
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @return the pluginClass
     */
    public Class<?> getPluginClass() {
        return pluginClass;
    }

    /**
     * @param pluginClass the pluginClass to set
     */
    private void setPluginClass(Class<?> pluginClass) {
        this.pluginClass = pluginClass;
    }
}
