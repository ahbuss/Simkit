package simkit.stat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import junit.framework.TestCase;

/**
 *
 * @author ahbuss
 */
public class QuantileTallyStatTest extends TestCase {
    
    private final Double[] data;

    public QuantileTallyStatTest(String testName) {
        super(testName);
        data = new Double[]{66.0, 91.0, 58.0, 90.0, 86.0, 64.0, 81.0, 74.0, 86.0, 57.0, 53.0, 53.0, 81.0};
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of reset method, of class QuantileTallyStat.
     */
    public void testReset() {
        System.out.println("reset");
        QuantileTallyStat instance = new QuantileTallyStat();
        for (double x : data) {
            instance.newObservation(x);
        }
        instance.reset();
        assertEquals(0, instance.getRawData().size());
    }

    /**
     * Test of newObservation method, of class QuantileTallyStat.
     */
    public void testNewObservation() {
        System.out.println("newObservation");
        QuantileTallyStat instance = new QuantileTallyStat();
        for (double x : data) {
            instance.newObservation(x);
        }
        assertEquals(data, data);
// TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of getRawData method, of class QuantileTallyStat.
     */
    public void testGetRawData() {
        System.out.println("getRawData");
        QuantileTallyStat instance = new QuantileTallyStat();
        List<Double> expResult = new ArrayList<>(Arrays.asList(data));
        for (double x : data) {
            instance.newObservation(x);
        }

        List<Double> result = instance.getRawData();
        assertEquals(expResult, result);
    }

    /**
     * Test of getQuantile method, of class QuantileTallyStat.
     */
    public void testGetQuantile() {
        System.out.println("getQuantile");
        double[] pVals = new double[]{0.0, 0.25, 0.50, 0.75, 0.80, 0.90, 1.0};
        double[] expected = new double[]{53.00, 57.67, 74.00, 86.00, 86.00, 90.33, 91.00};
        QuantileTallyStat instance = new QuantileTallyStat();
        for (double x : data) {
            instance.newObservation(x);
        }

        for (int i = 0; i < pVals.length; ++i) {
            double expResult = expected[i];
            double result = instance.getQuantile(pVals[i]);
            result = Math.round(result * 100) * 0.01;
            assertEquals(expResult, result, 0.0);
        }
    }

}
