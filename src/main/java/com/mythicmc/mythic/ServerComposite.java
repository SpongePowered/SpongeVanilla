package com.mythicmc.mythic;

import java.awt.GraphicsEnvironment;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;


public class ServerComposite {
	static Class<?> serverClass = null;
	static Object server = null;
	
	public static void create(String[] args){
		//attempt to locate the MinecraftServer class
		//XXX: Obfuscated reference: net.minecraft.server.dedicated.DedicatedServer
		serverClass = CompositeHelper.getClass("po");

	    //attempt to locate Bootstrap class
		//XXX: Obfuscated reference: net.minecraft.init.Bootstrap
		Class<?> bootstrapClass = CompositeHelper.getClass("od");
		//XXX: Obfuscated reference: net.minecraft.init.Bootstrap.func_151354_b
		CompositeHelper.invokeMethod("c", bootstrapClass, new Class[0], new Object[0]);

        boolean showGui = true;
        String serverOwner = null;
        String worldsDirectory = ".";
        String world = null;
        boolean demo = false;
        boolean bonusChest = false;
        int serverPort = -1;

        for (int argIndex = 0; argIndex < args.length; ++argIndex){
            String argument = args[argIndex];
            String argumentValue = argIndex == args.length - 1 ? null : args[argIndex + 1];
            boolean hasValue = false;

            if (!argument.equals("nogui") && !argument.equals("--nogui")){
                if (argument.equals("--port") && argumentValue != null){
                    hasValue = true;
                    try{ serverPort = Integer.parseInt(argumentValue); }
                    catch (NumberFormatException var13){}
                } else if (argument.equals("--singleplayer") && argumentValue != null){
                    hasValue = true;
                    serverOwner = argumentValue;
                }else if (argument.equals("--universe") && argumentValue != null){
                    hasValue = true;
                    worldsDirectory = argumentValue;
                }else if (argument.equals("--world") && argumentValue != null){
                    hasValue = true;
                    world = argumentValue;
                }else if (argument.equals("--demo")){
                    demo = true;
                }else if (argument.equals("--bonusChest")){
                    bonusChest = true;
                }
            }else showGui = false;

            if (hasValue) ++argIndex;

        }
        //creating proxy
        ProxyFactory factory = new ProxyFactory();
	    factory.setSuperclass(serverClass);
	    
	    MethodHandler handler = new MethodHandler() {
	        @Override
	        public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args){
	            //System.out.println("Handling " + thisMethod + " via the method handler");
	            try {
	            	return proceed.invoke(self, args);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					// TODO Auto-generated catch block
					Logger.error("Failed to invoke " + proceed);
					e.printStackTrace();
				}
	            return null;
	        }
	    };
	    
	    try {
			server = factory.create(new Class[]{File.class}, new Object[]{new File(worldsDirectory)}, handler);
		} catch (NoSuchMethodException | IllegalArgumentException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
			Logger.error("Failed to instanciate server.");
			e.printStackTrace();
			return;
		}
	    
        //these will need to be refactored to the obfuscated variants
        //if (serverOwner != null) serverProxy.setServerOwner(serverOwner);
        //if (world != null) serverProxy.setFolderName(world);
        //if (serverPort >= 0) serverProxy.setServerPort(serverPort);
        //if (demo) serverProxy.setDemo(true);
        //if (bonusChest) serverProxy.canCreateBonusChest(true);
        //if (showGui && !GraphicsEnvironment.isHeadless()) serverProxy.setGuiEnabled();     
	    
	    // fire up the server thread
		try{
			//XXX: Obfuscated reference: net.minecraft.server.MinecraftServer.startServerThread()
		    Method startServerThreadMethod = server.getClass().getMethod("B", new Class[0]);
		    startServerThreadMethod.invoke(server,  new Object[0]);
		} catch ( IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | IllegalAccessException e) {
			Logger.error("Failed to start server thread.");
			e.printStackTrace();
		}
		
		//attempt to locate the ThreadServerShutdown class
		Object serverShutdownThread;
	    try {
	    	//XXX: Obfuscated reference: net.minecraft.server.ThreadServerShutdown (name pending- not in mcp yet)
			serverShutdownThread = Class.forName("pe").getDeclaredConstructor(new Class[]{String.class, serverClass}).newInstance(new Object[]{"Server Shutdown Thread", server});
	    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e){
			Logger.error("Failed to load server shutdown thread class.");
			e.printStackTrace();
			return;
	    }
	    
        //add shutdown hook
        Runtime.getRuntime().addShutdownHook((Thread) serverShutdownThread);
    
        //end minecraft main
	}
}
