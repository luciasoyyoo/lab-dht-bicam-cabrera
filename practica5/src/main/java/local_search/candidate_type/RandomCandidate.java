/**
 * @(#) AleatoryCandidate.java
 */

package local_search.candidate_type;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import problem.definition.State;

public class RandomCandidate extends SearchCandidate {

	@Override
	public State stateSearch(List<State> listNeighborhood) {
		int size = listNeighborhood.size();
		int pos = (size > 0) ? ThreadLocalRandom.current().nextInt(size) : 0;
		State stateAleatory = listNeighborhood.get(pos);
		return stateAleatory;
	}
}
