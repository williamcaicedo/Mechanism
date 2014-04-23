package co.mechanism.optimizers.evolutionary.genetic;

import java.util.List;

import co.mechanism.optimizers.evolutionary.Individual;

public interface ICrossoverProvider {
	
	public List<Individual> mate(Individual firstParent, Individual secondParent);

}
