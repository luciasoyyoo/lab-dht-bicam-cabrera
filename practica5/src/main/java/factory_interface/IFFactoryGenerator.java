package factory_interface;

import java.lang.reflect.InvocationTargetException;

import metaheuristics.generators.Generator;
import metaheuristics.generators.GeneratorType;

/**
 * IFFactoryGenerator - Interface for creating generator instances.
 */
public interface IFFactoryGenerator {
	
	Generator createGenerator(GeneratorType Generatortype)throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException ;
}
