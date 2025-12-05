/**
 * @(#) CandidateValue.java
 */

package local_search.candidate_type;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import problem.definition.State;

import local_search.complement.StrategyType;
import local_search.complement.TabuSolutions;
import metaheurictics.strategy.Strategy;

//import ceis.grial.problem.Problem;
import factory_interface.IFFactoryCandidate;
import factory_method.FactoryCandidate;

/**
 * CandidateValue - Factory/wrapper for creating and using SearchCandidate
 *                  selection strategies.
 *
 * @brief Responsible for creating concrete SearchCandidate implementations
 *        (via factories), filtering neighborhoods using tabu lists and
 *        returning candidate states chosen by the selected strategy.
 */
public class CandidateValue {


	private IFFactoryCandidate ifFactory;

	// strategy and typecand fields were unused; removed to avoid SpotBugs unread-field warnings.

	private TabuSolutions tabusolution;

	private SearchCandidate searchcandidate;

	public CandidateValue(){}

	// Keep a no-arg constructor; parameterized constructor was removed because its fields were unused.

	/**
	 * Create a new SearchCandidate instance for the provided candidate type.
	 *
	 * @param typecandidate the enum value describing which concrete
	 *                      SearchCandidate to create
	 * @return a new SearchCandidate instance
	 * @throws ReflectiveOperationException when the factory cannot create the class
	 */
	public SearchCandidate newSearchCandidate(CandidateType typecandidate) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		ifFactory = new FactoryCandidate();
		searchcandidate = ifFactory.createSearchCandidate(typecandidate);
		return searchcandidate;
	}

	/**
	 * Choose a candidate state from the provided neighborhood according to the
	 * specified candidate selection type and strategy.
	 *
	 * If the StrategyType is TABU, the neighborhood is first filtered through
	 * the tabu list. On exception the method will attempt to regenerate the
	 * neighborhood via the strategy's problem operator and retry.
	 *
	 * @param stateCurrent   the current state
	 * @param typeCandidate  which candidate selection strategy to use
	 * @param strategy       the local search strategy type (e.g. TABU)
	 * @param operatornumber operator index used to generate new states if needed
	 * @param neighborhood   list of neighbor states to select from
	 * @return the chosen candidate state
	 * @throws ReflectiveOperationException when factory/reflective creation fails
	 */
	public State stateCandidate(State stateCurrent, CandidateType typeCandidate, StrategyType strategy, Integer operatornumber, List<State> neighborhood) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		//Problem problem = ExecuteGenerator.getExecuteGenerator().getProblem();
		State stateCandidate;
		List<State> auxList = new ArrayList<State>();
		for (int i = 0; i < neighborhood.size(); i++) {
			auxList.add(neighborhood.get(i));
		}
		this.tabusolution = new TabuSolutions();
		if (strategy.equals(StrategyType.TABU)) {
			try {
				auxList = this.tabusolution.filterNeighborhood(auxList);
			}
			catch (Exception e) {
				Strategy strategys = Strategy.getStrategy();
				if(strategys.getProblem()!=null){
					neighborhood = strategys.getProblem().getOperator().generatedNewState(neighborhood.get(0), operatornumber);
				}
				return stateCandidate(stateCurrent, typeCandidate, strategy, operatornumber, neighborhood);
			}
		}
		SearchCandidate searchCand = newSearchCandidate(typeCandidate);
		stateCandidate = searchCand.stateSearch(auxList);
		return stateCandidate;
	}

	/**
	 * Get the currently stored tabu solutions helper instance.
	 *
	 * @return TabuSolutions manager used for neighborhood filtering
	 */
	public TabuSolutions getTabusolution() {
		return tabusolution;
	}

	/**
	 * Set the TabuSolutions helper used by this CandidateValue.
	 *
	 * @param tabusolution TabuSolutions instance to use for neighborhood filtering
	 */
	public void setTabusolution(TabuSolutions tabusolution) {
		this.tabusolution = tabusolution;
	}
}
