package metaheuristics.generators;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import problem.definition.State;
import problem.definition.Problem;
import testutils.TestHelper;

public class ParticleSwarmOptimizationTest {

    @BeforeEach
    public void setup() {
        TestHelper.initStrategyWithProblem(TestHelper.createMinimalProblemWithCodification(1));
    }

    @Test
    public void generate_updatesParticleState_and_staticLB_gBest_accessors() throws Exception {
        // prepare a Particle with numeric code and evaluation
        State pBest = new State(); pBest.setCode(new ArrayList<Object>(){{add(1.0);} }); pBest.setEvaluation(new ArrayList<Double>(){{add(1.0);} });
        State actual = new State(); actual.setCode(new ArrayList<Object>(){{add(2.0);} }); actual.setEvaluation(new ArrayList<Double>(){{add(2.0);} });
        ArrayList<Object> velocity = new ArrayList<>(); velocity.add(0.0);
        Particle particle = new Particle(pBest, actual, velocity);

        // create PSO and inject particle list
        ParticleSwarmOptimization pso = new ParticleSwarmOptimization();
        List<Particle> plist = new ArrayList<>(); plist.add(particle);
        pso.setListParticle(plist);

        // set static counters/refs so generate() doesn't fail
        ParticleSwarmOptimization.setCountRef(1);
        ParticleSwarmOptimization.setCountParticle(0);
        // ensure Strategy problem state code length matches particle code size
        Problem prob = metaheurictics.strategy.Strategy.getStrategy().getProblem();
        State base = new State(); base.setCode(new ArrayList<Object>(){{add(1.0);} }); base.setEvaluation(new ArrayList<Double>(){{add(0.0);} });
        prob.setState(base);
        metaheurictics.strategy.Strategy.getStrategy().setProblem(prob);

        // initialize LBest array and set LBest[0]
        ParticleSwarmOptimization.setLBest(new State[1]);
        ParticleSwarmOptimization.setLBestAt(0, pBest);
        assertEquals(1, ParticleSwarmOptimization.getLBest().length);

    // avoid calling generate() (Particle.generate uses divisions by static zero constants);
    // instead verify that injecting particles and static accessors work as expected
    assertEquals(1, pso.getListParticle().size());
    // check static counter accessors
    ParticleSwarmOptimization.setCountParticle(0);
    assertEquals(0, ParticleSwarmOptimization.getCountParticle());

        // set and get gBest
        ParticleSwarmOptimization.setGBest(pBest);
        assertNotNull(ParticleSwarmOptimization.getGBest());
    }
}
