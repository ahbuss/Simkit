package simkit.random;

/**
 * Generates Beta(alpha, beta) random variates. mean = alpha/(alpha + beta)
 *
 * It uses the ratio of two Gamma variates to generate a Beta - perhaps not the
 * most efficient way, but gets the job done.
 *
 * <p>
 * The pdf is f(x) = x<sup>&alpha;-1</sup>(1-x)<sup>&beta;-1</sup>, 0&le;x&le;1
 * <br>
 * Parameters:
 * <ul><li><code>alpha</code>: &alpha; in pdf</li>
 * <li><code>beta</code>: &beta; in pdf</li></ul>
 * Note: This RandomVariate should be instantiated with 
 * <code>RandomVariateFactory.getInstance(...)</code>
 *
 * @author Arnold Buss
 * 
 */
public class BetaVariate extends RandomVariateBase {

    private double alpha;
    private double beta;

    private RandomVariate gammaVariate1;
    private RandomVariate gammaVariate2;

    /**
     * Creates a new BetaVariate. Prior to use the parameters must be set using
     * setParameters or setAlpha() and setBeta().
     */
    public BetaVariate() {
        gammaVariate1 = RandomVariateFactory.getInstance("simkit.random.GammaVariate", 1.0, 1.0);
        gammaVariate2 = RandomVariateFactory.getInstance("simkit.random.GammaVariate", 1.0, 1.0);
    }

    /**
     * Generates the next value of this variate.
     *
     */
    @Override
    public double generate() {
        double u1 = gammaVariate1.generate();
        double u2 = gammaVariate2.generate();
        return u1 / (u1 + u2);
    }

    /**
     * Sets the parameters alpha and beta to the contents of the array. Both
     * parameters must be greater than zero.
     *
     * @param params A two element array containing alpha, beta as Numbers
     * @throws IllegalArgumentException If the parameter array does not contain
     * exactly two parameters, or either of the parameters is not a Number
     * greater than zero.
     */
    @Override
    public void setParameters(Object... params) {
        if (params.length != 2) {
            throw new IllegalArgumentException(this.getClass().getName() + " must have two arguments: " + params.length
                    + " passed");
        } else if (params[0] instanceof Number && params[1] instanceof Number) {
            this.setAlpha(((Number) params[0]).doubleValue());
            this.setBeta(((Number) params[1]).doubleValue());
        } else {
            throw new IllegalArgumentException("Parameters both must be of type Number");
        }
    }

    /**
     * Returns an array containing the parameters alpha and beta as Objects.
     *
     * @return (&alpha;, &beta;)
     */
    @Override
    public Object[] getParameters() {
        return new Object[]{alpha, beta};
    }

    /**
     * Creates the two instances of GammaVariate used to generate this
     * BetaVariate.
     *     
*
     */
    private void setGammas() {
        gammaVariate1 = RandomVariateFactory.getInstance("simkit.random.GammaVariate", getAlpha(), 1.0);
        gammaVariate2 = RandomVariateFactory.getInstance("simkit.random.GammaVariate", getBeta(), 1.0);
    }

    /**
     * @return &alpha;
     */
    public double getAlpha() {
        return alpha;
    }

    /**
     * @return &beta;
     */
    public double getBeta() {
        return beta;
    }

    /**
     * 
     * @param alpha &alpha; parameter
     * @throws IllegalArgumentException If alpha is not greater than 0.0.
     */
    public void setAlpha(double alpha) {
        if (alpha > 0.0) {
            this.alpha = alpha;
            gammaVariate1.setParameters(getAlpha(), 1.0);
        } else {
            throw new IllegalArgumentException("Alpha parameter must be > 0.0");
        }
    }

    /**
     * 
     * @param beta &beta; parameter
     * @throws IllegalArgumentException If beta is not greater than 0.0.
     */
    public void setBeta(double beta) {
        if (beta > 0.0) {
            this.beta = beta;
            gammaVariate2.setParameters(getBeta(), 1.0);
        } else {
            throw new IllegalArgumentException("Beta parameter must be > 0.0");
        }
    }

    @Override
    public void setRandomNumber(RandomNumber rng) {
        super.setRandomNumber(rng);
    }

    @Override
    public String toString() {
        return String.format("Beta (%.3f, %.3f)", getAlpha(),getBeta());
    }

}
