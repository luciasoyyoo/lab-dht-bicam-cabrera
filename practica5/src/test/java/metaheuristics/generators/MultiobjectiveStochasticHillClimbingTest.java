package metaheuristics.generators;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import metaheurictics.strategy.Strategy;
import problem.definition.Operator;
import problem.definition.Problem;
import problem.definition.State;

public class MultiobjectiveStochasticHillClimbingTest {

    private Strategy strategy;
    private Problem backupProblem;

    @BeforeEach
    public void setUp() {
        strategy = Strategy.getStrategy();
        backupProblem = strategy.getProblem();
        Problem p = new Problem();
        p.setTypeProblem(Problem.ProblemType.Maximizar);
        p.setOperator(new Operator() {
            @Override
            public List<State> generatedNewState(State stateCurrent, Integer operatornumber) {
                return new ArrayList<State>();
            }

            @Override
            public List<State> generateRandomState(Integer operatornumber) {
                List<State> r = new ArrayList<State>();
                State s = new State(); ArrayList<Object> cs = new ArrayList<Object>(); cs.add("rand"); s.setCode(cs); r.add(s);
                return r;
            }
        });
        strategy.setProblem(p);
        // ensure generator non-null to avoid Dominance referencing it
        strategy.generator = new MultiobjectiveStochasticHillClimbing();
    }

    @AfterEach
    public void tearDown() {
        strategy.setProblem(backupProblem);
    }

    @Test
    public void testGenerate_returnsOperatorNeighbor() throws Exception {
        Problem p = strategy.getProblem();
        final State neighbor = new State();
        ArrayList<Object> code = new ArrayList<Object>(); code.add("nb"); neighbor.setCode(code);
        ArrayList<Double> eval = new ArrayList<Double>(); eval.add(1.0); neighbor.setEvaluation(eval);

        p.setOperator(new Operator() {
            @Override
            public List<State> generatedNewState(State stateCurrent, Integer operatornumber) {
                List<State> l = new ArrayList<State>(); l.add(neighbor); return l;
            }

            @Override
            public List<State> generateRandomState(Integer operatornumber) { return new ArrayList<State>(); }
        });

        MultiobjectiveStochasticHillClimbing gen = new MultiobjectiveStochasticHillClimbing();
        State ref = new State(); ArrayList<Object> rc = new ArrayList<Object>(); rc.add("r"); ref.setCode(rc); gen.setInitialReference(ref);
        State result = gen.generate(1);
        assertNotNull(result);
        assertEquals(neighbor.getCode(), result.getCode());
    }

    @Test
    public void testUpdateReference_acceptsCandidateWhenDominates() throws Exception {
        MultiobjectiveStochasticHillClimbing gen = new MultiobjectiveStochasticHillClimbing();
        State initial = new State(); ArrayList<Object> ic = new ArrayList<Object>(); ic.add("init"); initial.setCode(ic);
        ArrayList<Double> ie = new ArrayList<Double>(); ie.add(1.0); initial.setEvaluation(ie);
        gen.setInitialReference(initial);

        State candidate = new State(); ArrayList<Object> cc = new ArrayList<Object>(); cc.add("cand"); candidate.setCode(cc);
        ArrayList<Double> ce = new ArrayList<Double>(); ce.add(2.0); candidate.setEvaluation(ce);

        gen.updateReference(candidate, 0);

        assertEquals(candidate.getCode(), gen.getReference().getCode());

        List<State> refs = gen.getReferenceList();
        assertFalse(refs.isEmpty());
        assertEquals(candidate.getCode(), refs.get(refs.size()-1).getCode());
    }

    @Test
    public void testGetTrace_returnsWeight() {
        MultiobjectiveStochasticHillClimbing gen = new MultiobjectiveStochasticHillClimbing();
        float[] trace = gen.getTrace();
        assertNotNull(trace);
        // getTrace returns empty array in this class, check behavior is as implemented
        assertEquals(0, trace.length);
    }

}
