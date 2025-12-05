package factory_interface;

import java.lang.reflect.InvocationTargetException;

import evolutionary_algorithms.complement.Sampling;
import evolutionary_algorithms.complement.SamplingType;




/**
 * IFFSampling - Interface for creating sampling strategies.
 */
public interface IFFSampling {
	Sampling createSampling(SamplingType typesampling) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException;
}
