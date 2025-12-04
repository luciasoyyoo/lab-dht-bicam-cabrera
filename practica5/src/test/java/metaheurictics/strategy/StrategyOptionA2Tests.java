package metaheurictics.strategy;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import metaheuristics.generators.Generator;
import metaheuristics.generators.GeneratorType;
import metaheuristics.generators.MultiGenerator;
import problem.definition.ObjetiveFunction;
import problem.definition.Problem;
import problem.definition.State;
import local_search.complement.StopExecute;

public class StrategyOptionA2Tests {

    @AfterEach
    public void tearDown(){
        Strategy.destroyExecute();
        MultiGenerator.destroyMultiGenerator();
    }

    @Test
    public void update_switchesToEvolutionStrategies_whenCountRefMatches() throws Exception{
        Strategy s = Strategy.getStrategy();
        // set EvolutionStrategies.countRef to 4
        java.lang.Class<?> esClass = Class.forName("metaheuristics.generators.EvolutionStrategies");
        java.lang.reflect.Field esField = esClass.getDeclaredField("countRef");
        esField.setAccessible(true);
        esField.setInt(null, 4);

        // set strategy countCurrent to 3
        java.lang.reflect.Field f = Strategy.class.getDeclaredField("countCurrent");
        f.setAccessible(true);
        f.setInt(s, 3);

        s.update(Integer.valueOf(3));
        assertNotNull(Strategy.getStrategy().generator);
        assertEquals(GeneratorType.EvolutionStrategies, Strategy.getStrategy().generator.getType());
    }

    @Test
    public void update_switchesToDistributionEstimationAlgorithm_whenCountRefMatches() throws Exception{
        Strategy s = Strategy.getStrategy();
        java.lang.Class<?> deClass = Class.forName("metaheuristics.generators.DistributionEstimationAlgorithm");
        java.lang.reflect.Field deField = deClass.getDeclaredField("countRef");
        deField.setAccessible(true);
        deField.setInt(null, 5);

        java.lang.reflect.Field f = Strategy.class.getDeclaredField("countCurrent");
        f.setAccessible(true);
        f.setInt(s, 4);

        s.update(Integer.valueOf(4));
        assertNotNull(Strategy.getStrategy().generator);
        assertEquals(GeneratorType.DistributionEstimationAlgorithm, Strategy.getStrategy().generator.getType());
    }

    @Test
    public void updateRefGenerator_evolutionStrategies_updatesReferenceList() throws Exception{
        Strategy s = Strategy.getStrategy();
        Problem p = new Problem();
        java.util.ArrayList<ObjetiveFunction> funcs = new java.util.ArrayList<>();
        funcs.add(new ObjetiveFunction(){
            @Override
            public Double Evaluation(State st) { return Double.valueOf(2.2); }
        });
        p.setFunction(funcs);
        s.setProblem(p);

        DummyEvolution gen = new DummyEvolution();
        TestState t1 = new TestState(); t1.setEvaluation(new java.util.ArrayList<Double>(java.util.Arrays.asList(Double.valueOf(0.1))));
        TestState t2 = new TestState(); t2.setEvaluation(new java.util.ArrayList<Double>(java.util.Arrays.asList(Double.valueOf(0.2))));
        gen.referenceList.add(t1); gen.referenceList.add(t2);

        s.updateRefGenerator(gen);

        assertEquals(2.2, gen.referenceList.get(0).getEvaluation().get(0));
        assertEquals(2.2, gen.referenceList.get(1).getEvaluation().get(0));
    }

    @Test
    public void executeStrategy_withImmediateStop_doesNotLoopAndInitializes() throws Exception{
        Strategy s = Strategy.getStrategy();
        // set a simple problem and objective
        Problem p = new Problem();
        java.util.ArrayList<ObjetiveFunction> funcs = new java.util.ArrayList<>();
        funcs.add(new ObjetiveFunction(){
            @Override
            public Double Evaluation(State st) { return Double.valueOf(1.0); }
        });
        p.setFunction(funcs);
        s.setProblem(p);

        // set StopExecute stub that stops immediately
        s.setStopexecute(new StopExecute(){
            @Override
            public Boolean stopIterations(int countIterationsCurrent, int countmaxIterations){
                return true; // do not enter loop
            }
        });

        // run executeStrategy with 0 max iterations; should complete quickly and set bestState
        s.executeStrategy(0, 1, 1, GeneratorType.RandomSearch);
        assertNotNull(s.getBestState());
    }

    // helpers
    public static class TestState extends State {
        private java.util.ArrayList<Double> internal = new java.util.ArrayList<Double>();
        @Override
        public java.util.ArrayList<Double> getEvaluation() { return internal; }
        @Override
        public void setEvaluation(java.util.ArrayList<Double> evaluation) { internal = (evaluation == null) ? new java.util.ArrayList<>() : new java.util.ArrayList<>(evaluation); }
    }

    public static class DummyEvolution extends Generator {
        public java.util.List<TestState> referenceList = new java.util.ArrayList<>();
        @Override
        public State generate(Integer operatornumber) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, java.lang.reflect.InvocationTargetException, NoSuchMethodException { return new State(); }
        @Override
        public void updateReference(State stateCandidate, Integer countIterationsCurrent) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, java.lang.reflect.InvocationTargetException, NoSuchMethodException { }
        @Override
        public State getReference() { return null; }
        @Override
        public void setInitialReference(State stateInitialRef) { }
        @Override
        public GeneratorType getType() { return GeneratorType.EvolutionStrategies; }
        @Override
        public List<State> getReferenceList() { return new ArrayList<State>(referenceList); }
        @Override
        public List<State> getSonList() { return new ArrayList<State>(); }
        @Override
        public boolean awardUpdateREF(State stateCandidate) { return false; }
        @Override
        public void setWeight(float weight) { }
        @Override
        public float getWeight() { return 0; }
        @Override
        public float[] getTrace() { return new float[0]; }
        @Override
        public int[] getListCountBetterGender() { return new int[0]; }
        @Override
        public int[] getListCountGender() { return new int[0]; }
    }
}
