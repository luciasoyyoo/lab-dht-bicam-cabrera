package metaheurictics.strategy;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import metaheuristics.generators.Generator;
import metaheuristics.generators.GeneratorType;
import metaheuristics.generators.MultiGenerator;
import problem.definition.State;

/**
 * Deeper tests for Strategy small methods using safe stub Generators and controlled state.
 */
public class StrategyDeepTest {

    @BeforeEach
    public void setup() {
        Strategy.destroyExecute();
        Strategy s = Strategy.getStrategy();
        s.setProblem(testutils.TestHelper.createMinimalProblemWithCodification(1));
    }

    @AfterEach
    public void teardown() {
        MultiGenerator.setListGenerators(null);
        Strategy.destroyExecute();
    }

    @Test
    public void initializeGenerators_populatesMapGenerators() throws Exception {
        Strategy s = Strategy.getStrategy();
        s.initializeGenerators();
        assertNotNull(s.mapGenerators);
        assertEquals(GeneratorType.values().length, s.mapGenerators.size());
    }

    @Test
    public void updateCountGender_updatesArraysWithoutException() throws Exception {
        Strategy s = Strategy.getStrategy();
        // create two stub generators with listCount arrays length >=1
        StubGen g1 = new StubGen(GeneratorType.RandomSearch);
        StubGen g2 = new StubGen(GeneratorType.GeneticAlgorithm);
        g1.countGender = 3;
        g2.countGender = 5;
        Generator[] arr = new Generator[] { g1, g2 };
        MultiGenerator.setListGenerators(arr);

        // ensure periodo (private) is 0 to index array[0]
        // default is 0, but set explicitly via reflection for robustness
        Field periodoField = Strategy.class.getDeclaredField("periodo");
        periodoField.setAccessible(true);
        periodoField.setInt(s, 0);

        s.updateCountGender();

        assertEquals(3, g1.getListCountGender()[0]);
        assertEquals(5, g2.getListCountGender()[0]);
    }

    @Test
    public void updateWeight_setsWeightsTo50ForNonMulti() throws Exception {
        Strategy s = Strategy.getStrategy();
        StubGen g1 = new StubGen(GeneratorType.RandomSearch);
        StubGen g2 = new StubGen(GeneratorType.GeneticAlgorithm);
        g1.setWeight(2.0f);
        g2.setWeight(7.0f);
        Generator[] arr = new Generator[] { g1, g2 };
        MultiGenerator.setListGenerators(arr);

        s.updateWeight();

        assertEquals(50.0f, g1.getWeight());
        assertEquals(50.0f, g2.getWeight());
    }

    // Simple stub generator to exercise arrays and weight
    static class StubGen extends Generator {
        private GeneratorType type;
    // use inherited countGender/countBetterGender from Generator (do not redeclare)
    private int[] listCountGender = new int[] { 0 };
    private int[] listCountBetterGender = new int[] { 0 };
        private float weight = 0.0f;

        StubGen(GeneratorType t) { this.type = t; }

    @Override
    public State generate(Integer operatornumber) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, java.lang.reflect.InvocationTargetException, NoSuchMethodException { return new State(); }

    @Override
    public void updateReference(State stateCandidate, Integer countIterationsCurrent) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, java.lang.reflect.InvocationTargetException, NoSuchMethodException { }

        @Override
        public State getReference() { return null; }

        @Override
        public void setInitialReference(State stateInitialRef) { }

        @Override
        public GeneratorType getType() { return type; }

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
        public float[] getTrace() { return new float[10]; }

    @Override
    public int[] getListCountBetterGender() { return listCountBetterGender; }

    @Override
    public int[] getListCountGender() { return listCountGender; }
    }

}
