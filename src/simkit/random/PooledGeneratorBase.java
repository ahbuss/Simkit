package simkit.random;

/** Base class for pooled generators.  A subclass need only
 * implement the <code>drawLong()</code> method.
 * This base class supports pooling 2 RandomNumbers.
 * @author Arnold Buss
 * @version $Id$
 */
public abstract class PooledGeneratorBase implements Pooled {
    
    public static final double MULTIPLICATIVE_FACTOR_32BIT = 1.0 / (1L << 32);
    public static final double MULTIPLICATIVE_FACTOR_31BIT = 1.0 / (1L << 31);
    public static final long MASK_31BIT = 0x7FFFFFFFL;

/**
* The instance of the first RandomNumber.
**/
    protected RandomNumber first;

/**
* The instance of the second RandomNumber.
**/
    protected RandomNumber second;
    
    /** 
      * Creates a new instance of PooledGeneratorBase with no RandomNumbers
      * specified. The two RandomNumbers must be set prior to use.
      */
    public PooledGeneratorBase() {
    }
    
    /**
     * Returns the instance of the first RandomNumber.
     * @return First <code>RandomNumber</code> instance from pool
     */    
    public RandomNumber getFirst() {
        return first;
    }
    
    /**
     * Returns the instance of the second RandomNumber.
     * @return Second <code>RandomNumber</code> instance from pool
     */    
    public RandomNumber getSecond() {
        return second;
    }
    
    /** 
      * Do not use, get the seeds from the first or second RandomNumber.
      * @throws IllegalArgumentException In all cases.
     */
    public long getSeed() {
        throw new IllegalArgumentException("Get Seeds from first or second RandomNumber");
    }
    
    /** 
      * Do not use, get the seeds from the first or second RandomNumber.
      * @throws IllegalArgumentException In all cases.
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
     * Sets the instance of the first RandomNumber to be pooled.
     * @param first <code>RandomNumber</code> instance to be pooled with <code>second</code>
     */    
    public void setFirst(RandomNumber first) {
        this.first = first;
    }
    
    /**
     * Sets the instance of the second RandomNumber to be pooled.
     * @param second <code>RandomNumber</code> instance to be pooled with <code>first</code>
     */    
    public void setSecond(RandomNumber second) {
        this.second = second;
    }
    
/**
* Do not use, set the seeds of the first or second RandomNumber directly.
* @throws IllegalArgumentException In all cases.
**/
    public void setSeed(long seed) {
        throw new IllegalArgumentException("Set Seeds in first or second RandomNumber");
    }
    
/**
* Do not use, set the seeds of the first or second RandomNumber directly.
* @throws IllegalArgumentException In all cases.
**/
    public void setSeeds(long[] seed) {
        throw new IllegalArgumentException("Set Seeds in first or second RandomNumber");
    }
    
/**
* Returns a String that contains the name of this pooled generator with information
* about the two pooled RandomNumbers.
**/
    public String toString() {
        return getClass().getName() + " [" + first + ", " + second + "]";
    }
        
}
