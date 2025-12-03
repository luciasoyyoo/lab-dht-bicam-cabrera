package metaheuristics.generators;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import testutils.TestHelper;
import problem.definition.State;

public class MultiGeneratorTest {

    @BeforeEach
    public void setup() {
        TestHelper.initStrategyWithProblem(TestHelper.createMinimalProblemWithCodification(1));
    }

    @Test
    public void roulette_and_updateAward_behavior() throws Exception {
        // prepare listGenerators with simple generators with weight>0
        Generator[] gens = new Generator[4];
        gens[0] = new HillClimbing();
        gens[1] = new EvolutionStrategies();
        gens[2] = new LimitThreshold();
        gens[3] = new GeneticAlgorithm();
        MultiGenerator.setListGenerators(gens);

        MultiGenerator mg = new MultiGenerator();

        // roulette should return one of the generators (non-null)
        Generator chosen = mg.roulette();
        assertNotNull(chosen);

        // set activeGenerator and best state to test searchState/updateAward
    MultiGenerator.setActiveGenerator(gens[0]);
    State best = new State(); best.setEvaluation(new ArrayList<Double>(){{add(0.5);} });
    metaheurictics.strategy.Strategy.getStrategy().setBestState(best);
    // ensure Problem type is set to Maximizar for searchState logic
    metaheurictics.strategy.Strategy.getStrategy().getProblem().setTypeProblem(problem.definition.Problem.ProblemType.Maximizar);
    metaheurictics.strategy.Strategy.getStrategy().setCountCurrent(0);

    // exercise updateAwardSC and updateAwardImp paths by calling updateWeight, which delegates
        MultiGenerator.setActiveGenerator(gens[1]);
        metaheurictics.strategy.Strategy.getStrategy().setCountCurrent(0);
        mg.updateAwardSC();
        mg.updateAwardImp();
    // clone should not throw and return a MultiGenerator or clone
        Object cloned = mg.clone();
        assertNotNull(cloned);
    }
}
