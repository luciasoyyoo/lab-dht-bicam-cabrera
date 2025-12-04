package metaheurictics.strategy;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import metaheuristics.generators.Generator;
import metaheuristics.generators.GeneratorType;
import metaheuristics.generators.ParticleSwarmOptimization;
import problem.definition.ObjetiveFunction;
import problem.definition.Problem;
import problem.definition.State;

public class StrategyMoreDeepTest {

    @BeforeEach
    public void setup() {
        Strategy.destroyExecute();
        Strategy s = Strategy.getStrategy();
        s.setProblem(new Problem());
    }

    @AfterEach
    public void teardown() {
        Strategy.destroyExecute();
        ParticleSwarmOptimization.setCountRef(0);
    }

    @Test
    public void updateRefGenerator_hillClimbing_updatesReferenceEvaluation() throws Exception {
        Strategy s = Strategy.getStrategy();
        Problem p = new Problem();
        // objective function that returns constant 42.0
        ObjetiveFunction of = new ObjetiveFunction() {
            @Override
            public Double Evaluation(State state) {
                return 42.0;
            }
        };
        ArrayList<ObjetiveFunction> funcs = new ArrayList<>();
        funcs.add(of);
        p.setFunction(funcs);
        p.setTypeProblem(Problem.ProblemType.Maximizar);
        s.setProblem(p);

        // stub generator: HillClimbing with a single reference state
        StubGC hg = new StubGC();
    TestState ref = new TestState();
    ArrayList<Double> eval = new ArrayList<>();
    eval.add(0.0);
    ref.setEvaluationDirect(eval);
    hg.ref = ref;

        s.updateRefGenerator(hg);

        assertEquals(42.0, hg.ref.getEvaluation().get(0));
    }

    @Test
    public void updateRefGenerator_geneticAlgorithm_updatesReferenceListEvaluations() throws Exception {
        Strategy s = Strategy.getStrategy();
        Problem p = new Problem();
        ObjetiveFunction of = new ObjetiveFunction() {
            @Override
            public Double Evaluation(State state) {
                return 7.0;
            }
        };
        ArrayList<ObjetiveFunction> funcs = new ArrayList<>();
        funcs.add(of);
        p.setFunction(funcs);
        p.setTypeProblem(Problem.ProblemType.Maximizar);
        s.setProblem(p);

        StubGA ga = new StubGA();
    TestState a = new TestState(); a.setEvaluationDirect(new ArrayList<Double>(List.of(0.0)));
    TestState b = new TestState(); b.setEvaluationDirect(new ArrayList<Double>(List.of(0.0)));
        ga.refList.add(a);
        ga.refList.add(b);

        s.updateRefGenerator(ga);

        assertEquals(7.0, ga.refList.get(0).getEvaluation().get(0));
        assertEquals(7.0, ga.refList.get(1).getEvaluation().get(0));
    }

    @Test
    public void update_switchesToPSO_whenCountRefMatches() throws Exception {
        Strategy s = Strategy.getStrategy();
        // ensure problem has a type and a trivial objective so generator constructors won't NPE
        Problem p = new Problem();
        p.setTypeProblem(Problem.ProblemType.Maximizar);
        ObjetiveFunction of = new ObjetiveFunction() {
            @Override
            public Double Evaluation(State state) { return 0.0; }
        };
        ArrayList<ObjetiveFunction> funcs = new ArrayList<>(); funcs.add(of); p.setFunction(funcs);
        s.setProblem(p);
        // set PSO countRef so that update(countRef-1) will match
        ParticleSwarmOptimization.setCountRef(3);
        int trigger = ParticleSwarmOptimization.getCountRef() - 1;

        // initialize mapGenerators to avoid generator constructors expecting it
        s.initialize();
    s.update(trigger);

        assertNotNull(s.generator);
        assertTrue(s.generator.getType().equals(GeneratorType.ParticleSwarmOptimization));
    }

    @Test
    public void calculateOffLinePerformance_variousIndices() {
        Strategy s = Strategy.getStrategy();
        s.countPeriodChange = 4;
        s.calculateOffLinePerformance(8.0f, 0);
        assertEquals(2.0f, s.listOfflineError[0]);
        s.calculateOffLinePerformance(0.0f, 1);
        assertEquals(0.0f, s.listOfflineError[1]);
    }

    // HillClimbing stub
    static class StubGC extends Generator {
        public State ref = new State();

        @Override
        public State generate(Integer operatornumber) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, java.lang.reflect.InvocationTargetException, NoSuchMethodException { return new State(); }

        @Override
        public void updateReference(State stateCandidate, Integer countIterationsCurrent) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, java.lang.reflect.InvocationTargetException, NoSuchMethodException { }

    @Override
    public State getReference() { return ref; }

    @Override
    public void setInitialReference(State stateInitialRef) { this.ref = stateInitialRef; }

        @Override
        public GeneratorType getType() { return GeneratorType.HillClimbing; }

        @Override
        public java.util.List<State> getReferenceList() { return new ArrayList<>(); }

        @Override
        public java.util.List<State> getSonList() { return new ArrayList<>(); }

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

    // GA-like stub
    static class StubGA extends Generator {
        public List<State> refList = new ArrayList<>();

        @Override
        public State generate(Integer operatornumber) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, java.lang.reflect.InvocationTargetException, NoSuchMethodException { return new State(); }

        @Override
        public void updateReference(State stateCandidate, Integer countIterationsCurrent) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, java.lang.reflect.InvocationTargetException, NoSuchMethodException { }

    @Override
    public State getReference() { return (refList.isEmpty()) ? null : refList.get(0); }

        @Override
        public void setInitialReference(State stateInitialRef) { }

        @Override
        public GeneratorType getType() { return GeneratorType.GeneticAlgorithm; }

        @Override
        public java.util.List<State> getReferenceList() { return refList; }

        @Override
        public java.util.List<State> getSonList() { return new ArrayList<>(); }

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

    // TestState overrides getEvaluation to return the internal list (no defensive copy)
    static class TestState extends State {
        public TestState() { super(); }
        @Override
        public ArrayList<Double> getEvaluation() {
            return this.evaluation; // access protected field directly
        }
        // helper to set evaluation without defensive copy
        public void setEvaluationDirect(ArrayList<Double> list) {
            this.evaluation = list;
        }
    }

}
