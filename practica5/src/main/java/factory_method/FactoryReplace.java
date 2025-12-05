package factory_method;

import java.lang.reflect.InvocationTargetException;


import evolutionary_algorithms.complement.Replace;
import evolutionary_algorithms.complement.ReplaceType;
import factory_interface.IFFactoryReplace;





/**
 * FactoryReplace - Class responsible for creating replacement strategies.
 */
public class FactoryReplace implements IFFactoryReplace {

private Replace replace;
	
	/**
	 * createReplace - Creates an instance of Replace based on the provided ReplaceType.
	 * @param typereplace 
	 * @return returns an instance of Replace
	 */
	public Replace createReplace( ReplaceType typereplace ) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		String className = "evolutionary_algorithms.complement." + typereplace.toString();
		replace = (Replace) FactoryLoader.getInstance(className);
		return replace;
	}
}
