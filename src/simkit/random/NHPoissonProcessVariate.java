/*
 * NHPoissonProcessVariate.java
 *
 * Created on October 1, 2001, 4:24 PM
 */

package simkit.random;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Generates interarrival times for a Non-homogenious (non-stationary) Poisson Process.
 * <p>This implementation uses the inverse-integral transform. The user must supply
 * a Method that is the inverse of the expectation function (&Lambda;).
 * See Law and Kelton: Simulation Modeling and Analysis 2nd Edition, pp 509-510 or 
 * 3rd edition pp486-487.</p>
 *
 * @author  Arnold Buss
 * @version $Id$
 */
public class NHPoissonProcessVariate implements simkit.random.RandomVariate {
    
/**
* The time to start the process.
**/
    private double startTime;

/**
* The instance of the supporting RandomNumber.
**/
    private RandomNumber rng;

/**
* A Method to provide the inverse of the expectation function.
**/
    private Method inverseIntegratedRate;

/**
* The instance of an Object that contains the inverse expectation function.
**/
    private Object rateInvoker;

/**
* The last unit rate arrival time.
**/
    private double lastUnitRatePoisson;

/**
* The last actual arrival time.
**/
    private double lastGeneratedTime;

/**
* Holds the argument for the inverse expectation function.
**/
    private Object[] arg;

    /** 
      * Creates new NHPoissonProcessVariate, the parameters must be set prior to use.
    */
    public NHPoissonProcessVariate() {
        rng = RandomNumberFactory.getInstance();
        arg = new Object[1];
    }

    /**
     * Generate a random variate having this class's distribution.
     * @return The generated random variate or NaN if there was an error during generation.
     */
    public double generate() {
        lastUnitRatePoisson -= Math.log(rng.draw());
        arg[0] = new Double(lastUnitRatePoisson);
        try {
            Number num = (Number) this.inverseIntegratedRate.invoke(rateInvoker, arg ); 
            double inter = num.doubleValue() - this.lastGeneratedTime;
            lastGeneratedTime = num.doubleValue();
            return inter;
        }
        catch (IllegalAccessException e) { System.err.println(e);}
        catch (InvocationTargetException e) { System.err.println(e.getTargetException());}
        finally{}
        return Double.NaN;
    }
    
    /**
      * Returns a 3 element array containing the Method for the inverse function, 
      * the Object in which the inverse function exists, and the start time.
     */
    public Object[] getParameters() {
        return new Object[] { inverseIntegratedRate, rateInvoker, new Double(startTime) };
    }
    
    /**
     * Gets the instance of the underlying RandomNumber.
     * @return The underlying RandomNumber instance (should be a copy)
     */
    public RandomNumber getRandomNumber() {
        return rng; 
    }
    
    /**
      * Sets the Lambda-inverse Method and the starting time.
      * @param params A 3 element array containing the Method to evaluate the 
      * inverse expectation function (Lambda inverse), the Object in which
      * Lambda inverse resides, and the start time.
      * @throws IllegalArgumentException If the array does not contain exactly
      * 3 elements.
     */
    public void setParameters(Object[] params) {
        if (params.length != 3)  {
            throw new IllegalArgumentException(
                "NHPoissonProcessVariate requires 3 parameters: " +
                "(<reference to Lambda-inverse Method>, <object method belongs to>, "+
                " (start time)");
        }
        this.setInverseIntegratedRate((Method) params[0] );
        this.setRateInvoker(params[1]);
        if (params.length == 3) {
           this.setStartTime(((Number) params[2]).doubleValue());
        } 
    }
    
    /** Sets the supporting RandomNumber object
     * @param rng The RandomNumber instance supporting the generating algorithm
     */
    public void setRandomNumber(RandomNumber rng) {
        this.rng = rng;
    }
    
/**
* Sets the Lambda inverse Method.
**/
    public void setInverseIntegratedRate(Method m) { inverseIntegratedRate = m; }
    
/**
* Returns a reference to the Lambda inverse Method.
**/
    public Method getInverseIntegratedRate() { return inverseIntegratedRate; }
    
/**
* Sets the instance of the Object on which to invoke the Lambda inverse Method.
**/
    public void setRateInvoker(Object o) { rateInvoker = o; }

/**
* Returns the instance of the Object on which to invoke the Lambda inverse Method.
**/
    public Object getRateInvoker() { return rateInvoker; }
    
/**
* Sets the time to start the process.
**/
    public void setStartTime(double time) { 
        if (time >= 0.0) {
            this.startTime = time;
            this.lastUnitRatePoisson = getStartTime();
            this.lastGeneratedTime = getStartTime();
        } else {
           throw new IllegalArgumentException("Start time must be non-negative: " + 
               time);
        }
    }

/**
* Returns the time the process started.
**/
    public double getStartTime() { return startTime; }
    
/**
* Returns a String containing the name of this variate, the name of the 
* inverse Method, information about the Object on which the inverse Method is
* invoked, and the start time.
**/
    public String toString() {
        return "Non-Homogeneous Poisson Interarrival Times (" +
            this.getInverseIntegratedRate().getName() + ", " +
            this.getRateInvoker() + ", " +
            this.getStartTime() + ")";
    }
        
}
