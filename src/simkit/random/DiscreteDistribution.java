/*
 * DiscreteDistribution.java
 *
 * Created on September 19, 2001, 4:42 PM
 */

package simkit.random;

/**
 *
 * @author  Arnold Buss
 * @version 
 */
public interface DiscreteDistribution extends Distribution {
    
    public double pmf(double x);
        
}

