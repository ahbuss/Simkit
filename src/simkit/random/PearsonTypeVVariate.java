package simkit.random;

/**
 * Generates random variates having a Pearson Type V distribution. A Pearson
 * Type V (&alpha;, &beta;) is distributed as 1.0 / &Gamma;(&alpha;, 1/&beta;).
 *
 * <p>
 * Note: Needs to be tested.
 *
 * @author Arnold Buss
 * @version $Id$
 */
public class PearsonTypeVVariate implements RandomVariate  {

    private double alpha;
    private double beta;

    /**
     * The instance of the GammaVariate used to generate the Pearson type V.
     *
     */
    private RandomVariate gammaVariate;

    /**
     * Constructs a new PearsonTypeVVariate. Parameters must be set prior to
     * use.
     *
     */
    public PearsonTypeVVariate() {
        this.alpha = 1.0;
        this.beta = 1.0;
        gammaVariate = 
                RandomVariateFactory.getInstance("Gamma",
                getAlpha(), 1.0 / getBeta());
    }

    @Override
    public double generate() {
        return 1.0 / gammaVariate.generate();
    }

    /**
     * Returns the alpha and beta parameters.
     *
     * @return A 2 element array with &alpha; and &beta; as Doubles.
     */
    @Override
    public Object[] getParameters() {
        return new Object[]{getAlpha(), getBeta()};
    }

    /**
     * Sets &alpha; and &beta; for this variate.
     *
     * @param params A 2 element array with &alpha; and &beta; as Numbers.
     * @throws IllegalArgumentException If the array does not have exactly 2
     * elements, if either element is not a Number, or if either element is not
     * positive.
     *
     */
    @Override
    public void setParameters(Object... params) {
        if (params.length != 2) {
            throw new IllegalArgumentException("Need 2 parameters: " + params.length);
        }
        if (params[0] instanceof Number && params[1] instanceof Number) {
            setAlpha(((Number) params[0]).doubleValue());
            setBeta(((Number) params[1]).doubleValue());

        } else {
            throw new IllegalArgumentException("Both parameters must be Numbers.");
        }
    }

    /**
     * @return a String containing the name of this variate with the value of
     * alpha and beta.
     */
    @Override
    public String toString() {
        return String.format("Pearson Type V (%.3f, %.3f)", getAlpha(), getBeta());
    }

    /**
     * Sets the value of alpha, but does not update the underlying gamma
     * distribution. Use setParameters to update underlying gamma distribution
     *
     * @param alpha value of alpha
     * @throws IllegalArgumentException if alpha &le; 0.0
     */
    public void setAlpha(double alpha) {
        if (alpha > 0) {
            this.alpha = alpha;
        } else {
            throw new IllegalArgumentException("Alpha must be > 0: " + alpha);
        }
    }

    /**
     * @return the value of alpha for this random variate.
     */
    public double getAlpha() {
        return alpha;
    }

    /**
     * Sets the value of beta, updates the underlying gamma distribution. Use
     * setParameters to set beta.
     *
     * @param beta New value of beta
     */
    public void setBeta(double beta) {
        if (beta > 0) {
            this.beta = beta;
            gammaVariate.setParameters(getAlpha(), getBeta());
        } else {
            throw new IllegalArgumentException("Beta must be > 0: " + beta);
        }
    }

    /**
     * @return the value of beta for this radom variate.
     */
    public double getBeta() {
        return beta;
    }

    @Override
    public void setRandomNumber(RandomNumber rng) {
        gammaVariate.setRandomNumber(rng);
    }

    @Override
    public RandomNumber getRandomNumber() {
        return gammaVariate.getRandomNumber();
    }
    
}
