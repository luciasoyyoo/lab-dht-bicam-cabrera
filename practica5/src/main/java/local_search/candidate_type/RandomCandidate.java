/**
 * @(#) AleatoryCandidate.java
 */

package local_search.candidate_type;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import problem.definition.State;

public class RandomCandidate extends SearchCandidate {
	/**
     * Uses ThreadLocalRandom for algorithmic (non-cryptographic) randomness.
     * This RNG is used to decide whether a mutation occurs in the
     * evolutionary algorithm and is not used for security-sensitive purposes.
     * Suppress Sonar security hotspot S2245 for this usage.
     */
    @SuppressWarnings("squid:S2245")
	@Override
	public State stateSearch(List<State> listNeighborhood) {
		int size = listNeighborhood.size();
		int pos = (size > 0) ? ThreadLocalRandom.current().nextInt(size) : 0;
		State stateAleatory = listNeighborhood.get(pos);
		return stateAleatory;
	}
}
