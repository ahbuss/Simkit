package simkit.random;

public class UniformVariate extends RandomVariateBase {
    
    private static double DEFAULT_LOW;
    private static double DEFAULT_HIGH;
    
    private double low;
    private double high;
    private double range;
    
    static {
        DEFAULT_LOW = 0.0;
        DEFAULT_HIGH = 1.0;
    }
    
    public UniformVariate() {
    }
    
    public void setParameters(Object[] params) {
        double newLow = ((Number)params[0]).doubleValue();
        double newHigh = ((Number)params[1]).doubleValue();
        
        if (newLow > newHigh) {
            throw new IllegalArgumentException("Uniform parameters must have low < high: (" +
            newLow + ", " + newHigh + ")");
        }
        this.low = newLow;
        this.high = newHigh;
        range = high - low;
    }
    
    public Object[] getParameters() {
        return new Object[] {new Double(low), new Double(high)};
    }
    
    public double generate() {
        return low + range * rng.draw();
    }
    
    public void setMinimum(double min) {
        low = min;
        range = high - low;
    }
    
    public double getMinimum() { return low; }
    
    public void setMaximum(double max) {
        high = max;
        range = high - low;
    }
    
    public double getLow() { return low; }
    public double getHigh() { return high; }
    
    public String toString() { return "Uniform (" + low + ", " + high + ")"; }
}
