package evolutionary_algorithms.complement;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.Test;

import problem.definition.State;
import problem.definition.Codification;
import metaheurictics.strategy.Strategy;
import local_search.complement.StrategyType;

public class OnePointCrossoverTest {

    private State makeState(Integer... vals) {
        State s = new State();
        ArrayList<Object> code = new ArrayList<>();
        for (Integer v: vals) code.add(v);
        s.setCode(code);
        return s;
    }

    private void setSimpleCodification(int varCount) {
        Strategy.getStrategy().setProblem(new problem.definition.Problem());
        Strategy.getStrategy().getProblem().setCodification(new Codification() {
            @Override public boolean validState(State state) { return true; }
            @Override public Object getVariableAleatoryValue(int key) { return 0; }
            @Override public int getAleatoryKey() { return 0; }
            @Override public int getVariableCount() { return varCount; }
        });
    }

    @Test public void t1_noCrossoverWhenPCZero() {
        State s1 = makeState(1,2,3);
        State s2 = makeState(4,5,6);
        OnePointCrossover c = new OnePointCrossover();
        State out = c.crossover(s1,s2,0.0);
        assertEquals(3, out.getCode().size());
    }

    @Test public void t2_crossoverWhenPCOne() {
        setSimpleCodification(3);
        State s1 = makeState(1,2,3);
        State s2 = makeState(4,5,6);
        OnePointCrossover c = new OnePointCrossover();
        State out = c.crossover(s1,s2,1.0);
        assertEquals(3, out.getCode().size());
        for (int i=0;i<3;i++) {
            Object v = out.getCode().get(i);
            assertTrue(v.equals(1) || v.equals(2) || v.equals(3) || v.equals(4) || v.equals(5) || v.equals(6));
        }
    }

    @Test public void t3_resultUsesParentsOnly() {
        setSimpleCodification(3);
        State s1 = makeState(10,11,12);
        State s2 = makeState(20,21,22);
        OnePointCrossover c = new OnePointCrossover();
        State out = c.crossover(s1,s2,1.0);
        for (int i=0;i<3;i++) {
            Object v = out.getCode().get(i);
            assertTrue(v.equals(10) || v.equals(11) || v.equals(12) || v.equals(20) || v.equals(21) || v.equals(22));
        }
    }

    @Test public void t4_handlesEmptyCode() {
        setSimpleCodification(0);
        State s1 = makeState();
        State s2 = makeState();
        OnePointCrossover c = new OnePointCrossover();
        State out = c.crossover(s1,s2,1.0);
        assertEquals(0, out.getCode().size());
    }

    @Test public void t5_multipleCallsProduceSameLength() {
        setSimpleCodification(4);
        State s1 = makeState(1,2,3,4);
        State s2 = makeState(5,6,7,8);
        OnePointCrossover c = new OnePointCrossover();
        for (int i=0;i<10;i++) {
            State out = c.crossover(s1,s2,1.0);
            assertEquals(4, out.getCode().size());
        }
    }

    @Test public void t6_positionsRemainWithinBounds() {
        setSimpleCodification(5);
        State s1 = makeState(1,2,3,4,5);
        State s2 = makeState(6,7,8,9,10);
        OnePointCrossover c = new OnePointCrossover();
        State out = c.crossover(s1,s2,1.0);
        assertEquals(5, out.getCode().size());
    }

    @Test public void t7_repeatedCallsYieldParentElementsOnly() {
        setSimpleCodification(3);
        State s1 = makeState(100,101,102);
        State s2 = makeState(200,201,202);
        OnePointCrossover c = new OnePointCrossover();
        for (int i=0;i<20;i++) {
            State out = c.crossover(s1,s2,1.0);
            for (Object v: out.getCode()) assertTrue(v.equals(100) || v.equals(101) || v.equals(102) || v.equals(200) || v.equals(201) || v.equals(202));
        }
    }

    @Test public void t8_withSingleElementCodes() {
        setSimpleCodification(1);
        State s1 = makeState(7);
        State s2 = makeState(8);
        OnePointCrossover c = new OnePointCrossover();
        State out = c.crossover(s1,s2,1.0);
        assertEquals(1, out.getCode().size());
        Object v = out.getCode().get(0);
        assertTrue(v.equals(7) || v.equals(8));
    }

    @Test public void t9_pcBetween0And1StillProducesLength() {
        setSimpleCodification(3);
        State s1 = makeState(1,1,1);
        State s2 = makeState(2,2,2);
        OnePointCrossover c = new OnePointCrossover();
        State out = c.crossover(s1,s2,0.5);
        assertEquals(3, out.getCode().size());
    }

    @Test public void t10_nonNullResult() { setSimpleCodification(2); OnePointCrossover c=new OnePointCrossover(); State s1=makeState(1,2); State s2=makeState(3,4); assertNotNull(c.crossover(s1,s2,1.0)); }

    @Test public void t11_parentCopiesNotModified() { setSimpleCodification(3); State s1=makeState(1,2,3); State s2=makeState(4,5,6); OnePointCrossover c=new OnePointCrossover(); State s1copy = new State(s1); c.crossover(s1,s2,1.0); assertEquals(s1.getCode(), s1copy.getCode()); }

    @Test public void t12_multipleSizes() { for(int n=0;n<5;n++){ setSimpleCodification(n); State s1=new State(); State s2=new State(); ArrayList<Object> a=new ArrayList<>(); ArrayList<Object> b=new ArrayList<>(); for(int i=0;i<n;i++){ a.add(i); b.add(i+100); } s1.setCode(a); s2.setCode(b); OnePointCrossover c=new OnePointCrossover(); assertEquals(n, c.crossover(s1,s2,1.0).getCode().size()); } }

    @Test public void t13_resultElementsFromEitherParent() { setSimpleCodification(4); State s1=makeState(1,2,3,4); State s2=makeState(5,6,7,8); OnePointCrossover c=new OnePointCrossover(); State out = c.crossover(s1,s2,1.0); for(int i=0;i<4;i++){ Object v=out.getCode().get(i); assertTrue(v.equals(s1.getCode().get(i)) || v.equals(s2.getCode().get(i))); } }

    @Test public void t14_manyIterationsStability() { setSimpleCodification(3); State s1=makeState(1,1,1); State s2=makeState(2,2,2); OnePointCrossover c=new OnePointCrossover(); for(int i=0;i<50;i++){ State out=c.crossover(s1,s2,1.0); assertEquals(3,out.getCode().size()); } }

    @Test public void t15_zeroVariableCount() { setSimpleCodification(0); State s1=makeState(); State s2=makeState(); OnePointCrossover c=new OnePointCrossover(); assertEquals(0,c.crossover(s1,s2,1.0).getCode().size()); }

    @Test public void t16_inputsUnchanged() { setSimpleCodification(3); State s1=makeState(9,9,9); State s2=makeState(8,8,8); OnePointCrossover c=new OnePointCrossover(); c.crossover(s1,s2,1.0); assertEquals(3, s1.getCode().size()); assertEquals(3, s2.getCode().size()); }

    @Test public void t17_resultContainsNoNullsWhenParentsNonNull() { setSimpleCodification(3); State s1=makeState(1,2,3); State s2=makeState(4,5,6); OnePointCrossover c=new OnePointCrossover(); State out=c.crossover(s1,s2,1.0); for(Object o: out.getCode()) assertNotNull(o); }

    @Test public void t18_repeatedCallsNotThrowing() { setSimpleCodification(3); State s1=makeState(1,2,3); State s2=makeState(4,5,6); OnePointCrossover c=new OnePointCrossover(); for(int i=0;i<100;i++) c.crossover(s1,s2,1.0); }

    @Test public void t19_variousPCs() { setSimpleCodification(3); State s1=makeState(1,2,3); State s2=makeState(4,5,6); OnePointCrossover c=new OnePointCrossover(); for(double pc: new double[]{0.0,0.25,0.5,0.75,1.0}) { State out=c.crossover(s1,s2,pc); assertEquals(3,out.getCode().size()); } }

    @Test public void t20_resultElementsBelongToParentSets() { setSimpleCodification(3); State s1=makeState(1,2,3); State s2=makeState(4,5,6); OnePointCrossover c=new OnePointCrossover(); State out=c.crossover(s1,s2,1.0); HashSet<Object> allowed = new HashSet<>(); allowed.addAll(s1.getCode()); allowed.addAll(s2.getCode()); for(Object o: out.getCode()) assertTrue(allowed.contains(o)); }
}
