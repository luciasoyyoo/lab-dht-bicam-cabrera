package local_search.complement;

import factory_interface.IFFactoryGenerator;
import factory_method.FactoryGenerator;

import java.lang.reflect.InvocationTargetException;

import metaheurictics.strategy.Strategy;
import metaheuristics.generators.DistributionEstimationAlgorithm;
import metaheuristics.generators.EvolutionStrategies;
import metaheuristics.generators.GeneratorType;
import metaheuristics.generators.GeneticAlgorithm;
import metaheuristics.generators.ParticleSwarmOptimization;


/**
 * UpdateParameter - utility that updates generator parameters during search.
 *
 * @brief Responsible for incrementing an iteration counter and switching the
 *        active generator implementation in the global Strategy when certain
 *        iteration thresholds are reached.
 */
public class UpdateParameter {
    
	private static IFFactoryGenerator ifFactoryGenerator;
    
	/**
	 * Increment the provided iteration counter and, when thresholds for
	 * different generators are reached, replace the Strategy's generator with
	 * the corresponding implementation.
	 *
	 * Common use: called at each iteration to advance the count and rotate
	 * the generator when configured counts are reached.
	 *
	 * @param countIterationsCurrent current iteration counter
	 * @return incremented iteration counter
	 * @throws ReflectiveOperationException when reflective generator creation fails
	 */
	public static Integer updateParameter(Integer countIterationsCurrent) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {//HashMap<String, Object> map, 
		countIterationsCurrent = countIterationsCurrent + 1;
		//        Here update parameter for update and change generator.
		if(countIterationsCurrent.equals(GeneticAlgorithm.countRef - 1)){
			ifFactoryGenerator = new FactoryGenerator();
			Strategy.getStrategy().generator = ifFactoryGenerator.createGenerator(GeneratorType.GeneticAlgorithm);
		}
		else{
			if(countIterationsCurrent.equals(EvolutionStrategies.countRef - 1)){
				ifFactoryGenerator = new FactoryGenerator();
				Strategy.getStrategy().generator = ifFactoryGenerator.createGenerator(GeneratorType.EvolutionStrategies);
			}           
			if(countIterationsCurrent.equals(DistributionEstimationAlgorithm.countRef - 1)){
				ifFactoryGenerator = new FactoryGenerator();
				Strategy.getStrategy().generator = ifFactoryGenerator.createGenerator(GeneratorType.DistributionEstimationAlgorithm);
			}
			if(countIterationsCurrent.equals(ParticleSwarmOptimization.getCountRef() - 1)){
				ifFactoryGenerator = new FactoryGenerator();
				Strategy.getStrategy().generator = ifFactoryGenerator.createGenerator(GeneratorType.ParticleSwarmOptimization);
			}
		}
		return countIterationsCurrent;
	}
}
	


