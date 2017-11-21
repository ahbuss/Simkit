package simkit.random;

import simkit.random.NormalVariate;

/**
 * Generates Normal variates truncated at 0.0.
 * @version $Id$
 * @author ahbuss
 */
public class TruncatedNormalVariate extends NormalVariate {

    /**
     * 
     * @return max(super.generate(), 0.0)
     */
    @Override
    public double generate() {
        return Math.max(super.generate(), 0.0);
    }

    @Override
    public String toString() {
        return super.toString().replaceAll("Normal", "TruncatedNormal");
    }

}
