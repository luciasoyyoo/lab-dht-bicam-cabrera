package metaheuristics.generators;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import factory_method.FactoryGenerator;

import metaheurictics.strategy.Strategy;

import problem.definition.Problem.ProblemType;
import problem.definition.State;

public class MultiGenerator extends Generator implements Cloneable {

	private GeneratorType generatortype;
	private static Generator[] listGenerators = new Generator[GeneratorType.values().length];
	public static final List<State> listGeneratedPP = Collections.synchronizedList(new ArrayList<State>());
	public static volatile Generator activeGenerator;
	public static final List<State> listStateReference = Collections.synchronizedList(new ArrayList<State>());
	
	public void setGeneratorType(GeneratorType generatortype) {
		this.generatortype = generatortype;
	}

	public MultiGenerator(){
		super();
	this.generatortype = GeneratorType.MultiGenerator;
	}
	

	public static void destroyMultiGenerator(){
		listGeneratedPP.clear();
		//listGenerators.clear();
		listStateReference.clear();
		activeGenerator = null;
		listGenerators = null;
	}
	
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
	
	public static void initializeGenerators() throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
//		Strategy.getStrategy().initializeGenerators();
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
		
		/*InstanceGA instanceGA = new InstanceGA();
		Thread threadGA = new Thread(instanceGA);
		
		InstanceEE instanceEE = new InstanceEE();
		Thread threadEE = new Thread(instanceEE);
		
		InstanceDE instanceDE = new InstanceDE();
		Thread threadDE = new Thread(instanceDE);
		
		threadGA.start();
		threadEE.start();
		threadDE.start();
		
		boolean stop = false;
		while (stop == false){
			if(instanceEE.isTerminate() == true && instanceGA.isTerminate() == true && instanceDE.isTerminate() == true) 
				stop = true;
		}*/
	}

	
	public static void createInstanceGeneratorsBPP() {
//		int i = 0;
//		boolean find = false;
		Generator generator = new RandomSearch();
//		while (find == false) {
//			if (listGenerators[i].getType().equals(GeneratorType.RandomSearch)) {
//				generator = listGenerators[i];
//				find = true;
//			}
//			else i++;
//		}
		int j = 0;
		while (j < EvolutionStrategies.countRef){
			State stateCandidate;
			try {
				stateCandidate = generator.generate(1);
				Strategy.getStrategy().getProblem().Evaluate(stateCandidate);
				//stateCandidate.setEvaluation(stateCandidate.getEvaluation());
				
				//stateCandidate = generator.generate(1);
				//Double evaluation = Strategy.getStrategy().getProblem().getFunction().get(0).Evaluation(stateCandidate);
				//stateCandidate.getEvaluation().set(0, evaluation);
				stateCandidate.setNumber(j);
				stateCandidate.setTypeGenerator(generator.getType());
				listGeneratedPP.add(stateCandidate);
			} catch (Exception e) {
				e.printStackTrace();
			} 
			j++;
		}
	}
	
	private static List<State> getListGeneratedPP() {
		return listGeneratedPP;
	}

	public static Generator[] getListGenerators() {
		// return a defensive copy to avoid exposing internal static array
		return (listGenerators == null) ? null : Arrays.copyOf(listGenerators, listGenerators.length);
	}

	public static void setListGenerators(Generator[] listGenerators) {
		// store a defensive copy to avoid keeping a reference to caller's mutable array
		MultiGenerator.listGenerators = (listGenerators == null) ? null : Arrays.copyOf(listGenerators, listGenerators.length);
	}

	public static Generator getActiveGenerator() {
		return activeGenerator;
	}

	public static void setActiveGenerator(Generator activeGenerator) {
		MultiGenerator.activeGenerator = activeGenerator;
	}

	public static void setListGeneratedPP(List<State> newListGeneratedPP) {
		synchronized (listGeneratedPP) {
			listGeneratedPP.clear();
			if (newListGeneratedPP != null) {
				listGeneratedPP.addAll(newListGeneratedPP);
			}
		}
	}

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
	public State getReference() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<State> getReferenceList() {
		// TODO Auto-generated method stub
		return listStateReference;
	}

	@Override
	public List<State> getSonList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GeneratorType getType() {
		return this.generatortype;
	}

	@Override
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
		/*if(Strategy.getStrategy().getProblem().getTypeProblem().equals(ProblemType.Maximizar)){
			if((stateCandidate.getEvaluation() > listStateReference.get(listStateReference.size() - 1).getEvaluation()))
				listStateReference.add(stateCandidate);
			else listStateReference.add(listStateReference.get(listStateReference.size() - 1)); 
		}
		else{
			if((Strategy.getStrategy().getProblem().getTypeProblem().equals(ProblemType.Minimizar)) && (stateCandidate.getEvaluation() < listStateReference.get(listStateReference.size() - 1).getEvaluation()))
				listStateReference.add(stateCandidate);
			else listStateReference.add(listStateReference.get(listStateReference.size() - 1));
		} */
	}
	
	public void updateWeight(State stateCandidate) { 
		boolean search = searchState(stateCandidate);//premio por calidad. 
		if(search == false)
			updateAwardImp();
		else updateAwardSC();
	}
	
	public boolean searchState(State stateCandidate) {
		if(Strategy.getStrategy().getProblem().getTypeProblem().equals(ProblemType.Maximizar)){
			if(stateCandidate.getEvaluation().get(0) > Strategy.getStrategy().getBestState().getEvaluation().get(0)){
				if(stateCandidate.getEvaluation().get(0) > Strategy.getStrategy().getBestState().getEvaluation().get(0))
					activeGenerator.countBetterGender++;
//				System.out.println(activeGenerator.getType().toString() + activeGenerator.countBetterGender);
//				System.out.println(activeGenerator.countBetterGender);
				return true;
			}
			else return false;
		}
		else {
			if(stateCandidate.getEvaluation().get(0) < Strategy.getStrategy().getBestState().getEvaluation().get(0)){
				if(stateCandidate.getEvaluation().get(0) < Strategy.getStrategy().getBestState().getEvaluation().get(0))
					activeGenerator.countBetterGender++;
//				System.out.println(activeGenerator.getType().toString() + activeGenerator.countBetterGender);
//				System.out.println(activeGenerator.countBetterGender);
				return true;
			}
			else return false;
		}
		
		
	}
	
	@Override
	public float getWeight() {
		// TODO Auto-generated method stub
		return 0;
	}
	
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
		float numbAleatory = (float) (Math.random() * (double)(1));
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
	public boolean awardUpdateREF(State stateCandidate) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@SuppressWarnings("static-access")
	public void updateAwardSC() {
		float weightLast = activeGenerator.getWeight();
		float weightUpdate = (float) (weightLast * (1 - 0.1) + 10);
		activeGenerator.setWeight(weightUpdate);
//		activeGenerator.getTrace()getTrace().add(weightUpdate);
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
	public void updateAwardImp() {
		float weightLast = activeGenerator.getWeight();
		float weightUpdate = (float) (weightLast * (1 - 0.1));
		activeGenerator.setWeight(weightUpdate);
//		activeGenerator.getTrace().add(weightUpdate);
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
	public void setWeight(float weight) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public float[] getTrace() {
		// TODO Auto-generated method stub
		// MultiGenerator does not maintain a single trace array; return empty array to avoid nulls
		return new float[0];
	}
	
	@SuppressWarnings("static-access")
	public void tournament(State stateCandidate,Integer countIterationsCurrent) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		State stateTem = new State(stateCandidate);
		for (int i = 0; i < MultiGenerator.getListGenerators().length; i++) {
				if(!listGenerators[i].getType().equals(GeneratorType.MultiGenerator))
					MultiGenerator.getListGenerators()[i].updateReference(stateTem, countIterationsCurrent);
		}
	}
	
	@Override
	public Object clone(){
		try{
			return super.clone();
		} catch (CloneNotSupportedException e) {
			return new MultiGenerator();
		}
	}

	@Override
	public int[] getListCountBetterGender() {
		// TODO Auto-generated method stub
		// MultiGenerator does not maintain these arrays itself; return empty array to avoid nulls
		return new int[0];
	}

	@Override
	public int[] getListCountGender() {
		// TODO Auto-generated method stub
		// MultiGenerator does not maintain these arrays itself; return empty array to avoid nulls
		return new int[0];
	}

}
