package simkit.random;

public class Sequential implements RandomNumber {

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
        index += 1;
        return Double.NaN;
    }
} 