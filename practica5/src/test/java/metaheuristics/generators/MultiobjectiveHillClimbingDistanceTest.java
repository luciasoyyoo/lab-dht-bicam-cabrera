package metaheuristics.generators;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import metaheurictics.strategy.Strategy;
import problem.definition.Operator;
import problem.definition.Problem;
import problem.definition.State;

public class MultiobjectiveHillClimbingDistanceTest {

    private Strategy strategy;
    private List<Double> backupDistance;
    private Problem backupProblem;

    @BeforeEach
    public void setUp() {
        strategy = Strategy.getStrategy();
        backupDistance = new ArrayList<Double>(MultiobjectiveHillClimbingDistance.distanceSolution);
        MultiobjectiveHillClimbingDistance.distanceSolution.clear();
        backupProblem = strategy.getProblem();
        Problem p = new Problem();
        p.setTypeProblem(Problem.ProblemType.Maximizar);
        p.setOperator(new Operator() {
            @Override
            public List<State> generatedNewState(State stateCurrent, Integer operatornumber) {
                return new ArrayList<State>();
            }

            @Override
            public List<State> generateRandomState(Integer operatornumber) {
                List<State> r = new ArrayList<State>();
                State s = new State(); ArrayList<Object> cs = new ArrayList<Object>(); cs.add("rand"); s.setCode(cs); r.add(s);
                return r;
            }
        });
        strategy.setProblem(p);
        strategy.generator = new MultiobjectiveHillClimbingDistance();
        strategy.listRefPoblacFinal = new ArrayList<State>();
    }

    @AfterEach
    public void tearDown() {
        MultiobjectiveHillClimbingDistance.distanceSolution.clear();
        MultiobjectiveHillClimbingDistance.distanceSolution.addAll(backupDistance);
        strategy.setProblem(backupProblem);
    }

    @Test
    public void testGenerate_returnsNeighbor() throws Exception {
        Problem p = strategy.getProblem();
        final State neigh = new State(); ArrayList<Object> nc = new ArrayList<Object>(); nc.add("n"); neigh.setCode(nc);
        ArrayList<Double> neval = new ArrayList<Double>(); neval.add(1.0); neigh.setEvaluation(neval);
        p.setOperator(new Operator() {
            @Override
            public List<State> generatedNewState(State stateCurrent, Integer operatornumber) {
                return Arrays.asList(neigh);
            }

            @Override
            public List<State> generateRandomState(Integer operatornumber) { return new ArrayList<State>(); }
        });

        MultiobjectiveHillClimbingDistance g = new MultiobjectiveHillClimbingDistance();
        State ref = new State(); ArrayList<Object> rc = new ArrayList<Object>(); rc.add("r"); ref.setCode(rc); g.setStateRef(ref);
        State res = g.generate(1);
        assertNotNull(res);
        assertEquals(neigh.getCode(), res.getCode());
    }

    @Test
    public void testDistanceCalculateAdd_twoSolutions_updatesDistances() {
        State s1 = new State(); ArrayList<Object> c1 = new ArrayList<Object>(); c1.add(0); s1.setCode(c1);
        State s2 = new State(); ArrayList<Object> c2 = new ArrayList<Object>(); c2.add(1); s2.setCode(c2);
        // initialize distanceSolution as if first element existed
        MultiobjectiveHillClimbingDistance.distanceSolution.clear();
        MultiobjectiveHillClimbingDistance.distanceSolution.add(Double.valueOf(0.0));
        List<State> solutions = new ArrayList<State>(); solutions.add(s1); solutions.add(s2);
        List<Double> out = MultiobjectiveHillClimbingDistance.distanceCalculateAdd(solutions);
        // distances should be [1.0, 1.0]
        assertEquals(2, out.size());
        assertEquals(1.0, out.get(0));
        assertEquals(1.0, out.get(1));
    }

    @Test
    public void testUpdateReference_acceptsCandidateWhenDominates() throws Exception {
        MultiobjectiveHillClimbingDistance g = new MultiobjectiveHillClimbingDistance();
        State initial = new State(); ArrayList<Object> ic = new ArrayList<Object>(); ic.add("x"); initial.setCode(ic);
        ArrayList<Double> ie = new ArrayList<Double>(); ie.add(1.0); initial.setEvaluation(ie);
        g.setStateRef(initial);

        State cand = new State(); ArrayList<Object> cc = new ArrayList<Object>(); cc.add("y"); cand.setCode(cc);
        ArrayList<Double> ce = new ArrayList<Double>(); ce.add(2.0); cand.setEvaluation(ce);

        // Ensure listRefPoblacFinal empty so updateReference will add initial and distance 0.0
        strategy.listRefPoblacFinal.clear();
        MultiobjectiveHillClimbingDistance.distanceSolution.clear();

        g.updateReference(cand, 0);

        assertEquals(cand.getCode(), g.getReference().getCode());
        assertFalse(strategy.listRefPoblacFinal.isEmpty());
        assertFalse(MultiobjectiveHillClimbingDistance.distanceSolution.isEmpty());
    }

    @Test
    public void testUpdateReference_rejectPath_callsSolutionMoreDistance() throws Exception {
        MultiobjectiveHillClimbingDistance g = new MultiobjectiveHillClimbingDistance();
        State initial = new State(); ArrayList<Object> ic = new ArrayList<Object>(); ic.add("x"); initial.setCode(ic);
        ArrayList<Double> ie = new ArrayList<Double>(); ie.add(1.0); initial.setEvaluation(ie);
        g.setStateRef(initial);

        State cand = new State(); ArrayList<Object> cc = new ArrayList<Object>(); cc.add("y"); cand.setCode(cc);
        ArrayList<Double> ce = new ArrayList<Double>(); ce.add(0.5); cand.setEvaluation(ce);

        // prepare listRefPoblacFinal and distanceSolution so solutionMoreDistance can return a value
        strategy.listRefPoblacFinal.clear();
        strategy.listRefPoblacFinal.add(initial.clone());
        MultiobjectiveHillClimbingDistance.distanceSolution.clear();
        MultiobjectiveHillClimbingDistance.distanceSolution.add(Double.valueOf(0.0));

        // Prepare neighborhood with one not-contained element
        State neigh = new State(); ArrayList<Object> nc = new ArrayList<Object>(); nc.add("n"); neigh.setCode(nc);
        ArrayList<Double> ne = new ArrayList<Double>(); ne.add(0.6); neigh.setEvaluation(ne);
        Problem p = strategy.getProblem();
        p.setOperator(new Operator() {
            @Override
            public List<State> generatedNewState(State stateCurrent, Integer operatornumber) {
                List<State> l = new ArrayList<State>(); l.add(neigh); return l;
            }

            @Override
            public List<State> generateRandomState(Integer operatornumber) { return new ArrayList<State>(); }
        });

        g.updateReference(cand, 0);

        // After reject path, reference should be set by solutionMoreDistance (likely initial)
        assertNotNull(g.getReference());
    }

}

