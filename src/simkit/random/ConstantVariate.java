package simkit.random;

/**  
 * A "RandomVariate" class that is constant.
 * Will always generate the value of its parameter.
 *
 * @author Arnold Buss
 * @version $Id$
 */
public class ConstantVariate extends RandomVariateBase {

/**
* The constant value to always be generated.
**/
    private double value;

/**
* Creates a new ConstantVariate with a value of 0.0.
**/
    public ConstantVariate() {
        this.setParameters(0.0);
    }

/**
* Sets the value to generate. Note that the argument array can have length greater
* than 1; only the first value is considered in that case.
* @param params A 1 element array containing the desired constant value as a Number.
* @throws IllegalArgumentException If the first element of array is not a Number.
* @throws ArrayIndexOutOfBoundsException if params.length == 0
**/
    public void setParameters(Object... params) {
        if (params[0] instanceof Number) {
            this.setValue(((Number)params[0]).doubleValue());
        }
        else {
            throw new IllegalArgumentException("Must be Number: " + params[0]);
        }
    }

/**
* Returns a 1 element array containing the constant value.
**/
    public Object[] getParameters() {
        return new Object[] { value };
    }

    /**
     * 
     * @param value the constant value to be generated.
     */
    public void setValue(double value) { this.value = value;}

    /**
     * 
     * @return the constant value.
     */
    public double getValue() { return value; }

/**
* Always generates the constant value.
**/
    public double generate() {return value;}

/**
* Returns the name of this ConstantVariate and its value.
**/
    public String toString() { return "Constant (" + value + ")"; }
    
}
