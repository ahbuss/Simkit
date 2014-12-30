package simkit.random;

/**
 * Generate from the Weibull distribution having pdf:
 * <BLOCKQUOTE>
 * <i>f(x) =
 * &alpha;x<sup>&alpha;-1</sup>&beta;<sup>-&alpha;</sup>e<sup>-(x/&beta;)<sup>&alpha;</sup></sup>,
 * x &gt; 0</i>
 * </BLOCKQUOTE>
 *
 * @version $Id$
 */
public class WeibullVariate extends RandomVariateBase {

    private double alpha;
    private double oneOverAlpha;
    private double beta;

    /**
     * Constructs a new WeibullVariate. Parameters must be set prior to use.
*
     */
    public WeibullVariate() {
    }

    @Override
    public double generate() {
        return beta * Math.pow(-Math.log(rng.draw()), oneOverAlpha);
    }

    /**
     * Sets the values of &alpha; and &beta;.
     *
     * @param params A two element array containing the values of alpha and beta
     * as Numbers.
     * @throws IllegalArgumentException If the array does not contain exactly 2
     * elements, if either element is not a Number, or if either element is not
     * positive.
*
     */
    @Override
    public void setParameters(Object... params) {
        if (params.length != 2) {
            throw new IllegalArgumentException("Must bave two parameters for Weibull, "
                    + params.length + " given");
        }
        if (params[0] instanceof Number && params[1] instanceof Number) {
            this.setAlpha(((Number) params[0]).doubleValue());
            this.setBeta(((Number) params[1]).doubleValue());
        } else {
            throw new IllegalArgumentException("For Weibull both parameters must "
                    + " be numbers: (" + params[0].getClass().getName() + ", "
                    + params[1].getClass().getName() + ") given");
        }
    }

    /**
     * @return a 2 element array containing alpha and beta as Numbers.
     */
    @Override
    public Object[] getParameters() {
        return new Object[]{getAlpha(), getBeta()};
    }

    /**
     * @return the current value of alpha.
     */
    public double getAlpha() {
        return alpha;
    }

    /**
     * @return the current value of beta.
     */
    public double getBeta() {
        return beta;
    }

    /**
     *
     * @param alpha the value of alpha.
     * @throws IllegalArgumentException If alpha is not positive.
*
     */
    public void setAlpha(double alpha) {
        if (alpha > 0.0) {
            this.alpha = alpha;
            this.oneOverAlpha = 1.0 / alpha;
        } else {
            throw new IllegalArgumentException("Alpha parameter must be > 0.0");
        }
    }

    /**
     * @param beta the value of beta.
     *
     * @throws IllegalArgumentException If beta is not positive.
     */
    public void setBeta(double beta) {
        if (beta > 0.0) {
            this.beta = beta;
        } else {
            throw new IllegalArgumentException("Beta parameter must be > 0.0");
        }
    }

    /**
     * @return a String containing the name of this Distribution and the values
     * of alpha and beta.
     */
    @Override
    public String toString() {
        return String.format("Weibull (%.3f, %.3f)", getAlpha(), getBeta());
    }
}
