package factory_method;

import java.lang.reflect.InvocationTargetException;

import evolutionary_algorithms.complement.Distribution;
import evolutionary_algorithms.complement.DistributionType;
import factory_interface.IFFactoryDistribution;

/**
 * FactoryDistribution - Interface for creating distribution strategies.
 */
public class FactoryDistribution implements IFFactoryDistribution {
	private Distribution distribution;

	/**
	 * createDistribution - Creates an instance of Distribution based on the provided DistributionType.
	 * @param distributiontype 
	 * @return returns an instance of Distribution
	 */
	public Distribution createDistribution(DistributionType distributiontype) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		
		String className = "evolutionary_algorithms.complement." + distributiontype.toString();
		distribution = (Distribution) FactoryLoader.getInstance(className);
		return distribution;
	}
}
