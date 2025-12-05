package metaheuristics.generators;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import metaheurictics.strategy.Strategy;

import problem.definition.State;
import problem.definition.Problem.ProblemType;

import evolutionary_algorithms.complement.DistributionType;
import evolutionary_algorithms.complement.FatherSelection;
import evolutionary_algorithms.complement.Replace;
import evolutionary_algorithms.complement.ReplaceType;
import evolutionary_algorithms.complement.Sampling;
import evolutionary_algorithms.complement.SamplingType;
import evolutionary_algorithms.complement.SelectionType;
import factory_interface.IFFSampling;
import factory_interface.IFFactoryFatherSelection;
import factory_interface.IFFactoryReplace;
import factory_method.FactoryFatherSelection;
import factory_method.FactoryReplace;
import factory_method.FactorySampling;

/**
 * DistributionEstimationAlgorithm - class for distribution estimation algorithms.
 */
public class DistributionEstimationAlgorithm extends Generator {

	private State stateReferenceDA;
	private List<State> referenceList = new ArrayList<State>(); 
	private static final List<State> sonList = new ArrayList<State>(); 
	private IFFactoryFatherSelection iffatherselection;
	private IFFSampling iffsampling;
	private IFFactoryReplace iffreplace;
	private DistributionType distributionType;
	private SamplingType Samplingtype;
	
//	private ReplaceType replaceType;
	public static final ReplaceType replaceType = ReplaceType.GenerationalReplace;
	public static final SelectionType selectionType = SelectionType.TruncationSelection;
	
	private GeneratorType generatorType;
	//private ProblemState candidate;
	public static final int truncation = 0;
	public static final int countRef = 0;
	private float weight;
	
	//problemas dinamicos: use counters declared in superclass `Generator`
	private int[] listCountBetterGender = new int[10];
	private int[] listCountGender = new int[10];
	private float[] listTrace = new float[1200000];
	
	
	/**
	 * DistributionEstimationAlgorithm - constructor for distribution estimation algorithms.
	 */
	public DistributionEstimationAlgorithm() {
		super();
		this.referenceList = getListStateRef(); // llamada al método que devuelve la lista.
		this.generatorType = GeneratorType.DistributionEstimationAlgorithm;
		this.distributionType = DistributionType.Univariate;
		this.Samplingtype = SamplingType.ProbabilisticSampling;
		this.weight = 50;
		listTrace[0] = weight;
		listCountBetterGender[0] = 0;
		listCountGender[0] = 0;
	}
	
	/**
	 * maxValue - method to find the state with the maximum value.
	 * @param listInd 
	 * @return returns the state with the maximum evaluation value.
	 */
	public State maxValue(List<State> listInd) {
		State state = new State(listInd.get(0));
		double max = state.getEvaluation().get(0);
		for (int i = 1; i < listInd.size(); i++) {
			if (listInd.get(i).getEvaluation().get(0) > max) {
				max = listInd.get(i).getEvaluation().get(0);
				state = new State(listInd.get(i));
			}
		}
		return state;
	}
	
	@Override
	/**
	 * generate - method to generate a new state.
	 * @param operatornumber 
	 * @return returns the generated state.
	 */
	public State generate(Integer operatornumber) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException,	NoSuchMethodException {
		
    	List<State> fathers = getfathersList();
		iffsampling = new FactorySampling();
    	Sampling samplingG = iffsampling.createSampling(Samplingtype);
    	List<State> ind = samplingG.sampling(fathers, operatornumber);
    	State candidate = null;
    	if(ind.size() > 1){
    		for (int i = 0; i < ind.size(); i++) {
    			double evaluation = Strategy.getStrategy().getProblem().getFunction().get(0).Evaluation(ind.get(i));
    			ArrayList<Double> listEval = new ArrayList<Double>();
    			listEval.add(evaluation);
    			ind.get(0).setEvaluation(listEval);
    		}
        	candidate = maxValue(ind);
    	}
    	else{
    		candidate = ind.get(0);
    	}

    	return candidate;
    	
    }
    		
	@Override
	/**
	 * getReference - method to get the current reference state.
	 * @return returns the current reference state.
	 */
	public State getReference() {
		stateReferenceDA = referenceList.get(0);
		if(Strategy.getStrategy().getProblem().getTypeProblem().equals(ProblemType.Maximizar)){
			for (int i = 1; i < referenceList.size(); i++) {
				if(stateReferenceDA.getEvaluation().get(0) < referenceList.get(i).getEvaluation().get(0))
					stateReferenceDA = referenceList.get(i);
			}
		}
		else{
			for (int i = 1; i < referenceList.size(); i++) {
				if(stateReferenceDA.getEvaluation().get(0) > referenceList.get(i).getEvaluation().get(0))
					stateReferenceDA = referenceList.get(i);
			}
		}
		return stateReferenceDA;
	}

	@Override
	/**
	 * getReferenceList - method to get the list of reference states.
	 * @return returns the list of reference states.
	 */
	public List<State> getReferenceList() {
		List<State> ReferenceList = new ArrayList<State>();
		for (int i = 0; i < referenceList.size(); i++) {
			State value = referenceList.get(i);
			ReferenceList.add( value);
		}
		return ReferenceList;
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
		this.stateReferenceDA = stateInitialRef;
	}

	@Override
	/**
	 * updateReference - method to update the reference state.
	 * @param stateCandidate 
	 * @param countIterationsCurrent 
	 */
	public void updateReference(State stateCandidate, Integer countIterationsCurrent) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException,	NoSuchMethodException {
		iffreplace = new FactoryReplace();
		Replace replace = iffreplace.createReplace(replaceType);
		referenceList = replace.replace(stateCandidate, referenceList);
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
			if(key.get(count).equals(GeneratorType.DistributionEstimationAlgorithm.toString())){
				GeneratorType keyGenerator = GeneratorType.valueOf(String.valueOf(key.get(count)));
				DistributionEstimationAlgorithm generator = (DistributionEstimationAlgorithm)Strategy.getStrategy().mapGenerators.get(keyGenerator);
				if(generator.getListReference().isEmpty()){
					referenceList.addAll(RandomSearch.listStateReference);
				}
				else{
					referenceList = generator.getListReference();
				}
			    found = true;
			}
			count++;
		}
		return referenceList;
	}

	/**
	 * getListReference - method to get the list of reference states.
	 * @return returns the list of reference states.
	 */
	public List<State> getListReference() {
		return referenceList;
	}

	/**
	 * setListReference - method to set the list of reference states.
	 * @param listReference 
	 */
	public void setListReference(List<State> listReference) {
		referenceList = listReference;
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

	/**
	 * getfathersList - method to get the list of father states.
	 * @return returns the list of father states.
	 */
	public List<State> getfathersList() throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		List<State> refList = new ArrayList<State>(this.referenceList); 
    	iffatherselection = new FactoryFatherSelection();
    	FatherSelection selection = iffatherselection.createSelectFather(selectionType);
    	List<State> fathers = selection.selection(refList, truncation);
    	return fathers;
	}

	@Override
	/**
	 * getSonList - method to get the list of son states.
	 * @return returns the list of son states.
	 */
	public List<State> getSonList() {
		// return a defensive copy to avoid exposing internal mutable list
		return new ArrayList<State>(sonList);
	}

	/**
	 * awardUpdateREF - method to award the update of the reference state.
	 * @param stateCandidate 
	 * @return returns true if the candidate state is already in the reference list, false otherwise.
	 */
	public boolean awardUpdateREF(State stateCandidate) {
		boolean find = false;
		int i = 0;
		while (find == false && i < this.referenceList.size()) {
			if(stateCandidate.equals(this.referenceList.get(i)))
				find = true;
			else i++;
		}
		return find;
	}

	@Override
	/**
	 * getWeight - method to get the weight.
	 * @return returns the weight.
	 */
	public float getWeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	/**
	 * setWeight - method to set the weight.
	 * @param weight 
	 */
	public void setWeight(float weight) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * getDistributionType - method to get the distribution type.
	 * @return returns the distribution type.
	 */
	public DistributionType getDistributionType() {
		return distributionType;
	}

	/**
	 * setDistributionType - method to set the distribution type.
	 * @param distributionType the distribution type to set
	 */
	public void setDistributionType(DistributionType distributionType) {
		this.distributionType = distributionType;
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