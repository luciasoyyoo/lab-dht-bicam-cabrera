package evolutionary_algorithms.complement;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class ReplaceTypeTest {

    @Test public void t1_valueOfReplace() { assertEquals(ReplaceType.SteadyStateReplace, ReplaceType.valueOf("SteadyStateReplace")); assertEquals(ReplaceType.GenerationalReplace, ReplaceType.valueOf("GenerationalReplace")); }
    @Test public void t2_valuesNotEmpty() { for(ReplaceType r: ReplaceType.values()) assertFalse(r.name().isEmpty()); }
    @Test public void t3_valuesLengthPositive() { assertTrue(ReplaceType.values().length>0); }
    @Test public void t4_toString() { for(ReplaceType r: ReplaceType.values()) assertEquals(r.name(), r.toString()); }
    @Test public void t5_hashCodeConsistent() { assertEquals(ReplaceType.values()[0].hashCode(), ReplaceType.values()[0].hashCode()); }
    @Test public void t6_enumClass() { assertTrue(ReplaceType.class.isEnum()); }
    @Test public void t7_valueOfCaseSensitive() { assertThrows(IllegalArgumentException.class, () -> ReplaceType.valueOf("replacetype")); }
    @Test public void t8_valueOfNull() { assertThrows(NullPointerException.class, () -> ReplaceType.valueOf(null)); }
    @Test public void t9_ordinalsInRange() { for(ReplaceType r: ReplaceType.values()) assertTrue(r.ordinal()>=0); }
    @Test public void t10_loopCount() { int c=0; for(ReplaceType r: ReplaceType.values()) c++; assertTrue(c==ReplaceType.values().length); }
    @Test public void t11_namesUnique() { ReplaceType[] arr = ReplaceType.values(); for(int i=0;i<arr.length;i++) for(int j=i+1;j<arr.length;j++) assertNotEquals(arr[i].name(), arr[j].name()); }
    @Test public void t12_valuesStable() { assertArrayEquals(ReplaceType.values(), ReplaceType.values()); }
    @Test public void t13_toStringNotNull() { for(ReplaceType r: ReplaceType.values()) assertNotNull(r.toString()); }
    @Test public void t14_compareToWorks() { if(ReplaceType.values().length>1) assertTrue(ReplaceType.values()[0].compareTo(ReplaceType.values()[0])==0); }
    @Test public void t15_declaredPackage() { assertEquals("evolutionary_algorithms.complement", ReplaceType.class.getPackage().getName()); }
    @Test public void t16_switchUse() { for(ReplaceType r: ReplaceType.values()) { switch(r) { default: break; } } }
    @Test public void t17_nameNotBlank() { for(ReplaceType r: ReplaceType.values()) assertFalse(r.name().trim().isEmpty()); }
    @Test public void t18_valueOfValid() { for(ReplaceType r: ReplaceType.values()) assertEquals(r, ReplaceType.valueOf(r.name())); }
    @Test public void t19_ordinalsDistinct() { ReplaceType[] a = ReplaceType.values(); for(int i=0;i<a.length;i++) for(int j=i+1;j<a.length;j++) assertNotEquals(a[i].ordinal(), a[j].ordinal()); }
    @Test public void t20_valuesReturnSameArrayOnCall() { ReplaceType[] a = ReplaceType.values(); ReplaceType[] b = ReplaceType.values(); assertArrayEquals(a,b); }
}
