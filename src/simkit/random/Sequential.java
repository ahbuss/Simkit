package simkit.random;

public class Sequential implements RandomNumber {

    private static final double MULTIPLIER = 1.0 / Long.MAX_VALUE;

    private long index;


    public Sequential() {
        resetSeed();
    }

    public void setSeed(long seed) { index = seed; }

    public long getSeed() { return index; }

    public void setSeeds(long[] seed) {
        if (seed.length > 0) {
            index = seed[0];
        }
    }

    public long[] getSeeds() { return new long[] { index }; }

    public void resetSeed() { index = 0L; }

    public double draw() {
        index = index + 1 % Long.MAX_VALUE;
        return index * MULTIPLIER;
    }
} 