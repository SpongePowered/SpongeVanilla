package com.mythicmc.mythic;

import com.mythicmc.mythic.utils.Logger;
import com.mythicmc.mythic.utils.ServerComposite;
import com.mythicmc.mythic.utils.ClassLoader;

import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        //load the server jar into the default classloader
    	File mcJar =  new File("minecraft_server.jar");
    	Logger.info("Loading jar from %s into classpath.",mcJar.getAbsolutePath());
    
    	if(!mcJar.exists()) {
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
        
    	Logger.debug("Got mapping : %s that is %s.", "net.minecraft.server.dedicated.DedicatedServer", Mappings.getClassByHumanName("net.minecraft.server.dedicated.DedicatedServer").getName());


        //now that classes are loaded, let's attempt to create the composite layer
        ServerComposite.create(args);
    }
}
