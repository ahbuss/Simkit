package simkit.random;

/**
 *
 * @author  Arnold Buss
 */
public class PooledXORGenerator implements Pooled {

    public static final double MULTIPLICATIVE_FACTOR = 1.0 / (1 << 32);
    
    private RandomNumber first;
    private RandomNumber second;
    
    /** Creates a new instance of PooledXORGenerator */
    public PooledXORGenerator() {
    }
    
    /** @return  The next Uniform(0, 1) random number
     */
    public double draw() {
        return drawLong() * MULTIPLICATIVE_FACTOR;
    }
    
    public long drawLong() {
        return first.drawLong() ^ second.drawLong();
    }
    
    public RandomNumber getFirst() {
        return first;
    }
    
    public RandomNumber getSecond() {
        return second;
    }
    
    /** @return  The current random number seed
     */
    public long getSeed() {
        throw new IllegalArgumentException("Get Seeds from first or second RandomNumber");
    }
    
    /** @return  The current array of random number seed s
     */
    public long[] getSeeds() {
        throw new IllegalArgumentException("Get Seeds from first or second RandomNumber");
    }
    
    /**  Resets seed to last setSeed() value
     */
    public void resetSeed() {
        first.resetSeed();
        second.resetSeed();
    }
    
    public void setFirst(RandomNumber first) {
        this.first = first;
    }
    
    public void setSecond(RandomNumber first) {
        this.second = second;
    }
    
    /** @param seed The new random number seed
     */
    public void setSeed(long seed) {
        throw new IllegalArgumentException("Set Seeds in first or second RandomNumber");
    }
    
    /** @param seed The new array of seeds
     */
    public void setSeeds(long[] seed) {
        throw new IllegalArgumentException("Set Seeds in first or second RandomNumber");
    }
    
    public Object clone() {
        PooledXORGenerator copy = new PooledXORGenerator();
        copy.setFirst((RandomNumber) first.clone());
        copy.setSecond((RandomNumber) second.clone());
        return copy;
    }
    
}
