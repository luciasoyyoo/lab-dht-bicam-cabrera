package local_search.acceptation_type;

import java.util.List;

import metaheurictics.strategy.Strategy;

import problem.definition.State;

/**
 * AcceptNotDominatedTabu - Acceptation that integrates a tabu-like Pareto list.
 *
 * @brief Maintains a list of non-dominated solutions and checks whether the
 *        candidate should be accepted into that list. Intended for tabu-like
 *        multi-objective variants.
 */
public class AcceptNotDominatedTabu extends AcceptableCandidate {

	@Override
	/**
	 * Evaluate candidate against the non-dominated list and update it.
	 *
	 * @param stateCurrent  the current state
	 * @param stateCandidate the candidate state
	 * @return true if candidate processing succeeded (this method returns true
	 *         in current implementation), false if not
	 */
	public Boolean acceptCandidate(State stateCurrent, State stateCandidate) {
		List<State> list = Strategy.getStrategy().listRefPoblacFinal;
		
		Dominance dominance = new Dominance();
		if(list.size() == 0){
			list.add(stateCurrent.clone());
		}
		//Verificando si la solución candidata domina a alguna de las soluciones de la lista de soluciones no dominadas
		//De ser asi se eliminan de la lista y se adiciona la nueva solución en la lista
		//De lo contrario no se adiciona la solución candidata a la lista
		//Si fue insertada en la lista entonces la solucion candidata se convierte en solucion actual
		dominance.listDominance(stateCandidate, list);
		return true;
	}
}
