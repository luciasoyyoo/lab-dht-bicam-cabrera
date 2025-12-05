package factory_method;


import java.lang.reflect.InvocationTargetException;

import factory_interface.IFFactoryGenerator;

import metaheuristics.generators.Generator;
import metaheuristics.generators.GeneratorType;



/**
 * FactoryGenerator - Interface for creating generator strategies.
 */
public class FactoryGenerator implements IFFactoryGenerator {

	private Generator generator;
	
	/**
	 * createGenerator - Creates an instance of Generator based on the provided GeneratorType.
	 * @param generatorType 
	 * @return returns an instance of Generator
	 */
	public Generator createGenerator(GeneratorType generatorType) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		String className = "metaheuristics.generators." + generatorType.toString();
		generator = (Generator) FactoryLoader.getInstance(className);
		return generator;
	}
}
