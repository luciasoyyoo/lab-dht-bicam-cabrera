package problem.definition;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import problem.extension.SolutionMethod;
import problem.extension.TypeSolutionMethod;


import factory_interface.IFFactorySolutionMethod;
import factory_method.FactorySolutionMethod;


/**
 * Problem
 *
 * @brief Encapsula la definición del problema a resolver: funciones objetivo,
 *        codificación, operador generador de estados y método de solución.
 *
 * Esta clase actúa como contenedor de las piezas necesarias para evaluar y
 * manipular estados de solución dentro de los metaheurísticos. No modifica
 * el comportamiento de evaluación —solo documenta y expone getters/setters
 * usados por los generadores y estrategias.
 */
public class Problem {

	/**
	 * ProblemType - descripcion (añade detalles).
	 */
	public enum ProblemType {Maximizar,Minimizar;}

	private ArrayList<ObjetiveFunction> function;
	private State state;
	private ProblemType typeProblem;
	private Codification codification;
	private Operator operator;
	private int possibleValue;
	private TypeSolutionMethod typeSolutionMethod;
	private IFFactorySolutionMethod factorySolutionMethod;
	
	/**
	 * Constructor por defecto.
	 *
	 * Inicializa una instancia vacía; los componentes concretos (funciones,
	 * codificación, operador) se deben inyectar mediante los setters antes de
	 * usar {@link #Evaluate(State)}.
	 */
	public Problem() {
		super();
	}

	/**
	 * Obtener la lista de funciones objetivo del problema.
	 *
	 * @return copia defensiva de la lista de {@link ObjetiveFunction}; nunca
	 *         devuelve la referencia interna (puede ser lista vacía).
	 */
	public ArrayList<ObjetiveFunction> getFunction() {
		return (function == null) ? new ArrayList<ObjetiveFunction>() : new ArrayList<ObjetiveFunction>(function);
	}

	/**
	 * Establecer las funciones objetivo del problema.
	 *
	 * Se realiza una copia defensiva de la lista proporcionada.
	 *
	 * @param function lista de funciones objetivo
	 */
	public void setFunction(ArrayList<ObjetiveFunction> function) {
		this.function = (function == null) ? new ArrayList<ObjetiveFunction>() : new ArrayList<ObjetiveFunction>(function);
	}

	/**
	 * Obtener una copia del estado actual del problema.
	 *
	 * Devuelve una copia para evitar aliasing con la representación interna.
	 *
	 * @return copia de {@link State} o null si no hay estado inicial.
	 */
	public State getState() {
		return (state == null) ? null : new State(state);
	}

	/**
	 * Establecer el estado actual del problema (copia defensiva).
	 *
	 * @param state estado a asignar; si es null se resetea el estado interno.
	 */
	public void setState(State state) {
		this.state = (state == null) ? null : new State(state);
	}

	/**
	 * Obtener el tipo de problema (Maximizar / Minimizar).
	 *
	 * @return el {@link ProblemType} configurado para este problema
	 */
	public ProblemType getTypeProblem() {
		return typeProblem;
	}
	/**
	 * Establecer el tipo de problema.
	 *
	 * @param typeProblem tipo de problema (Maximizar o Minimizar)
	 */
	public void setTypeProblem(ProblemType typeProblem) {
		this.typeProblem = typeProblem;
	}

	/**
	 * Obtener la codificación usada para representar estados.
	 *
	 * @return la {@link Codification} configurada
	 */
	public Codification getCodification() {
		return codification;
	}
	/**
	 * Establecer la codificación de variables para el problema.
	 *
	 * @param codification implementación de {@link Codification}
	 */
	public void setCodification(Codification codification) {
		this.codification = codification;
	}

	/**
	 * Obtener el operador encargado de generar nuevos estados.
	 *
	 * @return operador configurado
	 */
	public Operator getOperator() {
		return operator;
	}

	/**
	 * Establecer el operador de generación de estados.
	 *
	 * @param operator operador que implementa la generación de estados
	 */
	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	/**
	 * Obtener el número de valores posibles (uso dependiente de la codificación).
	 *
	 * @return número de valores posibles
	 */
	public int getPossibleValue() {
		return possibleValue;
	}

	/**
	 * Establecer el número de valores posibles.
	 *
	 * @param possibleValue número de valores
	 */
	public void setPossibleValue(int possibleValue) {
		this.possibleValue = possibleValue;
	}

	/**
	 * Evaluar un estado de solución.
	 *
	 * Dependiendo del {@link #typeSolutionMethod} realiza una evaluación simple
	 * usando la primera función objetivo o delega en un {@link SolutionMethod}.
	 *
	 * @param state estado a evaluar (se modifica su lista de evaluaciones)
	 * @throws ReflectiveOperationException si falla la creación reflexiva del método de solución
	 */
	public void Evaluate(State state) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		double eval = 0;       
		ArrayList<Double> evaluation = new ArrayList<Double>(this.function.size());
		if (typeSolutionMethod == null) {
			eval= function.get(0).Evaluation(state);
			evaluation.add(evaluation.size(), eval);
			state.setEvaluation(evaluation);
		}
		else {
			SolutionMethod method = newSolutionMethod(typeSolutionMethod);
			method.evaluationState(state);
		}
	}
	
	/**
	 * Obtener el tipo de método de solución configurado.
	 *
	 * @return tipo de método de solución o null
	 */
	public TypeSolutionMethod getTypeSolutionMethod() {
		return typeSolutionMethod;
	}
	/**
	 * Establecer el tipo de método de solución.
	 *
	 * @param typeSolutionMethod tipo a usar para evaluar estados
	 */
	public void setTypeSolutionMethod(TypeSolutionMethod typeSolutionMethod) {
		this.typeSolutionMethod = typeSolutionMethod;
	}
	/**
	 * Obtener la fábrica de métodos de solución.
	 *
	 * @return fábrica configurada (puede ser null)
	 */
	public IFFactorySolutionMethod getFactorySolutionMethod() {
		return factorySolutionMethod;
	}
	public void setFactorySolutionMethod(
			IFFactorySolutionMethod factorySolutionMethod) {
		this.factorySolutionMethod = factorySolutionMethod;
	}
	
	/**
	 * Crear una instancia de {@link SolutionMethod} para el tipo solicitado.
	 *
	 * @param typeSolutionMethod tipo de método de solución a crear
	 * @return instancia de {@link SolutionMethod}
	 * @throws ReflectiveOperationException si la creación vía fábrica falla
	 */
	public SolutionMethod newSolutionMethod(TypeSolutionMethod typeSolutionMethod) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		factorySolutionMethod = new FactorySolutionMethod();
		SolutionMethod solutionMethod = factorySolutionMethod.createdSolutionMethod(typeSolutionMethod);
		return solutionMethod;
	}
}

	
	

