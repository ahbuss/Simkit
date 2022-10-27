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

    protected double min;
    protected double max;

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
        return min + (max - min) * u1 / (u1 + u2);
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
        setMin(0.0);
        setMax(1.0);

        switch (params.length) {
            case 3:
                if (params[2] instanceof Number) {
                    if (((Number) params[2]).doubleValue() < getMin()) {
                        throw new IllegalArgumentException("min must be < max: " + min
                                + " " + max);
                    }
                    setMax(((Number) params[2]).doubleValue());
                    if (params[0] instanceof Number && params[1] instanceof Number) {
                        this.setAlpha(((Number) params[0]).doubleValue());
                        this.setBeta(((Number) params[1]).doubleValue());
                    } else {
                        throw new IllegalArgumentException("All Parameters must be of type Number");
                    }
                } else {
                    throw new IllegalArgumentException("All Parameters must be of type Number");
                }
                break;
            case 4:
                if (params[2] instanceof Number && params[3] instanceof Number) {
                    if (((Number) params[2]).doubleValue() >= ((Number) params[3]).doubleValue()) {
                        throw new IllegalArgumentException("min must be < max: "
                                + params[2] + " >= " + params[3]);
                    }
                    setMin(((Number) params[2]).doubleValue());
                    setMax(((Number) params[3]).doubleValue());
                } else {
                    throw new IllegalArgumentException("All Parameters must be of type Number");
                }
            case 2:
                if (params[0] instanceof Number && params[1] instanceof Number) {
                    this.setAlpha(((Number) params[0]).doubleValue());
                    this.setBeta(((Number) params[1]).doubleValue());
                } else {
                    throw new IllegalArgumentException("All Parameters must be of type Number");
                }
                break;
            default:
                throw new IllegalArgumentException(this.getClass().getName()
                        + " must have two, three, or four arguments: " + params.length
                        + " passed");
        }

    }

    /**
     * Returns an array containing the parameters alpha and beta as Objects.
     *
     * @return (&alpha;, &beta;)
     */
    @Override
    public Object[] getParameters() {
        return new Object[]{alpha, beta, min, max};
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
    public String toString() {
        return String.format("Beta (%.3f, %.3f, %.3f, %.3f)", getAlpha(), getBeta(),
                getMin(), getMax());
    }

    /**
     * @return the min
     */
    public double getMin() {
        return min;
    }

    /**
     * @param min the min to set
     */
    public void setMin(double min) {
        this.min = min;
    }

    /**
     * @return the max
     */
    public double getMax() {
        return max;
    }

    /**
     * @param max the max to set
     */
    public void setMax(double max) {
        this.max = max;
    }

}
