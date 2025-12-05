package local_search.acceptation_type;

import java.util.List;

import metaheurictics.strategy.Strategy;
import metaheuristics.generators.GeneratorType;
import metaheuristics.generators.MultiobjectiveHillClimbingDistance;
import problem.definition.Problem.ProblemType;
import problem.definition.State;

/**
 * Dominance - Utilities for Pareto dominance comparisons.
 *
 * @brief Provides helpers to test dominance relations between multi-objective
 *        solution states and to maintain a Pareto front (list of non-dominated
 *        solutions).
 */
public class Dominance {

	//---------------------------------Metodos que se utilizan en los algoritmos multiobjetivo-------------------------------------------------------//
	//Funcion que determina si la solucion X domina a alguna de las soluciones no dominadas de una lista
	//Devuelve la lista actualizada y true si fue adicionada a la lista o false de lo contrario
	/**
	 * Check whether solutionX dominates members of the supplied list and update
	 * the list accordingly. If solutionX is not dominated and not present it is
	 * added.
	 *
	 * @param solutionX the candidate solution to test
	 * @param list the list of non-dominated solutions (Pareto front)
	 * @return true if the solution was added to the list (i.e. it is non-dominated
	 *         and not duplicate), false otherwise
	 */
	public boolean listDominance(State solutionX, List<State> list){
		boolean domain = false;
		for (int i = 0; i < list.size() && domain == false; i++) {
			//Si la soluci�n X domina a la soluci�n de la lista
			if(dominance(solutionX, list.get(i)) == true){
				//Se elimina el elemento de la lista
				list.remove(i);
				if (i!=0) {
					i--;	
				}
					if (Strategy.getStrategy().generator.getType().equals(GeneratorType.MultiobjectiveHillClimbingDistance)&&list.size()!=0) {
						MultiobjectiveHillClimbingDistance.distanceCalculateAdd(list);
				}
			}
			if (list.size()>0) {
				if(dominance(list.get(i), solutionX) == true){
					domain = true;
				}
			}

		}
		//Si la soluci�n X no fue dominada
		if(domain == false){
			//Comprobando que la soluci�n no exista
			boolean found = false;
			for (int k = 0; k < list.size() && found == false; k++) {
				State element = list.get(k);
				found = solutionX.Comparator(element);
			}
			//Si la soluci�n no existe
			if(found == false){
				//Se guarda la soluci�n candidata en la lista de soluciones �ptimas de Pareto
				list.add(solutionX.clone());
				if (Strategy.getStrategy().generator.getType().equals(GeneratorType.MultiobjectiveHillClimbingDistance)) {
					MultiobjectiveHillClimbingDistance.distanceCalculateAdd(list);
				}
				return true;
			}
		}
		return false;


	}


	//Funcion que devuelve true si solutionX domina a solutionY
	/**
	 * Determine whether solutionX Pareto-dominates solutionY.
	 *
	 * @param solutionX the candidate solution
	 * @param solutionY the reference solution
	 * @return true if solutionX dominates solutionY according to the
	 *         problem objective type, false otherwise
	 */
	public boolean dominance(State solutionX,  State solutionY) 	{
		boolean dominance = false;
		int countBest = 0;
		int countEquals = 0;
		//Si solutionX domina a solutionY
		if(Strategy.getStrategy().getProblem().getTypeProblem().equals(ProblemType.Maximizar)) {
			//Recorriendo las evaluaciones de las funciones objetivo
			for (int i = 0; i < solutionX.getEvaluation().size(); i++) {
				if(solutionX.getEvaluation().get(i).floatValue() > solutionY.getEvaluation().get(i).floatValue()){
					countBest++;
				}
				if(solutionX.getEvaluation().get(i).floatValue() == solutionY.getEvaluation().get(i).floatValue()){
					countEquals++;
				}	
			}
		}
		else{
			//Recorriendo las evaluaciones de las funciones objetivo
			for (int i = 0; i < solutionX.getEvaluation().size(); i++) {
				if(solutionX.getEvaluation().get(i).floatValue() < solutionY.getEvaluation().get(i).floatValue()){
					countBest++;
				}
				if(solutionX.getEvaluation().get(i).floatValue() == solutionY.getEvaluation().get(i).floatValue()){
					countEquals++;
				}	
			}
		}
		if((countBest >= 1) && (countEquals + countBest == solutionX.getEvaluation().size())) {
			dominance = true;
		}
		return dominance;
	} 
}
