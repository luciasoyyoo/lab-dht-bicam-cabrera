package metaheuristics.generators;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import metaheurictics.strategy.Strategy;
import problem.definition.ObjetiveFunction;
import problem.definition.Problem;
import problem.definition.State;
import problem.definition.Problem.ProblemType;
import problem.definition.Operator;
import metaheuristics.generators.RandomSearch;

import metaheuristics.generators.EvolutionStrategies;
import metaheuristics.generators.GeneratorType;

public class EvolutionStrategiesTest {

    @BeforeEach
    public void beforeEach(){
        Strategy.destroyExecute();
        RandomSearch.listStateReference.clear();
        // ensure mapGenerators is initialized to avoid NPE in getListStateRef()
        Strategy.getStrategy().mapGenerators = new java.util.TreeMap<GeneratorType, Generator>();
    }

    private Problem makeProblem(ProblemType type, Operator op){
        Problem p = new Problem();
        p.setTypeProblem(type);
        ArrayList<ObjetiveFunction> funcs = new ArrayList<>();
        funcs.add(new ObjetiveFunction(){
            @Override
            public Double Evaluation(State state) { return 0.0; }
        });
        p.setFunction(funcs);
        p.setOperator(op);
        // set a base state used by generate() as copy
        State base = new State(); base.setEvaluation(new ArrayList<Double>(){{ add(0.0); }});
        p.setState(base);
        return p;
    }

    @Test
    public void testGetReference_selectsBestOrWorstBasedOnProblemType(){
        Operator noop = new Operator(){
            @Override
            public List<State> generatedNewState(State stateCurrent, Integer operatornumber) { return new ArrayList<>(); }
            @Override
            public List<State> generateRandomState(Integer operatornumber) { return new ArrayList<>(); }
        };
        EvolutionStrategies es = new EvolutionStrategies();
        // create three states with different evaluations
        State s1 = new State(); s1.setEvaluation(new ArrayList<Double>(){{ add(1.0); }});
        State s2 = new State(); s2.setEvaluation(new ArrayList<Double>(){{ add(5.0); }});
        State s3 = new State(); s3.setEvaluation(new ArrayList<Double>(){{ add(3.0); }});
        es.setListStateReference(Arrays.asList(s1, s2, s3));

        Strategy.getStrategy().setProblem(makeProblem(ProblemType.Maximizar, noop));
        State best = es.getReference();
        assertEquals(5.0, best.getEvaluation().get(0), 0.0);

        Strategy.getStrategy().setProblem(makeProblem(ProblemType.Minimizar, noop));
        State worst = es.getReference();
        assertEquals(1.0, worst.getEvaluation().get(0), 0.0);
    }

    @Test
    public void testGetListStateRef_usesRandomSearchStaticListWhenGeneratorEmpty(){
        // populate RandomSearch.listStateReference and mapGenerators so getListStateRef will add them
        State r1 = new State(); r1.setEvaluation(new ArrayList<Double>(){{ add(2.0); }});
        RandomSearch.listStateReference.add(r1);

        EvolutionStrategies es = new EvolutionStrategies();
        // ensure Strategy.mapGenerators contains an EvolutionStrategies entry so getListStateRef finds it
        Strategy.getStrategy().mapGenerators = new java.util.TreeMap<GeneratorType, Generator>();
        Strategy.getStrategy().mapGenerators.put(GeneratorType.EvolutionStrategies, new EvolutionStrategies());

        List<State> list = es.getListStateRef();
        assertFalse(list.isEmpty());
        assertEquals(1, list.size());
        assertEquals(2.0, list.get(0).getEvaluation().get(0), 0.0);
    }

    @Test
    public void testUpdateReference_generationalReplace_removesFirstAndAddsCandidate() throws Exception{
        Operator noop = new Operator(){
            @Override
            public List<State> generatedNewState(State stateCurrent, Integer operatornumber) { return new ArrayList<>(); }
            @Override
            public List<State> generateRandomState(Integer operatornumber) { return new ArrayList<>(); }
        };
        Strategy.getStrategy().setProblem(makeProblem(ProblemType.Maximizar, noop));

        EvolutionStrategies es = new EvolutionStrategies();
        State s1 = new State(); s1.setEvaluation(new ArrayList<Double>(){{ add(1.0); }});
        State s2 = new State(); s2.setEvaluation(new ArrayList<Double>(){{ add(2.0); }});
        es.setListStateReference(new ArrayList<State>(Arrays.asList(s1, s2)));

        State candidate = new State(); candidate.setEvaluation(new ArrayList<Double>(){{ add(9.0); }});
        es.updateReference(candidate, 0);
        List<State> after = es.getListStateReference();
        // GenerationalReplace removes index 0 and appends candidate
        assertEquals(2, after.size());
        assertEquals(2.0, after.get(0).getEvaluation().get(0), 0.0);
        assertEquals(9.0, after.get(1).getEvaluation().get(0), 0.0);
    }

    @Test
    public void testGettersAndSetters_andDefensiveCopies(){
        EvolutionStrategies es = new EvolutionStrategies();
        assertEquals(50.0f, es.getWeight(), 0.0f);
        es.setWeight(12.5f);
        assertEquals(12.5f, es.getWeight(), 0.0f);

        State s = new State(); s.setEvaluation(new ArrayList<Double>(){{ add(7.0); }});
        es.setListStateReference(Arrays.asList(s));
        List<State> l1 = es.getListStateReference();
        l1.clear();
        List<State> l2 = es.getListStateReference();
        assertEquals(1, l2.size());

        int[] cb = es.getListCountBetterGender(); cb[0] = 3;
        assertEquals(0, es.getListCountBetterGender()[0]);
        int[] c = es.getListCountGender(); c[0] = 4;
        assertEquals(0, es.getListCountGender()[0]);
        float[] t = es.getTrace(); t[0] = 0f;
        assertEquals(50.0f, es.getTrace()[0], 0.0f);
    }
}

