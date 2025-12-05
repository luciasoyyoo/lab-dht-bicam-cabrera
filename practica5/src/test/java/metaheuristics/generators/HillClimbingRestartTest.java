package metaheuristics.generators;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
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
    public void setup(){
        Strategy.destroyExecute();
    }

    @AfterEach
    public void teardown(){
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
        // set a base state so getState()/Evaluate can operate
        State base = new State(); base.setEvaluation(new ArrayList<Double>(){{ add(0.0); }});
        p.setState(base);
        return p;
    }

    @Test
    public void testSetInitialReferenceAndGetReferenceDefensiveCopy() {
        Operator noop = new Operator(){
            @Override
            public List<State> generatedNewState(State stateCurrent, Integer operatornumber) { return new ArrayList<>(); }
            @Override
            public List<State> generateRandomState(Integer operatornumber) { return new ArrayList<>(); }
        };
        Strategy.getStrategy().setProblem(makeProblem(ProblemType.Maximizar, noop));

        HillClimbingRestart hc = new HillClimbingRestart();

        State s = new State();
        s.setEvaluation(new ArrayList<Double>(){{ add(7.0); }});
        hc.setInitialReference(s);

        // getReference returns a defensive copy
        State ref1 = hc.getReference();
        assertNotNull(ref1);
        assertEquals(7.0, ref1.getEvaluation().get(0));

        // mutate returned reference and ensure internal not affected
        ref1.getEvaluation().set(0, 100.0);
        State ref2 = hc.getReference();
        assertEquals(7.0, ref2.getEvaluation().get(0));
    }

    @Test
    public void testSetStateRef_and_getReference_defensiveCopy() {
        Operator noop = new Operator(){
            @Override
            public List<State> generatedNewState(State stateCurrent, Integer operatornumber) { return new ArrayList<>(); }
            @Override
            public List<State> generateRandomState(Integer operatornumber) { return new ArrayList<>(); }
        };
        Strategy.getStrategy().setProblem(makeProblem(ProblemType.Maximizar, noop));

        HillClimbingRestart hc = new HillClimbingRestart();

        State s = new State();
        s.setEvaluation(new ArrayList<Double>(){{ add(3.0); }});
        hc.setStateRef(s);

        State r = hc.getReference();
        assertNotNull(r);
        assertEquals(3.0, r.getEvaluation().get(0));

        // change original state passed to setStateRef and ensure internal copy preserved
        s.getEvaluation().set(0, -1.0);
        State r2 = hc.getReference();
        assertEquals(3.0, r2.getEvaluation().get(0));
    }

    @Test
    public void testGetReferenceList_addsOnlyOnChange() {
        Operator noop = new Operator(){
            @Override
            public List<State> generatedNewState(State stateCurrent, Integer operatornumber) { return new ArrayList<>(); }
            @Override
            public List<State> generateRandomState(Integer operatornumber) { return new ArrayList<>(); }
        };
        Strategy.getStrategy().setProblem(makeProblem(ProblemType.Maximizar, noop));

        HillClimbingRestart hc = new HillClimbingRestart();
        State s1 = new State(); s1.setEvaluation(new ArrayList<Double>(){{ add(1.0); }});
        hc.setInitialReference(s1);
        List<State> l1 = hc.getReferenceList();
        assertEquals(1, l1.size());

        // calling again without change shouldn't add
        List<State> l2 = hc.getReferenceList();
        assertEquals(1, l2.size());

        // change reference and ensure new entry added
        State s2 = new State(); s2.setEvaluation(new ArrayList<Double>(){{ add(2.0); }});
        hc.setInitialReference(s2);
        List<State> l3 = hc.getReferenceList();
        assertTrue(l3.size() >= 2);
        assertEquals(2.0, l3.get(l3.size()-1).getEvaluation().get(0));
    }

    @Test
    public void testGeneratorType_setAndGet() {
        Operator noop = new Operator(){
            @Override
            public List<State> generatedNewState(State stateCurrent, Integer operatornumber) { return new ArrayList<>(); }
            @Override
            public List<State> generateRandomState(Integer operatornumber) { return new ArrayList<>(); }
        };
        Strategy.getStrategy().setProblem(makeProblem(ProblemType.Maximizar, noop));

        HillClimbingRestart hc = new HillClimbingRestart();
        hc.setGeneratorType(GeneratorType.HillClimbing);
        assertEquals(GeneratorType.HillClimbing, hc.getType());
    }

    @Test
    public void testTraceAndCountArrays_returnDefensiveCopies() {
        Operator noop = new Operator(){
            @Override
            public List<State> generatedNewState(State stateCurrent, Integer operatornumber) { return new ArrayList<>(); }
            @Override
            public List<State> generateRandomState(Integer operatornumber) { return new ArrayList<>(); }
        };
        Strategy.getStrategy().setProblem(makeProblem(ProblemType.Maximizar, noop));

        HillClimbingRestart hc = new HillClimbingRestart();
        float[] trace = hc.getTrace();
        int[] gender = hc.getListCountGender();
        int[] better = hc.getListCountBetterGender();

        // mutate returned arrays
        trace[0] = -999f; gender[0] = 123; better[0] = 321;

        // subsequent calls must return original values (constructor initialized trace[0]=50, counts 0)
        assertEquals(50.0f, hc.getTrace()[0], 0.0f);
        assertEquals(0, hc.getListCountGender()[0]);
        assertEquals(0, hc.getListCountBetterGender()[0]);
    }
}
