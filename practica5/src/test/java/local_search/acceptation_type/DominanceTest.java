package local_search.acceptation_type;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import metaheurictics.strategy.Strategy;
import metaheuristics.generators.RandomSearch;
import problem.definition.State;
import problem.definition.Problem;

public class DominanceTest {

    @Test
    public void dominanceMaximizarAndMinimizar() {
        Strategy.getStrategy().setProblem(testutils.TestHelper.createMinimalProblemWithCodification(1));
        Problem p = Strategy.getStrategy().getProblem();
        p.setTypeProblem(Problem.ProblemType.Maximizar);

        State a = new State();
        ArrayList<Double> ea = new ArrayList<>();
        ea.add(5.0);
        a.setEvaluation(ea);

        State b = new State();
        ArrayList<Double> eb = new ArrayList<>();
        eb.add(3.0);
        b.setEvaluation(eb);

        Dominance d = new Dominance();
        assertTrue(d.dominance(a, b));
        assertFalse(d.dominance(b, a));

        p.setTypeProblem(Problem.ProblemType.Minimizar);
        assertTrue(d.dominance(b, a));
        assertFalse(d.dominance(a, b));
    }

    @Test
    public void listDominanceRemovesAndAdds() {
        Strategy.getStrategy().setProblem(testutils.TestHelper.createMinimalProblemWithCodification(1));
        // ensure generator is set to avoid internal checks referencing it
        Strategy.getStrategy().generator = new RandomSearch();

        ArrayList<State> list = new ArrayList<>();
        State current = new State();
        ArrayList<Double> evalC = new ArrayList<>();
        evalC.add(2.0);
        current.setEvaluation(evalC);
        list.add(current.clone());

        State candidate = new State();
        ArrayList<Double> evalCand = new ArrayList<>();
        evalCand.add(5.0);
        candidate.setEvaluation(evalCand);

        Dominance d = new Dominance();
        boolean added = d.listDominance(candidate, list);
        assertTrue(added);
        // candidate should now be in the list
        assertTrue(list.stream().anyMatch(s -> s.getEvaluation().get(0).equals(5.0)));
    }
}
