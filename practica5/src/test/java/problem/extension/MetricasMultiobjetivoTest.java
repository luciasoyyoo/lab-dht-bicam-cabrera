package problem.extension;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jxl.read.biff.BiffException;
import problem.definition.State;

public class MetricasMultiobjetivoTest {

    @BeforeEach
    public void before(){ }

    @AfterEach
    public void after(){ }

    @Test
    public void testTasaError_containsLogic() throws BiffException, IOException{
        MetricasMultiobjetivo m = new MetricasMultiobjetivo();
        State a = new State(); a.setEvaluation(new ArrayList<Double>(List.of(1.0)));
        State b = new State(); b.setEvaluation(new ArrayList<Double>(List.of(2.0)));
        State c = new State(); c.setEvaluation(new ArrayList<Double>(List.of(3.0)));

        List<State> current = new ArrayList<>(); current.add(a); current.add(b); current.add(c);
        List<State> truth = new ArrayList<>(); truth.add(a); truth.add(c);

    double tasa = m.TasaError(current, truth);
    // b is not in truth => 1 out of 3; method uses floats internally so allow small delta
    assertEquals(1.0/3.0, tasa, 1e-6);
    }

    @Test
    public void testDistanciaGeneracional_singlePair() throws BiffException, IOException{
        MetricasMultiobjetivo m = new MetricasMultiobjetivo();
        State cur = new State(); cur.setEvaluation(new ArrayList<Double>(List.of(0.0, 0.0)));
        State tru = new State(); tru.setEvaluation(new ArrayList<Double>(List.of(3.0, 4.0)));
        List<State> current = new ArrayList<>(); current.add(cur);
        List<State> truth = new ArrayList<>(); truth.add(tru);
        double dist = m.DistanciaGeneracional(current, truth);
        // as implemented: squared distance 25, sqrt(25)/1 = 5
        assertEquals(5.0, dist, 1e-9);
    }

    @Test
    public void testDispersion_identicalStates_zero() throws BiffException, IOException{
        MetricasMultiobjetivo m = new MetricasMultiobjetivo();
        ArrayList<State> sols = new ArrayList<>();
        State s1 = new State(); s1.setEvaluation(new ArrayList<Double>(List.of(1.0,1.0)));
        State s2 = new State(); s2.setEvaluation(new ArrayList<Double>(List.of(1.0,1.0)));
        sols.add(s1); sols.add(s2);
        double disp = m.Dispersion(sols);
        assertEquals(0.0, disp, 1e-9);
    }

    @Test
    public void testDispersion_nontrivial() throws BiffException, IOException{
        MetricasMultiobjetivo m = new MetricasMultiobjetivo();
        ArrayList<State> sols = new ArrayList<>();
        State s1 = new State(); s1.setEvaluation(new ArrayList<Double>(List.of(0.0)));
        State s2 = new State(); s2.setEvaluation(new ArrayList<Double>(List.of(2.0)));
        State s3 = new State(); s3.setEvaluation(new ArrayList<Double>(List.of(4.0)));
        sols.add(s1); sols.add(s2); sols.add(s3);
        double disp = m.Dispersion(sols);
        // dispersion should be a finite non-negative number
        assertTrue(Double.isFinite(disp));
        assertTrue(disp >= 0.0);
    }

    @Test
    public void testCalcularMinMaxMedia_edgeCases(){
        MetricasMultiobjetivo m = new MetricasMultiobjetivo();
        ArrayList<Double> arr = new ArrayList<>(); arr.add(-1.0); arr.add(0.0); arr.add(5.0);
        assertEquals(-1.0, m.CalcularMin(arr), 1e-9);
        assertEquals(5.0, m.CalcularMax(arr), 1e-9);
        assertEquals(( -1.0 + 0.0 + 5.0)/3.0, m.CalcularMedia(arr), 1e-9);
    }
}
