package simkit.random;

/** Generate from the Weibull distribution having pdf:
 * <BLOCKQUOTE>
 * <i>f(x) = &alpha;x<sup>&alpha;-1</sup>&beta;<sup>-&alpha;</sup>e<sup>-(x/&beta;)<sup>&alpha;</sup></sup>, x > 0</i>
 * </BLOCKQUOTE>
 */
public class WeibullVariate extends RandomVariateBase{
    private double alpha;
    private double alphaInverse;
    private double beta;
    
    public WeibullVariate() {
    }
    
    public double generate() {
        return beta * Math.pow(- Math.log(rng.draw()), alphaInverse);
    }
    
    /**
     * @param params (&alpha;, &beta;)
     */    
    public void setParameters(Object[] params) {
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
    public Object[] getParameters() {
        return new Object[] {new Double(alpha),  new Double(beta)};
    }
    
    public double getAlpha() {return alpha; }
    public double getBeta() { return beta; }
    
    public void setAlpha(double a) {
        if ( a > 0.0) {
            alpha = a;
            alphaInverse = 1.0 / alpha;
        }
        else {
            throw new IllegalArgumentException("Alpha parameter must be > 0.0");
        }
    }
    
    public void setBeta(double b) {
        if ( b > 0.0) {
            beta = b;
        }
        else {
            throw new IllegalArgumentException("Beta parameter must be > 0.0");
        }
    }
    
    
    public String toString() { return "Weibull (" + alpha + ", " +  beta+ ")"; }
}