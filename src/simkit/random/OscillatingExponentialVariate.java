/*
 * OscillatingExponentialVariate.java
 *
 * Created on March 14, 2002, 2:31 PM
 */

package simkit.random;
import simkit.random.*;
import simkit.*;
/**
 *
 * @author  Arnold Buss
 * @version 
 */
public class OscillatingExponentialVariate implements simkit.random.RandomVariate {
    
    private static final double TWO_PI = 2.0 * Math.PI;
    
    private RandomNumber rng;
    
    private double mean;
    private double amplitude;
    private double frequency;
    private double phase;

    /** Creates new OscillatingExponentialVariate */
    public OscillatingExponentialVariate() {
        rng = RandomNumberFactory.getInstance();
    }

    /**
     * @return The underlying RandomNumber instance (should be a copy)
     */
    public RandomNumber getRandomNumber() { return rng; }
    
    /** Sets the supporting RandomNumber object
     * @param rng The RandomNumber instance supporting the generating algorithm
     */
    public void setRandomNumber(RandomNumber rng) { this.rng = rng; }
    
    /**
     * Returns the array of parameters as an Object[].
     * @return the array of parameters as an Object[].
     */
    public Object[] getParameters() {
        return new Object[] { new Double(mean), new Double(amplitude),  
                new Double(frequency), new Double(phase)};
    }
    
    /**
     * Sets the random variate's parameters.
     * Alternatively, the parameters could be set in the constructor or
     * in additional methods provided by the programmer.
     * @param params the array of parameters, wrapped in objects.
     */
    public void setParameters(Object[] params) {
        if (params == null) {
            throw new NullPointerException("Null parameters sent to OscillatingExponentialVariate");
        }
        if (params.length != 4) {
            throw new IllegalArgumentException("OscillatingExponentialVariate needs 4 " +
                " parameters: " + params.length);
        }
        try {
            setMean( ((Number) params[0]).doubleValue() );
            setAmplitude( ((Number) params[1]).doubleValue() );
            setFrequency( ((Number) params[2]).doubleValue() );
            setPhase( ((Number) params[3]).doubleValue() );
        }
        catch (ClassCastException e) {
            throw new IllegalArgumentException(
            "params in OscillatingExponentialVariate has a non-Number value");
        }
    }
    
    /**
     * Generate a random variate having this class's distribution.
     * @return The generated random variate
     */
    public double generate() {
        return - (mean + amplitude * Math.cos( TWO_PI * frequency * Schedule.getSimTime() 
            + phase)) * Math.exp(rng.draw());
    }
    
    public void setMean(double mean) { this.mean = mean; }
    
    public double getMean() { return mean; }
    
    public void setAmplitude(double amplitude) { this.amplitude = amplitude; }
    
    public double getAmplitude() {  return amplitude; }
    
    public void setFrequency(double frequency) { 
        this.frequency = frequency ; 
    }
    
    public double getFrequency() { return frequency; }
    
    public void setPhase(double phase) {
        phase = phase % TWO_PI;
    }
    
    public double getPhase() { return phase; }
    
    
    public Object clone() {
        try {
            return super.clone();
        }
        catch (CloneNotSupportedException e) {}
        return null;
    }
    
    public String toString() {
        return "Oscillating Exponential Variate (" + getMean() + ", " +
            getAmplitude() + ", " + getFrequency() + ", " + getPhase() +")";
    }
}
