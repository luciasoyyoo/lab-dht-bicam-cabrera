package local_search.acceptation_type;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import local_search.acceptation_type.AcceptAnyone;
import problem.definition.State;

public class AcceptAnyoneTest {

    @Test
    public void testAcceptsWhenBothNull() throws Exception{
        AcceptAnyone a = new AcceptAnyone();
        assertTrue(a.acceptCandidate(null, null));
    }

    @Test
    public void testAcceptsWithStates() throws Exception{
        AcceptAnyone a = new AcceptAnyone();
        State s1 = new State();
        s1.setEvaluation(new ArrayList<Double>(){{ add(1.0); }});
        State s2 = new State();
        s2.setEvaluation(new ArrayList<Double>(){{ add(2.0); }});
        assertTrue(a.acceptCandidate(s1, s2));
    }
}
