package problem.extension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import jxl.read.biff.BiffException;
import problem.definition.State;

/**
 * MetricasMultiobjetivo
 *
 * @brief Conjunto de métricas para evaluar frentes de Pareto en problemas
 *        multiobjetivo (error, distancia generacional, dispersión, etc.).
 */
public class MetricasMultiobjetivo {

// % de soluciones q no son miembros del frente de pareto verdadero
	/**
	 * Calcular el porcentaje de soluciones del frente actual que no pertenecen
	 * al frente de Pareto verdadero.
	 *
	 * @param solutionsFPcurrent frente de Pareto obtenido por el algoritmo
	 * @param solutionsFPtrue frente de Pareto de referencia (verdadero)
	 * @return proporción de soluciones incorrectas (0..1)
	 */
	public double TasaError(List<State> solutionsFPcurrent, List<State> solutionsFPtrue) throws BiffException, IOException{
		float tasaError = 0;
		for (int i = 0; i < solutionsFPcurrent.size() ; i++) { // frente de pareto actual
			State solutionVO = solutionsFPcurrent.get(i);
			if(!Contains(solutionVO, solutionsFPtrue)){ // no esta en el frente de pareto verdadero 
				tasaError++;
			}
		}
		double total = tasaError/solutionsFPcurrent.size();
		//System.out.println(solutionsFP.size() + "/" + solutions.size() + "/" + total);
		return total;
	}
	
// % Indica  qué  tan  lejos  están  los  elementos  del frente  de  Pareto  actual  respecto  al  frente  de  Pareto  verdadero	
	/**
	 * DistanciaGeneracional
	 *
	 * @brief Calcula la distancia media entre los miembros del frente actual y
	 *        sus puntos más cercanos en el frente verdadero.
	 *
	 * @param solutionsFPcurrent frente actual
	 * @param solutionsFPtrue frente verdadero
	 * @return distancia agregada normalizada
	 */
	public double DistanciaGeneracional(List<State> solutionsFPcurrent, List<State> solutionsFPtrue) throws BiffException, IOException{
		float min = 1000;
		float distancia = 0;
		float distanciaGeneracional = 0;
		for (int i = 0; i < solutionsFPcurrent.size();i++) {
			State solutionVO = solutionsFPcurrent.get(i);
			//Calculando la distancia euclideana entre solutionVO y el miembro más cercano del frente de Pareto verdadero
			min = 1000;
			for (int j = 0; j < solutionsFPtrue.size();j++) { 
				for (int j2 = 0; j2 < solutionVO.getEvaluation().size(); j2++) {
					State solutionFPV = solutionsFPtrue.get(j);
					// porq elevar la distancia al cuadrado
					distancia += (solutionVO.getEvaluation().get(j2) - solutionFPV.getEvaluation().get(j2))*  
							(solutionVO.getEvaluation().get(j2) - solutionFPV.getEvaluation().get(j2)); //ceros si el argumento es el cero, 1.0 si el argumento es mayor que el cero, -1.0 si el argumento está menos del cero
				}
				if(distancia < min){
					min = distancia;
				}
			}
			distanciaGeneracional += min;
		}
		double total = Math.sqrt(distanciaGeneracional)/solutionsFPcurrent.size();
		//System.out.println(total);
		return total;
	}

	/**
	 * Dispersion
	 *
	 * @brief Mide la dispersión (desviación típica) entre las distancias
	 *        inter-soluciones de un frente.
	 *
	 * @param solutions lista de estados que conforman el frente
	 * @return medida de dispersión
	 */
	public double Dispersion(ArrayList<State> solutions) throws BiffException, IOException{
		//Soluciones obtenidas con la ejecución del algoritmo X
		LinkedList<Float> distancias = new LinkedList<Float>();
		float distancia = 0;
		float min = 1000;
		for (Iterator<State> iter = solutions.iterator(); iter.hasNext();) {
			State solutionVO = (State) iter.next();
			min = 1000;
			for (Iterator<State> iterator = solutions.iterator(); iterator.hasNext();) {
				State solVO = (State) iterator.next();
				for (int i = 0; i < solutionVO.getEvaluation().size(); i++) {
					if(!solutionVO.getEvaluation().equals(solVO.getEvaluation())){
						distancia += (solutionVO.getEvaluation().get(i)- solVO.getEvaluation().get(i));
					}}
				if(distancia < min){
					min = distancia;
				}
			}
			distancias.add(Float.valueOf(min));
		}
		//Calculando las media de las distancias 
		float sum = 0;
		for (Iterator<Float> iter = distancias.iterator(); iter.hasNext();) {
			Float dist = (Float) iter.next();
			sum += dist;
		}
		float media = sum/distancias.size();
		float sumDistancias = 0;
		for (Iterator<Float> iter = distancias.iterator(); iter.hasNext();) {
			Float dist = (Float) iter.next();
			sumDistancias += Math.pow((media - dist),2);
		}
		//Calculando la dispersion
		double dispersion = 0;
		if(solutions.size() > 1){
			dispersion = Math.sqrt((1.0/(solutions.size()-1))*sumDistancias);
		}
		//System.out.println(dispersion);
		return dispersion;
	}
	/**
	 * Comprueba si una solución (por su vector de evaluación) está en una lista.
	 *
	 * @param solA solución a comprobar
	 * @param solutions lista de soluciones
	 * @return true si se encuentra una evaluación idéntica
	 */
	private boolean Contains(State solA, List<State> solutions){
		int i = 0;
		boolean result = false;
		while(i<solutions.size()&& result==false){
			if(solutions.get(i).getEvaluation().equals(solA.getEvaluation()))
				result=true;
			else
				i++;
		}
		return result;
	}
	/**
	 * Calcular el valor mínimo de una lista de métricas.
	 *
	 * @param allMetrics lista de métricas
	 * @return valor mínimo
	 */
	public double CalcularMin(ArrayList<Double> allMetrics){
		double min = 1000;
		for (Iterator<Double> iter = allMetrics.iterator(); iter.hasNext();) {
			double element = (Double) iter.next();
			if(element < min){
				min = element;
			}
		}
		return min;
	}

	/**
	 * Calcular el valor máximo de una lista de métricas.
	 *
	 * @param allMetrics lista de métricas
	 * @return valor máximo
	 */
	public double CalcularMax(ArrayList<Double> allMetrics){
		double max = 0;
		for (Iterator<Double> iter = allMetrics.iterator(); iter.hasNext();) {
			double element = (Double) iter.next();
			if(element > max){
				max = element;
			}
		}
		return max;
	}
	/**
	 * Calcular la media aritmética de una lista de métricas.
	 *
	 * @param allMetrics lista de métricas
	 * @return media
	 */
	public double CalcularMedia(ArrayList<Double> allMetrics){
		double sum = 0;
		for (Iterator<Double> iter = allMetrics.iterator(); iter.hasNext();) {
			double element = (Double) iter.next();
			sum = sum + element;
		}
		double media = sum/allMetrics.size();
		return media;
	}
}
