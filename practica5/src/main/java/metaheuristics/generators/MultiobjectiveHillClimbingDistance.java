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
	public State generate(Integer operatornumber) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		List<State> neighborhood = Strategy.getStrategy().getProblem().getOperator().generatedNewState(stateReferenceHC, operatornumber);
		State statecandidate = candidatevalue.stateCandidate(stateReferenceHC, typeCandidate, strategy, operatornumber, neighborhood);
		return statecandidate;
	}

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
//		Boolean restart= true;

//		while (restart==true) {
			Boolean accept = candidate.acceptCandidate(lastState, stateCandidate.clone());
			if(accept.equals(true)){
				stateReferenceHC = stateCandidate.clone();
				visitedState = new ArrayList<State>();
			//		restart=false;
			}

			else{

				boolean stop = false;
				while (i < neighborhood.size()&& stop==false) {
			if (contain(neighborhood.get(i))==false) {
				stateReferenceHC = solutionMoreDistance(Strategy.getStrategy().listRefPoblacFinal, distanceSolution);
						visitedState.add(stateReferenceHC);
						stop=true;
						lastState=stateReferenceHC.clone();
//						restart=false;
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
	public List<State> getReferenceList() {
		listStateReference.add(stateReferenceHC.clone());
		return listStateReference;
	}

	@Override
	public State getReference() {
		return stateReferenceHC;
	}

	public void setStateRef(State stateRef) {
		this.stateReferenceHC = stateRef;
	}

	@Override
	public void setInitialReference(State stateInitialRef) {
		this.stateReferenceHC = stateInitialRef;
	}

	public GeneratorType getGeneratorType() {
		return generatortype;
	}

	public void setGeneratorType(GeneratorType generatortype) {
		this.generatortype = generatortype;
	}

	@Override
	public GeneratorType getType() {
		return this.generatortype;
	}

	@Override
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
	public boolean awardUpdateREF(State stateCandidate) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public float getWeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setWeight(float weight) {
		// TODO Auto-generated method stub

	}

	@Override
	public int[] getListCountBetterGender() {
		// This generator doesn't maintain listCount arrays; return empty array to avoid nulls
		return new int[0];
	}

	@Override
	public int[] getListCountGender() {
		// This generator doesn't maintain listCount arrays; return empty array to avoid nulls
		return new int[0];
	}

	@Override
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
