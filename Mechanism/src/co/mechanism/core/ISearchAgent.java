/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.mechanism.core;

import java.util.List;

/**
 *
 * @author william
 */
public interface ISearchAgent extends Comparable<ISearchAgent>{
    
    public List<Double> getPosition();
    public void setPosition(List<Double> position);
    public Double getValue();
    public Double evaluate(ICostFunction f);
    
}
