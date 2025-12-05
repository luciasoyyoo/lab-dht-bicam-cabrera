package evolutionary_algorithms.complement;


import java.lang.reflect.InvocationTargetException;
import java.util.List;

import problem.definition.State;

/**
 * GenerationalReplace - replaces the worst individual in the population.
 */
public class GenerationalReplace extends Replace {

	@Override
	/**
	 * replace - replaces the worst individual in the population.
	 * @param stateCandidate 
	 * @param listState 
	 * @return returns the new population after replacing the worst individual with the stateCandidate
	 */
	public List<State> replace(State stateCandidate, List<State> listState) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		listState.remove(0);
		listState.add(stateCandidate);
		return listState;
	}
}
