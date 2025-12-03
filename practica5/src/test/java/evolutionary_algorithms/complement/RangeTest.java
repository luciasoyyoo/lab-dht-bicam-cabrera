package evolutionary_algorithms.complement;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class RangeTest {

    @Test
    public void testGettersSetters() {
        Range r = new Range();
        Probability p = new Probability();
        p.setKey("k");
        p.setValue("v");
        p.setProbability(0.25f);

        r.setData(p);
        r.setMax(10.5f);
        r.setMin(1.2f);

        assertSame(p, r.getData());
        assertEquals(10.5f, r.getMax(), 1e-6f);
        assertEquals(1.2f, r.getMin(), 1e-6f);
    }

    @Test
    public void testDefaultMinMaxAndDataNull() {
        Range r = new Range();
        assertNull(r.getData());
        assertEquals(0.0f, r.getMax(), 1e-6f);
        assertEquals(0.0f, r.getMin(), 1e-6f);
    }

    @Test
    public void testSetDataNull() {
        Range r = new Range();
        r.setData(null);
        assertNull(r.getData());
    }

    @Test
    public void testMinGreaterThanMaxAccepted() {
        Range r = new Range();
        r.setMin(5.0f);
        r.setMax(1.0f);
        assertEquals(5.0f, r.getMin(), 1e-6f);
        assertEquals(1.0f, r.getMax(), 1e-6f);
    }

    @Test
    public void testSetMaxMultipleTimes() {
        Range r = new Range();
        r.setMax(1.1f);
        r.setMax(2.2f);
        assertEquals(2.2f, r.getMax(), 1e-6f);
    }

    @Test
    public void testSetMinMultipleTimes() {
        Range r = new Range();
        r.setMin(-1.0f);
        r.setMin(-2.0f);
        assertEquals(-2.0f, r.getMin(), 1e-6f);
    }

    @Test
    public void testDataReferenceIsSameObject() {
        Range r = new Range();
        Probability p = new Probability();
        p.setProbability(0.9f);
        r.setData(p);
        p.setProbability(0.1f);
        // r.getData returns the same reference stored
        assertEquals(0.1f, r.getData().getProbability(), 1e-6f);
    }

    @Test
    public void testFloatPrecision() {
        Range r = new Range();
        r.setMin(0.1234567f);
        r.setMax(9.8765432f);
        assertEquals(0.1234567f, r.getMin(), 1e-7f);
        assertEquals(9.8765432f, r.getMax(), 1e-7f);
    }
}
