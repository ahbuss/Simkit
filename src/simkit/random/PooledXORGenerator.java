package simkit.random;

/** Pools two <code>RandomNumber</code> instances by XOR-ing
 * their generated long values.
 * @author Arnold Buss
 * @version $Id$
 */
public class PooledXORGenerator extends PooledGeneratorBase {

    /** 
      * Creates a new instance of PooledXORGenerator with no RandomNumbers set.
      * The instances of the RandomNumbers to pool must be set prior to use.
      */
    public PooledXORGenerator() {
    }
    
    /** 
     * Returns the results of drawing from the 2 RandomNumbers XOR'ed.
     * @return  The next long - XOR's the two <code>RandomNumber</code>
     *  instances.
     */
    public long drawLong() {
        return first.drawLong() ^ second.drawLong() ;
    }

     /**
     * Returns the next value as a U(0,1).
     * @return Next Un(0,1) pseudo-random number.
     */    
    public double draw() {
        return (drawLong() & MASK_31BIT) * MULTIPLICATIVE_FACTOR_31BIT;
    }
   
    /** 
      * The factor to mulitply drawLong() by the get U(0,1).
      * @return What gives Un(0,1) when multipled by return from drawLong()
     */
    public double getMultiplier() {
        return MULTIPLICATIVE_FACTOR_31BIT;
    }
    
}
