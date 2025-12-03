package metaheuristics.generators;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import problem.definition.Problem;
import problem.definition.State;
import testutils.TestHelper;

/**
 * Tests that `MultiGenerator.generate()` selects an active generator from the
 * configured list and delegates generate() to it.
 */
public class MultiGeneratorOrchestrationTest {

    private static class StubGenerator extends Generator {

        private GeneratorType type = GeneratorType.RandomSearch;

        @Override
        public State generate(Integer operatornumber) {
            State s = new State();
            s.setCode(new ArrayList<Object>(Arrays.asList(42)));
            s.setEvaluation(new ArrayList<Double>(Arrays.asList(42.0)));
            return s;
        }

        @Override
        public void updateReference(State stateCandidate, Integer countIterationsCurrent) { }

        @Override
        public State getReference() { return null; }

        @Override
        public void setInitialReference(State stateInitialRef) { }

        @Override
        public GeneratorType getType() { return type; }

        @Override
        public List<State> getReferenceList() { return new ArrayList<State>(); }

        @Override
        public List<State> getSonList() { return new ArrayList<State>(); }

        @Override
        public boolean awardUpdateREF(State stateCandidate) { return false; }

        @Override
        public void setWeight(float weight) { /* ignore */ }

        @Override
        public float getWeight() { return 1.0f; }

        @Override
        public float[] getTrace() { return new float[0]; }

        @Override
        public int[] getListCountBetterGender() { return new int[0]; }

        @Override
        public int[] getListCountGender() { return new int[0]; }
    }

    @Test
    public void multiGeneratorDelegatesToActiveGenerator() throws Exception {
        // Prepare minimal problem and strategy
        Problem p = TestHelper.createMinimalProblemWithCodification(1);
        TestHelper.initStrategyWithProblem(p);

        // Create stub generator and register it as the only generator
        StubGenerator stub = new StubGenerator();
        Generator[] arr = new Generator[] { stub };
        MultiGenerator.setListGenerators(arr);

        // Ensure generator array is picked and roulette() will return the stub
        MultiGenerator mg = new MultiGenerator();

        State got = mg.generate(1);

        assertNotNull(got, "generate must return a state");
        assertEquals(42, ((Number) got.getCode().get(0)).intValue());

        // active generator must be the stub and its countGender should have been incremented
        assertEquals(MultiGenerator.getActiveGenerator().getType(), stub.getType());
        assertEquals(1, stub.countGender, "active generator countGender should be incremented by MultiGenerator.generate");
    }
}
