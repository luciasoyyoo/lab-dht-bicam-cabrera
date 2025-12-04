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

public class HillClimbingRestartTest {

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
    public void testConstructor_initializesWeightAndTraceAndCounts(){
        Operator noop = new Operator(){
            @Override
            public List<State> generatedNewState(State stateCurrent, Integer operatornumber) { return new ArrayList<>(); }
            @Override
            public List<State> generateRandomState(Integer operatornumber) { return new ArrayList<>(); }
        };
        Strategy.getStrategy().setProblem(makeProblem(ProblemType.Maximizar, noop));
        HillClimbingRestart hcr = new HillClimbingRestart();
        assertEquals(50.0f, hcr.getTrace()[0], 0.0f);
        assertEquals(0, hcr.getListCountGender()[0]);
        assertEquals(0, hcr.getListCountBetterGender()[0]);

        // defensive copies
        float[] t = hcr.getTrace(); t[0] = 0.0f;
        assertEquals(50.0f, hcr.getTrace()[0], 0.0f);
        int[] c = hcr.getListCountGender(); c[0] = 2;
        assertEquals(0, hcr.getListCountGender()[0]);
    }

    @Test
    public void testGenerate_withoutRestart_usesNeighborhood() throws Exception{
        // Operator that returns a single neighbor
        Operator op = new Operator(){
            @Override
            public List<State> generatedNewState(State stateCurrent, Integer operatornumber) {
                State s = new State(); s.setEvaluation(new ArrayList<Double>(){{ add(42.0); }});
                return Arrays.asList(s);
            }
            @Override
            public List<State> generateRandomState(Integer operatornumber) { return Arrays.asList(new State()); }
        };
        Strategy.getStrategy().setProblem(makeProblem(ProblemType.Maximizar, op));
        // set countCurrent so count (0) != Strategy.countCurrent to avoid restart branch
        Strategy.getStrategy().setCountCurrent(1);

        HillClimbingRestart hcr = new HillClimbingRestart();
        State ref = new State(); ref.setEvaluation(new ArrayList<Double>(){{ add(10.0); }});
        hcr.setStateRef(ref);
        State out = hcr.generate(0);
        assertNotNull(out);
        assertEquals(42.0, out.getEvaluation().get(0), 0.0);
    }

    @Test
    public void testUpdateReference_acceptsAndRejectsAccordingToAcceptBest() throws Exception{
        Operator noop = new Operator(){
            @Override
            public List<State> generatedNewState(State stateCurrent, Integer operatornumber) { return new ArrayList<>(); }
            @Override
            public List<State> generateRandomState(Integer operatornumber) { return new ArrayList<>(); }
        };
        Strategy.getStrategy().setProblem(makeProblem(ProblemType.Maximizar, noop));
        HillClimbingRestart hcr = new HillClimbingRestart();

        State current = new State(); current.setEvaluation(new ArrayList<Double>(){{ add(5.0); }});
        State better = new State(); better.setEvaluation(new ArrayList<Double>(){{ add(6.0); }});
        State worse = new State(); worse.setEvaluation(new ArrayList<Double>(){{ add(4.0); }});

        hcr.setStateRef(current);
        hcr.updateReference(better, 0);
        assertEquals(6.0, hcr.getReference().getEvaluation().get(0), 0.0);

        // reset and test reject
        hcr.setStateRef(current);
        hcr.updateReference(worse, 0);
        assertEquals(5.0, hcr.getReference().getEvaluation().get(0), 0.0);
    }
}
