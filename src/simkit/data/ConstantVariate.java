package simkit.data;
/**
 *   A "RandomVariate" class that is constant.
**/
public class ConstantVariate extends RandomVariateBase {

    private double value;

    public ConstantVariate() {
        this(new Object[] {new Double(0.0)});
    }

    public ConstantVariate(Object[] params) {
        super(params);
    }

    public void setParameters(Object[] params) {
        if (params[0] instanceof Number) {
            value = ((Number)params[0]).doubleValue();
        }
        else {
            throw new IllegalArgumentException("Bad parameter: " + params[0]);
        }
    }

    public ConstantVariate(Object[] params, RandomNumber rng) {
        this(params);
    }

    public double generate() {return value;}
}
