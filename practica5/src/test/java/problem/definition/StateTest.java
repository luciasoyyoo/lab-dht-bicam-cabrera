package problem.definition;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

public class StateTest {

    @Test
    public void testCloneDeepCopy() {
        ArrayList<Object> code = new ArrayList<Object>();
        code.add("a");
        State s = new State(code);
        ArrayList<Double> eval = new ArrayList<Double>();
        eval.add(1.0);
        s.setEvaluation(eval);
        s.setNumber(42);

        State cloned = s.clone();

        // defensive copies
        assertNotSame(s.getCode(), cloned.getCode());
        assertEquals(s.getCode(), cloned.getCode());

        // modify the original state via setCode and ensure clone remains unaffected
        ArrayList<Object> newOriginalCode = s.getCode();
        newOriginalCode.add("b");
        s.setCode(newOriginalCode);
        assertEquals(2, s.getCode().size());
        assertEquals(1, cloned.getCode().size());

    // evaluation copy: modify original via setEvaluation and ensure clone unaffected
    assertNotSame(s.getEvaluation(), cloned.getEvaluation());
    ArrayList<Double> newEval = s.getEvaluation();
    newEval.add(2.0);
    s.setEvaluation(newEval);
    assertEquals(2, s.getEvaluation().size());
    assertEquals(1, cloned.getEvaluation().size());

        // numbers copied
        assertEquals(42, cloned.getNumber());
    }

    @Test
    public void testGetCopyAndComparatorDistance() {
        ArrayList<Object> code1 = new ArrayList<Object>();
        code1.add(1);
        code1.add(2);
        code1.add(3);
        State s1 = new State(code1);

        ArrayList<Object> code2 = new ArrayList<Object>();
        code2.add(1);
        code2.add(4);
        code2.add(3);
        State s2 = new State(code2);

        // comparator should be false
        assertFalse(s1.Comparator(s2));

        // distance should be 1 (only one differing element)
        double dist = s1.Distance(s2);
        assertEquals(1.0, dist);

        // getCopy returns an independent copy equal in content
        State copy = (State) s1.getCopy();
        assertEquals(s1.getCode(), copy.getCode());
        assertNotSame(s1.getCode(), copy.getCode());
    }
 
    @Test
    public void testDefaultConstructorAndAccessors() {
        State s = new State();
        assertNotNull(s.getCode());
        assertEquals(0, s.getCode().size());
        assertNull(s.getEvaluation());
        assertEquals(0, s.getNumber());
    }

    @Test
    public void testSetCodeNullProducesEmptyList() {
        State s = new State();
        s.setCode(null);
        assertNotNull(s.getCode());
        assertEquals(0, s.getCode().size());
    }

    @Test
    public void testSetEvaluationNullProducesNull() {
        State s = new State();
        s.setEvaluation(null);
        assertNull(s.getEvaluation());
    }

    @Test
    public void testSetAndGetNumber() {
        State s = new State();
        s.setNumber(123);
        assertEquals(123, s.getNumber());
    }

    @Test
    public void testTypeGeneratorSetterGetter() {
        State s = new State();
        s.setTypeGenerator(metaheuristics.generators.GeneratorType.GeneticAlgorithm);
        assertEquals(metaheuristics.generators.GeneratorType.GeneticAlgorithm, s.getTypeGenerator());
    }

    @Test
    public void testComparatorTrueWhenCodesEqual() {
        ArrayList<Object> code = new ArrayList<Object>();
        code.add("x");
        State s1 = new State(code);
        State s2 = new State(code);
        assertTrue(s1.Comparator(s2));
    }

    @Test
    public void testDistanceZeroForSameContent() {
        ArrayList<Object> code = new ArrayList<Object>();
        code.add(5);
        State s1 = new State(code);
        State s2 = new State(code);
        assertEquals(0.0, s1.Distance(s2));
    }

    @Test
    public void testCloneIndependentSetters() {
        ArrayList<Object> code = new ArrayList<Object>();
        code.add("a");
        State s = new State(code);
        State cloned = s.clone();
        ArrayList<Object> modified = cloned.getCode();
        modified.add("z");
        cloned.setCode(modified);
        // original should remain unchanged
        assertEquals(1, s.getCode().size());
        assertEquals(2, cloned.getCode().size());
    }
}
