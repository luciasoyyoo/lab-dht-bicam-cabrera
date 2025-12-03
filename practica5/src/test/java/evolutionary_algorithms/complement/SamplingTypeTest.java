package evolutionary_algorithms.complement;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class SamplingTypeTest {

    @Test public void t1_valuesExist() { assertTrue(SamplingType.values().length>0); }
    @Test public void t2_valueOfRoundtrip() { for(SamplingType s: SamplingType.values()) assertEquals(s, SamplingType.valueOf(s.name())); }
    @Test public void t3_namesNotEmpty() { for(SamplingType s: SamplingType.values()) assertFalse(s.name().isEmpty()); }
    @Test public void t4_toString() { for(SamplingType s: SamplingType.values()) assertEquals(s.name(), s.toString()); }
    @Test public void t5_hashConsistent() { SamplingType x = SamplingType.values()[0]; assertEquals(x.hashCode(), x.hashCode()); }
    @Test public void t6_isEnum() { assertTrue(SamplingType.class.isEnum()); }
    @Test public void t7_invalidValue() { assertThrows(IllegalArgumentException.class, () -> SamplingType.valueOf("nope")); }
    @Test public void t8_nullValue() { assertThrows(NullPointerException.class, () -> SamplingType.valueOf(null)); }
    @Test public void t9_ordinals() { for(SamplingType s: SamplingType.values()) assertTrue(s.ordinal()>=0); }
    @Test public void t10_loop() { int c=0; for(SamplingType s: SamplingType.values()) c++; assertEquals(SamplingType.values().length, c); }
    @Test public void t11_uniqueNames() { SamplingType[] a = SamplingType.values(); for(int i=0;i<a.length;i++) for(int j=i+1;j<a.length;j++) assertNotEquals(a[i].name(), a[j].name()); }
    @Test public void t12_valuesStable() { assertArrayEquals(SamplingType.values(), SamplingType.values()); }
    @Test public void t13_toStringNotNull() { for(SamplingType s: SamplingType.values()) assertNotNull(s.toString()); }
    @Test public void t14_compareTo() { SamplingType[] a = SamplingType.values(); assertEquals(0, a[0].compareTo(a[0])); }
    @Test public void t15_package() { assertEquals("evolutionary_algorithms.complement", SamplingType.class.getPackage().getName()); }
    @Test public void t16_switchUsage() { for(SamplingType s: SamplingType.values()) { switch(s) { default: break; } } }
    @Test public void t17_nameNotBlank() { for(SamplingType s: SamplingType.values()) assertFalse(s.name().trim().isEmpty()); }
    @Test public void t18_valueOfValid() { for(SamplingType s: SamplingType.values()) assertEquals(s, SamplingType.valueOf(s.name())); }
    @Test public void t19_ordinalsDistinct() { SamplingType[] a = SamplingType.values(); for(int i=0;i<a.length;i++) for(int j=i+1;j<a.length;j++) assertNotEquals(a[i].ordinal(), a[j].ordinal()); }
    @Test public void t20_repeatValuesArray() { SamplingType[] a = SamplingType.values(); SamplingType[] b = SamplingType.values(); assertArrayEquals(a,b); }
}
