package metaheuristics.generators;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;


import local_search.acceptation_type.AcceptType;
import local_search.acceptation_type.AcceptableCandidate;
import local_search.candidate_type.CandidateType;
import local_search.candidate_type.CandidateValue;
import local_search.complement.StrategyType;
import metaheurictics.strategy.Strategy;

import problem.definition.State;
import problem.definition.Problem.ProblemType;



import factory_interface.IFFactoryAcceptCandidate;
import factory_method.FactoryAcceptCandidate;


/**
 * HillClimbing - class that implements the Hill Climbing metaheuristic.
 */
public class HillClimbing extends Generator{

    protected CandidateValue candidatevalue;
	protected AcceptType typeAcceptation;
	protected StrategyType strategy;
	protected CandidateType typeCandidate;
	protected State stateReferenceHC;
	protected IFFactoryAcceptCandidate ifacceptCandidate;
	protected GeneratorType generatortype;
	protected List<State> listStateReference = new ArrayList<State>(); 
	protected float weight;
	
	//problemas dinamicos
	//problemas dinamicos: use instance fields from Generator (countGender, countBetterGender)
	private int[] listCountBetterGender = new int[10];
	private int[] listCountGender = new int[10];
	private float[] listTrace = new float[1200000];
	
	/**
	 * HillClimbing - class that implements the Hill Climbing metaheuristic.
	 */
	public HillClimbing() {
		super();
		this.typeAcceptation = AcceptType.AcceptBest;
		this.strategy = StrategyType.NORMAL;
		if(Strategy.getStrategy().getProblem().getTypeProblem().equals(ProblemType.Maximizar)) {
			this.typeCandidate = CandidateType.GreaterCandidate;
		}
		else{
			this.typeCandidate = CandidateType.SmallerCandidate;
		}
		this.candidatevalue = new CandidateValue();
	this.generatortype = GeneratorType.HillClimbing;
		this.weight = 50;
		listTrace[0] = this.weight;
		listCountBetterGender[0] = 0;
		listCountGender[0] = 0;
	}

	@Override
	/**
	 * generate - generate a new state based on the current state and the operator number.
	 * @param operatornumber the operator number to use for generating the new state
	 * @return the newly generated state
	 */
	public State generate(Integer operatornumber) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		List<State> neighborhood = Strategy.getStrategy().getProblem().getOperator().generatedNewState(stateReferenceHC, operatornumber);
	    State statecandidate = candidatevalue.stateCandidate(stateReferenceHC, typeCandidate, strategy, operatornumber, neighborhood);
	  
	    return statecandidate;
	}

	@Override
	/**
	 * updateReference - update the reference state if the candidate state is accepted.
	 * @param stateCandidate 
	 * @param countIterationsCurrent 
	 */
	public void updateReference(State stateCandidate, Integer countIterationsCurrent) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		ifacceptCandidate = new FactoryAcceptCandidate();
		AcceptableCandidate candidate = ifacceptCandidate.createAcceptCandidate(typeAcceptation);
		Boolean accept = candidate.acceptCandidate(stateReferenceHC, stateCandidate);
		if(accept.equals(true))
		  stateReferenceHC = stateCandidate;
	}
	
	@Override
	/**
	 * getReferenceList - get the list of reference states.
	 * @return the list of reference states
	 */
	public List<State> getReferenceList() {
		if (stateReferenceHC != null) {
			// keep internal list updated but do not expose it directly
			if (listStateReference.isEmpty() || listStateReference.get(listStateReference.size() - 1) != stateReferenceHC) {
				listStateReference.add(stateReferenceHC);
			}
		}
		return new ArrayList<State>(listStateReference);
	}

	@Override
	/**
	 * getReference - get the reference state.
	 * @return the reference state
	 */
	public State getReference() {
		return (stateReferenceHC == null) ? null : new State(stateReferenceHC);
	}

	/**
	 * setStateRef - set the reference state.
	 * @param stateRef 
	 */
	public void setStateRef(State stateRef) {
		this.stateReferenceHC = (stateRef == null) ? null : new State(stateRef);
	}

	@Override
	/**
	 * setInitialReference - set the initial reference state.
	 * @param stateInitialRef 
	 */
	public void setInitialReference(State stateInitialRef) {
		this.stateReferenceHC = (stateInitialRef == null) ? null : new State(stateInitialRef);
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
	 * @param generatortype 
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
	 * getSonList - get the list of son states.
	 * @return the list of son states
	 */
	public List<State> getSonList() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * setTypeCandidate - set the type of candidate.
	 * @param typeCandidate 
	 */
	public void setTypeCandidate(CandidateType typeCandidate){
		this.typeCandidate = typeCandidate;
	}

	@Override
	/**
	 * awardUpdateREF - award the update of the reference state if the candidate state is accepted.
	 * @param stateCandidate 
	 * @return returns true if the update is awarded, false otherwise.
	 */
	public boolean awardUpdateREF(State stateCandidate) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	/**
	 * getWeight - get the weight of the candidate.
	 * @return the weight of the candidate
	 */
	public float getWeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	/**
	 * setWeight - set the weight of the candidate.
	 * @param weight 
	 */
	public void setWeight(float weight) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	/**
	 * getListCountBetterGender - get the list of counts of better candidates by gender.
	 * @return the list of counts of better candidates by gender
	 */
	public int[] getListCountBetterGender() {
		// TODO Auto-generated method stub
		return (this.listCountBetterGender == null) ? new int[0] : java.util.Arrays.copyOf(this.listCountBetterGender, this.listCountBetterGender.length);
	}

	@Override
	/**
	 * getListCountGender - get the list of counts of candidates by gender.
	 * @return the list of counts of candidates by gender
	 */
	public int[] getListCountGender() {
		// TODO Auto-generated method stub
		return (this.listCountGender == null) ? new int[0] : java.util.Arrays.copyOf(this.listCountGender, this.listCountGender.length);
	}

	@Override
	/**
	 * getTrace - get the trace of the candidate.
	 * @return the trace of the candidate
	 */
	public float[] getTrace() {
		// TODO Auto-generated method stub
		return (this.listTrace == null) ? new float[0] : java.util.Arrays.copyOf(this.listTrace, this.listTrace.length);
	}
}
