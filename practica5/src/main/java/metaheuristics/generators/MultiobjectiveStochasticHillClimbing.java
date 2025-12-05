package metaheuristics.generators;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import factory_interface.IFFactoryAcceptCandidate;
import factory_method.FactoryAcceptCandidate;
import local_search.acceptation_type.AcceptType;
import local_search.acceptation_type.AcceptableCandidate;
import local_search.candidate_type.CandidateType;
import local_search.candidate_type.CandidateValue;
import local_search.complement.StrategyType;
import metaheurictics.strategy.Strategy;
import problem.definition.State;


/**
 * MultiobjectiveStochasticHillClimbing - class that implements the Multiobjective Stochastic Hill Climbing metaheuristic.
 */
public class MultiobjectiveStochasticHillClimbing extends Generator{

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
	
	/**
	 * MultiobjectiveStochasticHillClimbing - class that implements the Multiobjective Stochastic Hill Climbing metaheuristic.
	 */
	public MultiobjectiveStochasticHillClimbing() {
		super();
		this.typeAcceptation = AcceptType.AcceptNotDominated;
		this.strategy = StrategyType.NORMAL;
		this.typeCandidate = CandidateType.NotDominatedCandidate;
		this.candidatevalue = new CandidateValue();
		this.generatortype = GeneratorType.MultiobjectiveStochasticHillClimbing;
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
		ifacceptCandidate = new FactoryAcceptCandidate();
		AcceptableCandidate candidate = ifacceptCandidate.createAcceptCandidate(typeAcceptation);
		Boolean accept = candidate.acceptCandidate(stateReferenceHC, stateCandidate);
		if(accept.equals(true))
		  stateReferenceHC = stateCandidate.clone();
		getReferenceList();
	}
	
	@Override
	/**
	 * getReferenceList - get the list of reference states.
	 * @return the list of reference states
	 */
	public List<State> getReferenceList() {
		listStateReference.add( stateReferenceHC.clone());
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
		return new float[0];
	}

	@Override
	/**
	 * getListCountBetterGender - get the list of counts of better gender solutions.
	 * @return the list of counts of better gender solutions
	 */
	public int[] getListCountBetterGender() {
		// TODO Auto-generated method stub
		return new int[0];
	}

	@Override
	/**
	 * getListCountGender - get the list of counts of gender solutions.
	 * @return the list of counts of gender solutions
	 */
	public int[] getListCountGender() {
		// TODO Auto-generated method stub
		return new int[0];
	}
}
