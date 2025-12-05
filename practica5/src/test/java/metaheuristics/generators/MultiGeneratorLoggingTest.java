package metaheuristics.generators;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import testutils.TestHelper;
import metaheurictics.strategy.Strategy;

import static org.junit.jupiter.api.Assertions.*;

public class MultiGeneratorLoggingTest {

    @BeforeEach
    public void setup() {
        TestHelper.initStrategyWithProblem(TestHelper.createMinimalProblemWithCodification(1));
    }

    @AfterEach
    public void tearDown() {
        try { MultiGenerator.destroyMultiGenerator(); } catch (Exception e) {}
        Strategy.destroyExecute();
    }

    @Test
    public void classLoads_and_createInstanceGeneratorsBPP_runs_without_exception() throws Exception {
        // This test ensures the MultiGenerator class is loaded (LOGGER field initialized)
        // and that calling the helper methods exercises the new code paths we added.
        MultiGenerator mg = new MultiGenerator();
        assertNotNull(mg);

        // initialize the internal list of generators (should not throw)
        MultiGenerator.initializeListGenerator();

        // call createInstanceGeneratorsBPP (loop is guarded by EvolutionStrategies.countRef == 0,
        // but invoking the method exercises the code and the new logger declaration).
        MultiGenerator.createInstanceGeneratorsBPP();

        // If we reach here, the method executed without throwing; assert listGenerators is set
        assertNotNull(MultiGenerator.getListGenerators());
    }
}
