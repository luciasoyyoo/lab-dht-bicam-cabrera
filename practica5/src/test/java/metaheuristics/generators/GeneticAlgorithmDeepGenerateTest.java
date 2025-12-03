package metaheuristics.generators;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import problem.definition.State;
import problem.definition.Problem;
import testutils.TestHelper;

public class GeneticAlgorithmDeepGenerateTest {

    @BeforeEach
    public void setup() {
        TestHelper.initStrategyWithProblem(TestHelper.createMinimalProblemWithCodification(1));
    }

    @Test
    public void generate_pipeline_selection_crossover_mutation_returnsChild() throws Exception {
        GeneticAlgorithm ga = new GeneticAlgorithm();

        // prepare a single father so random pos1/pos2 will be 0
        State father = new State();
        father.setCode(new ArrayList<Object>(){{add(7);} });
        father.setEvaluation(new ArrayList<Double>(){{add(3.0);} });
        List<State> ref = new ArrayList<>(); ref.add(father);
        ga.setListState(ref);

        // ensure Strategy.problem.state exists (TestHelper created it)
        Problem p = metaheurictics.strategy.Strategy.getStrategy().getProblem();
        assertNotNull(p.getState());

        // call generate: selection -> crossover -> mutation
        State child = ga.generate(0);
        assertNotNull(child);
        // child should have code size equal to father code size (1)
        assertNotNull(child.getCode());
        assertEquals(1, child.getCode().size());

        // check some getters to increase coverage
        State refState = ga.getReference();
        assertNotNull(refState);
        List<State> listRef = ga.getReferenceList();
        assertNotNull(listRef);
        assertEquals(1, listRef.size());
    }
}
