package co.mechanism.optimizers.evolutionary.genetic;

import java.util.List;

import co.mechanism.optimizers.evolutionary.Individual;

public interface IReplacementProvider {

	
	public List<Individual> replaceIndividuals(List<Individual> offspring, List<Individual> population);
}
