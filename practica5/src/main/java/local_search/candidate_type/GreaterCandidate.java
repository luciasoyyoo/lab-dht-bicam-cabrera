/**
 * @(#) MajorCandidate.java
 */

package local_search.candidate_type;


import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import problem.definition.State;

public class GreaterCandidate extends SearchCandidate {
    
	/**
	 * Uses ThreadLocalRandom for algorithmic (non-cryptographic) randomness.
	 * This RNG is used to pick a random neighbor when multiple equal bests
	 * are found and is not security-sensitive. Suppress Sonar S2245.
	 */
	@SuppressWarnings("squid:S2245")
	@Override
	public State stateSearch(List<State> listNeighborhood) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		State stateGreater = null;
		if(listNeighborhood.size() > 1){
			double counter = 0;
			double currentCount = listNeighborhood.get(0).getEvaluation().get(0);
			for (int i = 1; i < listNeighborhood.size(); i++) {
				counter = listNeighborhood.get(i).getEvaluation().get(0);
				if (counter > currentCount) {
					currentCount = counter;
					stateGreater = listNeighborhood.get(i);
				}
				counter = 0;
			}
			if(stateGreater == null){
				// bound is listNeighborhood.size() - 1 (>=1 since size>1)
				int pos = ThreadLocalRandom.current().nextInt(listNeighborhood.size() - 1);
				stateGreater = listNeighborhood.get(pos);
			}
		}
		else stateGreater = listNeighborhood.get(0);
		return stateGreater;
	}
}