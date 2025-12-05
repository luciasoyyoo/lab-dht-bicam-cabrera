package metaheurictics.strategy;

import java.lang.reflect.InvocationTargetException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import factory_interface.IFFactoryGenerator;
import factory_method.FactoryGenerator;

import problem.definition.Problem;
import problem.definition.State;
import problem.definition.Problem.ProblemType;

import local_search.acceptation_type.Dominance;
import local_search.complement.StopExecute;
import local_search.complement.UpdateParameter;
import metaheuristics.generators.DistributionEstimationAlgorithm;
import metaheuristics.generators.EvolutionStrategies;
import metaheuristics.generators.Generator;
import metaheuristics.generators.GeneratorType;
import metaheuristics.generators.GeneticAlgorithm;
import metaheuristics.generators.MultiGenerator;
import metaheuristics.generators.ParticleSwarmOptimization;
import metaheuristics.generators.RandomSearch;

/**
 * Strategy - 
 */
public class Strategy {

	private static Strategy strategy = null;
	private State bestState;
	private Problem problem;
	public SortedMap<GeneratorType, Generator> mapGenerators;
	private StopExecute stopexecute;
	private UpdateParameter updateparameter;
	private IFFactoryGenerator ifFactoryGenerator;
	private int countCurrent;
	private int countMax;
	public Generator generator;
	public double threshold;

	public List<State> listStates; //lista de todos los estados generados en cada iteracion
	public List<State> listBest; //lista de la mejor solucion en cada iteracion
	public List<State> listRefPoblacFinal = new ArrayList<State> (); //lista de soluciones no dominadas
	public Dominance notDominated; 

	
	public boolean saveListStates; //guardar lista de estados generados
	public boolean saveListBestStates; // guardar lista con los mejores estados encontrados en cada iteracion
	public boolean saveFreneParetoMonoObjetivo; //guardar lista de soluciones no dominadas de una ejecuci�n
	public boolean calculateTime; // calcular tiempo de ejecuci�n de un algoritmo
	
	//calculo del Tiempo inicial y final
	long initialTime;
	long finalTime;
	long timeExecute;
	
	public float[] listOfflineError = new float[100]; // para almacenar la metrica offlinePerformance
	public int countPeriodChange = 0; // cantidad de iteeraciones antes de un cambio
	public int countChange = 0;
	private int countPeriodo; // contador para saber cada cuantas iteraciones tiene que guardar en la lista listCountBetterGender de cada generador
	private int periodo; //contador para controlar el periodo que esta guardando


	/**
	 * Strategy - method constructor of Strategy singleton
	 */
	private Strategy(){
		super();
	}
	/**
	 * getStrategy - getter of Strategy singleton instance
	 * @return Strategy instance
	 */
	public static Strategy getStrategy() {
		if (strategy == null) {
			strategy = new Strategy();
		}
		return strategy;
	}

	/**
	 * executeStrategy - method to execute the strategy.
	 * @param countmaxIterations 
	 * @param countIterationsChange 
	 * @param operatornumber 
	 * @param generatorType 
	 */
	public void executeStrategy (int countmaxIterations, int countIterationsChange, int operatornumber, GeneratorType generatorType) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		// si se quiere calcular el tiempo de ejecucion del un algoritmo
		if(calculateTime == true){
			initialTime = System.currentTimeMillis();
		}
		this.countMax = countmaxIterations; // max cantidad de iteraciones
		//generar estado inicial de la estrategia
		Generator randomInitial = new RandomSearch();
		State initialState = randomInitial.generate(operatornumber);
		problem.Evaluate(initialState); //evaluar ese estado
		initialState.setTypeGenerator(generatorType);
		getProblem().setState(initialState);
		//si se va a salvar la lista de estados generados, adicionar el estado
		if(saveListStates ==  true){
			listStates = new ArrayList<State>(); //list de estados generados
			listStates.add(initialState);
		}
		//si se va a salvar la lista de mejores soluciones encontradas en cada iteracion
		if(saveListBestStates == true){
			listBest = new ArrayList<State>(); // list de mejores estados encontrados
			listBest.add(initialState);
		}
				if(saveFreneParetoMonoObjetivo == true){
				notDominated = new Dominance();
		}
		// crear el generador a ejecutar
		generator = newGenerator(generatorType);
		generator.setInitialReference(initialState);
		bestState = initialState;
		countCurrent = 0;
		listRefPoblacFinal = new ArrayList<State>();
		MultiGenerator multiGenerator = null;
		countPeriodChange = countIterationsChange;
		countChange = countIterationsChange;
		countPeriodo = countIterationsChange / 10; //cantidad de iteraciones de un periodo 
		//verificar que es portafolio e inicializar los generadores del portafolio
		if(generatorType.equals(GeneratorType.MultiGenerator)){
			initializeGenerators();
			MultiGenerator.initializeGenerators();
			MultiGenerator.listGeneratedPP.clear();
			// create a clone of the MultiGenerator using reflection to access protected clone()
			try {
				final java.lang.reflect.Method cloneMethod = generator.getClass().getDeclaredMethod("clone");
				// Grant privileged permission for reflective access to non-public clone()
				AccessController.doPrivileged(new PrivilegedAction<Void>() {
					/**
					 * run - method to grant privileged permission for reflective access to non-public clone()
					 * @return 
					 */
					public Void run() {
						cloneMethod.setAccessible(true);
						return null;
					}
				});
				multiGenerator = (MultiGenerator) cloneMethod.invoke(generator);
			} catch (Exception e) {
				throw new RuntimeException("Failed to clone MultiGenerator", e);
			}
		}
		else initialize(); //crea el mapa de generadores
		update(countCurrent);
		
		float sumMax = 0; // suma acumulativa para almacenar la evaluacion de la mejor solucion encotrada y calcular el OfflinePerformance
		int countOff = 0; // variable par contar los OfflinePerformance que se van salvando en el arreglo
		//ciclio de ejecuci�n del algoritmo
		while (!stopexecute.stopIterations(countCurrent, countmaxIterations)){
			//si se detecta un cambio
			if(countCurrent == countChange){
				//calcular offlinePerformance
				calculateOffLinePerformance(sumMax, countOff);
				countOff++;
				sumMax = 0;
//				countIterationsChange = countIterationsChange + countPeriodChange; // actualizar la cantidad de iteraciones
				//actualizar la referencia luego de un cambio
				updateRef(generatorType);
				countChange = countChange + countPeriodChange;
				//generar un nuevo candidato en la iteracion, dependiendo del generador
				State stateCandidate = null;
				if(generatorType.equals(GeneratorType.MultiGenerator)){
					if(countPeriodo == countCurrent){
						updateCountGender();
						countPeriodo = countPeriodo + countPeriodChange / 10;
						periodo = 0;
						MultiGenerator.activeGenerator.countBetterGender = 0;
					}
					updateWeight();//actualizar el peso de los generadores si se reinician cuando ocurre un cambio
					//generar el estado candidato de la iteraci�n
					stateCandidate = multiGenerator.generate(operatornumber);
					problem.Evaluate(stateCandidate);
					stateCandidate.setEvaluation(stateCandidate.getEvaluation());
					stateCandidate.setNumber(countCurrent);
					stateCandidate.setTypeGenerator(generatorType);
					multiGenerator.updateReference(stateCandidate, countCurrent);
				}
				else {
					stateCandidate = generator.generate(operatornumber);
					problem.Evaluate(stateCandidate);
					stateCandidate.setEvaluation(stateCandidate.getEvaluation());
					stateCandidate.setNumber(countCurrent);
					stateCandidate.setTypeGenerator(generatorType);
					generator.updateReference(stateCandidate, countCurrent);
					if(saveListStates ==  true){
						listStates.add(stateCandidate);
					}
//					listStates.add(stateCandidate);
				}
				//actualizar el mejor estado encontrado solo tiene sentido para algoritmos monoobjetivos
				
				//actualizar el mejor estado encontrado solo tiene sentido para algoritmos monoobjetivos
				if ((getProblem().getTypeProblem().equals(ProblemType.Maximizar)) && bestState.getEvaluation().get(bestState.getEvaluation().size() - 1) < stateCandidate.getEvaluation().get(bestState.getEvaluation().size() - 1)) {
					bestState = stateCandidate;
				}
				if ((problem.getTypeProblem().equals(ProblemType.Minimizar)) && bestState.getEvaluation().get(bestState.getEvaluation().size() - 1) > stateCandidate.getEvaluation().get(bestState.getEvaluation().size() - 1)) {
					bestState = stateCandidate;
				}
				//					System.out.println("Evaluacion: "+ bestState.getEvaluation());
				if(saveListBestStates == true){
					listBest.add(bestState);
				}
				sumMax = (float) (sumMax + bestState.getEvaluation().get(0));

			}
			// no ha ocurrido un cambio
			else {
				State stateCandidate = null;
				if(generatorType.equals(GeneratorType.MultiGenerator)){
					if(countPeriodo == countCurrent){
						updateCountGender();
						countPeriodo = countPeriodo + countPeriodChange / 10;
						periodo++;
						MultiGenerator.activeGenerator.countBetterGender = 0;
					}
					stateCandidate = multiGenerator.generate(operatornumber);
					problem.Evaluate(stateCandidate);
					stateCandidate.setEvaluation(stateCandidate.getEvaluation());
					stateCandidate.setNumber(countCurrent);
					stateCandidate.setTypeGenerator(generatorType);
					multiGenerator.updateReference(stateCandidate, countCurrent);
				}
				else {
					//generar estado candidato y evaluar si es aceptado o no 
					stateCandidate = generator.generate(operatornumber);
					problem.Evaluate(stateCandidate);
					stateCandidate.setEvaluation(stateCandidate.getEvaluation());
					stateCandidate.setNumber(countCurrent);
					stateCandidate.setTypeGenerator(generatorType);
					generator.updateReference(stateCandidate, countCurrent); // actualizar la referencia del estado
					if(saveListStates ==  true){
						listStates.add(stateCandidate);
					}
					if(saveFreneParetoMonoObjetivo == true){
						notDominated.listDominance(stateCandidate, listRefPoblacFinal);
					}
				}
				countCurrent = UpdateParameter.updateParameter(countCurrent);
				//actualizar el mejor estado encontrado solo tiene sentido para algoritmos monoobjetivos
				if ((getProblem().getTypeProblem().equals(ProblemType.Maximizar)) && bestState.getEvaluation().get(bestState.getEvaluation().size() - 1) < stateCandidate.getEvaluation().get(bestState.getEvaluation().size() - 1)) {
					bestState = stateCandidate;
				}
				if ((problem.getTypeProblem().equals(ProblemType.Minimizar)) && bestState.getEvaluation().get(bestState.getEvaluation().size() - 1) > stateCandidate.getEvaluation().get(bestState.getEvaluation().size() - 1)) {
					bestState = stateCandidate;
				}
				//					System.out.println("Evaluacion: "+ bestState.getEvaluation());
				if(saveListBestStates == true){
					listBest.add(bestState);
				}
				sumMax = (float) (sumMax + bestState.getEvaluation().get(0));
			}
//			System.out.println("Iteracion: " + countCurrent);
		}
		//calcular tiempo final
		if(calculateTime == true){
			finalTime = System.currentTimeMillis();
			timeExecute = finalTime - initialTime;
			System.out.println("El tiempo de ejecucion: " + timeExecute);
		}
		if(generatorType.equals(GeneratorType.MultiGenerator)){
			listBest = new ArrayList<State>(multiGenerator.getReferenceList());
			//calcular offlinePerformance
			calculateOffLinePerformance(sumMax, countOff);
			if(countPeriodo == countCurrent){
				updateCountGender();
			}
		}
		else{
			listBest = new ArrayList<State>(generator.getReferenceList());
			calculateOffLinePerformance(sumMax, countOff);
		} 
	}
	
	public void updateCountGender(){ // actualizar la cantidad de mejoras y cantidad de veces que se uso un generador en un periodo dado
		for (int i = 0; i < MultiGenerator.getListGenerators().length; i++) {
			if(!MultiGenerator.getListGenerators()[i].getType().equals(GeneratorType.MultiGenerator) ){
				MultiGenerator.getListGenerators()[i].getListCountGender()[periodo] = MultiGenerator.getListGenerators()[i].countGender + MultiGenerator.getListGenerators()[i].getListCountGender()[periodo];
				MultiGenerator.getListGenerators()[i].getListCountBetterGender()[periodo] = MultiGenerator.getListGenerators()[i].countBetterGender + MultiGenerator.getListGenerators()[i].getListCountBetterGender()[periodo];
				MultiGenerator.getListGenerators()[i].countGender = 0;
				MultiGenerator.getListGenerators()[i].countBetterGender = 0;
			}
		}
	}
	
	/**
	 * updateWeight - method to update the weight of the generators.
	 */
	public void updateWeight(){
		for (int i = 0; i < MultiGenerator.getListGenerators().length; i++) {
			if(!MultiGenerator.getListGenerators()[i].getType().equals(GeneratorType.MultiGenerator)){
				MultiGenerator.getListGenerators()[i].setWeight((float) 50.0);
			}
		}
	}
	
	
	public void update(Integer countIterationsCurrent) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {//HashMap<String, Object> map, 
		//		Here update parameter for update and change generator.
		if(countIterationsCurrent.equals(GeneticAlgorithm.countRef - 1)){
			ifFactoryGenerator = new FactoryGenerator();
			Strategy.getStrategy().generator = ifFactoryGenerator.createGenerator(GeneratorType.GeneticAlgorithm);
		}
		if(countIterationsCurrent.equals(EvolutionStrategies.countRef - 1)){
			ifFactoryGenerator = new FactoryGenerator();
			Strategy.getStrategy().generator = ifFactoryGenerator.createGenerator(GeneratorType.EvolutionStrategies);
		}			
		if(countIterationsCurrent.equals(DistributionEstimationAlgorithm.countRef - 1)){
			ifFactoryGenerator = new FactoryGenerator();
			Strategy.getStrategy().generator = ifFactoryGenerator.createGenerator(GeneratorType.DistributionEstimationAlgorithm);
		}
		if(countIterationsCurrent.equals(ParticleSwarmOptimization.getCountRef() - 1)){
			ifFactoryGenerator = new FactoryGenerator();
			Strategy.getStrategy().generator = ifFactoryGenerator.createGenerator(GeneratorType.ParticleSwarmOptimization);
		}
	}

	/**
	 * newGenerator - method to create a new generator.
	 * @param Generatortype 
	 * @return return a Generator
	 */
	public Generator newGenerator(GeneratorType Generatortype) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		ifFactoryGenerator = new FactoryGenerator();
		Generator generator = ifFactoryGenerator.createGenerator(Generatortype);
		return generator;
	}

	/**
	 * getBestState - method to get the best state.
	 * @return return the best state
	 */
	public State getBestState() {
		return bestState;
	}

	/**
	 * setBestState - method to set the best state.
	 * @param besState 
	 */
	public void setBestState(State besState) {
		this.bestState = besState;
	}
	/**
	 * getStopexecute - method to get the stop execute.
	 * @return return the stop execute
	 */
	public StopExecute getStopexecute() {
		return stopexecute;
	}

	/**
	 * getCountMax - method to get the maximum count.
	 * @return return the maximum count
	 */
	public int getCountMax() {
		return countMax;
	}

	/**
	 * setCountMax - method to set the maximum count.
	 * @param countMax 
	 */
	public void setCountMax(int countMax) {
		this.countMax = countMax;
	}

	/**
	 * setStopexecute - method to set the stop execute.
	 * @param stopexecute 
	 */
	public void setStopexecute(StopExecute stopexecute) {
		this.stopexecute = stopexecute;
	}

	/**
	 * getUpdateparameter - method to get the update parameter.
	 * @return return the update parameter
	 */
	public UpdateParameter getUpdateparameter() {
		return updateparameter;
	}

	/**
	 * setUpdateparameter - method to set the update parameter.
	 * @param updateparameter 
	 */
	public void setUpdateparameter(UpdateParameter updateparameter) {
		this.updateparameter = updateparameter;
	}

	/**
	 * getProblem - method to get the problem.
	 * @return return the problem
	 */
	public Problem getProblem() {
		return problem;
	}

	/**
	 * setProblem - method to set the problem.
	 * @param problem 
	 */
	public void setProblem(Problem problem) {
		this.problem = problem;
	}

	/**
	 * getListKey - method to get the list of keys.
	 * @return return the list of keys
	 */
	public ArrayList<String> getListKey(){
		ArrayList<String> listKeys = new ArrayList<String>();
		String key = mapGenerators.keySet().toString();
		String returnString = key.substring(1, key.length() - 1);
		returnString = returnString + ", ";
		int countKey = mapGenerators.size();
		for (int i = 0; i < countKey; i++) {
			String r = returnString.substring(0, returnString.indexOf(','));
			returnString = returnString.substring(returnString.indexOf(',') + 2);
			listKeys.add(r);
		}
		return listKeys;
	}

	/**
	 * initializeGenerators - method to initialize the generators.
	 */
	public void initializeGenerators()throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		List<GeneratorType>	listType = new ArrayList<GeneratorType>();
		this.mapGenerators = new TreeMap<GeneratorType, Generator>();
		GeneratorType type[]= GeneratorType.values();
		for (GeneratorType generator : type) {
			listType.add(generator);
		}
		for (int i = 0; i < listType.size(); i++) {
			Generator generator = newGenerator(listType.get(i));
			mapGenerators.put(listType.get(i), generator);
		}
	}

	/**
	 * initialize - method to initialize the generators.
	 */
	public void initialize()throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		List<GeneratorType>	listType = new ArrayList<GeneratorType>();
		this.mapGenerators = new TreeMap<GeneratorType, Generator>();
		GeneratorType type[]= GeneratorType.values();
		for (GeneratorType generator : type) {
			listType.add(generator);
		}
		for (int i = 0; i < listType.size(); i++) {
			Generator generator = newGenerator(listType.get(i));
			mapGenerators.put(listType.get(i), generator);
		}
	}

	/**
	 * getCountCurrent - method to get the current count.
	 * @return return the current count
	 */
	public int getCountCurrent() {
		return countCurrent;
	}

	/**
	 * setCountCurrent - method to set the current count.
	 * @param countCurrent 
	 */
	public void setCountCurrent(int countCurrent) {
		this.countCurrent = countCurrent;
	}

	public static void destroyExecute() {
		strategy = null;
		RandomSearch.listStateReference.clear();
	}
	
	/**
	 * getThreshold - method to get the threshold.
	 * @return return the threshold
	 */
	public double getThreshold() {
		return threshold;
	}

	/**
	 * setThreshold - method to set the threshold.
	 * @param threshold 
	 */
	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}
	
	/**
	 * calculateOffLinePerformance - method to calculate the offline performance.
	 * @param sumMax 
	 * @param countOff 
	 */
	public void calculateOffLinePerformance(float sumMax, int countOff){
		float off = sumMax / countPeriodChange;
		listOfflineError[countOff] = off;
	}
	
	/**
	 * updateRef - method to update the reference.
	 * @param generatorType 
	 */
	public void updateRef(GeneratorType generatorType){
		if(generatorType.equals(GeneratorType.MultiGenerator)){
			updateRefMultiG();
			bestState = MultiGenerator.listStateReference.get( MultiGenerator.listStateReference.size() - 1);
		}
		else{
			updateRefGenerator(generator);
			bestState = generator.getReference();
		}
	}
	/**
	 * updateRefMultiG - method to update the reference for multi-generators.
	 */
	public void updateRefMultiG() {
		for (int i = 0; i < MultiGenerator.getListGenerators().length; i++) {
			updateRefGenerator(MultiGenerator.getListGenerators()[i]);
		}
	}
	/**
	 * updateRefGenerator - method to update the reference for generators.
	 * @param generator 
	 */
	public void updateRefGenerator(Generator generator) {
		if(generator.getType().equals(GeneratorType.HillClimbing) || generator.getType().equals(GeneratorType.TabuSearch) || generator.getType().equals(GeneratorType.RandomSearch) || generator.getType().equals(GeneratorType.SimulatedAnnealing)){
			double evaluation = getProblem().getFunction().get(0).Evaluation(generator.getReference());
			generator.getReference().getEvaluation().set(0, evaluation);
		}
		if(generator.getType().equals(GeneratorType.GeneticAlgorithm) || generator.getType().equals(GeneratorType.DistributionEstimationAlgorithm) || generator.getType().equals(GeneratorType.EvolutionStrategies)){
			for (int j = 0; j < generator.getReferenceList().size(); j++) {
				double evaluation = getProblem().getFunction().get(0).Evaluation(generator.getReferenceList().get(j));
				generator.getReferenceList().get(j).getEvaluation().set(0, evaluation);
			}
		}
	}

}
