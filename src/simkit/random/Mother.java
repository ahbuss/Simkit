/*
 * Mother.java
 *
 * Created on October 10, 2002, 2:20 PM
 */

package simkit.random;

/**
 * The "Mother-of-All" random number generators. A multiply-with-carry generator
 * invented by George Marsaglia.
 * <p/>Cycle length is 3 x 10<sup>47</sup> 
 * @author Paul Sanchez
 * @author  Arnold Buss
 * @version $Id$
 */
public class Mother implements RandomNumber {
    
    public static final long BITMASK = (1L << 32) - 1L;
    
    public static final double MULTIPLIER = 1.0 / (1L << 32);
    
    private long[] originalX;
    private long originalResult;
    
    private long result;
    private long[] x;
    
/** 
* Creates a new instance of Mother with default seeds.
*/

    public Mother() {
        x = new long[4];
        setSeeds(new long[] {
            123456L,
            654321L,
            42424242L,
            69696969L,
            987654321L * 123456789L
        });
    }
    
/**
* Generates the next number.
**/
    public long drawLong() {
        result = 2111111111L * x[0] +
                 1492L * x[1] +
                 1776L * x[2] +
                 5115L * x[3] +
                 (result >>> 32);
        x[0] = x[1];
        x[1] = x[2];
        x[2] = x[3];
        x[3] = result & BITMASK;
        return x[3];
    }
    
    /** 
      * Generates the next U(0,1)
      * @return  The next Uniform(0, 1) random number
     */
    public double draw() {
        return drawLong() * MULTIPLIER;
    }
    
    /** 
      * Returns the current seed.
      * @return  The current random number seed
     */
    public long getSeed() {
        return x[3];
    }
    
    /** 
      * Returns an array containing the 5 seeds of the generator.
      * @return  The current array of random number seed s
     */
    public long[] getSeeds() {
        return new long[] { x[0], x[1], x[2], x[3], result};
    }
    
    /**  Resets seed to last setSeed() value
     */
    public void resetSeed() {
        x = (long[]) originalX.clone();
        result = originalResult;
    }
    
    /**
     * Not used since this generator requires 4 seeds (use setSeeds(long[])) 
     * @throws IllegalArgumentException In all cases.
     */
    public void setSeed(long seed) {
        throw new IllegalArgumentException("Use setSeeds(long[]) with 5 values instead");
    }
    
    /** 
     * Sets this generators seeds to the 5 seeds in the given array.
     * @param seed An array with at least 5 elements. The elements should be unique
     * and non-zero.
     * @throws IllegalArgumentException If the array does not have at least 5 elements.  
     */
    public void setSeeds(long[] seed) {
        if (seed.length < 5) {
            throw new IllegalArgumentException("Need at least 5 longs: " + seed.length);
        }
        for (int i = 0; i < x.length; i++) {
            x[i] = seed[i];
        }
        result = seed[4];
        
        originalX = (long[]) x.clone();
        originalResult = result;
    }
    
    public Object clone() {
        Object copy = null;
        try {
            copy = super.clone();
        } catch (CloneNotSupportedException e) {}
        return null;
    }
    
/**
* Returns "Mother of All Generators"
**/
    public String toString() { return "Mother of All Generators"; }
    
    /** 
     * Tests the random generator.
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        RandomNumber mother = RandomNumberFactory.getInstance("simkit.random.Mother");
        for (int i = 0; i < 100; i++) {
            System.out.print(mother.draw() + " ");
            if (i % 10 == 9) { System.out.println(); }
        }
//        mother.resetSeed();
//        for (int i = 0; i < 100; i++) {
//            System.out.print(mother.draw() + " ");
//            if (i % 10 == 9) { System.out.println(); }
//        }
        
         RandomNumber mother2 = RandomNumberFactory.getInstance(
            "simkit.random.Mother", new long[] {
            123456L,
            654321L,
            42424242L,
            69696969L,
            987654321L * 123456789L
        });
        System.out.println("----------------------------------");
        for (int i = 0; i < 100; i++) {
            System.out.print(mother2.draw() + " ");
            if (i % 10 == 9) { System.out.println(); }
        }
//         RandomNumberFactory.getInstance("simkit.random.Mother", 1L);
    }
        
/**
* Returns the multiplier used to convert the random number to a U(0,1).
**/
    public double getMultiplier() {
        return MULTIPLIER;
    }
    
}
