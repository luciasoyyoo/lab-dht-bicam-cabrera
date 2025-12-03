package metaheuristics.generators;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import metaheurictics.strategy.Strategy;
import problem.definition.State;

public class PSOSafeTest {

    @BeforeEach
    public void beforeEach() {
        // ensure clean strategy singleton
        Strategy.destroyExecute();
    }

    @AfterEach
    public void afterEach() {
        // reset static PSO arrays
        ParticleSwarmOptimization.setLBest(null);
        ParticleSwarmOptimization.setGBest(null);
        ParticleSwarmOptimization.setCountCurrentIterPSO(0);
        ParticleSwarmOptimization.setCountParticle(0);
        ParticleSwarmOptimization.setCountRef(0);
    }

    @Test
    public void testSetAndGetLBest_defensiveCopy() {
        State s1 = new State();
        State s2 = new State();
        State[] arr = new State[] { s1, s2 };

        ParticleSwarmOptimization.setLBest(arr);
        State[] got = ParticleSwarmOptimization.getLBest();
        assertEquals(2, got.length);
        assertNotSame(s1, got[0]); // defensive clone should produce distinct object

        // modifying original array should not affect returned copy
        arr[0] = null;
        State[] got2 = ParticleSwarmOptimization.getLBest();
        assertNotNull(got2[0]);
    }

    @Test
    public void testSetLBestAt_and_getters() {
        State s1 = new State();
        State s2 = new State();
        ParticleSwarmOptimization.setLBest(new State[] { s1, s2 });

        State s3 = new State();
        ParticleSwarmOptimization.setLBestAt(1, s3);

        State[] got = ParticleSwarmOptimization.getLBest();
        assertNotSame(s3, got[1]);

        ParticleSwarmOptimization.setGBest(s3);
        assertNotNull(ParticleSwarmOptimization.getGBest());
    }

    @Test
    public void testCountAccessors() {
        ParticleSwarmOptimization.setCountParticle(5);
        ParticleSwarmOptimization.setCountCurrentIterPSO(11);
        assertEquals(5, ParticleSwarmOptimization.getCountParticle());
        assertEquals(11, ParticleSwarmOptimization.getCountCurrentIterPSO());
    }
}
