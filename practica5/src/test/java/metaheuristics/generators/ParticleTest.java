package metaheuristics.generators;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import problem.definition.Problem;
import problem.definition.State;
import testutils.TestHelper;

public class ParticleTest {

    @Test
    public void generateWithEmptyCodeDoesNotThrow() throws Exception {
        Problem p = TestHelper.createMinimalProblemWithCodification(0);
        // ensure no division by zero in Strategy-based formulas
        TestHelper.initStrategyWithProblem(p);
        // small safe values
        TestHelper.initStrategyWithProblem(p);
        // ensure countMax != 0
        metaheurictics.strategy.Strategy.getStrategy().setCountMax(1);
        // ensure PSO iter counters safe
        ParticleSwarmOptimization.setCountCurrentIterPSO(0);

    // prepare particle with empty codes so loops are skipped and no division by zero occurs
        State sActual = new State();
        sActual.setCode(new ArrayList<Object>());
        State sPbest = new State();
        sPbest.setCode(new ArrayList<Object>());

    // override the problem's state to ensure PSO/Particle use our empty-code state
    p.setState(sActual);
    metaheurictics.strategy.Strategy.getStrategy().setProblem(p);

        Particle particle = new Particle(sPbest, sActual, new ArrayList<Object>());

        // call generate; production method returns null but must not throw and must set internal fields
        State ret = particle.generate(1);
        assertNull(ret, "Particle.generate currently returns null (side-effects expected)");
        // velocity and stateActual code remain empty but accessible
        assertNotNull(particle.getVelocity());
        assertEquals(0, particle.getVelocity().size());
        assertNotNull(particle.getStateActual().getCode());
        assertEquals(0, particle.getStateActual().getCode().size());
    }
}
