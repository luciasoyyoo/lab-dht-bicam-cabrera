/**
 * @(#) AcceptNoBad.java
 */

package local_search.acceptation_type;

import metaheurictics.strategy.Strategy;
import problem.definition.Problem;
import problem.definition.State;
import problem.definition.Problem.ProblemType;

/**
 * AcceptNotBad - Accept candidate if it is not worse than the current state.
 *
 * @brief Strategy that accepts a candidate if it is at least as good as the
 *        current state according to the problem objective.
 */
public class AcceptNotBad extends AcceptableCandidate{

	@Override
	/**
	 * Determine acceptance by comparing candidate and current state.
	 *
	 * @param stateCurrent  the current state
	 * @param stateCandidate the candidate state
	 * @return true if candidate is not worse than current state, false otherwise
	 */
	public Boolean acceptCandidate(State stateCurrent, State stateCandidate) {
		Boolean accept = null;
		Problem problem = Strategy.getStrategy().getProblem();
		for (int i = 0; i < problem.getFunction().size(); i++) {
		if (problem.getTypeProblem().equals(ProblemType.Maximizar)) {
			if (stateCandidate.getEvaluation().get(0) >= stateCurrent.getEvaluation().get(0)) {
				accept = true;
			} else {
				accept = false;
			}
		} else {
			if (stateCandidate.getEvaluation().get(0) <= stateCurrent.getEvaluation().get(0)) {
				accept = true;
			} else {
				accept = false;
			}
		}}
		return accept;
	}
}
