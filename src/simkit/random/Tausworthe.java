package simkit.random;

/**
 *
 * @author  Arnold Buss
 */
public class Tausworthe implements RandomNumber {
    
    public static final long BITMASK = (1L << 32) - 1L;
    public static final int SHIFT_RIGHT = 13;
    public static final int SHIFT_LEFT = 18;
    
    public static final double MULTIPLICATIVE_FACTOR = 1.0 / (1 << 32);

    private long seed;
    private long originalSeed;
    
    /** Creates a new instance of Tausworthe */
    public Tausworthe() {
        setSeed(42L);
    }
    
    public long drawLong() {
        seed ^= seed >> SHIFT_RIGHT;
        seed ^= seed << SHIFT_LEFT;
        seed &= BITMASK;
        return seed;
    }
    
    /** @return  The next Uniform(0, 1) random number
     */
    public double draw() {
        return drawLong() * MULTIPLICATIVE_FACTOR;
    }
    
    /** @return  The current random number seed
     */
    public long getSeed() {
        return seed;
    }
    
    /** @return  The current array of random number seed s
     */
    public long[] getSeeds() {
        return new long[] { seed };
    }
    
    /**  Resets seed to last setSeed() value
     */
    public void resetSeed() {
        seed = originalSeed;
    }
    
    /** @param seed The new random number seed
     */
    public void setSeed(long seed) {
        originalSeed = seed;
        resetSeed();
    }
    
    /** @param seed The new array of seeds
     */
    public void setSeeds(long[] seed) {
        if (seed.length < 1) {
            throw new IllegalArgumentException("Nees at least one seed: " + seed.length);
        }
        setSeed(seed[0]);
    }
    
    public Object clone() {
        Object copy = null;
        try {
            copy = super.clone();
        } catch (CloneNotSupportedException e) {}
        return copy;
    }
    
    public String toString() {
        return "Tausworthe Generator (" + getSeed() + ")";
    }
    
}
