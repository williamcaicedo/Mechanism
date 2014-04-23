/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.mechanism.optimizers.evolutionary.de;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import co.mechanism.core.AbstractOptimizer;
import co.mechanism.core.ICostFunction;
import co.mechanism.optimizers.evolutionary.Individual;
import co.mechanism.utils.MersenneTwisterFast;
import co.mechanism.utils.Utils;

/**
 * 
 * @author william
 */
public class DEOptimizer extends AbstractOptimizer<Individual> {

	private int dimensions;
	private int numberOfIndividuals;
	private List<Individual> mutantPopulation;
	private List<Individual> trialPopulation;
	private double F;
	private double cr;
	private boolean constrainedSearch;

//	public DEOptimizer(int dimensions, int numberOfIndividuals, double F,
//			double cr, List<Double> lowerBound, List<Double> upperBound,
//			String functionClassName, boolean constrainedSearch) {
//		super(lowerBound, upperBound, (ICostFunction) Utils
//				.getInstanceByReflection(functionClassName));
//		if (cr > 1 || cr < 0)
//			throw new RuntimeException(
//					"Crossover probability must be in the interval [0,1]");
//		this.init(dimensions, numberOfIndividuals, F, cr, constrainedSearch);
//	}

	public DEOptimizer(int dimensions, int numberOfIndividuals, double F,
			double cr, List<Double> lowerBound, List<Double> upperBound,
			ICostFunction costFunction, boolean constrainedSearch) {
		super(lowerBound, upperBound, costFunction);
		if (cr > 1 || cr < 0)
			throw new RuntimeException(
					"Crossover probability must be in the interval [0,1]");
		this.init(dimensions, numberOfIndividuals, F, cr, constrainedSearch);
	}

	protected void init(int dimensions, int numberOfIndividuals, double F,
			double cr, boolean constrainedSearch) {
		this.dimensions = dimensions;
		this.numberOfIndividuals = numberOfIndividuals;
		this.mutantPopulation = new ArrayList<Individual>(numberOfIndividuals);
		this.trialPopulation = new ArrayList<Individual>(numberOfIndividuals);
		this.F = F;
		this.cr = cr;
		this.constrainedSearch = constrainedSearch;
		// System.out.println("Población inicial");
		MersenneTwisterFast mt = Utils.getMTInstance();
		double v;
		for (int i = 0; i < this.numberOfIndividuals; i++) {
			// Individual ind = new Individual();
			List<Double> temp = new LinkedList<Double>();
			for (int j = 0; j < this.dimensions; j++) {
				v = this.getLowerBound().get(j)
						+ (mt.nextDouble() * ((this.getUpperBound().get(j) - this
								.getLowerBound().get(j))));
				temp.add(v);
				// this.population.get(i).getParameters().set(j,v);
			}

			this.getPopulation().add(new Individual(temp));
			// System.out.println(temp+ " Fitness: "+ new
			// Individual(temp).getValue());
		}
		// System.out.println("****************");
	}

	@Override
	public void evolve() {
		// for(int i = 0; i < generations; i++) {
		this.mutate();

		this.crossover();
		this.doSelection();

		/*
		 * System.out.println("Nueva poblaciÃ³n"); this.printPopulation();
		 * System.out.println("***************");
		 */
		// }
	}

	private void mutate() {
		// new Random().
		this.mutantPopulation.clear();
		MersenneTwisterFast mt = Utils.getMTInstance();
		double offset = mt.nextInt(this.numberOfIndividuals);
		// double offset = Math.floor(Math.random()*this.numberOfIndividuals);
		for (int i = 0; i < this.numberOfIndividuals; i++) {
			// Individual ind = new Individual(this.dimensions);
			List<Double> temp = new ArrayList<Double>(this.dimensions);
			int base = (int) ((i + offset) % this.numberOfIndividuals);
			int v1;
			int v2;
			do {
				v1 = mt.nextInt(this.numberOfIndividuals);
			} while (v1 == base || v1 == i);
			do {
				v2 = mt.nextInt(this.numberOfIndividuals);
			} while (v2 == base || v1 == i || v2 == v1);

			for (int j = 0; j < this.dimensions; j++) {
				Double mutantValue = this.getPopulation().get(base)
						.getPosition().get(j)
						+ this.F
						* (this.getPopulation().get(v1).getPosition().get(j) - this
								.getPopulation().get(v2).getPosition().get(j));
				// if (this.constrainedSearch)

				temp.add((this.constrainedSearch) ? this.checkBounds(
						mutantValue, j, base) : mutantValue);
			}
			// if (this.mutantPopulation.isEmpty()) {
			this.mutantPopulation.add(new Individual(temp));
			/*
			 * }else{ this.mutantPopulation.set(i, new Individual(temp)); }
			 */

		}
	}

	private Double checkBounds(Double mutantValue, int dimension, int base) {
		// Bounce-Back boundary constraints handling
		if (mutantValue < this.getLowerBound().get(dimension)) {
			mutantValue = this.getPopulation().get(base).getPosition()
					.get(dimension)
					+ Math.random()
					* (this.getLowerBound().get(dimension) - this
							.getPopulation().get(base).getPosition()
							.get(dimension));
		}
		if (mutantValue > this.getUpperBound().get(dimension)) {
			mutantValue = this.getPopulation().get(base).getPosition()
					.get(dimension)
					+ Math.random()
					* (this.getUpperBound().get(dimension) - this
							.getPopulation().get(base).getPosition()
							.get(dimension));
		}
		return mutantValue;
	}

	private void crossover() {
		this.trialPopulation.clear();
		MersenneTwisterFast mt = Utils.getMTInstance();
		for (int i = 0; i < this.numberOfIndividuals; i++) {
			int jr = mt.nextInt(dimensions);
			List<Double> temp = new LinkedList<Double>();
			for (int j = 0; j < this.dimensions; j++) {
				if (j == jr) {
					temp.add(this.mutantPopulation.get(i).getPosition().get(j));
				} else {
					double p = mt.nextDouble();
					if (p <= this.cr) {
						temp.add(this.mutantPopulation.get(i).getPosition()
								.get(j));
					} else {
						temp.add(this.getPopulation().get(i).getPosition()
								.get(j));
					}
				}
			}
			// if (this.trialPopulation.isEmpty()) {
			this.trialPopulation.add(new Individual(temp));
			/*
			 * }else{ this.trialPopulation.set(i, new Individual(temp)); }
			 */
		}
	}

	private void doSelection() {
		for (int i = 0; i < this.numberOfIndividuals; i++) {
			Individual parentIndividual = this.getPopulation().get(i);
			Individual trialIndividual = this.trialPopulation.get(i);
			double trialFitness = trialIndividual.evaluate(this
					.getCostFunction());
			double parentFitness = parentIndividual.evaluate(this
					.getCostFunction());
			if (trialFitness <= parentFitness) {
				this.getPopulation().set(i, trialIndividual);
			}/*
			 * else{ double boltzmann = Math.exp(-(trialFitness -
			 * parentFitness)/temp); if (Math.random()<boltzmann) {
			 * this.population.set(i, trialIndividual); } }
			 */
			// System.out.println(parentIndividual.getValue()+" vs "+trialIndividual.getValue());
		}
	}

}
