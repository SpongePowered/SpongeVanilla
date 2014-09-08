package com.mythicmc.mythic;

import java.io.File;
import java.io.IOException;

import com.mythicmc.mythic.utils.ClassLoader;
import com.mythicmc.mythic.utils.Logger;
import com.mythicmc.mythic.utils.ServerComposite;

public class MythicStartupThread extends Thread{
	private String[] args;
	public MythicStartupThread(String string, String[] args) {
		setName(string);
		this.args = args;
	}

	public void run() {
		Logger.infoc(getName(), "Starting Granite version @VERSION@");
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
        ServerComposite.create(args);

	}

}
