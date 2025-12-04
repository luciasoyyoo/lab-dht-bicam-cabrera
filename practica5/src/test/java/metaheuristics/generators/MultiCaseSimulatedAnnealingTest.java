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

public class MultiCaseSimulatedAnnealingTest {

    private double backupTinitial;
    private int backupCountT;
    private Strategy strategy;

    @BeforeEach
    public void setUp() {
        strategy = Strategy.getStrategy();
        // backup static temperature and counters
        backupTinitial = MultiCaseSimulatedAnnealing.getTinitial();
        backupCountT = MultiCaseSimulatedAnnealing.getCountIterationsT();

        // Prepare a Problem with Maximizar and a default operator
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
        // ensure pareto list exists
        strategy.listRefPoblacFinal = new ArrayList<State>();
        // ensure a non-null generator in Strategy to avoid NPEs in dominance checks
        strategy.generator = new MultiCaseSimulatedAnnealing();
    }

    @AfterEach
    public void tearDown() {
        // restore static fields
        MultiCaseSimulatedAnnealing.setTinitial(backupTinitial);
        MultiCaseSimulatedAnnealing.setCountIterationsT(backupCountT);
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

        MultiCaseSimulatedAnnealing msa = new MultiCaseSimulatedAnnealing();
        State ref = new State(); ArrayList<Object> rc = new ArrayList<Object>(); rc.add("r"); ref.setCode(rc); msa.setInitialReference(ref);
        State result = msa.generate(1);
        assertNotNull(result);
        assertEquals(neighbor.getCode(), result.getCode());
    }

    @Test
    public void testUpdateReference_acceptsAndUpdatesTemperatureWhenCountReached() throws Exception {
        // Set initial static counters
        MultiCaseSimulatedAnnealing.setTinitial(200.0);
        MultiCaseSimulatedAnnealing.setCountIterationsT(2);

        MultiCaseSimulatedAnnealing msa = new MultiCaseSimulatedAnnealing();
        // prepare reference with low evaluation
        State initial = new State(); ArrayList<Object> ic = new ArrayList<Object>(); ic.add("init"); initial.setCode(ic);
        ArrayList<Double> ie = new ArrayList<Double>(); ie.add(1.0); initial.setEvaluation(ie);
        msa.setInitialReference(initial);

        // candidate that dominates the initial (higher evaluation)
        State candidate = new State(); ArrayList<Object> cc = new ArrayList<Object>(); cc.add("cand"); candidate.setCode(cc);
        ArrayList<Double> ce = new ArrayList<Double>(); ce.add(2.0); candidate.setEvaluation(ce);

        // call updateReference with countIterationsCurrent equal to getCountIterationsT()
        int beforeCount = MultiCaseSimulatedAnnealing.getCountIterationsT();
        double beforeT = MultiCaseSimulatedAnnealing.getTinitial();

        msa.updateReference(candidate, beforeCount);

        // reference should be updated to candidate
        assertEquals(candidate.getCode(), msa.getReference().getCode());

        // tinitial should be decreased by factor alpha
        assertEquals(beforeT * MultiCaseSimulatedAnnealing.alpha, MultiCaseSimulatedAnnealing.getTinitial(), 1e-6);

        // countIterationsT should be increased (old + old)
        assertEquals(beforeCount + beforeCount, MultiCaseSimulatedAnnealing.getCountIterationsT());
    }

    @Test
    public void testGetTrace_returnsWeight() throws Exception {
        MultiCaseSimulatedAnnealing msa = new MultiCaseSimulatedAnnealing();
        float[] trace = msa.getTrace();
        assertNotNull(trace);
        assertTrue(trace.length >= 1);
        assertEquals(50.0f, trace[0]);
    }

}
