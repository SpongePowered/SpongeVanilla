import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;


public class Mythic {
	/*
	 * Welcome to Mythic, the server mod developed for MythicMC (http://mythicmc.com/).
	 * This mod is developed due to the uncertainty regarding the future of Bukkit.
	 */
	private static MythicEventHooks eventHooks = null;
	private static MythicDatabase database = null;
	
	public static MythicEventHooks getEventManager(){
		if(eventHooks == null) install();
		return eventHooks;
	}
	
	public static void install(){
		
		//get a list of libraries from ./lib/
		File dir = new File("./lib/");
		File [] files = dir.listFiles(new FilenameFilter() {
		    @Override
		    public boolean accept(File dir, String name) {
		        return name.endsWith(".jar");
		    }
		});

		//install libraries
		for (File jarfile : files) {
			try {
				MythicClassLoader.addURL(jarfile.toURI().toURL());
				MythicLogger.info("Loaded library: " + jarfile.getName());
			} catch (IOException e) {
				MythicLogger.error("Failed to load library: " + jarfile.getName());
				e.printStackTrace();
			}
		}
		
		//gain access to configuration manager
		MythicServer.install();
		
		//setup database
		database = new MythicDatabase();
		
		//create event hooks
		eventHooks = new MythicEventHooks();
		
		MythicEssentials.onInstall();
		
	}
	
	public static MythicDatabase getDatabase(){
		if(database != null && database.isAvailable()){
			return database;
		} else {
			MythicLogger.error("Database is not available.");
			return null;
		}
	}
	
	public static void uninstall(){
		MythicEssentials.onUninstall();
		if(database != null){
			if(database.isAvailable()){
				database.close();
				MythicLogger.info("Closed database.");
			}
		}
	}
	
	public static ho getFormattedName(ahd playerEntity){
		if(playerEntity == null) return new hu("<NameError>");
		String playername = playerEntity.d_();
		String prefix = "";
		String suffix = "";
		if(playername.equalsIgnoreCase("Ripsand") || playername.equalsIgnoreCase("Zeldagx") || playername.equalsIgnoreCase("Glitchfinder")){
			prefix += "§c[Admin]§f ";
		} else if(playername.equalsIgnoreCase("taon2") || playername.equalsIgnoreCase("ImSoFancyMC") || playername.equalsIgnoreCase("aldeberon_29") || playername.equalsIgnoreCase("Laefy")){
			prefix += "§b[Moderator]§f ";
		} else {
			prefix += "§2[Member]§f ";
		}
		return new hu(prefix + playername + suffix);
	}
}
