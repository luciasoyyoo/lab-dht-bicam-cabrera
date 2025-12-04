package local_search.acceptation_type;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import metaheurictics.strategy.Strategy;
import local_search.acceptation_type.AcceptNotBad;
import problem.definition.Problem;
import problem.definition.ObjetiveFunction;
import problem.definition.State;
import problem.definition.Problem.ProblemType;

public class AcceptNotBadTest {

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
    public void testMaximizar_acceptsWhenCandidateIsGreaterOrEqual(){
        Strategy.getStrategy().setProblem(makeProblem(ProblemType.Maximizar));
        AcceptNotBad a = new AcceptNotBad();
        State cur = new State(); cur.setEvaluation(new ArrayList<Double>(){{ add(5.0); }});
        State cand = new State(); cand.setEvaluation(new ArrayList<Double>(){{ add(7.0); }});
        assertTrue(a.acceptCandidate(cur, cand));

        // equal case - update via setEvaluation (State.getEvaluation returns a copy)
        {
            ArrayList<Double> tmp = new ArrayList<>(cand.getEvaluation());
            tmp.set(0, 5.0);
            cand.setEvaluation(tmp);
        }
        assertTrue(a.acceptCandidate(cur, cand));
    }

    @Test
    public void testMaximizar_rejectsWhenCandidateIsLower(){
        Strategy.getStrategy().setProblem(makeProblem(ProblemType.Maximizar));
        AcceptNotBad a = new AcceptNotBad();
        State cur = new State(); cur.setEvaluation(new ArrayList<Double>(){{ add(5.0); }});
        State cand = new State(); cand.setEvaluation(new ArrayList<Double>(){{ add(3.0); }});
        assertFalse(a.acceptCandidate(cur, cand));
    }

    @Test
    public void testMinimizar_acceptsWhenCandidateIsLowerOrEqual(){
        Strategy.getStrategy().setProblem(makeProblem(ProblemType.Minimizar));
        AcceptNotBad a = new AcceptNotBad();
        State cur = new State(); cur.setEvaluation(new ArrayList<Double>(){{ add(10.0); }});
        State cand = new State(); cand.setEvaluation(new ArrayList<Double>(){{ add(2.0); }});
        assertTrue(a.acceptCandidate(cur, cand));

        // equal case
        cand.getEvaluation().set(0, 10.0);
        assertTrue(a.acceptCandidate(cur, cand));
    }

    @Test
    public void testMinimizar_rejectsWhenCandidateIsHigher(){
        Strategy.getStrategy().setProblem(makeProblem(ProblemType.Minimizar));
        AcceptNotBad a = new AcceptNotBad();
        State cur = new State(); cur.setEvaluation(new ArrayList<Double>(){{ add(1.0); }});
        State cand = new State(); cand.setEvaluation(new ArrayList<Double>(){{ add(3.0); }});
        assertFalse(a.acceptCandidate(cur, cand));
    }

    @Test 
    public void testEdge_case_equalEvaluations(){
        Strategy.getStrategy().setProblem(makeProblem(ProblemType.Maximizar));
        AcceptNotBad a = new AcceptNotBad();
        State cur = new State(); cur.setEvaluation(new ArrayList<Double>(){{ add(4.0); }});
        State cand = new State(); cand.setEvaluation(new ArrayList<Double>(){{ add(4.0); }});
        assertTrue(a.acceptCandidate(cur, cand));

        Strategy.getStrategy().setProblem(makeProblem(ProblemType.Minimizar));
        cur.getEvaluation().set(0, 6.0);
        cand.getEvaluation().set(0, 6.0);
        assertTrue(a.acceptCandidate(cur, cand));
    }

    @Test
    public void testNegative_maximizar_candidateGreater(){
        Strategy.getStrategy().setProblem(makeProblem(ProblemType.Maximizar));
        AcceptNotBad a = new AcceptNotBad();
        State cur = new State(); cur.setEvaluation(new ArrayList<Double>(){{ add(-2.0); }});
        State cand = new State(); cand.setEvaluation(new ArrayList<Double>(){{ add(-1.0); }});
        assertTrue(a.acceptCandidate(cur, cand)); // -1 > -2
    }

    @Test
    public void testNegative_maximizar_candidateLower(){
        Strategy.getStrategy().setProblem(makeProblem(ProblemType.Maximizar));
        AcceptNotBad a = new AcceptNotBad();
        State cur = new State(); cur.setEvaluation(new ArrayList<Double>(){{ add(-2.0); }});
        State cand = new State(); cand.setEvaluation(new ArrayList<Double>(){{ add(-3.0); }});
        assertFalse(a.acceptCandidate(cur, cand)); // -3 < -2
    }

    @Test
    public void testNegative_minimizar_cases(){
        Strategy.getStrategy().setProblem(makeProblem(ProblemType.Minimizar));
        AcceptNotBad a = new AcceptNotBad();
        State cur = new State(); cur.setEvaluation(new ArrayList<Double>(){{ add(-5.0); }});
        State cand = new State(); cand.setEvaluation(new ArrayList<Double>(){{ add(-6.0); }});
    // For Minimize, candidate <= current should be accepted
    assertTrue(a.acceptCandidate(cur, cand)); // -6 <= -5 -> accepted

    cand.setEvaluation(new ArrayList<Double>(){{ add(-4.0); }});
    // -4 <= -5 is false -> should be rejected
    assertFalse(a.acceptCandidate(cur, cand)); // -4 > -5 -> rejected
    }

    @Test
    public void testEdge_case_zeroEvaluations(){
        Strategy.getStrategy().setProblem(makeProblem(ProblemType.Maximizar));
        AcceptNotBad a = new AcceptNotBad();
        State cur = new State(); cur.setEvaluation(new ArrayList<Double>(){{ add(0.0); }});
        State cand = new State(); cand.setEvaluation(new ArrayList<Double>(){{ add(1.0); }});
        assertTrue(a.acceptCandidate(cur, cand)); // 1 > 0

        {
            ArrayList<Double> tmp = new ArrayList<>(cand.getEvaluation());
            tmp.set(0, -1.0);
            cand.setEvaluation(tmp);
        }
        assertFalse(a.acceptCandidate(cur, cand)); // -1 < 0

        Strategy.getStrategy().setProblem(makeProblem(ProblemType.Minimizar));
        {
            ArrayList<Double> tmpCur = new ArrayList<>(cur.getEvaluation());
            tmpCur.set(0, 0.0);
            cur.setEvaluation(tmpCur);
        }
        {
            ArrayList<Double> tmpCand = new ArrayList<>(cand.getEvaluation());
            tmpCand.set(0, -1.0);
            cand.setEvaluation(tmpCand);
        }
        assertTrue(a.acceptCandidate(cur, cand)); // -1 < 0

        {
            ArrayList<Double> tmpCand2 = new ArrayList<>(cand.getEvaluation());
            tmpCand2.set(0, 1.0);
            cand.setEvaluation(tmpCand2);
        }
        assertFalse(a.acceptCandidate(cur, cand)); // 1 > 0
    }
}
