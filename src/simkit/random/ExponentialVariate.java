package simkit.random;

public class ExponentialVariate extends RandomVariateBase implements RandomVariate {
    
    private double mean;
    
    public ExponentialVariate() {
    }
    
    public double generate() {
        return - mean * Math.log(rng.draw());
    }
    
    public void setParameters(Object[] params) {
        if (params.length != 1) {
            throw new IllegalArgumentException("Should be only one parameter for Exponential: " +
            params.length + " passed.");
        }
        if (params[0] instanceof Number) {
            double temp = ((Number) params[0]).doubleValue();
            if (temp > 0) {
                this.mean = ((Number) getParameters()[0]).doubleValue();
            }
            else {
                throw new IllegalArgumentException("Exponential mean must be positive: " + mean);
            }
        }
        else {
            throw new IllegalArgumentException("Parameters must be a Number");
        }
    }
    
    public Object[] getParameters() { return new Object[] { new Double(mean) }; }
    
    public void setMean(double mean) { this.setParameters(new Object[] { new Double(mean) }); }
    
    public double getMean() { return mean; }
    
    public String toString() { return "Exponential (" + mean + ")"; }
    
}