package simkit.random;

/**
 * Generates random variates having a Pearson Type V distribution.  A 
 * Pearson Type V (&alpha;, &beta;) is distributed as 
 * 1.0 / &Gamma;(&alpha;, 1/&beta;).
 * 
 * <p>Note: Needs to be tested.
 *
 * @author  Arnold Buss
 * @version $Id$
 */
public class PearsonTypeVVariate extends RandomVariateBase {
    
    private double alpha;
    private double beta;

/**
* The instance of the GammaVariate used to generate the Pearson type V.
**/
    private RandomVariate gammaVariate;
    
/**
* Constructs a new PearsonTypeVVariate. Parameters must be set prior to use.
**/
    public PearsonTypeVVariate() {
    }
    
//Javadoc inherited
    public double generate() {
        return 1.0 / gammaVariate.generate();
    }
    
/**
* Returns the alpha and beta parameters.
* @return A 2 element array with &alpha; and &beta; as Doubles.
**/
    public Object[] getParameters() {
        return new Object[] { new Double(getAlpha()), new Double(getBeta()) };
    }
    
/**
* Sets &alpha; and &beta; for this variate.
* @param params A 2 element array with &alpha; and &beta; as Numbers.
* @throws IllegalArgumentException If the array does not have exactly 2 elements, if
* either element is not a Number, or if either element is not positive.
**/
    public void setParameters(Object... params) {
        if (params.length != 2) {
            throw new IllegalArgumentException("Need 2 parameters: " + params.length);
        }
        if (params[0] instanceof Number && params[1] instanceof Number) {
            setAlpha( ((Number) params[0]).doubleValue());
            setBeta( ((Number) params[1]).doubleValue());
                gammaVariate = RandomVariateFactory.getInstance("Gamma",
                    new Object[] { new Double(getAlpha()), new Double(1.0/getBeta()) },
                    rng);
        } else {
            throw new IllegalArgumentException("Both parameters must be Numbers.");
        }
    }
    
/**
* Returns a String containing the name of this variate with the value of alpha and beta.
**/
    public String toString() {
        return "Pearson Type V (" + getAlpha() + ", " + getBeta() + ")" ;
    }
    
/**
* Sets the value of alpha, but does not update the underlying gamma distribution.
* Use setParameters to set alpha.
**/
    public void setAlpha(double a) { 
        if (a > 0 ) {
            alpha = a;
        }
        else {
            throw new IllegalArgumentException("Alpha must be > 0: " + a);
        }
    }
    
/**
* Returns the value of alpha for this random variate.
**/
    public double getAlpha() { return alpha; }
    
/**
* Sets the value of beta, but does not update the underlying gamma distribution.
* Use setParameters to set beta.
**/
    public void setBeta(double b) { 
        if (b > 0 ) {
            beta = b;
        }
        else {
            throw new IllegalArgumentException("Beta must be > 0: " + b);
        }
    }
    
/**
* Returns the value of beta for this radom variate.
**/
    public double getBeta() { return beta; }

}
