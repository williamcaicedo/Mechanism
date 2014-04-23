package co.mechanism.optimizers.evolutionary.genetic;

import java.util.List;

import co.mechanism.core.AbstractOptimizer;
import co.mechanism.core.ISearchAgent;

public interface IPositionProvider {
	
	
	List<Double> getRandomPosition(AbstractOptimizer<? extends ISearchAgent> opt);

}
