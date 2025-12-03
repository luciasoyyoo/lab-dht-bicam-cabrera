package metaheuristics.generators;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import problem.definition.Problem;
import problem.definition.State;
import testutils.TestHelper;

public class ParticleSwarmOptimizationTest {

    @BeforeEach
    public void setup() {
        TestHelper.initStrategyWithProblem(TestHelper.createMinimalProblemWithCodification(0));
        // make countMax safe for PSO/Particle formulas
        metaheurictics.strategy.Strategy.getStrategy().setCountMax(1);
    }

    @Test
    public void generateDelegatesToParticleAndReturnsState() throws Exception {
        ParticleSwarmOptimization.setCountCurrentIterPSO(0);
        ParticleSwarmOptimization.setCountRef(1);
        ParticleSwarmOptimization.setCountParticle(0);

        // create a particle with empty code (safe)
        State s = new State();
        s.setCode(new ArrayList<Object>());
        s.setEvaluation(new ArrayList<Double>());
        Particle particle = new Particle(s, s, new ArrayList<Object>());

    ParticleSwarmOptimization pso = new ParticleSwarmOptimization();
        // inject one particle to avoid IndexOutOfBounds
        List<Particle> list = new ArrayList<>();
        list.add(particle);
        pso.setListParticle(list);

    // override problem state to an empty-code state so PSO internals avoid divisions
    Problem prob = metaheurictics.strategy.Strategy.getStrategy().getProblem();
    prob.setState(new State());
    prob.getState().setCode(new ArrayList<Object>());
    metaheurictics.strategy.Strategy.getStrategy().setProblem(prob);

        State result = pso.generate(1);
        assertNotNull(result, "PSO.generate should return the particle's stateActual even if empty");
        assertNotNull(result.getCode());
    }

    @Test
    public void staticAccessorsAndLBest_gBest_work() throws Exception {
        // prepare a Particle with numeric code and evaluation
    State pBest = new State();
    ArrayList<Object> c1 = new ArrayList<>(); c1.add(1.0);
    pBest.setCode(c1);
    ArrayList<Double> e1 = new ArrayList<>(); e1.add(1.0);
    pBest.setEvaluation(e1);

    State actual = new State();
    ArrayList<Object> c2 = new ArrayList<>(); c2.add(2.0);
    actual.setCode(c2);
    ArrayList<Double> e2 = new ArrayList<>(); e2.add(2.0);
    actual.setEvaluation(e2);

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
    State base = new State();
    ArrayList<Object> bc = new ArrayList<>(); bc.add(1.0);
    base.setCode(bc);
    ArrayList<Double> be = new ArrayList<>(); be.add(0.0);
    base.setEvaluation(be);
        prob.setState(base);
        metaheurictics.strategy.Strategy.getStrategy().setProblem(prob);

        // initialize LBest array and set LBest[0]
        ParticleSwarmOptimization.setLBest(new State[1]);
        ParticleSwarmOptimization.setLBestAt(0, pBest);
        assertEquals(1, ParticleSwarmOptimization.getLBest().length);

        // verify particle injection and static accessors
        assertEquals(1, pso.getListParticle().size());
        ParticleSwarmOptimization.setCountParticle(0);
        assertEquals(0, ParticleSwarmOptimization.getCountParticle());

        // set and get gBest
        ParticleSwarmOptimization.setGBest(pBest);
        assertNotNull(ParticleSwarmOptimization.getGBest());
    }
}
