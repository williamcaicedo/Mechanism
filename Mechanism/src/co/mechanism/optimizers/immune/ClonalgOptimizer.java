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
		this.numberOfCells = numberOfCells;
		this.beta = beta;
		this.d = d;
		mt = Utils.getMTInstance();
		this.init();
	}

	protected void init() {
		for (int i = 0; i < this.numberOfCells; i++) {
			this.getPopulation().add(this.getRandomLymphocyte());
		}
	}

	protected Lymphocyte getRandomLymphocyte() {
		

		return new Lymphocyte(this.positionProvider.getRandomPosition(this));
	}

	@Override
	public void evolve() {
		clonalSelect();
		suppress();
	}

	protected void clonalSelect() {

		for (Lymphocyte cell : this.getPopulation()) {
			cell.evaluate(getCostFunction());
		}
		Collections.sort(this.getPopulation());
		Lymphocyte cell = null;
		Lymphocyte mutant = null;
		Lymphocyte bestMutant = null;
		long nc;
		double highestFitness;
		for (int i = 0; i < this.getPopulation().size(); i++) {

			highestFitness = Double.POSITIVE_INFINITY;
			cell = this.getPopulation().get(i);
			nc = Math.round(beta * this.getPopulation().size());
			for (int j = 0; j < nc; j++) {
				mutant = new Lymphocyte(Utils.doubleListDeepCopy(cell.getPosition()));
				mutant.setValue(cell.getValue());
				mutant = (Lymphocyte) this.mutationProvider.mutate(mutant, this);
				if (mutant.evaluate(getCostFunction()) < highestFitness) {
					highestFitness = mutant.getValue();
					bestMutant = mutant;
				}

			}
			if (bestMutant.getValue() < cell.getValue()) {
				this.getPopulation().set(i, bestMutant);
			}
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
