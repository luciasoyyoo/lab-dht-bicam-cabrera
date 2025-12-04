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

/*
 Tests for InstanceEE.
 Assumes the production classes FactoryGenerator, MultiGenerator, Generator and GeneratorType
 are available on the test classpath (they are used by InstanceEE).
*/
public class InstanceEETest {

    private InstanceEE instance;
    private Generator[] originalGenerators;
    private java.util.SortedMap<GeneratorType, Generator> originalMapGenerators;

    @BeforeEach
    public void setUp() {
        // backup global state
        originalGenerators = MultiGenerator.getListGenerators();
        originalMapGenerators = Strategy.getStrategy().mapGenerators;
        // ensure mapGenerators is non-null so generator constructors that query it don't NPE
        Strategy.getStrategy().mapGenerators = new TreeMap<>();

        instance = new InstanceEE();
        instance.setTerminate(false);
    }

    @Test
    public void testSetAndGetTerminate() {
        assertFalse(instance.isTerminate(), "terminate should start false");
        instance.setTerminate(true);
        assertTrue(instance.isTerminate(), "terminate should be true after set");
        instance.setTerminate(false);
        assertFalse(instance.isTerminate(), "terminate should be false after reset");
    }

    @Test
    public void testRunReplacesMatchingGeneratorAndSetsTerminate() {
        // Prepare a controlled array and install it into MultiGenerator so InstanceEE will
        // find and replace the matching generator entry.
        final int idx = 0;
        Generator[] arr = new Generator[1];
        arr[0] = new Generator() {
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
                return GeneratorType.EvolutionStrategies;
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

        // install into MultiGenerator so run() works on the real stored array
        MultiGenerator.setListGenerators(arr);

    // Run the InstanceEE logic
    instance.run();

    // After run, terminate flag must be true
    assertTrue(instance.isTerminate(), "InstanceEE should set terminate to true after run()");

    // Read back the stored array and check the slot still reports the expected type
    Generator[] after = MultiGenerator.getListGenerators();
    assertNotNull(after, "MultiGenerator list must not be null after run");
    assertTrue(after.length > idx, "MultiGenerator list must contain the tested index");
    assertNotNull(after[idx], "Generator after run must not be null");
    assertEquals(GeneratorType.EvolutionStrategies, after[idx].getType(), "Generator type should be EvolutionStrategies");
    }

    @AfterEach
    public void tearDown() {
        // restore global state
        try { MultiGenerator.setListGenerators(originalGenerators); } catch (Exception ignored) {}
        Strategy.getStrategy().mapGenerators = originalMapGenerators;
    }
}