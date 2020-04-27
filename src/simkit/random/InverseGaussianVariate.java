package simkit.random;

/**
 * Generates random variates having the Inverse Gaussian distribution.
 *
 * @author Arnold Buss
 * 
 */
public class InverseGaussianVariate extends RandomVariateBase {

    /**
     * The scale factor.
*
     */
    private double lambda;

    /**
     * The mean.
*
     */
    private double mu;

    /**
     * The instance of the NormalVariate that is used to generate Inverse
     * Gaussian.
*
     */
    private final NormalVariate normal;

    /**
     * Creates a new InverseGaussian without setting the parameters. The
     * parameters must be set prior to generating.
*
     */
    public InverseGaussianVariate() {
        normal = new NormalVariate();
        normal.setParameters(0.0, 1.0);
    }

    /**
     * Generate a random variate having an Inverse Gaussian distribution
     */
    public double generate() {
        double n = normal.generate();
        double x1 = mu + mu * mu * n * n * 0.5 / lambda
                - 0.5 * mu / lambda
                * Math.sqrt(4.0 * mu * lambda * n * n
                        + mu * mu * n * n * n * n);
        if (rng.draw() <= mu / (mu + x1)) {
            return x1;
        } else {
            return mu * mu / x1;
        }
    }

    /**
     * Returns a two element array containing mu (the mean) and lambda (the
     * scale).
     *
     * @return the array of parameters as an Object[].
     */
    @Override
    public Object[] getParameters() {
        return new Object[]{mu, lambda};
    }

    /**
     * Sets \u03BC (the mean) and \u019B (the scale) to the values given.
     *
     * @param params A two element array containing me and lambda as Numbers
     * @throws IllegalArgumentException If the array does not contain exactly
     * two Numbers or if lambda is not positive.
     */
    @Override
    public void setParameters(Object... params) {
        if (params.length != 2) {
            throw new IllegalArgumentException("InverseGaussianVariate requires two parameters "
                    + params.length + " given");
        }
        if (params[0] instanceof Number && params[1] instanceof Number) {
            setMu(((Number) params[0]).doubleValue());
            setLambda(((Number) params[1]).doubleValue());
        } else {
            throw new IllegalArgumentException(
                    "Need two Number objects; (" + params[0].getClass().getName()
                    + ", " + params[1].getClass().getName() + ") given");
        }
    }

    /**
     * 
     * @param mu the mean (&mu;).
     */
    public void setMu(double mu) {
        this.mu = mu;
    }

    /**
     * @return \u03BC (the mean).
     */
    public double getMu() {
        return mu;
    }

    /**
     * 
     * @param lambda the scale ( &lambda;)
     * @throws IllegalArgumentException If lambda &le; 0.0.
     */
    public void setLambda(double lambda) {
        if (lambda > 0.0) {
            this.lambda = lambda;
        } else {
            throw new IllegalArgumentException("Lambda must be > 0.0: " + lambda);
        }
    }

    /**
     * @return \u03BB (the scale).
     */
    public double getLambda() {
        return lambda;
    }

    /**
     * Returns a String containing the name of this distribution and its
     * parameters.
*
     */
    @Override
    public String toString() {
        return String.format("Inverse Gaussian (%.3f, %.3f)", getMu(), getLambda() );
    }
}
