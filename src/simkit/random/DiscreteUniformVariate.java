package simkit.random;

public class DiscreteUniformVariate extends RandomVariateBase {

    private int minimum;
    private int maximum;
    private int range;

    public DiscreteUniformVariate() {
    }

    public void setParameters(Object[] params) {
        if (params.length != 2) {
            throw new IllegalArgumentException("Two parameters needed, " +
                params.length + " given");
        }
        if (params[0] instanceof Number) {
            minimum = ((Number)params[0]).intValue();
        }
        if (params[1] instanceof Number) {
            maximum = ((Number)params[1]).intValue();
        }
        range = maximum - minimum + 1;
    }
    
    public Object[] getParameters() {
        return new Object[] { new Integer(minimum), new Integer(maximum) };
    }

    public double generate() {
        return (int) Math.floor(minimum + range * rng.draw());
    }

    public String toString() {
        return "Discrete Uniform (" + minimum + ", " + maximum + ")";
    }

}
