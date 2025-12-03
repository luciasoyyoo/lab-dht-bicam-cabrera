package testutils;

import metaheurictics.strategy.Strategy;
import metaheuristics.generators.Generator;
import metaheuristics.generators.GeneratorType;
import problem.definition.Problem;
import problem.definition.State;

import java.util.ArrayList;

public class TestHelper {

    public static Problem createMinimalProblemWithCodification(int variableCount) {
        MinimalCodification cod = new MinimalCodification(variableCount);
        MinimalProblem p = new MinimalProblem(cod);
        p.setFunction(new ArrayList<>());
        p.setTypeProblem(Problem.ProblemType.Maximizar);
        p.setOperator(new MinimalOperator());
        State base = new State();
        base.setCode(new ArrayList<Object>(){{add(0);} });
        p.setState(base);
        return p;
    }

    public static void initStrategyWithProblem(Problem p) {
        Strategy.getStrategy().mapGenerators = new java.util.TreeMap<GeneratorType, Generator>();
        Strategy.getStrategy().setProblem(p);
    }
}
