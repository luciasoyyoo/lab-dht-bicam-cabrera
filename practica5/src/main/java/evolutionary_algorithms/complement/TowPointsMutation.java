package evolutionary_algorithms.complement;


import metaheurictics.strategy.Strategy;
import problem.definition.State;
/**
 * TowPointsMutation - applies two-points mutation.
 */
public class TowPointsMutation extends Mutation {

	// legacy ProblemState-based mutation removed: use State-based mutation below

	@Override
	/**
	 * mutation - applies two-points mutation.
	 * @param newind 
	 * @param PM 
	 * @return returns the mutated state
	 */
	public State mutation(State newind, double PM) {
		Object key1 = Strategy.getStrategy().getProblem().getCodification().getAleatoryKey();
		Object key2 = Strategy.getStrategy().getProblem().getCodification().getAleatoryKey();
		Object value1 = Strategy.getStrategy().getProblem().getCodification().getVariableAleatoryValue((Integer) key1);
		Object value2 = Strategy.getStrategy().getProblem().getCodification().getVariableAleatoryValue((Integer) key2);
		newind.getCode().set((Integer) key1, (Integer)value2);
		newind.getCode().set((Integer) key2, (Integer)value1);
		return newind;
	}
}
  