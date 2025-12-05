package metaheuristics.generators;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import metaheurictics.strategy.Strategy;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Arrays;

import problem.definition.State;
import problem.definition.Problem.ProblemType;
import evolutionary_algorithms.complement.FatherSelection;
import evolutionary_algorithms.complement.Mutation;
import evolutionary_algorithms.complement.MutationType;
import evolutionary_algorithms.complement.Replace;
import evolutionary_algorithms.complement.ReplaceType;
import evolutionary_algorithms.complement.SelectionType;
import factory_interface.IFFactoryFatherSelection;
import factory_interface.IFFactoryMutation;
import factory_interface.IFFactoryReplace;
import factory_method.FactoryFatherSelection;
import factory_method.FactoryMutation;
import factory_method.FactoryReplace;

/**
 * EvolutionStrategies - class that implements the Evolution Strategies generator.
 */
public class EvolutionStrategies extends Generator {
	
	private State stateReferenceES;
	private List<State> listStateReference = new ArrayList<State>(); 
	private IFFactoryFatherSelection iffatherselection;
	private IFFactoryMutation iffactorymutation;
	private IFFactoryReplace iffreplace;
	private GeneratorType generatorType;
	public static final double PM = 0.0;
	public static final MutationType mutationType = MutationType.OnePointMutation;
	public static final ReplaceType replaceType = ReplaceType.GenerationalReplace;
	public static final SelectionType selectionType = SelectionType.TruncationSelection;
	public static final int countRef = 0;
	public static final int truncation = 0;
	private float weight = 50;
	
	//problemas dinamicos
	//problemas dinamicos: use instance fields from Generator (countGender, countBetterGender)
	private int[] listCountBetterGender = new int[10];
	private int[] listCountGender = new int[10];
	private float[] listTrace = new float[1200000];
	
	/**
	 * EvolutionStrategies - method to create an instance of EvolutionStrategies.
	 */
	public EvolutionStrategies() {
		super();
		this.listStateReference = getListStateRef(); 
		this.generatorType = GeneratorType.EvolutionStrategies;
		this.weight = 50;
		listTrace[0] = this.weight;
		listCountBetterGender[0] = 0;
		listCountGender[0] = 0;
	}
	/**
     * Uses ThreadLocalRandom for algorithmic (non-cryptographic) randomness.
     * This RNG is used to decide whether a mutation occurs in the
     * evolutionary algorithm and is not used for security-sensitive purposes.
     * Suppress Sonar security hotspot S2245 for this usage.
     */
    @SuppressWarnings("squid:S2245")
	@Override
	/**
	 * generate - method to generate a new state.
	 * @param operatornumber 
	 * @return returns the generated state.
	 */
	public State generate(Integer operatornumber) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException,	NoSuchMethodException {

    	iffatherselection = new FactoryFatherSelection();
    	FatherSelection selection = iffatherselection.createSelectFather(selectionType);
    	List<State> fathers = selection.selection(this.listStateReference, truncation);
    	int pos1 = (fathers.size() > 0) ? ThreadLocalRandom.current().nextInt(fathers.size()) : 0;
    	State candidate = (State) Strategy.getStrategy().getProblem().getState().getCopy();
    	candidate.setCode(new ArrayList<Object>(fathers.get(pos1).getCode()));
    	candidate.setEvaluation(fathers.get(pos1).getEvaluation());
    	candidate.setNumber(fathers.get(pos1).getNumber());
    	candidate.setTypeGenerator(fathers.get(pos1).getTypeGenerator());
    	
    	//**********mutacion******************************************** 	
    	iffactorymutation = new FactoryMutation();
    	Mutation mutation = iffactorymutation.createMutation(mutationType);
    	candidate = mutation.mutation(candidate, PM);
    	//list.add(candidate);    	
    	return candidate;
	}

	@Override
	/**
	 * getReference - method to get the current reference state.
	 * @return returns the current reference state.
	 */
	public State getReference() {
		if (listStateReference == null || listStateReference.isEmpty()) {
			return null;
		}
		stateReferenceES = listStateReference.get(0);
		if(Strategy.getStrategy().getProblem().getTypeProblem().equals(ProblemType.Maximizar)){
			for (int i = 1; i < listStateReference.size(); i++) {
				if(stateReferenceES.getEvaluation().get(0) < listStateReference.get(i).getEvaluation().get(0))
					stateReferenceES = listStateReference.get(i);
			}
		}
		else{
			for (int i = 1; i < listStateReference.size(); i++) {
				if(stateReferenceES.getEvaluation().get(0) > listStateReference.get(i).getEvaluation().get(0))
					stateReferenceES = listStateReference.get(i);
			}
		}
		return (stateReferenceES == null) ? null : new State(stateReferenceES);
	}
	
	/**
	 * setStateRef - method to set the current reference state.
	 * @param stateRef 
	 */
	public void setStateRef(State stateRef) {
		this.stateReferenceES = (stateRef == null) ? null : new State(stateRef);
	}

	@Override
	/**
	 * getType - method to get the generator type.
	 * @return returns the generator type.
	 */
	public GeneratorType getType() {
		return this.generatorType;
	}

	@Override
	/**
	 * setInitialReference - method to set the initial reference state.
	 * @param stateInitialRef 
	 */
	public void setInitialReference(State stateInitialRef) {
		this.stateReferenceES = (stateInitialRef == null) ? null : new State(stateInitialRef);
	}

	@Override
	/**
	 * updateReference - method to update the reference state.
	 * @param stateCandidate 
	 * @param countIterationsCurrent 
	 */
	public void updateReference(State stateCandidate, Integer countIterationsCurrent) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		iffreplace = new FactoryReplace();
		Replace replace = iffreplace.createReplace(replaceType);
		listStateReference = replace.replace(stateCandidate, listStateReference);

	}
	
	/**
	 * getListStateRef - method to get the list of reference states.
	 * @return returns the list of reference states.
	 */
	public List<State> getListStateRef(){
		Boolean found = false;
		List<String> key = Strategy.getStrategy().getListKey();
		int count = 0;
		while((found.equals(false)) && (Strategy.getStrategy().mapGenerators.size() > count)){
			if(key.get(count).equals(GeneratorType.EvolutionStrategies.toString())){
				GeneratorType keyGenerator = GeneratorType.valueOf(String.valueOf(key.get(count)));
				EvolutionStrategies generator = (EvolutionStrategies) Strategy.getStrategy().mapGenerators.get(keyGenerator);
				if(generator.getListStateReference().isEmpty()){
					listStateReference.addAll(RandomSearch.listStateReference);
				}
				else{
					listStateReference = generator.getListStateReference();
				}
			        found = true;
			}
			count++;
		}
		return (listStateReference == null) ? new ArrayList<State>() : new ArrayList<State>(listStateReference);
	}

	/**
	 * getListStateReference - method to get the list of reference states.
	 * @return returns the list of reference states.
	 */
	public List<State> getListStateReference() {
		return (listStateReference == null) ? new ArrayList<State>() : new ArrayList<State>(listStateReference);
	}

	/**
	 * setListStateReference - method to set the list of reference states.
	 * @param listStateReference 
	 */
	public void setListStateReference(List<State> listStateReference) {
		this.listStateReference = (listStateReference == null) ? new ArrayList<State>() : new ArrayList<State>(listStateReference);
	}

	/**
	 * getTypeGenerator - method to get the generator type.
	 * @return returns the generator type.
	 */
	public GeneratorType getTypeGenerator() {
		return generatorType;
	}

	/**
	 * setTypeGenerator - method to set the generator type.
	 * @param generatorType 
	 */
	public void setTypeGenerator(GeneratorType generatorType) {
		this.generatorType = generatorType;
	}

	@Override
	/**
	 * getReferenceList - method to get the list of reference states.
	 * @return returns the list of reference states.
	 */
	public List<State> getReferenceList() {
		List<State> ReferenceList = new ArrayList<State>();
		for (int i = 0; i < listStateReference.size(); i++) {
			State value = listStateReference.get(i);
			ReferenceList.add(value);
		}
		return new ArrayList<State>(ReferenceList);
	}

	@Override
	/**
	 * getSonList - method to get the list of son states.
	 * @return returns the list of son states.
	 */
	public List<State> getSonList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	/**
	 * awardUpdateREF - method to award the update of the reference state.
	 * @param stateCandidate 
	 * @return returns true if the update is awarded, false otherwise.
	 */
	public boolean awardUpdateREF(State stateCandidate) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	/**
	 * getWeight - method to get the weight.
	 * @return returns the weight.
	 */
	public float getWeight() {
		// TODO Auto-generated method stub
		return this.weight;
	}

	@Override
	/**
	 * setWeight - method to set the weight.
	 * @param weight 
	 */
	public void setWeight(float weight) {
		// TODO Auto-generated method stub
		this.weight = weight;
	}

	@Override
	/**
	 * getListCountBetterGender - method to get the list of count better gender.
	 * @return 
	 */
	public int[] getListCountBetterGender() {
		// TODO Auto-generated method stub
		return (this.listCountBetterGender == null) ? new int[0] : Arrays.copyOf(this.listCountBetterGender, this.listCountBetterGender.length);
	}

	@Override
	/**
	 * getListCountGender - method to get the list of count gender.
	 * @return returns the list of count gender.
	 */
	public int[] getListCountGender() {
		// TODO Auto-generated method stub
		return (this.listCountGender == null) ? new int[0] : Arrays.copyOf(this.listCountGender, this.listCountGender.length);
	}

	@Override
	/**
	 * getTrace - method to get the trace.
	 * @return returns the trace.
	 */
	public float[] getTrace() {
		// TODO Auto-generated method stub
		return (this.listTrace == null) ? new float[0] : Arrays.copyOf(this.listTrace, this.listTrace.length);
	}

}
