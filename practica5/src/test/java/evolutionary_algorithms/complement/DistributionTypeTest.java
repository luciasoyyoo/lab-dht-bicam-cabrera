package evolutionary_algorithms.complement;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class DistributionTypeTest {

    @Test public void t1_valuesExist() { assertTrue(DistributionType.values().length>0); }
    @Test public void t2_valueOfName() { for(DistributionType d: DistributionType.values()) assertEquals(d, DistributionType.valueOf(d.name())); }
    @Test public void t3_namesNonEmpty() { for(DistributionType d: DistributionType.values()) assertFalse(d.name().isEmpty()); }
    @Test public void t4_toStringEqualsName() { for(DistributionType d: DistributionType.values()) assertEquals(d.name(), d.toString()); }
    @Test public void t5_hashConsistent() { DistributionType x = DistributionType.values()[0]; assertEquals(x.hashCode(), x.hashCode()); }
    @Test public void t6_classIsEnum() { assertTrue(DistributionType.class.isEnum()); }
    @Test public void t7_valueOfInvalidCase() { assertThrows(IllegalArgumentException.class, () -> DistributionType.valueOf("invalid")); }
    @Test public void t8_valueOfNull() { assertThrows(NullPointerException.class, () -> DistributionType.valueOf(null)); }
    @Test public void t9_ordinalsValid() { for(DistributionType d: DistributionType.values()) assertTrue(d.ordinal()>=0); }
    @Test public void t10_loop() { int c=0; for(DistributionType d: DistributionType.values()) c++; assertEquals(DistributionType.values().length,c); }
    @Test public void t11_namesUnique() { DistributionType[] a = DistributionType.values(); for(int i=0;i<a.length;i++) for(int j=i+1;j<a.length;j++) assertNotEquals(a[i].name(), a[j].name()); }
    @Test public void t12_valuesStable() { assertArrayEquals(DistributionType.values(), DistributionType.values()); }
    @Test public void t13_toStringNotNull() { for(DistributionType d: DistributionType.values()) assertNotNull(d.toString()); }
    @Test public void t14_compareTo() { DistributionType[] a = DistributionType.values(); assertEquals(0, a[0].compareTo(a[0])); }
    @Test public void t15_packageCorrect() { assertEquals("evolutionary_algorithms.complement", DistributionType.class.getPackage().getName()); }
    @Test public void t16_switchUse() {
        for(DistributionType d: DistributionType.values()) {
            switch(d) { default: break; }
            assertNotNull(d);
        }
    }
    @Test public void t17_nameNotBlank() { for(DistributionType d: DistributionType.values()) assertFalse(d.name().trim().isEmpty()); }
    @Test public void t18_valueOfRoundtrip() { for(DistributionType d: DistributionType.values()) assertEquals(d, DistributionType.valueOf(d.name())); }
    @Test public void t19_ordinalsDistinct() { DistributionType[] a = DistributionType.values(); for(int i=0;i<a.length;i++) for(int j=i+1;j<a.length;j++) assertNotEquals(a[i].ordinal(), a[j].ordinal()); }
    @Test public void t20_valuesArrayRepeatable() { DistributionType[] a = DistributionType.values(); DistributionType[] b = DistributionType.values(); assertArrayEquals(a,b); }
}
