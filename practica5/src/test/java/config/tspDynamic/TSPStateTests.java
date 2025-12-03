package config.tspDynamic;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class TSPStateTests {

    @Test
    public void testDefaultValues() {
        TSPState s = new TSPState();
        // default ints are 0
        assertEquals(0, s.getValue());
        assertEquals(0, s.getIdCity());
    }

    @Test
    public void testSetAndGetValuePositive() {
        TSPState s = new TSPState();
        s.setValue(42);
        assertEquals(42, s.getValue());
    }

    @Test
    public void testSetAndGetValueZero() {
        TSPState s = new TSPState();
        s.setValue(0);
        assertEquals(0, s.getValue());
    }

    @Test
    public void testSetAndGetValueNegative() {
        TSPState s = new TSPState();
        s.setValue(-7);
        assertEquals(-7, s.getValue());
    }

    @Test
    public void testSetAndGetIdCityPositive() {
        TSPState s = new TSPState();
        s.setIdCity(100);
        assertEquals(100, s.getIdCity());
    }

    @Test
    public void testSetAndGetIdCityZero() {
        TSPState s = new TSPState();
        s.setIdCity(0);
        assertEquals(0, s.getIdCity());
    }

    @Test
    public void testSetAndGetIdCityNegative() {
        TSPState s = new TSPState();
        s.setIdCity(-1);
        assertEquals(-1, s.getIdCity());
    }

    @Test
    public void testMultipleSetCallsValue() {
        TSPState s = new TSPState();
        s.setValue(1);
        s.setValue(2);
        s.setValue(3);
        assertEquals(3, s.getValue());
    }

    @Test
    public void testMultipleSetCallsIdCity() {
        TSPState s = new TSPState();
        s.setIdCity(5);
        s.setIdCity(6);
        assertEquals(6, s.getIdCity());
    }

    @Test
    public void testIndependenceBetweenFields() {
        TSPState s = new TSPState();
        s.setValue(11);
        s.setIdCity(22);
        assertEquals(11, s.getValue());
        assertEquals(22, s.getIdCity());
    }

    @Test
    public void testBoundaryLargeValue() {
        TSPState s = new TSPState();
        s.setValue(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, s.getValue());
    }

    @Test
    public void testBoundarySmallValue() {
        TSPState s = new TSPState();
        s.setValue(Integer.MIN_VALUE);
        assertEquals(Integer.MIN_VALUE, s.getValue());
    }

    @Test
    public void testBoundaryLargeIdCity() {
        TSPState s = new TSPState();
        s.setIdCity(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, s.getIdCity());
    }

    @Test
    public void testBoundarySmallIdCity() {
        TSPState s = new TSPState();
        s.setIdCity(Integer.MIN_VALUE);
        assertEquals(Integer.MIN_VALUE, s.getIdCity());
    }

    @Test
    public void testRepeatedGettersDoNotChangeState() {
        TSPState s = new TSPState();
        s.setValue(7);
        s.setIdCity(3);
        for (int i = 0; i < 10; i++) {
            assertEquals(7, s.getValue());
            assertEquals(3, s.getIdCity());
        }
    }

    @Test
    public void testSetZeroAfterNonZero() {
        TSPState s = new TSPState();
        s.setValue(9);
        s.setValue(0);
        assertEquals(0, s.getValue());
    }

    @Test
    public void testSetIdCityAfterNonZero() {
        TSPState s = new TSPState();
        s.setIdCity(9);
        s.setIdCity(0);
        assertEquals(0, s.getIdCity());
    }

    @Test
    public void testSettersWithSameValue() {
        TSPState s = new TSPState();
        s.setValue(4);
        s.setValue(4);
        assertEquals(4, s.getValue());
        s.setIdCity(4);
        s.setIdCity(4);
        assertEquals(4, s.getIdCity());
    }

    @Test
    public void testChainingBehaviorManual() {
        // ensure setting one field doesn't affect the other
        TSPState a = new TSPState();
        a.setValue(1);
        TSPState b = new TSPState();
        b.setIdCity(2);
        assertEquals(1, a.getValue());
        assertEquals(0, a.getIdCity());
        assertEquals(0, b.getValue());
        assertEquals(2, b.getIdCity());
    }

}
