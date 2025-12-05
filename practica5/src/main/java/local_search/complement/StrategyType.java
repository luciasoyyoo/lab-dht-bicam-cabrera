/**
 * @(#) Strategy.java
 */

package local_search.complement;

/**
 * StrategyType - enumeration of local search strategy modes.
 *
 * @brief Represents the two supported local search strategy types: a TABU
 *        variant which uses tabu lists and a NORMAL (non-tabu) variant.
 */
public enum StrategyType
{
	/** Use tabu-list based neighborhood filtering and behavior. */
	TABU,
	/** Use normal (non-tabu) neighborhood processing. */
	NORMAL; 	
}
