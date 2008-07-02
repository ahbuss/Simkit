/*
 * OscillatingExponentialVariate.java
 *
 * Created on March 14, 2002, 2:31 PM
 */

package simkit.random;
import simkit.Schedule;
/**
 * Generates an Exponential variate that is scaled by a sinusoid. 
 * The random draw is equal to (&mu; + a * cos(2&pi;ft + &Theta;))Exp(1).<BR/>
 * Where: <ul>
 * <li> &mu; is the mean value of the sinusoid.</li>
 * <li> a is the amplitude of the sinusoid.</li>
 * <li> f is the frequency in cycles per unit of sim time.</li>
 * <li> t is the current sim time.</li>
 * <li> &Theta; is the phase shift in cycles.</li>
 * <li> Exp(1) is an exponential random variate with mean of 1.0</li>
 * </ul>
 * @author  Arnold Buss
 * @version $Id$
 */
public class OscillatingExponentialVariate implements simkit.random.RandomVariate {
    
    private static final double TWO_PI = 2.0 * Math.PI;
    
/**
* The supporting RandomNumber.
**/
    private RandomNumber rng;
    
/**
* The mean of the sinusoid.
**/
    private double mean;

/**
* The amplitude of the sinusoid.
**/
    private double amplitude;

/**
* The frequency in cycles per unit of SimTime.
**/
    private double frequency;

/**
* The phase shift in cycles.
**/
    private double phase;

    /** Creates new OscillatingExponentialVariate, parameters must be set prior to use. */
    public OscillatingExponentialVariate() {
        setRandomNumber(RandomVariateFactory.getDefaultRandomNumber());
    }

    /**
     * Returns the instance of the underlying RandomNumber.
     * @return The underlying RandomNumber instance (should be a copy)
     */
    public RandomNumber getRandomNumber() { return rng; }
    
    /** Sets the supporting RandomNumber object
     * @param rng The RandomNumber instance supporting the generating algorithm
     */
    public void setRandomNumber(RandomNumber rng) { this.rng = rng; }
    
    /**
      * Returns a 4 element array containing the mean, amplitude, frequency, and phase
      * as Doubles.
     */
    public Object[] getParameters() {
        return new Object[] { new Double(mean), new Double(amplitude),  
                new Double(frequency), new Double(phase)};
    }
    
    /**
      * Sets the mean, amplitude, frequency, and phase.
      * @param params A 4 element array containing the mean, amplitude, frequency, and
      * phase shift as Numbers.
      * @throws IllegalArgumentException If the array does not contain exactly 4 elements, or
      * if any of the elements is not a Number.
     */
    public void setParameters(Object... params) {
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
            + phase)) * Math.log(rng.draw());
    }
    
/**
* The mean of the sinusoid.
**/
    public void setMean(double mean) { this.mean = mean; }
    
/**
* The mean of the sinusoid.
**/
    public double getMean() { return mean; }
    
/**
* The amplitude of the sinusoid.
**/
    public void setAmplitude(double amplitude) { this.amplitude = amplitude; }
    
/**
* The amplitude of the sinusoid.
**/
    public double getAmplitude() {  return amplitude; }
    
/**
* The frequency in cycles per unit of SimTime.
**/
    public void setFrequency(double frequency) { 
        this.frequency = frequency ; 
    }
    
/**
* The frequency in cycles per unit of SimTime.
**/
    public double getFrequency() { return frequency; }
    
/**
* The phase shift in cycles.
**/
    public void setPhase(double phase) {
        this.phase = phase % TWO_PI;
    }
    
/**
* The phase shift in cycles.
**/
    public double getPhase() { return phase; }
    
/**
* Returns an String containing the name of this variate with the mean, amplitude,
* frequency, and phase.
**/
    public String toString() {
        return "Oscillating Exponential Variate (" + getMean() + ", " +
            getAmplitude() + ", " + getFrequency() + ", " + getPhase() +")";
    }

    @Override
    public void setParameter(String paramName, Object paramValue) {
        if(paramName.equalsIgnoreCase("mean")) {
            Number p = (Number)paramValue;
            this.setMean(p.doubleValue());
        } else if(paramName.equalsIgnoreCase("amplitude")) {
            Number p = (Number)paramValue;
            this.setAmplitude(p.doubleValue());
        } else if(paramName.equalsIgnoreCase("frequency")) {
            Number p = (Number)paramValue;
            this.setFrequency(p.doubleValue());
        } else if(paramName.equalsIgnoreCase("phase")) {
            Number p = (Number)paramValue;
            this.setPhase(p.doubleValue());
        } else {
            throw new IllegalArgumentException(paramName + " is not a" +
                    " random variate parameter for " + this.getClass().getName());
        }
    }
}
