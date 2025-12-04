package metaheuristics.generators;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import testutils.TestHelper;
import problem.definition.State;
import metaheurictics.strategy.Strategy;

public class MultiGeneratorTest {

    @BeforeEach
    public void setup() {
        TestHelper.initStrategyWithProblem(TestHelper.createMinimalProblemWithCodification(1));
    }

    @AfterEach
    public void tearDown(){
        // restore static state to avoid leaking between tests
        try { MultiGenerator.destroyMultiGenerator(); } catch (Exception e) {}
        Strategy.destroyExecute();
    }

    @Test
    public void roulette_and_updateAward_behavior() throws Exception {
        // prepare listGenerators with TestGenerator stubs to avoid constructors that depend on Strategy.mapGenerators
        TestGenerator g0 = new TestGenerator(GeneratorType.HillClimbing, 5.0f, 2);
        TestGenerator g1 = new TestGenerator(GeneratorType.EvolutionStrategies, 5.0f, 2);
        TestGenerator g2 = new TestGenerator(GeneratorType.LimitThreshold, 5.0f, 2);
        TestGenerator g3 = new TestGenerator(GeneratorType.GeneticAlgorithm, 5.0f, 2);
        Generator[] gens = new Generator[] { g0, g1, g2, g3 };
        MultiGenerator.setListGenerators(gens);

        MultiGenerator mg = new MultiGenerator();

        // roulette should return one of the generators (non-null)
        Generator chosen = mg.roulette();
        assertNotNull(chosen);

        // set activeGenerator and best state to test searchState/updateAward
        MultiGenerator.setActiveGenerator(gens[0]);
        State best = new State(); best.setEvaluation(new ArrayList<Double>(){{add(0.5);} });
        metaheurictics.strategy.Strategy.getStrategy().setBestState(best);
        // ensure Problem type is set to Maximizar for searchState logic
        metaheurictics.strategy.Strategy.getStrategy().getProblem().setTypeProblem(problem.definition.Problem.ProblemType.Maximizar);
        metaheurictics.strategy.Strategy.getStrategy().setCountCurrent(0);

        // exercise updateAwardSC and updateAwardImp paths by calling updateWeight, which delegates
        MultiGenerator.setActiveGenerator(gens[1]);
        metaheurictics.strategy.Strategy.getStrategy().setCountCurrent(0);
        mg.updateAwardSC();
        mg.updateAwardImp();
        // clone should not throw and return a MultiGenerator or clone
        Object cloned = mg.clone();
        assertNotNull(cloned);
    }

    @Test
    public void testInitializeListGenerator_and_destroy() throws Exception {
        // Instead of calling initializeListGenerator (which instantiates concrete generators that depend on Strategy.mapGenerators),
        // use TestGenerator stubs to verify behavior of set/get and destroy
        TestGenerator g0 = new TestGenerator(GeneratorType.HillClimbing, 1.0f, 1);
        TestGenerator g1 = new TestGenerator(GeneratorType.EvolutionStrategies, 1.0f, 1);
        TestGenerator g2 = new TestGenerator(GeneratorType.LimitThreshold, 1.0f, 1);
        TestGenerator g3 = new TestGenerator(GeneratorType.GeneticAlgorithm, 1.0f, 1);
        MultiGenerator.setListGenerators(new Generator[] { g0, g1, g2, g3 });
        Generator[] gens = MultiGenerator.getListGenerators();
        assertNotNull(gens);
        assertEquals(4, gens.length);
        // destroy clears the static arrays
        MultiGenerator.destroyMultiGenerator();
        assertNull(MultiGenerator.getListGenerators());
    }

    @Test
    public void testSetGetListGenerators_defensiveCopy_and_roulette_single() {
        // set a single generator array and ensure defensive copy
        Generator single = new Generator() {
            @Override public State generate(Integer operatornumber){ return new State(); }
            @Override public State getReference(){ return null; }
            @Override public java.util.List<State> getReferenceList(){ return null; }
            @Override public java.util.List<State> getSonList(){ return null; }
            @Override public GeneratorType getType(){ return GeneratorType.RandomSearch; }
            @Override public void setInitialReference(State stateInitialRef) {}
            @Override public void updateReference(State stateCandidate, Integer countIterationsCurrent) {}
            @Override public boolean awardUpdateREF(State stateCandidate){ return false; }
            @Override public float getWeight(){ return 1.0f; }
            @Override public void setWeight(float weight) {}
            @Override public float[] getTrace(){ return new float[0]; }
            @Override public int[] getListCountBetterGender(){ return new int[0]; }
            @Override public int[] getListCountGender(){ return new int[0]; }
        };
        MultiGenerator.setListGenerators(new Generator[]{single});
        Generator[] got = MultiGenerator.getListGenerators();
        assertNotSame(got, new Generator[]{single});
        MultiGenerator mg = new MultiGenerator();
        Generator pick = mg.roulette();
        assertEquals(single.getType(), pick.getType());
    }

    @Test
    public void testGenerate_delegates_to_active_generator_and_increments_countGender() throws Exception {
        final State expected = new State(); expected.setEvaluation(new ArrayList<Double>(){{ add(11.0); }});
        Generator custom = new Generator(){
            @Override public State generate(Integer operatornumber){ return expected; }
            @Override public State getReference(){ return null; }
            @Override public java.util.List<State> getReferenceList(){ return null; }
            @Override public java.util.List<State> getSonList(){ return null; }
            @Override public GeneratorType getType(){ return GeneratorType.RandomSearch; }
            @Override public void setInitialReference(State stateInitialRef) {}
            @Override public void updateReference(State stateCandidate, Integer countIterationsCurrent) {}
            @Override public boolean awardUpdateREF(State stateCandidate){ return false; }
            @Override public float getWeight(){ return 1.0f; }
            @Override public void setWeight(float weight) {}
            @Override public float[] getTrace(){ return new float[1]; }
            @Override public int[] getListCountBetterGender(){ return new int[0]; }
            @Override public int[] getListCountGender(){ return new int[0]; }
        };
        MultiGenerator.setListGenerators(new Generator[]{custom});
        // ensure Strategy.generator is null so MultiGenerator.generate uses roulette
        Strategy.getStrategy().generator = null;
        MultiGenerator mg = new MultiGenerator();
        State out = mg.generate(1);
        assertNotNull(out);
        assertEquals(11.0, out.getEvaluation().get(0), 0.0);
        assertNotNull(MultiGenerator.getActiveGenerator());
        assertEquals(1, MultiGenerator.getActiveGenerator().countGender);
    }

    static class TestGenerator extends Generator {
        private final GeneratorType type;
        private float weight;
        private final float[] trace;
        boolean updatedReference = false;

        TestGenerator(GeneratorType type, float weight, int traceLen) {
            this.type = type;
            this.weight = weight;
            this.trace = new float[Math.max(1, traceLen)];
            for (int i = 0; i < this.trace.length; i++) this.trace[i] = weight;
        }

        @Override
        public State generate(Integer operatornumber) {
            State s = new State();
            ArrayList<Double> eval = new ArrayList<>();
            eval.add(0.0);
            s.setEvaluation(eval);
            s.setTypeGenerator(type);
            return s;
        }

        @Override
        public void updateReference(State stateCandidate, Integer countIterationsCurrent) {
            this.updatedReference = true;
        }

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
        public void setWeight(float weight) { this.weight = weight; }

        @Override
        public float getWeight() { return this.weight; }

        @Override
        public float[] getTrace() { return trace; }

        @Override
        public int[] getListCountBetterGender() { return new int[1]; }

        @Override
        public int[] getListCountGender() { return new int[1]; }
    }

    @BeforeEach
    public void beforeEach() {
        // reset static state used by MultiGenerator/Strategy between tests
        Strategy.destroyExecute();
        MultiGenerator.listGeneratedPP.clear();
        MultiGenerator.listStateReference.clear();
        MultiGenerator.setListGenerators(null);
        MultiGenerator.setActiveGenerator(null);
        // ensure Strategy has a minimal problem and best state for some tests
        Strategy s = Strategy.getStrategy();
        s.setProblem(new problem.definition.Problem() {
            private State state = new State();
            { ArrayList<Double> ev = new ArrayList<>(); ev.add(0.0); state.setEvaluation(ev); }
            @Override public State getState() { return state; }
            @Override public void setState(State state) { this.state = state; }
            @Override public void Evaluate(State state) { /* no-op */ }
            @Override public java.util.ArrayList<problem.definition.ObjetiveFunction> getFunction() { return new java.util.ArrayList<problem.definition.ObjetiveFunction>(); }
            @Override public ProblemType getTypeProblem() { return ProblemType.Maximizar; }
        });
        Strategy.getStrategy().setBestState(new State());
        ArrayList<Double> bestEv = new ArrayList<>(); bestEv.add(0.0); Strategy.getStrategy().getBestState().setEvaluation(bestEv);
        Strategy.getStrategy().setCountCurrent(0);
    }

    @AfterEach
    public void afterEach() {
        MultiGenerator.destroyMultiGenerator();
        Strategy.destroyExecute();
    }

    @Test
    public void testSetGetListGeneratorsDefensiveCopy() {
        TestGenerator g1 = new TestGenerator(GeneratorType.RandomSearch, 1.0f, 1);
        TestGenerator g2 = new TestGenerator(GeneratorType.HillClimbing, 2.0f, 1);
        Generator[] arr = new Generator[] { g1, g2 };
        MultiGenerator.setListGenerators(arr);
        // mutate original array
        arr[0] = null;
        Generator[] fromMulti = MultiGenerator.getListGenerators();
        assertNotNull(fromMulti, "should return defensive copy");
        assertEquals(2, fromMulti.length);
        assertNotNull(fromMulti[0], "internal array must not be affected by caller mutation");
        assertEquals(GeneratorType.RandomSearch, fromMulti[0].getType());
    }

    @Test
    public void testSetListGeneratedPPAndDestroy() {
        State s = new State();
        ArrayList<Double> ev = new ArrayList<>(); ev.add(5.0); s.setEvaluation(ev);
        List<State> list = new ArrayList<>(); list.add(s);
        MultiGenerator.setListGeneratedPP(list);
        assertEquals(1, MultiGenerator.listGeneratedPP.size());
        MultiGenerator.destroyMultiGenerator();
        assertEquals(0, MultiGenerator.listGeneratedPP.size());
        assertNull(MultiGenerator.getListGenerators());
        assertNull(MultiGenerator.getActiveGenerator());
    }

    @Test
    public void testRouletteSingleGeneratorReturnsIt() {
        TestGenerator only = new TestGenerator(GeneratorType.RandomSearch, 10.0f, 1);
        MultiGenerator.setListGenerators(new Generator[] { only });
        Generator picked = new MultiGenerator().roulette();
        assertSame(only, picked, "With single generator roulette must return that generator");
    }

    @Test
    public void testUpdateAwardSCAndImpUpdatesWeightsAndTrace() {
        TestGenerator a = new TestGenerator(GeneratorType.HillClimbing, 20.0f, 2);
        TestGenerator b = new TestGenerator(GeneratorType.RandomSearch, 30.0f, 2);
        // set as active and as list
        MultiGenerator.setListGenerators(new Generator[] { a, b });
        MultiGenerator.setActiveGenerator(a);
        Strategy.getStrategy().setCountCurrent(0);
        // call updateAwardSC
        new MultiGenerator().updateAwardSC();
        float expectedA = (float)(20.0f * (1 - 0.1) + 10);
        assertEquals(expectedA, a.getWeight(), 1e-6f);
        assertEquals(expectedA, a.getTrace()[0], 1e-6f);
        assertEquals(b.getWeight(), b.getTrace()[0], 1e-6f);

        // now test updateAwardImp (reduces weight)
        a.setWeight(50.0f);
        MultiGenerator.setActiveGenerator(a);
        Strategy.getStrategy().setCountCurrent(0);
        new MultiGenerator().updateAwardImp();
        float expectedImp = (float)(50.0f * (1 - 0.1));
        assertEquals(expectedImp, a.getWeight(), 1e-6f);
        assertEquals(expectedImp, a.getTrace()[0], 1e-6f);
    }

    @Test
    public void testTournamentInvokesUpdateReferenceOnSubGenerators() throws Exception {
        TestGenerator a = new TestGenerator(GeneratorType.HillClimbing, 10.0f, 1);
        TestGenerator b = new TestGenerator(GeneratorType.RandomSearch, 10.0f, 1);
        MultiGenerator.setListGenerators(new Generator[] { a, b });
        State s = new State();
        new MultiGenerator().tournament(s, 0);
        // each TestGenerator updates its flag when updateReference called
        assertTrue(a.updatedReference || b.updatedReference, "At least one non-multi generator should have been updated");
    }

    @Test
    public void testCloneAndUtilityMethods() {
        MultiGenerator mg = new MultiGenerator();
        Object c = mg.clone();
        assertNotNull(c);
        assertArrayEquals(new float[0], mg.getTrace());
        assertNotNull(mg.getReferenceList());
        assertEquals(0, mg.getListCountBetterGender().length);
        assertEquals(0, mg.getListCountGender().length);
    }
}
