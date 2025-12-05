package evolutionary_algorithms.complement;

/**
 * Range - represents a range of values with associated probabilities.
 */
public class Range {
   private Probability data;
   private float max;
   private float min;
   
   /**
    * getData - getter for the data.
    * @return returns the data
    */
   public Probability getData() {
	  return data;
   }
   /**
    * setData - setter for the data.
    * @param data 
    */
   public void setData(Probability data) {
	  this.data = data;
   }
   /**
    * getMax - getter for the max.
    * @return returns the max
    */
   public float getMax() {
	  return max;
   }
   /**
    * setMax - setter for the max.
    * @param max 
    */
   public void setMax(float max) {
	  this.max = max;
   }
   /**
    * getMin - getter for the min.
    * @return returns the min
    */
   public float getMin() {
	  return min;
   }
   /**
    * setMin - setter for the min.
    * @param min 
    */
   public void setMin(float min) {
	  this.min = min;
   }
}
