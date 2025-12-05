package metaheuristics.generators;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import metaheurictics.strategy.Strategy;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Arrays;

import problem.definition.State;
import problem.definition.Problem.ProblemType;

import evolutionary_algorithms.complement.Crossover;
import evolutionary_algorithms.complement.CrossoverType;
import evolutionary_algorithms.complement.FatherSelection;
import evolutionary_algorithms.complement.Mutation;
import evolutionary_algorithms.complement.MutationType;
import evolutionary_algorithms.complement.Replace;
import evolutionary_algorithms.complement.ReplaceType;
import evolutionary_algorithms.complement.SelectionType;
import factory_interface.IFFactoryCrossover;
import factory_interface.IFFactoryFatherSelection;
import factory_interface.IFFactoryMutation;
import factory_interface.IFFactoryReplace;
import factory_method.FactoryCrossover;
import factory_method.FactoryFatherSelection;
import factory_method.FactoryMutation;
import factory_method.FactoryReplace;

/**
 * GeneticAlgorithm - descripcion (añade detalles).
 */
public class GeneticAlgorithm extends Generator {

	private State stateReferenceGA;
	private List<State> listState = new ArrayList<State>(); 
	private IFFactoryFatherSelection iffatherselection;
	private IFFactoryCrossover iffactorycrossover;
	private IFFactoryMutation iffactorymutation;
	private IFFactoryReplace iffreplace;
	
	
//	private SelectionType selectionType;
//	private CrossoverType crossoverType;
//	private MutationType mutationType;
//	private ReplaceType replaceType;
	private GeneratorType generatorType;
	public static final MutationType mutationType = MutationType.OnePointMutation;
	public static final CrossoverType crossoverType = CrossoverType.UniformCrossover;
	public static final ReplaceType replaceType = ReplaceType.GenerationalReplace;
	public static final SelectionType selectionType = SelectionType.TruncationSelection;
	public static final double PC = 0.6;
	public static final double PM = 0.01;
	public static final int countRef = 0;
	public static final int truncation = 0;
	private float weight;
	
	//problemas dinamicos
	//problemas dinamicos: use instance fields from Generator (countGender, countBetterGender)
	private int[] listCountBetterGender = new int[10];
	private int[] listCountGender = new int[10];
	private float[] listTrace = new float[1200000];
	
    /**
     * GeneticAlgorithm - method to create an instance of GeneticAlgorithm.
     */
    public GeneticAlgorithm() {
		super();
		this.listState = getListStateRef(); // llamada al método que devuelve la lista.
		this.generatorType = GeneratorType.GeneticAlgorithm;
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
	 * @return 
	 */
	public State generate(Integer operatornumber) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
    	
		//******************selection*******************************

		List<State> refList = new ArrayList<State>(this.listState); 
    	iffatherselection = new FactoryFatherSelection();
    	FatherSelection selection = iffatherselection.createSelectFather(selectionType);
    	List<State> fathers = selection.selection(refList, truncation);
    	int pos1 = (fathers.size() > 0) ? ThreadLocalRandom.current().nextInt(fathers.size()) : 0;
    	int pos2 = (fathers.size() > 0) ? ThreadLocalRandom.current().nextInt(fathers.size()) : 0;
    	
    	State auxState1 = (State) Strategy.getStrategy().getProblem().getState().getCopy();
    	auxState1.setCode(new ArrayList<Object>(fathers.get(pos1).getCode()));
    	auxState1.setEvaluation(fathers.get(pos1).getEvaluation());
    	auxState1.setNumber(fathers.get(pos1).getNumber());
    	auxState1.setTypeGenerator(fathers.get(pos1).getTypeGenerator());
    	
    	State auxState2 = (State) Strategy.getStrategy().getProblem().getState().getCopy();
    	auxState2.setCode(new ArrayList<Object>(fathers.get(pos2).getCode()));
    	auxState2.setEvaluation(fathers.get(pos2).getEvaluation());
    	auxState2.setNumber(fathers.get(pos2).getNumber());
    	auxState2.setTypeGenerator(fathers.get(pos2).getTypeGenerator());
    	
	    //**********cruzamiento*************************************
	    iffactorycrossover = new FactoryCrossover();
    	Crossover crossover = iffactorycrossover.createCrossover(crossoverType);
    	auxState1 = crossover.crossover(auxState1, auxState2, PC);
	    
    	//**********mutacion******************************************** 	
    	iffactorymutation = new FactoryMutation();
    	Mutation mutation =iffactorymutation.createMutation(mutationType);
    	auxState1 = mutation.mutation(auxState1, PM);
    	//list.add(auxState1);
    	
    	return auxState1;
	}
    
	@Override
	/**
	 * getReference - method to get the current reference state.
	 * @return returns the current reference state.
	 */
	public State getReference() {
		if (listState == null || listState.isEmpty()) {
			return null;
		}
		stateReferenceGA = listState.get(0);
		if(Strategy.getStrategy().getProblem().getTypeProblem().equals(ProblemType.Maximizar)){
			for (int i = 1; i < listState.size(); i++) {
				if(stateReferenceGA.getEvaluation().get(0) < listState.get(i).getEvaluation().get(0))
					stateReferenceGA = listState.get(i);
			}
		}
		else{
			for (int i = 1; i < listState.size(); i++) {
				if(stateReferenceGA.getEvaluation().get(0) > listState.get(i).getEvaluation().get(0))
					stateReferenceGA = listState.get(i);
			}
		}
		return (stateReferenceGA == null) ? null : new State(stateReferenceGA);
	}

	/**
	 * setStateRef - method to set the current reference state.
	 * @param stateRef 
	 */
	public void setStateRef(State stateRef) {
		this.stateReferenceGA = (stateRef == null) ? null : new State(stateRef);
	}
	@Override
	/**
	 * setInitialReference - method to set the initial reference state.
	 * @param stateInitialRef 
	 */
	public void setInitialReference(State stateInitialRef) {
		this.stateReferenceGA = (stateInitialRef == null) ? null : new State(stateInitialRef);
	}

	@Override
	/**
	 * updateReference - method to update the current reference state.
	 * @param stateCandidate 
	 * @param countIterationsCurrent 
	 */
	public void updateReference(State stateCandidate, Integer countIterationsCurrent) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException,	NoSuchMethodException {
		iffreplace = new FactoryReplace();
		Replace replace = iffreplace.createReplace(replaceType);
		listState = replace.replace(stateCandidate, listState);
	}
	
	/**
	 * getListState - method to get the current list of states.
	 * @return returns the current list of states.
	 */
	public List<State> getListState() {
		return (listState == null) ? new ArrayList<State>() : new ArrayList<State>(listState);
	}

	/**
	 * setListState - method to set the current list of states.
	 * @param listState 
	 */
	public void setListState(List<State> listState) {
		this.listState = (listState == null) ? new ArrayList<State>() : new ArrayList<State>(listState);
	}
	
	/**
	 * getListStateRef - method to get the current list of reference states.
	 * @return returns the current list of reference states.
	 */
	public List<State> getListStateRef(){
		Boolean found = false;
		List<String> key = Strategy.getStrategy().getListKey();
		int count = 0;

		while((found.equals(false)) && (Strategy.getStrategy().mapGenerators.size() > count)){
			if(key.get(count).equals(GeneratorType.GeneticAlgorithm.toString())){
				GeneratorType keyGenerator = GeneratorType.valueOf(String.valueOf(key.get(count)));
				GeneticAlgorithm generator = (GeneticAlgorithm) Strategy.getStrategy().mapGenerators.get(keyGenerator);
				if(generator.getListState().isEmpty()){
					listState.addAll(RandomSearch.listStateReference);

				}
				else{
					listState = generator.getListState();
				}
			        found = true;
			}
			count++;
		}
		return (listState == null) ? new ArrayList<State>() : new ArrayList<State>(listState);
	}

	/**
	 * getGeneratorType - method to get the generator type.
	 * @return returns the generator type.
	 */
	public GeneratorType getGeneratorType() {
		return generatorType;
	}

	/**
	 * setGeneratorType - method to set the generator type.
	 * @param generatorType 
	 */
	public void setGeneratorType(GeneratorType generatorType) {
		this.generatorType = generatorType;
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
	 * getReferenceList - method to get the current list of reference states.
	 * @return returns the current list of reference states.
	 */
	public List<State> getReferenceList() {
		List<State> ReferenceList = new ArrayList<State>();
		for (int i = 0; i < listState.size(); i++) {
			State value = listState.get(i);
			ReferenceList.add(value);
		}
		return new ArrayList<State>(ReferenceList);
	}

	@Override
	/**
	 * getSonList - method to get the current list of son states.
	 * @return returns the current list of son states.
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
	 * @return returns the list of count better gender.
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
