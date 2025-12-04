package evolutionary_algorithms.complement;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import metaheurictics.strategy.Strategy;
import problem.definition.State;
import problem.definition.Problem;
import testutils.TestHelper;

public class TruncationSelectionTest {

    @BeforeEach
    public void setup(){
        TestHelper.initStrategyWithProblem(TestHelper.createMinimalProblemWithCodification(1));
    }

    @AfterEach
    public void teardown(){
        Strategy.destroyExecute();
    }

    @Test
    public void testOrderBetter_sortsDescendingByEvaluation(){
        TruncationSelection sel = new TruncationSelection();
        List<State> list = new ArrayList<>();
        State a = new State(); a.setEvaluation(new ArrayList<Double>(){{ add(1.0); }});
        State b = new State(); b.setEvaluation(new ArrayList<Double>(){{ add(3.0); }});
        State c = new State(); c.setEvaluation(new ArrayList<Double>(){{ add(2.0); }});
        list.add(a); list.add(b); list.add(c);

        List<State> out = sel.orderBetter(list);
        assertEquals(3.0, out.get(0).getEvaluation().get(0), 0.0);
        assertEquals(2.0, out.get(1).getEvaluation().get(0), 0.0);
        assertEquals(1.0, out.get(2).getEvaluation().get(0), 0.0);
    }

    @Test
    public void testAscOrderBetter_sortsAscendingByEvaluation(){
        TruncationSelection sel = new TruncationSelection();
        List<State> list = new ArrayList<>();
        State a = new State(); a.setEvaluation(new ArrayList<Double>(){{ add(2.0); }});
        State b = new State(); b.setEvaluation(new ArrayList<Double>(){{ add(0.5); }});
        State c = new State(); c.setEvaluation(new ArrayList<Double>(){{ add(1.5); }});
        list.add(a); list.add(b); list.add(c);

        List<State> out = sel.ascOrderBetter(list);
        assertEquals(0.5, out.get(0).getEvaluation().get(0), 0.0);
        assertEquals(1.5, out.get(1).getEvaluation().get(0), 0.0);
        assertEquals(2.0, out.get(2).getEvaluation().get(0), 0.0);
    }

    @Test
    public void testSelection_truncation_maximizar_returnsTopN(){
        // set problem to Maximizar
        Strategy.getStrategy().getProblem().setTypeProblem(Problem.ProblemType.Maximizar);

        TruncationSelection sel = new TruncationSelection();
        List<State> list = new ArrayList<>();
        State s1 = new State(); s1.setEvaluation(new ArrayList<Double>(){{ add(1.0); }});
        State s2 = new State(); s2.setEvaluation(new ArrayList<Double>(){{ add(4.0); }});
        State s3 = new State(); s3.setEvaluation(new ArrayList<Double>(){{ add(2.5); }});
        list.add(s1); list.add(s2); list.add(s3);

        List<State> res = sel.selection(list, 2);
        assertEquals(2, res.size());
        // should pick 4.0 and 2.5
        assertEquals(4.0, res.get(0).getEvaluation().get(0), 0.0);
        assertEquals(2.5, res.get(1).getEvaluation().get(0), 0.0);
    }

    @Test
    public void testSelection_truncation_minimizar_returnsBottomN(){
        // set problem to Minimizar
        Strategy.getStrategy().getProblem().setTypeProblem(Problem.ProblemType.Minimizar);

        TruncationSelection sel = new TruncationSelection();
        List<State> list = new ArrayList<>();
        State s1 = new State(); s1.setEvaluation(new ArrayList<Double>(){{ add(5.0); }});
        State s2 = new State(); s2.setEvaluation(new ArrayList<Double>(){{ add(1.0); }});
        State s3 = new State(); s3.setEvaluation(new ArrayList<Double>(){{ add(3.0); }});
        list.add(s1); list.add(s2); list.add(s3);

        List<State> res = sel.selection(list, 2);
        assertEquals(2, res.size());
        // should pick 1.0 and 3.0 in ascending order
        assertEquals(1.0, res.get(0).getEvaluation().get(0), 0.0);
        assertEquals(3.0, res.get(1).getEvaluation().get(0), 0.0);
    }
}
