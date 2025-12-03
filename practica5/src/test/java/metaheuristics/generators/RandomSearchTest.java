package metaheuristics.generators;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import problem.definition.State;

public class RandomSearchTest {

    @BeforeEach
    public void setup() {
        // Ensure static list is clean between tests
        RandomSearch.listStateReference.clear();
    }

    @Test
    public void testDefaultTypeGenerator() {
        RandomSearch rs = new RandomSearch();
        assertEquals(GeneratorType.RandomSearch, rs.getType());
        assertEquals(GeneratorType.RandomSearch, rs.getTypeGenerator());
    }

    @Test
    public void testSetAndGetTypeGenerator() {
        RandomSearch rs = new RandomSearch();
        rs.setTypeGenerator(GeneratorType.ParticleSwarmOptimization);
        assertEquals(GeneratorType.ParticleSwarmOptimization, rs.getTypeGenerator());
    }

    @Test
    public void testDefaultWeightAndSetWeight() {
        RandomSearch rs = new RandomSearch();
        assertEquals(50.0f, rs.getWeight(), 0.0001f);
        rs.setWeight(12.5f);
        assertEquals(12.5f, rs.getWeight(), 0.0001f);
    }

    @Test
    public void testGetListCountArraysAreCopies() {
        RandomSearch rs = new RandomSearch();
        int[] arr1 = rs.getListCountBetterGender();
        assertNotNull(arr1);
        assertTrue(arr1.length >= 0);
        // mutate returned array and ensure original is not affected
        int original0 = rs.getListCountBetterGender()[0];
        arr1[0] = original0 + 7;
        int[] arr2 = rs.getListCountBetterGender();
        assertEquals(original0, arr2[0]);
    }

    @Test
    public void testGetTraceContainsWeightAtZero() {
        RandomSearch rs = new RandomSearch();
        float[] trace = rs.getTrace();
        assertNotNull(trace);
        // trace[0] is initialized to weight in constructor
        assertEquals(rs.getWeight(), trace[0], 0.0001f);
    }

    @Test
    public void testGetReferenceListAddsReference() {
        RandomSearch rs = new RandomSearch();
        State s = new State();
        ArrayList<Object> code = new ArrayList<>();
        code.add(1);
        s.setCode(code);
        // initially getReference is null
        assertNull(rs.getReference());
        rs.setInitialReference(s);
        assertNotNull(rs.getReference());
        // getReferenceList should add the reference to static list
        java.util.List<State> list = rs.getReferenceList();
        assertTrue(list.contains(rs.getReference()));
        // static list should also contain it
        assertTrue(RandomSearch.listStateReference.contains(rs.getReference()));
    }

}
