package org.granitemc.granite.utils;

import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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

public class ServerComposite {
    static ProxyFactory serverFactory = null;
    static Class<?> dedicatedServerClass = null;
    static Class<?> commandHandlerClass = null;
    static Object server = null;
    static Field fieldServerCommandManager = null;

    public static void create(String[] args) {
        /*
         * Our goals in this process are this:
         * 1.) Create a server proxy that intercepts method invocation on MinecraftServer
         * 2.) Create a command proxy that intercepts method invocation on ServerCommandManager
         */
        // get a handle on the command proxy target class
        commandHandlerClass = Mappings.getClassByHumanName("net.minecraft.command.ServerCommandHandler");
        //first, lets get a handle for the proxy's supersuper commandHandler field
        try {
            for (Field field : Class.forName("net.minecraft.server.MinecraftServer").getDeclaredFields()) {
                if(field.getType() == Class.forName("ad")){
                    fieldServerCommandManager = field;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //fieldServerCommandManager = Mappings.getField("net.minecraft.server.MinecraftServer", "commandHandler");
        //and reset access flags so that it is not final or private at instanciation
        fieldServerCommandManager.setAccessible(true);

        //attempt to locate the DedicatedServer class
        dedicatedServerClass = Mappings.getClassByHumanName("net.minecraft.server.dedicated.DedicatedServer");

        //create the server proxy method invocation handler
        MethodHandler serverHandler = new MethodHandler() {
            @Override
            public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) {
                if(thisMethod.getReturnType() == Mappings.getClassByHumanName("net.minecraft.server.MinecraftServer")){
                    return server;
                } else {
                    try {
                        return proceed.invoke(self, args);
                    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                        Logger.error("Failed to invoke " + proceed);
                        e.printStackTrace();
                    }
                }
                return null;
            }
        };

        //start bootstrapping the server
        Mappings.call(null, "net.minecraft.init.Bootstrap", "func_151354_b");

        //prepare commandline arguments
        String worldsDirectory = ".";

        //finally, create the server proxy class and store it at the server field for now, we'll start it in a moment
        try {
            serverFactory = new ProxyFactory();
            serverFactory.setSuperclass(dedicatedServerClass);
            server = serverFactory.create(new Class[]{File.class}, new Object[]{new File(worldsDirectory)}, serverHandler);
        } catch (NoSuchMethodException | IllegalArgumentException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            Logger.error("Failed to instantiate server.");
            e.printStackTrace();
            return;
        }
        //install the command proxy
        try {
            Field commandProxyDSField = server.getClass().getSuperclass().getSuperclass().getDeclaredField("p");
            commandProxyDSField.setAccessible(true);
            Object oldCommandManager = commandProxyDSField.get(server);
            Object cproxy = java.lang.reflect.Proxy.newProxyInstance(oldCommandManager.getClass().getClassLoader(), new Class[]{commandProxyDSField.getType()}, new CommandProxy(oldCommandManager));
            commandProxyDSField.set(Class.forName("net.minecraft.server.MinecraftServer").cast(server), cproxy);
        } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        //now, we invoke the various commandline argument settings on the server:
        //if (serverOwner != null) serverProxy.setServerOwner(serverOwner);
        //if (world != null) serverProxy.setFolderName(world);
        //if (serverPort >= 0) serverProxy.setServerPort(serverPort);
        //if (demo) serverProxy.setDemo(true);
        //if (bonusChest) serverProxy.canCreateBonusChest(true);
        //if (showGui && !GraphicsEnvironment.isHeadless()) serverProxy.setGuiEnabled();     

        // and start the server start thread
        try {
            Mappings.call(server, "net.minecraft.server.dedicated.DedicatedServer", "startServerThread");
        } catch (IllegalArgumentException | SecurityException | NullPointerException e) {
            Logger.error("Failed to start server thread.");
            e.printStackTrace();
        }

        //attempt to locate the ThreadServerShutdown class
        Object serverShutdownThread;
        try {
            serverShutdownThread = Mappings.getClassByHumanName("net.minecraft.server.ThreadServerShutdown").getDeclaredConstructor(String.class, dedicatedServerClass).newInstance("Server Shutdown Thread", server);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            Logger.error("Failed to load server shutdown thread class.");
            e.printStackTrace();
            return;
        }

        //add shutdown hook
        Runtime.getRuntime().addShutdownHook((Thread) serverShutdownThread);

        //end minecraft main
    }
}
