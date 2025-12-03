package evolutionary_algorithms.complement;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class CrossoverTypeTest {

    @Test public void t1_valueOfOnePoint() { assertEquals(CrossoverType.OnePointCrossover, CrossoverType.valueOf("OnePointCrossover")); }
    @Test public void t2_valueOfUniform() { assertEquals(CrossoverType.UniformCrossover, CrossoverType.valueOf("UniformCrossover")); }
    @Test public void t3_namesNotEmpty() { for (CrossoverType c : CrossoverType.values()) assertFalse(c.name().isEmpty()); }
    @Test public void t4_ordinalsDistinct() { CrossoverType[] vs = CrossoverType.values(); assertNotEquals(vs[0].ordinal(), vs[1].ordinal()); }
    @Test public void t5_valuesLength() { assertEquals(2, CrossoverType.values().length); }
    @Test public void t6_toStringMatchesName() { for (CrossoverType c: CrossoverType.values()) assertEquals(c.name(), c.toString()); }
    @Test public void t7_compareToOrder() { assertTrue(CrossoverType.OnePointCrossover.compareTo(CrossoverType.UniformCrossover) < 0 || CrossoverType.OnePointCrossover.compareTo(CrossoverType.UniformCrossover) > 0); }
    @Test public void t8_classIsEnum() { assertTrue(CrossoverType.class.isEnum()); }
    @Test public void t9_valueEquality() { CrossoverType a = CrossoverType.OnePointCrossover; CrossoverType b = CrossoverType.OnePointCrossover; assertSame(a,b); }
    @Test public void t10_valueInequality() { assertNotSame(CrossoverType.OnePointCrossover, CrossoverType.UniformCrossover); }
    @Test public void t11_loopValues() { int count=0; for(CrossoverType c: CrossoverType.values()) count++; assertEquals(2,count); }
    @Test public void t12_nameCaseSensitive() { assertThrows(IllegalArgumentException.class, () -> CrossoverType.valueOf("onepointcrossover")); }
    @Test public void t13_valueOfNullThrows() { assertThrows(NullPointerException.class, () -> CrossoverType.valueOf(null)); }
    @Test public void t14_hashCodeStable() { assertEquals(CrossoverType.OnePointCrossover.hashCode(), CrossoverType.OnePointCrossover.hashCode()); }
    @Test public void t15_valuesContainExpected() { boolean found=false; for(CrossoverType c: CrossoverType.values()) if(c==CrossoverType.UniformCrossover) found=true; assertTrue(found); }
    @Test public void t16_enumOrdinalBounds() { for(CrossoverType c: CrossoverType.values()) assertTrue(c.ordinal()>=0 && c.ordinal()<CrossoverType.values().length); }
    @Test public void t17_switchBehaviour() { switch(CrossoverType.OnePointCrossover) { case OnePointCrossover: break; default: fail(); } }
    @Test public void t18_declaredInPackage() { assertEquals("evolutionary_algorithms.complement", CrossoverType.class.getPackage().getName()); }
    @Test public void t19_valueNamesUnique() { String a = CrossoverType.OnePointCrossover.name(); String b = CrossoverType.UniformCrossover.name(); assertNotEquals(a,b); }
    @Test public void t20_valuesStableOnRepeatedCall() { CrossoverType[] a = CrossoverType.values(); CrossoverType[] b = CrossoverType.values(); assertArrayEquals(a,b); }
}
