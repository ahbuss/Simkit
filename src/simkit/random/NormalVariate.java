package simkit.random;

public class NormalVariate extends RandomVariateBase {
    
    private double savedValue;
    private boolean hasSavedValue;
    private double mean;
    private double sigma;
    
    public NormalVariate() {
    }
    
    public void setParameters(Object[] params) {
        if (params.length != 2) {
            throw new IllegalArgumentException("Need (mean, std. dev.), received " +
            params.length + " parameters");
        }
        else {
            setMean( ((Number) params[0]).doubleValue());
            setStandardDeviation(((Number) params[1]).doubleValue());
        }
    }
    
    public Object[] getParameters() { return new Object[] { new Double(mean), new Double(sigma) }; }
    
    public double generate() {
        double value = Double.NaN;
        if (hasSavedValue) {
            value = savedValue;
            hasSavedValue = false;
        }
        else {
            double w;
            double v1;
            double v2;
            do {
                v1 = 2.0 * rng.draw() - 1.0;
                v2 = 2.0 * rng.draw() - 1.0;
                w = v1 * v1 + v2 * v2;
            } while (w > 1.0);
            double y = Math.sqrt(-2.0 * Math.log(w) / w);
            value = v1 * y;
            savedValue = v2 * y;
            hasSavedValue = true;
        }
        return value * sigma + mean;
    }
    
    public void setMean(double mean) { this.mean = mean; }
    
    public void setStandardDeviation(double std) {
        if (std >= 0.0) {
            sigma = std;
        }
        else {
            throw new IllegalArgumentException("Standard Deviation must be > 0.0");
        }
    }
    
    public void setVariance(double var) {
        if (var >= 0.0) {
            sigma = Math.sqrt(var);
        }
        else {
            throw new IllegalArgumentException("Variance must be > 0.0");
        }
    }
    
    public double getMean() { return mean; }
    
    public double getStandardDeviation() { return sigma; }
    
    public double getVariance() { return sigma * sigma; }
    
    public String toString() { return "Normal (" + mean + ", " + sigma + ")"; }
    
}

