package simkit.random;

/** 
 * Generates random variates by resampling from a given data array.  A generated value is
 * equally likely to be from any value in the data array.
 * @author Arnold Buss
 * @version $Id$
 */
public class ResampleVariate extends RandomVariateBase {
    
/**
* The array of data from which to sample.
**/
    private double[] data;
    
    /**
     * Generates the next value by sampling with equal likelihood from the
     * data array. 
     * @return resampled value, equally likely from data array
     */    
    public double generate() {
        return data[ (int) (rng.draw() * data.length)];
    }
    
    /**
     * Returns a single element array that contains a copy of the data array.
     * @return copy of data array as first (and only) element
     */    
    public Object[] getParameters() {
        return new Object[] { getData() };
    }
    
    /**
     * Sets the contents of the array from which to sample.
     * @param obj  array with one element containing the double[]
     * data values to be resampled.
     * @throws IllegalArgumentException If the array does not have exactly 1
     * element or if the element is not an array of <code>doubles</code>.
     */    
    public void setParameters(Object... obj) {
        if (obj.length != 1) {
            throw new IllegalArgumentException("Need 1 element: " + obj.length);
        }
        else if ( !(obj[0] instanceof double[]) ) {
            throw new IllegalArgumentException("double[] needed: " + obj.getClass());
        }
        setData( (double[]) obj[0] );
    }
    
/**
* Sets the array from which to sample.
**/
    public void setData(double[] data) {
        this.data = (double[]) data.clone();
    }
    
    /**
     * Returns a copy of the data array.
     */    
    public double[] getData() {
        return (double[]) data.clone();
    }
    
/**
* Returns a String containing the contents of the data array.
**/
    public String toString() {
        StringBuffer buf = new StringBuffer("Resample Variate\n[");
        int count = 0;
        for (int i = 0; i < data.length; ++i) {
            buf.append(data[i]);
            if ( ++count % 10 == 0) {
                buf.append('\n');
            }
            else {
                buf.append(' ');
            }
        }
        buf.append(']');
        return buf.toString();
    }
    
}
