package simkit.random;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Generates the convolution (sum) of a number of RandomVariates. An array of
 * instances of RandomVariates are supplied as the parameter. Each RandomVariate
 * instance is passed the same RandomNumber instance, to avoid seed management
 * problems.
 *
 * @author Arnold Buss
 *
 */
public class ConvolutionVariate extends RandomVariateBase {

    public static final String _VERSION_ = "$Id$";

    public static final Logger log = Logger.getLogger("simkit.random");

    /**
     * The array of RandomVariates that are summed.
     *
     */
    private RandomVariate[] rv;

    /**
     * Creates a new instance of ConvolutionVariate. Generate will return 0.0
     * until the parameter is set.
     */
    public ConvolutionVariate() {
        rv = new RandomVariate[0];
    }

    /**
     * Generates the next value, which is the sum of the values of the
     * underlying RandomVariate instances.
     *
     * @return sum of a draw from each RandomVariate instance
     */
    @Override
    public double generate() {
        double value = 0.0;
        for (int i = 0; i < rv.length; ++i) {
            value += rv[i].generate();
        }
        return value;
    }

    /**
     * Returns a single element Object array that contains a clone of the
     * RandomVariate array.
     *
     * @return clone of RandomVariate[] array in Object[] array
     */
    @Override
    public Object[] getParameters() {
        return new Object[]{rv.clone()};
    }

    /**
     *
     * Sets the underlying RandomVariates.
     *
     * @param params A single element array containing an array of
     * RandomVariates.
     * @throws IllegalArgumentException If obj is length 0
     * @throws IllegalArgumentException if any elements of obj are not
     * RandomVariates
     */
    @Override
    public void setParameters(Object... params) {
        if (params.length == 0) {
            String msg = "No RandomVariates in parameters for ConvolutionVariate";
            log.severe(msg);
            throw new IllegalArgumentException(msg);
        }

        Object[] array = (Object[]) params[0];
        ArrayList<Integer> badArgs = new ArrayList<>();
        RandomVariate[] variates = new RandomVariate[array.length];
        for (int i = 0; i < array.length; ++i) {
            if (array[i] instanceof RandomVariate) {
                variates[i] = (RandomVariate) array[i];
            } else if (array[i] instanceof String) {
                variates[i] = RandomVariateFactory.getInstance(array[i].toString());
            } else {
                badArgs.add(i);
            }
        }

        if (badArgs.size() > 0) {
            throw new IllegalArgumentException(
                    "Non-RandomVariates passed in indices: "
                    + badArgs);
        }
        setRandomVariates(variates);
    }

    /**
     * Sets the array of RandomVariates.
     *
     * @param rand Array of RandomVariate[] instances
     */
    public void setRandomVariates(RandomVariate[] rand) {
        rv = rand.clone();
        for (int i = 0; i < rv.length; ++i) {
            rv[i].setRandomNumber(rng);
        }
    }

    /**
     * Gets a clone of the array of RandomVariates.
     *
     * @return clone of RandomVariate[] array
     */
    public RandomVariate[] getRandomVariates() {
        return rv.clone();
    }

    /**
     * Sets the supporting RandomNumber of each underlying RandomVariate.
     *
     * @param rand given RandomNumber
     */
    @Override
    public void setRandomNumber(RandomNumber rand) {
        super.setRandomNumber(rand);
        if (rv != null) {
            for (int i = 0; i < rv.length; ++i) {
                rv[i].setRandomNumber(rng);
            }
        }
    }

    /**
     * Return a String containing information about the underlying
     * RandomVariates.
     *
     * @return String including each RandomVariate's toString() on a separate
     * line.
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("ConvolutionRandomVariate");
        for (int i = 0; i < rv.length; ++i) {
            builder.append('\n');
            builder.append('\t');
            builder.append(rv[i]);
        }
        return builder.toString();
    }
}
