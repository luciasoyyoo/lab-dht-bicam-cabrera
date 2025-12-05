package evolutionary_algorithms.complement;


import java.util.ArrayList;
import java.util.List;

import metaheurictics.strategy.Strategy;

import problem.definition.State;
import problem.definition.Problem.ProblemType;

/**
 * TruncationSelection - applies truncation selection strategy.
 */
public class TruncationSelection extends FatherSelection {
	
	/**
	 * orderBetter - applies truncation selection strategy.
	 * @param listState 
	 * @return returns the list of states ordered from best to worst.
	 */
	public List<State> orderBetter (List<State> listState){
		State var = null;
		for (int i = 0; i < listState.size()- 1; i++) {
			for (int j = i+1; j < listState.size(); j++) {
				if(listState.get(i).getEvaluation().get(0) < listState.get(j).getEvaluation().get(0)){
					var = listState.get(i);
					listState.set(i, listState.get(j));
					listState.set(j,var);
				}
			}
		}
		return listState;
	}
	
	/**
	 * ascOrderBetter - applies truncation selection strategy.
	 * @param listState 
	 * @return returns the list of states ordered from worst to best.
	 */
	public List<State> ascOrderBetter (List<State> listState){
		State var = null;
		for (int i = 0; i < listState.size()- 1; i++) {
			for (int j = i+1; j < listState.size(); j++) {
				if(listState.get(i).getEvaluation().get(0) > listState.get(j).getEvaluation().get(0)){
					var = listState.get(i);
					listState.set(i, listState.get(j));
					listState.set(j,var);
				}
			}
		}
		return listState;
	}
    
	@Override
	/**
	 * selection - applies truncation selection strategy.
	 * @param listState 
	 * @param truncation 
	 * @return returns the selected list of states based on truncation.
	 */
	public List<State> selection(List<State> listState, int truncation) {
		List<State> AuxList = new ArrayList<State>();
		if (Strategy.getStrategy().getProblem().getTypeProblem().equals(ProblemType.Maximizar)) {
			listState = orderBetter(listState);
		} else {
			if(Strategy.getStrategy().getProblem().getTypeProblem().equals(ProblemType.Minimizar))
				listState = ascOrderBetter(listState);
		}
		int i = 0;
		while(AuxList.size()< truncation){
			AuxList.add(listState.get(i));
			i++;
		}
		return AuxList;
	}
}
