package evolutionary_algorithms.complement;

import metaheurictics.strategy.Strategy;
import java.util.concurrent.ThreadLocalRandom;

import problem.definition.State;

public class UniformCrossover extends Crossover {
	
	/**
	 * Uses ThreadLocalRandom for algorithmic (non-cryptographic) randomness.
	 * This RNG is used for evolutionary algorithm operations (crossover point,
	 * selection between children, etc.) and is not used for security-sensitive
	 * purposes. Therefore we suppress the Sonar security hotspot S2245 here.
	 */
	@SuppressWarnings("squid:S2245")
	public int[] mascara(int length){
		int[] mascara = new int[length];
		for (int i = 0; i < mascara.length; i++) {
			int value = ThreadLocalRandom.current().nextInt(2);
			mascara[i] = value;
		}
		return mascara;
	}	
    
	@Override
	public State crossover(State father1, State father2, double PC) {
		Object value;
		State state = (State) father1.getCopy();
		int[] mascara = mascara(father1.getCode().size());
   		for (int k = 0; k < mascara.length; k++) {
   			if(mascara[k] == 1){
   				value = father1.getCode().get(k);
   				state.getCode().set(k, value);
   			}
   			else{
   				if(mascara[k] == 0){
   					value = father2.getCode().get(k);  
   					state.getCode().set(k, value);
   				}
   			}
		}
		return state;
	}
}
