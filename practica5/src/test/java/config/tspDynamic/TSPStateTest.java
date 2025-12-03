package config.tspDynamic;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class TSPStateTest {

    @Test
    public void testGettersSetters() {
        TSPState s = new TSPState();
        s.setValue(7);
        s.setIdCity(42);
        assertEquals(7, s.getValue());
        assertEquals(42, s.getIdCity());
    }

    @Test
    public void testDefaultValues() {
        TSPState s = new TSPState();
        assertEquals(0, s.getValue());
        assertEquals(0, s.getIdCity());
    }

    @Test
    public void testMultipleUpdates() {
        TSPState s = new TSPState();
        s.setValue(1);
        s.setValue(2);
        s.setIdCity(10);
        s.setIdCity(20);
        assertEquals(2, s.getValue());
        assertEquals(20, s.getIdCity());
    }

    @Test
    public void testNegativeValues() {
        TSPState s = new TSPState();
        s.setValue(-5);
        s.setIdCity(-1);
        assertEquals(-5, s.getValue());
        assertEquals(-1, s.getIdCity());
    }

    @Test
    public void testZeroValues() {
        TSPState s = new TSPState();
        s.setValue(0);
        s.setIdCity(0);
        assertEquals(0, s.getValue());
        assertEquals(0, s.getIdCity());
    }

    @Test
    public void testInstancesIndependent() {
        TSPState a = new TSPState();
        TSPState b = new TSPState();
        a.setValue(3);
        b.setValue(4);
        assertEquals(3, a.getValue());
        assertEquals(4, b.getValue());
    }

    @Test
    public void testBoundaryLargeValue() {
        TSPState s = new TSPState();
        s.setValue(Integer.MAX_VALUE);
        s.setIdCity(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, s.getValue());
        assertEquals(Integer.MAX_VALUE, s.getIdCity());
    }

    @Test
    public void testRepeatedSetGet() {
        TSPState s = new TSPState();
        for (int i = 0; i < 5; i++) {
            s.setValue(i);
            s.setIdCity(i*2);
            assertEquals(i, s.getValue());
            assertEquals(i*2, s.getIdCity());
        }
    }
}
