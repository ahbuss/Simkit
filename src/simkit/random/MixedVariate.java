/*
 * MixedVariate.java
 *
 * Created on August 31, 2001, 11:27 AM
 */

package simkit.random;

/**
 * @author Arnold Buss
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
    
    /** Returns the array of parameters as an Object[].
     * @return (double[], RandomVariate[]) - (mixing probabilities, distributions)
     */
    public Object[] getParameters() {
        return new Object[] { mixing.getProbabilities(), deepCopy(distributions) };
    }
    
    /** The first element is a double[] that are the
     * mixing probabilities; the second is a RandomVariate[]
     * from which the values will be generated.
     * @param params (double[], RandomVariate[])
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
        buf.append(System.getProperty("line.separator"));
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
