/**
 * @(#) AcceptNoBadT.java
 */

package local_search.acceptation_type;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ThreadLocalRandom;

import metaheurictics.strategy.Strategy;
import metaheuristics.generators.SimulatedAnnealing;
import problem.definition.Problem;
import problem.definition.State;
import problem.definition.Problem.ProblemType;

/**
 * AcceptNotBadT - Temperature based acceptation (simulated annealing style).
 *
 * @brief Accepts candidates with a probability depending on temperature and
 *        the evaluation difference when the candidate is worse.
 */
public class AcceptNotBadT extends AcceptableCandidate{

	/**
	 * Uses ThreadLocalRandom for algorithmic (non-cryptographic) randomness.
	 * This RNG is used to decide acceptance in simulated annealing and is not
	 * used for security-sensitive purposes. Suppress Sonar S2245 for this usage.
	 */
	@SuppressWarnings("squid:S2245")
	@Override
	/**
	 * Decide acceptance using temperature-based probability.
	 *
	 * @param stateCurrent  the current state
	 * @param stateCandidate the candidate state
	 * @return true if accepted, false otherwise
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
			double result = (stateCandidate.getEvaluation().get(0) - stateCurrent.getEvaluation().get(0)) / SimulatedAnnealing.getTinitial();
			double probaleatory = ThreadLocalRandom.current().nextDouble();
			double exp = Math.exp(result);
			if ((stateCandidate.getEvaluation().get(0) >= stateCurrent.getEvaluation().get(0))
					|| (probaleatory < exp))
 				accept = true;
			else
				accept = false;
		} else {
			double result_min = (stateCandidate.getEvaluation().get(0) - stateCurrent.getEvaluation().get(0)) / SimulatedAnnealing.getTinitial();
			if ((stateCandidate.getEvaluation().get(0) <= stateCurrent.getEvaluation().get(0))
					|| (ThreadLocalRandom.current().nextDouble() < Math.exp(result_min)))
				accept = true;
			else
				accept = false;
		}
		return accept;
	}
}
