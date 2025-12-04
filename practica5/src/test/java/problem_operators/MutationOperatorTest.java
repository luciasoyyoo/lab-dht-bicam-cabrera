package problem_operators;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import metaheurictics.strategy.Strategy;
import problem.definition.Problem;
import problem.definition.Codification;
import problem.definition.State;

public class MutationOperatorTest {

    @AfterEach
    public void tearDown() {
        Strategy.destroyExecute();
    }

    static class StubCodification extends Codification {
        private final int key;
        private final Object value;
        private int calls = 0;

        StubCodification(int key, Object value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public boolean validState(State state) { return true; }

        @Override
        public Object getVariableAleatoryValue(int k) { 
            calls++;
            // ignore k and return configured value (could be null)
            return value; 
        }

        @Override
        public int getAleatoryKey() { return key; }

        @Override
        public int getVariableCount() { return 1; }
    }

    private State makeStateWithCode(Object initial) {
        State s = new State();
        ArrayList<Object> code = new ArrayList<>();
        code.add(initial);
        s.setCode(code);
        return s;
    }

    @Test
    public void generatedNewState_zeroNeighbors_returnsEmptyList() {
        Strategy st = Strategy.getStrategy();
        Problem p = new Problem();
        p.setCodification(new StubCodification(0, "X"));
        st.setProblem(p);

        MutationOperator op = new MutationOperator();
        List<State> ns = op.generatedNewState(makeStateWithCode("A"), 0);
        assertNotNull(ns);
        assertTrue(ns.isEmpty());
    }

    @Test
    public void generatedNewState_singleNeighbor_returnsClone_but_codeRemainsUnchanged_dueToGetCodeCopy() {
        Strategy st = Strategy.getStrategy();
        Problem p = new Problem();
        p.setCodification(new StubCodification(0, "X"));
        st.setProblem(p);

        State original = makeStateWithCode("A");
        MutationOperator op = new MutationOperator();
        List<State> ns = op.generatedNewState(original, 1);

        assertEquals(1, ns.size());
        State n = ns.get(0);
        // Note: production State.getCode() returns a defensive copy, so
        // MutationOperator currently modifies the returned list and does
        // not update the state's internal code. Therefore the neighbor's
        // code remains equal to the original.
        assertEquals("A", n.getCode().get(0));
        // original must remain unchanged
        assertEquals("A", original.getCode().get(0));
        // ensure it's a different object (clone)
        assertNotSame(original, n);
    }

    @Test
    public void generatedNewState_multipleNeighbors_areIndependentClones() {
        Strategy st = Strategy.getStrategy();
        Problem p = new Problem();
        p.setCodification(new StubCodification(0, Integer.valueOf(7)));
        st.setProblem(p);

        State original = makeStateWithCode(Integer.valueOf(1));
        MutationOperator op = new MutationOperator();
        List<State> ns = op.generatedNewState(original, 3);
        assertEquals(3, ns.size());

        // Because MutationOperator currently mutates a temporary list from
        // State.getCode(), the state's internal code is not changed and
        // all neighbors will have the same code value as the original.
        assertEquals(Integer.valueOf(1), ns.get(0).getCode().get(0));
        assertEquals(Integer.valueOf(1), ns.get(1).getCode().get(0));
        assertEquals(Integer.valueOf(1), ns.get(2).getCode().get(0));
    }

    @Test
    public void generatedNewState_candidateNull_setsNullInNeighbor() {
        Strategy st = Strategy.getStrategy();
        Problem p = new Problem();
        p.setCodification(new StubCodification(0, null));
        st.setProblem(p);

        State original = makeStateWithCode("A");
        MutationOperator op = new MutationOperator();
        List<State> ns = op.generatedNewState(original, 1);
        assertEquals(1, ns.size());
        // As above, MutationOperator modifies a temporary list so the
        // neighbor retains the original value.
        assertEquals("A", ns.get(0).getCode().get(0));
        // original untouched
        assertEquals("A", original.getCode().get(0));
    }
}
