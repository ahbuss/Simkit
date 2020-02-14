/*
 * MixedVariate.java
 *
 * Created on August 31, 2001, 11:27 AM
 */

package simkit.random;

/**
 * Generates random number from a mix of RandomVariates. The parameters
 * specify the RandomVariates to be mixed and the probability of choosing each.
 * Internally, a DiscreteVariate is created that does the actual mixing.
 * @author Arnold Buss
 * @version $Id$
 */
public class MixedVariate extends simkit.random.RandomVariateBase {
    
    /**
     * A DiscreteVariate used to choose a RandomVariate with the desired
     * probability.
     **/
    private DiscreteVariate mixing;
    
    /**
     * The RandomVariates to mix.
     **/
    private RandomVariate[] distributions;
    
    /**
     * Creates new MixedVariate with no RandomVariates. The probabilities and
     * RandomVariates must be set with setParameters prior to generating.
     */
    public MixedVariate() {
    }
    
    /**
     * The "probabilities" can in fact be frequencies.  The DiscreteVariate
     * instance that is created to actually do the mixing will
     * normalize frequencies to probabilities.
     * @param mixProbs Mixing probabilities
     */    
    public void setMixingProbs(double[] mixProbs) {
        double[] values = new double[mixProbs.length];
        for (int i = 0; i < values.length; i++) {
            values[i] = i;
        }
        mixing = (DiscreteVariate) RandomVariateFactory.getInstance(
                "simkit.random.DiscreteVariate", rng, values, mixProbs );
    }
    
    /**
     *
     * @return Mixing probabilities
     */    
    public double[] getMixingProbs() {
        return (double[]) mixing.getProbabilities().clone();
    }
    
    /**
     *
     * @param dist RandomVariate[] array to be mixed
     */    
    public void setDistributions(RandomVariate[] dist) {
        distributions = deepCopy(dist);
    }
    
    /**
     *
     * @return RandomVariate[] array that is mixed
     */    
    public RandomVariate[] getDistributions() {
        return (RandomVariate[]) distributions.clone();
    }
    
    /**
     * Generate a random variate having this class's distribution.
     */
    @Override
    public double generate() {
        int index = (int) mixing.generate();
        return distributions[index].generate();
    }
    
    /**
     * Returns a two element array that contains the mixing DiscreteVariate
     * and a RandomVariate array containing the distributions to mix.
     * @return (double[], RandomVariate[]) - (mixing probabilities, distributions)
     */
    @Override
    public Object[] getParameters() {
        return new Object[] {
            getMixingProbs(), getDistributions()
        };
    }
    
    /**
     * Sets the probabilities and distributions that define this RandomVariate.
     * The first element is a double[] that are the
     * mixing probabilities; the second is a RandomVariate[]
     * from which the values will be generated.
     * @param params A two element array. The first element is a double array
     * containing the mixing probability for each RandomVariate. The second
     * element is a RandomVariate array containing the RandomVariates to mix.
     * @throws IllegalArgumentException If the given array does not contain 2 elements,
     * or the first element is not a double[], or the second element is not
     * a RandomVariate[], or the two arrays are not the same length.
     */
    public void setParameters(Object... params) {
        if (params.length != 2) {
            throw new IllegalArgumentException("Need 2 arguments: " + params.length);
        }
        if (!(params[0] instanceof double[] )) {
            throw new IllegalArgumentException("Need double[] as first argument: " + 
                params[0].getClass().getName());
        }
        if (!(params[1] instanceof RandomVariate[])) {
            throw new IllegalArgumentException("Need RandomVariate[] as second argument: " + 
                params[1].getClass().getName());
        }
        double[] probs = (double[]) params[0];
        RandomVariate[] rv = (RandomVariate[]) params[1];
        if (probs.length != rv.length) {
            throw new IllegalArgumentException("Different # probabilites and distributions: " +
                probs.length + " <-> " + rv.length);
        }

        setMixingProbs(probs);
        setDistributions(rv);
    }
    
    /**
     * 
     * @param live array of RandomVariates
     * @return a deep copy of the given array of RandomVariates.
     */
    protected RandomVariate[] deepCopy(RandomVariate[] live) {
        RandomVariate[] memorex = new RandomVariate[live.length];
        for (int i = 0; i < memorex.length; i++) {
            memorex[i] = RandomVariateFactory.getInstance(live[i]);
            memorex[i].setRandomNumber(rng);
        }
        return memorex;
    }
    
    /**
     * Returns a String containing list of the mixing probabilities and distributions.
     **/
    public String toString() {
        java.text.DecimalFormat df = new java.text.DecimalFormat("0.000");
        StringBuilder buf = new StringBuilder("Mixed Distribution");
        buf.append(System.getProperty("line.separator"));
        double[] prob = mixing.getProbabilities();
        for (int i = 0; i < prob.length; i++ ) {
            buf.append(df.format(prob[i]));
            buf.append('\t');
            buf.append(distributions[i]);
            if (i < prob.length - 1) { buf.append(System.getProperty("line.separator")); }
        }
        return buf.toString();
    }
}
