/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.mechanism.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author william
 */
public abstract class AbstractOptimizer <T extends ISearchAgent> {
    private List<T> agents;
    private List<Double> lowerBound;
    private List<Double> upperBound;
    private ICostFunction costFunction;
    
    public AbstractOptimizer(List<Double> lowerBound, List<Double> upperBound, ICostFunction costFunction) {
    	this.lowerBound = lowerBound;
    	this.upperBound = upperBound;
    	this.costFunction = costFunction;
    	agents = new ArrayList<T>();
    }
    public abstract void evolve();
    public void evolve(int generations) {
        for(int i = 0; i < generations; i++) {
            evolve();
        }
    }
    public T getBestSolution() {
        return Collections.min(agents);
    }
    public List<T> getPopulation() {
        return this.agents;
    }
    public ICostFunction getCostFunction() {
    	return this.costFunction;
    }
    public void setCostFunction(ICostFunction f) {
    	this.costFunction = f;
    }
	public List<Double> getLowerBound() {
		return lowerBound;
	}
	public void setLowerBound(List<Double> lowerBound) {
		this.lowerBound = lowerBound;
	}
	public List<Double> getUpperBound() {
		return upperBound;
	}
	public void setUpperBound(List<Double> upperBound) {
		this.upperBound = upperBound;
	}
    
}
