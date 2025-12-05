package metaheuristics.generators;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import metaheurictics.strategy.Strategy;
import problem.definition.Problem;
import problem.definition.State;

public class TabuSearchAccessorsTest {

    @BeforeEach
    public void setup() {
        // minimal safe strategy/problem setup used across tests
        Strategy.getStrategy().setProblem(new Problem() {
            private State state = new State();
            { ArrayList<Double> ev = new ArrayList<>(); ev.add(0.0); state.setEvaluation(ev); }
            @Override public State getState() { return state; }
            @Override public void setState(State s) { this.state = s; }
            @Override public void Evaluate(State s) { /* no-op */ }
            @Override public java.util.ArrayList<problem.definition.ObjetiveFunction> getFunction() { return new java.util.ArrayList<>(); }
            @Override public problem.definition.Problem.ProblemType getTypeProblem() { return problem.definition.Problem.ProblemType.Maximizar; }
        });
    }

    @AfterEach
    public void tearDown() {
        Strategy.destroyExecute();
    }

    @Test
    public void testTypeGeneratorAndGetType() {
        TabuSearch ts = new TabuSearch();
        ts.setTypeGenerator(GeneratorType.RandomSearch);
        assertEquals(GeneratorType.RandomSearch, ts.getTypeGenerator());
        // getType() should delegate to the same underlying field
        assertEquals(GeneratorType.RandomSearch, ts.getType());
    }

    @Test
    public void testWeightSetAndGet() {
        TabuSearch ts = new TabuSearch();
        ts.setWeight(123.45f);
        assertEquals(123.45f, ts.getWeight(), 1e-6f);
    }

    @Test
    public void testSetStateRefAndGetReference() {
        TabuSearch ts = new TabuSearch();
        State s = new State(); ArrayList<Object> c = new ArrayList<>(); c.add("x"); s.setCode(c);
        ts.setStateRef(s);
        assertNotNull(ts.getReference());
        assertEquals(s.getCode(), ts.getReference().getCode());
    }

    @Test
    public void testSetTypeCandidate_reflectsInternalField() throws Exception {
        TabuSearch ts = new TabuSearch();
        ts.setTypeCandidate(local_search.candidate_type.CandidateType.RandomCandidate);
        // use reflection to assert private field changed
        Field f = TabuSearch.class.getDeclaredField("typeCandidate");
        f.setAccessible(true);
        Object val = f.get(ts);
        assertEquals(local_search.candidate_type.CandidateType.RandomCandidate, val);
    }
}
