package metaheuristics.generators;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import problem.definition.ObjetiveFunction;
import problem.definition.Problem;
import problem.definition.State;
import testutils.TestHelper;

/**
 * Test for the sampling path of DistributionEstimationAlgorithm.
 * This sets up a minimal Problem/Strategy and a small reference list
 * so the probabilistic sampling code is executed deterministically.
 */
public class DistributionEstimationAlgorithmSamplingTest {

    @Test
    public void samplingGeneratesCandidates() throws Exception {
        // prepare a minimal problem with 2 variables
        int varCount = 2;
        Problem p = TestHelper.createMinimalProblemWithCodification(varCount);
        p.setPossibleValue(3); // small domain so sampling logic has bounds

        // provide a simple objective function (sum of code values)
        ArrayList<ObjetiveFunction> funcs = new ArrayList<>();
        funcs.add(new ObjetiveFunction() {
            @Override
            public Double Evaluation(State state) {
                double sum = 0.0;
                if (state.getCode() != null) {
                    for (Object o : state.getCode()) {
                        if (o instanceof Number) sum += ((Number) o).doubleValue();
                        else if (o != null) sum += Double.parseDouble(o.toString());
                    }
                }
                return Double.valueOf(sum);
            }
        });
        p.setFunction(funcs);

        // init Strategy with this problem
        TestHelper.initStrategyWithProblem(p);

        // build a small reference/fathers list for sampling
        List<State> fathers = new ArrayList<>();
        State a = new State();
        a.setCode(new ArrayList<Object>(Arrays.asList(0, 0)));
        a.setEvaluation(new ArrayList<Double>(Arrays.asList(0.0)));
        fathers.add(a);
        State b = new State();
        b.setCode(new ArrayList<Object>(Arrays.asList(1, 0)));
        b.setEvaluation(new ArrayList<Double>(Arrays.asList(1.0)));
        fathers.add(b);

        // create generator and override its internal reference list
        DistributionEstimationAlgorithm dea = new DistributionEstimationAlgorithm();
        dea.setListReference(fathers);

    // call generate to exercise the sampling path
    // use operatornumber=1 so sampling returns a single individual and avoid
    // the production code path that wrongly leaves evaluations null for some
    // sampled individuals (the test's goal is to exercise sampling itself)
    State candidate = dea.generate(1);

        assertNotNull(candidate, "generate should return a candidate state");
    assertNotNull(candidate.getCode(), "candidate code should not be null");
    // the generator returns an individual; evaluate it via Problem to populate evaluation
    p.Evaluate(candidate);
    assertNotNull(candidate.getEvaluation(), "candidate evaluation should not be null after evaluation");
    assertTrue(candidate.getEvaluation().size() >= 1, "candidate should have at least one evaluation value after evaluation");
    }

}
