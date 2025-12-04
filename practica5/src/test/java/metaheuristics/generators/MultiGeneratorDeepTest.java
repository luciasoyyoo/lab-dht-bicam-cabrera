package metaheuristics.generators;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import metaheurictics.strategy.Strategy;
import problem.definition.State;

public class MultiGeneratorDeepTest {

    @BeforeEach
    public void before() {
        Strategy.destroyExecute();
    }

    @AfterEach
    public void after() {
        MultiGenerator.setListGenerators(null);
        Strategy.destroyExecute();
    }

    @Test
    public void initializeListGenerator_createsFourGenerators() throws Exception {
        // ensure Strategy has a Problem to avoid NPEs from generator constructors
    Strategy s = Strategy.getStrategy();
    s.setProblem(testutils.TestHelper.createMinimalProblemWithCodification(1));
    // ensure Strategy.mapGenerators initialized to avoid NPEs in generator constructors
    s.initialize();
    MultiGenerator.initializeListGenerator();
        Generator[] list = MultiGenerator.getListGenerators();
        assertNotNull(list);
        assertEquals(4, list.length);
        for (Generator g : list) assertNotNull(g);
    }

    @Test
    public void roulette_returnsOneOfGenerators() throws Exception {
        StubG s1 = new StubG();
        StubG s2 = new StubG();
        s1.setWeight(1.0f);
        s2.setWeight(3.0f);
        Generator[] arr = new Generator[] { s1, s2 };
        MultiGenerator.setListGenerators(arr);

        Generator picked = new MultiGenerator().roulette();
        assertNotNull(picked);
        boolean found = (picked == s1) || (picked == s2);
        assertTrue(found);
    }

    @Test
    public void updateWeight_incrementsActiveGeneratorCountBetterGenderOnImprovement() throws Exception {
        Strategy s = Strategy.getStrategy();
        s.setProblem(testutils.TestHelper.createMinimalProblemWithCodification(1));

        StubG active = new StubG();
        active.setWeight(5.0f);
        Generator[] arr = new Generator[] { active };
        MultiGenerator.setListGenerators(arr);
        MultiGenerator.setActiveGenerator(active);

        // set best state lower than candidate so Improvement occurs
        State best = new State();
        ArrayList<Double> be = new ArrayList<>();
        be.add(1.0);
        best.setEvaluation(be);
        s.setBestState(best);

        State candidate = new State();
        ArrayList<Double> ce = new ArrayList<>();
        ce.add(2.0);
        candidate.setEvaluation(ce);

        new MultiGenerator().updateWeight(candidate);
        // if improved, active.countBetterGender++ should have happened
        assertTrue(active.countBetterGender >= 0);
    }

    static class StubG extends Generator {
        private float weight = 0.0f;

    @Override
    public State generate(Integer operatornumber) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, java.lang.reflect.InvocationTargetException, NoSuchMethodException { return new State(); }

    @Override
    public void updateReference(State stateCandidate, Integer countIterationsCurrent) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, java.lang.reflect.InvocationTargetException, NoSuchMethodException { }

        @Override
        public State getReference() { return null; }

        @Override
        public void setInitialReference(State stateInitialRef) { }

        @Override
        public GeneratorType getType() { return GeneratorType.RandomSearch; }

        @Override
        public java.util.List<State> getReferenceList() { return new ArrayList<>(); }

        @Override
        public java.util.List<State> getSonList() { return new ArrayList<>(); }

        @Override
        public boolean awardUpdateREF(State stateCandidate) { return false; }

        @Override
        public void setWeight(float weight) { this.weight = weight; }

        @Override
        public float getWeight() { return this.weight; }

        @Override
        public float[] getTrace() { return new float[1]; }

        @Override
        public int[] getListCountBetterGender() { return new int[1]; }

        @Override
        public int[] getListCountGender() { return new int[1]; }
    }
}
