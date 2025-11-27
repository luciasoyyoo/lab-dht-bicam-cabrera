package factory_method;
import java.lang.reflect.InvocationTargetException;


public class FactoryLoader {

	public static Object getInstance(String className) throws ClassNotFoundException, IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		// Let exceptions propagate to caller instead of swallowing them (prevents null dereference)
		@SuppressWarnings("rawtypes")
		Class c = Class.forName(className);
		// use getDeclaredConstructor().newInstance() to avoid deprecated Class.newInstance()
		Object o = c.getDeclaredConstructor().newInstance();
		return o;
	}
}
