package simkit.random;

/**
* Generates "random" numbers that are simply the last value + 1.
*
* @version $Id$
**/
public class Sequential implements RandomNumber {
    
    private static final double MULTIPLIER = 1.0 / Long.MAX_VALUE;
    
/**
* The current value of the seed. 
**/
    private long index;
    
/**
* Constructs a new Sequential that will the initial value set to 0.
**/
    public Sequential() {
        resetSeed();
    }
    
/**
* Sets the value to the given number.
**/
    public void setSeed(long seed) { index = seed; }
    
/**
* Returns the current seed.
**/
    public long getSeed() { return index; }
    
/**
* Sets the seed to the first element in the given array. If the array
* is length zero, does nothing.
**/
    public void setSeeds(long[] seed) {
        if (seed.length > 0) {
            index = seed[0];
        }
    }
    
/**
* Gets the value of the current seed.
**/
    public long[] getSeeds() { return new long[] { index }; }
    
/**
* Sets the current value to 0.
**/
    public void resetSeed() { index = 0L; }
    
/**
* Generates the next value and scales it to between 0 and 1. Each draw will
* increment by 1/<code>Long.MAX_VALUE</code>
**/
    public double draw() {
        return drawLong() * MULTIPLIER;
    }
    
/**
* Increments the seed and returns its new value. Wraps around at
* the maximum value for a <code>long</code>.
**/
    public long drawLong() {
        index = (index + 1) % Long.MAX_VALUE;
        return index;
    }
    
    /** 
      * Returns the value to multiply a <code>long</code> by to scale it to between
      * 0 and 1.
     */
    public double getMultiplier() {
        return MULTIPLIER;
    }
    
}
