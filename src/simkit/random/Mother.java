/*
 * Mother.java
 *
 * Created on October 10, 2002, 2:20 PM
 */

package simkit.random;

/**
 *
 * @author Paul Sanchez
 * @author  Arnold Buss
 */
public class Mother implements RandomNumber {
    
    public static final long BITMASK = (1L << 32) - 1L;
    
    public static final double MULTIPLIER = 1.0 / (1L << 32);
    
    private long[] originalX;
    private long originalResult;
    
    private long result;
    private long[] x;
    
/** Creates a new instance of Mother */
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
    
    /** @return  The next Uniform(0, 1) random number
     */
    public double draw() {
        return drawLong() * MULTIPLIER;
    }
    
    /** @return  The current random number seed
     */
    public long getSeed() {
        return x[3];
    }
    
    /** @return  The current array of random number seed s
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
     * Crank through another generator to produce the initial values.
     *  
     * @param seed The new random number seed.  Seeds the generator producing
     * the initial 4 values.
     */
    public void setSeed(long seed) {
        throw new IllegalArgumentException("Use setSeeds(long[]) with 5 values instead");
    }
    
    /** @param seed The new array of seeds
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
    
    public String toString() { return "Mother of All Generators"; }
    
    /**
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
        
}
