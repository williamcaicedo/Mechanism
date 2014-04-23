package co.mechanism.optimizers.evolutionary.genetic;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import co.mechanism.optimizers.evolutionary.Individual;

public class ElitismReplacementProvider implements IReplacementProvider {

	@Override
	public List<Individual> replaceIndividuals(List<Individual> offspring, List<Individual> population) {
		int size = population.size();
		Iterator<Individual> it = offspring.iterator();
		while(it.hasNext()) {
			population.add(it.next().clone());
		}
		Collections.sort(population);
		for(int i = population.size()-1; i >= size; i--) {
			population.remove(i);
		}
		//System.out.println(population.size());
		return population;
	}

}
