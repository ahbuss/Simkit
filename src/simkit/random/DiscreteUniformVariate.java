package simkit.random;

public class DiscreteUniformVariate extends RandomVariateBase implements DiscreteRandomVariate {

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
        if (params[0] instanceof Number && params[1] instanceof Number) {
            minimum = ((Integer)params[0]).intValue();
            maximum = ((Integer)params[1]).intValue();
            range = maximum - minimum + 1;
        }
        else {
            throw new IllegalArgumentException("Parameters must be Integer");
        }
    }
    
    public Object[] getParameters() {
        return new Object[] { new Integer(minimum), new Integer(maximum) };
    }

    public double generate() {
        return (double) generateInt();
    }

    public String toString() {
        return "Discrete Uniform (" + minimum + ", " + maximum + ")";
    }

    public int generateInt() {
        return (int) Math.floor(minimum + range * rng.draw());
    }
    
    public void setMinimum(int min) {
            minimum = min;
            range = maximum - minimum;
    }

    public void setMaximum(int max) {
            maximum = max;
            range = maximum - minimum;
    }

    public int getMaximum() { return maximum; }
    public int getMinimum() { return minimum; }
}
