package org.granitemc.granite.api.events;

import java.io.File;

import org.granitemc.granite.api.PluginContainer;
import org.granitemc.granite.events.Event;
import org.granitemc.granite.utils.Logger;

public class PreloadEvent extends Event {

	public PreloadEvent(PluginContainer c) {
		super(c, "plugin_preload",c);
	}
	
	public File getMinecraftJar() {
		return  new File("minecraft_server.jar");
	}
	
	public File getSuggestedConfigurationFile() {
		return new File("configuration/" +  ((PluginContainer)data[0]).getId() + ".conf");
	}
	
	public File getConfigDirectory() {
		return new File("configuration/");
	}
	
	public Logger.PluginLogger getPluginLog() {
		return new Logger.PluginLogger(((PluginContainer)data[0]).getId());
	}

}
