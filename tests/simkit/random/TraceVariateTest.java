package simkit.random;

import junit.framework.TestCase;
import static org.junit.Assert.assertArrayEquals;

/**
 *
 * @author ahbuss
 */
public class TraceVariateTest extends TestCase {

    private static final double EPSILON = 1.0E-10;

    public TraceVariateTest(String testName) {
        super(testName);
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
     * Test of setParameters method, of class TraceVariate.
     */
    public void testSetParameters() {
        System.out.println("setParameters");
        double[] values = new double[]{2, 4, 6, 8};
        Object[] params = new Object[]{values};
        TraceVariate instance = new TraceVariate();
        instance.setParameters(params);
        Object[] result = instance.getParameters();
        assertArrayEquals(result, params);

        int expectedDefault = 3;
        instance.setParameters(values, expectedDefault);
        assertEquals(expectedDefault, instance.getDefaultValue(), EPSILON);

        params = new Object[]{"Hi Mom!"};
        try {
            instance.setParameters(params);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    /**
     * Test of generate method, of class TraceVariate.
     */
    public void testGenerate() {
        System.out.println("generate");
        TraceVariate instance = new TraceVariate();
        double[] values = new double[]{3, 2, 1};
        instance.setParameters(values);
        double expResult = values[0];
        double result = instance.generate();
        assertEquals(expResult, result, EPSILON);

        expResult = values[1];
        result = instance.generate();
        assertEquals(expResult, result, EPSILON);

        instance.getRandomNumber().resetSeed();

        for (int i = 0; i < values.length; ++i) {
            expResult = values[i];
            result = instance.generate();
            assertEquals(expResult, result, EPSILON);
        }

        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

}
