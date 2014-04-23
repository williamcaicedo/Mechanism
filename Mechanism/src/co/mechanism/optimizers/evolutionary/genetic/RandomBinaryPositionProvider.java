package co.mechanism.optimizers.evolutionary.genetic;

import java.util.ArrayList;
import java.util.List;

import co.mechanism.core.AbstractOptimizer;
import co.mechanism.core.ISearchAgent;
import co.mechanism.utils.MersenneTwisterFast;
import co.mechanism.utils.Utils;

public class RandomBinaryPositionProvider implements IPositionProvider {

	private MersenneTwisterFast mt;
	
	
	public RandomBinaryPositionProvider() {
		this.mt = Utils.getMTInstance();
	}


	@Override
	public List<Double> getRandomPosition(AbstractOptimizer<? extends ISearchAgent> opt) {
		double v;
		List<Double> temp = new ArrayList<Double>();
		for (int j = 0; j < opt.getLowerBound().size(); j++) {
			v = (this.mt.nextBoolean()) ? 1 : 0;
			temp.add(v);
		}
		return temp;
	}

}
