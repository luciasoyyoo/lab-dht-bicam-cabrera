package evolutionary_algorithms.complement;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class ProbabilityTest {

    @Test
    public void testGettersSetters() {
        Probability p = new Probability();
        p.setKey("k");
        p.setValue("v");
        p.setProbability(0.5f);
        assertEquals("k", p.getKey());
        assertEquals("v", p.getValue());
        assertEquals(0.5f, p.getProbability(), 1e-6f);
    }

    @Test
    public void testNullKeyValue() {
        Probability p = new Probability();
        p.setKey(null);
        p.setValue(null);
        assertNull(p.getKey());
        assertNull(p.getValue());
    }

    @Test
    public void testDifferentTypesForKeyValue() {
        Probability p = new Probability();
        p.setKey(123);
        p.setValue(45.6);
        assertEquals(123, p.getKey());
        assertEquals(45.6, p.getValue());
    }

    @Test
    public void testProbabilityZeroAndOne() {
        Probability p = new Probability();
        p.setProbability(0.0f);
        assertEquals(0.0f, p.getProbability(), 1e-6f);
        p.setProbability(1.0f);
        assertEquals(1.0f, p.getProbability(), 1e-6f);
    }

    @Test
    public void testProbabilityOverwrite() {
        Probability p = new Probability();
        p.setProbability(0.2f);
        p.setProbability(0.8f);
        assertEquals(0.8f, p.getProbability(), 1e-6f);
    }

    @Test
    public void testNegativeProbabilityAllowed() {
        Probability p = new Probability();
        p.setProbability(-0.5f);
        assertEquals(-0.5f, p.getProbability(), 1e-6f);
    }

    @Test
    public void testLargeProbabilityAllowed() {
        Probability p = new Probability();
        p.setProbability(2.5f);
        assertEquals(2.5f, p.getProbability(), 1e-6f);
    }

    @Test
    public void testOverwriteKeyValue() {
        Probability p = new Probability();
        p.setKey("a");
        p.setValue("b");
        p.setKey("x");
        p.setValue("y");
        assertEquals("x", p.getKey());
        assertEquals("y", p.getValue());
    }

    @Test
    public void testDefaultProbabilityIsZero() {
        Probability p = new Probability();
        assertEquals(0.0f, p.getProbability(), 1e-6f);
    }
}
