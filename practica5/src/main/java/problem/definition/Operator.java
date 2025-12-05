package problem.definition;

import java.util.List;

/**
 * Operator
 *
 * @brief Interfaz abstracta para operadores que generan nuevos estados a
 *        partir de un estado actual o que producen estados aleatorios.
 */
public abstract class Operator {
	
		/**
		 * Generar nuevos estados a partir de un estado dado.
		 *
		 * @param stateCurrent estado base
		 * @param operatornumber índice de operador (uso dependiente de la implementación)
		 * @return lista de estados generados
		 */
		public abstract List<State> generatedNewState(State stateCurrent, Integer operatornumber);
		
		/**
		 * Generar un conjunto de estados aleatorios.
		 *
		 * @param operatornumber índice/semilla opcional para la generación
		 * @return lista de estados aleatorios
		 */
		public abstract List<State> generateRandomState (Integer operatornumber);

	}

