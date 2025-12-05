package local_search.complement;


/**
 * StopExecute - termination predicate for local search loops.
 *
 * @brief Utility that provides a simple iterations-based stopping condition
 *        for local search algorithms.
 */
public class StopExecute {
	
	/**
	 * Check whether the maximum number of iterations has been reached.
	 *
	 * @param countIterationsCurrent current iteration count (0-based or 1-based
	 *                               depending on caller convention)
	 * @param countmaxIterations maximum allowed iterations
	 * @return true if execution should stop (current >= max), false otherwise
	 */
	public Boolean stopIterations(int countIterationsCurrent, int countmaxIterations) {
		if (countIterationsCurrent < countmaxIterations) {
			return false;
		} else {
			return true;
		}
	}
}
