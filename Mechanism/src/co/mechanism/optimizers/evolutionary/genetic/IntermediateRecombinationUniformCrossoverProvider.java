package co.mechanism.optimizers.evolutionary.genetic;

import java.util.ArrayList;
import java.util.List;

import co.mechanism.optimizers.evolutionary.Individual;
import co.mechanism.utils.MersenneTwisterFast;

public class IntermediateRecombinationUniformCrossoverProvider implements
		ICrossoverProvider {

	private MersenneTwisterFast rng;

	public IntermediateRecombinationUniformCrossoverProvider() {
		this.rng = new MersenneTwisterFast();
	}
	
	@Override
	public List<Individual> mate(Individual firstParent, Individual secondParent) {
		// TODO Auto-generated method stub
		Individual offspring1 = firstParent.clone();
		Individual offspring2 = secondParent.clone();
		double alpha;
		for (int i = 0; i < firstParent.getPosition().size(); i++) {
			alpha = rng.nextDouble();
			offspring1.getPosition().set(i,
					alpha*offspring1.getPosition().get(i) + (1-alpha)*offspring2.getPosition().get(i));
			alpha = 1 - alpha;
			offspring2.getPosition().set(i,
					alpha*offspring1.getPosition().get(i) + (1-alpha)*offspring2.getPosition().get(i));
		}
		List<Individual> results = new ArrayList<Individual>();
		results.add(offspring1);
		results.add(offspring2);
		return results;		
	}

}
