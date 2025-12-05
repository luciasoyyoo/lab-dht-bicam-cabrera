package metaheuristics.generators;


import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import factory_interface.IFFactoryAcceptCandidate;
import factory_method.FactoryAcceptCandidate;

import problem.definition.State;
import local_search.acceptation_type.AcceptType;
import local_search.acceptation_type.AcceptableCandidate;
import local_search.candidate_type.CandidateType;
import local_search.candidate_type.CandidateValue;
import local_search.complement.StrategyType;
import metaheurictics.strategy.Strategy;

/**
 * MultiobjectiveHillClimbingDistance - class that implements the Multiobjective Hill Climbing Distance metaheuristic.
 */
public class MultiobjectiveHillClimbingDistance extends Generator{

	protected CandidateValue candidatevalue;
	protected AcceptType typeAcceptation;
	protected StrategyType strategy;
	protected CandidateType typeCandidate;
	protected State stateReferenceHC;
	protected IFFactoryAcceptCandidate ifacceptCandidate;
	protected GeneratorType generatortype;
	protected List<State> listStateReference = new ArrayList<State>(); 
	protected float weight;
	protected List<Float> listTrace = new ArrayList<Float>();
	private List<State> visitedState = new ArrayList<State>();
	private static final int sizeNeighbors = 10;

	/**
	 * Accessor for sizeNeighbors.
	 */
	public static int getSizeNeighbors() {
		return sizeNeighbors;
	}
	//Lista que contiene las distancias de cada soluci�n del frente de Pareto estimado
	static List<Double> distanceSolution = new ArrayList<Double>();


	/**
	 * MultiobjectiveHillClimbingDistance - class that implements the Multiobjective Hill Climbing Distance metaheuristic.
	 */
	public MultiobjectiveHillClimbingDistance() {
		super();
		this.typeAcceptation = AcceptType.AcceptNotDominated;
		this.strategy = StrategyType.NORMAL;
		this.typeCandidate = CandidateType.NotDominatedCandidate;
		this.candidatevalue = new CandidateValue();
	this.generatortype = GeneratorType.MultiobjectiveHillClimbingDistance;
	this.stateReferenceHC = new State();
		this.weight = 50;
		listTrace.add(weight);
	}

	@Override
	/**
	 * generate - generate a new state based on the operator number.
	 * @param operatornumber the operator number to use for generating the state
	 * @return the generated state
	 */
	public State generate(Integer operatornumber) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		List<State> neighborhood = Strategy.getStrategy().getProblem().getOperator().generatedNewState(stateReferenceHC, operatornumber);
		State statecandidate = candidatevalue.stateCandidate(stateReferenceHC, typeCandidate, strategy, operatornumber, neighborhood);
		return statecandidate;
	}

	/**
	 * updateReference - update the reference state and the list of visited states.
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
	public void updateReference(State stateCandidate, Integer countIterationsCurrent) throws IllegalArgumentException, SecurityException, ClassNotFoundException, 
	InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		//Agregando la primera soluci�n a la lista de soluciones no dominadas
		if(Strategy.getStrategy().listRefPoblacFinal.size() == 0){
			Strategy.getStrategy().listRefPoblacFinal.add(stateReferenceHC.clone());
			distanceSolution.add(Double.valueOf(0.0));
		}
		ifacceptCandidate = new FactoryAcceptCandidate();
		AcceptableCandidate candidate = ifacceptCandidate.createAcceptCandidate(typeAcceptation);
		State lastState = Strategy.getStrategy().listRefPoblacFinal.get(Strategy.getStrategy().listRefPoblacFinal.size()-1);
		List<State> neighborhood = Strategy.getStrategy().getProblem().getOperator().generatedNewState(stateReferenceHC, getSizeNeighbors());
		int i= 0;

			Boolean accept = candidate.acceptCandidate(lastState, stateCandidate.clone());
			if(accept.equals(true)){
				stateReferenceHC = stateCandidate.clone();
				visitedState = new ArrayList<State>();
			}

			else{

				boolean stop = false;
				while (i < neighborhood.size()&& stop==false) {
			if (contain(neighborhood.get(i))==false) {
				stateReferenceHC = solutionMoreDistance(Strategy.getStrategy().listRefPoblacFinal, distanceSolution);
						visitedState.add(stateReferenceHC);
						stop=true;
						lastState=stateReferenceHC.clone();
					}
					i++;
				}
				int coutrestart=0;
				while (stop == false && coutrestart < getSizeNeighbors() && accept==false) {
					stateCandidate = Strategy.getStrategy().getProblem().getOperator().generateRandomState(1).get(0);
			if (contain(stateCandidate)==false) {
						Strategy.getStrategy().getProblem().Evaluate(stateCandidate);  
						visitedState.add(stateCandidate);
						stop=true;
						coutrestart++;
						accept = candidate.acceptCandidate(lastState, stateCandidate.clone());
					}
				}
				if(accept.equals(true)){
					stateReferenceHC = stateCandidate.clone();
					visitedState = new ArrayList<State>();
					//tomar xc q pertenesca a la vecindad de xa
				}
			}

		getReferenceList();
	}

	/**
	 * solutionMoreDistance - find the solution with the maximum distance.
	 * @param state the list of states
	 * @param distanceSolution the list of distances corresponding to the states
	 * @return the state with the maximum distance
	 */
	private State solutionMoreDistance(List<State> state, List<Double> distanceSolution) {
		Double max = (double) -1;
		int pos = -1;
		Double[] distance = distanceSolution.toArray(new Double[distanceSolution.size()]);
		State[] solutions = state.toArray(new State[state.size()]);
		for (int i = 0; i < distance.length; i++) {
			Double dist = distance[i];
			if(dist > max){
				max = dist;
				pos = i;
			}
		}
		if(pos != -1)
			return solutions[pos];
		else
			return null;
	}

	@Override
	/**
	 * getReferenceList - get the list of reference states.
	 * @return the list of reference states
	 */
	public List<State> getReferenceList() {
		listStateReference.add(stateReferenceHC.clone());
		return listStateReference;
	}

	@Override
	/**
	 * getReference - get the reference state.
	 * @return the reference state
	 */
	public State getReference() {
		return stateReferenceHC;
	}

	/**
	 * setStateRef - set the reference state.
	 * @param stateRef the reference state to set
	 */
	public void setStateRef(State stateRef) {
		this.stateReferenceHC = stateRef;
	}

	@Override
	/**
	 * setInitialReference - set the initial reference state.
	 * @param stateInitialRef the initial reference state to set
	 */
	public void setInitialReference(State stateInitialRef) {
		this.stateReferenceHC = stateInitialRef;
	}

	/**
	 * getGeneratorType - get the generator type.
	 * @return the generator type
	 */
	public GeneratorType getGeneratorType() {
		return generatortype;
	}

	/**
	 * setGeneratorType - set the generator type.
	 * @param generatortype the generator type to set
	 */
	public void setGeneratorType(GeneratorType generatortype) {
		this.generatortype = generatortype;
	}

	@Override
	/**
	 * getType - get the generator type.
	 * @return the generator type
	 */
	public GeneratorType getType() {
		return this.generatortype;
	}

	@Override
	/**
	 * getSonList - get the list of child states.
	 * @return the list of child states
	 */
	public List<State> getSonList() {
		// TODO Auto-generated method stub
		return null;
	}

	public static List<Double> distanceCalculateAdd(List<State> solution) {
		State[] solutions = solution.toArray(new State[solution.size()]);
		Double distance = 0.0;
		List<Double>listDist=new ArrayList<Double>();
		State lastSolution = solution.get(solution.size()-1);
		//Actualizando las distancias de todos los elmentos excepto el nuevo insertando
		for (int k = 0; k < solutions.length-1; k++) {
			State solA = solutions[k];
			distance = solA.Distance(lastSolution);
			listDist.add(distanceSolution.get(k)+distance);
//			distanceSolution.set(k, distanceSolution.get(k) + distance);
		}
		distance = 0.0;
		//Calculando la distancia del �ltimo elemento (elemento insertado) respecto al resto de los elementos
		if (solutions.length==1) {
			return distanceSolution;
		
		}else {
		
			for (int l = 0; l < solutions.length-1; l++) {
				State solB = solutions[l];
				distance += lastSolution.Distance(solB);
			}
			listDist.add(distance);
//			distanceSolution.add(distance);
			distanceSolution=listDist;
			
			return distanceSolution;
		}

	}


	/**
	 * contain - check if the state is contained in the visited states.
	 * @param state 
	 * @return return true if the state is contained, false otherwise
	 */
	private boolean contain(State state){
		boolean found = false;
		for (Iterator<State> iter = visitedState.iterator(); iter.hasNext();) {
			State element = (State) iter.next();
			if(element.Comparator(state)){
				found = true;
			}
		}
		return found;
	}

	@Override
	/**
	 * awardUpdateREF - check if the candidate state should update the reference state.
	 * @param stateCandidate 
	 * @return return true if the reference state should be updated, false otherwise
	 */
	public boolean awardUpdateREF(State stateCandidate) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	/**
	 * getWeight - get the weight of the generator.
	 * @return the weight of the generator
	 */
	public float getWeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	/**
	 * setWeight - set the weight of the generator.
	 * @param weight 
	 */
	public void setWeight(float weight) {
		// TODO Auto-generated method stub

	}

	@Override
	/**
	 * getListCountBetterGender - get the list of counts for better gender.
	 * @return the list of counts for better gender
	 */
	public int[] getListCountBetterGender() {
		// This generator doesn't maintain listCount arrays; return empty array to avoid nulls
		return new int[0];
	}

	@Override
	/**
	 * getListCountGender - get the list of counts for gender.
	 * @return the list of counts for gender
	 */
	public int[] getListCountGender() {
		// This generator doesn't maintain listCount arrays; return empty array to avoid nulls
		return new int[0];
	}

	@Override
	/**
	 * getTrace - get the trace of the generator.
	 * @return the trace of the generator
	 */
	public float[] getTrace() {
		// TODO Auto-generated method stub
		if (this.listTrace == null) return new float[0];
		float[] arr = new float[this.listTrace.size()];
		for (int i = 0; i < this.listTrace.size(); i++) {
			Float v = this.listTrace.get(i);
			arr[i] = (v == null) ? 0f : v.floatValue();
		}
		return arr;
	}
}
