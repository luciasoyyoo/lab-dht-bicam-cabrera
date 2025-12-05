package metaheuristics.generators;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import metaheurictics.strategy.Strategy;

import problem.definition.Problem.ProblemType;
import problem.definition.State;

/**
 * Particle - class that represents a particle in the Particle Swarm Optimization algorithm.
 */
public class Particle extends Generator {

	private State statePBest;
	private State stateActual;
	private ArrayList<Object> velocity;
	
	
	/**
	 * Particle - method constructor.
	 */
	public Particle() {
		super();
		this.stateActual = new State();
		this.statePBest = new State();
		this.velocity = new ArrayList<Object>();
	}
	
	/**
	 * Particle - method constructor.
	 * @param statePBest 
	 * @param stateActual 
	 * @param velocity 
	 */
	public Particle(State statePBest, State stateActual, ArrayList<Object> velocity) {
		super();
		this.statePBest = statePBest;
		this.stateActual = stateActual;
		this.velocity = velocity;
	}

	/**
	 * getVelocity - get the velocity of the particle.
	 * @return the velocity of the particle
	 */
	public ArrayList<Object> getVelocity() {
		return velocity;
	}

	/**
	 * setVelocity - set the velocity of the particle.
	 * @param velocity the velocity to set 
	 */
	public void setVelocity(ArrayList<Object> velocity) {
		this.velocity = velocity;
	}

	/**
	 * getStatePBest - get the personal best state of the particle.
	 * @return the personal best state of the particle
	 */
	public State getStatePBest() {
		return statePBest;
	}

	/**
	 * setStatePBest - set the personal best state of the particle.
	 * @param statePBest the personal best state to set
	 */
	public void setStatePBest(State statePBest) {
		this.statePBest = statePBest;
	}

	/**
	 * getStateActual - get the current state of the particle.
	 * @return the current state of the particle
	 */
	public State getStateActual() {
		return stateActual;
	}

	/**
	 * setStateActual - set the current state of the particle.
	 * @param stateActual the current state to set
	 */
	public void setStateActual(State stateActual) {
		this.stateActual = stateActual;
	}
	/**
	 * generate - generate a new state for the particle.
	 * @param operatornumber
	 * @return
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	@Override
	public State generate(Integer operatornumber)
			throws IllegalArgumentException, SecurityException,
			ClassNotFoundException, InstantiationException,
			IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		// TODO Auto-generated method stub
		
		ArrayList<Object> actualVelocity = UpdateVelocity();
		ArrayList<Object> newCode = UpdateCode(actualVelocity);
		this.velocity = actualVelocity;
		this.stateActual.setCode(newCode);
		return null;
	}
	
	
	/**
	 * Uses ThreadLocalRandom for algorithmic (non-cryptographic) randomness.
	 * This RNG updates particle velocity and is not security-sensitive.
	 * Suppress Sonar hotspot S2245 for this usage.
	 */
	@SuppressWarnings("squid:S2245")
	private ArrayList<Object> UpdateVelocity(){ // actualizar velocidad
    	double w = ParticleSwarmOptimization.wmax - ((ParticleSwarmOptimization.wmax - ParticleSwarmOptimization.wmin) / Strategy.getStrategy().getCountMax()) * ParticleSwarmOptimization.getCountCurrentIterPSO();  //CALCULO DE LA INERCIA
		double rand1 = ThreadLocalRandom.current().nextDouble();
		double rand2 = ThreadLocalRandom.current().nextDouble();
    	double inertia, cognitive, social;
    	int learning = ParticleSwarmOptimization.learning1 + ParticleSwarmOptimization.learning2; // ratios de aprendizaje cognitivo y social
    	ParticleSwarmOptimization.constriction = 2/(Math.abs(2 - learning-Math.sqrt((learning * learning)- 4 * learning)));   // Factor de costriccion
    	ArrayList<Object> actualVelocity = new ArrayList<Object>();
    	if(velocity.isEmpty()){
    		for (int i = 0; i < Strategy.getStrategy().getProblem().getState().getCode().size(); i++){
    			velocity.add(0.0);
    		}
    	}
    	// recorre el vector velocidad y lo actualiza
    	for (int i = 0; i < Strategy.getStrategy().getProblem().getState().getCode().size(); i++) {  
    		// cumulo donde se encuentra la particula
				int swarm = ParticleSwarmOptimization.getCountParticle() / ParticleSwarmOptimization.getCountParticleBySwarm(); 
           	inertia = w * (Double)velocity.get(i);  
           	if(ParticleSwarmOptimization.binary == true){
           		cognitive = (Double)(ParticleSwarmOptimization.learning1 * rand1 * ((Integer)(this.statePBest.getCode().get(i)) - (Integer)(stateActual.getCode().get(i))));
           		social = (Double)(ParticleSwarmOptimization.learning2 * rand2 * (((Integer)(((State) ParticleSwarmOptimization.lBest[swarm]).getCode().get(i))) - ((Integer)(stateActual.getCode().get(i)))));
           	}
           	else{
           		cognitive = (Double)(ParticleSwarmOptimization.learning1 * rand1 * ((Double)(this.statePBest.getCode().get(i)) - (Double)(stateActual.getCode().get(i))));
           		social = (Double)(ParticleSwarmOptimization.learning2 * rand2 * (((Double)(((State) ParticleSwarmOptimization.lBest[swarm]).getCode().get(i))) - ((Double)(stateActual.getCode().get(i)))));
           	}
        	actualVelocity.add(ParticleSwarmOptimization.constriction*(inertia + cognitive + social));
        }
 
        return actualVelocity;
    }
	
	/**
	 * Uses ThreadLocalRandom for algorithmic (non-cryptographic) randomness
	 * when computing binary code updates. Not security-sensitive; suppress S2245.
	 */
	@SuppressWarnings("squid:S2245")
	private ArrayList<Object> UpdateCode(ArrayList<Object> actualVelocity) {  // CALCULO DE LA NUEA POSICION DE LA PARTICULA
    	ArrayList<Object> newCode = new ArrayList<Object>();
		//poner la condicion de si se esta trabajando con valores continuos o binarios
		if(ParticleSwarmOptimization.binary == false){	
			for (int i = 0; i < stateActual.getCode().size(); i++) {
					newCode.add( (Double)(stateActual.getCode().get(i)) + (Double)(actualVelocity.get(i)) );
		    }
			return newCode;
	    }
		 else{                                                  //c�lculo de la posicion para codificacion binaria
			ArrayList<Object> binaryCode = new ArrayList<Object>();
						for (int i = 0; i < stateActual.getCode().size(); i++){
							double rand = ThreadLocalRandom.current().nextDouble();
			  double s = 1/(1 + 1.72 * (Double)(actualVelocity.get(i))); // 
			  if (rand < s){
			     binaryCode.add(1);
			  }
			   else{
			     	binaryCode.add(0);
			     	}
			  }
	          return binaryCode;
		}
		
	}
	
	/**
	 * updateReference - update the reference state and the personal best state of the particle.
	 * @param stateCandidate
	 * @param countIterationsCurrent
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	@Override
	public void updateReference(State stateCandidate,
			Integer countIterationsCurrent) throws IllegalArgumentException,
			SecurityException, ClassNotFoundException, InstantiationException,
			IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		// TODO Auto-generated method stub
		if(Strategy.getStrategy().getProblem().getTypeProblem().equals(ProblemType.Maximizar)){
			if(stateActual.getEvaluation().get(0) > statePBest.getEvaluation().get(0)){
				statePBest.setCode(new ArrayList<Object>(stateActual.getCode()));
				statePBest.setEvaluation(stateActual.getEvaluation());
			}
		}
		else{
			if(stateCandidate.getEvaluation().get(0) < statePBest.getEvaluation().get(0)){
				statePBest.setCode(new ArrayList<Object>(stateCandidate.getCode()));
				statePBest.setEvaluation(stateCandidate.getEvaluation());
			}
		}
		
	}

	@Override
	/**
	 * getReference - get the reference state.
	 * @return reference state
	 */
	public State getReference() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	/**
	 * setInitialReference - set the initial reference state for the particle.
	 * @param stateInitialRef 
	 */
	public void setInitialReference(State stateInitialRef) {
		// TODO Auto-generated method stub

	}

	@Override
	/**
	 * getType - get the type of the generator.
	 * @return return the type of the generator
	 */
	public GeneratorType getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	/**
	 * getReferenceList - get the list of reference states.
	 * @return list of reference states
	 */
	public List<State> getReferenceList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	/**
	 * getSonList - get the list of son states.
	 * @return list of son states
	 */
	public List<State> getSonList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	/**
	 * awardUpdateREF - award the update of the reference state.
	 * @param stateCandidate 
	 * @return  return true if the reference state was updated, false otherwise
	 */
	public boolean awardUpdateREF(State stateCandidate) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	/**
	 * setWeight - set the weight of the particle.
	 * @param weight 
	 */
	public void setWeight(float weight) {
		// TODO Auto-generated method stub

	}

	@Override
	/**
	 * getWeight - get the weight of the particle.
	 * @return return the weight of the particle
	 */
	public float getWeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	/**
	 * getTrace - get the trace of the particle.
	 * @return return the trace of the particle
	 */
	public float[] getTrace() {
		// TODO Auto-generated method stub
		return new float[0];
	}

	@Override
	/**
	 * getListCountBetterGender - get the list of counts of better genders.
	 * @return list of counts of better genders
	 */
	public int[] getListCountBetterGender() {
		// TODO Auto-generated method stub
		return new int[0];
	}

	@Override
	/**
	 * getListCountGender - get the list of counts of genders.
	 * @return list of counts of genders
	 */
	public int[] getListCountGender() {
		// TODO Auto-generated method stub
		return new int[0];
	}

}
