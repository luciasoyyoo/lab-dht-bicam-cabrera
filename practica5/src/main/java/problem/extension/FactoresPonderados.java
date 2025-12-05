package problem.extension;

import java.util.ArrayList;

import metaheurictics.strategy.Strategy;

import problem.definition.State;
import problem.definition.Problem.ProblemType;

/**
 * FactoresPonderados
 *
 * @brief Implementación de {@link SolutionMethod} que combina funciones
 *        objetivo mediante factores ponderados.
 */
public class FactoresPonderados extends SolutionMethod {

	@Override
	/**
	 * evaluationState
	 *
	 * @brief Evalúa un estado aplicando la suma ponderada de cada función
	 *        objetivo, teniendo en cuenta si la función es de maximizar o
	 *        minimizar.
	 *
	 * @param state estado a evaluar; la lista de evaluación del estado se
	 *              actualiza con el valor calculado.
	 */
	public void evaluationState(State state) {
		// TODO Auto-generated method stub
		double eval = 0;       
		double tempWeight = 0;	
		ArrayList<Double> evaluation = new ArrayList<Double>(Strategy.getStrategy().getProblem().getFunction().size());
		
		for (int i = 0; i < Strategy.getStrategy().getProblem().getFunction().size(); i++) {

			tempWeight = 0;
			if(Strategy.getStrategy().getProblem().getTypeProblem().equals(ProblemType.Maximizar)){
				if(Strategy.getStrategy().getProblem().getFunction().get(i).getTypeProblem().equals(ProblemType.Maximizar)){
					tempWeight = Strategy.getStrategy().getProblem().getFunction().get(i).Evaluation(state);
					tempWeight = tempWeight * Strategy.getStrategy().getProblem().getFunction().get(i).getWeight();
				}
				else{
					tempWeight = 1 - Strategy.getStrategy().getProblem().getFunction().get(i).Evaluation(state);
					tempWeight = tempWeight * Strategy.getStrategy().getProblem().getFunction().get(i).getWeight();
				}
			}
			else{
				if(Strategy.getStrategy().getProblem().getFunction().get(i).getTypeProblem().equals(ProblemType.Maximizar)){
					tempWeight = 1 - Strategy.getStrategy().getProblem().getFunction().get(i).Evaluation(state);
					tempWeight = tempWeight * Strategy.getStrategy().getProblem().getFunction().get(i).getWeight();
				}
				else{
					tempWeight = Strategy.getStrategy().getProblem().getFunction().get(i).Evaluation(state);
					tempWeight = tempWeight * Strategy.getStrategy().getProblem().getFunction().get(i).getWeight();
				}
			}
			eval += tempWeight;
		}
		evaluation.add(evaluation.size(), eval);
		state.setEvaluation(evaluation);
		
	}

}
