package problem.definition;

import java.util.ArrayList;

import metaheuristics.generators.GeneratorType;

/**
 * State
 *
 * @brief Representa una solución (estado) dentro del espacio de búsqueda.
 *
 * Contiene la codificación de la solución, su evaluación (lista de valores
 * para problemas multiobjetivo), un identificador numérico y el tipo de
 * generador que produjo este estado.
 */
public class State implements Cloneable {
	
	protected GeneratorType typeGenerator;
	protected ArrayList<Double> evaluation;
	protected int number;
	protected ArrayList<Object> code;
	
	/**
	 * Constructor copia.
	 *
	 * Realiza copias defensivas de las colecciones internas.
	 *
	 * @param ps estado a copiar (puede ser null)
	 */
	public State(State ps) {
		if (ps == null) {
			this.code = new ArrayList<Object>();
			this.evaluation = null;
			this.number = 0;
			this.typeGenerator = null;
			return;
		}
		typeGenerator = ps.getTypeGenerator();
		ArrayList<Double> eval = ps.getEvaluation();
		this.evaluation = (eval == null) ? null : new ArrayList<Double>(eval);
		number = ps.getNumber();
		ArrayList<Object> c = ps.getCode();
		this.code = (c == null) ? new ArrayList<Object>() : new ArrayList<Object>(c);
	}
	
	/**
	 * Construir un estado a partir de la representación de código.
	 *
	 * @param code lista de objetos que representan la codificación interna
	 */
	public State(ArrayList<Object> code) {
		super();
		this.code = (code == null) ? new ArrayList<Object>() : new ArrayList<Object>(code);
	}
	
	/**
	 * Constructor por defecto: crea un estado con código vacío.
	 */
	public State() {
		code=new ArrayList<Object>();
	}	
	
	/**
	 * Obtener la codificación del estado (copia defensiva).
	 *
	 * @return lista con los elementos de código
	 */
	public ArrayList<Object> getCode() {
		return (code == null) ? new ArrayList<Object>() : new ArrayList<Object>(code);
	}

	/**
	 * Establecer la codificación del estado (se copia la lista entrante).
	 *
	 * @param listCode nueva codificación
	 */
	public void setCode(ArrayList<Object> listCode) {
		this.code = (listCode == null) ? new ArrayList<Object>() : new ArrayList<Object>(listCode);
	}

	/**
	 * Obtener el tipo de generador asociado a este estado.
	 *
	 * @return tipo de generador
	 */
	public GeneratorType getTypeGenerator() {
		return typeGenerator;
	}
	/**
	 * Establecer el tipo de generador responsable de crear este estado.
	 *
	 * @param typeGenerator tipo de generador
	 */
	public void setTypeGenerator(GeneratorType typeGenerator) {
		this.typeGenerator = typeGenerator;
	}

	
	/**
	 * Obtener la lista de evaluaciones del estado.
	 *
	 * @return copia de la lista de evaluaciones (puede ser null)
	 */
	public ArrayList<Double> getEvaluation() {
		return (evaluation == null) ? null : new ArrayList<Double>(evaluation);
	}

	/**
	 * Establecer la lista de evaluaciones del estado (copia defensiva).
	 *
	 * @param evaluation lista de doubles que representan la evaluación
	 */
	public void setEvaluation(ArrayList<Double> evaluation) {
		this.evaluation = (evaluation == null) ? null : new ArrayList<Double>(evaluation);
	}

	/**
	 * Obtener el identificador numérico del estado.
	 *
	 * @return número asignado al estado
	 */
	public int getNumber() {
		return number;
	}
	/**
	 * Asignar el identificador numérico al estado.
	 *
	 * @param number nuevo identificador
	 */
	public void setNumber(int number) {
		this.number = number;
	}
	@Override
	/**
	 * clone
	 *
	 * @brief Devuelve una copia superficial segura (deep-copy de colecciones
	 *        mutables) del estado.
	 *
	 * @return nueva instancia {@link State} con copias de las colecciones
	 */
	public State clone(){
		try {
			State s = (State) super.clone();
			// deep-copy mutable collections
			s.code = (this.code == null) ? new ArrayList<Object>() : new ArrayList<Object>(this.code);
			s.evaluation = (this.evaluation == null) ? null : new ArrayList<Double>(this.evaluation);
			// primitive and enum-like fields are safe to copy by assignment
			return s;
		} catch (CloneNotSupportedException e) {
			// fallback to copy constructor
			return new State(this);
		}
	}
	
	/**
	 * Obtener una copia por valor del estado (implementado con copy-constructor).
	 *
	 * @return objeto {@link State} nuevo
	 */
	public Object getCopy(){
		return new State(this);
	}
	
	/**
	 * Comparar la codificación de dos estados.
	 *
	 * @param state estado con el que comparar
	 * @return true si las listas de código son iguales
	 */
	public boolean Comparator(State state){

		boolean result=false;
		if(state.getCode().equals(getCode())){
			result=true;
		}
		return result;
	}
	/**
	 * Calcular la distancia (cantidad de posiciones diferentes) entre dos códigos.
	 *
	 * @param state otro estado a comparar
	 * @return número de posiciones distintas
	 */
	public double Distance(State state){
		double distancia = 0;
		for (int i = 0; i < state.getCode().size(); i++) {
			if (!(state.getCode().get(i).equals(this.getCode().get(i)))) {
				distancia++;
			}
		}
	return distancia;
	}
}
