package simkit.random;

/**
 * Implements a Tausworthe random number generator.
 * <p>The next seed is ((current seed) xor (current seed shifted 13 bits to the right) xor
 * (current seed shifted 18 bits to the left)) and 0x7FFFFFFF [31 bits]</p>
 * @author  Arnold Buss
 * @version $Id: Tausworthe.java 503 2003-12-19 21:53:06Z ahbuss $
 */
public class Tausworthe implements RandomNumber {
    
    public static final long BITMASK = 0x7FFFFFFFL;
    public static final int SHIFT_RIGHT = 13;
    public static final int SHIFT_LEFT = 18;
    
    public static final double MULTIPLICATIVE_FACTOR = 1.0 / (1L << 31);

/**
* The current value of the seed.
**/
    private long seed;

/**
* The seed specified by the last setSeed.
**/
    private long originalSeed;
    
/** 
* Creates a new instance of Tausworthe with the default seed (42)
 */
    public Tausworthe() {
        setSeed(42L);
    }
    
/**
* Generates the next value.
**/
    public long drawLong() {
        seed ^= seed >> SHIFT_RIGHT;
        seed ^= seed << SHIFT_LEFT;
        seed &= BITMASK;
        return seed;
    }
    
    /** 
      * Generates the next value scaled to be U(0,1)
      * @return  The next Uniform(0, 1) random number
     */
    public double draw() {
        return drawLong() * MULTIPLICATIVE_FACTOR;
    }
    
    /**
      * Returns the current value of the seed.
     */
    public long getSeed() {
        return seed;
    }
    
    /** 
      * Returns a single element array containing the current value of the seed.
     */
    public long[] getSeeds() {
        return new long[] { seed };
    }
    
    /**  Resets seed to last setSeed() value
     */
    public void resetSeed() {
        seed = originalSeed;
    }
    
    /** 
      * Sets the seed to the given value.
     */
    public void setSeed(long seed) {
        originalSeed = seed;
        resetSeed();
    }
    
    /**
      * Sets the seed to the value of the first element in the array.
      * @param seed An array with at least one element containing the new seed.
      * @throws IllegalArgumentException If the array does not have at least one element. 
     */
    public void setSeeds(long[] seed) {
        if (seed.length < 1) {
            throw new IllegalArgumentException("Nees at least one seed: " + seed.length);
        }
        setSeed(seed[0]);
    }
    
/**
* Returns a String containing the name of this RandomNumber with the current seed.
**/
    public String toString() {
        return "Tausworthe Generator (" + getSeed() + ")";
    }
    
/**
* The number to multiply the seed by to scale it to a U(0,1)
**/
    public double getMultiplier() {
        return MULTIPLICATIVE_FACTOR;
    }
    
}
