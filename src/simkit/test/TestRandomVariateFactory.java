/*
 * MediatorFactory.java
 *
 * Created on March 14, 2002, 10:02 PM
 */

package simkit.test;
import simkit.random.*;
/**
 *
 * @author  Arnold Buss
 * @version 
 */
public class TestRandomVariateFactory {

    /** Creates new MediatorFactory */
    public TestRandomVariateFactory() {
    }

    /**
    * @param args the command line arguments
    */
    public static void main (String args[]) {
        
        Class[] classes = new Class[] {
            simkit.random.ExponentialVariate.class,
            simkit.random.NormalVariate.class,
            simkit.random.GammaVariate.class
        };
        
        String[] name = new String[] {
                "simkit.random.ExponentialVariate",
                "simkit.random.NormalVariate",
                "GammaVariate"
        };
        
        Object[][] param = new Object[][] {
            new Object[] { new Double(1.0) },
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
        RandomVariate seq = RandomVariateFactory.getInstance("SequenceVariate", new Object[] {});
        for (int i = 0; i < 10; i++) {
            System.out.println(seq.generate());
        }
    }

}
