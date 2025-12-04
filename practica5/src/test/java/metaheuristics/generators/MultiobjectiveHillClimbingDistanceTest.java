package metaheuristics.generators;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import problem.definition.State;

public class MultiobjectiveHillClimbingDistanceTest {

    @Test
    public void defaultsAndGetters() {
        MultiobjectiveHillClimbingDistance m = new MultiobjectiveHillClimbingDistance();
        // sizeNeighbors constant
        assertEquals(10, MultiobjectiveHillClimbingDistance.getSizeNeighbors());
        // generator type default
        assertEquals(GeneratorType.MultiobjectiveHillClimbingDistance, m.getGeneratorType());
        // trace contains initial weight (50f)
        float[] trace = m.getTrace();
        assertNotNull(trace);
        assertTrue(trace.length >= 1);
        assertEquals(50.0f, trace[0], 0.0001f);
        // getSonList returns null by contract
        assertNull(m.getSonList());
    }

    @Test
    public void setAndGetReferenceAndList() {
        MultiobjectiveHillClimbingDistance m = new MultiobjectiveHillClimbingDistance();
        State s = new State();
        ArrayList<Object> code = new ArrayList<>();
        code.add(Integer.valueOf(1));
        s.setCode(code);

        m.setInitialReference(s);
        State ref = m.getReference();
        assertNotNull(ref);
        assertTrue(ref.Comparator(s));

        List<State> refs = m.getReferenceList();
        assertNotNull(refs);
        // getReferenceList should have added a clone of reference
        assertTrue(refs.size() >= 1);
        assertTrue(refs.get(0).Comparator(ref));
    }

    @Test
    public void distanceCalculateAdd_singleAndTwoStates() {
        // Reset static distanceSolution to a clean state
        MultiobjectiveHillClimbingDistance.distanceSolution = new ArrayList<Double>();

        // Single solution: should return the (empty) static list
        State s1 = new State();
        ArrayList<Object> c1 = new ArrayList<>();
        c1.add("A");
        s1.setCode(c1);
        List<State> single = new ArrayList<>();
        single.add(s1);

        List<Double> resSingle = MultiobjectiveHillClimbingDistance.distanceCalculateAdd(single);
        assertNotNull(resSingle);
        assertEquals(0, resSingle.size());

        // Two solutions: prepare static distanceSolution with one zero entry
        MultiobjectiveHillClimbingDistance.distanceSolution = new ArrayList<Double>();
        MultiobjectiveHillClimbingDistance.distanceSolution.add(Double.valueOf(0.0));

        State s2 = new State();
        ArrayList<Object> c2 = new ArrayList<>();
        c2.add("B");
        s2.setCode(c2);

        List<State> two = new ArrayList<>();
        two.add(s1);
        two.add(s2);

        List<Double> res = MultiobjectiveHillClimbingDistance.distanceCalculateAdd(two);
        // Distance between s1 and s2 is 1.0 (one differing element)
        assertNotNull(res);
        assertEquals(2, res.size());
        assertEquals(1.0, res.get(0), 0.0001);
        assertEquals(1.0, res.get(1), 0.0001);
    }
}
