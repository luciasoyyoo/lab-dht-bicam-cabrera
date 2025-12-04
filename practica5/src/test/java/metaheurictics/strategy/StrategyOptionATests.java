package metaheurictics.strategy;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import metaheuristics.generators.Generator;
import metaheuristics.generators.GeneratorType;
import metaheuristics.generators.MultiGenerator;
import metaheuristics.generators.ParticleSwarmOptimization;
import problem.definition.ObjetiveFunction;
import problem.definition.Problem;
import problem.definition.State;

public class StrategyOptionATests {

    @AfterEach
    public void tearDown(){
        Strategy.destroyExecute();
        MultiGenerator.destroyMultiGenerator();
        ParticleSwarmOptimization.setLBest(null);
        ParticleSwarmOptimization.setGBest(null);
    }

    @Test
    public void initialize_populatesMapGenerators_and_getListKey_sizeMatches() throws Exception{
        Strategy s = Strategy.getStrategy();
        s.initialize();
        assertNotNull(s.mapGenerators);
        int expected = metaheuristics.generators.GeneratorType.values().length;
        assertEquals(expected, s.mapGenerators.size());
        List<String> keys = s.getListKey();
        assertEquals(expected, keys.size());
    }

    @Test
    public void updateRefGenerator_geneticAlgorithm_updatesReferenceList() throws Exception{
        Strategy s = Strategy.getStrategy();
        // problem with single objetivo that returns 7.7
        Problem p = new Problem();
        java.util.ArrayList<ObjetiveFunction> funcs = new java.util.ArrayList<>();
        funcs.add(new ObjetiveFunction(){
            @Override
            public Double Evaluation(State st) {
                return Double.valueOf(7.7);
            }
        });
        p.setFunction(funcs);
        s.setProblem(p);

        // create dummy GA-like generator with referenceList of two TestStates
        DummyGA gen = new DummyGA();
        TestState t1 = new TestState(); t1.setEvaluation(new java.util.ArrayList<Double>(java.util.Arrays.asList(Double.valueOf(1.0))));
        TestState t2 = new TestState(); t2.setEvaluation(new java.util.ArrayList<Double>(java.util.Arrays.asList(Double.valueOf(2.0))));
        gen.referenceList.add(t1); gen.referenceList.add(t2);

        s.updateRefGenerator(gen);

        assertEquals(7.7, gen.referenceList.get(0).getEvaluation().get(0));
        assertEquals(7.7, gen.referenceList.get(1).getEvaluation().get(0));
    }

    @Test
    public void update_switchesToParticleSwarmOpt_whenCountRefMatches() throws Exception{
        Strategy s = Strategy.getStrategy();
        // set PSO countRef to 4 and set countCurrent to 3
        ParticleSwarmOptimization.setCountRef(4);
        java.lang.reflect.Field f = Strategy.class.getDeclaredField("countCurrent");
        f.setAccessible(true);
        f.setInt(s, 3);

        s.update(Integer.valueOf(3));
        assertNotNull(Strategy.getStrategy().generator);
        assertEquals(GeneratorType.ParticleSwarmOptimization, Strategy.getStrategy().generator.getType());
    }

    @Test
    public void updateRefMultiG_updatesAllGenerators() throws Exception{
        Strategy s = Strategy.getStrategy();
        // set problem function
        Problem p = new Problem();
        java.util.ArrayList<ObjetiveFunction> funcs = new java.util.ArrayList<>();
        funcs.add(new ObjetiveFunction(){
            @Override
            public Double Evaluation(State st) { return Double.valueOf(3.14); }
        });
        p.setFunction(funcs);
        s.setProblem(p);

        // create two generators: HillClimbing and GeneticAlgorithm
        DummyHill genHill = new DummyHill();
        TestState refHill = new TestState(); refHill.setEvaluation(new java.util.ArrayList<Double>(java.util.Arrays.asList(Double.valueOf(0.5))));
        genHill.reference = refHill;

        DummyGA genGA = new DummyGA();
        TestState g1 = new TestState(); g1.setEvaluation(new java.util.ArrayList<Double>(java.util.Arrays.asList(Double.valueOf(0.6))));
        TestState g2 = new TestState(); g2.setEvaluation(new java.util.ArrayList<Double>(java.util.Arrays.asList(Double.valueOf(0.7))));
        genGA.referenceList.add(g1); genGA.referenceList.add(g2);

        MultiGenerator.setListGenerators(new Generator[] { genHill, genGA });

        s.updateRefMultiG();

        assertEquals(3.14, genHill.reference.getEvaluation().get(0));
        assertEquals(3.14, genGA.referenceList.get(0).getEvaluation().get(0));
        assertEquals(3.14, genGA.referenceList.get(1).getEvaluation().get(0));
    }

    // --- test helpers ---
    public static class TestState extends State {
        private java.util.ArrayList<Double> internal = new java.util.ArrayList<Double>();
        @Override
        public java.util.ArrayList<Double> getEvaluation() {
            return internal;
        }
        @Override
        public void setEvaluation(java.util.ArrayList<Double> evaluation) {
            internal = (evaluation == null) ? new java.util.ArrayList<>() : new java.util.ArrayList<>(evaluation);
        }
    }

    public static class DummyGA extends Generator {
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
        public GeneratorType getType() { return GeneratorType.GeneticAlgorithm; }
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

    public static class DummyHill extends Generator {
        public TestState reference = new TestState();
        @Override
        public State generate(Integer operatornumber) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, java.lang.reflect.InvocationTargetException, NoSuchMethodException { return new State(); }
        @Override
        public void updateReference(State stateCandidate, Integer countIterationsCurrent) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, java.lang.reflect.InvocationTargetException, NoSuchMethodException { }
        @Override
        public State getReference() { return reference; }
        @Override
        public void setInitialReference(State stateInitialRef) { this.reference = (TestState) stateInitialRef; }
        @Override
        public GeneratorType getType() { return GeneratorType.HillClimbing; }
        @Override
        public List<State> getReferenceList() { return new ArrayList<State>(); }
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
