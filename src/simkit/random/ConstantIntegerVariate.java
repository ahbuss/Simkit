package simkit.random;

/**
 * This always generates the same <code>(int) value</code>. It is the integer
 * equivalent to ConstantVariate (and in fact extends it). Note that
 * <code>value</code> can have any double value, but integers will be generated
 * by <code>generateInt()</code> and <code>generate</code> will always be just
 * the integer part.
 *
 * @author ahbuss
 */
public class ConstantIntegerVariate extends ConstantVariate implements DiscreteRandomVariate {

    /**
     *
     * @return (int) value
     */
    @Override
    public double generate() {
        return generateInt();
    }

    /**
     *
     * @return (int) value
     */
    @Override
    public int generateInt() {
        return (int) getValue();
    }

}
