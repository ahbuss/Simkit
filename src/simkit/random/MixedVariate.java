/*
 * MixedVariate.java
 *
 * Created on August 31, 2001, 11:27 AM
 */

package simkit.random;

/**
 *
 * @author  Arnold Buss
 * @version
 */
public class MixedVariate extends simkit.random.RandomVariateBase {
    
    private DiscreteVariate mixing;
    private RandomVariate[] distributions;
    
    /** Creates new MixedVariate */
    public MixedVariate() {
    }
    
    /**
     * Generate a random variate having this class's distribution.
     */
    public double generate() {
        int index = (int) mixing.generate();
        return distributions[index].generate();
    }
    
    /**
     * Returns the array of parameters as an Object[].
     */
    public Object[] getParameters() {
        return new Object[] { mixing.getProbabilities(), deepCopy(distributions) };
    }
    
    /**
     * Sets the random variate's parameters.
     * Alternatively, the parameters could be set in the constructor or
     * in additional methods provided by the programmer.
     * @param params the array of parameters, wrapped in objects.
     */
    public void setParameters(Object[] params) {
        if (params.length != 2) {
            throw new IllegalArgumentException("Wrong params length");
        }
        if (params[0] instanceof double[] && params[1] instanceof RandomVariate[]) {
            double[] probs = (double[]) params[0];
            RandomVariate[] rv = (RandomVariate[]) params[1];
            if (probs.length != rv.length) {
                throw new IllegalArgumentException("Different # probabilites and distributions");
            }
            double[] values = new double[probs.length];
            for (int i = 0; i < values.length; i++) { values[i] = i; }
            mixing = (DiscreteVariate) RandomVariateFactory.getInstance("simkit.random.DiscreteVariate",
            new Object[] { values, probs } , rng);
            distributions = deepCopy(rv);
        }
    }
    
    protected RandomVariate[] deepCopy(RandomVariate[] live) {
        RandomVariate[] memorex = new RandomVariate[live.length];
        for (int i = 0; i < memorex.length; i++) {
            memorex[i] = RandomVariateFactory.getInstance(live[i]);
            memorex[i].setRandomNumber(rng);
        }
        return memorex;
    }
    
    public String toString() {
        java.text.DecimalFormat df = new java.text.DecimalFormat("0.000");
        StringBuffer buf = new StringBuffer("Mixed Distribution");
        double[] prob = mixing.getProbabilities();
        for (int i = 0; i < prob.length; i++ ) {
            buf.append(df.format(prob[i]));
            buf.append('\t');
            buf.append(distributions[i]);
            buf.append(System.getProperty("line.separator"));
        }
        return buf.toString();
    }
}
