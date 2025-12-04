package problem.extension;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import metaheurictics.strategy.Strategy;
import problem.definition.ObjetiveFunction;
import problem.definition.Problem;
import problem.definition.Problem.ProblemType;
import problem.definition.State;

public class MultiObjetivoPuroTest {

    @BeforeEach
    public void beforeEach(){
        // ensure fresh singleton for each test
        Strategy.destroyExecute();
    }

    @Test
    public void testMaxProblem_withMaxObjective_returnsDirectEvaluation(){
        Problem p = new Problem();
        p.setTypeProblem(ProblemType.Maximizar);
        ArrayList<ObjetiveFunction> funcs = new ArrayList<>();
        ObjetiveFunction of = new ObjetiveFunction(){
            private static final long serialVersionUID = 1L;
            @Override
            public Double Evaluation(State state) {
                return 0.3;
            }
        };
        of.setTypeProblem(ProblemType.Maximizar);
        funcs.add(of);
        p.setFunction(funcs);

        Strategy.getStrategy().setProblem(p);

        State s = new State();
        MultiObjetivoPuro m = new MultiObjetivoPuro();
        m.evaluationState(s);

        assertNotNull(s.getEvaluation());
        assertEquals(1, s.getEvaluation().size());
        assertEquals(0.3, s.getEvaluation().get(0), 1e-9);
    }

    @Test
    public void testMaxProblem_withMinObjective_invertsEvaluation(){
        Problem p = new Problem();
        p.setTypeProblem(ProblemType.Maximizar);
        ArrayList<ObjetiveFunction> funcs = new ArrayList<>();
        ObjetiveFunction of = new ObjetiveFunction(){
            private static final long serialVersionUID = 1L;
            @Override
            public Double Evaluation(State state) {
                return 0.2;
            }
        };
        of.setTypeProblem(ProblemType.Minimizar);
        funcs.add(of);
        p.setFunction(funcs);

        Strategy.getStrategy().setProblem(p);

        State s = new State();
        MultiObjetivoPuro m = new MultiObjetivoPuro();
        m.evaluationState(s);

        assertNotNull(s.getEvaluation());
        assertEquals(1, s.getEvaluation().size());
        // when problem is Maximizar and objective is Minimize, code does 1 - eval
        assertEquals(0.8, s.getEvaluation().get(0), 1e-9);
    }

    @Test
    public void testMinProblem_withMaxObjective_invertsEvaluation(){
        Problem p = new Problem();
        p.setTypeProblem(ProblemType.Minimizar);
        ArrayList<ObjetiveFunction> funcs = new ArrayList<>();
        ObjetiveFunction of = new ObjetiveFunction(){
            private static final long serialVersionUID = 1L;
            @Override
            public Double Evaluation(State state) {
                return 0.25;
            }
        };
        of.setTypeProblem(ProblemType.Maximizar);
        funcs.add(of);
        p.setFunction(funcs);

        Strategy.getStrategy().setProblem(p);

        State s = new State();
        MultiObjetivoPuro m = new MultiObjetivoPuro();
        m.evaluationState(s);

        assertNotNull(s.getEvaluation());
        assertEquals(1, s.getEvaluation().size());
        // when problem is Minimize and objective is Maximize, code does 1 - eval
        assertEquals(0.75, s.getEvaluation().get(0), 1e-9);
    }

    @Test
    public void testMinProblem_withMinObjective_returnsDirectEvaluation(){
        Problem p = new Problem();
        p.setTypeProblem(ProblemType.Minimizar);
        ArrayList<ObjetiveFunction> funcs = new ArrayList<>();
        ObjetiveFunction of = new ObjetiveFunction(){
            private static final long serialVersionUID = 1L;
            @Override
            public Double Evaluation(State state) {
                return 0.6;
            }
        };
        of.setTypeProblem(ProblemType.Minimizar);
        funcs.add(of);
        p.setFunction(funcs);

        Strategy.getStrategy().setProblem(p);

        State s = new State();
        MultiObjetivoPuro m = new MultiObjetivoPuro();
        m.evaluationState(s);

        assertNotNull(s.getEvaluation());
        assertEquals(1, s.getEvaluation().size());
        assertEquals(0.6, s.getEvaluation().get(0), 1e-9);
    }

    @Test
    public void testMultipleObjectives_respectsOrderAndTransforms(){
        Problem p = new Problem();
        p.setTypeProblem(ProblemType.Maximizar);
        ArrayList<ObjetiveFunction> funcs = new ArrayList<>();

        ObjetiveFunction f1 = new ObjetiveFunction(){
            private static final long serialVersionUID = 1L;
            @Override
            public Double Evaluation(State state) { return 0.1; }
        };
        f1.setTypeProblem(ProblemType.Maximizar);

        ObjetiveFunction f2 = new ObjetiveFunction(){
            private static final long serialVersionUID = 1L;
            @Override
            public Double Evaluation(State state) { return 0.4; }
        };
        f2.setTypeProblem(ProblemType.Minimizar);

        funcs.add(f1);
        funcs.add(f2);
        p.setFunction(funcs);

        Strategy.getStrategy().setProblem(p);

        State s = new State();
        MultiObjetivoPuro m = new MultiObjetivoPuro();
        m.evaluationState(s);

        assertNotNull(s.getEvaluation());
        assertEquals(2, s.getEvaluation().size());
        // first is direct 0.1
        assertEquals(0.1, s.getEvaluation().get(0), 1e-9);
        // second is 1 - 0.4 = 0.6 because problem is Max and objective is Min
        assertEquals(0.6, s.getEvaluation().get(1), 1e-9);
    }
}
