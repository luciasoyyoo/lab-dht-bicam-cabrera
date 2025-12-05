package evolutionary_algorithms.complement;

/**
 * Probability - represents the probability of a certain event.
 */
public class Probability {
    private Object key;
    private Object value;
	private float probability;
	
	
	/**
	 * getProbability - getter for the probability.
	 * @return returns the probability
	 */
	public float getProbability() {
		return probability;
	}
	/**
	 * setProbability - setter for the probability.
	 * @param probability 
	 */
	public void setProbability(float probability) {
		this.probability = probability;
	}
	/**
	 * getKey - getter for the key.
	 * @return returns the key
	 */
	public Object getKey() {
		return key;
	}
	/**
	 * setKey - setter for the key.
	 * @param key 
	 */
	public void setKey(Object key) {
		this.key = key;
	}
	/**
	 * getValue - getter for the value.
	 * @return returns the value
	 */
	public Object getValue() {
		return value;
	}
	/**
	 * setValue - setter for the value.
	 * @param value 
	 */
	public void setValue(Object value) {
		this.value = value;
	}
}
