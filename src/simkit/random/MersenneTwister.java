package simkit.random;

/**
 * Mersenne Twister:
 * <P>
 * This is a Java version of the C-program for MT19937: Integer version.
 * genrand() generates one pseudorandom unsigned integer (32bit)
 * which is uniformly distributed among 0 to 2^32-1  for each
 * call. sgenrand(seed) set initial values to the working area
 * of 624 words. Before genrand(), sgenrand(seed) must be
 * called once. (seed is any 32-bit integer except for 0).
 * Orignally Coded by Takuji Nishimura, considering the suggestions by
 * Topher Cooper and Marc Rieffel in July-Aug. 1997.
 * More information can be found at
 * <A HREF="http://www.math.keio.ac.jp/matumoto/emt.html">http://www.math.keio.ac.jp/matumoto/emt.html</A>.
 * <P>
 * Translated to Java by Michael Lecuyer January 30, 1999
 * Copyright (C) 1999 Michael Lecuyer
 * <p>
 * Adapted to conform to the <code>RandomNumber</code> interface in Simkit
 * by Arnie Buss, 8 October 2002.  None of the code was substantially changed,
 * simply shoved around a bit.  For example, filling the int buffer was broken
 * out to a separate method.  Since the RandomNumber interface has methods like
 * setSeed(long), getSeed(), and resetSeed(), there was a desire to make these
 * work in a reasonable manner.  setSeed(long) and resetSeed() appear to be working,
 * but there is something I am not getting about "what" the seed is, so
 * getSeed() does not appear to be correct.  The following code
 * <code>rng.setSeed(rng.getSeed());</code> should leave the object essentially
 * in the same state, and it does not.
 * <p>Simkit is also copyleft under the GNU General Public Licence.
 * <P>
 * This library is free software; you can redistribute it and or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later
 * version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Library General Public License for more details.
 * You should have received a copy of the GNU Library General
 * Public License along with this library; if not, write to the
 * Free Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 * 02111-1307  USA
 * <P>
 * Makoto Matsumoto and Takuji Nishimura, the original authors
 * ask "When you use this, send an email to: matumoto@math.keio.ac.jp
 * with an appropriate reference to your work"  You might also point
 * out this was a translation.
 *	<P>
 * REFERENCE
 * M. Matsumoto and T. Nishimura,
 * "Mersenne Twister: A 623-Dimensionally Equidistributed Uniform
 * Pseudo-Random Number Generator",
 * ACM Transactions on Modeling and Computer Simulation,
 * Vol. 8, No. 1, January 1998, pp 3--30.
 *
 */

public class MersenneTwister implements RandomNumber {
    // Period parameters
    private static final int N = 624;
    private static final int M = 397;
    private static final int MATRIX_A = 0x9908b0df;   //	private static final * constant vector a
    private static final int UPPER_MASK = 0x80000000; // most significant w-r bits
    private static final int LOWER_MASK = 0x7fffffff; // least significant r bits
    
    private static final double MODULUS_MULT = 1.0 / 2147483647L;
    // Tempering parameters
    private static final int TEMPERING_MASK_B = 0x9d2c5680;
    private static final int TEMPERING_MASK_C = 0xefc60000;
    private static final int UMASK = 0xffffffff;	// unsigned mask for promoting int -> long
    
    // #define TEMPERING_SHIFT_U(y)  (y >>> 11)
    // #define TEMPERING_SHIFT_S(y)  (y << 7)
    // #define TEMPERING_SHIFT_T(y)  (y << 15)
    // #define TEMPERING_SHIFT_L(y)  (y >>> 18)
    
    private int mt[]; // the array for the state vector
    private int mti; // mti==N+1 means mt[N] is not initialized
    private int mag01[];
    
    private int originalSeed;
    
    
    /**
     * Constructor using the default seed.
     */
    public MersenneTwister() {
        setSeed(4357L);
    }
    
    /**
     * Initalize the pseudo random number generator.
     *
     * @param seed from constructor
     *
     */
    private void sgenrand(int seed) {
        mt = new int[N];
        
        // setting initial seeds to mt[N] using
        // the generator Line 25 of Table 1 in
        // [KNUTH 1981, The Art of Computer Programming
        //    Vol. 2 (2nd Ed.), pp102]
        mt[0]= seed & 0xffffffff;
        
        for (mti = 1; mti < N; mti++)
            mt[mti] = (69069 * mt[mti-1]) & 0xffffffff;
        //                save original seed for resetSeed()
        originalSeed = seed;
        // mag01[x] = x * MATRIX_A  for x=0,1
        mag01 = new int[2];
        mag01[0] = 0x0;
        mag01[1] = MATRIX_A;
    }
    
    /**
     * Generate the next random number.
     */
    public int genrand() {
        int y;
        
        if (mti >= N) {	// generate N words at one time
            fill();
        }
        y = mt[mti++];
        y ^= y >>> 11;								// TEMPERING_SHIFT_U(y)
        y ^= (y << 7) & TEMPERING_MASK_B;	// TEMPERING_SHIFT_S(y)
        y ^= (y << 15) & TEMPERING_MASK_C;	// TEMPERING_SHIFT_T(y)
        y ^= (y >>> 18);							// TEMPERING_SHIFT_L(y)
        
        return y;
    }
    
    private void fill() {
        int kk;
        int y;
        for (kk = 0; kk < N - M; kk++) {
            y = (mt[kk] & UPPER_MASK) | (mt[kk+1] & LOWER_MASK);
            mt[kk] = mt[kk+M] ^ (y >>> 1) ^ mag01[y & 0x1];
        }
        for (; kk < N-1; kk++) {
            y = (mt[kk] & UPPER_MASK) | (mt[kk+1] & LOWER_MASK);
            mt[kk] = mt[kk+(M-N)] ^ (y >>> 1) ^ mag01[y & 0x1];
        }
        y = (mt[N-1] & UPPER_MASK) | (mt[0] & LOWER_MASK);
        mt[N-1] = mt[M-1] ^ (y >>> 1) ^ mag01[y & 0x1];
        
        mti = 0;
    }
    
    public long drawLong() {
        return (long) genrand();
    }
    
    /** @return  The next Uniform(0, 1) random number
     */
    public double draw() {
        return (double) (genrand() & 0xffffffffL) * MODULUS_MULT ;
    }
    
    /**
     * This does not work properly and should not be relied on.  Under "normal"
     * usage this shouldn't be necessary.
     * @return  The current random number seed
     */
    public long getSeed() {
        if (mti >= N) {
            fill();
        }
        return (long) mt[mti];
    }
    
    /** @return  The current array of random number seed s
     */
    public long[] getSeeds() {
        return new long[] { getSeed() };
    }
    
    /**  Resets seed to last setSeed() value
     */
    public void resetSeed() {
        sgenrand(originalSeed);
    }
    
    /** @param seed The new random number seed
     */
    public void setSeed(long seed) {
        if (seed != 0) {
            sgenrand( (int) seed);
        }
        else {
            throw new IllegalArgumentException("Seed cannot be 0");
        }
    }
    /** @param seed The new array of seeds
     */
    public void setSeeds(long[] seed) {
        if (seed.length == 0) {
            throw new IllegalArgumentException("Need at least one seed");
        }
        setSeed(seed[0]);
    }
    
    public String toString() {
        return "Mersenne Twister";
    }
    
    public Object clone() {
        Object copy = null;
        try {
            copy = super.clone();
        } catch (CloneNotSupportedException e) {}
        return copy;
    }
    /**
     * This main() outputs first 1000 generated numbers
     */
    public static void main(String args[]) {
        int j;
        
        //      Mersenne r = new Mersenne();
        //      a reference to simkit.random.MersenneTwister can be obtained from
        //      RandomNumberFactory
        RandomNumber r = RandomNumberFactory.getInstance("simkit.random.MersenneTwister");
        //       Use the object to generate Exponentials
        RandomVariate rv = RandomVariateFactory.getInstance("Exponential",
        new Object[] { new Double(1.0) }, r);
        for (j = 0; j < 1000; j++) {
            System.out.print((long) (r.draw() * 2147483647L) + " ");
            //          System.out.print(r.draw() + " ");
            //         System.out.print((((long)(r.genrand())) & 0xffffffffL) + " ");
            if (j%8==7) System.out.println();
        }
        System.out.println("100 " + rv + " RVs using MersenneTwister:");
        for (int i = 0; i < 100; i++) {
            System.out.print(rv.generate() + " ");
            if ( (i+1) % 10 == 0) { System.out.println(); }
        }
    }
    
}
