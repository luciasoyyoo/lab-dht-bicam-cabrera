package local_search.complement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import problem.definition.State;

/**
 * TabuSolutions - helper that manages a tabu list and filters neighborhoods.
 *
 * @brief Provides a synchronized global tabu list and a utility to remove tabu
 *        entries from candidate neighborhoods. The filtering method will
 *        throw an Exception if all neighbors are tabu.
 */
public class TabuSolutions {
    
	/** Global synchronized tabu list shared by the local search components. */
	public static final List<State> listTabu = Collections.synchronizedList(new ArrayList<State>());

	/** Maximum number of elements allowed in the tabu list (unused/placeholder). */
	public static final int maxelements = 0; 

	/**
	 * Remove tabu solutions from the supplied neighborhood list.
	 *
	 * This method iterates the provided neighborhood and removes any element
	 * that equals an element in the global `listTabu`. If after filtering the
	 * neighborhood becomes empty the method throws an Exception to signal the
	 * caller that no valid neighbors remain.
	 *
	 * @param listNeighborhood list of neighbor States to filter
	 * @return a filtered List<State> with tabu entries removed
	 * @throws Exception when no neighbors remain after filtering
	 */
	public List<State> filterNeighborhood(List<State> listNeighborhood) throws Exception {
	List<State> listFiltrate;
		// If there are entries in the tabu list, remove matching neighbors
		if (!listTabu.isEmpty()) {
			for (int i = listNeighborhood.size() - 1; i >= 0 ; i--) {
				int count_tabu = 0; 
				while (listTabu.size() > count_tabu) {
					if (listNeighborhood.get(i).equals(listTabu.get(count_tabu))) {
						listNeighborhood.remove(i);
					}
					count_tabu++;
				}
			}
			listFiltrate = listNeighborhood;
			if (listFiltrate.isEmpty()) {
				throw new Exception();
			}
		} else {
			listFiltrate = listNeighborhood;
		}
		return listFiltrate;
	}
}