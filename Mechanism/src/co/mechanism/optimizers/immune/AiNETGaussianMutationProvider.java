package co.mechanism.optimizers.immune;

import java.util.ArrayList;
import java.util.List;

import co.mechanism.core.AbstractOptimizer;
import co.mechanism.core.DefaultSearchAgent;
import co.mechanism.core.IMutationProvider;
import co.mechanism.core.ISearchAgent;

public class AiNETGaussianMutationProvider implements IMutationProvider {

	

	
	@Override
	public ISearchAgent mutate(ISearchAgent cell,AbstractOptimizer<? extends DefaultSearchAgent> optimizer) {
		// TODO Auto-generated method stub
		List<Double> mutatedParameters = new ArrayList<Double>(cell
				.getPosition().size());
		int dim = 0;
		double mp;
		double max = optimizer.getPopulation()
				.get(optimizer.getPopulation().size() - 1).getValue();
		for (Double p : cell.getPosition()) {
			// Mutation as defined for opt-aiNET
			double alpha = (1 / ((ClonalgOptimizer) optimizer).getBeta()) * Math.exp(-cell.getValue() / max);

			mp = p + alpha * ((ClonalgOptimizer) optimizer).getMt().nextGaussian();
			if (mp < optimizer.getLowerBound().get(dim)) {
				mp = optimizer.getLowerBound().get(dim);
			} else if (mp > optimizer.getUpperBound().get(dim)) {
				mp = optimizer.getUpperBound().get(dim);
			}

			mutatedParameters.add(mp);
			dim++;
		}
		return new Lymphocyte(mutatedParameters);
	}

}
