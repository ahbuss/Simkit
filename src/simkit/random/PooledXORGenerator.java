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
        return first.drawLong() ^ second.drawLong();
    }

}
