package factory_method;

import java.lang.reflect.InvocationTargetException;


import evolutionary_algorithms.complement.Sampling;
import evolutionary_algorithms.complement.SamplingType;
import factory_interface.IFFSampling;




/**
 * FactorySampling - Class responsible for creating sampling strategies.
 */
public class FactorySampling implements IFFSampling {
    private Sampling sampling;
	/**
	 * createSampling - Creates an instance of Sampling based on the provided SamplingType.
	 * @param typesampling
	 * @return returns an instance of Sampling
	 */
	public Sampling createSampling(SamplingType typesampling) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		
		String className = "evolutionary_algorithms.complement." + typesampling.toString();
		sampling = (Sampling) FactoryLoader.getInstance(className);
		return sampling;
	}
}
