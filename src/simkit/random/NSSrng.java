// Oringinal C++ Version Copyright 2003 Metron, Incorporated
// All Rights Reserved
package simkit.random;

/**
 * An implementation of the RandomNumber Generator contained in the
 * Naval Simulation System. <p>It is an implementation of a combined tausworthe generator as
 *  described by L'Ecuyer in his paper titled, 'Maximally Equidistributed
 *  Combined Tausworthe Generators.'
 *  This uses a fast software implementation which is available when
 *  the proper conditions are met. The fast implementation is used to generate
 *  random numbers from three Tausworthe generator which are combined
 *  through a simple bitwise exclusive-or of the results of each generator.
 *  The period of the individual generators are 2^31-1, 2^29-1 and 2^28-1;
 *  yielding a period for the combined generator in the order of approximately
 *  2^88.  It requires the keeping track of 3 seed values- one for each
 *  generator.  The seeds, s1, s2, s3, must be initially set to non-zero values
 *  greater than 1, 7 and 15.  This is explained further in the paper.  Also in
 *  talking to the author and making observations of the generators, I
 *  found that if all of the starting values are small then the generator
 *  requires a few steps to warm up.
 *  The individual tausworthe generator will produce numbers between their
 *  minimum starting value and (2^32)-1 inclusive, but the combined generator
 *  will produce numbers from zero to the max value, inclusive.  </p>
 * <p>
 * Original C++ implementation by Metron, Incorporated.<br/>
 * Translated to Java by John Ruck, Rolands and Associates Corporation.</p>
 * @version: $Id$
 */

public class NSSrng implements RandomNumber {
    
    /**
     * Maximum allowed value of the seeds. Equals (2^32)-1
     **/
    private static final long MAX_VALUE = 4294967295L;
    
    /**
     * The reciprocal of MAX_VALUE
     **/
    private static double INVERSE = 2.328306437e-10;
    
    /**
     * mask for first generator = (2^32)-2
     **/
    private static final long M1 = 4294967294L;
    
    /**
     * mask for second generator = (2^32)-8
     **/
    private static final long M2 = 4294967288L;
    
    /**
     * mask for third generator = (2^32)-16
     **/
    private static final long M3 = 4294967280L;
    
    /**
     * The total number of calls.
     **/
    private long RANDOM_CALLS = 0;
    
    /**
     * One of three seeds.
     **/
    protected long S1 = 0;
    
    /**
     * One of three seeds.
     **/
    protected long S2 = 0;
    
    /**
     * One of three seeds.
     **/
    protected long S3 = 0;
    
    /**
     * The oringinal value of S1, the generator will be reset using this value
     * when resetSeed is called.
     **/
    protected long originalSeed = 0;
    
    /**
     * If true, prints debug information
     **/
    protected boolean debug = false;
    
    /**
     * Constructs a new NSSrng. Should be constructed using the RandomNumberFactory
     **/
    public NSSrng() {
    }
       /**
     * If true, prints debug information
     **/ 
    public void setDebug(boolean debug) {
        this.debug = debug;
        if (this.debug) {
            System.out.println("+++ Debug enabled for " + this);
        } else {
            System.out.println("--- Debug disabled for " + this);
        }
    }
    
    //javadoc inherited
    public long drawLong() {
        long b;
        long temp;

        /* 
         * Java change: Any operation that causes a number to get bigger
         * is followed by anding with MAX_VALUE to simulate the rollover
         * that would occur in C++ using a 32bit unsigned long, we are
         * using a 64bit signed long.
         */
        b = ((((S1 << 13) & MAX_VALUE) ^ S1) >> 19) ;
        if (debug) System.out.print(b + "\t");
        S1 = ((((S1 & M1) << 12) & MAX_VALUE) ^ b) ;
        if (debug) System.out.print(S1 + "\t");
        b = ((((S2 << 2) & MAX_VALUE) ^ (S2)) >> 25);
        if (debug) System.out.print(b + "\t");
        S2 = ((((S2 & M2) << 4) & MAX_VALUE) ^ b) ;
        if (debug) System.out.print(S2 + "\t");
        b = ((((S3 << 3) & MAX_VALUE) ^ S3) >> 11) ;
        if (debug) System.out.print(b + "\t");
        S3 = ((((S3 & M3) << 17) & MAX_VALUE) ^ b) ;
        if (debug) System.out.println(S3 + "\t");
        if ((RANDOM_CALLS)==198) {
            int a=0;
        }
        RANDOM_CALLS++;
        
        temp = ((S1) ^ (S2) ^ (S3)) & MAX_VALUE;
        return temp;
    }
    
    //javadoc inherited
    public double draw() {
        return drawLong() * INVERSE;
    }
    
    /**
     * Sets the seed for S2 and S3 based on the value of S1 and "warms up" the
     * generator.
     **/
    protected void initialize_random_generator() {
        int i, warm_up_iterations;
        
      /* The following ensures that the numbers initially chosen for S2 & S3 are
      between their respective minimum values (8 and 16) and the maximum
      value.  This handles integer overflow so that the code remains
      machine independant of the machine's overflow handling. */
        
        long distance_to_max_value = (MAX_VALUE - S1);
        
        if (distance_to_max_value < 701)
            S2 = 701 - distance_to_max_value + 7;
        
        else
            S2 = S1 + 701;
        
        if (distance_to_max_value < 1500)
            S3 = 1500 - distance_to_max_value + 15;
        
        else
            S3 = S1 + 1500;
        
     /* Since each generator only looks at a portion of the seed value's bits--
        the most significant 31 bits for S1, 29 for S2 and 28 for S3, I added
        this little extra step to ensure that each seed when entered will give
        an unique stream of random numbers.  Although this uniqueness, in the case
        of neighboring numbers that differ only in their last bit, will be due
        to a shift in starting position of the stream of random numbers.
        This should provide sufficient variation. */
        
        if ((S1 & 1) == 1)
            warm_up_iterations = 100;
        
        else
            warm_up_iterations = 153;
        
        if (debug) System.out.println("+++ NSSrng performing " + warm_up_iterations + "warmups");
        for (i = 0; i < warm_up_iterations; i++)
            drawLong();
    }
   
    /**
     * Sets the seed to the given value. The seed must be greater than 1.
     * Seeds larger than 4294967295L, the max value of a C++ unsigned long
     * (32 bits) will be truncated.
     * @throws IllegalArgumentException If the seed is not greater than 1
     **/
    public void setSeed(long seed) {
        
   /* our seed must be larger than 1 to ensure a properly formed stream of
      random number.  If the seed is 1 or smaller than the first random
      number generator (s1) will degenerate and return a stream of 0's
      which we don't want.  The generator will continue to generate non-
      zero number because the other two generators should be functioning
      normally by the way their seeds are created from the user inputed seed
      but this is not okay. */
        
        if (seed < 2) {
            throw new IllegalArgumentException("Seed must be greater than 1");
        }
        if (seed > MAX_VALUE) {
            seed = seed & MAX_VALUE;
            System.out.println("??? WARNING: Attempt to set the seed of NSSrng > " + MAX_VALUE +
                " truncating to 32 bits. New seed is " + seed);
        }
        S1 = seed;
        RANDOM_CALLS = 0;
        originalSeed = seed;
        initialize_random_generator();
    }
    
    /**
     * Returns the current value of the current seed. This RandomNumber has
     * 3 seeds so getSeeds() should be used instead. It is not guaranteed that
     * setting the seed with the return value of this method would restore the
     * state of the generator.
     **/
    public long getSeed() {
        return S1;
    }
    
    //javadoc inherited
    public void resetSeed() {
        setSeed(originalSeed);
    }
    
    /**
     * Sets all 3 seeds of this RandomNumber. The performance of the generator may
     * depend on the relationship of the seeds which is automattically established with
     * setSeed. This method does not perform
     * the "warm-up" draws that was part of the NSS generator. Internal limits
     * on the seeds are not enforced. Therefore, setSeed should normally be
     * used.
     * @param seed An array of 3 longs.
     * @throws IllegalArgumentException If the array doesn't contain exactly 3 Longs.
     **/
    public void setSeeds(long[] seed) {
        if (seed.length != 3) {
            throw new IllegalArgumentException("NSSrng.setSeeds requires an array of 3 numbers" +
            ", the array length was " + seed.length);
        }
        
        S1 = seed[0];
        S2 = seed[1];
        S3 = seed[2];
    }
    
    //javadoc inherited
    public long[] getSeeds() {
        long[] ret = {S1, S2, S3};
        return ret;
    }
    
    public double getMultiplier() {
        return INVERSE;
    }
    
    /**
     * Creates a copy of the current instance.
     * @return copy of current instance with same seeds.
     */
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {}
        finally{}
        return null;
    }
    
    public String toString() {
        return new String("RandomNumber implemenation, NSSrng with initial seed of " + originalSeed +
            "\t[" + super.toString() + "]");
    }
}

