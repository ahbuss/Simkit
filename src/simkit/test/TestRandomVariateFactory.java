/*
 * MediatorFactory.java
 *
 * Created on March 14, 2002, 10:02 PM
 */

package simkit.test;
import simkit.random.RandomNumber;
import simkit.random.RandomNumberFactory;
import simkit.random.RandomVariate;
import simkit.random.RandomVariateFactory;
/**
 *
 * @author  Arnold Buss
 * @version $Id$
 */
public class TestRandomVariateFactory {

    /** Creates new MediatorFactory */
    public TestRandomVariateFactory() {
    }

    /**
    * @param args the command line arguments
    */
    public static void main (String args[]) {
        
        System.out.println( (1L << 31) - 1 );
        System.out.println(simkit.random.Congruential.MODULUS);
        
        
        RandomVariateFactory.setVerbose(true);
        
        Class[] classes = new Class[] {
            simkit.random.ExponentialVariate.class,
            simkit.random.NormalVariate.class,
            simkit.random.GammaVariate.class,
            simkit.random.BetaVariate.class
        };
        
        String[] name = new String[] {
                "simkit.random.ExponentialVariate",
                "simkit.random.NormalVariate",
                "GammaVariate",
                "Beta"
        };
        
        Object[][] param = new Object[][] {
            new Object[] { new Double(1.0) },
            new Object[] { new Double(1.0), new Double(2.0) },
            new Object[] { new Double(1.0), new Double(2.0) },
            new Object[] { new Double(1.0), new Double(2.0) }
        };
        
        long seed = 12345L;
        long[] seeds = new long[] { seed };
        
        RandomVariate[] rand = new RandomVariate[name.length];
        RandomVariate[] rand2 = new RandomVariate[name.length];
        for (int i = 0; i < classes.length; i++) {
            rand[i] = RandomVariateFactory.getInstance(classes[i], param[i]);
            System.out.println(rand[i] + "\t" + rand[i].generate());
            rand2[i] = RandomVariateFactory.getInstance(classes[i], param[i]);
            System.out.println(rand2[i] + "\t" + rand2[i].generate());
        }
        for (int i = 0; i < classes.length; i++) {
            rand[i] = RandomVariateFactory.getInstance(classes[i], param[i], seed);
            System.out.println(rand[i] + "\t" + rand[i].generate());
            rand2[i] = RandomVariateFactory.getInstance(classes[i], param[i], seeds);
            System.out.println(rand2[i] + "\t" + rand2[i].generate());
        }
        for (int i = 0; i < classes.length; i++) {
            rand2[i] = RandomVariateFactory.getInstance(rand[i]);
            System.out.println(rand[i] + "\t" + rand[i].generate());
            System.out.println(rand2[i] + "\t" + rand2[i].generate());
            System.out.println(rand[i].getRandomNumber() == rand2[i].getRandomNumber());
        }
        
        RandomVariateFactory.addSearchPackage("simkit.test");
        RandomVariate seq = RandomVariateFactory.getInstance("Sequence", new Object[] {});
        for (int i = 0; i < 5; i++) {
            System.out.println(seq.generate());
        }
        
        seq = RandomVariateFactory.getInstance("Sequence", new Object[] {});
        for (int i = 0; i < 5; i++) {
            System.out.println(seq.generate());
        }
        
        System.out.println(RandomVariateFactory.getCache());
        
        System.out.println("Testing copy method for " + rand[0]);
        RandomVariate indCopy = RandomVariateFactory.getInstance(rand[0]);
        RandomVariate depCopy = RandomVariateFactory.getInstance(
            rand[0].getClass(), rand[0].getParameters(), 
            RandomNumberFactory.getInstance((RandomNumber) rand[0].getRandomNumber())
        );
        System.out.println("Independent Copy is: " + indCopy);
        System.out.println("Dependent) Copy is: " + depCopy);
        System.out.println("Live\t\t\tDependent\t\tIndependent");
        for (int i = 0; i < 4; ++i) {
            System.out.println(rand[0].generate() + "\t" + 
                depCopy.generate() + "\t" + indCopy.generate());
        }
    }

}
