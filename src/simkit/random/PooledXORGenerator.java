package simkit.random;

/** Pools two <code>RandomNumber</code> instances by XOR-ing
 * their generated long values.
 * @author Arnold Buss
 */
public class PooledXORGenerator extends PooledGeneratorBase {

    /** Creates a new instance of PooledXORGenerator */
    public PooledXORGenerator() {
    }
    
    /** @return  The next long - XOR's the two <code>RandomNumber</code>
     *  instances.
     */
    public long drawLong() {
        return first.drawLong() ^ second.drawLong() ;
    }

        /**
     * @return Next Un(0,1) pseudo-random number.
     */    
    public double draw() {
        return (drawLong() & MASK_31BIT) * MULTIPLICATIVE_FACTOR_31BIT;
    }
   
    /** @return When gives Un(0,1) when multipled by return from drawLong()
     */
    public double getMultiplier() {
        return MULTIPLICATIVE_FACTOR_31BIT;
    }
    
}
