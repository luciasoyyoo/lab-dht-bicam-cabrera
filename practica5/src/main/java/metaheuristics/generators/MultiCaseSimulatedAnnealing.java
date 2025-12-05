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
import problem.definition.Problem;
import problem.definition.State;


/**
 * MultiCaseSimulatedAnnealing - class that implements the Multi-Case Simulated Annealing metaheuristic.
 */
public class MultiCaseSimulatedAnnealing extends Generator {

	private CandidateValue candidatevalue;
	private AcceptType typeAcceptation;
	private StrategyType strategy;
	private CandidateType typeCandidate;
	private State stateReferenceSA;
    private IFFactoryAcceptCandidate ifacceptCandidate;
	// Cooling factor: fixed constant for update rule. Set a conservative default.
	public static final double alpha = 0.93;
	// Make initial temperature private and provide accessors so it can be protected/validated.
	private static Double tinitial = 250.0;
	public static final Double tfinal = 41.66;
	static int countIterationsT;
    private int countRept;
    private GeneratorType typeGenerator;
    private List<State> listStateReference = new ArrayList<State>();
    private float weight;
	private List<Float> listTrace = new ArrayList<Float>();

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

	// Accessors for tinitial to avoid exposing a mutable static field directly.
	public static Double getTinitial() {
		return tinitial;
	}
	/**
	 * setTinitial - set the initial temperature.
	 * @param t the initial temperature to set
	 */
	public static void setTinitial(Double t) {
		tinitial = t;
	}

	/**
	 * MultiCaseSimulatedAnnealing - class that implements the Multi-Case Simulated Annealing metaheuristic.
	 */
	public MultiCaseSimulatedAnnealing(){
    	super();
    	this.typeAcceptation = AcceptType.AcceptMulticase;
		this.strategy = StrategyType.NORMAL;
		this.typeCandidate = CandidateType.RandomCandidate;
		this.candidatevalue = new CandidateValue();
		this.typeGenerator = GeneratorType.MultiCaseSimulatedAnnealing;
		this.weight = 50;
		listTrace.add(weight);
    }

	@Override
	/**
	 * generate - generate a new state based on the operator number.
	 * @param operatornumber the operator number to use for generating the new state
	 * @return the generated state
	 */
	public State generate(Integer operatornumber) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Problem problem = Strategy.getStrategy().getProblem();
		List<State> neighborhood = problem.getOperator().generatedNewState(stateReferenceSA, operatornumber);
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
	 * updateReference - update the reference state based on the candidate state.
	 * @param stateCandidate 
	 * @param countIterationsCurrent 
	 */
	public void updateReference(State stateCandidate, Integer countIterationsCurrent)throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		countRept = MultiCaseSimulatedAnnealing.getCountIterationsT();
		ifacceptCandidate = new FactoryAcceptCandidate();
		AcceptableCandidate candidate = ifacceptCandidate.createAcceptCandidate(typeAcceptation);
		Boolean accept = candidate.acceptCandidate(stateReferenceSA, stateCandidate);
		if(accept.equals(true))
		  stateReferenceSA = stateCandidate.clone();
		if(countIterationsCurrent.equals(MultiCaseSimulatedAnnealing.getCountIterationsT())){
			// update via accessors to avoid writing static fields inside an instance method
			MultiCaseSimulatedAnnealing.setTinitial(MultiCaseSimulatedAnnealing.getTinitial() * alpha);
			//Variante Fast MOSA
			System.out.println("La T:" + MultiCaseSimulatedAnnealing.getTinitial());
			MultiCaseSimulatedAnnealing.setCountIterationsT(MultiCaseSimulatedAnnealing.getCountIterationsT() + countRept);
			System.out.println("La Cant es: " + MultiCaseSimulatedAnnealing.getCountIterationsT());
		}
		getReferenceList();
	}
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
		listStateReference.add(stateReferenceSA.clone());
		return listStateReference;
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
	 * getListCountBetterGender - get the list of counts for better gender.
	 * @return the list of counts for better gender
	 */
	public int[] getListCountBetterGender() {
		// TODO Auto-generated method stub
		return new int[0];
	}

	@Override
	/**
	 * getListCountGender - get the list of counts for gender.
	 * @return the list of counts for gender
	 */
	public int[] getListCountGender() {
		// TODO Auto-generated method stub
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
