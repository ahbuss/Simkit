package simkit.random;

public class Congruential implements RandomNumber {

    private static final long MODULUS = 2147483647L;
    private static final long MULTIPLIER = 630360016L;

    private long   startingSeed;
    private long   currentSeed;
    
    public Congruential() {
        setSeed(CongruentialSeeds.SEED[0]);
    }
/**
  * @param seed The new random number seed
**/
    public void setSeed(long seed) {
        startingSeed = seed;
        resetSeed();
    }

/**
  * @return  The current random number seed
**/
    public long getSeed() { return currentSeed;}
    public void resetSeed() { currentSeed = startingSeed; }
/**
  * @param seed The new array of seeds
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
  * @return  The current array of random number seed s
**/
    public long[] getSeeds() { return new long[] {currentSeed}; }

/**
  * @return  The next Uniform(0, 1) random number
**/
    public double draw() {
        currentSeed = currentSeed * MULTIPLIER % MODULUS;
        if ( currentSeed == startingSeed ) {
            System.err.println("WARNING: Random seed repetition detected.");
        }
        return (double) currentSeed / MODULUS;
    }
}