package simkit.random;
/**
 *   A "RandomVariate" class that is constant.
**/
public class ConstantVariate extends RandomVariateBase {

    private double value;

    public ConstantVariate() {
        this.setParameters(new Object[] { new Double(0.0)});
    }

    public void setParameters(Object[] params) {
        if (params[0] instanceof Number) {
            this.setValue(((Number)params[0]).doubleValue());
        }
        else {
            throw new IllegalArgumentException("Must be Number: " + params[0]);
        }
    }

    public Object[] getParameters() {
        return new Object[] { new Double(value) };
    }

    public void setValue(double value) { this.value = value;}

    public double getValue() { return value; }

    public double generate() {return value;}

    public String toString() { return "Constant (" + value + ")"; }
}
