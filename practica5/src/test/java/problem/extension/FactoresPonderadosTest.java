package problem.extension;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import metaheurictics.strategy.Strategy;
import problem.definition.ObjetiveFunction;
import problem.definition.Problem;
import problem.definition.State;
import problem.definition.Problem.ProblemType;

public class FactoresPonderadosTest {

    @BeforeEach
    public void setup(){
        Strategy.destroyExecute();
    }

    @AfterEach
    public void tearDown(){
        Strategy.destroyExecute();
    }

    @Test
    public void testAllMaximizarFunctions() {
        Strategy st = Strategy.getStrategy();
        Problem p = new Problem();
        p.setTypeProblem(ProblemType.Maximizar);
        ArrayList<ObjetiveFunction> funcs = new ArrayList<>();
        ObjetiveFunction f1 = new ObjetiveFunction(){
            { setTypeProblem(ProblemType.Maximizar); setWeight(0.5f); }
            @Override public Double Evaluation(State state){ return 0.6; }
        };
        ObjetiveFunction f2 = new ObjetiveFunction(){
            { setTypeProblem(ProblemType.Maximizar); setWeight(2.0f); }
            @Override public Double Evaluation(State state){ return 0.4; }
        };
        funcs.add(f1); funcs.add(f2);
        p.setFunction(funcs);
        st.setProblem(p);

        State s = new State();
        new FactoresPonderados().evaluationState(s);
        List<Double> eval = s.getEvaluation();
        // expected: 0.6*0.5 + 0.4*2.0 = 0.3 + 0.8 = 1.1
        assertEquals(1, eval.size());
        assertEquals(1.1, eval.get(0), 1e-9);
    }

    @Test
    public void testMaximize_withMixedFunctionTypes(){
        Strategy st = Strategy.getStrategy();
        Problem p = new Problem();
        p.setTypeProblem(ProblemType.Maximizar);
        ArrayList<ObjetiveFunction> funcs = new ArrayList<>();
        ObjetiveFunction maxF = new ObjetiveFunction(){
            { setTypeProblem(ProblemType.Maximizar); setWeight(1.0f); }
            @Override public Double Evaluation(State state){ return 0.6; }
        };
        ObjetiveFunction minF = new ObjetiveFunction(){
            { setTypeProblem(ProblemType.Minimizar); setWeight(1.0f); }
            @Override public Double Evaluation(State state){ return 0.25; }
        };
        funcs.add(maxF); funcs.add(minF);
        p.setFunction(funcs);
        st.setProblem(p);

        State s = new State();
        new FactoresPonderados().evaluationState(s);
        List<Double> eval = s.getEvaluation();
        // expected: 0.6*1.0 + (1-0.25)*1.0 = 0.6 + 0.75 = 1.35
        assertEquals(1, eval.size());
        assertEquals(1.35, eval.get(0), 1e-9);
    }

    @Test
    public void testMinimize_withMixedFunctionTypes(){
        Strategy st = Strategy.getStrategy();
        Problem p = new Problem();
        p.setTypeProblem(ProblemType.Minimizar);
        ArrayList<ObjetiveFunction> funcs = new ArrayList<>();
        ObjetiveFunction maxF = new ObjetiveFunction(){
            { setTypeProblem(ProblemType.Maximizar); setWeight(2.0f); }
            @Override public Double Evaluation(State state){ return 0.6; }
        };
        ObjetiveFunction minF = new ObjetiveFunction(){
            { setTypeProblem(ProblemType.Minimizar); setWeight(0.5f); }
            @Override public Double Evaluation(State state){ return 0.25; }
        };
        funcs.add(maxF); funcs.add(minF);
        p.setFunction(funcs);
        st.setProblem(p);

        State s = new State();
        new FactoresPonderados().evaluationState(s);
        List<Double> eval = s.getEvaluation();
        // expected: (1-0.6)*2.0 + 0.25*0.5 = 0.4*2 + 0.125 = 0.8 + 0.125 = 0.925
        assertEquals(1, eval.size());
        assertEquals(0.925, eval.get(0), 1e-9);
    }

    @Test
    public void testZeroWeights_and_multipleFunctions(){
        Strategy st = Strategy.getStrategy();
        Problem p = new Problem();
        p.setTypeProblem(ProblemType.Maximizar);
        ArrayList<ObjetiveFunction> funcs = new ArrayList<>();
        for (int i = 0; i < 4; i++){
            final double val = i * 0.1;
            ObjetiveFunction f = new ObjetiveFunction(){
                { setTypeProblem(ProblemType.Maximizar); setWeight(0.0f); }
                @Override public Double Evaluation(State state){ return val; }
            };
            funcs.add(f);
        }
        p.setFunction(funcs);
        st.setProblem(p);

        State s = new State();
        new FactoresPonderados().evaluationState(s);
        List<Double> eval = s.getEvaluation();
        // all weights zero => total 0
        assertEquals(1, eval.size());
        assertEquals(0.0, eval.get(0), 1e-9);
    }

}
