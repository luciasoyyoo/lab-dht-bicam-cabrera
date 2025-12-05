package problem.definition;

import problem.definition.Problem.ProblemType;

/**
 * ObjetiveFunction
 *
 * @brief Representa una función objetivo del problema. Puede ser usada en
 *        problemas mono o multi-objetivo y contiene el peso y el tipo
 *        (Maximizar/Minimizar).
 */
public abstract class ObjetiveFunction {
	
	private ProblemType typeProblem;
	private float weight;
	
	/**
	 * Obtener el peso relativo de esta función objetivo.
	 *
	 * @return peso (float)
	 */
	public float getWeight() {
		return weight;
	}

	/**
	 * Establecer el peso relativo de la función objetivo.
	 *
	 * @param weight valor de peso
	 */
	public void setWeight(float weight) {
		this.weight = weight;
	}

	/**
	 * Obtener si la función es de tipo Maximizar o Minimizar.
	 *
	 * @return tipo de problema asociado a la función
	 */
	public ProblemType getTypeProblem() {
		return typeProblem;
	}

	/**
	 * Establecer el tipo (Maximizar/Minimizar) de la función objetivo.
	 *
	 * @param typeProblem tipo a asignar
	 */
	public void setTypeProblem(ProblemType typeProblem) {
		this.typeProblem = typeProblem;
	}

	/**
	 * Evaluar el estado respecto a esta función objetivo.
	 *
	 * @param state estado a evaluar
	 * @return valor de evaluación (Double)
	 */
	public abstract Double Evaluation(State state);
}
