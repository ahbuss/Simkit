package simkit.random;

import junit.framework.TestCase;
import org.junit.Assert;
import static org.junit.Assert.assertArrayEquals;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

/**
 *
 * @author ahbuss
 */
public class IntegerTraceVariateTest extends TestCase {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    public IntegerTraceVariateTest(String testName) {
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
     * Test of setParameters method, of class IntegerTraceVariate.
     */
    public void testSetParameters() {
        System.out.println("setParameters");
        Object[] params = new Object[0];
        IntegerTraceVariate instance = new IntegerTraceVariate();
        exception.expect(IllegalArgumentException.class);
        boolean fail = true;
        try {
            instance.setParameters(params);
        } catch (IllegalArgumentException ex) {
            fail = false;
        }
        assertFalse(fail);

        fail = true;
        params = new Object[]{new double[]{}};
        try {
            instance.setParameters(params);
        } catch (IllegalArgumentException ex) {
            fail = false;
        }
        assertFalse(fail);

        params = new Object[]{new int[]{}, 3.1};
        try {
            instance.setParameters(params);
        } catch (IllegalArgumentException ex) {
            fail = false;
        }
        assertFalse(fail);

        params = new Object[]{new int[100]};
        instance.setParameters(params);
        System.out.println(instance);

    }

    /**
     * Test of generateInt method, of class IntegerTraceVariate.
     */
    public void testGenerateInt() {
        System.out.println("generateInt");
        int[] values = new int[]{5, 4, 3, 2, 1};
        IntegerTraceVariate instance = (IntegerTraceVariate) RandomVariateFactory.getDiscreteRandomVariateInstance("IntegerTrace", values);
//        System.out.println(instance);
        for (int i = 0; i < values.length; ++i) {
            int expResult = values[i];
            int result = instance.generateInt();
            assertEquals(expResult, result);
        }

        int expResult = Integer.MIN_VALUE;
        int result = instance.generateInt();
        assertEquals(expResult, result);

        instance.getRandomNumber().resetSeed();
        for (int i = 0; i < values.length; ++i) {
            expResult = values[i];
            result = instance.generateInt();
            assertEquals(expResult, result);
        }

        expResult = -1;
        instance.setParameters(values, expResult);
        result = instance.generateInt();
        assertEquals(expResult, result);

        Integer[] values2 = new Integer[]{2, 4, 5, 6, 7};
        int[] expResult2 = new int[]{2, 4, 5, 6, 7};
        instance.setParameters(new Object[] {values2});
        int[] result2 = instance.getTraceValues();
        assertArrayEquals(result2, expResult2);
    }

    /**
     * Test of convert method, of class IntegerTraceVariate.
     */
    public void testConvert() {
        System.out.println("convert");
        Integer[] values = new Integer[]{3, 5, 7, 9};
        int[] expResult = new int[]{3, 5, 7, 9};
        int[] result = IntegerTraceVariate.convert(values);
        assertArrayEquals(expResult, result);
    }

}
