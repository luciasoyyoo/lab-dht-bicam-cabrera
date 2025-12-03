package metaheurictics.strategy;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import metaheuristics.generators.Generator;
import metaheuristics.generators.GeneratorType;
import metaheuristics.generators.MultiGenerator;
import problem.definition.Problem;
import problem.definition.State;

/**
 * Safe, low-risk unit tests for small Strategy behaviors.
 */
public class SafeStrategyUnitTest {

    @BeforeEach
    public void setup() {
        // ensure singleton is fresh
        Strategy.destroyExecute();
        Strategy s = Strategy.getStrategy();
        // set a minimal problem placeholder so methods that access problem won't NPE
        s.setProblem(new MinimalProblemForTests());
    }

    @AfterEach
    public void teardown() {
        MultiGenerator.setListGenerators(null);
        Strategy.destroyExecute();
    }

    @Test
    public void testGetListKey_returnsAllKeysInMap() {
        Strategy s = Strategy.getStrategy();
        // prepare mapGenerators with two entries
        s.mapGenerators = new java.util.TreeMap<>();
        s.mapGenerators.put(GeneratorType.RandomSearch, new StubGenerator(GeneratorType.RandomSearch));
        s.mapGenerators.put(GeneratorType.GeneticAlgorithm, new StubGenerator(GeneratorType.GeneticAlgorithm));

        java.util.ArrayList<String> keys = s.getListKey();
        assertEquals(2, keys.size());
        // keys should contain the enum names
        assertTrue(keys.contains(GeneratorType.RandomSearch.toString()));
        assertTrue(keys.contains(GeneratorType.GeneticAlgorithm.toString()));
    }

    @Test
    public void testUpdateWeight_setsAllNonMultiGeneratorWeightsTo50() {
        Strategy s = Strategy.getStrategy();
        // create two stub generators and set into MultiGenerator
        Generator[] arr = new Generator[2];
        StubGenerator g1 = new StubGenerator(GeneratorType.RandomSearch);
        StubGenerator g2 = new StubGenerator(GeneratorType.GeneticAlgorithm);
        arr[0] = g1;
        arr[1] = g2;
        MultiGenerator.setListGenerators(arr);

        // ensure starting weights are different
        g1.setWeight(1.0f);
        g2.setWeight(2.0f);

        s.updateWeight();

        assertEquals(50.0f, g1.getWeight());
        assertEquals(50.0f, g2.getWeight());
    }

    @Test
    public void testCalculateOffLinePerformance_storesValue() {
        Strategy s = Strategy.getStrategy();
        s.countPeriodChange = 2; // avoid division by zero
        float sumMax = 10.0f;
        int index = 0;
        s.calculateOffLinePerformance(sumMax, index);
        assertEquals(5.0f, s.listOfflineError[index]);
    }

    // Minimal Problem implementation used only for tests to avoid NPEs
    static class MinimalProblemForTests extends Problem {
        public MinimalProblemForTests() {
            super();
            // default minimal configuration: single-objective maximize with trivial function
            this.setTypeProblem(ProblemType.Maximizar);
            // ... leave function list empty since tests don't call Evaluation in these tests
        }
    }

    // A tiny Generator stub that implements the abstract methods minimally
    static class StubGenerator extends Generator {

        private GeneratorType type;
        private float weight = 0.0f;

        StubGenerator(GeneratorType type){
            this.type = type;
        }

        @Override
        public State generate(Integer operatornumber) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
            return new State();
        }

        @Override
        public void updateReference(State stateCandidate, Integer countIterationsCurrent) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
            // no-op
        }

        @Override
        public State getReference() {
            return null;
        }

        @Override
        public void setInitialReference(State stateInitialRef) {
            // no-op
        }

        @Override
        public GeneratorType getType() {
            return type;
        }

        @Override
        public java.util.List<State> getReferenceList() {
            return new java.util.ArrayList<State>();
        }

        @Override
        public java.util.List<State> getSonList() {
            return new java.util.ArrayList<State>();
        }

        @Override
        public boolean awardUpdateREF(State stateCandidate) {
            return false;
        }

        @Override
        public void setWeight(float weight) {
            this.weight = weight;
        }

        @Override
        public float getWeight() {
            return weight;
        }

        @Override
        public float[] getTrace() {
            return new float[100];
        }

        @Override
        public int[] getListCountBetterGender() {
            return new int[10];
        }

        @Override
        public int[] getListCountGender() {
            return new int[10];
        }

    }

}
