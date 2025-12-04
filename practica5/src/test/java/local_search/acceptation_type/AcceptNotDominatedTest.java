package local_search.acceptation_type;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import metaheurictics.strategy.Strategy;
import metaheuristics.generators.RandomSearch;
import local_search.acceptation_type.AcceptNotDominated;
import problem.definition.Problem;
import problem.definition.ObjetiveFunction;
import problem.definition.State;
import problem.definition.Problem.ProblemType;

public class AcceptNotDominatedTest {

    @BeforeEach
    public void beforeEach(){
        Strategy.destroyExecute();
    }

    private Problem makeProblem(ProblemType type){
        Problem p = new Problem();
        p.setTypeProblem(type);
        ArrayList<ObjetiveFunction> funcs = new ArrayList<>();
        funcs.add(new ObjetiveFunction(){
            @Override
            public Double Evaluation(State state) { return 0.0; }
        });
        p.setFunction(funcs);
        return p;
    }

    @Test
    public void testCandidateDominatesCurrent_maximizar_acceptsAndReplacesList() throws Exception{
        Strategy s = Strategy.getStrategy();
        s.setProblem(makeProblem(ProblemType.Maximizar));
        // ensure generator is non-null to avoid NPEs inside Dominance
        s.generator = new RandomSearch();

        State current = new State(); current.setEvaluation(new ArrayList<Double>(){{ add(1.0); }});
        State candidate = new State(); candidate.setEvaluation(new ArrayList<Double>(){{ add(2.0); }});

        // listRefPoblacFinal starts empty
        s.listRefPoblacFinal.clear();

        AcceptNotDominated a = new AcceptNotDominated();
        boolean accepted = a.acceptCandidate(current, candidate);
        assertTrue(accepted, "candidate that dominates current should be accepted");

        // list should now contain the candidate (clone)
        assertEquals(1, s.listRefPoblacFinal.size());
        assertEquals(2.0, s.listRefPoblacFinal.get(0).getEvaluation().get(0), 1e-9);
    }

    @Test
    public void testCurrentDominatesCandidate_maximizar_rejectsAndKeepsList() throws Exception{
        Strategy s = Strategy.getStrategy();
        s.setProblem(makeProblem(ProblemType.Maximizar));
        s.generator = new RandomSearch();

        State current = new State(); current.setEvaluation(new ArrayList<Double>(){{ add(3.0); }});
        State candidate = new State(); candidate.setEvaluation(new ArrayList<Double>(){{ add(1.0); }});

        s.listRefPoblacFinal.clear();

        AcceptNotDominated a = new AcceptNotDominated();
        boolean accepted = a.acceptCandidate(current, candidate);
        assertFalse(accepted, "candidate worse than current should be rejected");

        // list should contain the original current clone
        assertEquals(1, s.listRefPoblacFinal.size());
        assertEquals(3.0, s.listRefPoblacFinal.get(0).getEvaluation().get(0), 1e-9);
    }

    @Test
    public void testEqualEvaluations_maximizar_rejectsAndDoesNotAddDuplicate() throws Exception{
        Strategy s = Strategy.getStrategy();
        s.setProblem(makeProblem(ProblemType.Maximizar));
        s.generator = new RandomSearch();

        State current = new State(); current.setEvaluation(new ArrayList<Double>(){{ add(2.0); }});
        State candidate = new State(); candidate.setEvaluation(new ArrayList<Double>(){{ add(2.0); }});

        s.listRefPoblacFinal.clear();

        AcceptNotDominated a = new AcceptNotDominated();
        boolean accepted = a.acceptCandidate(current, candidate);
        // equal evaluations should not be accepted (not dominating)
        assertFalse(accepted);
        // list should only contain the original clone (no duplicate added)
        assertEquals(1, s.listRefPoblacFinal.size());
        assertEquals(2.0, s.listRefPoblacFinal.get(0).getEvaluation().get(0), 1e-9);
    }

    @Test
    public void testCandidateDominatesCurrent_minimizar_acceptsAndReplacesList() throws Exception{
        Strategy s = Strategy.getStrategy();
        s.setProblem(makeProblem(ProblemType.Minimizar));
        s.generator = new RandomSearch();

        State current = new State(); current.setEvaluation(new ArrayList<Double>(){{ add(5.0); }});
        State candidate = new State(); candidate.setEvaluation(new ArrayList<Double>(){{ add(3.0); }});

        s.listRefPoblacFinal.clear();

        AcceptNotDominated a = new AcceptNotDominated();
        boolean accepted = a.acceptCandidate(current, candidate);
        assertTrue(accepted, "for Minimizar, lower candidate should be accepted");
        assertEquals(1, s.listRefPoblacFinal.size());
        assertEquals(3.0, s.listRefPoblacFinal.get(0).getEvaluation().get(0), 1e-9);
    }
}
