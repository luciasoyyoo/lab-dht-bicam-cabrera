/**
 * @(#) AcceptNoBadU.java
 */

package local_search.acceptation_type;

import java.lang.reflect.InvocationTargetException;

import metaheurictics.strategy.Strategy;
import problem.definition.Problem;
import problem.definition.State;
import problem.definition.Problem.ProblemType;

/**
 * AcceptNotBadU - Threshold based acceptation strategy.
 *
 * @brief Accepts a candidate when the evaluation difference between the
 *        current and candidate state is below/above a configured threshold
 *        (depending on maximization/minimization).
 */
public class AcceptNotBadU extends AcceptableCandidate{

	@Override
	/**
	 * Decide acceptance using a configured threshold in the strategy.
	 *
	 * @param stateCurrent  the current state
	 * @param stateCandidate the candidate state
	 * @return true if candidate is accepted according to threshold, false otherwise
	 * @throws IllegalArgumentException on invalid arguments
	 * @throws SecurityException on reflective access problems
	 * @throws ClassNotFoundException when a referenced class cannot be found
	 * @throws InstantiationException on reflective instantiation failures
	 * @throws IllegalAccessException on illegal reflective access
	 * @throws InvocationTargetException when a reflective call throws
	 * @throws NoSuchMethodException when a reflective method lookup fails
	 */
	public Boolean acceptCandidate(State stateCurrent, State stateCandidate) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Boolean accept = null;
		Problem problem = Strategy.getStrategy().getProblem();
		if (problem.getTypeProblem().equals(ProblemType.Maximizar)) {
			Double result = stateCurrent.getEvaluation().get(0) - stateCandidate.getEvaluation().get(0);
			if (result < Strategy.getStrategy().getThreshold())
				accept = true;
			else
				accept = false;
		} else {
			Double result_min = stateCurrent.getEvaluation().get(0) - stateCandidate.getEvaluation().get(0);
			if (result_min > Strategy.getStrategy().getThreshold())
				accept = true;
			else
				accept = false;
		}
		return accept;
	}
}
