package simkit.random;

/** Generates Beta(alpha, beta) random variates.
 *  mean =  alpha/(alpha + beta)
 *
 * It uses the ratio of two Gamma variates to generate a Beta -
 * perhaps not the most efficient way, but gets the job done.
 * 
 * <p>The pdf is f(x) = x<sup>&alpha;-1</sup>(1-x)<sup>&beta;-1</sup>, 
 *  0&le;x&le;1 <br>
 * Parameters:
 * <ul><li><code>alpha</code>: &alpha; in pdf</li>
 * <li><code>beta</code>: &beta; in pdf</li></ul>
 * Note: This RandomVariate should be instantiated by using 
 * <code>RandomVariateFactory</code>.
 *
 * @author Arnold Buss
 */
public class BetaVariate extends RandomVariateBase {

    private double alpha;
    private double beta;

    private RandomVariate gammaVariate1;
    private RandomVariate gammaVariate2;

/**
* Creates a new BetaVariate. Prior to use the parameters must be set
* using setParameters or setAlpha and setBeta. 
**/
    public BetaVariate() {
    }

/**
* Generates the next value of this variate.
**/
    public double generate() {
        double u1 = gammaVariate1.generate();
        double u2 = gammaVariate2.generate();
        return u1 / (u1 + u2);
    }

    /**
     * Sets the parameters alpha and beta to the contents of the array.
     * Both parameters must be greater than zero.
     * @param params A two element array containing alpha, beta as Numbers
     * @throws IllegalArgumentException If the parameter array does not
     * contain exactly two parameters, or either of the parameters is not a Number
     * greater than zero.
     */    
    public void setParameters(Object[] params) {
        if (params.length != 2) {
            throw new IllegalArgumentException(this.getClass().getName() + " must have two arguments: " + params.length +
                                             " passed");
        }
        else if (params[0] instanceof Number && params[1] instanceof Number) {
            this.setAlpha(((Number) params[0]).doubleValue());
            this.setBeta(((Number) params[1]).doubleValue());
            this.setGammas(rng);
        }
        else {
            throw new IllegalArgumentException("Parameters both must be of type Number");
        }
    }
    
/**
* Returns an array containing the parameters alpha and beta as Objects.
**/
    public Object[] getParameters() { return new Object[] { new Double(alpha), new Double(beta) }; }

/**
* Creates the two instances of GammaVariate used to generate this BetaVariate.
* @param rng The supporting instance of RandomNumber for both GammVariates.
**/
    private void setGammas(RandomNumber rng) {
        gammaVariate1 = RandomVariateFactory.getInstance("simkit.random.GammaVariate", new Object[] {new Double(alpha), new Double(1.0)}, rng);
        gammaVariate2 = RandomVariateFactory.getInstance("simkit.random.GammaVariate", new Object[] {new Double(beta), new Double(1.0)}, rng);
    }

/**
* Returns the value of the alpha parameter.
**/
    public double getAlpha() {return alpha; }

/**
* Returns the value of the beta parameter.
**/
    public double getBeta() { return beta; }
    
/**
* Sets the alpha parameter.
* @throws IllegalArgumentException If alpha is not greater than 0.0.
**/
    public void setAlpha(double a) {
        if ( a > 0.0) {
            alpha = a;
        }
        else {
            throw new IllegalArgumentException("Alpha parameter must be > 0.0");
        }
    }

/**
* Sets the beta parameter.
* @throws IllegalArgumentException If beta is not greater than 0.0.
**/
    public void setBeta(double b) {
        if ( b > 0.0) {
            beta = b;
        }
        else {
            throw new IllegalArgumentException("Beta parameter must be > 0.0");
        }
    }

/**
* Sets the instance of RandomNumber that this BetaVariate is based on.
**/
    public void setRandomNumber(RandomNumber rng) {
        super.setRandomNumber(rng);
        setGammas(rng);
    }
    
/**
* Returns a String containing the name of the distribution of this Beta and
* its parameters.
**/
    public String toString() { return "Beta (" + getAlpha() + ", " + getBeta() + ")"; }

} 
