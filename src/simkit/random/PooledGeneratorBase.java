package simkit.random;

/** Base class for pooled generators.  A subclass need only
 * implement the <code>drawLong()</code> method.
 * @author Arnold Buss
 */
public abstract class PooledGeneratorBase implements Pooled {
    
    public static final double MULTIPLICATIVE_FACTOR = 1.0 / (1 << 32);

    protected RandomNumber first;
    protected RandomNumber second;
    
    /** Creates a new instance of PooledGeneratorBase */
    public PooledGeneratorBase() {
    }
    
    /**
     * @return Next Un(0,1) pseudo-random number.
     */    
    public double draw() {
        return drawLong() * MULTIPLICATIVE_FACTOR;
    }
    
    /**
     * @return First <code>RandomNumber</code> instance from pool
     */    
    public RandomNumber getFirst() {
        return first;
    }
    
    /**
     * @return Second <code>RandomNumber</code> instance from pool
     */    
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
    
    /**
     * @param first <code>RandomNumber</code> instance to be pooled with <code>second</code>
     */    
    public void setFirst(RandomNumber first) {
        this.first = first;
    }
    
    /**
     * @param second <code>RandomNumber</code> instance to be pooled with <code>first</code>
     */    
    public void setSecond(RandomNumber second) {
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
    
    /** Subclasses that have non-primitive instance variables
     * should override this to be sure that clean copies are
     * likewise made.
     * @return Deep copy, with copies of <code>first</code> and <code>second</code>
     */    
    public Object clone() {
        PooledXORGenerator copy = new PooledXORGenerator();
        copy.setFirst((RandomNumber) first.clone());
        copy.setSecond((RandomNumber) second.clone());
        return copy;
    }
    
    public String toString() {
        return getClass().getName() + " [" + first + ", " + second + "]";
    }
        
}
