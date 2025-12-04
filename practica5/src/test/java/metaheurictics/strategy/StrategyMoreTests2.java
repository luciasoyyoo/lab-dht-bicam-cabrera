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

public class StrategyMoreTests2 {

    @AfterEach
    public void tearDown(){
        Strategy.destroyExecute();
        MultiGenerator.destroyMultiGenerator();
        ParticleSwarmOptimization.setLBest(null);
        ParticleSwarmOptimization.setGBest(null);
    }

    @Test
    public void calculateOffLinePerformance_writesIntoArray() {
        Strategy s = Strategy.getStrategy();
        // set countPeriodChange to avoid div by zero
        try {
            java.lang.reflect.Field f = Strategy.class.getDeclaredField("countPeriodChange");
            f.setAccessible(true);
            f.setInt(s, 2);
        } catch (Exception e) { /* ignore */ }

        s.listOfflineError = new float[10];
        s.calculateOffLinePerformance(10f, 0);
        assertEquals(5.0f, s.listOfflineError[0]);
    }

    @Test
    public void update_switchesToGeneticAlgorithm_whenCountRefMatches() throws Exception{
        Strategy s = Strategy.getStrategy();
        // set countCurrent so it equals GeneticAlgorithm.countRef -1
        java.lang.reflect.Field f = Strategy.class.getDeclaredField("countCurrent");
        f.setAccessible(true);
        // set GeneticAlgorithm.countRef to 3 for test
        java.lang.Class<?> gaClass = Class.forName("metaheuristics.generators.GeneticAlgorithm");
        java.lang.reflect.Field gaCount = gaClass.getDeclaredField("countRef");
        gaCount.setAccessible(true);
        gaCount.setInt(null, 3);
        f.setInt(s, 2);

        // call update() and expect Strategy.generator to be non-null (FactoryGenerator will create)
        s.update(Integer.valueOf(2));
        assertNotNull(Strategy.getStrategy().generator);
    }

    @Test
    public void multiGenerator_roulette_returnsOneOfList() throws Exception{
        // prepare three test generators with different weights
        TestGen g1 = new TestGen(GeneratorType.HillClimbing, 0f);
        TestGen g2 = new TestGen(GeneratorType.RandomSearch, 1f);
        TestGen g3 = new TestGen(GeneratorType.GeneticAlgorithm, 1f);
        MultiGenerator.setListGenerators(new Generator[] { g1, g2, g3 });

        // run roulette many times ensuring we always get one of provided generators
        for (int i = 0; i < 20; i++) {
            Generator picked = new MultiGenerator().roulette();
            assertTrue(picked == g1 || picked == g2 || picked == g3);
        }
    }

    @Test
    public void multiGenerator_updateWeight_searchState_true_updatesActiveGeneratorTrace() throws Exception{
        Strategy s = Strategy.getStrategy();
        // prepare bestState value
        State best = new State();
        java.util.ArrayList<Double> be = new java.util.ArrayList<>(); be.add(Double.valueOf(5.0));
        best.setEvaluation(be);
        s.setBestState(best);

        TestGen active = new TestGen(GeneratorType.HillClimbing, 10f);
        // ensure trace length > 1 to avoid OOB
        active.trace = new float[5];
        active.trace[0] = 10f;
        MultiGenerator.setListGenerators(new Generator[] { active });
        MultiGenerator.setActiveGenerator(active);

        State candidate = new State();
        java.util.ArrayList<Double> ce = new java.util.ArrayList<>(); ce.add(Double.valueOf(20.0));
        candidate.setEvaluation(ce);

        // call updateWeight on multi generator
        MultiGenerator mg = new MultiGenerator();
        mg.updateWeight(candidate);

        // active generator weight should have been increased or decreased depending on searchState path
        assertTrue(active.getWeight() != 10f);
    }

    @Test
    public void pso_staticAccessors_setAndGetLBestGBestCountRef() {
        // set small LBest array
        State s1 = new State(); java.util.ArrayList<Double> e1 = new java.util.ArrayList<>(); e1.add(Double.valueOf(1.0)); s1.setEvaluation(e1);
        ParticleSwarmOptimization.setLBest(new State[] { s1 });
        State[] got = ParticleSwarmOptimization.getLBest();
        assertEquals(1, got.length);
        assertEquals(1.0, got[0].getEvaluation().get(0));

        ParticleSwarmOptimization.setGBest(s1);
        assertNotNull(ParticleSwarmOptimization.getGBest());

        ParticleSwarmOptimization.setCountRef(5);
        assertEquals(5, ParticleSwarmOptimization.getCountRef());
    }

    // helper test generator
    public static class TestGen extends Generator {
        private GeneratorType type;
        private float weight;
        public float[] trace = new float[0];
        public TestGen(GeneratorType t, float w){ this.type = t; this.weight = w; }

        @Override
        public State generate(Integer operatornumber) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, java.lang.reflect.InvocationTargetException, NoSuchMethodException { return new State(); }

        @Override
        public void updateReference(State stateCandidate, Integer countIterationsCurrent) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, java.lang.reflect.InvocationTargetException, NoSuchMethodException { }

        @Override
        public State getReference() { return new State(); }

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
        public float[] getTrace() { return trace; }

        @Override
        public int[] getListCountBetterGender() { return new int[0]; }

        @Override
        public int[] getListCountGender() { return new int[0]; }
    }
}
