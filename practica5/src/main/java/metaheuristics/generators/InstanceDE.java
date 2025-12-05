package metaheuristics.generators;

import factory_method.FactoryGenerator;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * InstanceDE - class that implements the Runnable interface to create an instance of
 *               the Distribution Estimation Algorithm generator.
 */
public class InstanceDE implements Runnable {

	private static final Logger LOGGER = Logger.getLogger(InstanceDE.class.getName());

	private boolean terminate = false;
    
	/**
	 * run - create an instance of the Distribution Estimation Algorithm generator.
	 */
	public void run() {
		FactoryGenerator ifFactoryGenerator = new FactoryGenerator();
		Generator generatorDE = null;
		try {
			generatorDE = ifFactoryGenerator.createGenerator(GeneratorType.DistributionEstimationAlgorithm);
		} catch (Exception e) {
			// Log exception instead of printing stack trace so debug info is available
			// but the code is safe for production.
			LOGGER.log(Level.SEVERE, "Failed to create DistributionEstimationAlgorithm generator", e);
		}
		boolean find = false;
		int i = 0;
		while (find == false) {
			if(MultiGenerator.getListGenerators()[i].getType().equals(GeneratorType.DistributionEstimationAlgorithm)){
				MultiGenerator.getListGenerators()[i] = generatorDE;
				find = true;
			}
			else i++;    
		}
		terminate = true;
	}

	/**
	 * isTerminate - check if the instance is terminated.
	 * @return true if terminated, false otherwise
	 */
	public boolean isTerminate() {
		return terminate;
	}

	/**
	 * setTerminate - set the termination status of the instance.
	 * @param terminate true to terminate, false to continue
	 */
	public void setTerminate(boolean terminate) {
		this.terminate = terminate;
	}
}
