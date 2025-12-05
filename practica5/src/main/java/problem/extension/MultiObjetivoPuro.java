package problem.extension;

import java.util.ArrayList;

import metaheurictics.strategy.Strategy;

import problem.definition.ObjetiveFunction;
import problem.definition.State;
import problem.definition.Problem.ProblemType;

/**
 * MultiObjetivoPuro
 *
 * @brief Método de evaluación multiobjetivo puro: calcula el vector de
 *        evaluaciones sin agregación (mantiene cada función objetivo por separado).
 */
public class MultiObjetivoPuro extends SolutionMethod {

	@Override
	/**
	 * evaluationState
	 *
	 * @brief Rellena la lista de evaluaciones del estado con los valores
	 *        correspondientes a cada función objetivo, adaptando según si
	 *        la función o el problema es de maximizar/minimizar.
	 *
	 * @param state estado a evaluar
	 */
	public void evaluationState(State state) {
		// TODO Auto-generated method stub
		double tempEval = -1;
		ArrayList<Double> evaluation = new ArrayList<Double>(Strategy.getStrategy().getProblem().getFunction().size());
		for (int i = 0; i < Strategy.getStrategy().getProblem().getFunction().size(); i++)
		{
			ObjetiveFunction objfunction = (ObjetiveFunction)Strategy.getStrategy().getProblem().getFunction().get(i);
			if(Strategy.getStrategy().getProblem().getTypeProblem().equals(ProblemType.Maximizar)){
				if(objfunction.getTypeProblem().equals(ProblemType.Maximizar))
				{
					tempEval = objfunction.Evaluation(state);
				}
				else{
					tempEval = 1-objfunction.Evaluation(state);
				}
			}
			else{
				if(objfunction.getTypeProblem().equals(ProblemType.Maximizar))
				{
					tempEval = 1-objfunction.Evaluation(state);
				}
				else{
					tempEval = objfunction.Evaluation(state);
				}
			}
			evaluation.add(tempEval);
		}
		//evaluation.add( (double) -1);
		state.setEvaluation(evaluation);
	}

}
