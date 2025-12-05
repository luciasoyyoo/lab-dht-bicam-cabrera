package metaheuristics.generators;

/**
 * LimitRoulette - class that implements the Limit Roulette structure.
 */
public class LimitRoulette {

	private float limitLow;
	private float limitHigh;
	private Generator generator;
	
	/**
	 * getGenerator - get the generator associated with the Limit Roulette.
	 * @return the generator
	 */
	public Generator getGenerator() {
		return generator;
	}
	/**
	 * setGenerator - set the generator associated with the Limit Roulette.
	 * @param generator the generator to set
	 */
	public void setGenerator(Generator generator) {
		this.generator = generator;
	}
	/**
	 * getLimitHigh - get the upper limit of the Limit Roulette.
	 * @return the upper limit
	 */
	public float getLimitHigh() {
		return limitHigh;
	}
	/**
	 * setLimitHigh - set the upper limit of the Limit Roulette.
	 * @param limitHigh the upper limit to set
	 */
	public void setLimitHigh(float limitHigh) {
		this.limitHigh = limitHigh;
	}
	/**
	 * getLimitLow - get the lower limit of the Limit Roulette.
	 * @return the lower limit	
	 */
	public float getLimitLow() {
		return limitLow;
	}
	/**
	 * setLimitLow - set the lower limit of the Limit Roulette.
	 * @param limitLow the lower limit to set
	 */
	public void setLimitLow(float limitLow) {
		this.limitLow = limitLow;
	}
	
	
}
