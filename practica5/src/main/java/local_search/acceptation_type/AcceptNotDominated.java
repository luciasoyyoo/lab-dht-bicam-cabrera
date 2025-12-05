package local_search.acceptation_type;


import local_search.acceptation_type.Dominance;
import metaheurictics.strategy.Strategy;

import problem.definition.State;

/**
 * AcceptNotDominated - Accept candidate if it is not dominated by current
 *                    non-dominated set.
 *
 * @brief Multi-objective acceptation strategy that updates and consults
 *        a Pareto front (listRefPoblacFinal) to decide acceptance.
 */
public class AcceptNotDominated extends AcceptableCandidate {
	
	@Override
	/**
	 * Evaluate and accept a candidate based on Pareto dominance against the
	 * stored set of non-dominated solutions.
	 *
	 * @param stateCurrent  the current state
	 * @param stateCandidate the candidate state
	 * @return true if candidate should be accepted (becomes part of Pareto set),
	 *         false otherwise
	 */
	public Boolean acceptCandidate(State stateCurrent, State stateCandidate) {
		Boolean accept = false;
		Dominance dominace = new Dominance();
		
		if(Strategy.getStrategy().listRefPoblacFinal.size() == 0){
			Strategy.getStrategy().listRefPoblacFinal.add(stateCurrent.clone());
		}
		if(dominace.dominance(stateCurrent, stateCandidate)== false)
		{
			//Verificando si la solución candidata domina a alguna de las soluciones de la lista de soluciones no dominadas
			//De ser asi se eliminan de la lista y se adiciona la nueva solución en la lista
			//De lo contrario no se adiciona la solución candidata a la lista
			//Si fue insertada en la lista entonces la solución candidata se convierte en solución actual
			if(dominace.listDominance(stateCandidate, Strategy.getStrategy().listRefPoblacFinal) == true){
				//Se pone la solución candidata como solución actual
				accept = true;
			}
			else{
				accept = false;
			}
		}
		return accept;
	}

}
