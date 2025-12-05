package evolutionary_algorithms.complement;

import java.util.List;

import problem.definition.State;


/**
 * FatherSelection - selects the best fathers from the population.
 */
public abstract class FatherSelection {
	/**
	 * selection - selects the best fathers from the population.
	 * @param listState
	 * @param truncation
	 * @return returns a list of selected fathers
	 */
	public abstract List<State> selection(List<State> listState, int truncation);

}
