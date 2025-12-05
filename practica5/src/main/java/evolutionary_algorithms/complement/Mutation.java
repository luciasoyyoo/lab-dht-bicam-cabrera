package evolutionary_algorithms.complement;

import problem.definition.State;

/**
 * Mutation - applies a mutation to a given state.
 */
public abstract class Mutation {
	/**
	 * mutation - applies the mutation to the state.
	 * @param state
	 * @param PM
	 * @return returns the mutated state
	 */
	public abstract State mutation (State state, double PM);

}
