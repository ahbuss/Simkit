package simkit.random;

import simkit.util.Math64;

/** Generate from the Weibull distribution having pdf:<br>
 * <i>f(x) = &alpha;x<sup>&alpha;-1</sup>&beta;<sup>-&alpha;</sup>e<sup>-(x/&beta;)<sup>&alpha;</sup></sup>, x &gt; 0</i>
 *
 * <P>Uses simkit.util.Math64.log() function for replicability on 64-bit
 * platforms.
 * @version $Id$
 */
public class Weibull_64Variate extends RandomVariateBase{
    private double alpha;
    private double alphaInverse;
    private double beta;
    
/**
* Constructs a new WeibullVariate. Parameters must be set prior to use.
**/
    public Weibull_64Variate() {
    }
    
//javadoc inherited
    public double generate() {
        return beta * Math.pow(- Math64.log(rng.draw()), alphaInverse);
    }
    
/**
* Sets the values of &alpha; and &beta;.
* @param params A two element array containing the values of alpha and beta as Numbers.
* @throws IllegalArgumentException If the array does not contain exactly 2 elements,
* if either element is not a Number, or if either element is not positive.
**/
    public void setParameters(Object... params) {
        if (params.length != 2) {
            throw new IllegalArgumentException("Must bave two parameters for Weibull, "
            + params.length + " given");
        }
        if (params[0] instanceof Number && params[1] instanceof Number) {
            this.setAlpha(((Number) params[0]).doubleValue());
            this.setBeta(((Number) params[1]).doubleValue());
        }
        else {
            throw new IllegalArgumentException("For Weibull both parameters must "+
            " be numbers: (" + params[0].getClass().getName() + ", " +
            params[1].getClass().getName() + ") given" );
        }
    }

/**
* Returns a 2 element array containing alpha and beta as Numbers.
**/
    public Object[] getParameters() {
        return new Object[] {new Double(alpha),  new Double(beta)};
    }
    
    /**
     * 
     * @return the current value of alpha.
     */
    public double getAlpha() {return this.alpha; }
    
    /**
     * 
     * @return the current value of beta
     */
    public double getBeta() { return this.beta; }
    
    /**
     * Also sets alphaInverse to 1/alpha
     * @param alpha the value of alpha.
     * @throws IllegalArgumentException If alpha &le; 0.0.
     */
    public void setAlpha(double alpha) {
        if ( alpha > 0.0) {
            this.alpha = alpha;
            this.alphaInverse = 1.0 / alpha;
        }
        else {
            throw new IllegalArgumentException("Alpha parameter must be > 0.0");
        }
    }
    
    /**
     * 
     * @param b the value of beta.
     * @throws IllegalArgumentException If beta &le; 0.0.
     */
    public void setBeta(double b) {
        if ( b > 0.0) {
            this.beta = b;
        }
        else {
            throw new IllegalArgumentException("Beta parameter must be > 0.0");
        }
    }
    
/**
* Returns a String containing the name of this Distribution and the values of
* alpha and beta.
**/    
    public String toString() { return "Weibull_64 (" + alpha + ", " +  beta+ ")"; }
}
