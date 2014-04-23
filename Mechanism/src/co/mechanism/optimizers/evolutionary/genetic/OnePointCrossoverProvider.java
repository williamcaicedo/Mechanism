package co.mechanism.optimizers.evolutionary.genetic;

import java.util.ArrayList;
import java.util.List;

import co.mechanism.optimizers.evolutionary.Individual;
import co.mechanism.utils.MersenneTwisterFast;
import co.mechanism.utils.Utils;

public class OnePointCrossoverProvider implements ICrossoverProvider {
	
	
	private MersenneTwisterFast rng;

	public OnePointCrossoverProvider() {
		this.rng = Utils.getMTInstance();
	}

	@Override
	public List<Individual> mate(Individual firstParent, Individual secondParent) {
		// TODO Auto-generated method stub
		int point = rng.nextInt(firstParent.getPosition().size());
		List<Double> pos1 = Utils.doubleListDeepCopy(firstParent.getPosition().subList(0, point));
		pos1.addAll(Utils.doubleListDeepCopy(secondParent.getPosition().subList(point, firstParent.getPosition().size())));
		List<Double> pos2 = Utils.doubleListDeepCopy(secondParent.getPosition().subList(point, firstParent.getPosition().size()));
		pos2.addAll(Utils.doubleListDeepCopy(firstParent.getPosition().subList(0, point)));
		Individual offspring1 = new Individual(pos1);
		Individual offspring2 = new Individual(pos2);
		List<Individual> results = new ArrayList<Individual>();
		results.add(offspring1);
		results.add(offspring2);
		return results;
	}

}
