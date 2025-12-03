package metaheuristics.generators;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import problem.definition.State;
import problem.definition.Problem;
import testutils.TestHelper;
import evolutionary_algorithms.complement.DistributionType;

public class DistributionEstimationAlgorithmTest {

    @BeforeEach
    public void setup() {
        TestHelper.initStrategyWithProblem(TestHelper.createMinimalProblemWithCodification(1));
    }

    @Test
    public void defaults_and_distributionType() {
        DistributionEstimationAlgorithm da = new DistributionEstimationAlgorithm();
        assertEquals(GeneratorType.DistributionEstimationAlgorithm, da.getType());
        assertEquals(DistributionType.Univariate, da.getDistributionType());
        float[] trace = da.getTrace();
        assertNotNull(trace);
        // length defined in class
        assertEquals(1200000, trace.length);
    }

    @Test
    public void maxValue_returnsStateWithMaxEvaluation() {
        DistributionEstimationAlgorithm da = new DistributionEstimationAlgorithm();
        List<State> list = new ArrayList<>();
        State s1 = new State();
        ArrayList<Double> e1 = new ArrayList<>(); e1.add(1.0); s1.setEvaluation(e1);
        State s2 = new State();
        ArrayList<Double> e2 = new ArrayList<>(); e2.add(5.0); s2.setEvaluation(e2);
        State s3 = new State();
        ArrayList<Double> e3 = new ArrayList<>(); e3.add(3.0); s3.setEvaluation(e3);
        list.add(s1); list.add(s2); list.add(s3);
        State max = da.maxValue(list);
        assertNotNull(max);
        assertEquals(5.0, max.getEvaluation().get(0));
    }

    @Test
    public void maxValue_and_getReference_behavior() throws Exception {
        DistributionEstimationAlgorithm dea = new DistributionEstimationAlgorithm();

        // create two states with different evaluations
        State s1 = new State(); s1.setCode(new ArrayList<Object>(){{add(1);} }); s1.setEvaluation(new ArrayList<Double>(){{add(1.0);} });
        State s2 = new State(); s2.setCode(new ArrayList<Object>(){{add(2);} }); s2.setEvaluation(new ArrayList<Double>(){{add(3.0);} });
        List<State> refs = new ArrayList<>(); refs.add(s1); refs.add(s2);
        dea.setListReference(refs);

        // default ProblemType is Maximizar from TestHelper
        State ref = dea.getReference();
        assertNotNull(ref);
        assertEquals(3.0, ref.getEvaluation().get(0));

        // also exercise maxValue directly
        State max = dea.maxValue(refs);
        assertEquals(3.0, max.getEvaluation().get(0));

        // switch to Minimizar and verify getReference picks smaller
        Problem p = metaheurictics.strategy.Strategy.getStrategy().getProblem();
        p.setTypeProblem(Problem.ProblemType.Minimizar);
        State refMin = dea.getReference();
        assertEquals(1.0, refMin.getEvaluation().get(0));

        // basic getters for trace and counts
        assertNotNull(dea.getListCountGender());
        assertNotNull(dea.getListCountBetterGender());
        assertNotNull(dea.getTrace());
    }

    @Test
    public void awardUpdateREF_detectsEquality() {
        DistributionEstimationAlgorithm da = new DistributionEstimationAlgorithm();
        State s = new State();
        ArrayList<Object> code = new ArrayList<>(); code.add("x"); s.setCode(code);
        ArrayList<State> backing = new ArrayList<>();
        backing.add(s);
        da.setListReference(backing);
        // awardUpdateREF uses Object.equals (identity), so passing the same instance should return true
        assertTrue(da.awardUpdateREF(s));
    }
}

