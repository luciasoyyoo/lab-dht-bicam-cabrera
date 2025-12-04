package local_search.acceptation_type;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import metaheurictics.strategy.Strategy;
import local_search.acceptation_type.AcceptNotBadU;
import problem.definition.Problem;
import problem.definition.ObjetiveFunction;
import problem.definition.State;
import problem.definition.Problem.ProblemType;

public class AcceptNotBadUTest {

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
    public void testMaximizar_acceptsWhenDifferenceLessThanThreshold() throws Exception{
        Strategy.getStrategy().setProblem(makeProblem(ProblemType.Maximizar));
        Strategy.getStrategy().setThreshold(1.0); // threshold = 1.0
        AcceptNotBadU a = new AcceptNotBadU();
        State cur = new State(); cur.setEvaluation(new ArrayList<Double>(){{ add(10.0); }});
        State cand = new State(); cand.setEvaluation(new ArrayList<Double>(){{ add(9.5); }});
        // result = cur - cand = 0.5 < 1.0 -> accept true
        assertTrue(a.acceptCandidate(cur, cand));
    }

    @Test
    public void testMaximizar_rejectsWhenDifferenceGreaterOrEqualThreshold() throws Exception{
        Strategy.getStrategy().setProblem(makeProblem(ProblemType.Maximizar));
        Strategy.getStrategy().setThreshold(0.5); // threshold = 0.5
        AcceptNotBadU a = new AcceptNotBadU();
        State cur = new State(); cur.setEvaluation(new ArrayList<Double>(){{ add(10.0); }});
        State cand = new State(); cand.setEvaluation(new ArrayList<Double>(){{ add(9.0); }});
        // result = 1.0 >= 0.5 -> accept false
        assertFalse(a.acceptCandidate(cur, cand));
    }

    @Test
    public void testMinimizar_acceptsWhenDifferenceGreaterThanThreshold() throws Exception{
        Strategy.getStrategy().setProblem(makeProblem(ProblemType.Minimizar));
        Strategy.getStrategy().setThreshold(-0.5); // threshold negative
        AcceptNotBadU a = new AcceptNotBadU();
        State cur = new State(); cur.setEvaluation(new ArrayList<Double>(){{ add(2.0); }});
        State cand = new State(); cand.setEvaluation(new ArrayList<Double>(){{ add(3.0); }});
        // result_min = cur - cand = -1.0; since -1.0 > -0.5 is false -> not accept
        assertFalse(a.acceptCandidate(cur, cand));

        // change threshold so result > threshold
        Strategy.getStrategy().setThreshold(-2.0);
        // now -1.0 > -2.0 -> true
        assertTrue(a.acceptCandidate(cur, cand));
    }

    @Test
    public void testEdge_case_equalToThreshold() throws Exception{
        Strategy.getStrategy().setProblem(makeProblem(ProblemType.Maximizar));
        Strategy.getStrategy().setThreshold(0.0);
        AcceptNotBadU a = new AcceptNotBadU();
        State cur = new State(); cur.setEvaluation(new ArrayList<Double>(){{ add(5.0); }});
        State cand = new State(); cand.setEvaluation(new ArrayList<Double>(){{ add(5.0); }});
        // result = 0.0; for Maximizar, result < threshold? 0 < 0 false -> reject
        assertFalse(a.acceptCandidate(cur, cand));
    }
}
