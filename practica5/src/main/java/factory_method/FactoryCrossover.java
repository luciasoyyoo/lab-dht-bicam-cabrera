package factory_method;

import java.lang.reflect.InvocationTargetException;


import evolutionary_algorithms.complement.Crossover;
import evolutionary_algorithms.complement.CrossoverType;
import factory_interface.IFFactoryCrossover;




/**
 * FactoryCrossover - Interface for creating crossover strategies.
 */
public class FactoryCrossover implements IFFactoryCrossover {
	private Crossover crossing;

	/**
	 * createCrossover - Creates an instance of Crossover based on the provided CrossoverType.
	 * @param Crossovertype 
	 * @return returns an instance of Crossover
	 */
	public Crossover createCrossover(CrossoverType Crossovertype) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		
		String className = "evolutionary_algorithms.complement." + Crossovertype.toString();
		crossing = (Crossover) FactoryLoader.getInstance(className);
		return crossing;
	}
}
