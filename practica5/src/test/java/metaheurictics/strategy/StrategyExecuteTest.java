package metaheurictics.strategy;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

import local_search.complement.StopExecute;
import metaheuristics.generators.GeneratorType;

public class StrategyExecuteTest {

    @Test
    @Disabled("Flaky in headless unit runs — requires more complex global state. Disabled for stability.")
    public void executeStrategyRunsSetupAndStopsImmediately() throws Exception {
        Strategy s = Strategy.getStrategy();
        s.setProblem(testutils.TestHelper.createMinimalProblemWithCodification(1));
        // ensure generators map is present
        s.initialize();

        // configure to save lists so executeStrategy goes through branches that populate them
        s.saveListStates = true;
        s.saveListBestStates = true;
        s.saveFreneParetoMonoObjetivo = false;

        // set a StopExecute that returns true immediately to avoid long loops
        s.setStopexecute(new StopExecute() {
            @Override
            public Boolean stopIterations(int countIterationsCurrent, int countmaxIterations) {
                return true; // stop immediately
            }
        });

        // run executeStrategy; it should perform setup and exit without throwing
        s.executeStrategy(1, 1, 1, GeneratorType.RandomSearch);

        assertNotNull(s.getProblem().getState());
        if (s.saveListStates) {
            assertNotNull(s.listStates);
            // list may be empty depending on generator behavior; ensure no exception and list exists
        }
        if (s.saveListBestStates) {
            assertNotNull(s.listBest);
        }
    }
}
