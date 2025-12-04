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

public class HillClimbingTest {

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
    public void testConstructor_initializesFieldsAndDefensiveCopies(){
        Operator noop = new Operator(){
            @Override
            public List<State> generatedNewState(State stateCurrent, Integer operatornumber) { return new ArrayList<>(); }
            @Override
            public List<State> generateRandomState(Integer operatornumber) { return new ArrayList<>(); }
        };
        Strategy.getStrategy().setProblem(makeProblem(ProblemType.Maximizar, noop));
        HillClimbing hc = new HillClimbing();
        assertEquals(50.0f, hc.getTrace()[0], 0.0f);
        assertEquals(0, hc.getListCountGender()[0]);
        assertEquals(0, hc.getListCountBetterGender()[0]);

        float[] t = hc.getTrace(); t[0] = 0f;
        assertEquals(50.0f, hc.getTrace()[0], 0.0f);
        int[] g = hc.getListCountGender(); g[0] = 9;
        assertEquals(0, hc.getListCountGender()[0]);
    }

    @Test
    public void testGenerate_returnsNeighborhoodCandidate_MaximizarAndMinimizar() throws Exception{
        Operator opMax = new Operator(){
            @Override
            public List<State> generatedNewState(State stateCurrent, Integer operatornumber) {
                State s = new State(); s.setEvaluation(new ArrayList<Double>(){{ add(99.0); }});
                return Arrays.asList(s);
            }
            @Override
            public List<State> generateRandomState(Integer operatornumber) { return new ArrayList<>(); }
        };
        Strategy.getStrategy().setProblem(makeProblem(ProblemType.Maximizar, opMax));
        HillClimbing hcMax = new HillClimbing();
        State ref = new State(); ref.setEvaluation(new ArrayList<Double>(){{ add(50.0); }});
        hcMax.setStateRef(ref);
        State out = hcMax.generate(0);
        assertNotNull(out);
        assertEquals(99.0, out.getEvaluation().get(0), 0.0);

        Operator opMin = new Operator(){
            @Override
            public List<State> generatedNewState(State stateCurrent, Integer operatornumber) {
                State s = new State(); s.setEvaluation(new ArrayList<Double>(){{ add(1.0); }});
                return Arrays.asList(s);
            }
            @Override
            public List<State> generateRandomState(Integer operatornumber) { return new ArrayList<>(); }
        };
        Strategy.getStrategy().setProblem(makeProblem(ProblemType.Minimizar, opMin));
        HillClimbing hcMin = new HillClimbing();
        hcMin.setStateRef(ref);
        State out2 = hcMin.generate(0);
        assertNotNull(out2);
        assertEquals(1.0, out2.getEvaluation().get(0), 0.0);
    }

    @Test
    public void testUpdateReference_acceptsAndRejects() throws Exception{
        Operator noop = new Operator(){
            @Override
            public List<State> generatedNewState(State stateCurrent, Integer operatornumber) { return new ArrayList<>(); }
            @Override
            public List<State> generateRandomState(Integer operatornumber) { return new ArrayList<>(); }
        };
        Strategy.getStrategy().setProblem(makeProblem(ProblemType.Maximizar, noop));
        HillClimbing hc = new HillClimbing();

        State cur = new State(); cur.setEvaluation(new ArrayList<Double>(){{ add(10.0); }});
        State better = new State(); better.setEvaluation(new ArrayList<Double>(){{ add(11.0); }});
        State worse = new State(); worse.setEvaluation(new ArrayList<Double>(){{ add(9.0); }});

        hc.setStateRef(cur);
        hc.updateReference(better, 0);
        assertEquals(11.0, hc.getReference().getEvaluation().get(0), 0.0);

        hc.setStateRef(cur);
        hc.updateReference(worse, 0);
        assertEquals(10.0, hc.getReference().getEvaluation().get(0), 0.0);
    }

    @Test
    public void testGetReferenceList_updatesInternalListAndReturnsCopy(){
        Operator noop = new Operator(){
            @Override
            public List<State> generatedNewState(State stateCurrent, Integer operatornumber) { return new ArrayList<>(); }
            @Override
            public List<State> generateRandomState(Integer operatornumber) { return new ArrayList<>(); }
        };
        Strategy.getStrategy().setProblem(makeProblem(ProblemType.Maximizar, noop));
        HillClimbing hc = new HillClimbing();
        State s1 = new State(); s1.setEvaluation(new ArrayList<Double>(){{ add(2.0); }});
        hc.setStateRef(s1);
        List<State> list1 = hc.getReferenceList();
        assertEquals(1, list1.size());
        // mutate returned list and ensure internal list not affected
        list1.clear();
        List<State> list2 = hc.getReferenceList();
        assertEquals(1, list2.size());
    }
}
