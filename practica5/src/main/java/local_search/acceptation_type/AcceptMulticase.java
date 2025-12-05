package local_search.acceptation_type;

import metaheuristics.generators.*;
import metaheurictics.strategy.*;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import problem.definition.State;

/**
 * AcceptMulticase - Multi-case acceptation strategy for multiobjective algorithms.
 *
 * @brief Decides whether to accept a candidate state based on dominance,
 *        rank and simulated annealing like probabilities.
 */
public class AcceptMulticase extends AcceptableCandidate {
	/**
     * Uses ThreadLocalRandom for algorithmic (non-cryptographic) randomness.
     * This RNG is used to decide whether a mutation occurs in the
     * evolutionary algorithm and is not used for security-sensitive purposes.
     * Suppress Sonar security hotspot S2245 for this usage.
     */
    @SuppressWarnings("squid:S2245")
	@Override
	/**
	 * Evaluate a candidate for acceptance using dominance, rank and
	 * probabilistic criteria.
	 *
	 * @param stateCurrent  the current state
	 * @param stateCandidate the candidate state
	 * @return true if the candidate should be accepted, false otherwise
	 */
	public Boolean acceptCandidate(State stateCurrent, State stateCandidate) {
		// TODO Auto-generated method stub
		Boolean accept = false;
		List<State> list = Strategy.getStrategy().listRefPoblacFinal;
		
		if(list.size() == 0){
			list.add(stateCurrent.clone());
		}
	Double T = MultiCaseSimulatedAnnealing.getTinitial();
		double pAccept = 0;
	// use ThreadLocalRandom to avoid creating a new Random instance on each call
		Dominance dominance= new Dominance();
		//Verificando si la soluci�n candidata domina a la soluci�n actual
		//Si la soluci�n candidata domina a la soluci�n actual
		if(dominance.dominance(stateCandidate, stateCurrent) == true){
			//Se asigna como soluci�n actual la soluci�n candidata con probabilidad 1
			pAccept = 1; 
		}
		else if(dominance.dominance(stateCandidate, stateCurrent)== false){	
			if(dominanceCounter(stateCandidate, list) > 0){
				pAccept = 1;
			}
			else if(dominanceRank(stateCandidate, list) == 0){
				pAccept = 1;
			}
			else if(dominanceRank(stateCandidate, list) < dominanceRank(stateCurrent, list)){
				pAccept = 1;
			}
			else if(dominanceRank(stateCandidate, list) == dominanceRank(stateCurrent, list)){
				//Calculando la probabilidad de aceptaci�n
				List<Double> evaluations = stateCurrent.getEvaluation();
				double total = 0;
				for (int i = 0; i < evaluations.size()-1; i++) {
					Double evalA = evaluations.get(i);
					Double evalB = stateCandidate.getEvaluation().get(i);
					if (evalA != 0 && evalB != 0) {
						total += (evalA - evalB)/((evalA + evalB)/2);
					}
				}	
				pAccept = Math.exp(-(1-total)/T);
			}
			else if (dominanceRank(stateCandidate, list) > dominanceRank(stateCurrent, list) && dominanceRank(stateCurrent, list)!= 0){
				// avoid integer division -> compute ranks once and use floating point division
				int rankCandidate = dominanceRank(stateCandidate, list);
				int rankCurrent = dominanceRank(stateCurrent, list);
				float value = (rankCurrent == 0) ? 0f : ((float) rankCandidate) / ((float) rankCurrent);
				pAccept = Math.exp(-((double)value+1.0)/T);
			}
			else{
				//Calculando la probabilidad de aceptaci�n
				List<Double> evaluations = stateCurrent.getEvaluation();
				double total = 0;
				for (int i = 0; i < evaluations.size()-1; i++) {
					Double evalA = evaluations.get(i);
					Double evalB = stateCandidate.getEvaluation().get(i);
					if (evalA != 0 && evalB != 0) {
						total += (evalA - evalB)/((evalA + evalB)/2);
					}
				}
				pAccept = Math.exp(-(1-total)/T);
			}
		}
		//Generar un n�mero aleatorio
			if((ThreadLocalRandom.current().nextFloat()) < pAccept){
			// Don't assign to the parameter reference (dead store). The caller should handle state updates.
			// Verificando que la soluci�n candidata domina a alguna de las soluciones
				accept = dominance.listDominance(stateCandidate, list);
		}
		return accept;
	}


	private int dominanceCounter(State stateCandidate, List<State> list) { //chequea el n�mero de soluciones de Pareto que son dominados por la nueva soluci�n
		int counter = 0;
		for (int i = 0; i < list.size(); i++) {
			State solution = list.get(i);
			Dominance dominance = new Dominance();
			if(dominance.dominance(stateCandidate, solution) == true)
				counter++;
		}
		return counter;
	}

	private int dominanceRank(State stateCandidate, List<State> list) { //calculando el n�mero de soluciones en el conjunto de Pareto que dominan a la soluci�n
		int rank = 0;
		for (int i = 0; i < list.size(); i++) {
			State solution = list.get(i);
			Dominance dominance = new Dominance();
			if(dominance.dominance(solution, stateCandidate) == true){
				rank++;
			}
		}
		
		return rank;
	}

}
