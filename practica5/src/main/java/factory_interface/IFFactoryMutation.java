package factory_interface;

import java.lang.reflect.InvocationTargetException;

import evolutionary_algorithms.complement.Mutation;
import evolutionary_algorithms.complement.MutationType;




/**
 * IFFactoryMutation - Interface for creating mutation strategies.
 */
public interface IFFactoryMutation {
	Mutation createMutation(MutationType typeMutation)throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException ;
}
