package factory_method;
import java.lang.reflect.InvocationTargetException;


/**
 * FactoryLoader - Class responsible for loading factory classes.
 */
public class FactoryLoader {
	/**
	 * FactoryLoader - Carga dinámicamente una clase por su nombre y crea una instancia.
	 * @param className
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public static Object getInstance(String className) throws ClassNotFoundException, IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		// Let exceptions propagate to caller instead of swallowing them (prevents null dereference)
		@SuppressWarnings("rawtypes")
		Class c = Class.forName(className);
		// use getDeclaredConstructor().newInstance() to avoid deprecated Class.newInstance()
		Object o = c.getDeclaredConstructor().newInstance();
		return o;
	}
}
