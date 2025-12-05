/**
 * @(#) FactoryAcceptCandidate.java
 */

package factory_method;

import java.lang.reflect.InvocationTargetException;

import local_search.acceptation_type.AcceptType;
import local_search.acceptation_type.AcceptableCandidate;


import factory_interface.IFFactoryAcceptCandidate;




/**
 * FactoryAcceptCandidate - Interface for creating acceptable candidates.
 */
public class FactoryAcceptCandidate implements IFFactoryAcceptCandidate{
	private AcceptableCandidate acceptCandidate;
	
	/**
	 * createAcceptCandidate - Creates an instance of AcceptableCandidate based on the provided AcceptType.
	 * @param typeacceptation 
	 * @return 
	 */
	public AcceptableCandidate createAcceptCandidate( AcceptType typeacceptation ) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		String className = "local_search.acceptation_type." + typeacceptation.toString();
		acceptCandidate = (AcceptableCandidate) FactoryLoader.getInstance(className);
		return acceptCandidate;
	}
}
