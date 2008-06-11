package simkit.test;
import simkit.random.RandomNumber;
import simkit.random.RandomNumberFactory;
/**
 * Tests NSSrng. A similar test routine exists in the NSS CVS under the branch: RNG_TESTS
 * Generates doubles to stdout.
 * @author  John Ruck
 * @version $Id: NSSrngTest.java 873 2006-05-19 00:37:30Z ahbuss $
 */
public class NSSrngTest {
    
    /** Creates a new instance of NSSrngTest */
    public NSSrngTest() {
    }
 /**
  * Tests NSSrng.
  * 
  * args[1] Number to generate, default 10
  * @param args args[0] = optional Starting seed, default 12; 
  *   args[1] = optional Number to generate, default 10
  */   
    
    public static void main(String[] args) {
        long seed = 12;
        long numberToGen =10;
        long draw = 0;
        System.out.println( "Running NSSrngTest.main() $Id: NSSrngTest.java 873 2006-05-19 00:37:30Z ahbuss $");
        if (args.length > 0) {
            seed = Long.valueOf(args[0]).longValue();
        }
        if (args.length > 1) {
            numberToGen = Long.valueOf(args[1]).longValue();
        }
        
        System.out.println( "Seed: " + seed + "\nGenerating: " + numberToGen + "\n");
        
        RandomNumber nss =  RandomNumberFactory.getInstance("NSSrng",seed);
        System.out.println(nss);
        
        long[] temp = nss.getSeeds();
        for (int i = 0; i< temp.length; i++) {
            System.out.print(temp[i] + " ");
        }
        System.out.println();
        for (int i = 0; i < numberToGen; i++) {
            draw = nss.drawLong();
            System.out.println(draw);
        }
        nss.resetSeed();
        System.out.println(nss);
        System.out.println(nss.drawLong());
        nss.setSeeds(temp);
        System.out.println(nss);
        System.out.println(nss.getSeed());
        System.out.println(nss.drawLong());
        
        long[] longArray = new long[] {1L, 2L};
 
        
        try {
            nss.setSeeds(longArray);
        } catch (Exception e) {
            System.out.println("In the catch for the 2 element long array\n\t" + e);
        }
        nss.setSeed(9999999999999L);
        System.out.println("After setting the hugh seed\n\t" + nss);
        nss.setSeed(112);
        
        System.out.println("After setting the seed to 112\n\t" + nss);
        
        try {
            nss.setSeed(1);
        } catch (Exception e) {
            System.out.println("In the catch for a seed of 1\n\t" + e);
        }
        System.out.println("After trying to set the seed to 1\n\t" + nss);
        nss.setSeed(2);
        System.out.println("After trying to set the seed to 2\n\t" + nss);
    }
    
}
