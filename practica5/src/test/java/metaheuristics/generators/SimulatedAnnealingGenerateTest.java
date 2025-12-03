package metaheuristics.generators;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import problem.definition.State;
import problem.definition.Problem;
import problem.definition.Operator;
import testutils.TestHelper;

public class SimulatedAnnealingGenerateTest {

    @BeforeEach
    public void setup() {
        TestHelper.initStrategyWithProblem(TestHelper.createMinimalProblemWithCodification(1));
    }

    @Test
    public void generate_returnsCandidate_fromOperatorNeighborhood() throws Exception {
        // prepare Problem with operator returning a controlled neighborhood
        Problem p = metaheurictics.strategy.Strategy.getStrategy().getProblem();
        p.setOperator(new Operator() {
            @Override
            public List<State> generatedNewState(State stateCurrent, Integer operatornumber) {
                List<State> neigh = new ArrayList<>();
                State s = new State();
                s.setCode(new ArrayList<Object>(){{add(42);} });
                ArrayList<Double> eval = new ArrayList<>(); eval.add(2.5);
                s.setEvaluation(eval);
                neigh.add(s);
                return neigh;
            }

            @Override
            public List<State> generateRandomState(Integer operatornumber) {
                return new ArrayList<State>();
            }
        });

        // create SA and set an initial reference state
        SimulatedAnnealing sa = new SimulatedAnnealing();
        State ref = new State(); ref.setCode(new ArrayList<Object>(){{add(1);} });
        ArrayList<Double> eref = new ArrayList<>(); eref.add(1.0); ref.setEvaluation(eref);
        sa.setStateRef(ref);

        // call generate() — should return a candidate created by CandidateValue logic using neighborhood
        State candidate = sa.generate(0);
        assertNotNull(candidate);
        // candidate is produced by CandidateValue.stateCandidate; ensure it has evaluation (non-null)
        assertNotNull(candidate.getEvaluation());

        // exercise updateReference: use same candidate and ensure no exceptions
        sa.updateReference(candidate, SimulatedAnnealing.getCountIterationsT());

        // getters
        assertEquals(sa.getType(), GeneratorType.SimulatedAnnealing);
        assertNotNull(sa.getReferenceList());
        assertNotNull(sa.getListCountGender());
        assertNotNull(sa.getListCountBetterGender());
        assertNotNull(sa.getTrace());
    }
}
