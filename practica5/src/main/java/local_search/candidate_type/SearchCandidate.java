/**
 * @(#) SearchCandidate.java
 */

package local_search.candidate_type;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import problem.definition.State;


/**
 * SearchCandidate - abstract base class for candidate selection strategies.
 *
 * @brief Concrete subclasses implement `stateSearch` to select a candidate
 *        State from a provided neighborhood.
 */
public abstract class SearchCandidate {
    
	/**
	 * Choose a candidate state from the provided neighborhood.
	 *
	 * @param listNeighborhood the list of neighboring States to choose from
	 * @return chosen State
	 * @throws ReflectiveOperationException when reflective operations fail
	 */
	public abstract State stateSearch(List<State> listNeighborhood) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException ;
}
