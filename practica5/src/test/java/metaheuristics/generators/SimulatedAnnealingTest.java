package metaheuristics.generators;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class SimulatedAnnealingTest {

    @Test
    public void staticTemperature_and_countIterationsT_behaviour() {
        // initial defaults
        Double init = SimulatedAnnealing.getTinitial();
        int cnt = SimulatedAnnealing.getCountIterationsT();
        // modify and verify
        SimulatedAnnealing.setTinitial(100.0);
        assertEquals(100.0, SimulatedAnnealing.getTinitial());
        SimulatedAnnealing.setCountIterationsT(123);
        assertEquals(123, SimulatedAnnealing.getCountIterationsT());
        // restore some values to avoid side effects for other tests
        SimulatedAnnealing.setTinitial(init);
        SimulatedAnnealing.setCountIterationsT(cnt);
    }

    @Test
    public void weight_and_reference_list_behaviour() {
        SimulatedAnnealing sa = new SimulatedAnnealing();
        assertEquals(50.0f, sa.getWeight(), 0.001f);
        sa.setWeight(9.9f);
        assertEquals(9.9f, sa.getWeight(), 0.001f);
        // setting and getting reference list
        assertNotNull(sa.getReferenceList());
    }
}
