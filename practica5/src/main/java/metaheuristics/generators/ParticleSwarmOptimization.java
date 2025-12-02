package metaheuristics.generators;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;


import metaheurictics.strategy.Strategy;


import problem.definition.Problem.ProblemType;
import problem.definition.State;

public class ParticleSwarmOptimization extends Generator {
	
	private State stateReferencePSO;
	private List<State> listStateReference = new ArrayList<State>(); 
	private List<Particle> listParticle =  new ArrayList<Particle> ();
	private GeneratorType generatorType;
	static int countParticle = 0;       // CANTIDAD DE PARTICULAS QUE SE HAN MOVIDO EN CADA CUMULO
	public static final int coutSwarm = 0;           //CANTIDAD DE CUMULOS
	public static final int countParticleBySwarm = 0; //CANTIDAD DE PARTICULAS POR CUMULO
	private static int countRef = coutSwarm * countParticleBySwarm;            // CANTIDAD DE PARTICULAS TOTAL = coutSwarm * countParticleSwarm
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
			
	public ParticleSwarmOptimization(){
		super();
	this.setListParticle(getListStateRef()); 
//		listStateReference = new ArrayList<State>(Strategy.getStrategy().listBest);
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
	public State generate(Integer operatornumber) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{ //PSO
	if (getCountParticle() >= countRef)
	    setCountParticle(0);
//		System.out.println("Contador de particulas: " + getCountParticle() + " Contador de iteraciones " + Strategy.getStrategy().getCountCurrent());
	listParticle.get(getCountParticle()).generate(1);
	return listParticle.get(getCountParticle()).getStateActual();
	}
   	
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
	public State getReference() {
		return null;
	}
	
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


	public State getStateReferencePSO() {
		return stateReferencePSO;
	}

	public void setStateReferencePSO(State stateReferencePSO) {
		this.stateReferencePSO = stateReferencePSO;
	}

	public List<Particle> getListStateReference() {
		return this.getListParticle();
	}

	public void setListStateReference(List<State> listStateReference) {
		this.listStateReference = listStateReference;
	}

	public List<Particle> getListParticle() {
		return listParticle;
	}

	public List<Particle> setListParticle(List<Particle> listParticle) {
		this.listParticle = listParticle;
		return listParticle;
	}

	public GeneratorType getGeneratorType() {
		return generatorType;
	}

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
	public void setInitialReference(State stateInitialRef) {
		// TODO Auto-generated method stub

	}

	@Override
	public GeneratorType getType() {
		// TODO Auto-generated method stub
		return this.generatorType;
	}

	@Override
	public List<State> getReferenceList() {
		// TODO Auto-generated method stub
		return new ArrayList<State>(this.listStateReference);
	}

	@Override
	public List<State> getSonList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean awardUpdateREF(State stateCandidate) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setWeight(float weight) {
		// TODO Auto-generated method stub

	}

	@Override
	public float getWeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int[] getListCountBetterGender() {
		// TODO Auto-generated method stub
		return (this.listCountBetterGender == null) ? new int[0] : Arrays.copyOf(this.listCountBetterGender, this.listCountBetterGender.length);
	}

	@Override
	public int[] getListCountGender() {
		// TODO Auto-generated method stub
		return (this.listCountGender == null) ? new int[0] : Arrays.copyOf(this.listCountGender, this.listCountGender.length);
	}

	@Override
	public float[] getTrace() {
		// TODO Auto-generated method stub
		return (this.listTrace == null) ? new float[0] : Arrays.copyOf(this.listTrace, this.listTrace.length);
	}


	
}
