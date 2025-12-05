/**
 * @(#) AleatoryCandidate.java
 */

package local_search.candidate_type;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import problem.definition.State;

/**
 * RandomCandidate - select a neighbor at random.
 *
 * @brief Returns a uniformly random neighbor from the provided list. Uses
 *        ThreadLocalRandom for thread-safe, efficient random number generation.
 */
public class RandomCandidate extends SearchCandidate {
	/**
     * Uses ThreadLocalRandom for algorithmic (non-cryptographic) randomness.
     * This RNG is used to decide whether a mutation occurs in the
     * evolutionary algorithm and is not used for security-sensitive purposes.
     * Suppress Sonar security hotspot S2245 for this usage.
     */
    @SuppressWarnings("squid:S2245")
	@Override
	/**
	 * Choose a random neighbor from the list.
	 *
	 * @param listNeighborhood list of neighbor states
	 * @return a randomly selected State from the list
	 */
	public State stateSearch(List<State> listNeighborhood) {
		int size = listNeighborhood.size();
		int pos = (size > 0) ? ThreadLocalRandom.current().nextInt(size) : 0;
		State stateAleatory = listNeighborhood.get(pos);
		return stateAleatory;
	}
}
