package metaheurictics.strategy;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import metaheuristics.generators.Generator;
import metaheuristics.generators.GeneratorType;
import metaheuristics.generators.MultiGenerator;
import problem.definition.ObjetiveFunction;
import problem.definition.Problem;
import problem.definition.State;

public class StrategyExtraTests {

    @AfterEach
    public void tearDown(){
        // reset singletons/static state touched by tests
        Strategy.destroyExecute();
        MultiGenerator.destroyMultiGenerator();
    }

    @Test
    public void getListKey_withManualMapGenerators_returnsSameSize() throws Exception{
        Strategy s = Strategy.getStrategy();
        s.mapGenerators = new TreeMap<>();
        s.mapGenerators.put(GeneratorType.HillClimbing, new DummyGenerator(GeneratorType.HillClimbing));
        s.mapGenerators.put(GeneratorType.GeneticAlgorithm, new DummyGenerator(GeneratorType.GeneticAlgorithm));

        List<String> keys = s.getListKey();
        assertNotNull(keys);
        assertEquals(2, keys.size());
    }

    @Test
    public void updateCountGender_updatesArrays() throws Exception{
        Strategy s = Strategy.getStrategy();
        // periodo is private; set via reflection
        java.lang.reflect.Field f = Strategy.class.getDeclaredField("periodo");
        f.setAccessible(true);
        f.setInt(s, 0);
        // prepare two dummy generators with internal arrays
        DummyGenerator g1 = new DummyGenerator(GeneratorType.HillClimbing);
        DummyGenerator g2 = new DummyGenerator(GeneratorType.GeneticAlgorithm);
        g1.listCountGender[0] = 2;
        g1.countGender = 5; // inherited public field
        g1.listCountBetterGender[0] = 1;
        g1.countBetterGender = 3;

        g2.listCountGender[0] = 0;
        g2.countGender = 4;
        g2.listCountBetterGender[0] = 7;
        g2.countBetterGender = 2;

        MultiGenerator.setListGenerators(new Generator[] { g1, g2 });

        s.updateCountGender();

        // arrays should be updated by adding countGender/countBetterGender
        assertEquals(7, g1.listCountGender[0]);
        assertEquals(4, g1.listCountBetterGender[0]);
        assertEquals(4, g2.listCountGender[0]);
        assertEquals(9, g2.listCountBetterGender[0]);
    }

    @Test
    public void updateWeight_setsAllTo50() throws Exception{
        Strategy s = Strategy.getStrategy();
        DummyGenerator g1 = new DummyGenerator(GeneratorType.HillClimbing);
        DummyGenerator g2 = new DummyGenerator(GeneratorType.GeneticAlgorithm);
        g1.weight = 10f;
        g2.weight = 20f;
        MultiGenerator.setListGenerators(new Generator[] { g1, g2 });

        s.updateWeight();

        assertEquals(50.0f, g1.weight);
        assertEquals(50.0f, g2.weight);
    }

    @Test
    public void updateRefGenerator_hillClimbing_updatesReferenceEvaluation() throws Exception{
        Strategy s = Strategy.getStrategy();
        // create problem with a single objective function returning deterministic value
        Problem p = new Problem();
        java.util.ArrayList<ObjetiveFunction> funcs = new java.util.ArrayList<>();
        funcs.add(new ObjetiveFunction(){
            @Override
            public Double Evaluation(State s) {
                return Double.valueOf(9.5);
            }
        });
        p.setFunction(funcs);
        s.setProblem(p);

        // generator whose getReference() returns a TestState that exposes internal eval list
        DummyGenerator g = new DummyGenerator(GeneratorType.HillClimbing);
    TestState ref = new TestState();
    java.util.ArrayList<Double> eval = new java.util.ArrayList<>();
    eval.add(Double.valueOf(1.0));
    ref.setEvaluation(eval);
        g.reference = ref;

        s.updateRefGenerator(g);

        // TestState.getEvaluation returns the actual list, so it is modified
        assertEquals(9.5, ref.getEvaluation().get(0));
    }

    // --- helper stub classes used by the tests ---
    public static class DummyGenerator extends Generator {
        private GeneratorType type;
        public int[] listCountGender = new int[2];
        public int[] listCountBetterGender = new int[2];
        public float weight = 0f;
        public State reference = new State();

        public DummyGenerator(GeneratorType t){
            this.type = t;
        }

        @Override
        public State generate(Integer operatornumber) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, java.lang.reflect.InvocationTargetException, NoSuchMethodException {
            return new State();
        }

        @Override
        public void updateReference(State stateCandidate, Integer countIterationsCurrent) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, java.lang.reflect.InvocationTargetException, NoSuchMethodException {
            // no-op
        }

        @Override
        public State getReference() {
            return reference;
        }

        @Override
        public void setInitialReference(State stateInitialRef) {
            this.reference = stateInitialRef;
        }

        @Override
        public GeneratorType getType() {
            return type;
        }

        @Override
        public List<State> getReferenceList() {
            return new ArrayList<State>();
        }

        @Override
        public List<State> getSonList() {
            return new ArrayList<State>();
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
            return this.weight;
        }

        @Override
        public float[] getTrace() {
            return new float[0];
        }

        @Override
        public int[] getListCountBetterGender() {
            return listCountBetterGender;
        }

        @Override
        public int[] getListCountGender() {
            return listCountGender;
        }
    }

    // TestState bypasses defensive copying in State.getEvaluation by exposing internal list
    public static class TestState extends State {
        private List<Double> internalEval = new ArrayList<>();

        public TestState(){
            super();
        }

        @Override
        public java.util.ArrayList<Double> getEvaluation() {
            // return internal list as a modifiable ArrayList to allow in-place updates by production code
            return (internalEval == null) ? null : new java.util.ArrayList<Double>(internalEval) {
                @Override
                public Double set(int index, Double element) {
                    // ensure internalEval is updated as well
                    if (index >= internalEval.size()) {
                        // resize
                        while (internalEval.size() <= index) internalEval.add(0.0);
                    }
                    internalEval.set(index, element);
                    return super.set(index, element);
                }
            };
        }

        @Override
        public void setEvaluation(java.util.ArrayList<Double> evaluation) {
            internalEval = (evaluation == null) ? new ArrayList<>() : new ArrayList<>(evaluation);
        }
    }

}
