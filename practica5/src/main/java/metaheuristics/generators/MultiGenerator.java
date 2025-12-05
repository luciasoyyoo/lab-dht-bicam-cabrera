package metaheuristics.generators;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

import factory_method.FactoryGenerator;

import metaheurictics.strategy.Strategy;

import problem.definition.Problem.ProblemType;
import problem.definition.State;

/**
 * MultiGenerator - class that implements the Multi-Generator metaheuristic.
 */
public class MultiGenerator extends Generator implements Cloneable {

	private GeneratorType generatortype;
	private static Generator[] listGenerators = new Generator[GeneratorType.values().length];
	public static final List<State> listGeneratedPP = Collections.synchronizedList(new ArrayList<State>());
	public static volatile Generator activeGenerator;
	public static final List<State> listStateReference = Collections.synchronizedList(new ArrayList<State>());
	private static final Logger LOGGER = Logger.getLogger(MultiGenerator.class.getName());
	
	/**
	 * setGeneratorType - set the type of the generator.
	 * @param generatortype the type of the generator to set
	 */
	public void setGeneratorType(GeneratorType generatortype) {
		this.generatortype = generatortype;
	}

	/**
	 * MultiGenerator - default constructor.
	 */
	public MultiGenerator(){
		super();
	this.generatortype = GeneratorType.MultiGenerator;
	}

	/**
	 * destroyMultiGenerator - destroy the MultiGenerator instance.
	 */
	public static void destroyMultiGenerator(){
		listGeneratedPP.clear();
		//listGenerators.clear();
		listStateReference.clear();
		activeGenerator = null;
		listGenerators = null;
	}
	
	/**
	 * initializeListGenerator - initialize the list of generators.
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public static void initializeListGenerator()throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		listGenerators = new Generator[4];
		Generator generator1 = new HillClimbing();
		Generator generator2 = new EvolutionStrategies();
		Generator generator3 = new LimitThreshold();
		Generator generator4 = new GeneticAlgorithm();
		listGenerators[0] = generator1;
		listGenerators[1] = generator2;
		listGenerators[2] = generator3;
		listGenerators[3] = generator4;
	}
	
	/**
	 * 	initializeGenerators - initialize the generators.
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public static void initializeGenerators() throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

		initializeListGenerator();
		State stateREF = new State(Strategy.getStrategy().getProblem().getState());
		listStateReference.add(stateREF);
		for (int i = 0; i < listGenerators.length; i++) {
			if ((listGenerators[i].getType().equals(GeneratorType.HillClimbing)) || (listGenerators[i].getType().equals(GeneratorType.RandomSearch)) || (listGenerators[i].getType().equals(GeneratorType.TabuSearch)) || (listGenerators[i].getType().equals(GeneratorType.SimulatedAnnealing) || (listGenerators[i].getType().equals(GeneratorType.LimitThreshold)))){
				listGenerators[i].setInitialReference(stateREF);
			}
		}
		createInstanceGeneratorsBPP();
		Strategy.getStrategy().listStates = MultiGenerator.getListGeneratedPP();
		
		FactoryGenerator ifFactoryGeneratorEE = new FactoryGenerator();
		Generator generatorEE = ifFactoryGeneratorEE.createGenerator(GeneratorType.EvolutionStrategies);
		
		FactoryGenerator ifFactoryGeneratorGA = new FactoryGenerator();
		Generator generatorGA = ifFactoryGeneratorGA.createGenerator(GeneratorType.GeneticAlgorithm);
		
		FactoryGenerator ifFactoryGeneratorEDA = new FactoryGenerator();
		Generator generatorEDA = ifFactoryGeneratorEDA.createGenerator(GeneratorType.DistributionEstimationAlgorithm);
		
		for (int i = 0; i < MultiGenerator.getListGenerators().length; i++) {
			if(MultiGenerator.getListGenerators()[i].getType().equals(GeneratorType.EvolutionStrategies)){
				MultiGenerator.getListGenerators()[i] = generatorEE;
			}
			if(MultiGenerator.getListGenerators()[i].getType().equals(GeneratorType.GeneticAlgorithm)){
				MultiGenerator.getListGenerators()[i] = generatorGA;
			}
			if(MultiGenerator.getListGenerators()[i].getType().equals(GeneratorType.DistributionEstimationAlgorithm)){
				MultiGenerator.getListGenerators()[i] = generatorEDA;
			}
		}
	
	}

	/**
	 * createInstanceGeneratorsBPP - create instances of generators for the BPP problem.
	 */
	public static void createInstanceGeneratorsBPP() {
		Generator generator = new RandomSearch();

		int j = 0;
		while (j < EvolutionStrategies.countRef){
			State stateCandidate;
			try {
				stateCandidate = generator.generate(1);
				Strategy.getStrategy().getProblem().Evaluate(stateCandidate);
				stateCandidate.setNumber(j);
				stateCandidate.setTypeGenerator(generator.getType());
				listGeneratedPP.add(stateCandidate);
			} catch (Exception e) {
				// Log exception instead of printing stack trace to avoid debug output in production
				LOGGER.log(Level.SEVERE, "Failed to create state candidate in createInstanceGeneratorsBPP", e);
			}
			j++;
		}
	}
	
	/**
	 * getListGeneratedPP - get the list of generated states for the PP problem.
	 * @return the list of generated states
	 */
	private static List<State> getListGeneratedPP() {
		return listGeneratedPP;
	}

	/**
	 * getListGenerators - get the list of generators.
	 * @return the list of generators
	 */
	public static Generator[] getListGenerators() {
		// return a defensive copy to avoid exposing internal static array
		return (listGenerators == null) ? null : Arrays.copyOf(listGenerators, listGenerators.length);
	}
	/**
	 * setListGenerators - set the list of generators.
	 * @param listGenerators the list of generators to set
	 */
	public static void setListGenerators(Generator[] listGenerators) {
		// store a defensive copy to avoid keeping a reference to caller's mutable array
		MultiGenerator.listGenerators = (listGenerators == null) ? null : Arrays.copyOf(listGenerators, listGenerators.length);
	}
	/**
	 * getActiveGenerator - get the active generator.
	 * @return the active generator
	 */
	public static Generator getActiveGenerator() {
		return activeGenerator;
	}
	/**
	 * setActiveGenerator - set the active generator.
	 * @param activeGenerator the active generator to set
	 */
	public static void setActiveGenerator(Generator activeGenerator) {
		MultiGenerator.activeGenerator = activeGenerator;
	}
	/**
	 * setListGeneratedPP - set the list of generated states for the PP problem.
	 * @param newListGeneratedPP the new list of generated states
	 */
	public static void setListGeneratedPP(List<State> newListGeneratedPP) {
		synchronized (listGeneratedPP) {
			listGeneratedPP.clear();
			if (newListGeneratedPP != null) {
				listGeneratedPP.addAll(newListGeneratedPP);
			}
		}
	}

	/**
	 * generate - generate a new state using the MultiGenerator.
	 * @param operatornumber
	 * @return
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	@Override
	public State generate(Integer operatornumber)
			throws IllegalArgumentException, SecurityException,
			ClassNotFoundException, InstantiationException,
			IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
	// TODO Auto-generated method stub
	Strategy.getStrategy().generator = roulette();
	MultiGenerator.setActiveGenerator(Strategy.getStrategy().generator);
		activeGenerator.countGender++;
		State state = Strategy.getStrategy().generator.generate(1);
		return state;
	}

	@Override
	/**
	 * getReference - get the reference state.
	 * @return the reference state
	 */
	public State getReference() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	/**
	 * getReferenceList - get the list of reference states.
	 * @return the list of reference states
	 */
	public List<State> getReferenceList() {
		// TODO Auto-generated method stub
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
	 * getType - get the type of the generator.
	 * @return the type of the generator
	 */
	public GeneratorType getType() {
		return this.generatortype;
	}

	@Override
	/**
	 * setInitialReference - set the initial reference state.
	 * @param stateInitialRef the initial reference state
	 */
	public void setInitialReference(State stateInitialRef) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateReference(State stateCandidate,
			Integer countIterationsCurrent) throws IllegalArgumentException,
			SecurityException, ClassNotFoundException, InstantiationException,
			IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		// TODO Auto-generated method stub
		updateWeight(stateCandidate);
		tournament(stateCandidate, countIterationsCurrent);
	}
	
	/**
	 * updateWeight - modify the weight of the generator.
	 * @param stateCandidate 
	 */
	public void updateWeight(State stateCandidate) { 
		boolean search = searchState(stateCandidate);//premio por calidad. 
		if(search == false)
			updateAwardImp();
		else updateAwardSC();
	}
	
	/**
	 * searchState - search for a state in the reference list.
	 * @param stateCandidate 
	 * @return return true if the state is found, false otherwise
	 */
	public boolean searchState(State stateCandidate) {
		if(Strategy.getStrategy().getProblem().getTypeProblem().equals(ProblemType.Maximizar)){
			if(stateCandidate.getEvaluation().get(0) > Strategy.getStrategy().getBestState().getEvaluation().get(0)){
				if(stateCandidate.getEvaluation().get(0) > Strategy.getStrategy().getBestState().getEvaluation().get(0))
					activeGenerator.countBetterGender++;
				return true;
			}
			else return false;
		}
		else {
			if(stateCandidate.getEvaluation().get(0) < Strategy.getStrategy().getBestState().getEvaluation().get(0)){
				if(stateCandidate.getEvaluation().get(0) < Strategy.getStrategy().getBestState().getEvaluation().get(0))
					activeGenerator.countBetterGender++;
				return true;
			}
			else return false;
		}
		
		
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
	
	/**
	 * roulette - select a generator using roulette wheel selection.
	 * @return the selected generator
	 */
	public Generator roulette() {
		float totalWeight = 0;
		for (int i = 0; i < listGenerators.length; i++) {
			totalWeight = listGenerators[i].getWeight() + totalWeight;
		}
		List<Float> listProb = new ArrayList<Float>();
		for (int i = 0; i < listGenerators.length; i++) {
			float probF = listGenerators[i].getWeight() / totalWeight;
			listProb.add(probF);
		}
		List<LimitRoulette> listLimit = new ArrayList<LimitRoulette>();
		float limitHigh = 0;
		float limitLow = 0;
		for (int i = 0; i < listProb.size(); i++) {
			LimitRoulette limitRoulette = new LimitRoulette();
			limitHigh = listProb.get(i) + limitHigh;
			limitRoulette.setLimitHigh(limitHigh);
			limitRoulette.setLimitLow(limitLow);
			limitLow = limitHigh;
			limitRoulette.setGenerator(listGenerators[i]);
			listLimit.add(limitRoulette);
		}
		// Uses ThreadLocalRandom for algorithmic (non-cryptographic) randomness.
		// This RNG chooses a generator according to roulette weights and is not
		// used for security-sensitive purposes. Suppress Sonar hotspot S2245.
		@SuppressWarnings("squid:S2245")
		float numbAleatory = ThreadLocalRandom.current().nextFloat();
		boolean find = false;
		int i = 0;
		while ((find == false) && (i < listLimit.size())){
			if((listLimit.get(i).getLimitLow() <= numbAleatory) && (numbAleatory <= listLimit.get(i).getLimitHigh())){
				find = true;
			}
			else i++;
		}
		if (find) {
			return listLimit.get(i).getGenerator();
		}
		else return listLimit.get(listLimit.size() - 1).getGenerator();
	}

	@Override
	/**
	 * awardUpdateREF - update the reference of the generator.
	 * @param stateCandidate 
	 * @return return true if the reference was updated, false otherwise
	 */
	public boolean awardUpdateREF(State stateCandidate) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@SuppressWarnings("static-access")
	/**
	 * updateAwardSC - update the award for the selected generator.
	 */
	public void updateAwardSC() {
		float weightLast = activeGenerator.getWeight();
		float weightUpdate = (float) (weightLast * (1 - 0.1) + 10);
		activeGenerator.setWeight(weightUpdate);
		for (int i = 0; i < listGenerators.length; i++) {
			if(listGenerators[i].equals(activeGenerator))
				activeGenerator.getTrace()[Strategy.getStrategy().getCountCurrent()] = weightUpdate;
			else{
				if(!listGenerators[i].getType().equals(GeneratorType.MultiGenerator)){
					float trace = listGenerators[i].getWeight();
					listGenerators[i].getTrace() [Strategy.getStrategy().getCountCurrent()] = trace;
				}
			}
		}
	}

	@SuppressWarnings("static-access")
	/**
	 * updateAwardImp - update the award for the selected generator.
	 */
	public void updateAwardImp() {
		float weightLast = activeGenerator.getWeight();
		float weightUpdate = (float) (weightLast * (1 - 0.1));
		activeGenerator.setWeight(weightUpdate);
		for (int i = 0; i < listGenerators.length; i++) {
			if(listGenerators[i].equals(activeGenerator))
				activeGenerator.getTrace()[Strategy.getStrategy().getCountCurrent()] = weightUpdate;
			else{
				if(!listGenerators[i].getType().equals(GeneratorType.MultiGenerator)){
					float trace = listGenerators[i].getWeight();
					listGenerators[i].getTrace() [Strategy.getStrategy().getCountCurrent()] = trace;
				}
			}
		}
	}
	
	@Override
	/**
	 * setWeight - set the weight of the generator.
	 * @param weight 
	 */
	public void setWeight(float weight) {
		// TODO Auto-generated method stub
		
	}
	@Override
	/**
	 * getTrace - get the trace of the generator.
	 * @return the trace array
	 */
	public float[] getTrace() {
		// TODO Auto-generated method stub
		// MultiGenerator does not maintain a single trace array; return empty array to avoid nulls
		return new float[0];
	}
	
	@SuppressWarnings("static-access")
	/**
	 * tournament - update the tournament for the selected generator.
	 * @param stateCandidate 
	 * @param countIterationsCurrent 
	 */
	public void tournament(State stateCandidate,Integer countIterationsCurrent) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		State stateTem = new State(stateCandidate);
		for (int i = 0; i < MultiGenerator.getListGenerators().length; i++) {
				if(!listGenerators[i].getType().equals(GeneratorType.MultiGenerator))
					MultiGenerator.getListGenerators()[i].updateReference(stateTem, countIterationsCurrent);
		}
	}
	
	@Override
	/**
	 * clone - create a copy of the generator.
	 * @return a clone of the generator
	 */
	public Object clone(){
		try{
			return super.clone();
		} catch (CloneNotSupportedException e) {
			return new MultiGenerator();
		}
	}

	@Override
	/**
	 * getListCountBetterGender - get the list of counts for better gender.
	 * @return the list of counts for better gender
	 */
	public int[] getListCountBetterGender() {
		// TODO Auto-generated method stub
		// MultiGenerator does not maintain these arrays itself; return empty array to avoid nulls
		return new int[0];
	}

	@Override
	/**
	 * getListCountGender - get the list of counts for gender.
	 * @return the list of counts for gender
	 */
	public int[] getListCountGender() {
		// TODO Auto-generated method stub
		// MultiGenerator does not maintain these arrays itself; return empty array to avoid nulls
		return new int[0];
	}

}
