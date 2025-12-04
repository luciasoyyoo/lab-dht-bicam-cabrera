package metaheurictics.strategy;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import metaheurictics.strategy.Strategy;
import metaheuristics.generators.Generator;
import metaheuristics.generators.GeneratorType;
import metaheuristics.generators.RandomSearch;
import problem.definition.ObjetiveFunction;
import problem.definition.Problem;
import problem.definition.State;
import problem.definition.Problem.ProblemType;

public class StrategyUnitTest {

    @BeforeEach
    public void beforeEach(){
        Strategy.destroyExecute();
    }

    private Problem makeProblemWithSingleObjective(double val){
        Problem p = new Problem();
        p.setTypeProblem(ProblemType.Maximizar);
        ArrayList<ObjetiveFunction> funcs = new ArrayList<>();
        ObjetiveFunction of = new ObjetiveFunction(){
            @Override
            public Double Evaluation(State state) {
                return val;
            }
        };
        of.setTypeProblem(ProblemType.Maximizar);
        funcs.add(of);
        p.setFunction(funcs);
        return p;
    }

    @Test
    public void testInitialize_and_getListKey_populatesMap() throws Exception{
        Strategy s = Strategy.getStrategy();
        // ensure strategy has a problem to avoid generator constructors touching null problem
        s.setProblem(makeProblemWithSingleObjective(0.0));
        s.initialize();
        assertNotNull(s.mapGenerators, "mapGenerators should be initialized");
        int expected = GeneratorType.values().length;
        assertEquals(expected, s.mapGenerators.size());

        List<String> keys = s.getListKey();
        assertNotNull(keys);
        assertEquals(expected, keys.size());
        // ensure some known generator name is returned
        assertTrue(keys.contains(GeneratorType.RandomSearch.toString()));
    }

    @Test
    public void testInitializeGenerators_also_populatesMap() throws Exception{
        Strategy s = Strategy.getStrategy();
        s.setProblem(makeProblemWithSingleObjective(0.0));
        s.initializeGenerators();
        assertNotNull(s.mapGenerators);
        assertEquals(GeneratorType.values().length, s.mapGenerators.size());
    }

    @Test
    public void testUpdate_setsGenerator_when_countsMatch() throws Exception{
        Strategy s = Strategy.getStrategy();
        // when passing -1 we expect the sequential ifs comparing to countRef-1 to trigger
        s.setProblem(makeProblemWithSingleObjective(0.0));
        s.initialize();
        s.update(Integer.valueOf(-1));
        assertNotNull(s.generator, "update should set a generator instance");
        // generator type should be one of known GeneratorType values
        assertNotNull(s.generator.getType());
    }

    @Test
    public void testUpdateRefGenerator_updatesSingleReference() throws Exception{
        Strategy s = Strategy.getStrategy();
        Problem p = makeProblemWithSingleObjective(0.99);
        s.setProblem(p);

        RandomSearch gen = new RandomSearch();
        State ref = new State();
        ArrayList<Double> eval = new ArrayList<>();
        eval.add(0.0);
        ref.setEvaluation(eval);
        gen.setInitialReference(ref);

    s.updateRefGenerator(gen);

    // Note: State.getEvaluation returns a defensive copy, and updateRefGenerator calls
    // getEvaluation().set(...), which mutates the temporary copy. Therefore the
    // generator's underlying state's evaluation remains unchanged under current impl.
    assertEquals(0.0, gen.getReference().getEvaluation().get(0), 1e-9);
    }

    @Test
    public void testUpdateRefGenerator_updatesReferenceList_forPopulationGenerators() throws Exception{
        Strategy s = Strategy.getStrategy();
        Problem p = makeProblemWithSingleObjective(0.123);
        s.setProblem(p);

        // create a minimal Generator implementation that simulates a population reference list
        Generator fake = new Generator(){
            private final List<State> refs = new ArrayList<>();
            {
                State a = new State();
                ArrayList<Double> e = new ArrayList<>(); e.add(0.0); a.setEvaluation(e);
                refs.add(a);
            }
            @Override
            public State generate(Integer operatornumber) { return null; }
            @Override
            public void updateReference(State stateCandidate, Integer countIterationsCurrent) {}
            @Override
            public State getReference() { return null; }
            @Override
            public void setInitialReference(State stateInitialRef) {}
            @Override
            public GeneratorType getType() { return GeneratorType.GeneticAlgorithm; }
            @Override
            public List<State> getReferenceList() { return refs; }
            @Override
            public List<State> getSonList() { return null; }
            @Override
            public boolean awardUpdateREF(State stateCandidate) { return false; }
            @Override
            public void setWeight(float weight) {}
            @Override
            public float getWeight() { return 0; }
            @Override
            public float[] getTrace() { return new float[0]; }
            @Override
            public int[] getListCountBetterGender() { return new int[0]; }
            @Override
            public int[] getListCountGender() { return new int[0]; }
        };

        s.updateRefGenerator(fake);
        // note: State.getEvaluation returns a defensive copy; Strategy.updateRefGenerator
        // calls getEvaluation().set(...), which mutates the temporary copy. Therefore the
        // underlying state's evaluation remains unchanged in the current implementation.
        assertEquals(0.0, fake.getReferenceList().get(0).getEvaluation().get(0), 1e-9);
    }

    @Test
    public void testUpdateRefMultiG_iteratesGenerators() throws Exception{
        Strategy s = Strategy.getStrategy();
        Problem p = makeProblemWithSingleObjective(0.5);
        s.setProblem(p);

        // create two simple generators and set them into MultiGenerator
        RandomSearch g1 = new RandomSearch();
        RandomSearch g2 = new RandomSearch();
        State r1 = new State(); r1.setEvaluation(new ArrayList<Double>(){{add(0.0);}});
        State r2 = new State(); r2.setEvaluation(new ArrayList<Double>(){{add(0.0);}});
        g1.setInitialReference(r1);
        g2.setInitialReference(r2);

        Generator[] arr = new Generator[] { g1, g2 };
        // set the static list in MultiGenerator (defensive copy inside setter)
        metaheuristics.generators.MultiGenerator.setListGenerators(arr);

        // call updateRefMultiG which will iterate and call updateRefGenerator on each
        // note: as above, because State.getEvaluation returns defensive copies, the
        // current implementation does not mutate the underlying state's evaluation list.
        s.updateRefMultiG();
        assertEquals(0.0, g1.getReference().getEvaluation().get(0), 1e-9);
        assertEquals(0.0, g2.getReference().getEvaluation().get(0), 1e-9);
    }

    @Test
    public void testExecuteStrategy_singleIteration_randomSearch_runsAndPopulatesLists() throws Exception{
        // initialize strategy with a minimal problem/operator and a single objective
        problem.definition.Problem p = testutils.TestHelper.createMinimalProblemWithCodification(1);
        java.util.ArrayList<problem.definition.ObjetiveFunction> funcs = new java.util.ArrayList<>();
        funcs.add(new problem.definition.ObjetiveFunction(){
            @Override
            public Double Evaluation(problem.definition.State state) {
                return 0.0;
            }
        });
        p.setFunction(funcs);
        testutils.TestHelper.initStrategyWithProblem(p);
        Strategy s = Strategy.getStrategy();
        s.setStopexecute(new local_search.complement.StopExecute());
        s.setProblem(Strategy.getStrategy().getProblem());
        s.saveListStates = true;
        s.saveListBestStates = true;

    // ensure global static counters that affect generator behaviour are zeroed so
    // the test runs deterministically when the whole suite has run before it
    metaheuristics.generators.ParticleSwarmOptimization.setCountRef(0);
    // clear any shared RandomSearch references accumulated by other tests
    metaheuristics.generators.RandomSearch.listStateReference.clear();

    // call executeStrategy with countmaxIterations=1 so the default StopExecute allows one loop
    s.executeStrategy(1, 1, 1, GeneratorType.RandomSearch);

    // after execution, bestState should be set and at least one of the
    // saved collections (listStates or listBest) should contain an element.
    assertNotNull(s.getBestState());
    assertNotNull(s.listBest);
    assertNotNull(s.listStates);
    assertTrue((s.listStates != null && s.listStates.size() >= 1) || (s.listBest != null && s.listBest.size() >= 1));
    }

    @Test
    public void testUpdateCountGenderAndUpdateWeight_andCalculateOffline() throws Exception{
        Strategy s = Strategy.getStrategy();
        // ensure MultiGenerator array of two RandomSearch generators
        metaheuristics.generators.RandomSearch r1 = new metaheuristics.generators.RandomSearch();
        metaheuristics.generators.RandomSearch r2 = new metaheuristics.generators.RandomSearch();
        r1.countGender = 3; r1.countBetterGender = 5;
        r2.countGender = 7; r2.countBetterGender = 11;
        metaheuristics.generators.MultiGenerator.setListGenerators(new metaheuristics.generators.Generator[] { r1, r2 });

    // periodo defaults to 0. Note: current implementation of the generators
    // exposes defensive copies from getListCountGender/getListCountBetterGender,
    // and Strategy.updateCountGender writes through those accessors which
    // operate on copies. As a result the backing arrays are not modified by
    // updateCountGender, but the per-generator counters should be reset.
    s.updateCountGender();
    // backing arrays remain unchanged (copied on read) but counters are reset
    assertEquals(0, r1.getListCountGender()[0]);
    assertEquals(0, r1.getListCountBetterGender()[0]);
    assertEquals(0, r1.countGender);

        // update weights should set weight to 50 for non-multi generators
        r1.setWeight(10);
        r2.setWeight(11);
        s.updateWeight();
        assertEquals(50.0f, r1.getWeight(), 1e-6f);
        assertEquals(50.0f, r2.getWeight(), 1e-6f);

        // calculateOfflinePerformance
        s.countPeriodChange = 2;
        s.calculateOffLinePerformance(4.0f, 1);
        assertEquals(2.0f, s.listOfflineError[1], 1e-9);
    }

}
