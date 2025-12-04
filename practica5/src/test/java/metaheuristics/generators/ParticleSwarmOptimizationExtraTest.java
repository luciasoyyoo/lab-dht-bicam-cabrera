package metaheuristics.generators;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import metaheurictics.strategy.Strategy;
import problem.definition.Problem;
import problem.definition.State;

public class ParticleSwarmOptimizationExtraTest {

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
    public void particle_updateReference_maximizar_updatesPBest() throws Exception{
        Strategy s = Strategy.getStrategy();
        Problem p = new Problem();
        p.setTypeProblem(Problem.ProblemType.Maximizar);
        s.setProblem(p);

        Particle particle = new Particle();
        State actual = new State();
        java.util.ArrayList<Object> code = new java.util.ArrayList<>(); code.add(Double.valueOf(1.0));
        actual.setCode(code);
        java.util.ArrayList<Double> evalA = new java.util.ArrayList<>(); evalA.add(Double.valueOf(10.0));
        actual.setEvaluation(evalA);

        State pbest = new State();
        java.util.ArrayList<Object> code2 = new java.util.ArrayList<>(); code2.add(Double.valueOf(0.0));
        pbest.setCode(code2);
        java.util.ArrayList<Double> evalP = new java.util.ArrayList<>(); evalP.add(Double.valueOf(1.0));
        pbest.setEvaluation(evalP);

        // set fields via reflection
        java.lang.reflect.Field fStateActual = Particle.class.getDeclaredField("stateActual");
        fStateActual.setAccessible(true);
        fStateActual.set(particle, actual);
        java.lang.reflect.Field fStatePBest = Particle.class.getDeclaredField("statePBest");
        fStatePBest.setAccessible(true);
        fStatePBest.set(particle, pbest);

        // call updateReference (Maximizar path uses stateActual vs statePBest)
        particle.updateReference(new State(), Integer.valueOf(0));

        State newPBest = (State) fStatePBest.get(particle);
        assertEquals(10.0, newPBest.getEvaluation().get(0));
    }

    @Test
    public void particle_updateReference_minimizar_updatesPBestFromCandidate() throws Exception{
        Strategy s = Strategy.getStrategy();
        Problem p = new Problem();
        p.setTypeProblem(Problem.ProblemType.Minimizar);
        s.setProblem(p);

        Particle particle = new Particle();
        State pbest = new State();
        java.util.ArrayList<Double> evalP = new java.util.ArrayList<>(); evalP.add(Double.valueOf(5.0));
        pbest.setEvaluation(evalP);
        java.lang.reflect.Field fStatePBest = Particle.class.getDeclaredField("statePBest");
        fStatePBest.setAccessible(true);
        fStatePBest.set(particle, pbest);

        State candidate = new State();
        java.util.ArrayList<Double> evalC = new java.util.ArrayList<>(); evalC.add(Double.valueOf(1.0));
        candidate.setEvaluation(evalC);

        particle.updateReference(candidate, Integer.valueOf(0));

        State newPBest = (State) fStatePBest.get(particle);
        assertEquals(1.0, newPBest.getEvaluation().get(0));
    }

    @Test
    public void pso_gBestInicial_selectsCorrectBest_forMax() throws Exception{
        // create two states
        State s1 = new State(); java.util.ArrayList<Double> e1 = new java.util.ArrayList<>(); e1.add(Double.valueOf(2.0)); s1.setEvaluation(e1);
        State s2 = new State(); java.util.ArrayList<Double> e2 = new java.util.ArrayList<>(); e2.add(Double.valueOf(5.0)); s2.setEvaluation(e2);
        // ensure environment is minimal so constructor doesn't throw
        Strategy st = Strategy.getStrategy();
        st.mapGenerators = new java.util.TreeMap<>();
        metaheuristics.generators.RandomSearch.listStateReference.clear();

        ParticleSwarmOptimization pso = new ParticleSwarmOptimization();
        ParticleSwarmOptimization.setLBest(new State[] { s1, s2 });

        Problem p = new Problem(); p.setTypeProblem(Problem.ProblemType.Maximizar); st.setProblem(p);

        State best = pso.gBestInicial();
        assertEquals(5.0, best.getEvaluation().get(0));
    }

    @Test
    public void pso_gBestInicial_selectsCorrectBest_forMin() throws Exception{
        State s1 = new State(); java.util.ArrayList<Double> e1 = new java.util.ArrayList<>(); e1.add(Double.valueOf(2.0)); s1.setEvaluation(e1);
        State s2 = new State(); java.util.ArrayList<Double> e2 = new java.util.ArrayList<>(); e2.add(Double.valueOf(5.0)); s2.setEvaluation(e2);
        Strategy st = Strategy.getStrategy();
        st.mapGenerators = new java.util.TreeMap<>();
        metaheuristics.generators.RandomSearch.listStateReference.clear();
        ParticleSwarmOptimization pso = new ParticleSwarmOptimization();
        ParticleSwarmOptimization.setLBest(new State[] { s1, s2 });
        Problem p = new Problem(); p.setTypeProblem(Problem.ProblemType.Minimizar); st.setProblem(p);
        State best = pso.gBestInicial();
        assertEquals(2.0, best.getEvaluation().get(0));
    }
}
