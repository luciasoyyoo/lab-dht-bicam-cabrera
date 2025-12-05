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
import local_search.complement.TabuSolutions;
import metaheurictics.strategy.Strategy;
import problem.definition.Problem;
import problem.definition.State;




/**
 * MultiobjectiveTabuSearch - class that implements the Multiobjective Tabu Search metaheuristic.
 */
public class MultiobjectiveTabuSearch extends Generator {

	private CandidateValue candidatevalue;
	private AcceptType typeAcceptation;
	private StrategyType strategy;
	private CandidateType typeCandidate;
	private State stateReferenceTS;
    private IFFactoryAcceptCandidate ifacceptCandidate;
    private GeneratorType typeGenerator;
    private List<State> listStateReference = new ArrayList<State>();
    private float weight;
	private List<Float> listTrace = new ArrayList<Float>();

    /**
     * getStateReferenceTS - get the reference state for Tabu Search.
     * @return the reference state for Tabu Search
     */
    public State getStateReferenceTS() {
		return stateReferenceTS;
	}

	/**
	 * setStateReferenceTS - 	set the reference state for Tabu Search.
	 * @param stateReferenceTS 
	 */
	public void setStateReferenceTS(State stateReferenceTS) {
		this.stateReferenceTS = stateReferenceTS;
	}

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
	 * MultiobjectiveTabuSearch - class that implements the Multiobjective Tabu Search metaheuristic.
	 */
	public MultiobjectiveTabuSearch() {
    	super();
		this.typeAcceptation = AcceptType.AcceptNotDominatedTabu;
		this.strategy = StrategyType.TABU;
	// Use problem API directly when needed; avoid unused local variable
		this.typeCandidate = CandidateType.RandomCandidate;
		this.candidatevalue = new CandidateValue();
		this.typeGenerator = GeneratorType.MultiobjectiveTabuSearch;
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
		//Devuelve la lista de soluciones no dominadas de todos los vecinos posibles de stateReferenceTS que no se encuentran en la lista Tabu
		List<State> neighborhood = Strategy.getStrategy().getProblem().getOperator().generatedNewState(stateReferenceTS, operatornumber);
		//Se escoge uno aleatoriamente como vecino con RandomCandidate
		State statecandidate = candidatevalue.stateCandidate(stateReferenceTS, typeCandidate, strategy, operatornumber, neighborhood);
		return statecandidate;
	}

	@Override
	/**
	 * updateReference - update the reference state and the list of visited states.
	 * @param stateCandidate 
	 * @param countIterationsCurrent 
	 */
	public void updateReference(State stateCandidate, Integer countIterationsCurrent)throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		ifacceptCandidate = new FactoryAcceptCandidate();
		AcceptableCandidate candidate = ifacceptCandidate.createAcceptCandidate(typeAcceptation);
		Boolean acept = candidate.acceptCandidate(stateReferenceTS, stateCandidate);
		if(acept.equals(true))
		  stateReferenceTS = stateCandidate;

		if (strategy.equals(StrategyType.TABU) && acept.equals(true)) {
			if (TabuSolutions.listTabu.size() < TabuSolutions.maxelements) {
				Boolean find = false;
				int count = 0;
				while ((TabuSolutions.listTabu.size() > count) && (find.equals(false))) {
					if (TabuSolutions.listTabu.get(count).equals(stateCandidate)) {
						find = true;
					}
					count++;
				}
				if (find.equals(false)) {
					TabuSolutions.listTabu.add(stateCandidate);
				}
			} else {
				TabuSolutions.listTabu.remove(0);
				Boolean find = false;
				int count = 0;
				while (TabuSolutions.listTabu.size() > count && find.equals(false)) {
					if (TabuSolutions.listTabu.get(count).equals(stateCandidate)) {
						find = true;
					}
					count++;
				}
				if (find.equals(false)) {
					TabuSolutions.listTabu.add(stateCandidate);
				}
			}
	}
		getReferenceList();
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
		listStateReference.add(stateReferenceTS);
		return listStateReference;
	}

	@Override
	/**
	 * getReference - get the reference state.
	 * @return the reference state
	 */
	public State getReference() {
		return stateReferenceTS;
	}

	@Override
	/**
	 * setInitialReference - set the initial reference state.
	 * @param stateInitialRef the initial reference state to set
	 */
	public void setInitialReference(State stateInitialRef) {
		this.stateReferenceTS = stateInitialRef;
	}

	/**
	 * setStateRef - set the reference state.
	 * @param stateRef 
	 */
	public void setStateRef(State stateRef) {
		this.stateReferenceTS = stateRef;
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
	 * getWeight - get the weight of the solution.
	 * @return the weight of the solution
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
	 * getListCountBetterGender - get the list of counts of better solutions by gender.
	 * @return the list of counts of better solutions by gender
	 */
	public int[] getListCountBetterGender() {
		// TODO Auto-generated method stub
		// This generator doesn't maintain listCount arrays; return empty array to avoid nulls
		return new int[0];
	}

	@Override
	/**
	 * getListCountGender - get the list of counts of solutions by gender.
	 * @return the list of counts of solutions by gender
	 */
	public int[] getListCountGender() {
		// TODO Auto-generated method stub
		// This generator doesn't maintain listCount arrays; return empty array to avoid nulls
		return new int[0];
	}

	@Override
	/**
	 * getTrace - get the trace of the solution.
	 * @return the trace of the solution
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


