package co.mechanism.optimizers.immune;

import java.util.ArrayList;
import java.util.List;

import co.mechanism.core.AbstractOptimizer;
import co.mechanism.core.DefaultSearchAgent;
import co.mechanism.core.IMutationProvider;
import co.mechanism.core.ISearchAgent;
import co.mechanism.utils.MersenneTwisterFast;

public class BitFlipMutationProvider implements IMutationProvider {

	private double probability;
	private MersenneTwisterFast rng;
	
	public BitFlipMutationProvider(double flipProbability){
		this.probability = flipProbability;
		this. rng = new MersenneTwisterFast();
	}

	@Override
	//public ISearchAgent mutate(ISearchAgent agent, AbstractOptimizer<? extends DefaultSearchAgent> opt) {
	public ISearchAgent mutate(ISearchAgent cell, AbstractOptimizer<? extends DefaultSearchAgent> opt) {
		// TODO Auto-generated method stub
		//Lymphocyte mutant;
		double mp = 0;
		//int dim = 0;
		List<Double> mutatedParameters = new ArrayList<Double>();
		for (Double p : cell.getPosition()) {
            if (rng.nextDouble()< probability) {
            	mp = (p==0)?1:0;
            }else{
            	mp = p;
            }
            mutatedParameters.add(mp);
            //dim++;
        }
		cell.setPosition(mutatedParameters);
        //mutant = new Lymphocyte(mutatedParameters);
        return cell;
	}

}
