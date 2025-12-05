/**
 * @(#) TypeCandidate.java
 */

package local_search.candidate_type;

/**
 * CandidateType - enumeration of available candidate selection strategies.
 *
 * @brief Enumerates strategies used to pick a candidate state from a
 *        neighborhood during local search operators.
 */
public enum CandidateType{
    
	/** Select the smallest/best candidate according to objective. */
	SmallerCandidate,
	/** Select the largest/best candidate according to objective. */
	GreaterCandidate,
	/** Select a candidate at random from the neighborhood. */
	RandomCandidate,
	/** Select a candidate that is not dominated (multi-objective). */
	NotDominatedCandidate;
}
