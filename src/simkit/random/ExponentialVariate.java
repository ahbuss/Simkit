package simkit.random;

public class ExponentialVariate extends RandomVariateBase implements RandomVariate {

    private double mean;
    public final static double DEFAULT_MEAN = 1.0;

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
        double temp = ((Number) params[0]).doubleValue();
        if (temp > 0) {
            super.setParameters(params);
            this.mean = ((Number) getParameters()[0]).doubleValue();
        }
        else {
            throw new IllegalArgumentException("Exponential mean must be positive: " + mean);
        }
    }

    public void setMean(double mean) { this.setParameters(new Object[] { new Double(mean) }); }

    public double getMean() { return mean; }

    public String toString() { return "Exponential (" + mean + ")"; }
 
} 