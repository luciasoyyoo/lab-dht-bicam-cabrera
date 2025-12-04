package local_search.candidate_type;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import metaheurictics.strategy.Strategy;
import problem.definition.ObjetiveFunction;
import problem.definition.Problem;
import problem.definition.State;

public class NotDominatedCandidateTest {

    @AfterEach
    public void tearDown() {
        Strategy.destroyExecute();
    }

    private static class NumObjective extends ObjetiveFunction {
        @Override
        public Double Evaluation(State state) {
            // use state's number as objective value
            return Double.valueOf(state.getNumber());
        }
    }

    @Test
    public void singleElementReturnsThatState() throws Exception {
        NotDominatedCandidate cand = new NotDominatedCandidate();
        State s = new State();
        s.setNumber(7);
        List<State> list = Arrays.asList(s);

    Problem p = new Problem();
    p.setTypeProblem(Problem.ProblemType.Maximizar);
    ArrayList<ObjetiveFunction> funcs = new ArrayList<>();
    funcs.add(new NumObjective());
    p.setFunction(funcs);
        Strategy.getStrategy().setProblem(p);

        State res = cand.stateSearch(list);
        assertNotNull(res);
        assertEquals(7, res.getNumber());
    }

    @Test
    public void secondDominatesFirstMaximizar() throws Exception {
        NotDominatedCandidate cand = new NotDominatedCandidate();
        State a = new State(); a.setNumber(1);
        State b = new State(); b.setNumber(5);
        List<State> list = Arrays.asList(a, b);

    Problem p = new Problem();
    p.setTypeProblem(Problem.ProblemType.Maximizar);
    ArrayList<ObjetiveFunction> funcs = new ArrayList<>();
    funcs.add(new NumObjective());
    p.setFunction(funcs);
        Strategy.getStrategy().setProblem(p);

        State res = cand.stateSearch(list);
        assertNotNull(res);
        assertEquals(5, res.getNumber(), "should pick the dominating (larger) value for Maximizar");
    }

    @Test
    public void equalValuesReturnFirst() throws Exception {
        NotDominatedCandidate cand = new NotDominatedCandidate();
        State a = new State(); a.setNumber(3);
        State b = new State(); b.setNumber(3);
        List<State> list = Arrays.asList(a, b);

    Problem p = new Problem();
    p.setTypeProblem(Problem.ProblemType.Maximizar);
    ArrayList<ObjetiveFunction> funcs = new ArrayList<>();
    funcs.add(new NumObjective());
    p.setFunction(funcs);
        Strategy.getStrategy().setProblem(p);

        State res = cand.stateSearch(list);
        assertNotNull(res);
        assertEquals(3, res.getNumber(), "equal evaluations -> should return first element");
    }

    @Test
    public void secondDominatesFirstMinimizar() throws Exception {
        NotDominatedCandidate cand = new NotDominatedCandidate();
        State a = new State(); a.setNumber(10);
        State b = new State(); b.setNumber(2);
        List<State> list = Arrays.asList(a, b);

    Problem p = new Problem();
    p.setTypeProblem(Problem.ProblemType.Minimizar);
    ArrayList<ObjetiveFunction> funcs = new ArrayList<>();
    funcs.add(new NumObjective());
    p.setFunction(funcs);
        Strategy.getStrategy().setProblem(p);

        State res = cand.stateSearch(list);
        assertNotNull(res);
        assertEquals(2, res.getNumber(), "for Minimizar, smaller value dominates");
    }

}
