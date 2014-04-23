package co.mechanism.optimizers.evolutionary.genetic;

import java.util.List;

import co.mechanism.optimizers.evolutionary.Individual;

public interface ISelectionProvider {

	public List<Individual> selectParents(List<Individual> population);
}
