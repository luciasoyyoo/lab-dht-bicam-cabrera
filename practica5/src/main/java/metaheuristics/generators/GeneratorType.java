package metaheuristics.generators;

/**
 * GeneratorType - enumeration of different types of solution generators used in metaheuristic algorithms.
 */
public enum GeneratorType {
	HillClimbing, TabuSearch, SimulatedAnnealing, RandomSearch, LimitThreshold, HillClimbingRestart, //un punto
	GeneticAlgorithm, EvolutionStrategies, DistributionEstimationAlgorithm, ParticleSwarmOptimization, //poblaciones de puntos
	MultiGenerator, 
	MultiobjectiveTabuSearch, MultiobjectiveStochasticHillClimbing,MultiCaseSimulatedAnnealing, MultiobjectiveHillClimbingRestart, MultiobjectiveHillClimbingDistance; //mutiobjetivos
}
