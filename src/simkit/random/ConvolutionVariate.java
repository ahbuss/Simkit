package simkit.random;
import simkit.random.*;
/**
 * Specify array of RandomVariate[].  Generates the convolution (sum) of each one.
 * Each RandomVariate instance is passed the same RandomNumber instance, to
 * avoid seed management problems.
 * @author  Arnold Buss
 */
public class ConvolutionVariate extends RandomVariateBase {
    
    private RandomVariate[] rv;
    
    /** Creates a new instance of ConvolutionVariate */
    public ConvolutionVariate() {
    }
    
    /**
     * @return sum of a draw from each RandomVariate instance
     */    
    public double generate() {
        double value = 0.0;
        for (int i = 0; i < rv.length; ++i) {
            value += rv[i].generate();
        }
        return value;
    }
    
    /**
     * @return clone of RandomVariate[] array in Object[] array
     */    
    public Object[] getParameters() {
        return new Object[] { rv.clone() };
    }
    
    /**
     * @param obj Must be a single instance of RandomVariate[]
     */    
    public void setParameters(Object[] obj) {
        if (obj.length != 1) {
            throw new IllegalArgumentException("Require 1 parameter: " + obj.length);
        }
        if (!(obj[0] instanceof RandomVariate[])) {
            throw new IllegalArgumentException("Require RandomVariate[]: " + obj[0].getClass().getName());
        }
        
        setRandomVariates( (RandomVariate[]) obj[0] );
    }
    
    /**
     * @param rand Array of RandomVariate[] instances
     */    
    public void setRandomVariates(RandomVariate[] rand) {
        rv = (RandomVariate[]) rand.clone();
        for (int i = 0; i < rv.length; ++i) {
            rv[i].setRandomNumber(rng);
        }
    }
    
    /**
     * @return clone of RandomVariate[] array
     */    
    public RandomVariate[] getRandomVariates() { return (RandomVariate[]) rv.clone(); }
    
    /**
     * @param rand RandomVariate[] array
     */    
    public void setRandomNumber(RandomNumber rand) {
        super.setRandomNumber(rand);
        for (int i = 0; i < rv.length; ++i) {
            rv[i].setRandomNumber(rng);
        }
    }
    
    /**
     * @return String including each RandomVariate's toString() on
     * a separate line.
     */    
    public String toString() {
        StringBuffer buf = new StringBuffer("ConvolutionRandomVariate");
        for (int i = 0; i < rv.length; ++i) {
            buf.append('\n');
            buf.append('\t');
            buf.append(rv[i]);
        }
        return buf.toString();
    }
}
