package metaheurictics.strategy;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import metaheuristics.generators.Generator;
import metaheuristics.generators.GeneratorType;
import metaheuristics.generators.RandomSearch;
import metaheurictics.strategy.Strategy;
import problem.definition.Problem;
import problem.definition.State;

public class StrategyAdditionalTest {

    @BeforeEach
    public void beforeEach(){
        Strategy.destroyExecute();
    }

    @AfterEach
    public void afterEach(){
        Strategy.destroyExecute();
    }

    @Test
    public void testSingletonAndDestroy_clearsRandomSearchReferences() {
        Strategy s1 = Strategy.getStrategy();
        // add a dummy reference into RandomSearch.listStateReference
        metaheuristics.generators.RandomSearch.listStateReference.add(new State());
        assertFalse(metaheuristics.generators.RandomSearch.listStateReference.isEmpty());

        Strategy.destroyExecute();
        Strategy s2 = Strategy.getStrategy();
        assertNotSame(s1, s2, "destroyExecute should create a fresh Strategy instance on next getStrategy()");
        assertTrue(metaheuristics.generators.RandomSearch.listStateReference.isEmpty(), "destroyExecute must clear RandomSearch.listStateReference");
    }

    @Test
    public void testThresholdBestStateAndCounts() {
        Strategy s = Strategy.getStrategy();
        s.setThreshold(3.1415);
        assertEquals(3.1415, s.getThreshold(), 1e-12);

        State st = new State();
        s.setBestState(st);
        assertSame(st, s.getBestState());

        s.setCountCurrent(5);
        assertEquals(5, s.getCountCurrent());

        s.setCountMax(10);
        assertEquals(10, s.getCountMax());
    }

    @Test
    public void testNewGenerator_returnsConcreteType() throws Exception {
        Strategy s = Strategy.getStrategy();
        // ensure minimal problem exists so generator constructors won't NPE
        s.setProblem(new Problem());

        Generator g = s.newGenerator(GeneratorType.RandomSearch);
        assertNotNull(g);
        assertTrue(g instanceof RandomSearch, "newGenerator(RandomSearch) should return RandomSearch instance");
    }

    @Test
    public void testGetListKey_singleEntry_parsingWorks() throws Exception {
        Strategy s = Strategy.getStrategy();
        s.mapGenerators = new java.util.TreeMap<GeneratorType, Generator>();
        s.mapGenerators.put(GeneratorType.RandomSearch, new RandomSearch());

        java.util.ArrayList<String> keys = s.getListKey();
        assertNotNull(keys);
        assertEquals(1, keys.size());
        assertEquals(GeneratorType.RandomSearch.toString(), keys.get(0));
    }

}
