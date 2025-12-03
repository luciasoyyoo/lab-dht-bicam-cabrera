package metaheuristics.generators;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import problem.definition.State;

public class EvolutionStrategiesTest {

    @BeforeEach
    public void setup() {
        metaheurictics.strategy.Strategy.getStrategy().mapGenerators = new java.util.TreeMap<GeneratorType, Generator>();
    }

    @Test
    public void defaultValues_and_getReference_whenEmpty() {
        EvolutionStrategies es = new EvolutionStrategies();
        assertEquals(GeneratorType.EvolutionStrategies, es.getType());
        assertEquals(50.0f, es.getWeight(), 0.001f);
        // when internal list is empty, getReference should return null
        es.setListStateReference(new ArrayList<>());
        assertNull(es.getReference());
    }

    @Test
    public void setAndGetListStateReference_and_getReference_returnsCopy() {
        EvolutionStrategies es = new EvolutionStrategies();
        // Strategy must have a Problem with a type to allow getReference logic to execute
        problem.definition.Problem p = new problem.definition.Problem();
        p.setTypeProblem(problem.definition.Problem.ProblemType.Maximizar);
        metaheurictics.strategy.Strategy.getStrategy().setProblem(p);
        State s = new State();
        ArrayList<Object> code = new ArrayList<>(); code.add(2);
        s.setCode(code);
        ArrayList<State> list = new ArrayList<>(); list.add(s);
        es.setListStateReference(list);
        State ref = es.getReference();
        assertNotNull(ref);
        assertEquals(s.getCode(), ref.getCode());
    }
}
