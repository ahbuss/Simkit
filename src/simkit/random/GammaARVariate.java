package simkit.random;

import static java.lang.Math.pow;
import java.util.logging.Logger;

/**
 * <p>
 * Instances of this class generate correlated Gamma random variates based on an
 * approach in Gaver and Lewis (1986). The correlation rho must be ≥ 0 and the
 * shape parameter k must be an integer. The generated sequence have a gamma
 * distribution with the given parameters (k, lambda), where k is the shape and
 * lambda is the scale. The lag-1 autocorrelation is rho.
 *
 * <p>
 * The recursion is X<sub>n+1</sub>=&rho; X<sub>n</sub> + &epsilon;<sub>n</sub>
 * where &epsilon;<sub>n</sub> is generated from a mixture of constant(0),
 * Exponential(&lambda;) and Gamma(k, &lambda;), k = 2,...,k. If k = 1, then the
 * marginals are Exponential(&lambda;).
 *
 * @author ahbuss
 */
public class GammaARVariate extends RandomVariateBase {

    private static final Logger LOGGER = Logger.getLogger(GammaARVariate.class.getName());

    private double rho;

    private double lambda;

    private int k;

    private RandomVariate mixtureVariate;

    private double currentValue;

    public GammaARVariate() {
    }

    @Override
    public double generate() {
        currentValue = getRho() * currentValue + mixtureVariate.generate();
        return currentValue;
    }

    /**
     * Parameters:<ul>
     * <li>params[0] is &lambda; (double)
     * <li>params[1] is &rho; (double)
     * <li>(optional) params[2] is k (defaults to 2)</ul>
     *
     * @param params Given parameters
     * @throws IllegalArgumentException if params.length ≠ 2 or 3
     * @throws IllegalArgumentException if an element of params is not Number
     */
    @Override
    public void setParameters(Object... params) {
        if (params.length == 2) {
            if (params[0] instanceof Number && params[1] instanceof Number) {
                setLambda(((Number) params[0]).doubleValue());
                setRho(((Number) params[1]).doubleValue());
                setK(2);
                setupMixture();
            } else {
                String message = String.format("parameters must be Number: (%s, %s)",
                        params[0].getClass().getSimpleName(),
                        params[1].getClass().getSimpleName());
                LOGGER.severe(message);
                throw new IllegalArgumentException(message);
            }
        } else if (params.length == 3) {
            if (params[0] instanceof Number && params[1] instanceof Number && params[2] instanceof Number) {
                setLambda(((Number) params[0]).doubleValue());
                setRho(((Number) params[1]).doubleValue());
                setK(((Number) params[2]).intValue());
                setupMixture();
            } else {
                String message = String.format("parameters must be Number: (%s, %s, %s)",
                        params[0].getClass().getSimpleName(),
                        params[1].getClass().getSimpleName(),
                        params[2].getClass().getSimpleName());

            }
        } else {
            String message = "GammaARVariate requires 2 or 3 parameters: " + params.length;
            LOGGER.severe(message);
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * <p>
     * Creates the MixedVariate. The mixing probabilities are given by
     * C(k,i)&rho;<sup>k-1</sup>(1-&rho;)<sup>i</sup> for i = 0,...,k.
     * <p>
     * The probability distributions are Gamma(i, &lambda;) for i = 0,...,k,
     * noting that Gamma(0,&lambda;) is Constant(0) and Gamma(1,&lambda;) is
     * Exponential(&lambda;).
     */
    private void setupMixture() {
        RandomVariate[] all = new RandomVariate[k + 1];
        all[0] = RandomVariateFactory.getInstance("Constant", 0.0);
        all[1] = RandomVariateFactory.getInstance("Exponential", getLambda());
        for (int alpha = 2; alpha <= k; ++alpha) {
            all[alpha] = RandomVariateFactory.getInstance("Gamma", alpha, getLambda());
        }

        double oneMinusRho = 1.0 - rho;
        double[] mixture = new double[all.length];
        for (int i = 0; i <= k; ++i) {
            mixture[i] = binomialCoefficient(k, i) * pow(rho, k - i) * pow(oneMinusRho, i);
        }

        this.mixtureVariate = RandomVariateFactory.getInstance("Mixed", mixture, all);
//        this.currentValue = all[2].generate();
    }

    @Override
    public Object[] getParameters() {
        return new Object[]{getLambda(), getRho(), getK()};
    }

    /**
     * @return the rho
     */
    public double getRho() {
        return rho;
    }

    /**
     * @param rho the rho to set
     * @throws IllegalArgumentException if &rho; ∉ [0,1]
     */
    public void setRho(double rho) {
        if (rho < 0.0) {
            String message = String.format("rho must be ∈ [0,1]: %,f", rho);
            LOGGER.severe(message);
            throw new IllegalArgumentException(message);
        }
        this.rho = rho;
    }

    /**
     * @return the lambda
     */
    public double getLambda() {
        return lambda;
    }

    /**
     * @param lambda the lambda to set
     * @throws IllegalArgumentException if &lambda; ≤ 0.0
     */
    public void setLambda(double lambda) {
        if (lambda <= 0.0) {
            String message = "Lambda must be > 0.0: " + lambda;
            LOGGER.severe(message);
            throw new IllegalArgumentException(message);
        }
        this.lambda = lambda;
    }

    public String toString() {
        return String.format("GammaAR (%.3f, %,.3f, %,d)", getLambda(), getRho(), getK());
    }

    /**
     * @return the currentValue
     */
    public double getCurrentValue() {
        return currentValue;
    }

    /**
     * @param currentValue the currentValue to set
     */
    public void setCurrentValue(double currentValue) {
        this.currentValue = currentValue;
    }

    /**
     * @return the k
     */
    public int getK() {
        return k;
    }

    /**
     * @param k the k to set
     * @throws IllegalArgumentException if k < 1
     */
    public void setK(int k) {
        if (k < 1) {
            String message = "k must be ≥ 1: " + k;
            LOGGER.severe(message);
            throw new IllegalArgumentException(message);
        }
        this.k = k;
    }

    /**
     *
     * @param n Given "n"
     * @param k Given "choose k"
     * @return Binomial coefficient C(n,k)
     * @throws IllegalArgumentException if n < 0 or k < 0
     */
    public static int binomialCoefficient(int n, int k) {
        if (k < 0 || n < 0) {
            String message = String.format("n and k must be ≥ 0: n = %,d, k = %,d", n, k);
            LOGGER.severe(message);
            throw new IllegalArgumentException(message);
        }
        if (k == 0 || k == n) {
            return 1;
        }
        return binomialCoefficient(n - 1, k - 1) + binomialCoefficient(n - 1, k);
    }

    /**
     * @return the mixtureVariate
     */
    public RandomVariate getMixtureVariate() {
        return mixtureVariate;
    }

}
