package problem.extension;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jxl.read.biff.BiffException;
import metaheurictics.strategy.Strategy;
import problem.definition.ObjetiveFunction;
import problem.definition.Problem;
import problem.definition.State;
import problem.definition.Problem.ProblemType;

public class ProblemExtensionTests {

    @BeforeEach
    public void beforeEach(){
        Strategy.destroyExecute();
    }

    @AfterEach
    public void afterEach(){
        Strategy.destroyExecute();
    }

    @Test
    public void testMultiObjetivoPuro_Maximize_MixedTypes(){
        Strategy.destroyExecute();
        Strategy st = Strategy.getStrategy();
        Problem p = new Problem();
        p.setTypeProblem(ProblemType.Maximizar);
        ArrayList<ObjetiveFunction> funcs = new ArrayList<>();
        ObjetiveFunction f1 = new ObjetiveFunction(){
            { setTypeProblem(ProblemType.Maximizar); }
            @Override public Double Evaluation(State state){ return 0.2; }
        };
        ObjetiveFunction f2 = new ObjetiveFunction(){
            { setTypeProblem(ProblemType.Minimizar); }
            @Override public Double Evaluation(State state){ return 0.7; }
        };
        f1.setWeight(1.0f); f2.setWeight(1.0f);
        funcs.add(f1); funcs.add(f2);
        p.setFunction(funcs);
        st.setProblem(p);

        State s = new State();
        new MultiObjetivoPuro().evaluationState(s);
        List<Double> eval = s.getEvaluation();
        // For maximize problem: f1 -> 0.2 (same), f2 (Minimizar) -> 1 - 0.7 = 0.3
        assertEquals(2, eval.size());
        assertEquals(0.2, eval.get(0));
        assertEquals(0.3, eval.get(1));
    }

    @Test
    public void testFactoresPonderados_computation(){
        Strategy.destroyExecute();
        Strategy st = Strategy.getStrategy();
        Problem p = new Problem();
        p.setTypeProblem(ProblemType.Maximizar);
        ArrayList<ObjetiveFunction> funcs = new ArrayList<>();
        ObjetiveFunction f1 = new ObjetiveFunction(){
            { setTypeProblem(ProblemType.Maximizar); setWeight(0.5f); }
            @Override public Double Evaluation(State state){ return 0.6; }
        };
        ObjetiveFunction f2 = new ObjetiveFunction(){
            { setTypeProblem(ProblemType.Minimizar); setWeight(2.0f); }
            @Override public Double Evaluation(State state){ return 0.25; }
        };
        funcs.add(f1); funcs.add(f2);
        p.setFunction(funcs);
        st.setProblem(p);

        State s = new State();
        new FactoresPonderados().evaluationState(s);
        List<Double> eval = s.getEvaluation();
        // f1: 0.6 * 0.5 = 0.3 ; f2: (1 - 0.25) * 2 = 1.5 ; total = 1.8
        assertEquals(1, eval.size());
        assertEquals(1.8, eval.get(0));
    }

    @Test
    public void testMetricas_TasaError_and_Calculars() throws BiffException, IOException{
        MetricasMultiobjetivo metrics = new MetricasMultiobjetivo();
        State a = new State(); a.setEvaluation(new ArrayList<Double>(List.of(1.0)));
        State b = new State(); b.setEvaluation(new ArrayList<Double>(List.of(2.0)));
        List<State> current = new ArrayList<>(); current.add(a); current.add(b);
        List<State> truth = new ArrayList<>(); truth.add(a);
        double tasa = metrics.TasaError(current, truth);
        assertEquals(0.5, tasa);

        // CalcularMin/Max/Media
        ArrayList<Double> arr = new ArrayList<>(); arr.add(1.0); arr.add(3.0); arr.add(2.0);
        assertEquals(1.0, metrics.CalcularMin(arr));
        assertEquals(3.0, metrics.CalcularMax(arr));
        assertEquals(2.0, metrics.CalcularMedia(arr));
    }

    @Test
    public void testMetricas_DistanciaGeneracional_and_Dispersion() throws BiffException, IOException{
        MetricasMultiobjetivo metrics = new MetricasMultiobjetivo();
        // DistanciaGeneracional simple: current [0,0] vs true [3,4] => distance = sqrt(25) = 5.0
        State cur = new State(); cur.setEvaluation(new ArrayList<Double>(List.of(0.0, 0.0)));
        State tru = new State(); tru.setEvaluation(new ArrayList<Double>(List.of(3.0, 4.0)));
        List<State> current = new ArrayList<>(); current.add(cur);
        List<State> truth = new ArrayList<>(); truth.add(tru);
        double dist = metrics.DistanciaGeneracional(current, truth);
        assertEquals(5.0, dist);

        // Dispersion: identical states -> dispersion 0
        ArrayList<State> sols = new ArrayList<>();
        State s1 = new State(); s1.setEvaluation(new ArrayList<Double>(List.of(1.0,1.0)));
        State s2 = new State(); s2.setEvaluation(new ArrayList<Double>(List.of(1.0,1.0)));
        sols.add(s1); sols.add(s2);
        double disp = metrics.Dispersion(sols);
        assertEquals(0.0, disp);
    }

    @Test
    public void testTypeSolutionMethod_enum(){
        // simple sanity check on enum constants
        assertTrue(TypeSolutionMethod.values().length >= 2);
        assertNotNull(TypeSolutionMethod.FactoresPonderados);
        assertNotNull(TypeSolutionMethod.MultiObjetivoPuro);
    }

}
