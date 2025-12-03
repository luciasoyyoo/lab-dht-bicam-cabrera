package factory_method;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class FactoryLoaderTest {

    @Test
    public void testGetInstanceSuccess() throws Exception {
        Object o = FactoryLoader.getInstance("evolutionary_algorithms.complement.Probability");
        assertNotNull(o);
        assertTrue(o instanceof evolutionary_algorithms.complement.Probability);
    }

    @Test
    public void testGetInstanceClassNotFound() {
        assertThrows(ClassNotFoundException.class, () -> {
            FactoryLoader.getInstance("no.such.ClassName");
        });
    }

    @Test
    public void testGetInstanceRange() throws Exception {
        Object o = FactoryLoader.getInstance("evolutionary_algorithms.complement.Range");
        assertNotNull(o);
        assertTrue(o instanceof evolutionary_algorithms.complement.Range);
    }

    @Test
    public void testGetInstanceProbability() throws Exception {
        Object o = FactoryLoader.getInstance("evolutionary_algorithms.complement.Probability");
        assertNotNull(o);
        assertTrue(o instanceof evolutionary_algorithms.complement.Probability);
    }

    @Test
    public void testGetInstanceState() throws Exception {
        Object o = FactoryLoader.getInstance("problem.definition.State");
        assertNotNull(o);
        assertTrue(o instanceof problem.definition.State);
    }

    @Test
    public void testGetInstanceTSPState() throws Exception {
        Object o = FactoryLoader.getInstance("config.tspDynamic.TSPState");
        assertNotNull(o);
        assertTrue(o instanceof config.tspDynamic.TSPState);
    }

    @Test
    public void testInstantiationOfAbstractClassFails() {
        assertThrows(InstantiationException.class, () -> {
            FactoryLoader.getInstance("problem.definition.Operator");
        });
    }

    @Test
    public void testNullClassNameThrows() {
        assertThrows(NullPointerException.class, () -> {
            FactoryLoader.getInstance(null);
        });
    }

    @Test
    public void testMultipleCallsReturnDistinctInstances() throws Exception {
        Object a = FactoryLoader.getInstance("evolutionary_algorithms.complement.Probability");
        Object b = FactoryLoader.getInstance("evolutionary_algorithms.complement.Probability");
        assertNotSame(a, b);
    }
}
