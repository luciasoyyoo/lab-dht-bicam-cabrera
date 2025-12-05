package metaheuristics.generators;

import factory_method.FactoryGenerator;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * InstanceGA - class that implements the Runnable interface to create an instance of
 *               the Genetic Algorithm generator.
 */
public class InstanceGA implements Runnable {

	private static final Logger LOGGER = Logger.getLogger(InstanceGA.class.getName());

	private boolean terminate = false;
    
	/**
	 * run - create an instance of the Genetic Algorithm generator.
	 */
	public void run() {
		FactoryGenerator ifFactoryGenerator = new FactoryGenerator();
		Generator generatorGA = null;
		try {
			generatorGA = ifFactoryGenerator.createGenerator(GeneratorType.GeneticAlgorithm);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Failed to create GeneticAlgorithm generator", e);
		}
		boolean find = false;
		int i = 0;
		while (find == false) {
			if(MultiGenerator.getListGenerators()[i].getType().equals(GeneratorType.GeneticAlgorithm)){
				MultiGenerator.getListGenerators()[i] = generatorGA;
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
	 * setTerminate - determine the termination status of the instance.
	 * @param terminate true to terminate, false to continue
	 */
	public void setTerminate(boolean terminate) {
		this.terminate = terminate;
	}

}
