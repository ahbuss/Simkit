/*
 * ContinuousDistribution.java
 *
 * Created on September 19, 2001, 4:32 PM
 */

package simkit.random;

/**
 *
 * @author  Arnold Buss
 * @version 
 */
public interface ContinuousDistribution extends Distribution{
    
    public double pdf(double x);
    
}

