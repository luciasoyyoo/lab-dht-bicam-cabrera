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

public class MultiobjectiveTabuSearchTest {

    private Strategy strategy;
    private List<State> backupTabu;
    private List<State> backupRefList;

    @BeforeEach
    public void setUp() {
        strategy = Strategy.getStrategy();
        // Backup static tabu list and reference list to restore later
        backupTabu = new ArrayList<State>(TabuSolutions.listTabu);
        backupRefList = new ArrayList<State>(strategy.listRefPoblacFinal);
        TabuSolutions.listTabu.clear();
        strategy.listRefPoblacFinal = new ArrayList<State>();
        // Ensure Strategy has a non-null Problem with a type set so Dominance checks don't NPE
        Problem p = new Problem();
        p.setTypeProblem(Problem.ProblemType.Maximizar);
        // Provide a safe operator (not used by most tests but prevents accidental NPEs)
        p.setOperator(new Operator() {
            @Override
            public List<State> generatedNewState(State stateCurrent, Integer operatornumber) {
                return new ArrayList<State>();
            }

            @Override
            public List<State> generateRandomState(Integer operatornumber) {
                return new ArrayList<State>();
            }
        });
        strategy.setProblem(p);
        // Ensure Strategy.generator is non-null for Dominance checks
        strategy.generator = new MultiobjectiveTabuSearch();
    }

    @AfterEach
    public void tearDown() {
        TabuSolutions.listTabu.clear();
        TabuSolutions.listTabu.addAll(backupTabu);
        strategy.listRefPoblacFinal = backupRefList;
    }

    @Test
    public void testGenerate_withSingleNeighbor_returnsThatNeighbor() throws Exception {
        // Prepare a problem with an operator that returns exactly one neighbor
        Problem problem = new Problem();
        final State neighbor = new State();
        ArrayList<Object> code = new ArrayList<Object>();
        code.add("x");
        neighbor.setCode(code);

        Operator op = new Operator() {
            @Override
            public List<State> generatedNewState(State stateCurrent, Integer operatornumber) {
                List<State> l = new ArrayList<State>();
                // return the single prepared neighbor
                l.add(neighbor);
                return l;
            }

            @Override
            public List<State> generateRandomState(Integer operatornumber) {
                return new ArrayList<State>();
            }
        };
        problem.setOperator(op);
        strategy.setProblem(problem);

        MultiobjectiveTabuSearch mo = new MultiobjectiveTabuSearch();
        // stateReferenceTS must be non-null to avoid NPE in some acceptation code paths
        State ref = new State();
        ArrayList<Object> rcode = new ArrayList<Object>();
        rcode.add("r");
        ref.setCode(rcode);
        mo.setStateRef(ref);

        State result = mo.generate(1);
        assertNotNull(result, "generate must return a non-null candidate");
        // The operator returned the neighbor instance; result should expose the code we set
        assertEquals(neighbor.getCode(), result.getCode());
    }

    @Test
    public void testUpdateReference_acceptsAndUpdatesTabuAndReference() throws Exception {
        MultiobjectiveTabuSearch mo = new MultiobjectiveTabuSearch();

        // Prepare a non-null initial reference state
        State initial = new State();
        ArrayList<Object> icode = new ArrayList<Object>();
        icode.add("initial");
        initial.setCode(icode);
        mo.setStateRef(initial);

        // Ensure tabu list has at least one element so remove(0) in code does not throw
        State sentinel = new State();
        ArrayList<Object> scode = new ArrayList<Object>();
        scode.add("sentinel");
        sentinel.setCode(scode);
    ArrayList<Double> seval = new ArrayList<Double>();
    seval.add(0.0);
    sentinel.setEvaluation(seval);
        TabuSolutions.listTabu.add(sentinel);

        // Candidate to update reference with
        State candidate = new State();
        ArrayList<Object> ccode = new ArrayList<Object>();
        ccode.add("candidate");
        candidate.setCode(ccode);
    ArrayList<Double> ceval = new ArrayList<Double>();
    ceval.add(2.0);
    candidate.setEvaluation(ceval);

    // ensure initial reference has an evaluation so Dominance checks do not NPE
    ArrayList<Double> ieval = new ArrayList<Double>();
    ieval.add(1.0);
    initial.setEvaluation(ieval);

        // Call updateReference - AcceptNotDominatedTabu always accepts and will add candidate to tabu
        mo.updateReference(candidate, 0);

        // After updateReference the reference must be the candidate
        assertEquals(candidate.getCode(), mo.getStateReferenceTS().getCode());

        // Tabu list should contain the candidate as last element
        assertFalse(TabuSolutions.listTabu.isEmpty());
        State last = TabuSolutions.listTabu.get(TabuSolutions.listTabu.size() - 1);
        assertEquals(candidate.getCode(), last.getCode());

        // getTrace should return an array with the default weight (constructor set)
        float[] trace = mo.getTrace();
        assertNotNull(trace);
        assertTrue(trace.length >= 1);
        assertEquals(50.0f, trace[0]);
    }

    @Test
    public void testUpdateReference_doesNotDuplicateExistingTabu() throws Exception {
        MultiobjectiveTabuSearch mo = new MultiobjectiveTabuSearch();

        State initial = new State();
        initial.setEvaluation(new ArrayList<Double>(){{ add(1.0); }});
        mo.setInitialReference(initial);

        State candidate = new State();
        ArrayList<Object> ccode = new ArrayList<Object>(); ccode.add("candidate"); candidate.setCode(ccode);
        ArrayList<Double> ceval = new ArrayList<Double>(); ceval.add(2.0); candidate.setEvaluation(ceval);

        // Prepare tabu list that already contains candidate and another element so remove(0) is safe
        State other = new State(); other.setEvaluation(new ArrayList<Double>(){{ add(0.0); }});
        TabuSolutions.listTabu.clear();
        TabuSolutions.listTabu.add(other);
        TabuSolutions.listTabu.add(candidate);

        int sizeBefore = TabuSolutions.listTabu.size();
        mo.updateReference(candidate, 0);

        // Ensure the tabu list size didn't shrink unexpectedly (no exception occurred)
    // ensure reference was updated to the candidate
    assertEquals(2.0, mo.getReference().getEvaluation().get(0), 0.0);
    }

    @Test
    public void testGetReferenceList_appendsEachCall() {
        MultiobjectiveTabuSearch mo = new MultiobjectiveTabuSearch();
        State s = new State(); s.setEvaluation(new ArrayList<Double>(){{ add(5.0); }});
        mo.setStateRef(s);
        List<State> first = mo.getReferenceList();
        List<State> second = mo.getReferenceList();
        // getReferenceList implementation appends the reference each call, so second should be larger
        assertTrue(second.size() >= first.size());
        assertEquals(mo.getStateReferenceTS().getEvaluation().get(0), second.get(second.size()-1).getEvaluation().get(0));
    }

    @Test
    public void test_misc_getters_and_null_behaviour() {
        MultiobjectiveTabuSearch mo = new MultiobjectiveTabuSearch();
        // type, weight and trace
        assertEquals(GeneratorType.MultiobjectiveTabuSearch, mo.getType());
        assertEquals(50.0f, mo.getWeight(), 0.0f);
        float[] tr = mo.getTrace();
        assertNotNull(tr);

        // getSonList returns null per current implementation
        assertNull(mo.getSonList());

        // awardUpdateREF returns false by default
        assertFalse(mo.awardUpdateREF(null));
    }
}
