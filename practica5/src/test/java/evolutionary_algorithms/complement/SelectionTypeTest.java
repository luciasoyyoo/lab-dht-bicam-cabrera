package evolutionary_algorithms.complement;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class SelectionTypeTest {

    @Test public void t1_valuesExist() { assertTrue(SelectionType.values().length>0); }
    @Test public void t2_roundtrip() { for(SelectionType s: SelectionType.values()) assertEquals(s, SelectionType.valueOf(s.name())); }
    @Test public void t3_namesNonEmpty() { for(SelectionType s: SelectionType.values()) assertFalse(s.name().isEmpty()); }
    @Test public void t4_toString() { for(SelectionType s: SelectionType.values()) assertEquals(s.name(), s.toString()); }
    @Test public void t5_hashConsistent() { SelectionType x = SelectionType.values()[0]; assertEquals(x.hashCode(), x.hashCode()); }
    @Test public void t6_isEnum() { assertTrue(SelectionType.class.isEnum()); }
    @Test public void t7_invalidName() { assertThrows(IllegalArgumentException.class, () -> SelectionType.valueOf("x")); }
    @Test public void t8_nullName() { assertThrows(NullPointerException.class, () -> SelectionType.valueOf(null)); }
    @Test public void t9_ordinals() { for(SelectionType s: SelectionType.values()) assertTrue(s.ordinal()>=0); }
    @Test public void t10_loopCount() { int c=0; for(SelectionType s: SelectionType.values()) c++; assertEquals(SelectionType.values().length, c); }
    @Test public void t11_uniqueNames() { SelectionType[] a = SelectionType.values(); for(int i=0;i<a.length;i++) for(int j=i+1;j<a.length;j++) assertNotEquals(a[i].name(), a[j].name()); }
    @Test public void t12_valuesStable() { assertArrayEquals(SelectionType.values(), SelectionType.values()); }
    @Test public void t13_toStringNotNull() { for(SelectionType s: SelectionType.values()) assertNotNull(s.toString()); }
    @Test public void t14_compareTo() { SelectionType[] a = SelectionType.values(); assertEquals(0, a[0].compareTo(a[0])); }
    @Test public void t15_packageName() { assertEquals("evolutionary_algorithms.complement", SelectionType.class.getPackage().getName()); }
    @Test public void t16_switchWorks() { for(SelectionType s: SelectionType.values()) { switch(s) { default: break; } } }
    @Test public void t17_nameNotBlank() { for(SelectionType s: SelectionType.values()) assertFalse(s.name().trim().isEmpty()); }
    @Test public void t18_valueOfValid() { for(SelectionType s: SelectionType.values()) assertEquals(s, SelectionType.valueOf(s.name())); }
    @Test public void t19_ordinalsDistinct() { SelectionType[] a = SelectionType.values(); for(int i=0;i<a.length;i++) for(int j=i+1;j<a.length;j++) assertNotEquals(a[i].ordinal(), a[j].ordinal()); }
    @Test public void t20_repeatableValues() { SelectionType[] a = SelectionType.values(); SelectionType[] b = SelectionType.values(); assertArrayEquals(a,b); }
}
