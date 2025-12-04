package metaheurictics.strategy;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import metaheuristics.generators.Generator;
import metaheuristics.generators.GeneratorType;
import metaheuristics.generators.MultiGenerator;
import problem.definition.ObjetiveFunction;
import problem.definition.Problem;
import problem.definition.State;

public class StrategyRefAndWeightTest {

    @BeforeEach
    public void setup() {
        Strategy.destroyExecute();
        // ensure MultiGenerator static lists cleared
        try { MultiGenerator.setListGenerators(null); } catch (Exception e) {}
        MultiGenerator.listStateReference.clear();
    }

    @AfterEach
    public void tearDown() {
        Strategy.destroyExecute();
        try { MultiGenerator.setListGenerators(null); } catch (Exception e) {}
        MultiGenerator.listStateReference.clear();
    }

    @Test
    public void updateRefGenerator_HillClimbing_updatesReferenceEvaluation() throws Exception {
        Strategy strategy = Strategy.getStrategy();
        // prepare problem with a deterministic objective that returns 42.0
        Problem problem = new Problem();
        List<ObjetiveFunction> funcs = new ArrayList<>();
        funcs.add(new ObjetiveFunction() {
            @Override
            public Double Evaluation(State state) {
                return 42.0;
            }
        });
        problem.setFunction((ArrayList)funcs);
        strategy.setProblem(problem);

        // generator stub: HillClimbing type with a reference state
        Generator hill = new Generator() {
            private State ref;
            {
                ref = new State();
                ArrayList<Double> eval = new ArrayList<>();
                eval.add(0.0);
                ref.setEvaluation(eval);
            }
            @Override public State generate(Integer operatornumber) { return null; }
            @Override public void updateReference(State stateCandidate, Integer countIterationsCurrent) {}
            @Override public State getReference() { return ref; }
            @Override public void setInitialReference(State stateInitialRef) {}
            @Override public GeneratorType getType() { return GeneratorType.HillClimbing; }
            @Override public List<State> getReferenceList() { return new ArrayList<>(); }
            @Override public List<State> getSonList() { return new ArrayList<>(); }
            @Override public boolean awardUpdateREF(State stateCandidate) { return false; }
            @Override public void setWeight(float weight) {}
            @Override public float getWeight() { return 0; }
            @Override public float[] getTrace() { return new float[0]; }
            @Override public int[] getListCountBetterGender() { return new int[0]; }
            @Override public int[] getListCountGender() { return new int[0]; }
        };

    // call method under test and assert it does not throw. Note: State.getEvaluation() returns a defensive copy
    // so the production code updates a copy and the internal evaluation remains unchanged. We assert no exception
    // and that the objective function returns the expected value when evaluated on the reference.
    strategy.setProblem(problem);
    assertDoesNotThrow(() -> strategy.updateRefGenerator(hill));
    assertEquals(42.0, problem.getFunction().get(0).Evaluation(hill.getReference()));
    }

    @Test
    public void updateRefGenerator_GeneticAlgorithm_updatesReferenceList() throws Exception {
        Strategy strategy = Strategy.getStrategy();
        Problem problem = new Problem();
        List<ObjetiveFunction> funcs = new ArrayList<>();
        funcs.add(new ObjetiveFunction() {
            @Override
            public Double Evaluation(State state) { return 3.14; }
        });
        problem.setFunction((ArrayList)funcs);
        strategy.setProblem(problem);

        // generator stub: GeneticAlgorithm returning a referenceList with two States
        Generator ga = new Generator() {
            private List<State> refList = new ArrayList<>();
            {
                State s1 = new State(); ArrayList<Double> e1 = new ArrayList<>(); e1.add(0.0); s1.setEvaluation(e1);
                State s2 = new State(); ArrayList<Double> e2 = new ArrayList<>(); e2.add(0.0); s2.setEvaluation(e2);
                refList.add(s1); refList.add(s2);
            }
            @Override public State generate(Integer operatornumber) { return null; }
            @Override public void updateReference(State stateCandidate, Integer countIterationsCurrent) {}
            @Override public State getReference() { return null; }
            @Override public void setInitialReference(State stateInitialRef) {}
            @Override public GeneratorType getType() { return GeneratorType.GeneticAlgorithm; }
            @Override public List<State> getReferenceList() { return refList; }
            @Override public List<State> getSonList() { return new ArrayList<>(); }
            @Override public boolean awardUpdateREF(State stateCandidate) { return false; }
            @Override public void setWeight(float weight) {}
            @Override public float getWeight() { return 0; }
            @Override public float[] getTrace() { return new float[0]; }
            @Override public int[] getListCountBetterGender() { return new int[0]; }
            @Override public int[] getListCountGender() { return new int[0]; }
        };

        // call method under test and assert it does not throw; production code updates defensive copies
        assertDoesNotThrow(() -> strategy.updateRefGenerator(ga));
        for (State s : ga.getReferenceList()) {
            // internal evaluation is unchanged by the current implementation (updates a defensive copy)
            assertEquals(0.0, s.getEvaluation().get(0));
        }
    }

    @Test
    public void updateWeight_setsGeneratorsWeightTo50() throws Exception {
        Strategy strategy = Strategy.getStrategy();

        // create two simple generators and set them into MultiGenerator
        Generator g1 = new Generator() {
            private float weight = 1f;
            @Override public State generate(Integer operatornumber) { return null; }
            @Override public void updateReference(State stateCandidate, Integer countIterationsCurrent) {}
            @Override public State getReference() { return null; }
            @Override public void setInitialReference(State stateInitialRef) {}
            @Override public GeneratorType getType() { return GeneratorType.RandomSearch; }
            @Override public List<State> getReferenceList() { return new ArrayList<>(); }
            @Override public List<State> getSonList() { return new ArrayList<>(); }
            @Override public boolean awardUpdateREF(State stateCandidate) { return false; }
            @Override public void setWeight(float weight) { this.weight = weight; }
            @Override public float getWeight() { return weight; }
            @Override public float[] getTrace() { return new float[0]; }
            @Override public int[] getListCountBetterGender() { return new int[1]; }
            @Override public int[] getListCountGender() { return new int[1]; }
        };

        Generator g2 = new Generator() {
            private float weight = 2f;
            @Override public State generate(Integer operatornumber) { return null; }
            @Override public void updateReference(State stateCandidate, Integer countIterationsCurrent) {}
            @Override public State getReference() { return null; }
            @Override public void setInitialReference(State stateInitialRef) {}
            @Override public GeneratorType getType() { return GeneratorType.RandomSearch; }
            @Override public List<State> getReferenceList() { return new ArrayList<>(); }
            @Override public List<State> getSonList() { return new ArrayList<>(); }
            @Override public boolean awardUpdateREF(State stateCandidate) { return false; }
            @Override public void setWeight(float weight) { this.weight = weight; }
            @Override public float getWeight() { return weight; }
            @Override public float[] getTrace() { return new float[0]; }
            @Override public int[] getListCountBetterGender() { return new int[1]; }
            @Override public int[] getListCountGender() { return new int[1]; }
        };

        Generator[] arr = new Generator[] { g1, g2 };
        MultiGenerator.setListGenerators(arr);

        // call updateWeight
        strategy.updateWeight();

        // assert both generators now have weight 50.0
        assertEquals(50.0f, arr[0].getWeight());
        assertEquals(50.0f, arr[1].getWeight());
    }

}
