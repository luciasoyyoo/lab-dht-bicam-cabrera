package local_search;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import local_search.candidate_type.GreaterCandidate;
import local_search.candidate_type.SmallerCandidate;
import local_search.candidate_type.RandomCandidate;
import local_search.complement.StopExecute;
import local_search.complement.TabuSolutions;
import local_search.complement.UpdateParameter;
import local_search.acceptation_type.Dominance;
import metaheurictics.strategy.Strategy;
import metaheuristics.generators.Generator;
import metaheuristics.generators.GeneratorType;
import problem.definition.ObjetiveFunction;
import problem.definition.Problem;
import problem.definition.State;

public class LocalSearchUnitTest {

    @BeforeEach
    public void beforeEach(){
        // ensure fresh singleton and clear static lists
        Strategy.destroyExecute();
        TabuSolutions.listTabu.clear();
    }

    @AfterEach
    public void afterEach(){
        Strategy.destroyExecute();
        TabuSolutions.listTabu.clear();
    }

    @Test
    public void testStopExecute_false_then_true(){
        StopExecute s = new StopExecute();
        assertFalse(s.stopIterations(0, 1));
        assertTrue(s.stopIterations(1, 1));
        assertTrue(s.stopIterations(2, 1));
    }

    @Test
    public void testTabuSolutions_noTabu_returnsSameList() throws Exception{
        TabuSolutions.listTabu.clear();
        TabuSolutions ts = new TabuSolutions();
        List<State> neigh = new ArrayList<>();
        State a = new State(); a.setCode(new ArrayList<Object>(List.of(1)));
        State b = new State(); b.setCode(new ArrayList<Object>(List.of(2)));
        neigh.add(a); neigh.add(b);
        List<State> result = ts.filterNeighborhood(new ArrayList<>(neigh));
        assertEquals(2, result.size());
    }

    @Test
    public void testTabuSolutions_filtersAndThrowsWhenEmpty(){
        TabuSolutions.listTabu.clear();
        State a = new State(); a.setCode(new ArrayList<Object>(List.of(1)));
        State b = new State(); b.setCode(new ArrayList<Object>(List.of(2)));
        // put both elements into tabu
        TabuSolutions.listTabu.add(a);
        TabuSolutions.listTabu.add(b);
        TabuSolutions ts = new TabuSolutions();
        List<State> neigh = new ArrayList<>();
        neigh.add(a); neigh.add(b);
        Exception ex = assertThrows(Exception.class, () -> ts.filterNeighborhood(new ArrayList<>(neigh)));
        assertNotNull(ex);
        TabuSolutions.listTabu.clear();
    }

    @Test
    public void testGreaterAndSmallerCandidate_behaviour(){
    // arrange so the first is larger than the second for deterministic behavior
    State s1 = new State(); s1.setEvaluation(new ArrayList<Double>(List.of(5.0)));
    State s2 = new State(); s2.setEvaluation(new ArrayList<Double>(List.of(1.0)));
        List<State> neigh = new ArrayList<>(); neigh.add(s1); neigh.add(s2);
        GreaterCandidate g = new GreaterCandidate();
        SmallerCandidate m = new SmallerCandidate();
        try{
            State greater = g.stateSearch(neigh);
            State smaller = m.stateSearch(neigh);
            assertEquals(5.0, greater.getEvaluation().get(0));
            assertEquals(1.0, smaller.getEvaluation().get(0));
        }catch(Exception e){
            fail("Exception not expected: " + e.getMessage());
        }
    }

    @Test
    public void testRandomCandidate_returnsElement(){
        State s1 = new State(); s1.setEvaluation(new ArrayList<Double>(List.of(1.0)));
        State s2 = new State(); s2.setEvaluation(new ArrayList<Double>(List.of(2.0)));
        List<State> neigh = new ArrayList<>(); neigh.add(s1); neigh.add(s2);
        RandomCandidate r = new RandomCandidate();
        State picked = r.stateSearch(neigh);
        assertTrue(picked == s1 || picked == s2);
    }

    @Test
    public void testDominance_maximizar_and_listDominance(){
        Strategy.destroyExecute();
        Strategy st = Strategy.getStrategy();
        Problem p = new Problem();
        p.setTypeProblem(Problem.ProblemType.Maximizar);
        // add an objective function that returns the first evaluation value (identity)
        p.setFunction(new ArrayList<>());
        p.getFunction().add(new ObjetiveFunction(){
            @Override
            public Double Evaluation(State state) {
                // if evaluation present, return it, else 0
                return (state.getEvaluation() == null) ? 0.0 : state.getEvaluation().get(0);
            }
        });
        st.setProblem(p);
        Dominance dom = new Dominance();
        // ensure generator is present to avoid NPE inside listDominance
        st.generator = new Generator(){
            @Override public State generate(Integer operatornumber) { return null; }
            @Override public void updateReference(State stateCandidate, Integer countIterationsCurrent) {
                /* no-op test stub: this anonymous Generator is only used to provide a
                 * non-null `generator` instance for the unit test. The production
                 * updateReference behavior is irrelevant to the test expectations,
                 * so we intentionally leave this method empty. */
            }
            @Override public State getReference() { return null; }
            @Override public void setInitialReference(State stateInitialRef) {
                /* intentionally empty: test stub providing a minimal Generator
                 * implementation; initial reference management is not required
                 * for the Dominance unit test. */
            }
            @Override public GeneratorType getType() { return GeneratorType.RandomSearch; }
            @Override public List<State> getReferenceList() { return new ArrayList<>(); }
            @Override public List<State> getSonList() { return new ArrayList<>(); }
            @Override public boolean awardUpdateREF(State stateCandidate) { return false; }
            @Override public void setWeight(float weight) {
                /* intentionally empty: weight adjustments are irrelevant for the
                 * Dominance tests that only exercise dominance/listDominance. */
            }
            @Override public float getWeight() { return 0; }
            @Override public float[] getTrace() { return new float[0]; }
            @Override public int[] getListCountBetterGender() { return new int[0]; }
            @Override public int[] getListCountGender() { return new int[0]; }
        };
        State x = new State(); x.setEvaluation(new ArrayList<Double>(List.of(5.0)));
        State y = new State(); y.setEvaluation(new ArrayList<Double>(List.of(3.0)));
        assertTrue(dom.dominance(x, y));
        assertFalse(dom.dominance(y, x));

        // listDominance: x should be added to empty list
        List<State> list = new ArrayList<>();
        boolean added = dom.listDominance(x, list);
        assertTrue(added);
        assertEquals(1, list.size());
    }

    @Test
    public void testUpdateParameter_basicIncrement(){
        Strategy.destroyExecute();
        Strategy st = Strategy.getStrategy();
        // set a dummy generator so updateParameter won't NPE when trying to set generator
        st.generator = new Generator(){
            @Override public State generate(Integer operatornumber) { return null; }
            @Override public void updateReference(State stateCandidate, Integer countIterationsCurrent) {
                /* intentionally empty: this anonymous Generator is a minimal test stub.
                   The UpdateParameter unit test only needs a non-null Generator instance;
                   it does not exercise updateReference. Providing a no-op implementation
                   avoids coupling the test to generator internals while preventing NPEs. */
            }
            @Override public State getReference() { return null; }
            /**
             * Sets the initial reference state used by the local search test/unit.
             *
             * <p>Hook method intended for subclasses or test setups that need to establish
             * an initial reference state before running local search operations. The
             * default implementation performs no action.
             *
             * <!--
             * This method is intentionally left empty (no-op) in this test class because
             * the default test scenarios do not require an explicit initial reference.
             * Subclasses or specific test cases can override this method to provide an
             * initial state when necessary.
             * -->
             *
             * @param stateInitialRef the initial reference State to be set (may be ignored)
             */
            @Override public void setInitialReference(State stateInitialRef) {}
            @Override public GeneratorType getType() { return GeneratorType.RandomSearch; }
            @Override public List<State> getReferenceList() { return new ArrayList<>(); }
            @Override public List<State> getSonList() { return new ArrayList<>(); }
            @Override public boolean awardUpdateREF(State stateCandidate) { return false; }
            /**
             * No-op implementation of setWeight.
             *
             * This method is provided to satisfy the contract of the overridden method,
             * but this concrete class does not maintain or use a weight value.
             *
             * @param weight the weight value to set; ignored by this implementation
             *
             * @implNote
             * The method is intentionally empty to preserve compatibility with the
             * superclass/interface while indicating that this implementation does not
             * support or store a weight. Subclasses that require weight behavior should
             * override this method and provide a concrete implementation.
             */
            @Override public void setWeight(float weight) {}
            @Override public float getWeight() { return 0; }
            @Override public float[] getTrace() { return new float[0]; }
            @Override public int[] getListCountBetterGender() { return new int[0]; }
            @Override public int[] getListCountGender() { return new int[0]; }
        };
        try{
            Integer next = UpdateParameter.updateParameter(0);
            assertEquals(1, next.intValue());
        }catch(Exception e){
            fail("UpdateParameter threw: " + e.getMessage());
        }
    }
}
