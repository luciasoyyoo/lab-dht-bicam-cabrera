package metaheuristics.generators;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import metaheurictics.strategy.Strategy;
import problem.definition.ObjetiveFunction;
import problem.definition.Problem;
import problem.definition.State;
import problem.definition.Problem.ProblemType;
import problem.definition.Operator;

public class LimitThresholdTest {

    @BeforeEach
    public void beforeEach(){
        Strategy.destroyExecute();
    }

    private Problem makeProblem(ProblemType type, Operator op){
        Problem p = new Problem();
        p.setTypeProblem(type);
        ArrayList<ObjetiveFunction> funcs = new ArrayList<>();
        funcs.add(new ObjetiveFunction(){
            @Override
            public Double Evaluation(State state) { return 0.0; }
        });
        p.setFunction(funcs);
        p.setOperator(op);
        return p;
    }

    @Test
    public void testConstructorAndTraceAndCounts_areInitializedAndDefensiveCopied(){
        // prepare a simple operator (not used here)
        Operator noop = new Operator(){
            @Override
            public List<State> generatedNewState(State stateCurrent, Integer operatornumber) { return new ArrayList<>(); }
            @Override
            public List<State> generateRandomState(Integer operatornumber) { return new ArrayList<>(); }
        };
        Strategy.getStrategy().setProblem(makeProblem(ProblemType.Maximizar, noop));
        LimitThreshold lt = new LimitThreshold();

        // weight and trace[0] should be initialized to 50.0
        assertEquals(50.0f, lt.getWeight(), 0.0f);
        float[] trace = lt.getTrace();
        assertTrue(trace.length > 0);
        assertEquals(50.0f, trace[0], 0.0f);

        // defensive copy: modifying returned arrays should not change internal state
        trace[0] = 0.0f;
        float[] trace2 = lt.getTrace();
        assertEquals(50.0f, trace2[0], 0.0f);

        int[] countG = lt.getListCountGender();
        int[] countB = lt.getListCountBetterGender();
        assertEquals(0, countG[0]);
        assertEquals(0, countB[0]);
        countG[0] = 5;
        countB[0] = 7;
        int[] countG2 = lt.getListCountGender();
        int[] countB2 = lt.getListCountBetterGender();
        assertEquals(0, countG2[0]);
        assertEquals(0, countB2[0]);
    }

    @Test
    public void testGenerate_selectsGreaterOrSmallerCandidate_basedOnProblemType() throws Exception{
        // create operator that returns two neighbors
        Operator opMax = new Operator(){
            @Override
            public List<State> generatedNewState(State stateCurrent, Integer operatornumber) {
                State s = new State(); s.setEvaluation(new ArrayList<Double>(){{ add(7.0); }});
                return Arrays.asList(s);
            }
            @Override
            public List<State> generateRandomState(Integer operatornumber) { return new ArrayList<>(); }
        };

        // Maximizar -> operator returns a single candidate (7.0)
        Strategy.getStrategy().setProblem(makeProblem(ProblemType.Maximizar, opMax));
        LimitThreshold ltMax = new LimitThreshold();
        State ref = new State(); ref.setEvaluation(new ArrayList<Double>(){{ add(5.0); }});
        ltMax.setStateRef(ref);
        // sanity-check the operator returns the expected neighborhood (single element)
        List<State> neigh = Strategy.getStrategy().getProblem().getOperator().generatedNewState(ref, 0);
        assertNotNull(neigh);
        assertEquals(1, neigh.size());
    State outMax = ltMax.generate(0);
        assertNotNull(outMax, "generate() returned null even though neighborhood had 1 state");
        assertEquals(7.0, outMax.getEvaluation().get(0), 0.0);

        // Minimizar -> operator returns single candidate (3.0)
        Operator opMin = new Operator(){
            @Override
            public List<State> generatedNewState(State stateCurrent, Integer operatornumber) {
                State s = new State(); s.setEvaluation(new ArrayList<Double>(){{ add(3.0); }});
                return Arrays.asList(s);
            }
            @Override
            public List<State> generateRandomState(Integer operatornumber) { return new ArrayList<>(); }
        };
        Strategy.getStrategy().setProblem(makeProblem(ProblemType.Minimizar, opMin));
        LimitThreshold ltMin = new LimitThreshold();
        ltMin.setStateRef(ref);
        List<State> neighMin = Strategy.getStrategy().getProblem().getOperator().generatedNewState(ref, 0);
        assertEquals(1, neighMin.size());
        State outMin = ltMin.generate(0);
        assertNotNull(outMin);
        assertEquals(3.0, outMin.getEvaluation().get(0), 0.0);
    }

    @Test
    public void testUpdateReference_acceptsAndRejectsAccordingToThreshold() throws Exception{
        // operator not used for updateReference
        Operator noop = new Operator(){
            @Override
            public List<State> generatedNewState(State stateCurrent, Integer operatornumber) { return new ArrayList<>(); }
            @Override
            public List<State> generateRandomState(Integer operatornumber) { return new ArrayList<>(); }
        };
        Strategy.getStrategy().setProblem(makeProblem(ProblemType.Maximizar, noop));
        LimitThreshold lt = new LimitThreshold();

        State current = new State(); current.setEvaluation(new ArrayList<Double>(){{ add(10.0); }});
        State candidate = new State(); candidate.setEvaluation(new ArrayList<Double>(){{ add(9.0); }});
        lt.setStateRef(current);

        // if threshold is large (e.g., 2.0), result current - candidate = 1.0 < 2.0 -> accept
        Strategy.getStrategy().setThreshold(2.0);
        lt.updateReference(candidate, 0);
        assertEquals(candidate.getEvaluation().get(0), lt.getReference().getEvaluation().get(0));

        // reset reference
        lt.setStateRef(current);
        // threshold small (e.g., 0.5) -> 1.0 < 0.5 false -> reject (reference stays current)
        Strategy.getStrategy().setThreshold(0.5);
        lt.updateReference(candidate, 0);
        assertEquals(current.getEvaluation().get(0), lt.getReference().getEvaluation().get(0));
    }
}
