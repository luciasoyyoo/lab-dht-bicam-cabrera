package metaheuristics.generators;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import metaheurictics.strategy.Strategy;
import problem.definition.Problem;
import problem.definition.State;

public class ParticleSwarmOptimizationNewTest {

    @AfterEach
    public void tearDown(){
        // reset static PSO state to defaults to avoid cross-test pollution
        ParticleSwarmOptimization.setCountParticle(0);
        ParticleSwarmOptimization.setCountRef(0);
        ParticleSwarmOptimization.setCoutSwarm(0);
        ParticleSwarmOptimization.setCountParticleBySwarm(0);
        ParticleSwarmOptimization.setLBest(null);
        ParticleSwarmOptimization.setGBest(null);
        Strategy.destroyExecute();
    }

    @Test
    public void testSettersUpdateCountRefAndGetters() {
        // default 0
        assertEquals(0, ParticleSwarmOptimization.getCoutSwarm());
        assertEquals(0, ParticleSwarmOptimization.getCountParticleBySwarm());

        ParticleSwarmOptimization.setCoutSwarm(3);
        ParticleSwarmOptimization.setCountParticleBySwarm(4);

        assertEquals(3, ParticleSwarmOptimization.getCoutSwarm());
        assertEquals(4, ParticleSwarmOptimization.getCountParticleBySwarm());
        // countRef should be kept in sync by setters (3 * 4 = 12)
        assertEquals(12, ParticleSwarmOptimization.getCountRef());

        // change one and expect updated product
        ParticleSwarmOptimization.setCountParticleBySwarm(2);
        assertEquals(3 * 2, ParticleSwarmOptimization.getCountRef());
    }

    @Test
    public void testSettersRejectNegative() {
        assertThrows(IllegalArgumentException.class, () -> ParticleSwarmOptimization.setCoutSwarm(-1));
        assertThrows(IllegalArgumentException.class, () -> ParticleSwarmOptimization.setCountParticleBySwarm(-5));
    }

    @Test
    public void testInicialiceLBest_selectsBestPerSwarm() throws Exception {
        // ensure Strategy.mapGenerators exists to avoid NPEs in PSO logic
        Strategy.getStrategy().mapGenerators = new java.util.TreeMap<GeneratorType, Generator>();

        // configure 2 swarms with 2 particles each (total 4)
        ParticleSwarmOptimization.setCoutSwarm(2);
        ParticleSwarmOptimization.setCountParticleBySwarm(2);
        ParticleSwarmOptimization.setCountRef(2 * 2);
        ParticleSwarmOptimization.setCountParticle(0);

        // build PSO and inject 4 particles with pBest evaluations
        ParticleSwarmOptimization pso = new ParticleSwarmOptimization();
        List<Particle> parts = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            State pBest = new State();
            ArrayList<Double> ev = new ArrayList<>();
            // values: swarm0 -> 1.0, 2.0 ; swarm1 -> 3.0, 0.5
            double val = (i == 0) ? 1.0 : (i == 1) ? 2.0 : (i == 2) ? 3.0 : 0.5;
            ev.add(val);
            pBest.setEvaluation(ev);
            State actual = new State(); actual.setEvaluation(new ArrayList<Double>(List.of(0.0)));
            Particle particle = new Particle(pBest, actual, new ArrayList<>());
            parts.add(particle);
        }
        pso.setListParticle(parts);

        // minimal problem setup: only need type (Maximizar) for comparisons
        Strategy.getStrategy().setProblem(new Problem());
        Strategy.getStrategy().getProblem().setTypeProblem(Problem.ProblemType.Maximizar);

        // prepare lBest array of proper size and run initializer
        ParticleSwarmOptimization.setLBest(new State[ParticleSwarmOptimization.getCoutSwarm()]);
        ParticleSwarmOptimization.setCountParticle(0);
        pso.inicialiceLBest();

        State[] lbest = ParticleSwarmOptimization.getLBest();
        assertEquals(2, lbest.length);
        // expect best per swarm: first swarm -> 2.0, second -> 3.0
        assertEquals(2.0, lbest[0].getEvaluation().get(0), 0.0);
        assertEquals(3.0, lbest[1].getEvaluation().get(0), 0.0);
    }

}
