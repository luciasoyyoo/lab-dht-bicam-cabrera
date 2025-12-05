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
 * MultiobjectiveHillClimbingRestart - class that implements the Multiobjective Hill Climbing with Restart metaheuristic.
 */
public class MultiobjectiveHillClimbingRestart extends Generator{

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


	/**
	 * MultiobjectiveHillClimbingRestart - class that implements the Multiobjective Hill Climbing with Restart metaheuristic.
	 */
	public MultiobjectiveHillClimbingRestart() {
		super();
		this.typeAcceptation = AcceptType.AcceptNotDominated;
		this.strategy = StrategyType.NORMAL;
		//Problem problem = Strategy.getStrategy().getProblem();
		this.typeCandidate = CandidateType.NotDominatedCandidate;
		this.candidatevalue = new CandidateValue();
		this.generatortype = GeneratorType.MultiobjectiveHillClimbingRestart;
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

	@Override
	/**
	 * updateReference - update the reference state and the list of visited states.
	 * @param stateCandidate 
	 * @param countIterationsCurrent 
	 */
	public void updateReference(State stateCandidate, Integer countIterationsCurrent) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		//Agregando la primera soluci�n a la lista de soluciones no dominadas

		if(Strategy.getStrategy().listRefPoblacFinal.size() == 0){
			Strategy.getStrategy().listRefPoblacFinal.add(stateReferenceHC.clone());
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
			//tomar xc q pertenesca a la vecindad de xa
		}
		else{
			boolean stop = false;
			while (i < neighborhood.size()&& stop==false) {
				if (contain(neighborhood.get(i))==false) {
					stateCandidate = neighborhood.get(i);
					Strategy.getStrategy().getProblem().Evaluate(stateCandidate);  
					visitedState.add(stateCandidate);
					accept = candidate.acceptCandidate(lastState, stateCandidate.clone());
					stop=true;
				}
				i++;
			}
			while (stop == false) {
				stateCandidate = Strategy.getStrategy().getProblem().getOperator().generateRandomState(1).get(0);
				if (contain(stateCandidate)==false) {
					Strategy.getStrategy().getProblem().Evaluate(stateCandidate);  
					stop=true;
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

	/**
	 * contain - check if the state is contained in the visited states.
	 * @param state 
	 * @return return true if the state is contained, false otherwise
	 */
	private boolean contain(State state){
		boolean found = false;
		for (Iterator<State> iter = visitedState.iterator(); iter.hasNext();) {
			State element = (State) iter.next();
			if(element.Comparator(state)==true){
				found = true;
			}
		}
		return found;
	}

	@Override
	/**
	 * awardUpdateREF - award the update of the reference state.
	 * @param stateCandidate 
	 * @return return true if the reference state was updated, false otherwise
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
	 * @param weight the weight to set
	 */
	public void setWeight(float weight) {
		// TODO Auto-generated method stub

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

	@Override
	/**
	 * getListCountBetterGender - get the list of count of better solutions by gender.
	 * @return the list of count of better solutions by gender
	 */
	public int[] getListCountBetterGender() {
		// This generator doesn't maintain listCount arrays; return empty array to avoid nulls
		return new int[0];
	}

	@Override
	/**
	 * getListCountGender - get the list of count of solutions by gender.
	 * @return the list of count of solutions by gender
	 */
	public int[] getListCountGender() {
		// This generator doesn't maintain listCount arrays; return empty array to avoid nulls
		return new int[0];
	}
}
