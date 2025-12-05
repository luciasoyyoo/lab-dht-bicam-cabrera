/**
 * @(#) TypeAcceptation.java
 */

package local_search.acceptation_type;

/**
 * AcceptType - Enumeration of the available acceptation strategies.
 *
 * @brief Enumerates the strategies supported by the local search framework
 *        for deciding whether to accept candidate solutions.
 */
public enum AcceptType
{
	AcceptBest, AcceptAnyone, AcceptNotBadT, AcceptNotBadU, AcceptNotDominated, AcceptNotDominatedTabu, AcceptNotBad,AcceptMulticase;
    
}
