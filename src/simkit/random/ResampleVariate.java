package simkit.random;

/** 
 * Resamples from given data array.  A generated value is
 * equally likely to be from any value in the data array.
 * @author Arnold Buss
 */
public class ResampleVariate extends RandomVariateBase {
    
    private double[] data;
    
    /**
     * @return resampled value, equally likely from data array
     */    
    public double generate() {
        return data[ (int) (rng.draw() * data.length)];
    }
    
    /**
     * @return copy of data array as firts (and only) element
     */    
    public Object[] getParameters() {
        return new Object[] { getData() };
    }
    
    /**
     * @param obj  array with one element containing the double[]
     * data values to be resampled.
     */    
    public void setParameters(Object[] obj) {
        if (obj.length != 1) {
            throw new IllegalArgumentException("Need 1 element: " + obj.length);
        }
        else if ( !(obj[0] instanceof double[]) ) {
            throw new IllegalArgumentException("double[] needed: " + obj.getClass());
        }
        setData( (double[]) obj[0] );
    }
    
    public void setData(double[] data) {
        this.data = (double[]) data.clone();
    }
    
    /**
     * @return copy of data array
     */    
    public double[] getData() {
        return (double[]) data.clone();
    }
    
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
