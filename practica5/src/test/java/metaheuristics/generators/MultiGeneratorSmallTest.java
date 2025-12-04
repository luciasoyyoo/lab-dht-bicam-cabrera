package metaheuristics.generators;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import metaheurictics.strategy.Strategy;

public class MultiGeneratorSmallTest {

    @Test
    public void initializeListGeneratorAndRoulette() throws Exception {
        Strategy.getStrategy().setProblem(testutils.TestHelper.createMinimalProblemWithCodification(1));
        MultiGenerator.initializeListGenerator();
        Generator[] gens = MultiGenerator.getListGenerators();
        assertNotNull(gens);
        assertTrue(gens.length >= 4);

        MultiGenerator mg = new MultiGenerator();
        Generator g = mg.roulette();
        assertNotNull(g);
    }

    @Test
    public void updateAwardMethodsDoNotThrow() throws Exception {
        Strategy.getStrategy().setProblem(testutils.TestHelper.createMinimalProblemWithCodification(1));
        Strategy.getStrategy().setBestState(testutils.TestHelper.createMinimalProblemWithCodification(1).getState());
        MultiGenerator.initializeListGenerator();
        Generator[] gens = MultiGenerator.getListGenerators();
        MultiGenerator.setListGenerators(gens);
        MultiGenerator.setActiveGenerator(gens[0]);
        Strategy.getStrategy().setCountCurrent(0);

        MultiGenerator mg = new MultiGenerator();
        // call both award update variations; they should run without exception
        mg.updateAwardImp();
        mg.updateAwardSC();

        // basic assertions to ensure state remains consistent after updates
        assertNotNull(MultiGenerator.getActiveGenerator());
        assertSame(gens[0], MultiGenerator.getActiveGenerator());
    }
}
