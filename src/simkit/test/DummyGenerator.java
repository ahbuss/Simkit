package simkit.test;

import simkit.random.RandomNumber;

/**
 *
 * @author  Arnold Buss
 */
public class DummyGenerator implements RandomNumber, Cloneable {
    
    /** Creates a new instance of DummyGenerator */
    public DummyGenerator() {
    }
    
    /** @return  The next Uniform(0, 1) random number
     */
    public double draw() {
        return 0;
    }
    
    public long drawLong() {
        return 0;
    }
    
    /** @return  The current random number seed
     */
    public long getSeed() {
        return 0;
    }
    
    /** @return  The current array of random number seed s
     */
    public long[] getSeeds() {
        return new long[0];
    }
    
    /**  Resets seed to last setSeed() value
     */
    public void resetSeed() {
    }
    
    /** @param seed The new random number seed
     */
    public void setSeed(long seed) {
    }
    
    /** @param seed The new array of seeds
     */
    public void setSeeds(long[] seed) {
    }
    
    public Object clone() {
        Object copy = null;
        try {
            copy = super.clone();
        } catch (CloneNotSupportedException e) {}
        return copy;
    }
    
    public double getMultiplier() {
        return 0;
    }
    
}
