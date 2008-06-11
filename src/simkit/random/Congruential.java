package simkit.random;

/**
* An implementation of a multiplicative Linear Congruential Generator.
* When used with the seeds in <CODE>CongruentialSeeds</CODE> produces the
* same streams as the random number generator in SIMCRIPT.
* This generator has a period of 2147483647 for any starting seed.
* <CODE>LKSeeds</CODE> contains seeds for 100 streams spaced 100,000 apart.
*
* @version $Id: Congruential.java 503 2003-12-19 21:53:06Z ahbuss $
*/
public class Congruential implements RandomNumber {

//    public static final long MODULUS = 2147483647L;
    public static final long MODULUS = 0x7FFFFFFFL;
    public static final long MULTIPLIER = 630360016L;
    
    public static final double MULT = 1.0 / MODULUS;

    private long   startingSeed;
    private long   currentSeed;
    
/**
* Contructs a new Congruential with the starting seed of the first stream.
**/
    public Congruential() {
        setSeed(CongruentialSeeds.SEED[0]);
    }
/**
  * Sets the seed to the given value.
  * @param seed The new random number seed
**/
    public void setSeed(long seed) {
        startingSeed = seed;
        resetSeed();
    }

/**
  * Returns the current seed. (Not the original)
  * @return  The current random number seed
**/
    public long getSeed() { return currentSeed;}

/**
* Resets the seed to its original value.
**/
    public void resetSeed() { currentSeed = startingSeed; }

/**
  * Sets the starting seed to the value contained in the first
  * element of the array.
  * @param seed The new array of seeds
  * @throws IllegalArgumentException If the array has zero length.
**/
    public void setSeeds(long[] seed) {
        if (seed.length == 0) {
            throw new IllegalArgumentException("seed array must be > 0");
        }
        else {
            setSeed(seed[0]);
        }
    }
/**
  * Returns a one element array containing the current seed.
  * @return  The current array of random number seed s
**/
    public long[] getSeeds() { return new long[] {currentSeed}; }

    /**
     * Generates and returns the next random number.
     * @return Next long, numerator of next Un(0,1)
     */    
    public long drawLong() {
        currentSeed = currentSeed * MULTIPLIER % MODULUS;
        if ( currentSeed == startingSeed ) {
            System.err.println("WARNING: Random seed repetition detected.");
        }
        return currentSeed;
    }
    
/**
  * Generates and returns the next U(0,1) random variate.
  * @return  The next Uniform(0, 1) random number
**/
    public double draw() {
        return Math.abs((double) drawLong() * MULT);
    }
    
/**
* Returns a String containing the type of generator and the current seed.
**/
    public String toString() {
        return "Multiplicative Congruential (" + this.getSeed() + ")";
    }
    
/*
* Returns the reciprical of the modulus of this generator.
**/
    public double getMultiplier() {
        return MULT;
    }
    
}
