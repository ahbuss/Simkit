
package simkit.test;
import simkit.random.*;
/**
 * Tests NSSrng. A similar test routine exists in the NSS CVS under the branch: RNG_TESTS
 * Generates doubles to stdout.
 * @author  John Ruck
 * @version $Id$
 */
public class NSSrngTest {
    
    /** Creates a new instance of NSSrngTest */
    public NSSrngTest() {
    }
 /**
  * Tests NSSrng.
  * @param args[0] Starting seed, default 12
  * @param args[1] Number to generate, default 10
  */   
    
    public static void main(String[] args) {
        long seed = 12;
        long numberToGen =10;
        long draw = 0;
        System.out.println( "Running NSSrngTest.main() $Id$");
        if (args.length > 0) {
            seed = Long.valueOf(args[0]).longValue();
        }
        if (args.length > 1) {
            numberToGen = Long.valueOf(args[1]).longValue();
        }
        
        System.out.println( "Seed: " + seed + "\nGenerating: " + numberToGen + "\n");
        
        RandomNumber nss = RandomNumberFactory.getInstance("NSSrng",seed);
        //System.out.println(nss);

        for (int i = 0; i < numberToGen; i++) {
            draw = nss.drawLong();
            System.out.println(draw);
        }
        
    }
    
}
