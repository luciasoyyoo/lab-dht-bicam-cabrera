package problem.definition;

/**
 * Codification
 *
 * @brief Interfaz abstracta para la codificación de variables de un problema.
 *
 * Implementaciones concretas deben validar estados y generar valores aleatorios
 * para variables según el dominio del problema.
 */
public abstract class Codification {

	public abstract boolean validState(State state);
//	public abstract int getVariableDomain(int variable);
	public abstract Object getVariableAleatoryValue(int key);
	public abstract int getAleatoryKey ();
	public abstract int getVariableCount();

}