package simkit.random;

/**
 * @author  Arnold Buss
 */
public class InverseGaussianVariate extends RandomVariateBase {

    private double lambda;
    private double mu;
    private NormalVariate normal;
    
    public InverseGaussianVariate() {
        normal = new NormalVariate();
        normal.setRandomNumber(super.rng);
    }

/**
 * Generate a random variate having an Inverse Gaussian distribution
 */
    public double generate() {
        double n = normal.generate();
        double x1 = mu + mu * mu * n * n * 0.5 / lambda -
            0.5 * mu / lambda * 
                Math.sqrt(4.0 * mu * lambda * n * n +
                    mu * mu * n * n * n * n);
        if (rng.draw() <= mu / (mu + x1) ) {
            return x1;
        }
        else {
            return mu * mu / x1;
        }
    }
    
/**
 * @return the array of parameters as an Object[].
 */
    public Object[] getParameters() {
        return new Object[] { new Double(mu), new Double(lambda) };
    }
    
/**
 * Sets the random variate's parameters.
 * @param params the array of parameters, wrapped in objects.
 */
    public void setParameters(Object[] params) {
        if (params.length != 2) {
            throw new IllegalArgumentException("InverseGaussianVariate requires two parameters " +
                params.length + " given");
        }
        if (params[0] instanceof Number && params[1] instanceof Number) {
            setMu(((Number) params[0]).doubleValue());
            setLambda(((Number) params[1]).doubleValue());
        }
        else {
            throw new IllegalArgumentException(
                "Need two Number objects; (" + params[0].getClass().getName() +
                ", " + params[1].getClass().getName() + ") given");
        }
    }
    
    public void setMu(double m) {
        mu = m;
    }
    
    public double getMu() { return mu; }
    
    public void setLambda(double l) {
        if (l > 0.0) { 
            lambda = l;
        }
        else {
            throw new IllegalArgumentException("Lambda must be > 0.0: " + l);
        }
    }
    
    public double getLambda() { return lambda; }
    
    public String toString() {
        return "Inverse Gaussian (" + getMu() + ", " + getLambda() + ")";
    }
    
}
