package co.mechanism.optimizers.evolutionary.genetic;

import java.util.ArrayList;
import java.util.List;

import co.mechanism.optimizers.evolutionary.Individual;
import co.mechanism.utils.MersenneTwisterFast;

public class UniformCrossoverProvider implements ICrossoverProvider {

	private MersenneTwisterFast rng;

	public UniformCrossoverProvider() {
		this.rng = new MersenneTwisterFast();
	}

	@Override
	public List<Individual> mate(Individual firstParent, Individual secondParent) {
		Individual offspring1 = firstParent.clone();
		Individual offspring2 = secondParent.clone();
		for (int i = 0; i < firstParent.getPosition().size(); i++) {
			if (rng.nextBoolean()) {
				offspring1.getPosition().set(i,
						secondParent.getPosition().get(i).doubleValue());
				offspring2.getPosition().set(i,
						firstParent.getPosition().get(i).doubleValue());
			}
		}
		List<Individual> results = new ArrayList<Individual>();
		results.add(offspring1);
		results.add(offspring2);
		return results;
	}

}
