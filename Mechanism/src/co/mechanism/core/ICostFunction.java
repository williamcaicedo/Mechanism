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
public interface ICostFunction {

    public double evaluate(List<Double> inputs) throws Exception;
}
