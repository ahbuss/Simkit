/*
 * NSPoissonProcessVariate.java
 *
 * Created on October 1, 2001, 4:24 PM
 */

package simkit.random;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import simkit.Schedule;

/**
 *
 * @author  Arnold Buss
 * @version $Id$
 */
public class NHPoissonProcessVariate implements simkit.random.RandomVariate {
    
    private double startTime;
    private RandomNumber rng;
    private Method inverseIntegratedRate;
    private Object rateInvoker;
    private double lastUnitRatePoisson;
    private double lastGeneratedTime;
    private Object[] arg;

    /** Creates new NSPoissonProcessVariate */
    public NHPoissonProcessVariate() {
        rng = RandomNumberFactory.getInstance();
        arg = new Object[1];
    }

    /** Note: copy should be typically shallow in that the same RandomNumber instance is supporting the copy.
     * @return Copy of this RandomVariate instance.
     */
    public Object clone() {
        Object copy = null;
        try {
            copy = super.clone();
        } catch (CloneNotSupportedException e) {}
        return copy;        
    }
    
    /**
     * Generate a random variate having this class's distribution.
     * @return The generated random variate
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
     * Returns the array of parameters as an Object[].
     * @return the array of parameters as an Object[].
     */
    public Object[] getParameters() {
        return new Object[] { inverseIntegratedRate, rateInvoker, new Double(startTime) };
    }
    
    /**
     * @return The underlying RandomNumber instance (should be a copy)
     */
    public RandomNumber getRandomNumber() {
        return rng; 
    }
    
    /**
     * Sets the random variate's parameters.
     * Alternatively, the parameters could be set in the constructor or
     * in additional methods provided by the programmer.
     * @param params the array of parameters, wrapped in objects.
     */
    public void setParameters(Object[] params) {
        if (params.length != 3) {
            throw new IllegalArgumentException(
                "NSPoissonProcessVariate requires 3 parameters: " +
                "(<reference to Lambda-inverse Method>, <object method belongs to>, "+
                " start time)");
        }
        this.setInverseIntegratedRate((Method) params[0] );
        this.setRateInvoker(params[1]);
        this.setStartTime(((Number) params[2]).doubleValue());
    }
    
    /** Sets the supporting RandomNumber object
     * @param rng The RandomNumber instance supporting the generating algorithm
     */
    public void setRandomNumber(RandomNumber rng) {
        this.rng = (RandomNumber) rng.clone();
    }
    
    public void setInverseIntegratedRate(Method m) { inverseIntegratedRate = m; }
    
    public Method getInverseIntegratedRate() { return inverseIntegratedRate; }
    
    public void setRateInvoker(Object o) { rateInvoker = o; }
    public Object getRateInvoker() { return rateInvoker; }
    
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
    public double getStartTime() { return startTime; }
    
    public String toString() {
        return "Non-Homogeneous Poisson Interarrival Times (" +
            this.getInverseIntegratedRate().getName() + ", " +
            this.getRateInvoker() + ", " +
            this.getStartTime() + ")";
    }
        
}
