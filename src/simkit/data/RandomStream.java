// RandomStream.java

package simkit.data;

/**
 * Random Numbers for simkit.<br>
 *
 * This class produces the same random stream of
 * numbers produced by the random number generator
 * in SIMSCRIPT II.  This may not be the best random
 * number generator available, but at least you know
 * what you are getting.  The period is approximately 2^31 .
 * The 10 standard SIMSCRIPT streams can be selected
 * with the STREAM_x constants.
 * @deprecated The seeds have been replaced by <CODE>simkit.random.CongruentialSeeds<CODE>
 *             The random variate generation has been replaced by the <CODE>simkit.random.RandomVariate</CODE>
 *             interface and its various implementations, and random number generation
 *             has been replaced by the <CODE>simkit.random.RandomNumber</CODE> and
 *             its implementations, most notably <CODE>simkit.random.Congruential</CODE>
 * @see simkit.random.RandomNumber
 * @see simkit.random.RandomVariate
 * @see simkit.random.Congruential
 * @see simkit.random.CongruentialSeeds
 * @version   simkit 0.5.3
 * @author    Arnold H. Buss
 * @author    Kirk A. Stork (Java port & modifications)
 *
 * <UL>
 *  <LI> Java port                      [6.20.96]
 *  <LI> Added Rayleigh and ChiSquare   [8.25.96]
 *  <LI> 22 Oct 1998 Conforms to RandomNumber interface
 *  </UL>
 */
public class RandomStream implements RandomNumber {

    public static final long[] STREAM =
       new long[] {
           2116429302L,
           683743814L,
           964393174L,
           1217426631L,
           618433579L,
           1157240309L,
           15726055L,
           48108509L,
           1797920909L,
           477424540L
    };
    
    public final static long STREAM_1  = STREAM[0];
    public final static long STREAM_2  = STREAM[1];
    public final static long STREAM_3  = STREAM[2];
    public final static long STREAM_4  = STREAM[3];
    public final static long STREAM_5  = STREAM[4];
    public final static long STREAM_6  = STREAM[5];
    public final static long STREAM_7  = STREAM[6];
    public final static long STREAM_8  = STREAM[7];
    public final static long STREAM_9  = STREAM[8];
    public final static long STREAM_10 = STREAM[9];

   private static final long MODULUS = 2147483647L;
   private static final long MULTIPLIER = 630360016L;

   private long   startingSeed;
   private long   currentSeed;
   private int    boxFlop;
   private double boxSave;

   public RandomStream() {
      setSeed(STREAM_1);
      boxFlop = 1;
   }


   public RandomStream( long initialSeed ) {
      setSeed(initialSeed);
      boxFlop = 1;
   }

   public long getSeed() {return currentSeed;}

/**
  * @deprecated
**/
   public  long seed() {
      return currentSeed;
   }
   
   public  void setSeed(long newSeed) {
      startingSeed = newSeed;
      currentSeed = newSeed;
   }

   public void resetSeed() {currentSeed = startingSeed;}

   public String toString() {return "PMMLCG1";}

   public double draw() {
      currentSeed = currentSeed * MULTIPLIER % MODULUS;
      if ( currentSeed == startingSeed ) {
         System.err.println("WARNING: Random seed repetition detected.");
      }
      return (double) currentSeed / MODULUS;
   }

   // Generate uniform (0,1)

   public  double uniform() {
     return draw();
   }

   public double weibull( double a, double b) {
      return b * Math.pow( -( Math.log( uniform() ) ), ( 1.0 / a ));
   }
   
   public double rayleigh( double b) {
      return b * Math.pow( -( Math.log( uniform() ) ), ( 0.5 ));
   }
   
  public double uniform(double a, double b) {
    if (a <= b) {
      return a + (b - a) * uniform();
    }
    else {
      return (a + b) / 2.0;
    }
  }

   public double exponential(double mean) {
      if (mean > 0 ) {
         return - mean * Math.log( uniform() );
      } else {
         return mean;
      }
   }

   public double erlang(double mean, long k) {
      double erl ;
      long j ;
    
      if (mean <= 0) {
         return mean;
      }
      erl = 1.0;
      j = k;
      while (j-- > 0) {
         erl = erl * uniform();
      }
      return - Math.log( erl ) * mean / k ;
    }

   public double boxMuller() {
      double  v1 = 0.0 , v2 = 0.0, w, y;
      if (boxFlop > 0) {
         w = 2.0;
         while (w  > 1.0) {
            v1 = 2 * uniform() - 1;
            v2 = 2 * uniform() - 1;
            w = v1 * v1 + v2 * v2;
         }
         y = Math.sqrt( - 2.0 * Math.log(w) / w );
         boxSave = v2 * y;
         boxFlop = 0;
         return v1 * y;
      } else {
         boxFlop = 1;
         return boxSave;
      }
   }

   public double boxMuller(double mean, double std) {
      if (std <= 0.0) {
         return mean;
      } else {
         return mean + std * boxMuller();
      }
   }

   public double logNormal(double mean, double std) {
      return Math.exp( boxMuller(mean, std) );
   }


   public double gamma(double alpha) {
      double b, p, y;
      double a, q, theta, d, z, u1, v, w;
      if (alpha <= 0.0) {
         return alpha;
      }
      if (alpha < 1.0) {
         b = 1.0 + alpha / Math.E;
         while ( true ) {
            p = b * uniform();
            if ( p <= 1) {
               y = Math.pow(p, 1./alpha);
               if ( uniform() <= Math.exp(-y) ) {
                  return y;
               }
            } else {
               y = - Math.log( (b - p)/alpha);
               if (uniform() <= Math.pow( y, alpha - 1.0) ) {
                  return y;
               }
            }
         }
      } else {   // Set up parameters
         a = 1.0 / Math.sqrt(2.0 * alpha - 1.0);
         b = alpha - 1.38629436111989061883;    // Number is Math.log(4)
         q = alpha + 1.0 / a;
         theta = 4.5;
         d = 2.504077396776274;        // Math.log(1 + theta)
         while (true) {
            u1 = uniform();
            v = a * Math.log( u1 / (1.0 - u1) );
            y = alpha * Math.exp(v);
            z = u1 * u1 * uniform();
            w = b + q * v - y;
            if (w + d - theta * z >= 0) {
               return y;
            } else {
               if (w >= Math.log(z) ) {
                  return y;
               }
            }
         }
       }
   }


   public double gamma(double alpha, double beta) {
      if (beta <= 0) {
         return alpha * beta;
      }
      return gamma(alpha) * beta;
   }

   public double beta(double alpha1, double alpha2) {
      double u1;
      if (alpha1 <= 0) {
         return alpha1/(alpha1 + alpha1);
      }
      if (alpha2 <= 0) {
         return alpha1/(alpha1 + alpha2);
      }
      u1 = gamma(alpha1);     // Klunky way, not the best...
      return u1/( u1 + gamma(alpha2) );  // But works!
   }

   public int uniformI(int a, int b) {
      if ( a <= b) {
         return a + (int) Math.floor((b - a + 1) * uniform()) ;
      } else {
         return a;
      }
   } 

   public double chisq(int k) {
      return gamma( (double)(k/2), 2.0);
   }

   public static long[] generateSeeds(int numberStreams) {
     long z = 1973272912L;
     long[] generated = new long[numberStreams];
     if (generated.length > 0) {generated[0] = z;}
     for (int i = 1; i < numberStreams; i++) {
         z = z * 715L % MODULUS;
         z = z * 1058L % MODULUS;
         z = z * 1385L % MODULUS;
         generated[i] = z;
     }
     return generated;
   }

   public static void main(String[] args) {
 /*
     RandomStream x = new RandomStream();
     for (int i = 0; i < 10; i++) {
       System.out.println(x.uniform(1.5, 2.5));
     }
*/
      long newSeeds[] = RandomStream.generateSeeds(100);
      System.out.print('[');
      for (int i = 0; i < newSeeds.length; i++) {
        System.out.print(newSeeds[i]);
        if (i < newSeeds.length - 1) {System.out.print(", ");}
        if (i % 9 == 0) {System.out.println();}
      }
      System.out.print("]" + simkit.SimEntity.NL);
   }

}  // END CLASS RandomStream
