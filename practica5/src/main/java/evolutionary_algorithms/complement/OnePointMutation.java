package evolutionary_algorithms.complement;

import java.util.concurrent.ThreadLocalRandom;

import metaheurictics.strategy.Strategy;
import problem.definition.State;

public class OnePointMutation extends Mutation {


    /**
     * Uses ThreadLocalRandom for algorithmic (non-cryptographic) randomness.
     * This RNG is used to decide whether a mutation occurs in the
     * evolutionary algorithm and is not used for security-sensitive purposes.
     * Suppress Sonar security hotspot S2245 for this usage.
     */
    @SuppressWarnings("squid:S2245")
    @Override
    public State mutation(State state, double PM) {
        double probM = ThreadLocalRandom.current().nextDouble();
		if(PM >= probM)
		{
			Object key = Strategy.getStrategy().getProblem().getCodification().getAleatoryKey();
			Object value = Strategy.getStrategy().getProblem().getCodification().getVariableAleatoryValue((Integer)key);
			state.getCode().set((Integer) key, value);
		}
		return state;
	}
}
