package local_search.candidate_type;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import local_search.acceptation_type.Dominance;
import metaheurictics.strategy.Strategy;

import problem.definition.State;

/**
 * NotDominatedCandidate - select a non-dominated neighbor for multi-objective.
 *
 * @brief Chooses a neighbor that is not dominated by others in the neighborhood
 *        using the Domination comparator. Intended for multi-objective problems.
 */
public class NotDominatedCandidate extends SearchCandidate {

	@Override
	/**
	 * Select a non-dominated state from the neighborhood.
	 *
	 * @param listNeighborhood list of neighbor states
	 * @return a non-dominated State chosen from the list (or the first if only one)
	 * @throws ReflectiveOperationException when evaluation or reflective calls fail
	 */
	public State stateSearch(List<State> listNeighborhood) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		State state = new State();
		State stateA = listNeighborhood.get(0);
		boolean stop = false;
		if(listNeighborhood.size() == 1){
			stop = true;
			state = stateA;
		}
		else {
			Strategy.getStrategy().getProblem().Evaluate(stateA);
			State stateB;
			Dominance dominance = new Dominance();
			for (int i = 1; i < listNeighborhood.size(); i++) {
				while(stop == false){
					stateB = listNeighborhood.get(i);
					Strategy.getStrategy().getProblem().Evaluate(stateB);
					if(dominance.dominance(stateB, stateA) == true){
						stateA = stateB;
					}else{
						stop = true;
						state = stateA;
					}
				}
			}
		}
		return state;
	}

}
