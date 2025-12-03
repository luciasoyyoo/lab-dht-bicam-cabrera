package factory_method;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import evolutionary_algorithms.complement.Crossover;
import evolutionary_algorithms.complement.CrossoverType;
import problem.definition.Codification;
import problem.definition.Problem;
import problem.definition.State;
import metaheurictics.strategy.Strategy;

public class FactoryCrossoverIntegrationTest {

    @Test
    public void testCreateAndExecuteOnePointCrossover() throws Exception {
        // prepare minimal problem with codification
        Problem problem = new Problem();
        problem.setCodification(new Codification() {
            @Override
            public boolean validState(State state) { return true; }

            @Override
            public Object getVariableAleatoryValue(int key) { return null; }

            @Override
            public int getAleatoryKey() { return 0; }

            @Override
            public int getVariableCount() { return 3; }
        });

        // set problem into global Strategy singleton so OnePointCrossover can query variable count
        Strategy.getStrategy().setProblem(problem);

        // create two simple states with 3 code elements
        State s1 = new State();
        ArrayList<Object> c1 = new ArrayList<>();
        c1.add(1); c1.add(2); c1.add(3);
        s1.setCode(c1);

        State s2 = new State();
        ArrayList<Object> c2 = new ArrayList<>();
        c2.add(4); c2.add(5); c2.add(6);
        s2.setCode(c2);

        FactoryCrossover fc = new FactoryCrossover();
        Crossover cross = fc.createCrossover(CrossoverType.OnePointCrossover);
        assertNotNull(cross);

        State out = cross.crossover(s1, s2, 1.0);
        assertNotNull(out);
        // result should have same code length and contain three elements
        assertEquals(3, out.getCode().size());
    }
}
