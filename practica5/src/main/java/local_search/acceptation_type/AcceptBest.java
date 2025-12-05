/**
 * @(#) AcceptBest.java
 */

package local_search.acceptation_type;

import java.lang.reflect.InvocationTargetException;

import metaheurictics.strategy.Strategy;
import problem.definition.Problem;
import problem.definition.State;
import problem.definition.Problem.ProblemType;

/**
 * AcceptBest - Accept the candidate only if it improves (or ties) the current state.
 *
 * @brief Accepts the candidate state when it is better according to the
 *        problem objective type (maximize/minimize).
 */
public class AcceptBest extends AcceptableCandidate {

	@Override
	/**
	 * Evaluate and decide acceptance of a candidate state.
	 *
	 * @param stateCurrent   current state
	 * @param stateCandidate candidate state to evaluate
	 * @return true if candidate should be accepted according to the problem
	 *         objective (maximization/minimization), false otherwise
	 * @throws IllegalArgumentException when arguments are invalid
	 * @throws SecurityException on reflective access problems
	 * @throws ClassNotFoundException when a referenced class cannot be found
	 * @throws InstantiationException when a class cannot be instantiated reflectively
	 * @throws IllegalAccessException on illegal reflective access
	 * @throws InvocationTargetException when a reflective call throws an exception
	 * @throws NoSuchMethodException when a reflective method lookup fails
	 */
	public Boolean acceptCandidate(State stateCurrent, State stateCandidate) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Boolean accept = null;
		Problem problem = Strategy.getStrategy().getProblem();
		if(problem.getTypeProblem().equals(ProblemType.Maximizar)) {
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
		}
		return accept;
	}
}
