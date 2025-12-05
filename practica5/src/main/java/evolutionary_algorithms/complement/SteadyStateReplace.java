package evolutionary_algorithms.complement;


import java.util.List;
import metaheurictics.strategy.Strategy;
import problem.definition.State;
import problem.definition.Problem.ProblemType;


/**
 * SteadyStateReplace - applies steady-state replacement strategy.
 */
public class SteadyStateReplace extends Replace {

	@Override
	/**
	 * replace - applies steady-state replacement strategy.
	 * @param stateCandidate 
	 * @param listState 
	 * @return returns the updated list of states after replacement.
	 */
	public List<State> replace(State stateCandidate, List<State> listState) {
		State stateREP = null;
		if (Strategy.getStrategy().getProblem().getTypeProblem().equals(ProblemType.Maximizar)) {
			stateREP = minValue(listState);
			if(stateCandidate.getEvaluation().get(0) >= stateREP.getEvaluation().get(0)){
				Boolean find = false;
		        int count = 0;
		        while ((find.equals(false)) && (listState.size() > count)){
		        	if(listState.get(count).equals(stateREP)){
		        		listState.remove(count);
						listState.add(count, stateCandidate);
						find = true;
					}
		        	else count ++;
				}
			}
		}
		else {
				if(Strategy.getStrategy().getProblem().getTypeProblem().equals(ProblemType.Minimizar)){
				stateREP = maxValue(listState);
				if(stateCandidate.getEvaluation().get(0) <= stateREP.getEvaluation().get(0)){
					Boolean find = false;
			        int count = 0;
			        while ((find.equals(false)) && (listState.size() > count)){
			        	if(listState.get(count).equals(stateREP)){
			        		listState.remove(count);
							listState.add(count, stateCandidate);
							find = true;
						}
			        	else count ++;
					}
				}
			}
		}
		return listState;
	}
	
	/**
	 * minValue - applies steady-state replacement strategy.
	 * @param listState 
	 * @return returns the state with the minimum evaluation value.
	 */
	public State minValue(List<State> listState) {
		State value = listState.get(0);
		double min = listState.get(0).getEvaluation().get(0);
		for (int i = 1; i < listState.size(); i++) {
			if (listState.get(i).getEvaluation().get(0) < min) {
				min = listState.get(i).getEvaluation().get(0);
				value = listState.get(i);
			}
		}
		return value;
	}

	/**
	 * maxValue - applies steady-state replacement strategy.
	 * @param listState 
	 * @return returns the state with the maximum evaluation value.
	 */
	public State maxValue(List<State> listState) {
		State value = listState.get(0);
		double max = listState.get(0).getEvaluation().get(0);
		for (int i = 1; i < listState.size(); i++) {
			if (listState.get(i).getEvaluation().get(0) > max) {
				max = listState.get(i).getEvaluation().get(0);
				value = listState.get(i);
			}
		}
		return value;
	}
}
