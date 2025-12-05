package local_search.acceptation_type;

import java.lang.reflect.InvocationTargetException;

import problem.definition.State;

/**
 * AcceptableCandidate - Base type for acceptation strategies.
 *
 * @brief Abstract contract that all acceptation strategies must implement.
 */
public abstract class AcceptableCandidate {
  
	/**
	 * Evaluate whether a candidate state should be accepted.
	 *
	 * @param stateCurrent  the current state
	 * @param stateCandidate the candidate state
	 * @return true if the candidate should be accepted, false otherwise
	 * @throws IllegalArgumentException on invalid arguments
	 * @throws SecurityException on reflective access problems
	 * @throws ClassNotFoundException when a referenced class cannot be found
	 * @throws InstantiationException on reflective instantiation failures
	 * @throws IllegalAccessException on illegal reflective access
	 * @throws InvocationTargetException when a reflective call throws
	 * @throws NoSuchMethodException when a reflective method lookup fails
	 */
	public abstract Boolean acceptCandidate(State stateCurrent, State stateCandidate) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException ;
}
