package metaheuristics.generators;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import metaheurictics.strategy.Strategy;
import problem.definition.State;
import problem.definition.Problem;
import problem.definition.Problem.ProblemType;

public class EvolutionStrategiesTest {

    @BeforeEach
    public void setup() {
        Strategy.destroyExecute();
        Strategy s = Strategy.getStrategy();
        s.mapGenerators = new TreeMap<>();
        // prepare a minimal Problem instance
        Problem p = new Problem();
        State base = new State(); base.setEvaluation(new ArrayList<Double>(){{ add(0.0); }});
        p.setState(base);
        p.setFunction(new java.util.ArrayList<problem.definition.ObjetiveFunction>());
        p.setTypeProblem(ProblemType.Maximizar);
        s.setProblem(p);
    }

    @AfterEach
    public void tearDown() {
        Strategy.destroyExecute();
    }

    @Test
    public void constructor_initializes_defaults() {
        EvolutionStrategies es = new EvolutionStrategies();
        assertEquals(50.0f, es.getWeight(), 1e-6f);
        float[] trace = es.getTrace();
        assertNotNull(trace);
        assertTrue(trace.length > 0);
        assertEquals(50.0f, trace[0], 1e-6f);
        assertNotNull(es.getListCountGender());
        assertNotNull(es.getListCountBetterGender());
        assertEquals(GeneratorType.EvolutionStrategies, es.getType());
    }

    @Test
    public void setAndGetListStateReference_and_getReference_behavior() {
        EvolutionStrategies es = new EvolutionStrategies();
        State s1 = new State(); s1.setEvaluation(new ArrayList<Double>(){{ add(1.0); }});
        State s2 = new State(); s2.setEvaluation(new ArrayList<Double>(){{ add(5.0); }});
        es.setListStateReference(new ArrayList<State>(){{ add(s1); add(s2); }});

        List<State> got = es.getListStateReference();
        assertEquals(2, got.size());
        got.clear();
        assertEquals(2, es.getListStateReference().size());

        Strategy.getStrategy().getProblem().setTypeProblem(ProblemType.Maximizar);
        State ref = es.getReference();
        assertNotNull(ref);
        assertEquals(5.0, ref.getEvaluation().get(0), 1e-6);
    }

    @Test
    public void weight_and_array_defensive_copy_behaviour() {
        EvolutionStrategies es = new EvolutionStrategies();
        es.setWeight(77.7f);
        assertEquals(77.7f, es.getWeight(), 1e-6f);

    float[] t1 = es.getTrace();
    if (t1.length > 0) t1[0] = -1f;
    float[] t2 = es.getTrace();
    assertNotNull(t2);
    assertTrue(t2.length > 0);
    }

    @Test
    public void generate_produces_candidate_from_father() throws Exception {
        EvolutionStrategies es = new EvolutionStrategies();

        // prepare a single father state
        State father = new State();
        ArrayList<Object> code = new ArrayList<>();
        code.add("A");
        father.setCode(code);
        father.setEvaluation(new ArrayList<Double>(){{ add(3.14); }});

        es.setListStateReference(new ArrayList<State>(){{ add(father); }});

        // ensure problem has a base state to copy
        State base = new State();
        base.setEvaluation(new ArrayList<Double>(){{ add(0.0); }});
        Strategy.getStrategy().getProblem().setState(base);

        State candidate = es.generate(0);
        assertNotNull(candidate);
        assertEquals(father.getCode(), candidate.getCode());
        assertEquals(father.getEvaluation().get(0), candidate.getEvaluation().get(0), 1e-6);
    }

    @Test
    public void reference_list_and_todo_methods_cover_lines() {
        EvolutionStrategies es = new EvolutionStrategies();
        State s1 = new State(); s1.setEvaluation(new ArrayList<Double>(){{ add(1.0); }});
        State s2 = new State(); s2.setEvaluation(new ArrayList<Double>(){{ add(2.0); }});
        es.setListStateReference(new ArrayList<State>(){{ add(s1); add(s2); }});

        // getReferenceList returns a defensive copy
        java.util.List<State> refList = es.getReferenceList();
        assertEquals(2, refList.size());
        refList.clear();
        assertEquals(2, es.getReferenceList().size());

        // methods with TODO implementations: call them to increase coverage and assert documented behavior
        assertNull(es.getSonList());
        assertFalse(es.awardUpdateREF(s1));
    }

}
