/*
 * TriangleVariate.java
 *
 * Created on April 13, 2002, 2:25 PM
 */

package simkit.random;

/**
 *
 * @author  Arnold Buss
 * @version 
 */
public class TriangleVariate extends RandomVariateBase implements RandomVariate {

    protected double left;
    protected double right;
    protected double center;
    
    protected double centerMinusLeft;
    protected double rightMinusCenter;
    protected double centerMinusLeftOverRightMinusLeft;
    
    /** Creates new TriangleVariate */
    public TriangleVariate() {
    }

    /**
     * Returns the array of parameters as an Object[].
     * @return the array of parameters as an Object[].
     */
    public Object[] getParameters() {
        return new Object[] {new Double(left), new Double(right), new Double(center)};
    }
    
    /**
     * Sets the random variate's parameters.
     * Alternatively, the parameters could be set in the constructor or
     * in additional methods provided by the programmer.
     * @param params the array of parameters, wrapped in objects.
     */
    public void setParameters(Object[] params) {
        if (params == null) { throw new NullPointerException(); }
        if (params.length != 3) {
            throw new IllegalArgumentException("TriangleVariate needs 2 parameters: " + params.length);
        }
        if (params[0] instanceof Number && params[1] instanceof Number && params[2] instanceof Number) {
            setLeft(((Number) params[0]).doubleValue());
            setRight(((Number) params[1]).doubleValue());
            setCenter(((Number) params[2]).doubleValue());
            if (!validate()) {
                throw new IllegalArgumentException(
                "TriangleVariate needs a <= c <= b: (" +
                    left + ", " + right + ", " + center + ")"
                );
            }
        }
        else {
            throw new IllegalArgumentException(
            "TriangleVariate requires (Number, Number, Number): (" +
                params[0].getClass().getName() + ", " +
                params[1].getClass().getName() + ", " +
                params[2].getClass().getName() + ")"                
            );
        }
    }
    
    public void setLeft(double a) { left = a; }
    
    public double getLeft() { return left; }
    
    public void setRight(double b) { right = b; }
    
    public double getRight() { return right; }
    
    public void setCenter(double c) { center = c;  }
    
    public double getCenter() { return center; }
    
    protected boolean validate() {
        boolean valid = left <= center && center <= right;
        if (valid) {
            centerMinusLeft = center - left;
            rightMinusCenter = right - center;
            centerMinusLeftOverRightMinusLeft = (center - left) / (right - left);
        }
        return valid;
    }
    
    /**
     * Generate a random variate having this class's distribution.
     * @return The generated random variate
     */
    public double generate() {
        double u = rng.draw();
        double v = rng.draw();
        double w = rng.draw();
        
        if (u < centerMinusLeftOverRightMinusLeft) {
            return left + centerMinusLeft * Math.max(v, w);
        }
        else {
            return center + rightMinusCenter * Math.min(v, w);
        }
    }
    
    public String toString() {
        return "Triangle (" + left + ", " + right + ", " + center + ")";
    }
    
}
