/*
 * InverseGaussianVariate.java
 *
 * Created on August 30, 2001, 5:58 PM
 */

package simkit.random;

/**
 *
 * @author  Arnold Buss
 * @version 
 */
public class InverseGaussianVariate extends simkit.random.RandomVariateBase {

    private double lambda;
    private double mu;
    private NormalVariate normal;
    
    /** Creates new InverseGaussianVariate */
    public InverseGaussianVariate() {
        normal = new NormalVariate();
        normal.setRandomNumber(super.rng);
    }

    /**
     *
     * Generate a random variate having this class's distribution.
     *
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
     *
     * Returns the array of parameters as an Object[].
     *
 */
    public Object[] getParameters() {
        return new Object[] { new Double(mu), new Double(lambda) };
    }
    
    /**
     *
     * Sets the random variate's parameters.
     *
     * Alternatively, the parameters could be set in the constructor or
     *
     * in additional methods provided by the programmer.
     *
     * @param params the array of parameters, wrapped in objects.
     *
 */
    public void setParameters(Object[] params) {
        if (params.length != 2) {
            throw new IllegalArgumentException("InverseGaussianVariate requires two parameters " +
                params.length + " given");
        }
        if (params[0] instanceof Number && params[1] instanceof Number) {
            mu = ((Number) params[0]).doubleValue();
            lambda = ((Number) params[1]).doubleValue();
        }
        else {
            throw new IllegalArgumentException(
                "Need two Number objects; (" + params[0].getClass().getName() +
                ", " + params[1].getClass().getName() + ") given");
        }
    }
    
}
