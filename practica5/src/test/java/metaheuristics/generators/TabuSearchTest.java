package metaheuristics.generators;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import local_search.complement.TabuSolutions;
import metaheurictics.strategy.Strategy;
import problem.definition.Operator;
import problem.definition.Problem;
import problem.definition.State;

public class TabuSearchTest {

    private Strategy strategy;
    private List<State> backupTabu;
    private Problem backupProblem;

    @BeforeEach
    public void setUp() {
        strategy = Strategy.getStrategy();
        // Backup and clear tabu
        backupTabu = new ArrayList<State>(TabuSolutions.listTabu);
        TabuSolutions.listTabu.clear();

        // Backup and set a safe problem with type Maximizar
        backupProblem = strategy.getProblem();
        Problem p = new Problem();
        p.setTypeProblem(Problem.ProblemType.Maximizar);
        // default operator returns empty neighborhood unless overridden by test
        p.setOperator(new Operator() {
            @Override
            public List<State> generatedNewState(State stateCurrent, Integer operatornumber) {
                return new ArrayList<State>();
            }

            @Override
            public List<State> generateRandomState(Integer operatornumber) {
                List<State> r = new ArrayList<State>();
                State s = new State(); ArrayList<Object> c = new ArrayList<Object>(); c.add("r"); s.setCode(c); r.add(s);
                return r;
            }
        });
        strategy.setProblem(p);
    }

    @AfterEach
    public void tearDown() {
        TabuSolutions.listTabu.clear();
        TabuSolutions.listTabu.addAll(backupTabu);
        strategy.setProblem(backupProblem);
    }

    @Test
    public void testGenerate_returnsNeighborFromOperator() throws Exception {
        Problem p = strategy.getProblem();
        final State neighbor = new State();
        ArrayList<Object> code = new ArrayList<Object>(); code.add("nb");
        neighbor.setCode(code);
        ArrayList<Double> eval = new ArrayList<Double>(); eval.add(1.0); neighbor.setEvaluation(eval);

        p.setOperator(new Operator() {
            @Override
            public List<State> generatedNewState(State stateCurrent, Integer operatornumber) {
                List<State> l = new ArrayList<State>(); l.add(neighbor); return l;
            }

            @Override
            public List<State> generateRandomState(Integer operatornumber) { return new ArrayList<State>(); }
        });

        TabuSearch ts = new TabuSearch();
        // ensure reference state exists
        State ref = new State(); ArrayList<Object> rc = new ArrayList<Object>(); rc.add("ref"); ref.setCode(rc); ts.setInitialReference(ref);

        State result = ts.generate(1);
        assertNotNull(result);
        assertEquals(neighbor.getCode(), result.getCode());
    }

    @Test
    public void testUpdateReference_acceptsAndAddsToTabu_whenTabuNonEmpty() throws Exception {
        TabuSearch ts = new TabuSearch();
        State initial = new State(); ArrayList<Object> ic = new ArrayList<Object>(); ic.add("i"); initial.setCode(ic); ts.setInitialReference(initial);

        // add sentinel so remove(0) in code won't throw
        State sentinel = new State(); ArrayList<Object> sc = new ArrayList<Object>(); sc.add("s"); sentinel.setCode(sc);
        ArrayList<Double> se = new ArrayList<Double>(); se.add(0.0); sentinel.setEvaluation(se);
        TabuSolutions.listTabu.add(sentinel);

        State candidate = new State(); ArrayList<Object> cc = new ArrayList<Object>(); cc.add("c"); candidate.setCode(cc);
        ArrayList<Double> ce = new ArrayList<Double>(); ce.add(2.0); candidate.setEvaluation(ce);

        ts.updateReference(candidate, 0);

        // reference should be updated
        assertEquals(candidate.getCode(), ts.getReference().getCode());

        // tabu list should now contain the candidate as last element
        assertFalse(TabuSolutions.listTabu.isEmpty());
        State last = TabuSolutions.listTabu.get(TabuSolutions.listTabu.size() - 1);
        assertEquals(candidate.getCode(), last.getCode());
    }

    @Test
    public void testGetReferenceListAndTraceAndCounts() throws Exception {
        TabuSearch ts = new TabuSearch();
        State s = new State(); ArrayList<Object> sc = new ArrayList<Object>(); sc.add("x"); s.setCode(sc); ts.setInitialReference(s);

        List<State> refList = ts.getReferenceList();
        assertNotNull(refList);
        assertFalse(refList.isEmpty());

        float[] trace = ts.getTrace();
        assertNotNull(trace);
        assertEquals(1200000, trace.length);
        assertEquals(50.0f, trace[0]);

        int[] cnt = ts.getListCountGender();
        int[] cnt2 = ts.getListCountBetterGender();
        assertNotNull(cnt); assertNotNull(cnt2);
        assertEquals(10, cnt.length);
        assertEquals(10, cnt2.length);
    }

}
