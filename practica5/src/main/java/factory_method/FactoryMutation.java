package factory_method;

import java.lang.reflect.InvocationTargetException;


import evolutionary_algorithms.complement.Mutation;
import evolutionary_algorithms.complement.MutationType;
import factory_interface.IFFactoryMutation;




/**
 * FactoryMutation - Class responsible for creating mutation strategies.
 */
public class FactoryMutation implements IFFactoryMutation {
	private Mutation mutation;

	/**
	 * createMutation - Creates an instance of Mutation based on the provided MutationType.
	 * @param typeMutation 
	 * @return returns an instance of Mutation
	 */
	public Mutation createMutation(MutationType typeMutation) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		
		String className = "evolutionary_algorithms.complement." + typeMutation.toString();
		mutation = (Mutation) FactoryLoader.getInstance(className);
		return mutation;
	}
}
