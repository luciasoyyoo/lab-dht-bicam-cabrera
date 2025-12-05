package metaheuristics.generators;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Arrays;


import local_search.acceptation_type.AcceptType;
import local_search.acceptation_type.AcceptableCandidate;
import local_search.candidate_type.CandidateType;
import local_search.candidate_type.CandidateValue;
import local_search.complement.StrategyType;
import metaheurictics.strategy.Strategy;

import problem.definition.Problem;
import problem.definition.State;
import problem.definition.Problem.ProblemType;



import factory_interface.IFFactoryAcceptCandidate;
import factory_method.FactoryAcceptCandidate;


/**
 * LimitThreshold - class that implements the Limit Threshold metaheuristic.
 */
public class LimitThreshold extends Generator{
	
	private CandidateValue candidatevalue;
	private AcceptType typeAcceptation;
	private StrategyType strategy;
	private CandidateType typeCandidate;
	private State stateReferenceLT;
    private IFFactoryAcceptCandidate ifacceptCandidate;
	private GeneratorType typeGenerator;
	private float weight;
	
	//problemas dinamicos
	//problemas dinamicos: use instance fields from Generator (countGender, countBetterGender)
	private int[] listCountBetterGender = new int[10];
	private int[] listCountGender = new int[10];
	private float[] listTrace = new float[1200000];
	
	/**
	 * getTypeGenerator - get the type of the generator.
	 * @return the type of the generator
	 */
	public GeneratorType getTypeGenerator() {
		return typeGenerator;
	}

	/**
	 * setTypeGenerator - set the type of the generator.
	 * @param typeGenerator the type of the generator to set
	 */
	public void setTypeGenerator(GeneratorType typeGenerator) {
		this.typeGenerator = typeGenerator;
	}

	/**
	 * LimitThreshold - class that implements the Limit Threshold metaheuristic.
	 */
	public LimitThreshold() {
		super();
		this.typeAcceptation = AcceptType.AcceptNotBadU;
		this.strategy = StrategyType.NORMAL;


		Problem problem = Strategy.getStrategy().getProblem();

		if(problem.getTypeProblem().equals(ProblemType.Maximizar)) {
			this.typeCandidate = CandidateType.GreaterCandidate;
		}
		else{
			this.typeCandidate = CandidateType.SmallerCandidate;
		}

		this.candidatevalue = new CandidateValue();
		this.typeGenerator = GeneratorType.LimitThreshold;
		this.weight = (float) 50.0;
		listTrace[0] = weight;
		listCountBetterGender[0] = 0;
		listCountGender[0] = 0;

	}
	@Override
	/**
	 * generate - generate a new state based on the operator number.
	 * @param operatornumber the operator number to use for generating the new state
	 * @return the generated state
	 */
	public State generate(Integer operatornumber) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		List<State> neighborhood = Strategy.getStrategy().getProblem().getOperator().generatedNewState(stateReferenceLT, operatornumber);
	    State statecandidate = candidatevalue.stateCandidate(stateReferenceLT, typeCandidate, strategy, operatornumber, neighborhood);
	    return statecandidate;
	}

	@Override
	/**
	 * updateReference - update the reference state based on the candidate state.
	 * @param stateCandidate 
	 * @param countIterationsCurrent 
	 */
	public void updateReference(State stateCandidate, Integer countIterationsCurrent) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		ifacceptCandidate = new FactoryAcceptCandidate();
		AcceptableCandidate candidate = ifacceptCandidate.createAcceptCandidate(typeAcceptation);
		Boolean accept = candidate.acceptCandidate(stateReferenceLT , stateCandidate);
		if(accept.equals(true)){
			stateReferenceLT = stateCandidate;
		}
	}
	

	@Override
	/**
	 * getReference - get the reference state.
	 * @return the reference state
	 */
	public State getReference() {
		return stateReferenceLT;
	}

	/**
	 * setStateRef - set the reference state.
	 * @param stateRef the reference state to set
	 */
	public void setStateRef(State stateRef) {
		this.stateReferenceLT = stateRef;
	}

	@Override
	/**
	 * setInitialReference - set the initial reference state.
	 * @param stateInitialRef 
	 */
	public void setInitialReference(State stateInitialRef) {
		this.stateReferenceLT = stateInitialRef;
	}


	@Override
	/**
	 * getType - get the type of the generator.
	 * @return the type of the generator
	 */
	public GeneratorType getType() {
		return this.typeGenerator;
	}

	@Override
	/**
	 * getReferenceList - get the list of reference states.
	 * @return the list of reference states
	 */
	public List<State> getReferenceList() {
		return null;
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
	 * @param typeCandidate the type of candidate to set
	 */
	public void setTypeCandidate(CandidateType typeCandidate){
		this.typeCandidate = typeCandidate;
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

	/**
	 * getWeight - get the weight of the generator.
	 * @return the weight of the generator
	 */
	public float getWeight() {
		// TODO Auto-generated method stub
		return this.weight;
	}

	@Override
	/**
	 * setWeight - set the weight of the generator.
	 * @param weight 
	 */
	public void setWeight(float weight) {
		// TODO Auto-generated method stub
		this.weight = weight;
	}


	@Override
	/**
	 * getListCountBetterGender - get the list of count better gender.
	 * @return the list of count better gender
	 */
	public int[] getListCountBetterGender() {
		// TODO Auto-generated method stub
		return (this.listCountBetterGender == null) ? new int[0] : Arrays.copyOf(this.listCountBetterGender, this.listCountBetterGender.length);
	}

	@Override
	/**
	 * getListCountGender - get the list of count gender.
	 * @return the list of count gender
	 */
	public int[] getListCountGender() {
		// TODO Auto-generated method stub
		return (this.listCountGender == null) ? new int[0] : Arrays.copyOf(this.listCountGender, this.listCountGender.length);
	}

	@Override
	/**
	 * getTrace - get the trace of the generator.
	 * @return the trace of the generator
	 */
	public float[] getTrace() {
		// TODO Auto-generated method stub
		return (this.listTrace == null) ? new float[0] : Arrays.copyOf(this.listTrace, this.listTrace.length);
	}

}
