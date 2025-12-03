package evolutionary_algorithms.complement;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import problem.definition.State;

public class GenerationalReplaceTest {

    private State mk(double eval) { State s=new State(); ArrayList<Double> ev=new ArrayList<>(); ev.add(eval); s.setEvaluation(ev); return s; }

    @Test public void t1_removeFirstAndAdd() throws Exception { GenerationalReplace g=new GenerationalReplace(); List<State> list=new ArrayList<>(); State a=mk(1); State b=mk(2); list.add(a); list.add(b); State c=mk(3); List<State> out=g.replace(c,list); assertEquals(2,out.size()); assertSame(c,out.get(out.size()-1)); }
    @Test public void t2_sizeAfterReplace() throws Exception { GenerationalReplace g=new GenerationalReplace(); List<State> list=new ArrayList<>(); list.add(mk(1)); list.add(mk(2)); list.add(mk(3)); State c=mk(9); assertEquals(3,g.replace(c,list).size()); }
    @Test public void t3_repeatedReplaceKeepsOrder() throws Exception { GenerationalReplace g=new GenerationalReplace(); List<State> list=new ArrayList<>(); list.add(mk(1)); list.add(mk(2)); State c1=mk(10); g.replace(c1,list); State c2=mk(11); g.replace(c2,list); assertEquals(c2, list.get(list.size()-1)); }
    @Test public void t4_replaceOnSingleElement() throws Exception { GenerationalReplace g=new GenerationalReplace(); List<State> list=new ArrayList<>(); list.add(mk(1)); State c=mk(2); List<State> out=g.replace(c,list); assertEquals(1,out.size()); assertSame(c,out.get(0)); }
    @Test public void t5_replaceMaintainsNonEmpty() throws Exception { GenerationalReplace g=new GenerationalReplace(); List<State> list=new ArrayList<>(); list.add(mk(5)); list.add(mk(6)); State c=mk(7); List<State> out=g.replace(c,list); assertFalse(out.isEmpty()); }
    @Test public void t6_replaceDoesNotDuplicateSize() throws Exception { GenerationalReplace g=new GenerationalReplace(); List<State> list=new ArrayList<>(); list.add(mk(1)); list.add(mk(2)); State c=mk(3); g.replace(c,list); assertEquals(2,list.size()); }
    @Test public void t7_replaceWorksWithMany() throws Exception { GenerationalReplace g=new GenerationalReplace(); List<State> list=new ArrayList<>(); for(int i=0;i<5;i++) list.add(mk(i)); State c=mk(99); g.replace(c,list); assertEquals(5,list.size()); assertSame(c,list.get(list.size()-1)); }
    @Test public void t8_firstElementRemoved() throws Exception { GenerationalReplace g=new GenerationalReplace(); List<State> list=new ArrayList<>(); State a=mk(1); State b=mk(2); list.add(a); list.add(b); State c=mk(3); g.replace(c,list); assertFalse(list.contains(a)); }
    @Test public void t9_replaceIdempotent() throws Exception { GenerationalReplace g=new GenerationalReplace(); List<State> list=new ArrayList<>(); list.add(mk(1)); list.add(mk(2)); State c=mk(5); g.replace(c,list); g.replace(c,list); assertSame(c,list.get(list.size()-1)); }
    @Test public void t10_multipleSequential() throws Exception { GenerationalReplace g=new GenerationalReplace(); List<State> list=new ArrayList<>(); list.add(mk(1)); list.add(mk(2)); for(int i=0;i<10;i++){ g.replace(mk(i+100), list); } assertEquals(2,list.size()); }
    @Test public void t11_replaceKeepsOrderOfTail() throws Exception { GenerationalReplace g=new GenerationalReplace(); List<State> list=new ArrayList<>(); State a=mk(1); State b=mk(2); list.add(a); list.add(b); State c=mk(3); g.replace(c,list); assertEquals(c, list.get(1)); }
    @Test public void t12_replaceWithNullCandidate() throws Exception { GenerationalReplace g=new GenerationalReplace(); List<State> list=new ArrayList<>(); list.add(mk(1)); list.add(mk(2)); State c=null; g.replace(c,list); assertNull(list.get(list.size()-1)); }
    @Test public void t13_replaceDoesNotThrowOnEmptyList() throws Exception { GenerationalReplace g=new GenerationalReplace(); List<State> list=new ArrayList<>(); State c=mk(4); // remove(0) would throw; but method assumes list non-empty; we assert it throws
        assertThrows(IndexOutOfBoundsException.class, () -> g.replace(c,list)); }
    @Test public void t14_replaceWithLargeList() throws Exception { GenerationalReplace g=new GenerationalReplace(); List<State> list=new ArrayList<>(); for(int i=0;i<100;i++) list.add(mk(i)); State c=mk(999); g.replace(c,list); assertSame(c, list.get(list.size()-1)); }
    @Test public void t15_replaceFirstElementChanged() throws Exception { GenerationalReplace g=new GenerationalReplace(); List<State> list=new ArrayList<>(); State a=mk(1); State b=mk(2); list.add(a); list.add(b); g.replace(mk(3), list); assertNotSame(a, list.get(0)); }
    @Test public void t16_replaceWithSameCandidate() throws Exception { GenerationalReplace g=new GenerationalReplace(); List<State> list=new ArrayList<>(); State a=mk(1); State b=mk(2); list.add(a); list.add(b); State c=b; g.replace(c,list); assertSame(b, list.get(list.size()-1)); }
    @Test public void t17_replaceMaintainsOrderOfRemaining() throws Exception { GenerationalReplace g=new GenerationalReplace(); List<State> list=new ArrayList<>(); State a=mk(1); State b=mk(2); State c=mk(3); list.add(a); list.add(b); list.add(c); State d=mk(4); g.replace(d,list); assertEquals(b, list.get(0)); }
    @Test public void t18_replaceReturnsSameListInstance() throws Exception { GenerationalReplace g=new GenerationalReplace(); List<State> list=new ArrayList<>(); list.add(mk(1)); State c=mk(2); List<State> out = g.replace(c,list); assertSame(list,out); }
    @Test public void t19_replaceMultipleTypes() throws Exception { GenerationalReplace g=new GenerationalReplace(); List<State> list=new ArrayList<>(); list.add(mk(1)); list.add(mk(2)); g.replace(mk(10),list); g.replace(mk(11),list); assertEquals(2,list.size()); }
    @Test public void t20_replaceEdgeValues() throws Exception { GenerationalReplace g=new GenerationalReplace(); List<State> list=new ArrayList<>(); list.add(mk(Integer.MIN_VALUE)); list.add(mk(Integer.MAX_VALUE)); State c=mk(0); g.replace(c,list); assertEquals(2,list.size()); }
}
