package co.mechanism.optimizers.evolutionary.genetic;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import co.mechanism.optimizers.evolutionary.Individual;

public class SUSWithRankSelectionProvider implements ISelectionProvider {

	private double selectionPressure;
	private int numberOfParents;

	public SUSWithRankSelectionProvider(double selectionPressure,
			int numberOfParents) {
		this.selectionPressure = selectionPressure;
		this.numberOfParents = numberOfParents;
	}

	@Override
	public List<Individual> selectParents(List<Individual> population) {
		Collections.sort(population);
		List<Individual> selected = new LinkedList<Individual>();
		double totalFitness = 0;
		for (int i = 0; i < population.size(); i++) {
			totalFitness += getRankFitness(i, population.size());
			//System.out.println("rank: "+i+" fitness: "+getRankFitness(i, population.size())+" popsize: "+population.size());
			
		}
		//System.out.println(totalFitness);
		double pointerDistance = totalFitness / numberOfParents;
		//System.out.println(pointerDistance);
		double offset = Math.random() * pointerDistance;
		double cumulativeFitness = 0;
		for (int i = 0; i < population.size(); i++) {
			cumulativeFitness += getRankFitness(i, population.size())/totalFitness;
			while (cumulativeFitness > offset) {
			//for (double j = cumulativeFitness;j > offset; offset += pointerDistance) {
				selected.add(population.get(i).clone());
				offset += pointerDistance;
				//System.out.println("el individuo "+(i+1)+" fue seleccionado. "+ "cumulativeFitness: "+cumulativeFitness+". offset: "+offset);

			}
		}
		return selected;
	}

	private double getRankFitness(int rank, int size) {
		return Math.pow((1 - (double)rank / size), selectionPressure);
	}

}
