package evolutionary_algorithms.complement;


import problem.definition.State;

/**
 * Crossover - applies the crossover operation to two parent states.
 */
public abstract class Crossover {
	/**
	 * crossover - applies the crossover operation to two parent states.
	 * @param father1
	 * @param father2
	 * @param PC
	 * @return returns the offspring resulting from the crossover between father1 and father2
	 */
	public abstract State crossover(State father1, State father2, double PC);
}
