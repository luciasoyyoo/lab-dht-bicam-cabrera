package evolutionary_algorithms.complement;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import metaheurictics.strategy.Strategy;

import problem.definition.State;


public class OnePointCrossover extends Crossover {

	/**
	 * Uses ThreadLocalRandom for algorithmic (non-cryptographic) randomness.
	 * This RNG is used for evolutionary algorithm operations (crossover point,
	 * selection between children, etc.) and is not used for security-sensitive
	 * purposes. Therefore we suppress the Sonar security hotspot S2245 here.
	 */
	@SuppressWarnings("squid:S2245")
	@Override
	public State crossover(State father1, State father2, double PC) {
				
		State newInd = (State) father1.getCopy();
		
	    List<Object> ind1 = new ArrayList<Object>();
	    List<Object> ind2 = new ArrayList<Object>();
	    
		double number = ThreadLocalRandom.current().nextDouble(); // thread-safe, [0.0, 1.0)
		if(number <= PC){
			//llenar los valores de cada hijo
			int bound = Strategy.getStrategy().getProblem().getCodification().getVariableCount();
			int pos = (bound > 0) ? ThreadLocalRandom.current().nextInt(bound) : 0;
			for (int i = 0; i < father1.getCode().size(); i++) {
				if(i <= pos){
					ind1.add(father1.getCode().get(i));
					ind2.add(father2.getCode().get(i));
				}
				else{
					ind1.add(father2.getCode().get(i));
					ind2.add(father1.getCode().get(i));
				}
			}
			/*
			for (int j = pos; j < ind1.size(); j++) {
		       for (int k = pos; k < ind2.size(); k++) {
				  Integer value1 = (Integer) ind1.get(j);
		    	  ind1.set(j, ind2.get(k));
				  ind2.set(k,value1);
			   }				  
		    }*/
			//generar un numero aleatorio 0 o 1, si es 0 me quedo con ind1 si es 1 con ind2.
			int random = ThreadLocalRandom.current().nextInt(2);
			if(random == 0)
				newInd.setCode((ArrayList<Object>) ind1);
			else newInd.setCode((ArrayList<Object>) ind2); 
		}
		return newInd;			
	}
	
	
}
