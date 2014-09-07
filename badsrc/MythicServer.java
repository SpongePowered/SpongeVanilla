import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MythicServer {
	public static sn configurationManager = null;
	
	//some hacks to bypass protection - has to reach beyond virtual packaging limitations for obfuscated versions
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void install(){
		//for deobfuscated versions, just use this:
		//configurationManager = MinecraftServer.getServer().getConfigurationManager();
		
		//for obfuscated versions, its a bit trickier:
		Class serverClass;
		try {
			serverClass = Class.forName("net.minecraft.server.MinecraftServer");
			//XXX: obfuscation reference
		    Method getInstance = serverClass.getMethod("M");
		    Object instanceHandle = getInstance.invoke(serverClass);
			//XXX: obfuscation reference
		    Method getServerConfigurationManager = serverClass.getMethod("an");
		    Object serverConfigurationManager = getServerConfigurationManager.invoke(instanceHandle);
			//XXX: obfuscation reference
		    configurationManager = (sn) serverConfigurationManager;
		    MythicLogger.info("Installed configuration manager access.");
		    return;
		} catch (ClassNotFoundException e) {
			MythicLogger.error(e.getMessage());
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			MythicLogger.error(e.getMessage());
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			MythicLogger.error(e.getMessage());
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			MythicLogger.error(e.getMessage());
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			MythicLogger.error(e.getMessage());
			e.printStackTrace();
		} catch (SecurityException e) {
			MythicLogger.error(e.getMessage());
			e.printStackTrace();
		}
		MythicLogger.error("Failed to install configuration manager access.");
	}
	
	public static String[] getAllPlayerNames(){
		//XXX: obfuscation reference
		return configurationManager.g();
	}
	
	public static MythicPlayer getPlayerByName(String name){
		//XXX: obfuscation reference
		return new MythicPlayer(configurationManager.a(name));
	}
	
	public static String getMatchingPlayerName(String namePattern){
		String[] names = getAllPlayerNames();
		//pass 1: exact match seek
		for (String name : names){
		      if (name.equalsIgnoreCase(namePattern)) {
		         return name;
		      }
		}
		//pass 2: partial match seek
		String lowerCasePattern = namePattern.toLowerCase();
		for (String name : names){
			String lowerCaseName = name.toLowerCase();
		      if (lowerCaseName.contains(lowerCasePattern)) {
		         return name;
		      }
		}
		//failed
		return null;
	}
}
