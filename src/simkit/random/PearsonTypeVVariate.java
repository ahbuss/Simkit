package simkit.random;

/**
 * Generates random variates having a Pearson Type V distribution.  A 
 * Pearson Type V (&alpha;, &beta;) is distributed as 
 * 1.0 / &Gamma;(&alpha;, &beta;).
 * 
 * <p>Note: Needs to be tested.
 *
 * @author  Arnold Buss
 */
public class PearsonTypeVVariate extends RandomVariateBase {
    
    private double alpha;
    private double beta;
    private RandomVariate gammaVariate;
    
    public PearsonTypeVVariate() {
    }
    
    public double generate() {
        return 1.0 / gammaVariate.generate();
    }
    
    public Object[] getParameters() {
        return new Object[] { new Double(getAlpha()), new Double(getBeta()) };
    }
    
    public void setParameters(Object[] params) {
        if (params.length != 2) {
            throw new IllegalArgumentException("Need 2 parameters: " + params.length);
        }
        if (params[0] instanceof Number && params[1] instanceof Number) {
            setAlpha( ((Number) params[0]).doubleValue());
            setBeta( ((Number) params[1]).doubleValue());
                gammaVariate = RandomVariateFactory.getInstance("Gamma",
                    new Object[] { new Double(getAlpha()), new Double(1.0/getBeta()) },
                    rng);
        }
    }
    
    public String toString() {
        return "Pearson Type V (" + getAlpha() + ", " + getBeta() + ")" ;
    }
    
    public void setAlpha(double a) { 
        if (a > 0 ) {
            alpha = a;
        }
        else {
            throw new IllegalArgumentException("Alpha must be > 0: " + a);
        }
    }
    
    public double getAlpha() { return alpha; }
    
    public void setBeta(double b) { 
        if (b > 0 ) {
            beta = b;
        }
        else {
            throw new IllegalArgumentException("Beta must be > 0: " + b);
        }
    }
    
    public double getBeta() { return beta; }
    
}
