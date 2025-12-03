package evolutionary_algorithms.complement;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class MutationTypeTest {

    @Test public void t1_valuesExist() { assertTrue(MutationType.values().length>0); }
    @Test public void t2_roundtrip() { for(MutationType m: MutationType.values()) assertEquals(m, MutationType.valueOf(m.name())); }
    @Test public void t3_namesNotEmpty() { for(MutationType m: MutationType.values()) assertFalse(m.name().isEmpty()); }
    @Test public void t4_toString() { for(MutationType m: MutationType.values()) assertEquals(m.name(), m.toString()); }
    @Test public void t5_hashConsistent() { MutationType x = MutationType.values()[0]; assertEquals(x.hashCode(), x.hashCode()); }
    @Test public void t6_isEnum() { assertTrue(MutationType.class.isEnum()); }
    @Test public void t7_invalidName() { assertThrows(IllegalArgumentException.class, () -> MutationType.valueOf("X")); }
    @Test public void t8_nullName() { assertThrows(NullPointerException.class, () -> MutationType.valueOf(null)); }
    @Test public void t9_ordinals() { for(MutationType m: MutationType.values()) assertTrue(m.ordinal()>=0); }
    @Test public void t10_loop() { int c=0; for(MutationType m: MutationType.values()) c++; assertEquals(MutationType.values().length, c); }
    @Test public void t11_uniqueNames() { MutationType[] a = MutationType.values(); for(int i=0;i<a.length;i++) for(int j=i+1;j<a.length;j++) assertNotEquals(a[i].name(), a[j].name()); }
    @Test public void t12_valuesStable() { assertArrayEquals(MutationType.values(), MutationType.values()); }
    @Test public void t13_toStringNotNull() { for(MutationType m: MutationType.values()) assertNotNull(m.toString()); }
    @Test public void t14_compareTo() { MutationType[] a = MutationType.values(); assertEquals(0, a[0].compareTo(a[0])); }
    @Test public void t15_package() { assertEquals("evolutionary_algorithms.complement", MutationType.class.getPackage().getName()); }
    @Test public void t16_switchUse() { for(MutationType m: MutationType.values()) { switch(m) { default: break; } } }
    @Test public void t17_nameNotBlank() { for(MutationType m: MutationType.values()) assertFalse(m.name().trim().isEmpty()); }
    @Test public void t18_valueOfValid() { for(MutationType m: MutationType.values()) assertEquals(m, MutationType.valueOf(m.name())); }
    @Test public void t19_ordinalsDistinct() { MutationType[] a = MutationType.values(); for(int i=0;i<a.length;i++) for(int j=i+1;j<a.length;j++) assertNotEquals(a[i].ordinal(), a[j].ordinal()); }
    @Test public void t20_repeatableValues() { MutationType[] a = MutationType.values(); MutationType[] b = MutationType.values(); assertArrayEquals(a,b); }
}
