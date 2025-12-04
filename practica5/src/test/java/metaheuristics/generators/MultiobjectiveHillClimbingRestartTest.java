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

public class MultiobjectiveHillClimbingRestartTest {

    private Strategy strategy;
    private List<State> backupRef;

    @BeforeEach
    public void setUp() {
        strategy = Strategy.getStrategy();
        backupRef = new ArrayList<State>(strategy.listRefPoblacFinal);
        strategy.listRefPoblacFinal = new ArrayList<State>();
        // Ensure problem exists and is of type Maximizar
        Problem p = new Problem() {
            @Override
            public void Evaluate(State state) {
                // Default evaluation: if code contains string "good" -> 2.0, else 0.0
                List<Object> code = state.getCode();
                ArrayList<Double> eval = new ArrayList<Double>();
                if (code != null && code.size() > 0 && "good".equals(code.get(0))) {
                    eval.add(2.0);
                } else if (code != null && code.size() > 0 && "candidate".equals(code.get(0))) {
                    eval.add(0.5);
                } else {
                    eval.add(0.0);
                }
                state.setEvaluation(eval);
            }
        };
        p.setTypeProblem(Problem.ProblemType.Maximizar);
        // Provide a default operator to avoid NPEs; tests will override when needed
        p.setOperator(new Operator() {
            @Override
            public List<State> generatedNewState(State stateCurrent, Integer operatornumber) {
                return new ArrayList<State>();
            }

            @Override
            public List<State> generateRandomState(Integer operatornumber) {
                List<State> l = new ArrayList<State>();
                State s = new State();
                ArrayList<Object> code = new ArrayList<Object>();
                code.add("random");
                s.setCode(code);
                l.add(s);
                return l;
            }
        });
        strategy.setProblem(p);
        strategy.generator = new MultiobjectiveHillClimbingRestart();
    }

    @AfterEach
    public void tearDown() {
        strategy.listRefPoblacFinal = backupRef;
    }

    @Test
    public void testGenerate_returnsNeighborFromOperator() throws Exception {
        // Prepare operator that returns a specific neighbor
        Problem p = strategy.getProblem();
        final State neighbor = new State();
        ArrayList<Object> nc = new ArrayList<Object>();
        nc.add("nb");
        neighbor.setCode(nc);
        p.setOperator(new Operator() {
            @Override
            public List<State> generatedNewState(State stateCurrent, Integer operatornumber) {
                List<State> l = new ArrayList<State>();
                l.add(neighbor);
                return l;
            }

            @Override
            public List<State> generateRandomState(Integer operatornumber) {
                return new ArrayList<State>();
            }
        });

        MultiobjectiveHillClimbingRestart gen = new MultiobjectiveHillClimbingRestart();
        State result = gen.generate(1);
        assertNotNull(result);
        assertEquals(neighbor.getCode(), result.getCode());
    }

    @Test
    public void testUpdateReference_acceptsCandidateWhenDominates() throws Exception {
        MultiobjectiveHillClimbingRestart gen = new MultiobjectiveHillClimbingRestart();
        // initial reference with low evaluation
        State initial = new State();
        ArrayList<Object> ic = new ArrayList<Object>();
        ic.add("initial");
        initial.setCode(ic);
        ArrayList<Double> ie = new ArrayList<Double>(); ie.add(1.0);
        initial.setEvaluation(ie);
        gen.setStateRef(initial);

        // candidate with higher evaluation -> should be accepted
        State candidate = new State();
        ArrayList<Object> cc = new ArrayList<Object>(); cc.add("candidate");
        candidate.setCode(cc);
        ArrayList<Double> ce = new ArrayList<Double>(); ce.add(2.0);
        candidate.setEvaluation(ce);

        // call updateReference
        gen.updateReference(candidate, 0);

        // reference should be updated to candidate
        assertEquals(candidate.getCode(), gen.getReference().getCode());
        // reference list should contain at least one entry
        assertFalse(strategy.listRefPoblacFinal.isEmpty());
        // getTrace should return an array containing initial weight
        float[] trace = gen.getTrace();
        assertNotNull(trace);
        assertTrue(trace.length >= 1);
        assertEquals(50.0f, trace[0]);
    }

    @Test
    public void testUpdateReference_restartsUsingNeighborhoodWhenCandidateRejected() throws Exception {
        final MultiobjectiveHillClimbingRestart gen = new MultiobjectiveHillClimbingRestart();

        // initial reference with evaluation 1.0
        State initial = new State();
        ArrayList<Object> ic = new ArrayList<Object>(); ic.add("initial");
        initial.setCode(ic);
        ArrayList<Double> ie = new ArrayList<Double>(); ie.add(1.0);
        initial.setEvaluation(ie);
        gen.setStateRef(initial);

        // candidate that will be rejected (lower eval)
        State candidate = new State();
        ArrayList<Object> cc = new ArrayList<Object>(); cc.add("candidate");
        candidate.setCode(cc);
        ArrayList<Double> ce = new ArrayList<Double>(); ce.add(0.5);
        candidate.setEvaluation(ce);

        // Prepare neighborhood: first neighbor has code "good" so Evaluate will set eval=2.0 -> accepted
        Problem p = strategy.getProblem();
        p.setOperator(new Operator() {
            @Override
            public List<State> generatedNewState(State stateCurrent, Integer operatornumber) {
                List<State> l = new ArrayList<State>();
                State s1 = new State(); ArrayList<Object> c1 = new ArrayList<Object>(); c1.add("good"); s1.setCode(c1);
                l.add(s1);
                // add more dummy neighbors
                for (int i = 1; i < MultiobjectiveHillClimbingRestart.getSizeNeighbors(); i++) {
                    State s = new State(); ArrayList<Object> cs = new ArrayList<Object>(); cs.add("n"+i); s.setCode(cs); l.add(s);
                }
                return l;
            }

            @Override
            public List<State> generateRandomState(Integer operatornumber) {
                List<State> r = new ArrayList<State>();
                State s = new State(); ArrayList<Object> cs = new ArrayList<Object>(); cs.add("random"); s.setCode(cs); r.add(s);
                return r;
            }
        });

        // call updateReference with rejected candidate; it should pick a neighbor and accept it
        gen.updateReference(candidate, 0);

        // After update, reference should not be the original candidate (which was rejected), but should be one of neighborhood codes
        Object refCode = gen.getReference().getCode().get(0);
        assertTrue(refCode.equals("good") || refCode.toString().startsWith("n"));
    }

}
