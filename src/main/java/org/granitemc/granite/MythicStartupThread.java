package org.granitemc.granite;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.jar.JarFile;

import org.granitemc.granite.api.PluginLoader;
import org.granitemc.granite.utils.ClassLoader;
import org.granitemc.granite.utils.Logger;
import org.granitemc.granite.utils.ServerComposite;

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
        
        File plugins = new File("plugins");
        for(File plugin : plugins.listFiles(new FilenameFilter() {	
			@Override
			public boolean accept(File arg0, String arg1) {
				return arg0.getName().endsWith(".jar");
			}
		})){
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
