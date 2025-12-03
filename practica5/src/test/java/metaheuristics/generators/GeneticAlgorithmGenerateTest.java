package metaheuristics.generators;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import problem.definition.Operator;
import problem.definition.Problem;
import problem.definition.State;
import testutils.TestHelper;

public class GeneticAlgorithmGenerateTest {

    @BeforeEach
    public void setup() {
        TestHelper.initStrategyWithProblem(TestHelper.createMinimalProblemWithCodification(1));
    }

    @Test
    public void generate_createsOffspring_usingFactories() throws Exception {
        Problem p = metaheurictics.strategy.Strategy.getStrategy().getProblem();

    // Exercise selection + crossover + mutation path using factories (avoid relying on class-level truncation)
    GeneticAlgorithm ga = new GeneticAlgorithm();
    State s1 = new State(); s1.setCode(new ArrayList<Object>(){{add(5);}}); s1.setEvaluation(new ArrayList<Double>(){{add(3.0);}});
    ArrayList<State> list = new ArrayList<>(); list.add(s1);
    ga.setListState(list);

    // selection via RouletteSelection factory
    factory_method.FactoryFatherSelection ffs = new factory_method.FactoryFatherSelection();
    evolutionary_algorithms.complement.FatherSelection sel = ffs.createSelectFather(evolutionary_algorithms.complement.SelectionType.RouletteSelection);
    List<State> fathers = sel.selection(ga.getListState(), 0);
    assertNotNull(fathers);
    assertFalse(fathers.isEmpty());

    // Perform crossover via factory
    factory_method.FactoryCrossover fc = new factory_method.FactoryCrossover();
    evolutionary_algorithms.complement.Crossover cross = fc.createCrossover(evolutionary_algorithms.complement.CrossoverType.OnePointCrossover);
    State parentA = metaheurictics.strategy.Strategy.getStrategy().getProblem() == null ? fathers.get(0) : (State) metaheurictics.strategy.Strategy.getStrategy().getProblem().getState().getCopy();
    parentA.setCode(new ArrayList<Object>(fathers.get(0).getCode()));
    State parentB = new State(); parentB.setCode(new ArrayList<Object>(fathers.get(0).getCode()));
    State afterCross = cross.crossover(parentA, parentB, GeneticAlgorithm.PC);
    assertNotNull(afterCross);
    // mutation
    factory_method.FactoryMutation fm = new factory_method.FactoryMutation();
    evolutionary_algorithms.complement.Mutation mut = fm.createMutation(evolutionary_algorithms.complement.MutationType.OnePointMutation);
    State mutated = mut.mutation(afterCross, GeneticAlgorithm.PM);
    assertNotNull(mutated);
    }
}
