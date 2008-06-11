package simkit.random;

/**
 * @deprecated Use PooledXORGenerator instead
 * @see Pooled
 * @see PooledXORGenerator
 * @version $Id: PooledGenerator.java 503 2003-12-19 21:53:06Z ahbuss $
 */
public class PooledGenerator implements RandomNumber {
    
    public static final int MULTIPLIER = 630360016;
    public static final int MASK = 0x7FFFFFFF;
    public static final int SHIFT_RIGHT = 13;
    public static final int SHIFT_LEFT = 18;
    
    public static final long SCALE_FACTOR = 0x80000000L;
    public static final double MULTIPLICATIVE_FACTOR = 1.0 / (double) SCALE_FACTOR;
    
    private long[] originalSeeds;
    private long[] seeds;
    
    /**
     * @deprecated Use PooledXORGenerator instead
     */    
    public PooledGenerator() {
        setSeed(42L);
    }
    /**
     * Both the LCG and the Tausworth seeds are set to the same value.
     * @param seed The new random number seed
     **/
    public void setSeed(long seed) {
        seeds = new long[] { seed, seed};
        originalSeeds = new long[] { seed, seed};
    }
    
    /**
     * Only the LCG seed is returned - use <CODE>getSeeds()</CODE> to get both.
     * @return  The current random number seed
     **/
    public long getSeed() { return seeds[0];  }
    /**
     *  Resets seed to last setSeed() value
     **/
    public void resetSeed() {
        seeds = (long[]) originalSeeds.clone();
    }
    /**
     * @param seed The new array of seeds
     **/
    public void setSeeds(long[] seed) {
        if (seed.length == 2) {
            seeds = (long[]) seed.clone();
            originalSeeds = (long[]) seeds.clone();
        }
        else if (seed.length == 1){
            setSeed(seed[0]);
        }
        else {
            throw new IllegalArgumentException("Need 1 or two seeds: " +
            seed.length + " provided");
        }
        
    }
    /**
     * @return  The current array of random number seed s
     **/
    public long[] getSeeds() { return (long[]) seeds.clone();  }
    
    /** The LCG and the Tausworth generators are advanced, logical "or-d", and
     * the return scaled to [0, 1).
     * @return The next long value in the pseudo-random sequence
     * @deprecated Use PooledXORGenerator instead
     */
    public long drawLong() {
        seeds[0] *= MULTIPLIER;
        seeds[0] %= MASK;
        
        seeds[1] ^= seeds[1] >> 13;
        seeds[1] ^= seeds[1] << 18;
        seeds[1] &= MASK;
        
        return (seeds[0] ^ seeds[1]) ;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer("Pooled Generator (");
        for (int i = 0; i < seeds.length; i++) {
            buf.append(seeds[i]);
            if (i < seeds.length - 1) { buf.append(','); buf.append(' '); }
        }
        buf.append(')');
        return buf.toString();
    }
    
    public double draw() {
        return drawLong() * MULTIPLICATIVE_FACTOR;
    }
    
    public double getMultiplier() {
        return MULTIPLICATIVE_FACTOR;
    }
    
}
