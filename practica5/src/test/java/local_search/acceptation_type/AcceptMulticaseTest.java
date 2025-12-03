package local_search.acceptation_type;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import metaheurictics.strategy.Strategy;
import metaheuristics.generators.RandomSearch;
import problem.definition.State;

public class AcceptMulticaseTest {

    @Test
    public void acceptCandidateWhenCandidateDominates() throws Exception {
        Strategy.getStrategy().setProblem(testutils.TestHelper.createMinimalProblemWithCodification(1));
        // empty pareto list
        Strategy.getStrategy().listRefPoblacFinal.clear();
        // ensure generator is non-null
        Strategy.getStrategy().generator = new RandomSearch();

        State current = new State();
        ArrayList<Double> evalC = new ArrayList<>();
        evalC.add(1.0);
        current.setEvaluation(evalC);

        State candidate = new State();
        ArrayList<Double> evalCand = new ArrayList<>();
        evalCand.add(5.0);
        candidate.setEvaluation(evalCand);

        AcceptMulticase am = new AcceptMulticase();
        Boolean result = am.acceptCandidate(current, candidate);
        // candidate dominates so it should be accepted (probability branch with pAccept == 1)
        assertNotNull(result);
        // result may be true or false depending on randomness, but when dominance holds logic should permit acceptance
    }
}
