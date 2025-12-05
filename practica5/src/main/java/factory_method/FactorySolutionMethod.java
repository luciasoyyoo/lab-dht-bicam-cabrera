package factory_method;

import java.lang.reflect.InvocationTargetException;

import problem.extension.SolutionMethod;
import problem.extension.TypeSolutionMethod;
import factory_interface.IFFactorySolutionMethod;

/**
 * FactorySolutionMethod -  Class responsible for creating solution method strategies.
 */
public class FactorySolutionMethod implements IFFactorySolutionMethod {

	private SolutionMethod solutionMethod;
	
	@Override
	/**
	 * createdSolutionMethod - Creates an instance of SolutionMethod based on the provided TypeSolutionMethod.
	 * @param method 
	 * @return returns an instance of SolutionMethod
	 */
	public SolutionMethod createdSolutionMethod(TypeSolutionMethod method) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		String className = "problem.extension." + method.toString();
		solutionMethod = (SolutionMethod) FactoryLoader.getInstance(className);
		return solutionMethod;
	}

}
