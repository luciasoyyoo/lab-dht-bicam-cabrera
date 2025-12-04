package local_search.complement;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import metaheurictics.strategy.Strategy;
import metaheuristics.generators.GeneratorType;
import metaheuristics.generators.ParticleSwarmOptimization;

public class UpdateParameterTest {

    @AfterEach
    public void tearDown() {
        // reset singleton state between tests
        Strategy.destroyExecute();
    }

    @Test
    public void updateParameterIncrements() throws Exception {
        Integer start = 0;
        Integer next = UpdateParameter.updateParameter(start);
        assertEquals(Integer.valueOf(1), next);
        Integer next2 = UpdateParameter.updateParameter(next);
        assertEquals(Integer.valueOf(2), next2);
    }

    @Test
    public void updateParameterSetsGeneticGeneratorWhenMatching() throws Exception {
        // GeneticAlgorithm.countRef is 0, so to get (countRef - 1) after increment we pass -2
        Integer start = Integer.valueOf(-2);
        // ensure Strategy internal structures exist to avoid NPE during generator construction
        Strategy.getStrategy().mapGenerators = new java.util.TreeMap<>();
        Integer result = UpdateParameter.updateParameter(start);
        assertEquals(Integer.valueOf(-1), result);
        // generator should have been set on the Strategy singleton
        assertNotNull(Strategy.getStrategy().generator);
        assertEquals(GeneratorType.GeneticAlgorithm, Strategy.getStrategy().generator.getType());
    }

    @Test
    public void updateParameterSetsParticleSwarmWhenMatchingCountRef() throws Exception {
        // set ParticleSwarmOptimization countRef to a known value and trigger it
        int original = ParticleSwarmOptimization.getCountRef();
        try {
            ParticleSwarmOptimization.setCountRef(5);
            // we need countIterationsCurrent such that after increment equals getCountRef() - 1 => 4
            Integer start = Integer.valueOf(3);
            Strategy.getStrategy().mapGenerators = new java.util.TreeMap<>();
            Integer result = UpdateParameter.updateParameter(start);
            assertEquals(Integer.valueOf(4), result);
            assertNotNull(Strategy.getStrategy().generator);
            assertEquals(GeneratorType.ParticleSwarmOptimization, Strategy.getStrategy().generator.getType());
        } finally {
            // restore original static value to avoid side effects on other tests
            ParticleSwarmOptimization.setCountRef(original);
        }
    }

    @Test
    public void updateParameterNoMatchLeavesGeneratorNull() throws Exception {
        // choose a value that doesn't match any branch after increment
        Integer start = Integer.valueOf(100);
        // ensure mapGenerators exists to avoid constructor NPE when other branches would run
        Strategy.getStrategy().mapGenerators = new java.util.TreeMap<>();
        Integer result = UpdateParameter.updateParameter(start);
        assertEquals(Integer.valueOf(101), result);
        // no branch should have matched, so generator should remain null
        assertNull(Strategy.getStrategy().generator);
    }

    @Test
    public void updateParameterNullInputThrowsNPE() {
        assertThrows(NullPointerException.class, () -> {
            UpdateParameter.updateParameter(null);
        });
    }
}
