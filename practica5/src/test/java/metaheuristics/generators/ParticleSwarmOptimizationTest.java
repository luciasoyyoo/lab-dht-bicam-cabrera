package metaheuristics.generators;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import problem.definition.Problem;
import problem.definition.State;
import testutils.TestHelper;
import problem.definition.ObjetiveFunction;
import metaheurictics.strategy.Strategy;

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

    @AfterEach
    public void afterEach(){
        Strategy.destroyExecute();
        RandomSearch.listStateReference.clear();
        // ensure mapGenerators exists to avoid NPEs if any code checks it
        Strategy.getStrategy().mapGenerators = new java.util.TreeMap<GeneratorType, Generator>();
    }

    private Problem makeProblem(Problem.ProblemType type){
        Problem p = new Problem();
        p.setTypeProblem(type);
        ArrayList<ObjetiveFunction> funcs = new ArrayList<>();
        funcs.add(new ObjetiveFunction(){
            @Override
            public Double Evaluation(State state) { return 0.0; }
        });
        p.setFunction(funcs);
        // add a base state
        State base = new State(); base.setEvaluation(new ArrayList<Double>(){{ add(0.0); }});
        p.setState(base);
        return p;
    }

    @Test
    public void testSetAndGetLBest_and_defensiveCopyBehavior(){
        // prepare two states
        State a = new State(); a.setEvaluation(new ArrayList<Double>(){{ add(1.0); }});
        State b = new State(); b.setEvaluation(new ArrayList<Double>(){{ add(2.0); }});
        State[] arr = new State[]{a, b};

        ParticleSwarmOptimization.setLBest(arr);
        State[] got = ParticleSwarmOptimization.getLBest();
        assertEquals(2, got.length);
        // modify returned copy and ensure original internal array not affected
        got[0].getEvaluation().set(0, 99.0);
        State[] got2 = ParticleSwarmOptimization.getLBest();
        assertEquals(1.0, got2[0].getEvaluation().get(0), 0.0);

        // set null array should set internal lBest to null and getter returns empty
        ParticleSwarmOptimization.setLBest(null);
        State[] empty = ParticleSwarmOptimization.getLBest();
        assertEquals(0, empty.length);
    }

    @Test
    public void testSetLBestAt_boundsAndNoException(){
        State a = new State(); a.setEvaluation(new ArrayList<Double>(){{ add(1.0); }});
        State[] arr = new State[]{a};
        ParticleSwarmOptimization.setLBest(arr);
        // valid index
        State newS = new State(); newS.setEvaluation(new ArrayList<Double>(){{ add(5.0); }});
        ParticleSwarmOptimization.setLBestAt(0, newS);
        assertEquals(5.0, ParticleSwarmOptimization.getLBest()[0].getEvaluation().get(0), 0.0);

        // out-of-bounds: should not throw
        ParticleSwarmOptimization.setLBestAt(-1, newS);
        ParticleSwarmOptimization.setLBestAt(10, newS);
    }

    @Test
    public void testGBest_and_counts_setters_getters(){
        State s = new State(); s.setEvaluation(new ArrayList<Double>(){{ add(7.0); }});
        ParticleSwarmOptimization.setGBest(s);
        assertEquals(7.0, ParticleSwarmOptimization.getGBest().getEvaluation().get(0), 0.0);

        ParticleSwarmOptimization.setCountParticle(3);
        assertEquals(3, ParticleSwarmOptimization.getCountParticle());
        ParticleSwarmOptimization.setCountCurrentIterPSO(42);
        assertEquals(42, ParticleSwarmOptimization.getCountCurrentIterPSO());
    }

    @Test
    public void testConstructor_initializesEmptyParticleList_whenRandomSearchEmpty(){
        // ensure RandomSearch.listStateReference is empty
        RandomSearch.listStateReference.clear();
        ParticleSwarmOptimization pso = new ParticleSwarmOptimization();
        assertNotNull(pso.getListParticle());
        assertEquals(0, pso.getListParticle().size());
        // lBest length should be zero because coutSwarm is 0
        assertEquals(0, ParticleSwarmOptimization.getLBest().length);
    }

    @Test
    public void testSetAndGetListParticle_and_listStateReference_accessor(){
        ParticleSwarmOptimization pso = new ParticleSwarmOptimization();
        // create a simple Particle via constructor
        Particle particle = new Particle();
        State sb = new State(); sb.setEvaluation(new ArrayList<Double>(){{ add(3.0); }});
        State sa = new State(); sa.setEvaluation(new ArrayList<Double>(){{ add(4.0); }});
        particle.setStatePBest(sb);
        particle.setStateActual(sa);
        List<Particle> list = new ArrayList<>(); list.add(particle);
        pso.setListParticle(list);
        assertEquals(1, pso.getListParticle().size());
        // listStateReference getter proxies to getListParticle
        assertEquals(1, pso.getListStateReference().size());
    }
}
