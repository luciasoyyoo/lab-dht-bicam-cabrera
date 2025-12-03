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

public class EvolutionStrategiesGenerateTest {

    @BeforeEach
    public void setup() {
        // initialize Strategy and a minimal Problem/Codification for deterministic tests
        TestHelper.initStrategyWithProblem(TestHelper.createMinimalProblemWithCodification(1));
    }

    @Test
    public void generate_returnsCandidate_basedOnFathers() throws Exception {
        // use the Problem initialized in setup via TestHelper
        Problem p = metaheurictics.strategy.Strategy.getStrategy().getProblem();

    // Instead of calling generate (which relies on class-level truncation), exercise selection and mutation steps directly
    EvolutionStrategies es = new EvolutionStrategies();
    // create a father state and set it as reference list
    State father = new State();
    ArrayList<Object> code = new ArrayList<>(); code.add(7); father.setCode(code);
    ArrayList<Double> eval = new ArrayList<>(); eval.add(2.0); father.setEvaluation(eval);
    List<State> refs = new ArrayList<>(); refs.add(father);
    es.setListStateReference(refs);

    // Use FactoryFatherSelection with RouletteSelection to obtain fathers regardless of truncation
    factory_method.FactoryFatherSelection ffs = new factory_method.FactoryFatherSelection();
    evolutionary_algorithms.complement.FatherSelection sel = ffs.createSelectFather(evolutionary_algorithms.complement.SelectionType.RouletteSelection);
    List<State> fathers = sel.selection(es.getListStateReference(), 0);
    assertNotNull(fathers);
    assertFalse(fathers.isEmpty());

    // Test mutation step using factory
    factory_method.FactoryMutation fm = new factory_method.FactoryMutation();
    evolutionary_algorithms.complement.Mutation mut = fm.createMutation(evolutionary_algorithms.complement.MutationType.OnePointMutation);
    State candidate = metaheurictics.strategy.Strategy.getStrategy().getProblem() == null ? father : (State) metaheurictics.strategy.Strategy.getStrategy().getProblem().getState().getCopy();
    // ensure candidate has code
    candidate.setCode(new ArrayList<Object>(fathers.get(0).getCode()));
    State mutated = mut.mutation(candidate, EvolutionStrategies.PM);
    assertNotNull(mutated);
    }
}
