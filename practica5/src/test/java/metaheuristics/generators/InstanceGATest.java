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

/**
 * Deterministic test for InstanceGA.run().
 * Installs a controlled MultiGenerator array and a safe entry in Strategy.mapGenerators
 * to avoid constructor-time NPEs or ClassCast problems when FactoryGenerator instantiates
 * the real GeneticAlgorithm.
 */
public class InstanceGATest {

    private Generator[] originalGenerators;
    private java.util.SortedMap<GeneratorType, Generator> originalMapGenerators;

    @BeforeEach
    public void setup() {
        originalGenerators = MultiGenerator.getListGenerators();
        originalMapGenerators = Strategy.getStrategy().mapGenerators;

        // Ensure mapGenerators exists and contains a safe GeneticAlgorithm stub.
        TreeMap<GeneratorType, Generator> map = new TreeMap<>();
        // Provide a lightweight GeneticAlgorithm subclass that overrides getListStateRef
        // to avoid querying Strategy.mapGenerators during construction.
        GeneticAlgorithm safeGA = new GeneticAlgorithm() {
            @Override
            public List<State> getListStateRef() {
                return new ArrayList<>();
            }
        };
        map.put(GeneratorType.GeneticAlgorithm, safeGA);
        Strategy.getStrategy().mapGenerators = map;

        // Prepare a MultiGenerator array where index 0 reports the GeneticAlgorithm type
        Generator[] arr = new Generator[1];
        arr[0] = new Generator() {
            @Override public State generate(Integer operatornumber) { return new State(); }
            @Override public void updateReference(State stateCandidate, Integer countIterationsCurrent) {}
            @Override public State getReference() { return null; }
            @Override public void setInitialReference(State stateInitialRef) {}
            @Override public GeneratorType getType() { return GeneratorType.GeneticAlgorithm; }
            @Override public List<State> getReferenceList() { return java.util.Collections.emptyList(); }
            @Override public List<State> getSonList() { return java.util.Collections.emptyList(); }
            @Override public boolean awardUpdateREF(State stateCandidate) { return false; }
            @Override public void setWeight(float weight) {}
            @Override public float getWeight() { return 1.0f; }
            @Override public float[] getTrace() { return new float[0]; }
            @Override public int[] getListCountBetterGender() { return new int[0]; }
            @Override public int[] getListCountGender() { return new int[0]; }
        };
        MultiGenerator.setListGenerators(arr);
    }

    @AfterEach
    public void tearDown() {
        try { MultiGenerator.setListGenerators(originalGenerators); } catch (Exception ignored) {}
        Strategy.getStrategy().mapGenerators = originalMapGenerators;
    }

    @Test
    public void testRunSetsTerminateAndLeavesTypeObservable() {
        InstanceGA instance = new InstanceGA();
        instance.run();
        assertTrue(instance.isTerminate(), "InstanceGA must set terminate=true after run()");

        Generator[] after = MultiGenerator.getListGenerators();
        assertNotNull(after, "MultiGenerator list must not be null");
        assertTrue(after.length > 0, "MultiGenerator array must contain at least one element");
        assertNotNull(after[0], "Slot 0 must not be null");
        assertEquals(GeneratorType.GeneticAlgorithm, after[0].getType(), "Slot 0 should report GeneticAlgorithm type");
    }
}
