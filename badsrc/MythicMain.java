import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


public class MythicMain {
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void main(String [] args)
	{
		try {
			File jarfile = new File("./minecraft_server.jar");
			MythicClassLoader.addURL(jarfile.toURI().toURL());
			MythicLogger.info("Loaded server: " + jarfile.getName());
		} catch (IOException e) {
			MythicLogger.error("Failed to load minecraft_server.jar . Please make sure it exists in the same directory.");
			e.printStackTrace();
		}
		Class serverClass = null;
	    try {
			serverClass = Class.forName("net.minecraft.server.MinecraftServer");
	    } catch (ClassNotFoundException e){
			MythicLogger.error("Failed to load server class.");
			e.printStackTrace();
	    }
		ServerInvocationHandler handler = new ServerInvocationHandler(serverClass, args);
		Object proxy = Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(),new Class[] { serverClass },handler);
		handler.start();
	}
}