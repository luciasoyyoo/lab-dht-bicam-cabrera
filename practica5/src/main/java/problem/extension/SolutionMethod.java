package problem.extension;

import problem.definition.State;

/**
 * SolutionMethod
 *
 * @brief Abstracción para métodos concretos que evalúan un {@link problem.definition.State}.
 *
 * Implementaciones específicas calculan y asignan la lista de evaluaciones del estado.
 */
public abstract class SolutionMethod {

	/**
	 * Evaluar un estado según el método de solución concreto.
	 *
	 * @param state estado a evaluar; se espera que la implementación modifique
	 *              la lista de evaluaciones del estado.
	 */
	public abstract void evaluationState(State state);
}
