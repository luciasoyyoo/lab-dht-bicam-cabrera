/**
 * @(#) FactoryCandidate.java
 */
package factory_method;

import java.lang.reflect.InvocationTargetException;

import local_search.candidate_type.CandidateType;
import local_search.candidate_type.SearchCandidate;

import factory_interface.IFFactoryCandidate;



/**
 * FactoryCandidate - Interface for creating search candidates.
 */
public class FactoryCandidate implements IFFactoryCandidate{
	private SearchCandidate searchcandidate;
	
	/**
	 * createSearchCandidate - descripcion (añade detalles).
	 * @param typeCandidate 
	 * @return 
	 */
	public SearchCandidate createSearchCandidate(CandidateType typeCandidate) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		String className = "local_search.candidate_type." + typeCandidate.toString();
		searchcandidate = (SearchCandidate) FactoryLoader.getInstance(className);
		return searchcandidate;
	}
}
