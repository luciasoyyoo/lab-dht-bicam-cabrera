package evolutionary_algorithms.complement;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.junit.jupiter.api.Test;

import problem.definition.State;

public class UnivariateTest {

    @Test
    public void distribution_computesProbabilities_perVariable() {
        // fathers: three states with two-variable codes
        State s1 = new State(); s1.setCode(new ArrayList<Object>(){{ add(1); add(2); }});
        State s2 = new State(); s2.setCode(new ArrayList<Object>(){{ add(1); add(3); }});
        State s3 = new State(); s3.setCode(new ArrayList<Object>(){{ add(2); add(2); }});
        List<State> fathers = new ArrayList<>(); fathers.add(s1); fathers.add(s2); fathers.add(s3);

        Univariate u = new Univariate();
        List<Probability> probs = u.distribution(fathers);

        // Expect 4 Probability entries: two for variable 0 (values 1 and 2), two for variable 1 (values 2 and 3)
        assertEquals(4, probs.size());

        // find entries for variable 0
        Probability p00 = probs.get(0);
        assertEquals(0, ((Integer)p00.getKey()).intValue());
        assertEquals(1, ((Integer)p00.getValue()).intValue());
        assertEquals((float)2/3, p00.getProbability(), 1e-6f);

        Probability p01 = probs.get(1);
        assertEquals(0, ((Integer)p01.getKey()).intValue());
        assertEquals(2, ((Integer)p01.getValue()).intValue());
        assertEquals((float)1/3, p01.getProbability(), 1e-6f);

        // variable 1 entries appear next
        Probability p10 = probs.get(2);
        assertEquals(1, ((Integer)p10.getKey()).intValue());
        assertEquals(2, ((Integer)p10.getValue()).intValue());
        assertEquals((float)2/3, p10.getProbability(), 1e-6f);

        Probability p11 = probs.get(3);
        assertEquals(1, ((Integer)p11.getKey()).intValue());
        assertEquals(3, ((Integer)p11.getValue()).intValue());
        assertEquals((float)1/3, p11.getProbability(), 1e-6f);
    }

    @Test
    public void getListKey_extractsKeys_fromSortedMapInOrder() {
        SortedMap<String,Object> map = new TreeMap<>();
        map.put("b", 2);
        map.put("a", 1);
        map.put("c", 3);

        Univariate u = new Univariate();
        List<String> keys = u.getListKey(map);

        // TreeMap sorts keys alphabetically: a, b, c
        assertEquals(3, keys.size());
        assertEquals("a", keys.get(0));
        assertEquals("b", keys.get(1));
        assertEquals("c", keys.get(2));
    }
}
