package metaheurictics.strategy;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import metaheuristics.generators.GeneratorType;

public class StrategyTest {

    @Test
    public void initializeGeneratorsCreatesMapAndKeys() throws Exception {
        Strategy s = Strategy.getStrategy();
        // ensure a problem object is present to avoid NPEs inside generator creation
        s.setProblem(testutils.TestHelper.createMinimalProblemWithCodification(1));

        s.initializeGenerators();
        assertNotNull(s.mapGenerators, "mapGenerators must be initialized");
        int expected = GeneratorType.values().length;
        assertEquals(expected, s.mapGenerators.size(), "mapGenerators should contain all GeneratorType entries");

        ArrayList<String> keys = s.getListKey();
        assertNotNull(keys);
        assertEquals(expected, keys.size(), "getListKey should return one key per generator");
    }

    @Test
    public void setAndGetProblemAndCounters() {
        Strategy s = Strategy.getStrategy();
        s.setProblem(testutils.TestHelper.createMinimalProblemWithCodification(1));
        s.setCountMax(10);
        s.setCountCurrent(3);
        assertEquals(10, s.getCountMax());
        assertEquals(3, s.getCountCurrent());
        assertNotNull(s.getProblem());
    }
}
