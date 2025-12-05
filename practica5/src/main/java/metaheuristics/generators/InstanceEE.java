package metaheuristics.generators;

import factory_method.FactoryGenerator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * InstanceEE - class that implements the Runnable interface to create an instance of
 */
public class InstanceEE implements Runnable {

	private static final Logger LOGGER = Logger.getLogger(InstanceEE.class.getName());

	private boolean terminate = false;
	
	/**
	 * run - create an instance of the Evolution Strategies generator.
	 */
	public void run() {
		FactoryGenerator ifFactoryGenerator = new FactoryGenerator();
		Generator generatorEE = null;
			try {
				generatorEE = ifFactoryGenerator.createGenerator(GeneratorType.EvolutionStrategies);
			} catch (Exception e) {
				// Log exception instead of printing stack trace to avoid debug output in production
				LOGGER.log(Level.SEVERE, "Failed to create EvolutionStrategies generator", e);
		}
		boolean find = false;
		int i = 0;
		while (find == false) {
			if(MultiGenerator.getListGenerators()[i].getType().equals(GeneratorType.EvolutionStrategies)){
				MultiGenerator.getListGenerators()[i] = generatorEE;
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
