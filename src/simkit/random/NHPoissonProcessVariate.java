/*
 * NHPoissonProcessVariate.java
 *
 * Created on October 1, 2001, 4:24 PM
 */

package simkit.random;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import simkit.BasicSimEntity;

/**
 * Generates interarrival times for a Non-homogenious (non-stationary) Poisson Process.
 * <p>This implementation uses the inverse-integral transform. The user must supply
 * a Method that is the inverse of the expectation function (&Lambda;).
 * See Law and Kelton: Simulation Modeling and Analysis 2nd Edition, pp 509-510 or 
 * 3rd edition pp486-487.</p>
 *
 * @author  Arnold Buss
 * @version $Id: NHPoissonProcessVariate.java 1010 2007-03-16 18:38:16Z ahbuss $
 */
public class NHPoissonProcessVariate extends BasicSimEntity implements RandomVariate {
    
/**
* The time to start the process.
**/
    protected double startTime;

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
    protected double lastUnitRatePoisson;

/**
* The last actual arrival time.
**/
    protected double lastGeneratedTime;

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
    
    public void reset() {
        super.reset();
        startTime = getEventList().getSimTime();
        lastUnitRatePoisson = getStartTime();
        lastGeneratedTime = getStartTime();
    }

    /**
     * Generate a random variate having this class's distribution.
     * @return The generated random variate or NaN if there was an error during generation.
     */
    public double generate() {
        lastUnitRatePoisson -= Math.log(rng.draw());
        arg[0] = new Double(getLastUnitRatePoisson());
        try {
            Number num = (Number) this.inverseIntegratedRate.invoke(rateInvoker, arg ); 
            double inter = num.doubleValue() - this.getLastGeneratedTime();
            lastGeneratedTime = num.doubleValue();
            return inter;
        }
        catch (IllegalAccessException e) { throw new RuntimeException(e);}
        catch (InvocationTargetException e) { throw new RuntimeException(e.getTargetException());}
    }
    
    /**
      * Returns a 3 element array containing the Method for the inverse function, 
      * the Object in which the inverse function exists, and the start time.
     */
    public Object[] getParameters() {
        return new Object[] { getInverseIntegratedRate(), getRateInvoker() };
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
    public void setParameters(Object... params) {
        if (params.length != 2)  {
            throw new IllegalArgumentException(
                "NHPoissonProcessVariate requires 2 parameters: " +
                "(<Lambda-inverse Method>, <object method belongs to>");
        }
        this.setRateInvoker(params[1]);
        Method method = null;
        if (params[0] instanceof Method) {
            method = (Method) params[0];
        }
        else if (params[0] instanceof String) {
            Class<?> clazz = getRateInvoker().getClass();
            try {
                method = clazz.getMethod(params[0].toString(), new Class[] { double.class } );
            }
            catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
        this.setInverseIntegratedRate(method );
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
* Returns the time the process started.
**/
    public double getStartTime() { return startTime; }
    
/**
* Returns a String containing the name of this variate, the name of the 
* inverse Method, information about the Object on which the inverse Method is
* invoked, and the start time.
**/
    public String toString() {
        return "Non-Homogeneous Poisson - Inverse Transform (" +
            this.getInverseIntegratedRate().getName() + ", " +
            this.getRateInvoker() +  ")";
    }

    public String stateString() {
        return "State: " +
                "startTime = " + getStartTime() + " lastUnitRatePoisson = " +
                getLastUnitRatePoisson() + " lastGenerateTime = " +
                getLastGeneratedTime() ;
    }
    
    public void handleSimEvent(simkit.SimEvent event) {
    }

    public void processSimEvent(simkit.SimEvent event) {
    }

    public double getLastUnitRatePoisson() {
        return lastUnitRatePoisson;
    }

    public double getLastGeneratedTime() {
        return lastGeneratedTime;
    }
    
}