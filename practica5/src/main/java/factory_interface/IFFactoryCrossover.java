package factory_interface;

import java.lang.reflect.InvocationTargetException;

import evolutionary_algorithms.complement.Crossover;
import evolutionary_algorithms.complement.CrossoverType;




/**
 * IFFactoryCrossover - Interface for creating crossover operators.
 */
public interface IFFactoryCrossover {
	Crossover createCrossover(CrossoverType Crossovertype)throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException ;
}
