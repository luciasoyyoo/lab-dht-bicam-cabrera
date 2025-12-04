package metaheuristics.generators;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import problem.definition.State;
import metaheurictics.strategy.Strategy;
import problem.definition.Problem;

public class ParticleSwarmOptimizationMoreTest {

    @AfterEach
    public void tearDown(){
        Strategy.destroyExecute();
        ParticleSwarmOptimization.setLBest(null);
        ParticleSwarmOptimization.setGBest(null);
        ParticleSwarmOptimization.setCountRef(0);
        ParticleSwarmOptimization.setCountParticle(0);
        ParticleSwarmOptimization.setCountCurrentIterPSO(0);
    }

    @Test
    public void lBestSetAndGetAreDefensiveCopies() throws Exception{
        State s = new State();
        java.util.ArrayList<Double> eval = new java.util.ArrayList<>(); eval.add(Double.valueOf(3.14)); s.setEvaluation(eval);
        State[] arr = new State[] { s };
        ParticleSwarmOptimization.setLBest(arr);

        // mutate original
        eval.set(0, Double.valueOf(9.99));

        State[] got = ParticleSwarmOptimization.getLBest();
        // should not reflect mutation (defensive copy)
        assertEquals(3.14, got[0].getEvaluation().get(0));
    }

    @Test
    public void setLBest_nullResultsInEmptyGet() {
        ParticleSwarmOptimization.setLBest(null);
        State[] got = ParticleSwarmOptimization.getLBest();
        assertNotNull(got);
        assertEquals(0, got.length);
    }

    @Test
    public void setLBestAt_outOfRangeDoesNothing() throws Exception{
        State s1 = new State(); java.util.ArrayList<Double> e1 = new java.util.ArrayList<>(); e1.add(Double.valueOf(1.0)); s1.setEvaluation(e1);
        State s2 = new State(); java.util.ArrayList<Double> e2 = new java.util.ArrayList<>(); e2.add(Double.valueOf(2.0)); s2.setEvaluation(e2);
        ParticleSwarmOptimization.setLBest(new State[] { s1, s2 });

        // attempt out of range index
        ParticleSwarmOptimization.setLBestAt(5, new State());

        State[] got = ParticleSwarmOptimization.getLBest();
        assertEquals(2, got.length);
        assertEquals(1.0, got[0].getEvaluation().get(0));
        assertEquals(2.0, got[1].getEvaluation().get(0));
    }

    @Test
    public void gBestSetAndGet() {
        State s = new State(); java.util.ArrayList<Double> e = new java.util.ArrayList<>(); e.add(Double.valueOf(7.0)); s.setEvaluation(e);
        ParticleSwarmOptimization.setGBest(s);
        State g = ParticleSwarmOptimization.getGBest();
        assertNotNull(g);
        assertEquals(7.0, g.getEvaluation().get(0));
    }

    @Test
    public void countSettersAndGetters() {
        ParticleSwarmOptimization.setCountParticle(12);
        assertEquals(12, ParticleSwarmOptimization.getCountParticle());
        ParticleSwarmOptimization.setCountCurrentIterPSO(5);
        assertEquals(5, ParticleSwarmOptimization.getCountCurrentIterPSO());
    }
}
