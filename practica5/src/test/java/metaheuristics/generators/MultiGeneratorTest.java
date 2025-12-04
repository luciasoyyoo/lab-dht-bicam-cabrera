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
        // prepare listGenerators with simple generators with weight>0
        Generator[] gens = new Generator[4];
        gens[0] = new HillClimbing();
        gens[1] = new EvolutionStrategies();
        gens[2] = new LimitThreshold();
        gens[3] = new GeneticAlgorithm();
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
        // ensure initialize populates 4 generators
        MultiGenerator.initializeListGenerator();
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
}
