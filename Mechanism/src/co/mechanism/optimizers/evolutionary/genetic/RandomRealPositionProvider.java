package co.mechanism.optimizers.evolutionary.genetic;

import java.util.ArrayList;
import java.util.List;

import co.mechanism.core.AbstractOptimizer;
import co.mechanism.core.ISearchAgent;
import co.mechanism.utils.MersenneTwisterFast;
import co.mechanism.utils.Utils;

public class RandomRealPositionProvider implements IPositionProvider {

	private MersenneTwisterFast mt;

	public RandomRealPositionProvider() {
		this.mt = Utils.getMTInstance();
	}

	@Override
	public List<Double> getRandomPosition(
			AbstractOptimizer<? extends ISearchAgent> opt) {
		double v;
		List<Double> temp = new ArrayList<Double>(opt.getLowerBound().size());
		for (int j = 0; j < opt.getLowerBound().size(); j++) {
			v = opt.getLowerBound().get(j)
					+ (mt.nextDouble() * ((opt.getUpperBound().get(j) - opt
							.getLowerBound().get(j))));
			temp.add(v);
		}
		return temp;
	}
}
