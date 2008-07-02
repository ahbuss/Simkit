/*
 * RenewalProcessVariate.java
 *
 * Created on March 29, 2002, 7:46 AM
 */

package simkit.random;

/**
 * Generates arrival times for a renewal process. The distribution of the 
 * interarrival times is determined by the instance of the supporting RandomVariate.
 * @author  Arnold Buss
 * @version $Id$
 */
public class RenewalProcessVariate implements RandomVariate {
    
/**
* The supporting RandomVariate used to generate the interarrival times.
**/
    protected RandomVariate interarrivalTime;

/**
* Holds the value of the last arrival time.
**/
    protected double lastTime;

/** 
* Creates new RenewalProcessVariate with no supporting RandomVariate specified.
* The RandomVariate must be set prior to use.
 */

    public RenewalProcessVariate() {
        lastTime = 0.0;
    }

    /**
     * Returns the instance of the supporting RandomNumber.
     * @return The underlying RandomNumber instance
     */
    public RandomNumber getRandomNumber() {
        return interarrivalTime.getRandomNumber();
    }
    
    /** Sets the supporting RandomNumber object
     * @param rng The RandomNumber instance supporting the generating algorithm
     */
    public void setRandomNumber(RandomNumber rng) {
        interarrivalTime.setRandomNumber(rng);
    }
    
    /**
      * Returns a single element array containing the instance of the 
      * interarrival time RandomVariate.
     */
    public Object[] getParameters() {
        return new Object[] { interarrivalTime };
    }
    
    /**
      * Sets the RandomVariate used to generate the interarrival times.
      * @param params A single element array containing an instance of a RandomVariate.
      * @throws IllegalArgumentException If the array does not contain exactly 1
      * element or if the element is not a RandomVariate.
     */
    public void setParameters(Object... params) {
        if (params == null) {
            throw new NullPointerException("RenewalProcess params cannot be null");
        }
        if(params.length != 1) {
            throw new IllegalArgumentException("RenewalProcess needs 1 parameter: " + params.length);
        }
        if (!(params[0] instanceof RandomVariate)) {
            throw new IllegalArgumentException("RenewalProcess needs RandomVariate instance: " + params[0].getClass().getName());
        }
        this.setInterarrivalTime((RandomVariate) params[0]);
    }
    
    /**
      * Generate the next arrival time.
      * @return The arrival time.
     */
    public double generate() {
        lastTime += interarrivalTime.generate();
        return lastTime;
    }
    
/**
 * Sets the RandomVariate used to generate the interarrival times.
 * Note: the instance is passed in, not copied.
**/
    public void setInterarrivalTime(RandomVariate rv) {
        interarrivalTime = rv;
    }
    
/**
* Returns the RandomVariate used to generate the interarrival times.
**/
    public RandomVariate getInterarrivalTime() { return interarrivalTime; }

    @Override
    public void setParameter(String paramName, Object paramValue) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
