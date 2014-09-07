import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class CompositeHelper {
	public static Class<?> getClass(String forName){
		try {
			return Class.forName("po");
		} catch (ClassNotFoundException e) {
			Logger.error("Failed to find class: " + forName);
			e.printStackTrace();
		}
		return null;
	}
	
	public static Method getMethod(Class<?> clazz, String name, Class<?>... parameters){
		try {
			return clazz.getMethod(name, parameters);
		} catch (NoSuchMethodException | SecurityException e) {
			Logger.error("Failed to find method: " + clazz.getCanonicalName() + ":" + name);
			e.printStackTrace();
		}
		return null;
	}
	
	public static Object invokeMethod(String m, Object obj, Class<?>[] parameters, Object[] arguments){
	    Method method = CompositeHelper.getMethod(obj.getClass(), m, parameters);

		try {
			return method.invoke(obj, arguments);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			Logger.error("Failed to invoke " + m + " on " + obj.getClass().getCanonicalName());
			e.printStackTrace();
			return null;
		}
	}
}
