package evolutionary_algorithms.complement;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import problem.definition.State;


/**
 * Replace - applies replacement strategies for individuals in the population.
 */
public abstract class Replace {
	/**
	 * replace - replace the worst individual in the population.
	 * @param stateCandidate
	 * @param listState
	 * @return
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public abstract List<State> replace(State stateCandidate, List<State>listState) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException;
}
