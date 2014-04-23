/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.mechanism.optimizers.immune;

import java.util.Collections;
import java.util.List;

import co.mechanism.core.AbstractOptimizer;
import co.mechanism.core.ICostFunction;
import co.mechanism.core.IMutationProvider;
import co.mechanism.optimizers.evolutionary.genetic.IPositionProvider;
import co.mechanism.utils.MersenneTwisterFast;
import co.mechanism.utils.Utils;

/**
 * 
 * @author william
 */
public class ClonalgOptimizer extends AbstractOptimizer<Lymphocyte> {

	// List<Lymphocyte> limphocytes;
	// List<List<Lymphocyte>> clones;

	private int dimensions;
	private int numberOfCells;
	private final double d;
	private final double beta;
	private MersenneTwisterFast mt;
	private IMutationProvider mutationProvider;
	private IPositionProvider positionProvider;

	public ClonalgOptimizer(int dimensions, int numberOfCells, double beta,
			double d, List<Double> lowerBound, List<Double> upperBound,
			String functionClassName, IMutationProvider mutationProvider, IPositionProvider positionProvider,
			boolean constrainedSearch) {
		super(lowerBound, upperBound, (ICostFunction) Utils
				.getInstanceByReflection(functionClassName));
		// this.setCostFunction((ICostFunction)
		// Utils.getInstanceByReflection(functionClassName));
		this.mutationProvider = mutationProvider;
		this.positionProvider = positionProvider;
		this.dimensions = dimensions;
		this.numberOfCells = numberOfCells;
		this.beta = beta;
		this.d = d;
		mt = Utils.getMTInstance();
		this.init();
	}

	public ClonalgOptimizer(int dimensions, int numberOfCells, double beta,
			double d, List<Double> lowerBound, List<Double> upperBound,
			ICostFunction function, IMutationProvider mutationProvider, IPositionProvider positionProvider,
			boolean constrainedSearch) {
		super(lowerBound, upperBound, function);
		// this.setCostFunction((ICostFunction)
		// Utils.getInstanceByReflection(functionClassName));
		this.mutationProvider = mutationProvider;
		this.positionProvider = positionProvider;
		this.dimensions = dimensions;
		this.numberOfCells = numberOfCells;
		this.beta = beta;
		this.d = d;
		mt = Utils.getMTInstance();
		this.init();
	}

	protected void init() {

		// this. = new FastList<Lymphocyte>();
		for (int i = 0; i < this.numberOfCells; i++) {
			this.getPopulation().add(this.getRandomLymphocyte());
		}
	}

	protected Lymphocyte getRandomLymphocyte() {
		double v;
//		List<Double> temp = new FastList<Double>();
//		 for (int j = 0; j < this.dimensions; j++) {
//		 v = this.getLowerBound().get(j) + (mt.nextDouble() *
//		 ((this.getUpperBound().get(j) - this.getLowerBound().get(j))));
//		 temp.add(v);
//		 }
//		for (int j = 0; j < this.dimensions; j++) {
//			v = (this.mt.nextBoolean()) ? 1 : 0;
//			temp.add(v);
//		}
		return new Lymphocyte(this.positionProvider.getRandomPosition(this));
	}

	@Override
	public void evolve() {
		clonalSelect();
		// System.out.println(this.limphocytes.get(0).getValue());
		suppress();
	}

	protected void clonalSelect() {

		// List<Double> fitness = new FastList<Double>(this.limphocytes.size());
		for (Lymphocyte cell : this.getPopulation()) {
			cell.evaluate(getCostFunction());
		}
		Collections.sort(this.getPopulation());
		double max = this.getPopulation().get(this.getPopulation().size() - 1)
				.getValue();

		Lymphocyte cell = null;
		Lymphocyte mutant = null;
		Lymphocyte bestMutant = null;
		// List<BCell> c = null;
		long nc;
		// int dim;
		// double mp;
		double highestFitness;
		// List<Double> mutatedParameters = null;

		for (int i = 0; i < this.getPopulation().size(); i++) {

			highestFitness = Double.POSITIVE_INFINITY;
			cell = this.getPopulation().get(i);
			nc = Math.round(beta * this.getPopulation().size());
			// c = new FastList<BCell>((int)nc);
			for (int j = 0; j < nc; j++) {
				// mutatedParameters = new
				// FastList<Double>(cell.getPosition().size());
				// dim = 0;
				// for (Double p : cell.getPosition()) {
				// //Mutation as defined for opt-aiNET
				// double alpha = (1 / beta) * Math.exp(-cell.getValue() / max);
				//
				// //checking bounds
				// // do {
				// // mp = p + alpha * mt.nextGaussian();
				// // System.out.println("ojo");
				// // } while (mp < this.getLowerBound().get(dim) || mp >
				// this.getUpperBound().get(dim));
				// mp = p + alpha * mt.nextGaussian();
				// if (mp < this.getLowerBound().get(dim)) {
				// mp = this.getLowerBound().get(dim);
				// } else if (mp > this.getUpperBound().get(dim)) {
				// mp = this.getUpperBound().get(dim);
				// }
				//
				// mutatedParameters.add(mp);
				// dim++;
				// }
				// mutant = new Lymphocyte(mutatedParameters);
				mutant = new Lymphocyte(Utils.doubleListDeepCopy(cell.getPosition()));
				mutant.setValue(cell.getValue());
				mutant = (Lymphocyte) this.mutationProvider.mutate(mutant, this);
				// c.add(mutant);
				if (mutant.evaluate(getCostFunction()) < highestFitness) {
					highestFitness = mutant.getValue();
					bestMutant = mutant;
				}

			}
			if (bestMutant.getValue() < cell.getValue()) {
				this.getPopulation().set(i, bestMutant);
			}
			// this.clones.add(c);
		}

	}

	protected void suppress() {
		Collections.sort(this.getPopulation());

		Lymphocyte cell;
		int n = this.getPopulation().size()
				- (int) Math.round(this.getPopulation().size() * this.getD());
		for (int i = n; i < this.getPopulation().size(); i++) {
			cell = this.getRandomLymphocyte();
			cell.evaluate(getCostFunction());
			this.getPopulation().set(i, cell);
		}
	}

	public double getD() {
		return d;
	}

	public double getBeta() {
		return beta;
	}

	public MersenneTwisterFast getMt() {
		return mt;
	}

	public void setMt(MersenneTwisterFast mt) {
		this.mt = mt;
	}

}
