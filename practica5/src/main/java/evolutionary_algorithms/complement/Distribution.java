package evolutionary_algorithms.complement;

import java.util.List;

import problem.definition.State;


/**
 * Distribution - applies a probability distribution to a set of fathers.
 */
public abstract class Distribution {
	/**
	 * distribution - applies the distribution to the given fathers.
	 * @param fathers 
	 * @return returns a list of probabilities associated to the given fathers
	 */
	public abstract List<Probability> distribution(List<State> fathers);

}
