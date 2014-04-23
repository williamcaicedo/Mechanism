/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.mechanism.optimizers.immune;

import java.util.List;

import co.mechanism.core.ICostFunction;
import co.mechanism.core.IMutationProvider;
import co.mechanism.optimizers.evolutionary.genetic.IPositionProvider;
import co.mechanism.utils.Utils;

/**
 * 
 * @author william
 */
public class OPTaiNET extends ClonalgOptimizer {
	private final double FITNESS_THRESHOLD = 0.0001;
	private final double SUPPRESSION_THRESHOLD = 0.01;

	public OPTaiNET(int dimensions, int numberOfCells, double beta, double d,
			List<Double> lowerBound, List<Double> upperBound,
			String functionClassName, IMutationProvider mutationProvider, IPositionProvider positionProvider,
			boolean constrainedSearch) {
		super(dimensions, numberOfCells, beta, d, lowerBound, upperBound,
				functionClassName, mutationProvider, positionProvider, constrainedSearch);
	}

	public OPTaiNET(int dimensions, int numberOfCells, double beta, double d,
			List<Double> lowerBound, List<Double> upperBound,
			ICostFunction costFunction, IMutationProvider mutationProvider, IPositionProvider positionProvider,
			boolean constrainedSearch) {
		super(dimensions, numberOfCells, beta, d, lowerBound, upperBound,
				costFunction, mutationProvider, positionProvider, constrainedSearch);
	}

	@Override
	protected void clonalSelect() {
		double currentAvgFitness = 0d;
		double pastAvgFitness = 0d;
		double diff;
		double sum;
		List<Lymphocyte> population = null;
		do {
			sum = 0d;
			super.clonalSelect();
			population = this.getPopulation();
			for (Lymphocyte l : population) {
				sum += l.getValue();
			}
			currentAvgFitness = sum / this.getPopulation().size();
			diff = Math.abs(currentAvgFitness - pastAvgFitness);
			pastAvgFitness = currentAvgFitness;
		} while (diff > FITNESS_THRESHOLD);

	}

	@Override
	protected void suppress() {
		List<Lymphocyte> population = this.getPopulation();
		Lymphocyte l1 = null;
		Lymphocyte l2 = null;
		double affinity;
		for (int i = 0; i < population.size(); i++) {
			l1 = population.get(i);
			for (int j = i + 1; j < population.size() - 1; j++) {
				l2 = population.get(j);
				affinity = Utils.getEuclideanDistance(l1.getPosition(),
						l2.getPosition());
				if (affinity < SUPPRESSION_THRESHOLD) {
					if (l2.getValue() >= l1.getValue()) {
						population.remove(j);
					} else {
						population.remove(i);
						break;
					}
				}
			}
		}
		/*
		 * int n = (int) Math.round(population.size() * this.getD()); Lymphocyte
		 * cell; for (int i = 0; i < n; i++) { cell =
		 * this.getRandomLymphocyte(); cell.evaluate(this.getCostFunction());
		 * population.add(cell); }
		 */
		super.suppress();
	}

	public boolean testForConvergence() {

		return true;
	}

}
