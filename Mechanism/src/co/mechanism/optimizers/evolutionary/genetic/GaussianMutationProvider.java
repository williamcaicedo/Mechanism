package co.mechanism.optimizers.evolutionary.genetic;

import java.util.List;

import co.mechanism.core.AbstractOptimizer;
import co.mechanism.core.DefaultSearchAgent;
import co.mechanism.core.IMutationProvider;
import co.mechanism.core.ISearchAgent;
import co.mechanism.utils.MersenneTwisterFast;
import co.mechanism.utils.Utils;

public class GaussianMutationProvider implements IMutationProvider {
	private double probability;
	//private double std;
	public GaussianMutationProvider(double probability){
		this.probability = probability;
		//this.std = std;	
	}

	@Override
	public ISearchAgent mutate(ISearchAgent agent, AbstractOptimizer<? extends DefaultSearchAgent> opt) {
		// TODO Auto-generated method stub
		List<Double> upperBound = opt.getUpperBound();
		List<Double> lowerBound = opt.getLowerBound();
		
		MersenneTwisterFast rng = Utils.getMTInstance();
		double ub, lb = 0;
		for(int i = 0; i < agent.getPosition().size(); i++) {
			if (rng.nextDouble() < probability) {
				double old = agent.getPosition().get(i);
				ub = upperBound.get(i);
				lb = lowerBound.get(i);
				agent.getPosition().set(i, Math.min(Math.max((ub-lb)*rng.nextGaussian()+old,lb),ub));
			}
		}
		return agent;
	}


}
