package simkit.random;

import java.util.Arrays;
import java.util.List;
import junit.framework.TestCase;

/**
 *
 * @author Kirk Stork, The MOVES Institute, NPS
 * @version $Id:$
 */
public class DiscreteIntegerVariateTest extends TestCase {

    DiscreteIntegerVariate instance;

    public DiscreteIntegerVariateTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        instance = (DiscreteIntegerVariate)RandomVariateFactory
                .getUnconfiguredInstance("DiscreteIntegerVariate");
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testSetFrequencies() {
        double[] frequencies = new double[]{1, 1};
        int[] values = new int[]{1, 2};
        instance.setParameters(values, frequencies);
        double[] d = instance.getCdf();
        assertEquals(0.5, d[0], 1E-12);
        assertEquals(1.0, d[1], 1E-12);
    }

    public void testSetProportions() {
        double[] frequencies = new double[]{0.25, 0.75};
        int[] values = new int[]{1, 2};
        instance.setParameters(values, frequencies);
        double[] d = instance.getCdf();
        assertEquals(0.25, d[0], 1E-12);
        assertEquals(1.0, d[1], 1E-12);

//        int one = 0;
//        int two = 0;
//        for (int i=0; i < 1000; i++) {
//            int r = instance.generateInt();
//            if (1==r) {one++;}
//            if (2==r) {two++;}
//        }
//        System.out.println("One: "+ one);
//        System.out.println("Two: "+ two);
    }
}
