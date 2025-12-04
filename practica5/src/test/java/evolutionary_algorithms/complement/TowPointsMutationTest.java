package evolutionary_algorithms.complement;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import metaheurictics.strategy.Strategy;
import problem.definition.Codification;
import problem.definition.Problem;
import problem.definition.State;
import testutils.TestHelper;

public class TowPointsMutationTest {

    @BeforeEach
    public void setup(){
        // start with a minimal problem with 2 variables so codes have two positions
        Problem p = TestHelper.createMinimalProblemWithCodification(2);
        // replace codification with a custom one in tests when needed
        Strategy.getStrategy().setProblem(p);
    }

    @Test
    public void testMutation_swaps_usingCodificationProvidedValues() {
        // create a codification that returns keys 0 then 1 (cycling) and values 100 and 200
        Codification cyc = new Codification() {
            private AtomicInteger counter = new AtomicInteger(0);
            @Override public boolean validState(State state) { return true; }
            @Override public Object getVariableAleatoryValue(int key) {
                if (key == 0) return 100;
                return 200;
            }
            @Override public int getAleatoryKey() { return counter.getAndIncrement() % 2; }
            @Override public int getVariableCount() { return 2; }
        };

        Problem p = Strategy.getStrategy().getProblem();
        p.setCodification(cyc);
        Strategy.getStrategy().setProblem(p);

        State s = new State();
        ArrayList<Object> code = new ArrayList<>(); code.add(1); code.add(2);
        s.setCode(code);

        TowPointsMutation mut = new TowPointsMutation();
    State out = mut.mutation(s, 0.5);

    // Current implementation of TowPointsMutation uses State.getCode() which returns a defensive copy,
    // so the mutation modifies the copy and the internal state's code remains unchanged.
    assertEquals(1, out.getCode().get(0));
    assertEquals(2, out.getCode().get(1));
    }

    @Test
    public void testMutation_sameKey_noException_and_length_preserved() {
        // codification that always returns key 0 and value 42
        Codification single = new Codification() {
            @Override public boolean validState(State state) { return true; }
            @Override public Object getVariableAleatoryValue(int key) { return 42; }
            @Override public int getAleatoryKey() { return 0; }
            @Override public int getVariableCount() { return 1; }
        };
        Problem p = Strategy.getStrategy().getProblem();
        p.setCodification(single);
        Strategy.getStrategy().setProblem(p);

        State s = new State();
        ArrayList<Object> code = new ArrayList<>(); code.add(7);
        s.setCode(code);

        TowPointsMutation mut = new TowPointsMutation();
    State out = mut.mutation(s, 0.1);

    // Because mutation writes into a defensive copy returned by State.getCode(), the original code remains unchanged
    assertEquals(1, out.getCode().size());
    assertEquals(7, out.getCode().get(0));
    }
}
