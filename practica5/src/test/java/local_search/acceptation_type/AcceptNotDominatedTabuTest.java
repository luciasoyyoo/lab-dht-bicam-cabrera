package local_search.acceptation_type;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import metaheurictics.strategy.Strategy;
import metaheuristics.generators.RandomSearch;
import local_search.acceptation_type.AcceptNotDominatedTabu;
import problem.definition.Problem;
import problem.definition.ObjetiveFunction;
import problem.definition.State;
import problem.definition.Problem.ProblemType;

public class AcceptNotDominatedTabuTest {

    @BeforeEach
    public void beforeEach(){
        Strategy.destroyExecute();
        Strategy.getStrategy().listRefPoblacFinal = new ArrayList<>();
        // ensure a generator exists to avoid NPE in Dominance
        Strategy.getStrategy().generator = new RandomSearch();
    }

    private Problem makeProblem(ProblemType type){
        Problem p = new Problem();
        p.setTypeProblem(type);
        ArrayList<ObjetiveFunction> funcs = new ArrayList<>();
        funcs.add(new ObjetiveFunction(){
            @Override
            public Double Evaluation(State state) { return state.getEvaluation().get(0); }
        });
        p.setFunction(funcs);
        return p;
    }

    @Test
    public void testCandidateDominatesCurrent_Maximizar() throws Exception{
        Strategy.getStrategy().setProblem(makeProblem(ProblemType.Maximizar));
        AcceptNotDominatedTabu a = new AcceptNotDominatedTabu();

        State current = new State(); current.setEvaluation(new ArrayList<Double>(){{ add(1.0); }});
        State candidate = new State(); candidate.setEvaluation(new ArrayList<Double>(){{ add(2.0); }});

        boolean ret = a.acceptCandidate(current, candidate);
        assertTrue(ret);
        assertEquals(1, Strategy.getStrategy().listRefPoblacFinal.size());
        // final list should contain the candidate (dominates current)
        assertEquals(2.0, Strategy.getStrategy().listRefPoblacFinal.get(0).getEvaluation().get(0), 1e-9);
    }

    @Test
    public void testCandidateIsDominated_Maximizar() throws Exception{
        Strategy.getStrategy().setProblem(makeProblem(ProblemType.Maximizar));
        AcceptNotDominatedTabu a = new AcceptNotDominatedTabu();

        State current = new State(); current.setEvaluation(new ArrayList<Double>(){{ add(5.0); }});
        State candidate = new State(); candidate.setEvaluation(new ArrayList<Double>(){{ add(3.0); }});

        boolean ret = a.acceptCandidate(current, candidate);
        assertTrue(ret);
        assertEquals(1, Strategy.getStrategy().listRefPoblacFinal.size());
        // list should retain the current best (5.0)
        assertEquals(5.0, Strategy.getStrategy().listRefPoblacFinal.get(0).getEvaluation().get(0), 1e-9);
    }

    @Test
    public void testCandidateDominatesCurrent_Minimizar() throws Exception{
        Strategy.getStrategy().setProblem(makeProblem(ProblemType.Minimizar));
        AcceptNotDominatedTabu a = new AcceptNotDominatedTabu();

        State current = new State(); current.setEvaluation(new ArrayList<Double>(){{ add(10.0); }});
        State candidate = new State(); candidate.setEvaluation(new ArrayList<Double>(){{ add(2.0); }});

        boolean ret = a.acceptCandidate(current, candidate);
        assertTrue(ret);
        assertEquals(1, Strategy.getStrategy().listRefPoblacFinal.size());
        // for minimizar, lower is better -> candidate should be kept
        assertEquals(2.0, Strategy.getStrategy().listRefPoblacFinal.get(0).getEvaluation().get(0), 1e-9);
    }

}
