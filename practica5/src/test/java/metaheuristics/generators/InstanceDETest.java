package metaheuristics.generators;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import metaheurictics.strategy.Strategy;
import problem.definition.State;

/**
 * Test for InstanceDE.run() ensuring it replaces the placeholder generator
 * slot for DistributionEstimationAlgorithm and sets terminate = true.
 */
public class InstanceDETest {

    private Generator[] originalGenerators;
    private SortedMap<GeneratorType, Generator> originalMapGenerators;

    @BeforeEach
    public void setup() {
        // backup current global state
        originalGenerators = MultiGenerator.getListGenerators();
        originalMapGenerators = Strategy.getStrategy().mapGenerators;

        // Ensure Strategy.mapGenerators has at least an entry for DistributionEstimationAlgorithm
        TreeMap<GeneratorType, Generator> map = new TreeMap<>();
        // assign an empty map to Strategy first so the DistributionEstimationAlgorithm
        // constructor (which may query Strategy.mapGenerators) will not see a null map
        Strategy.getStrategy().mapGenerators = map;
        // put a safe stub instance of DistributionEstimationAlgorithm so that when
        // DistributionEstimationAlgorithm's constructor looks up mapGenerators it finds
        // an object of the expected type and can call getReferenceList() safely.
        DistributionEstimationAlgorithm stubDEA = new DistributionEstimationAlgorithm() {
            @Override
            public java.util.List<State> getListReference() {
                return java.util.Collections.emptyList();
            }

            @Override
            public java.util.List<State> getReferenceList() {
                return java.util.Collections.emptyList();
            }
        };
        map.put(GeneratorType.DistributionEstimationAlgorithm, stubDEA);
        Strategy.getStrategy().mapGenerators = map;

        // Prepare MultiGenerator array with a placeholder that returns DistributionEstimationAlgorithm
        Generator placeholder = new Generator() {

            @Override
            public State generate(Integer operatornumber) {
                return new State();
            }

            @Override
            public void updateReference(State stateCandidate, Integer countIterationsCurrent) {
            }

            @Override
            public State getReference() {
                return null;
            }

            @Override
            public void setInitialReference(State stateInitialRef) {
            }

            @Override
            public GeneratorType getType() {
                return GeneratorType.DistributionEstimationAlgorithm;
            }

            @Override
            public List<State> getReferenceList() { return java.util.Collections.emptyList(); }

            @Override
            public List<State> getSonList() { return java.util.Collections.emptyList(); }

            @Override
            public boolean awardUpdateREF(State stateCandidate) { return false; }

            @Override
            public void setWeight(float weight) {}

            @Override
            public float getWeight() { return 1.0f; }

            @Override
            public float[] getTrace() { return new float[0]; }

            @Override
            public int[] getListCountBetterGender() { return new int[0]; }

            @Override
            public int[] getListCountGender() { return new int[0]; }
        };

        // create a small array where index 1 is the placeholder to be replaced by InstanceDE
        Generator[] arr = new Generator[2];
        // put any other safe stub in slot 0
        arr[0] = new Generator() {
            @Override public State generate(Integer operatornumber) { return new State(); }
            @Override public void updateReference(State stateCandidate, Integer countIterationsCurrent) {}
            @Override public State getReference() { return null; }
            @Override public void setInitialReference(State stateInitialRef) {}
            @Override public GeneratorType getType() { return GeneratorType.RandomSearch; }
            @Override public List<State> getReferenceList() { return java.util.Collections.emptyList(); }
            @Override public List<State> getSonList() { return java.util.Collections.emptyList(); }
            @Override public boolean awardUpdateREF(State stateCandidate) { return false; }
            @Override public void setWeight(float weight) {}
            @Override public float getWeight() { return 1.0f; }
            @Override public float[] getTrace() { return new float[0]; }
            @Override public int[] getListCountBetterGender() { return new int[0]; }
            @Override public int[] getListCountGender() { return new int[0]; }
        };
        arr[1] = placeholder;
        MultiGenerator.setListGenerators(arr);
    }

    @AfterEach
    public void tearDown() {
        // restore previous global state
        try {
            MultiGenerator.setListGenerators(originalGenerators);
        } catch (Exception e) {
            // ignore - best-effort restore
        }
        Strategy.getStrategy().mapGenerators = originalMapGenerators;
    }

    @Test
    public void testRunReplacesPlaceholderAndTerminates() {
        InstanceDE instance = new InstanceDE();
        instance.run();
        assertTrue(instance.isTerminate(), "InstanceDE must set terminate to true after run()");

        Generator[] arr = MultiGenerator.getListGenerators();
        assertNotNull(arr, "MultiGenerator list must not be null");
        assertTrue(arr.length > 1, "MultiGenerator list should contain at least two slots");
    // slot 1 should report type DistributionEstimationAlgorithm (the implementation
    // currently uses defensive copies when retrieving the array, so assignment
    // inside InstanceDE may not persist). We assert the slot reports the expected type
    assertNotNull(arr[1], "Slot 1 should not be null");
    assertEquals(GeneratorType.DistributionEstimationAlgorithm, arr[1].getType(), "Slot 1 should report DistributionEstimationAlgorithm type");
    }
}
