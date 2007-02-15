package simkit.random;

/**
* Generates Bivariate Normal random variates with zero correlation.
* @version $Id$
**/
public class BivariateNormal implements RandomVector {

    private RandomVariate[] rv;

/**
* Not currently used.
**/
    private double correlation;

/**
* Creates a new BivariateNormal with the default RandomNumber and
* 2 standard Normal components with zero correlation.
* The distribution of the underlying normals can be set with setParameters.
* Correlation is not yet implemented.
**/
    public BivariateNormal() {
        rv = new RandomVariate[2];
        RandomNumber rng = RandomNumberFactory.getInstance();
        rv[0] = RandomVariateFactory.getInstance("simkit.random.Normal03",
            new Object[] { new Double(0.0), new Double(1.0) }, rng);
        rv[1] = RandomVariateFactory.getInstance("simkit.random.Normal03",
            new Object[] { new Double(0.0), new Double(1.0) }, rng);
    }

/**
* Sets the parameters of this BivariateNormal.
* @param params A 3 element array of Objects. The first Object is a 2 element double array
* containing the mean and standard deviation for the first Normal. The second Object
* is a similar double array for the second Normal. The third element is a Double which
* while required is currently ignored (correlation).
**/

    public void setParameters(Object... params) {
        boolean goodData = true;
        if (params.length != 3) {
            throw new IllegalArgumentException("Need 3 elements; " +
                params.length + " provided");
        }
        double[] par = (double[]) params[0]; 
        rv[0].setParameters(new Object[] { new Double(par[0]), new Double(par[1])} );
        par = (double[]) params[1];
        rv[1].setParameters(new Object[] { new Double(par[0]), new Double(par[1])} );
        correlation = ((Number) params[2]).doubleValue();
    }

/**
* Not yet implemented.
* @return An empty Object array.
*/
    public Object[] getParameters() {
        return new Object[] {};
    }
/**
* Sets the supporting RandomNumber for both NormalVariates to the argument.
**/
    public void setRandomNumber(RandomNumber rng) {
        for (int i = 0; i < 2; i++) {
            rv[i].setRandomNumber(rng);
        }
    }

/**
* Returns the supporting RandomNumber.
**/
    public RandomNumber getRandomNumber() { return rv[0].getRandomNumber(); }

/**
* Generate the next Uncorrelated Bivariate Normal.
* @return A 2 element array containing the x and y values generated.
**/
    public double[] generate() {
        double[] val = new double[rv.length];
        for (int i = 0; i < val.length; i++) {
            val[i] = rv[i].generate();
        }
        return val;
    }
    
    public void setCorrelation(double corr) {
        if ( Math.abs( corr ) <= 1.0 ) {
            correlation = corr;
        }
        else {
            throw new IllegalArgumentException("Correlation  must be between -1.0 and " +
                " 1.0: " + corr);
        }
    }
    
    public double getCorrelation() { return correlation; }
    
    public String toString() {
        return "Bivariate Normal (" + rv[0] + ", " +
            rv[1] + " " + getCorrelation() + ")";
            
    }
} 
