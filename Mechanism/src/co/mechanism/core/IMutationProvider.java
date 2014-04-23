/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.mechanism.core;


/**
 *
 * @author william
 */
public interface IMutationProvider {
	
	public ISearchAgent mutate(ISearchAgent agent,AbstractOptimizer< ? extends DefaultSearchAgent> optimizer);
    
}
