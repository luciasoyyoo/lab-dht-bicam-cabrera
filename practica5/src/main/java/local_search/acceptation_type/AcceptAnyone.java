/**
 * @(#) AcceptAnyone.java
 */
package local_search.acceptation_type;

import problem.definition.State;


/**
 * AcceptAnyone - Accept any candidate solution.
 *
 * @brief Simple acceptation strategy that always accepts the candidate
 *        solution provided to the local search operator.
 */
public class AcceptAnyone extends AcceptableCandidate{

	@Override
	/**
	 * Decide whether to accept the candidate state.
	 *
	 * @param stateCurrent  the current state
	 * @param stateCandidate the candidate state to evaluate
	 * @return true always (accept any candidate)
	 */
	public Boolean acceptCandidate(State stateCurrent, State stateCandidate) {
		Boolean accept = true;
		return accept;
	}
	
}
