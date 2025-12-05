package evolutionary_algorithms.complement;

import java.util.List;

import problem.definition.State;


/**
 * Sampling - represents a sampling strategy for selecting individuals.
 */
public abstract class Sampling {
	/**
	 * sampling - samples a given number of individuals from the list of fathers.
	 * @param fathers
	 * @param countInd
	 * @return
	 */
	public abstract List<State> sampling (List<State> fathers, int countInd);
}
