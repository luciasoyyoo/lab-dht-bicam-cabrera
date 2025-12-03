package evolutionary_algorithms.complement;

import java.util.ArrayList;
import java.util.List;

import metaheurictics.strategy.Strategy;
import problem.definition.State;
import problem.definition.Problem.ProblemType;

/**
 * Test shadow of TruncationSelection used to make GeneticAlgorithm.generate() testable.
 * In tests we want truncation==0 to behave as "select all" rather than returning empty list.
 */
public class TruncationSelection extends FatherSelection {

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
    public List<State> selection(List<State> listState, int truncation) {
        List<State> AuxList = new ArrayList<State>();
        if (Strategy.getStrategy().getProblem().getTypeProblem().equals(ProblemType.Maximizar)) {
            listState = orderBetter(listState);
        } else {
            if(Strategy.getStrategy().getProblem().getTypeProblem().equals(ProblemType.Minimizar))
                listState = ascOrderBetter(listState);
        }
        if (truncation <= 0) {
            // In tests treat truncation<=0 as "select all" to avoid empty selection
            AuxList.addAll(listState);
            return AuxList;
        }
        int i = 0;
        while(AuxList.size()< truncation){
            AuxList.add(listState.get(i));
            i++;
        }
        return AuxList;
    }
}
