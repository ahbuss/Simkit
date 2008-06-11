package simkit.random;

/**
 * Generates a right wedge random variate.
 * <p>The shape of the wedge is determined by 3 parameters. The first is the left side.
 * The second is the right side. The third determines the relative heights of the two sides.
 * <ul>
 * <li>If the height is 0 the wedge is a triangle distribution with c = b (the right side).</li>
 * <li>If the height equals the difference between the right and left the wedge is 
 * Uniform(left,right)</li>
 * <li>For intermediate values the height of the left side is (right-left)*height*center height.
 * </li></ul>Note: (right-left)*height cannot be greater than one.
 * 
 * @author  ahbuss
 * @version $Id: RightWedgeVariate.java 1051 2008-02-27 00:14:47Z ahbuss $
 */
public class RightWedgeVariate implements RandomVariate {
    
    protected double left;
    protected double right;
    protected double smallHeight;
    
    protected double test;
    protected double range;
    
    protected RandomNumber rng;
    
    /** 
      * Creates a new instance of RightWedgeVariate; the parameters must be set prior
      * to use.
    */
    public RightWedgeVariate() {
        setRandomNumber(RandomVariateFactory.getDefaultRandomNumber());
    }

//javadoc inherited    
    public double generate() {
        if (rng.draw() < test) {
            return getLeft() + range * rng.draw();
        }
        else {
            return getLeft() + range * Math.sqrt(rng.draw());
        }
    }
    
/**
* Returns a 3 element array containing the left (minimum), right (maximum), and
* the relative size of the left side.
**/
    public Object[] getParameters() {
        return new Object[] { new Double(left), new Double(right), new Double(smallHeight) };
    }
    
/**
* Returns the instance of the supporting RandomNumber.
**/
    public RandomNumber getRandomNumber() { return rng; }
    
/**
* Sets the left, right, and height parameters.
* @param params A 3 element array containing the left, right, and relative height as Numbers.
* @throws IllegalArgumentException If the array does not contain exactly 3 elements, any of the
* elements are not Numbers, the left is not smaller than the right, or the product of
* the height and the difference between right and left is greater than one.
**/
    public void setParameters(Object... params) {
        if (params.length != 3) {
            throw new IllegalArgumentException("Need 3 parameters: " + params.length);
        }
        if (params[0] instanceof Number) {
            setLeft( ((Number) params[0]).doubleValue());
        } else {
            throw new IllegalArgumentException("The left must be a Number. Supplied: " +
                                                params[0].getClass().getName());
        }
        if (params[1] instanceof Number) {
            setRight( ((Number) params[1]).doubleValue());
        } else {
            throw new IllegalArgumentException("The right must be a Number. Supplied: " +
                                                params[1].getClass().getName());
        }
        if (params[2] instanceof Number) {
            setSmallHeight( ((Number) params[2]).doubleValue());
        } else {
            throw new IllegalArgumentException("The height must be a Number. Supplied: " +
                                                params[2].getClass().getName());
        }
        
        if (range < 0.0) {
            throw new IllegalArgumentException("Left must be smaller than right: " + getLeft() +
            " ? <= ? " + getRight());
        }
        
        if ( test > 1.0 ) {
            throw new IllegalArgumentException("Must have c * (b - a) <= 1.0: " + test);
        }
    }
    
/**
* Sets the instance of the supporting RandomNumber.
**/
    public void setRandomNumber(RandomNumber rng) { this.rng = rng; }
    
/**
* Sets the value of the left side. Does no error checking.
**/
    public void setLeft(double x) {
        left = x;
        computeTest();
    }
    
/**
* Returns the current value of the left side.
**/
    public double getLeft() { return left; }
    
/**
* Sets the value of the right side. Does no error checking.
**/
    public void setRight(double x) {
        right = x;
        computeTest();
    }
    
/**
* Returns the current value of the right side.
**/
    public double getRight() { return right; }
    
/**
* Sets the value of the height. Does no error checking.
**/
    public void setSmallHeight(double x) {
        smallHeight = x;
        computeTest();
    }
    
/**
* Returns the current value of the height.
**/
    public double getSmallHeight() { return smallHeight; }
    
/**
* Precomputes the test and range values.
**/
    protected void computeTest() {
        test = getSmallHeight() * (getRight() - getLeft());
        range = getRight() - getLeft();
    }
    
/**
* Returns a String containing the name of this variate with the left, right, height.
**/
    public String toString() {
        return "RightWedge (" + getLeft() + ", " + getRight() +
        ", " + getSmallHeight() + ")";
    }
}
