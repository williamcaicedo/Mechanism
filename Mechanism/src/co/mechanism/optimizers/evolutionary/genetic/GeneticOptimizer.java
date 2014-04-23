/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.mechanism.optimizers.evolutionary.genetic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import co.mechanism.core.AbstractOptimizer;
import co.mechanism.core.ICostFunction;
import co.mechanism.core.IMutationProvider;
import co.mechanism.optimizers.evolutionary.Individual;
import co.mechanism.utils.Utils;

/**
 * 
 * @author william
 */
public class GeneticOptimizer extends AbstractOptimizer<Individual> {

	private ISelectionProvider selectionProvider;
	private List<Individual> selected;
	private List<Individual> offspring;
	private IMutationProvider mutationProvider;
	private double mutationProbability;
	private ICrossoverProvider crossoverProvider;
	private IReplacementProvider replacementProvider;
	private int populationSize;
	private IPositionProvider positionProvider;

	public GeneticOptimizer(int populationSize, double mutationProbability,
			List<Double> lowerBound, List<Double> upperBound, ICostFunction costFunction,
			ISelectionProvider selectionProvider,
			ICrossoverProvider crossoverProvider,
			IReplacementProvider replacementProvider,
			IMutationProvider mutationProvider, IPositionProvider positionProvider) {
		super(lowerBound, upperBound, costFunction);
		this.selectionProvider = selectionProvider;
		this.mutationProvider = mutationProvider;
		this.positionProvider = positionProvider;
		this.crossoverProvider = crossoverProvider;
		this.replacementProvider = replacementProvider;
		this.mutationProbability = mutationProbability;
		this.populationSize = populationSize;
		
		init();

	}

	private void init() {
		for (int i = 0; i < this.populationSize; i++) {
			this.getPopulation().add(new Individual(this.positionProvider.getRandomPosition(this)));
		}
	}

	@Override
	public void evolve() {
		selection();
		mutate();
		crossover();
		replace();
	}

	private void replace() {
		this.evaluatePopulation(offspring);
		this.replacementProvider.replaceIndividuals(offspring,
				this.getPopulation());
	}

	private void mutate() {
		for (Individual ind : this.selected) {
			double random = Utils.getMTRandom();
			if (random < this.mutationProbability)
				mutationProvider.mutate(ind, this);
		}
	}

	private void crossover() {
		Collections.shuffle(this.selected);
		Individual firstParent = null;
		Individual secondParent = null;
		offspring = new ArrayList<Individual>();
		Iterator<Individual> it = this.selected.iterator();
		while (it.hasNext()) {
			firstParent = it.next();
			if (it.hasNext()) {
				secondParent = it.next();
				offspring.addAll(this.crossoverProvider.mate(firstParent,
						secondParent));
			} else {
				secondParent = this.selected.get(0);
				offspring.addAll(this.crossoverProvider.mate(firstParent,
						secondParent));
			}
		}
	}

	private void selection() {
		evaluatePopulation(this.getPopulation());
		this.selected = selectionProvider.selectParents(this.getPopulation());
	}

	private void evaluatePopulation(List<Individual> ind) {
		for (Individual i : ind) {
			i.evaluate(this.getCostFunction());
		}
	}

}
