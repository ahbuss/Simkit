package simkit.random;

/**
 *
 * @author  ahbuss
 */
public class RightWedgeVariate implements RandomVariate {
    
    protected double left;
    protected double right;
    protected double smallHeight;
    
    protected double test;
    protected double range;
    
    protected RandomNumber rng;
    
    /** Creates a new instance of RightWedgeVariate */
    public RightWedgeVariate() {
        rng = RandomNumberFactory.getInstance();
    }
    
    public double generate() {
        if (rng.draw() < test) {
            return getLeft() + range * rng.draw();
        }
        else {
            return getLeft() + range * Math.sqrt(rng.draw());
        }
    }
    
    public Object[] getParameters() {
        return new Object[] { new Double(left), new Double(right), new Double(smallHeight) };
    }
    
    public RandomNumber getRandomNumber() { return rng; }
    
    public void setParameters(Object[] params) {
        if (params.length != 3) {
            throw new IllegalArgumentException("Need 3 parameters: " + params.length);
        }
        if (params[0] instanceof Number) {
            setLeft( ((Number) params[0]).doubleValue());
        }
        if (params[1] instanceof Number) {
            setRight( ((Number) params[1]).doubleValue());
        }
        if (params[2] instanceof Number) {
            setSmallHeight( ((Number) params[2]).doubleValue());
        }
        
        if (range < 0.0) {
            throw new IllegalArgumentException("Left must be smaller than right: " + getLeft() +
            " ? <= ? " + getRight());
        }
        
        if ( test > 1.0 ) {
            throw new IllegalArgumentException("Must have c * (b - a) <= 1.0: " + test);
        }
    }
    
    public void setRandomNumber(RandomNumber rng) { this.rng = rng; }
    
    public void setLeft(double x) {
        left = x;
        computeTest();
    }
    
    public double getLeft() { return left; }
    
    public void setRight(double x) {
        right = x;
        computeTest();
    }
    
    public double getRight() { return right; }
    
    public void setSmallHeight(double x) {
        smallHeight = x;
        computeTest();
    }
    
    public double getSmallHeight() { return smallHeight; }
    
    protected void computeTest() {
        test = getSmallHeight() * (getRight() - getLeft());
        range = getRight() - getLeft();
    }
    
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {}
        return null;
    }
    
    public String toString() {
        return "RightWedge (" + getLeft() + ", " + getRight() +
        ", " + getSmallHeight() + ")";
    }
}