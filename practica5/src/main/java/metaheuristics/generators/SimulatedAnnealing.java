package metaheuristics.generators;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import local_search.acceptation_type.AcceptType;
import local_search.acceptation_type.AcceptableCandidate;
import local_search.candidate_type.CandidateType;
import local_search.candidate_type.CandidateValue;
import local_search.complement.StrategyType;
import metaheurictics.strategy.Strategy;

import problem.definition.State;

import factory_interface.IFFactoryAcceptCandidate;
import factory_method.FactoryAcceptCandidate;

/**
 * SimulatedAnnealing - class that implements the Simulated Annealing metaheuristic.
 */
public class SimulatedAnnealing extends Generator {

	private CandidateValue candidatevalue;
	private AcceptType typeAcceptation;
	private StrategyType strategy;
	private CandidateType typeCandidate;
	private State stateReferenceSA;
    private IFFactoryAcceptCandidate ifacceptCandidate;
	// cooling factor - fixed constant
	public static final double alpha = 0.93;
	// default temperature parameters (can be adjusted per run)
	private static Double tinitial = 250.0;
	public static final Double tfinal = 41.66;
	static int countIterationsT = 50;

	/**
	 * getCountIterationsT - get the count of iterations for temperature update.
	 * @return the count of iterations for temperature update
	 */
	public static int getCountIterationsT() {
		return countIterationsT;
	}

	/**
	 * setCountIterationsT - set the count of iterations for temperature update.
	 * @param c the count of iterations to set
	 */
	public static void setCountIterationsT(int c) {
		countIterationsT = c;
	}
    private int countRept;
    private GeneratorType typeGenerator;
    private List<State> listStateReference = new ArrayList<State>();
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
	 * @param typeGenerator 
	 */
	public void setTypeGenerator(GeneratorType typeGenerator) {
		this.typeGenerator = typeGenerator;
	}

	/**
	 * Getter for initial temperature.
	 */
	public static Double getTinitial() {
		return tinitial;
	}

	/**
	 * Setter for initial temperature.
	 */
	public static void setTinitial(Double t) {
		tinitial = t;
	}

	/**
	 * SimulatedAnnealing - descripcion (añade detalles).
	 */
	public SimulatedAnnealing(){

    	super();
    	this.typeAcceptation = AcceptType.AcceptNotBadT;
		this.strategy = StrategyType.NORMAL;
		this.typeCandidate = CandidateType.RandomCandidate;
		this.candidatevalue = new CandidateValue();
		this.typeGenerator = GeneratorType.SimulatedAnnealing;
		this.weight = 50;
		listTrace[0] = this.weight;
		listCountBetterGender[0] = 0;
		listCountGender[0] = 0;
    }

	@Override
	/**
	 * generate - method to generate a new state.
	 * @param operatornumber 
	 * @return return the generated state
	 */
	public State generate(Integer operatornumber) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		List<State> neighborhood = Strategy.getStrategy().getProblem().getOperator().generatedNewState(stateReferenceSA, operatornumber);
	    State statecandidate = candidatevalue.stateCandidate(stateReferenceSA, typeCandidate, strategy, operatornumber, neighborhood);
	    return statecandidate;
	}

	@Override
	/**
	 * getReference - get the reference state.
	 * @return the reference state
	 */
	public State getReference() {
		return stateReferenceSA;
	}

	/**
	 * setStateRef - set the reference state.
	 * @param stateRef the reference state to set
	 */
	public void setStateRef(State stateRef) {
		this.stateReferenceSA = stateRef;
	}

	@Override
	/**
	 * setInitialReference - set the initial reference state.
	 * @param stateInitialRef the initial reference state to set
	 */
	public void setInitialReference(State stateInitialRef) {
		this.stateReferenceSA = stateInitialRef;
	}

	@Override
	/**
	 * updateReference - update the reference state.
	 * @param stateCandidate 
	 * @param countIterationsCurrent 
	 */
	public void updateReference(State stateCandidate, Integer countIterationsCurrent)throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		countRept = countIterationsT;
		ifacceptCandidate = new FactoryAcceptCandidate();
		AcceptableCandidate candidate = ifacceptCandidate.createAcceptCandidate(typeAcceptation);
		Boolean accept = candidate.acceptCandidate(stateReferenceSA, stateCandidate);
		if(accept.equals(true))
		  stateReferenceSA = stateCandidate;
		if(countIterationsCurrent.equals(getCountIterationsT())){
			// use static setters to avoid writing the static field from an instance method
			SimulatedAnnealing.setTinitial(SimulatedAnnealing.getTinitial() * alpha);
			SimulatedAnnealing.setCountIterationsT(SimulatedAnnealing.getCountIterationsT() + countRept);
		}
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
		listStateReference.add(stateReferenceSA);
		return new ArrayList<State>(listStateReference);
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
	 * @return return true if the update is awarded, false otherwise
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
		return this.weight;
	}

	@Override
	/**
	 * setWeight - set the weight of the generator.
	 * @param weight the weight to set
	 */
	public void setWeight(float weight) {
		// TODO Auto-generated method stub
		this.weight = weight;
	}

	@Override
	/**
	 * getListCountBetterGender - get the list of better gender counts.
	 * @return the list of better gender counts
	 */
	public int[] getListCountBetterGender() {
		// TODO Auto-generated method stub
		return (this.listCountBetterGender == null) ? new int[0] : Arrays.copyOf(this.listCountBetterGender, this.listCountBetterGender.length);
	}

	@Override
	/**
	 * getListCountGender - get the list of gender counts.
	 * @return the list of gender counts
	 */
	public int[] getListCountGender() {
		// TODO Auto-generated method stub
		return (this.listCountGender == null) ? new int[0] : Arrays.copyOf(this.listCountGender, this.listCountGender.length);
	}

	@Override
	/**
	 * getTrace -get the trace of the generator.
	 * @return the trace of the generator
	 */
	public float[] getTrace() {
		// TODO Auto-generated method stub
		return (this.listTrace == null) ? new float[0] : Arrays.copyOf(this.listTrace, this.listTrace.length);
	}

}
