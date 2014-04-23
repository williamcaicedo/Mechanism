/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.mechanism.core;

import java.util.List;

import co.mechanism.optimizers.evolutionary.Individual;
import co.mechanism.utils.Utils;

/**
 *
 * @author william
 */
public abstract class DefaultSearchAgent implements ISearchAgent{
    

	private List<Double> position;
    private Double value;
    public DefaultSearchAgent(List<Double> position) {
        this.position = position;
    }
    
    @Override
    public Double evaluate(ICostFunction f) {
        try {
            this.value = f.evaluate(position);
            return this.value;
        }catch(Exception e){
            System.out.println(position);
            System.out.println(e);
        }
        return Double.NaN;
    }
    
    @Override
    public List<Double> getPosition() {
        return position;
    }

    @Override
    public void setPosition(List<Double> position) {
        this.position = position;
    }

    @Override
    public Double getValue() {
        return value;
    }
    
    public void setValue(Double value) {
    	this.value = value;
    	
    }
    
    @Override
    public int compareTo(ISearchAgent agent) {
    	//System.out.println(agent);
        if (this.value < agent.getValue()) {
            return -1;
        }
        if (this.value > agent.getValue()) {
            return 1;
        } else {
            return 0;
        }
        //return (int)(this.value - cell.getValue());
    }
    
    @Override
	public Individual clone() {
		// TODO Auto-generated method stub
		Individual ind = new Individual(Utils.doubleListDeepCopy(getPosition()));
		ind.setValue(getValue());
		return ind;
	}

    @Override
	public String toString() {
		// TODO Auto-generated method stub
		return "position: "+position.toString()+" value: "+value;
	}
    
}
