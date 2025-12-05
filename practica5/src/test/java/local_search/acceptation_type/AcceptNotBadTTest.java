package local_search.acceptation_type;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import testutils.TestHelper;
import metaheurictics.strategy.Strategy;
import problem.definition.State;
import problem.definition.Problem;

public class AcceptNotBadTTest {

    @BeforeEach
    public void setup() {
        TestHelper.initStrategyWithProblem(TestHelper.createMinimalProblemWithCodification(1));
    }

    @AfterEach
    public void tearDown() {
        Strategy.destroyExecute();
    }

    @Test
    public void maximizar_candidateBetter_executesRandomAndAccepts() throws Exception {
        // arrange
        Strategy.getStrategy().getProblem().setTypeProblem(Problem.ProblemType.Maximizar);
        State current = new State(); current.setEvaluation(new java.util.ArrayList<Double>(){{ add(0.0); }});
        State candidate = new State(); candidate.setEvaluation(new java.util.ArrayList<Double>(){{ add(1.0); }});
        AcceptNotBadT acceptor = new AcceptNotBadT();

        // act
        Boolean accepted = acceptor.acceptCandidate(current, candidate);

        // assert: candidate is better so should be accepted (this also executes ThreadLocalRandom.current().nextDouble())
        assertTrue(accepted.booleanValue());
    }

    @Test
    public void minimizar_candidateWorse_butExpLarge_executesRandomAndAccepts() throws Exception {
        // arrange: set Minimizar and force Tinitial small so exp(result_min) > 1 and the random comparison is true
        Strategy.getStrategy().getProblem().setTypeProblem(Problem.ProblemType.Minimizar);
        // very small temperature to make exp((cand-cur)/T) large
        metaheuristics.generators.SimulatedAnnealing.setTinitial(1e-4);
        State current = new State(); current.setEvaluation(new java.util.ArrayList<Double>(){{ add(0.0); }});
        State candidate = new State(); candidate.setEvaluation(new java.util.ArrayList<Double>(){{ add(100.0); }});
        AcceptNotBadT acceptor = new AcceptNotBadT();

        // act
        Boolean accepted = acceptor.acceptCandidate(current, candidate);

        // assert: because exp(...) is huge (>1), the OR condition will evaluate true regardless of the RNG
        assertTrue(accepted.booleanValue());
    }
}
