
package metaheuristics.generators;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;


import metaheurictics.strategy.Strategy;


import problem.definition.Problem.ProblemType;
import problem.definition.State;

/**
 * ParticleSwarmOptimization - class that implements the Particle Swarm Optimization metaheuristic.
 */
public class ParticleSwarmOptimization extends Generator {
	
	private State stateReferencePSO;
	private List<State> listStateReference = new ArrayList<State>(); 
	private List<Particle> listParticle =  new ArrayList<Particle> ();
	private GeneratorType generatorType;
	static int countParticle = 0;       // CANTIDAD DE PARTICULAS QUE SE HAN MOVIDO EN CADA CUMULO
	// Make these configurable (not final). Default to 0 (no swarms) to preserve existing test expectations.
	// Declaring them non-final prevents static analysis from treating loops as dead code while keeping
	// the runtime default behavior (0) the same as before.
	// non-public backing field for number of swarms; use accessors to read/write
	private static int coutSwarm = 0; // CANTIDAD DE CUMULOS
	/**
	 * getCoutSwarm - get the number of swarms.
	 * @return return the number of swarms
	 */
	public static int getCoutSwarm() {
		return coutSwarm;
	}
	/**
	 * setCoutSwarm - set the number of swarms.
	 * @param v the number of swarms to set
	 */
	public static void setCoutSwarm(int v) {
		if (v < 0) throw new IllegalArgumentException("coutSwarm must be >= 0");
		coutSwarm = v;
		// keep dependent countRef in sync
		setCountRef(coutSwarm * countParticleBySwarm);
	}
	// CANTIDAD DE PARTICULAS POR CUMULO - make non-public and provide accessors
	private static int countParticleBySwarm = 0;
	/**
	 * getCountParticleBySwarm - get the number of particles per swarm.
	 * @return the number of particles per swarm
	 */
	public static int getCountParticleBySwarm() {
		return countParticleBySwarm;
	}
	/**
	 * setCountParticleBySwarm - set the number of particles per swarm.
	 * @param v the number of particles per swarm to set
	 */
	public static void setCountParticleBySwarm(int v) {
		if (v < 0) throw new IllegalArgumentException("countParticleBySwarm must be >= 0");
		countParticleBySwarm = v;
		// keep dependent countRef in sync
		setCountRef(coutSwarm * countParticleBySwarm);
	}
	private static int countRef = coutSwarm * countParticleBySwarm;            // CANTIDAD DE PARTICULAS TOTAL = coutSwarm * countParticleBySwarm
	private float weight = 50;
	public static final double wmax = 0.9;
	public static final double wmin = 0.2;
	public static final int learning1 = 2, learning2 = 2;
	static double constriction;
	public static final boolean binary = false;
	static State[] lBest; 
	static State gBest;
	static int countCurrentIterPSO;

	// Accessors to avoid direct writes to static fields from instance methods.
	// Defensive copies are used to avoid exposing internal mutable state.
	public static State[] getLBest() {
		if (lBest == null) return new State[0];
		State[] copy = new State[lBest.length];
		for (int i = 0; i < lBest.length; i++) {
			copy[i] = (lBest[i] == null) ? null : lBest[i].clone();
		}
		return copy;
	}
	/**
	 * setLBest - set the local best array.
	 * @param arr
	 */
	public static void setLBest(State[] arr) {
		if (arr == null) {
			lBest = null;
			return;
		}
		lBest = new State[arr.length];
		for (int i = 0; i < arr.length; i++) {
			lBest[i] = (arr[i] == null) ? null : arr[i].clone();
		}
	}
	/**
	 * setLBest - set the local best at index.
	 * @param idx
	 * @param s
	 */
	public static void setLBestAt(int idx, State s) {
		if (lBest == null) return;
		if (idx < 0 || idx >= lBest.length) return;
		lBest[idx] = (s == null) ? null : s.clone();
	}
	public static State getGBest() { return gBest; }
	public static void setGBest(State s) { gBest = s; }
	public static int getCountCurrentIterPSO() { return countCurrentIterPSO; }
	public static void setCountCurrentIterPSO(int v) { countCurrentIterPSO = v; }
	public static int getCountParticle() { return countParticle; }
	public static void setCountParticle(int v) { countParticle = v; }
	//problemas dinamicos
	//problemas dinamicos: use instance fields from Generator (countGender, countBetterGender)
    private int[] listCountBetterGender = new int[10];
    private int[] listCountGender = new int[10];
    private float[] listTrace = new float[1200000];
			
	/**
	 * Create a new ParticleSwarmOptimization instance.
	 *
	 * The constructor initializes internal arrays and sets default values
	 * for weights, counters and generators. It intentionally does not
	 * perform expensive operations; heavy initialization is deferred to
	 * {@link #getListStateRef()} when needed.
	 */
	public ParticleSwarmOptimization(){
		super();
	this.setListParticle(getListStateRef()); 
		this.generatorType = GeneratorType.ParticleSwarmOptimization;
		this.weight = 50;
		setLBest(new State[coutSwarm]);
		if(!listParticle.isEmpty()){
			setCountCurrentIterPSO(1);
			inicialiceLBest();
			setGBest(gBestInicial());
		}
		setCountParticle(0);
		listTrace[0] = this.weight;
		listCountBetterGender[0] = 0;
		listCountGender[0] = 0;
	}

	@Override
	/**
	 * Generate a new state by advancing the next particle in the swarm.
	 *
	 * @param operatornumber unused for PSO (kept for API compatibility)
	 * @return the newly generated State produced by the advanced particle
	 * @throws ReflectiveOperationException forwarded from nested generator calls
	 */
	public State generate(Integer operatornumber) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException { // PSO
		if (getCountParticle() >= countRef) {
			setCountParticle(0);
		}
		// Advance the current particle and return its active state.
		listParticle.get(getCountParticle()).generate(1);
		return listParticle.get(getCountParticle()).getStateActual();
	}
   	
	/**
	 * Initialize the local best {@code lBest} array for each swarm.
	 *
	 * The method inspects particle personal-bests within each swarm and
	 * selects the best among them according to the problem's objective
	 * type (maximize or minimize).
	 */
	public void inicialiceLBest (){
		for (int j = 0; j < coutSwarm; j++) {
			// pick initial reference from the particle's personal best
			State reference = listParticle.get(getCountParticle()).getStatePBest();
			int iterator = countParticleBySwarm + getCountParticle();
			if(Strategy.getStrategy().getProblem().getTypeProblem().equals(ProblemType.Maximizar)){
				for (int i = countParticle; i < iterator; i++) {
					if (listParticle.get(i).getStatePBest().getEvaluation().get(0) > reference.getEvaluation().get(0))
						reference = listParticle.get(i).getStatePBest();
					setCountParticle(getCountParticle() + 1);
				}
			}
			else{
				for (int i = countParticle; i < iterator; i++) {
					if (listParticle.get(i).getStatePBest().getEvaluation().get(0) < reference.getEvaluation().get(0))
						reference = listParticle.get(i).getStatePBest();
					setCountParticle(getCountParticle() + 1);
				}
			}
			
			lBest[j] = reference;
		}
	}
	
	
	@Override
	/**
	 * Return the single reference state used by single-reference generators.
	 *
	 * PSO manages multiple references (per-swarm and global), so this
	 * method returns {@code null} by design in the current implementation.
	 *
	 * @return null in this PSO implementation
	 */
	public State getReference() {
		return null;
	}
	
	/**
	 * Build or return the list of {@link Particle} instances used by PSO.
	 *
	 * If an existing PSO generator instance has an initialized particle
	 * list, that list is reused. Otherwise, states from
	 * {@link RandomSearch#listStateReference} are converted into particles.
	 *
	 * @return list of particles representing the swarm population
	 */
	private List<Particle> getListStateRef() {
		Boolean found = false;
		List<String> key = Strategy.getStrategy().getListKey();
		int count = 0;
		if(RandomSearch.listStateReference.size() == 0){
			return this.setListParticle(new ArrayList<Particle>());
		}
		while((found.equals(false)) && (Strategy.getStrategy().mapGenerators.size() > count)){
			//recorrer la lista de generadores, hasta que encuentre el PSO
			if(key.get(count).equals(GeneratorType.ParticleSwarmOptimization.toString())){
				//creo el generador PSO, y si su lista de particulas esta vacia entonces es la primera vez que lo estoy creando, y cada estado lo convierto en particulas
				GeneratorType keyGenerator = GeneratorType.valueOf(String.valueOf(key.get(count)));
				ParticleSwarmOptimization generator = (ParticleSwarmOptimization) Strategy.getStrategy().mapGenerators.get(keyGenerator);
				if(generator.getListParticle().isEmpty()){
					//convertir los estados en particulas
					for (int j = 0; j < RandomSearch.listStateReference.size(); j++) {
						//si el estado es creado con el generator RandomSearch entonces la convierto en particula
						if(getListParticle().size() != countRef){
							ArrayList<Object> velocity = new ArrayList<Object>();
							State stateAct = (State) RandomSearch.listStateReference.get(j).getCopy();
							stateAct.setCode(new ArrayList<Object>(RandomSearch.listStateReference.get(j).getCode()));
							stateAct.setEvaluation(RandomSearch.listStateReference.get(j).getEvaluation());
							
							State statePBest = (State) RandomSearch.listStateReference.get(j).getCopy();
							statePBest.setCode(new ArrayList<Object>(RandomSearch.listStateReference.get(j).getCode()));
							statePBest.setEvaluation(RandomSearch.listStateReference.get(j).getEvaluation());
							
							Particle particle = new Particle(stateAct, statePBest, velocity);
							getListParticle().add(particle);
						}
					}  
				}
				else{
					setListParticle(generator.getListStateReference());
				}
			        found = true;
			}
			count++;
		}
		return getListParticle();
	}


	/**
	 * Return the PSO-specific reference state used by some helpers.
	 *
	 * @return stored stateReferencePSO (may be null)
	 */
	public State getStateReferencePSO() {
		return stateReferencePSO;
	}

	/**
	 * Set the PSO-specific reference state.
	 *
	 * A defensive copy is not performed because {@link State} provides its
	 * own copy/clone methods; callers should clone if they want isolation.
	 *
	 * @param stateReferencePSO the state to store as PSO reference
	 */
	public void setStateReferencePSO(State stateReferencePSO) {
		this.stateReferencePSO = stateReferencePSO;
	}

	/**
	 * Return the current list of particles used internally.
	 *
	 * The returned list is the internal storage reference; callers that need
	 * mutability isolation should copy it.
	 *
	 * @return internal list of particles
	 */
	public List<Particle> getListStateReference() {
		return this.getListParticle();
	}

	/**
	 * Replace the internal state reference list. Provided for API
	 * compatibility; most callers should use {@link #setListParticle} instead.
	 *
	 * @param listStateReference new list of states
	 */
	public void setListStateReference(List<State> listStateReference) {
		this.listStateReference = listStateReference;
	}

	/**
	 * Get the internal particle list.
	 *
	 * @return internal list of {@link Particle} instances
	 */
	public List<Particle> getListParticle() {
		return listParticle;
	}

	/**
	 * Replace the internal particle list and return the newly set list.
	 *
	 * @param listParticle list to install
	 * @return the installed list
	 */
	public List<Particle> setListParticle(List<Particle> listParticle) {
		this.listParticle = listParticle;
		return listParticle;
	}

	/**
	 * Get the generator type associated with this instance.
	 *
	 * @return generator type (ParticleSwarmOptimization)
	 */
	public GeneratorType getGeneratorType() {
		return generatorType;
	}

	/**
	 * Set the generator type for this instance.
	 *
	 * @param generatorType generator type to set
	 */
	public void setGeneratorType(GeneratorType generatorType) {
		this.generatorType = generatorType;
	}

	public static int getCountRef() {
		return countRef;
	}

	public static void setCountRef(int countRef) {
		ParticleSwarmOptimization.countRef = countRef;
	}


	//*****************************************
	@Override
	/**
	 * Update the PSO internal references based on a newly generated
	 * candidate state. This method updates the particle personal-bests
	 * and possibly the local/global bests depending on the objective.
	 *
	 * @param stateCandidate newly generated candidate state
	 * @param countIterationsCurrent current iteration counter
	 * @throws ReflectiveOperationException forwarded from nested calls
	 */
	public void updateReference(State stateCandidate,Integer countIterationsCurrent) throws IllegalArgumentException,SecurityException, ClassNotFoundException, InstantiationException,IllegalAccessException, InvocationTargetException,NoSuchMethodException {
		Particle particle = listParticle.get(countParticle);
		int swarm = countParticle/countParticleBySwarm;
		if(Strategy.getStrategy().getProblem().getTypeProblem().equals(ProblemType.Maximizar)){
				if ((lBest[swarm]).getEvaluation().get(0) < particle.getStatePBest().getEvaluation().get(0)){
					setLBestAt(swarm, particle.getStatePBest());
				if(lBest[swarm].getEvaluation().get(0) > getReferenceList().get(getReferenceList().size() - 1).getEvaluation().get(0)){
						State tmp = new State();
						tmp.setCode(new ArrayList<Object>(lBest[swarm].getCode()));
						tmp.setEvaluation(lBest[swarm].getEvaluation());
						tmp.setTypeGenerator(lBest[swarm].getTypeGenerator());
						setGBest(tmp);
				}
			}
		}
		else {
			particle.updateReference(stateCandidate, countIterationsCurrent);
				if ((lBest[swarm]).getEvaluation().get(0) > particle.getStatePBest().getEvaluation().get(0)){
					setLBestAt(swarm, particle.getStatePBest());
				if(lBest[swarm].getEvaluation().get(0) < getReferenceList().get(getReferenceList().size() - 1).getEvaluation().get(0)){
						State tmp = new State();
						tmp.setCode(new ArrayList<Object>(lBest[swarm].getCode()));
						tmp.setEvaluation(lBest[swarm].getEvaluation());
						tmp.setTypeGenerator(lBest[swarm].getTypeGenerator());
						setGBest(tmp);
				}
			}
		}
	listStateReference.add(getGBest());
	setCountParticle(getCountParticle() + 1);
	setCountCurrentIterPSO(getCountCurrentIterPSO() + 1);
	}
	
	/**
	 * Compute the initial global best (gBest) from the local bests
	 * contained in {@code lBest}.
	 *
	 * @return the best State among {@code lBest}
	 */
	public State gBestInicial (){
		State stateBest = lBest[0];
		for (int i = 1; i < lBest.length; i++) {
			if(Strategy.getStrategy().getProblem().getTypeProblem().equals(ProblemType.Maximizar)){
				if (lBest[i].getEvaluation().get(0) > stateBest.getEvaluation().get(0)){
					stateBest = lBest[i];
				}
			}
			else{
				if (lBest[i].getEvaluation().get(0) < stateBest.getEvaluation().get(0)){
					stateBest = lBest[i];
				}
			}
		}
		return stateBest;
	}

	@Override
	/**
	 * Set the initial reference(s) for the generator. PSO uses swarm
	 * references and therefore this method is intentionally left as a
	 * no-op or to be implemented by callers that bridge different data.
	 *
	 * @param stateInitialRef initial state to use as a reference
	 */
	public void setInitialReference(State stateInitialRef) {
		// Intentionally left blank for PSO (no single initial reference)
	}

	@Override
	/**
	 * Return the concrete generator type of this generator.
	 *
	 * @return the {@link GeneratorType} for this generator
	 */
	public GeneratorType getType() {
		return this.generatorType;
	}

	@Override
	/**
	 * Return a copy of the list used as reference solutions by PSO.
	 *
	 * @return copy of internal reference list
	 */
	public List<State> getReferenceList() {
		return new ArrayList<State>(this.listStateReference);
	}

	@Override
	/**
	 * Return the currently generated offspring/state list for this
	 * generator. PSO does not maintain a son list in the classical sense
	 * and thus returns {@code null}.
	 *
	 * @return null for PSO
	 */
	public List<State> getSonList() {
		return null;
	}

	@Override
	/**
	 * Decide whether the provided candidate should update references.
	 *
	 * PSO uses {@code updateReference} directly; this helper returns
	 * {@code false} by default and can be implemented if integration
	 * requires it.
	 *
	 * @param stateCandidate candidate state to evaluate
	 * @return {@code false} by default
	 */
	public boolean awardUpdateREF(State stateCandidate) {
		return false;
	}

	@Override
	/**
	 * Set the internal inertia weight used by particle velocity updates.
	 *
	 * @param weight inertia weight value
	 */
	public void setWeight(float weight) {
		this.weight = weight;
	}

	@Override
	/**
	 * Get the current inertia weight used in PSO.
	 *
	 * @return current weight value
	 */
	public float getWeight() {
		return this.weight;
	}

	@Override
	/**
	 * Return a defensive copy of the counters that track how many times a
	 * particle improved during a period.
	 *
	 * @return copy of listCountBetterGender
	 */
	public int[] getListCountBetterGender() {
		return (this.listCountBetterGender == null) ? new int[0] : Arrays.copyOf(this.listCountBetterGender, this.listCountBetterGender.length);
	}

	@Override
	/**
	 * Return a defensive copy of usage counters per period.
	 *
	 * @return copy of listCountGender
	 */
	public int[] getListCountGender() {
		return (this.listCountGender == null) ? new int[0] : Arrays.copyOf(this.listCountGender, this.listCountGender.length);
	}

	@Override
	/**
	 * Return a copy of the internal trace array used for diagnostic
	 * information (weights over time, etc.).
	 *
	 * @return copy of listTrace
	 */
	public float[] getTrace() {
		return (this.listTrace == null) ? new float[0] : Arrays.copyOf(this.listTrace, this.listTrace.length);
	}


	
}
