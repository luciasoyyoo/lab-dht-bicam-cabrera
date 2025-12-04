package evolutionary_algorithms.complement;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import config.tspDynamic.TSPState;
import metaheurictics.strategy.Strategy;
import problem.definition.Codification;
import problem.definition.Problem;
import problem.definition.State;
import testutils.TestHelper;

public class AIOMutationTest {

    @BeforeEach
    public void setup(){
        // create a problem with 4 variables by default
        TestHelper.initStrategyWithProblem(TestHelper.createMinimalProblemWithCodification(4));
        AIOMutation.path.clear();
    }

    @AfterEach
    public void tearDown(){
        AIOMutation.path.clear();
        Strategy.destroyExecute();
    }

    @Test
    public void testFillPath_populates_path_with_indices() {
        Problem p = Strategy.getStrategy().getProblem();
        p.setCodification(new Codification() {
            @Override public boolean validState(State state) { return true; }
            @Override public Object getVariableAleatoryValue(int key) { return 0; }
            @Override public int getAleatoryKey() { return 0; }
            @Override public int getVariableCount() { return 3; }
        });
        Strategy.getStrategy().setProblem(p);

        AIOMutation.fillPath();
        assertEquals(3, AIOMutation.path.size());
        assertEquals(0, AIOMutation.path.get(0));
        assertEquals(1, AIOMutation.path.get(1));
        assertEquals(2, AIOMutation.path.get(2));
    }

    @Test
    public void testMutation_swaps_segment_between_keys() {
        // custom codification that will return first key=1 then key1=3
        AtomicInteger calls = new AtomicInteger(0);
        Codification seq = new Codification() {
            @Override public boolean validState(State state) { return true; }
            @Override public Object getVariableAleatoryValue(int key) { return 0; }
            @Override public int getAleatoryKey() { int c = calls.getAndIncrement(); return (c==0) ? 1 : 3; }
            @Override public int getVariableCount() { return 4; }
        };
        Problem p = Strategy.getStrategy().getProblem();
        p.setCodification(seq);
        Strategy.getStrategy().setProblem(p);

        State s = new State();
        List<Object> code = new ArrayList<>();
        // create 4 TSPState objects with idCity 10,20,30,40
        for (int i = 0; i < 4; i++) {
            TSPState t = new TSPState();
            t.setIdCity(10 + i*10);
            t.setValue(100 - i); // values decreasing to exercise sortedPathValue
            code.add(t);
        }
        s.setCode(new ArrayList<Object>(code));

        AIOMutation mut = new AIOMutation();
        State out = mut.mutation(s, 0.5);

        // After mutation with keys 1 and 3, elements at indices 1 and 3 should have swapped idCity
        TSPState idx1 = (TSPState) out.getCode().get(1);
        TSPState idx3 = (TSPState) out.getCode().get(3);
        assertEquals(40, idx1.getIdCity());
        assertEquals(20, idx3.getIdCity());
    }

}
