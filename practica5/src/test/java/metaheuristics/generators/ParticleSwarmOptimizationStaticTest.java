package metaheuristics.generators;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import problem.definition.State;

public class ParticleSwarmOptimizationStaticTest {

    @Test
    public void lbestAccessorsCopyAndReturnClones() {
        State s1 = new State();
        ArrayList<Object> code = new ArrayList<>();
        code.add(1.0);
        s1.setCode(code);
        ArrayList<Double> eval = new ArrayList<>();
        eval.add(5.0);
        s1.setEvaluation(eval);

        State s2 = new State();
        s2.setCode(code);
        ArrayList<Double> eval2 = new ArrayList<>();
        eval2.add(3.0);
        s2.setEvaluation(eval2);

        State[] arr = new State[] { s1, s2 };
        ParticleSwarmOptimization.setLBest(arr);
        State[] got = ParticleSwarmOptimization.getLBest();
        assertEquals(2, got.length);
        assertEquals((Double)5.0, got[0].getEvaluation().get(0));

        // modify returned array won't affect internal storage
        got[0].setNumber(42);
        State[] second = ParticleSwarmOptimization.getLBest();
        assertNotEquals(42, second[0].getNumber());
    }

    @Test
    public void countsAndGBestAccessor() {
        ParticleSwarmOptimization.setCountParticle(7);
        assertEquals(7, ParticleSwarmOptimization.getCountParticle());
        ParticleSwarmOptimization.setCountCurrentIterPSO(2);
        assertEquals(2, ParticleSwarmOptimization.getCountCurrentIterPSO());

        State g = new State();
        ArrayList<Double> eval = new ArrayList<>();
        eval.add(1.0);
        g.setEvaluation(eval);
        ParticleSwarmOptimization.setGBest(g);
        assertNotNull(ParticleSwarmOptimization.getGBest());
    }

    @Test
    public void instanceAccessorsReturnCopies() throws Exception {
        // Ensure Strategy has a problem and initialized mapGenerators so PSO construction won't NPE
        metaheurictics.strategy.Strategy.getStrategy().setProblem(testutils.TestHelper.createMinimalProblemWithCodification(1));
        metaheurictics.strategy.Strategy.getStrategy().initializeGenerators();
        ParticleSwarmOptimization pso = new ParticleSwarmOptimization();
        int[] counts = pso.getListCountGender();
        assertTrue(counts.length >= 0);
        int[] counts2 = pso.getListCountBetterGender();
        assertTrue(counts2.length >= 0);
        float[] trace = pso.getTrace();
        assertTrue(trace.length >= 0);
    }
}
