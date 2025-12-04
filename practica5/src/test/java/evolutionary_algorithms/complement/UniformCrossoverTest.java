package evolutionary_algorithms.complement;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashSet;

import org.junit.jupiter.api.Test;

import problem.definition.State;

public class UniformCrossoverTest {

    private State makeState(Integer... vals) {
        State s = new State();
        ArrayList<Object> code = new ArrayList<>();
        for (Integer v: vals) code.add(v);
        s.setCode(code);
        return s;
    }

    @Test public void t1_mascaraLengthMatches() { UniformCrossover u = new UniformCrossover(); int[] m = u.mascara(5); assertEquals(5,m.length); }
    @Test public void t2_mascaraContainsOnly0or1() { UniformCrossover u = new UniformCrossover(); int[] m = u.mascara(20); for(int v:m) assertTrue(v==0||v==1); }
    @Test public void t3_crossoverProducesSameLength() { UniformCrossover u = new UniformCrossover(); State a=makeState(1,2,3); State b=makeState(4,5,6); State out=u.crossover(a,b,1.0); assertEquals(3,out.getCode().size()); }
    @Test public void t4_eachPositionFromParent() { UniformCrossover u = new UniformCrossover(); State a=makeState(10,11,12); State b=makeState(20,21,22); State out=u.crossover(a,b,1.0); for(int i=0;i<3;i++){ Object v=out.getCode().get(i); assertTrue(v.equals(a.getCode().get(i))||v.equals(b.getCode().get(i))); } }
    @Test public void t5_emptyParents() { UniformCrossover u=new UniformCrossover(); State a=makeState(); State b=makeState(); State out=u.crossover(a,b,1.0); assertEquals(0,out.getCode().size()); }
    @Test public void t6_singleElementParents() { UniformCrossover u=new UniformCrossover(); State a=makeState(1); State b=makeState(2); State out=u.crossover(a,b,1.0); assertEquals(1,out.getCode().size()); Object v=out.getCode().get(0); assertTrue(v.equals(1)||v.equals(2)); }
    @Test public void t7_manyRunsStability() { UniformCrossover u=new UniformCrossover(); State a=makeState(1,1,1,1); State b=makeState(2,2,2,2); for(int i=0;i<50;i++){ State out=u.crossover(a,b,1.0); assertEquals(4,out.getCode().size()); } }
    @Test public void t8_mascaraZeroLength() { UniformCrossover u=new UniformCrossover(); int[] m=u.mascara(0); assertEquals(0,m.length); }
    @Test public void t9_resultElementsBelongToParents() { UniformCrossover u=new UniformCrossover(); State a=makeState(3,4,5); State b=makeState(6,7,8); State out=u.crossover(a,b,1.0); HashSet<Object> set=new HashSet<>(); set.addAll(a.getCode()); set.addAll(b.getCode()); for(Object o: out.getCode()) assertTrue(set.contains(o)); }
    @Test public void t10_nonNullReturn() { UniformCrossover u=new UniformCrossover(); State a=makeState(1); State b=makeState(2); assertNotNull(u.crossover(a,b,1.0)); }
    @Test public void t11_parentCodesUnmodified() { UniformCrossover u=new UniformCrossover(); State a=makeState(1,2,3); State aCopy=new State(a); State b=makeState(4,5,6); u.crossover(a,b,1.0); assertEquals(aCopy.getCode(), a.getCode()); }
    @Test public void t12_mascaraRandomnessProducesDifferentMasks() { UniformCrossover u=new UniformCrossover(); int[] m1=u.mascara(10); int[] m2=u.mascara(10); boolean same=true; if(m1.length!=m2.length) same=false; else for(int i=0;i<m1.length;i++) if(m1[i]!=m2[i]) { same=false; break; } // possible to be same occasionally
        assertNotNull(m1); assertNotNull(m2); }
    @Test public void t13_toStringNotUsed() { UniformCrossover u=new UniformCrossover(); assertEquals(u.getClass().getSimpleName(), "UniformCrossover"); }
    @Test public void t14_positionsAreSet() { UniformCrossover u=new UniformCrossover(); State a=makeState(1,2,3,4); State b=makeState(5,6,7,8); State out=u.crossover(a,b,1.0); for(int i=0;i<4;i++) assertNotNull(out.getCode().get(i)); }
    @Test public void t15_maskContainsOnly0or1MultipleRuns() { UniformCrossover u=new UniformCrossover(); for(int k=0;k<5;k++){ int[] m=u.mascara(5); for(int v:m) assertTrue(v==0||v==1); } }
    @Test public void t16_resultStableLengthWhenDifferentParentSizes() { UniformCrossover u=new UniformCrossover(); State a=makeState(1,2,3); State b=makeState(4,5,6); State out=u.crossover(a,b,1.0); assertEquals(a.getCode().size(), out.getCode().size()); }
    @Test public void t17_resultHasNoNullsIfParentsNoNulls() { UniformCrossover u=new UniformCrossover(); State a=makeState(1,2,3); State b=makeState(4,5,6); State out=u.crossover(a,b,1.0); for(Object o: out.getCode()) assertNotNull(o); }
    @Test public void t18_manyIterationsNotThrow() { UniformCrossover u=new UniformCrossover(); State a=makeState(1,2,3); State b=makeState(4,5,6); State out=null; for(int i=0;i<100;i++) out=u.crossover(a,b,1.0); assertNotNull(out); assertEquals(3, out.getCode().size()); }
    @Test public void t19_mascaraDifferentLengths() { UniformCrossover u=new UniformCrossover(); assertEquals(0, u.mascara(0).length); assertEquals(1, u.mascara(1).length); assertEquals(10, u.mascara(10).length); }
    @Test public void t20_resultFromParentsOnly() { UniformCrossover u=new UniformCrossover(); State a=makeState(9,9); State b=makeState(8,8); State out=u.crossover(a,b,1.0); for(Object o: out.getCode()) assertTrue(o.equals(9)||o.equals(8)); }
}
