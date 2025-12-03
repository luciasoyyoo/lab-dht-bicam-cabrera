package metaheuristics.generators;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import problem.definition.State;

public class GeneticAlgorithmTest {

    @BeforeEach
    public void setup() {
        // Ensure Strategy.mapGenerators is initialized to avoid NPE in GeneticAlgorithm constructor
        metaheurictics.strategy.Strategy.getStrategy().mapGenerators = new java.util.TreeMap<GeneratorType, Generator>();
    }

    @Test
    public void defaultWeightAndTrace_shouldBeInitialized() {
        GeneticAlgorithm ga = new GeneticAlgorithm();
        assertEquals(50.0f, ga.getWeight(), 0.0f);
        float[] trace = ga.getTrace();
        assertNotNull(trace);
        assertEquals(1200000, trace.length);
        assertEquals(50.0f, trace[0], 0.0f);
        ga.setWeight(12.5f);
        assertEquals(12.5f, ga.getWeight(), 0.0f);
    }

    @Test
    public void setListState_nullShouldResultInEmptyList() {
        GeneticAlgorithm ga = new GeneticAlgorithm();
        ga.setListState(null);
        List<State> list = ga.getListState();
        assertNotNull(list);
        assertTrue(list.isEmpty());
    }

    @Test
    public void listCountBetterGender_and_getListCountGender_lengths() {
        GeneticAlgorithm ga = new GeneticAlgorithm();
        int[] better = ga.getListCountBetterGender();
        int[] gender = ga.getListCountGender();
        assertNotNull(better);
        assertNotNull(gender);
        assertEquals(10, better.length);
        assertEquals(10, gender.length);
        assertEquals(0, better[0]);
    }
}
