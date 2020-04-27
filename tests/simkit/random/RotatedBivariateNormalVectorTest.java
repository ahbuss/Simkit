package simkit.random;

import java.util.Arrays;
import junit.framework.TestCase;

/**
 * 
 * @author ahbuss
 */
public class RotatedBivariateNormalVectorTest extends TestCase {

    private RotatedBivariateNormalVector instance;

    public RotatedBivariateNormalVectorTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        instance = (RotatedBivariateNormalVector) RandomVectorFactory.getInstance("RotatedBivariateNormal");
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of generate method, of class RotatedBivariateNormalVector.
     */
    public void testGenerate() {
        System.out.println("generate");
        double[] expResult = new double[]{1.380640745626035, -0.19671948482240748};
        double[] result = instance.generate();
        System.out.println(Arrays.toString(result));
    }

    /**
     * Test of setParameters method, of class RotatedBivariateNormalVector.
     */
    public void testSetParameters() {
        System.out.println("setParameters");
        Object[] expResult = new Object[]{0.0, 0.0, 1.0, 1.0, 0.0};
        instance.setParameters();
        Object[] result = instance.getParameters();
        for (int i = 0; i < expResult.length; ++i) {
            assertEquals(expResult[i], result[i]);
        }

        double angle = 2.4;
        expResult = new Object[]{0.0, 0.0, 1.0, 1.0, angle};
        instance.setParameters(angle);
        result = instance.getParameters();
        for (int i = 0; i < expResult.length; ++i) {
            assertEquals(expResult[i], result[i]);
        }

        double[] stdDev = new double[]{3.4, 5.6};
        expResult = new Object[]{0.0, 0.0, stdDev[0], stdDev[1], angle};
        instance.setParameters(stdDev[0], stdDev[1], angle);
        result = instance.getParameters();
        for (int i = 0; i < expResult.length; ++i) {
            assertEquals(expResult[i], result[i]);
        }

        double[] mean = new double[]{-10.0, 3.5};
        expResult = new Object[]{mean[0], mean[1], stdDev[0], stdDev[1], 0.0};
        instance.setParameters(mean[0], mean[1], stdDev[0], stdDev[1]);
        result = instance.getParameters();
        for (int i = 0; i < expResult.length; ++i) {
            assertEquals(expResult[i], result[i]);
        }
        expResult[4] = angle;
        instance.setParameters(expResult);
        result = instance.getParameters();
        for (int i = 0; i < expResult.length; ++i) {
            assertEquals(expResult[i], result[i]);
        }

    }

    /**
     * Test of getParameters method, of class RotatedBivariateNormalVector.
     */
    public void testGetParameters() {
        System.out.println("getParameters");
        Object[] expResult = new Object[]{0.0, 0.0, 1.0, 1.0, 0.0};
        Object[] result = instance.getParameters();
        for (int i = 0; i < expResult.length; ++i) {
            assertEquals(expResult[i], result[i]);
        }

        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of setRandomNumber method, of class RotatedBivariateNormalVector.
     */
    public void testSetRandomNumber() {
        System.out.println("setRandomNumber");
        RandomNumber rng = new Tausworthe();
        instance.setRandomNumber(rng);
        assertEquals(rng, instance.getRandomNumber());
    }

    /**
     * Test of setMean method, of class RotatedBivariateNormalVector.
     */
    public void testSetMean_doubleArr() {
        System.out.println("setMean");
        double[] mean = new double[]{3.5, -10.2};
        instance.setMean(mean);
        for (int i = 0; i < mean.length; ++i) {
            assertEquals(mean[i], instance.getMean()[i]);
        }
        mean = new double[]{1.0};
        try {
            instance.setMean(mean);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
        mean = new double[]{};
        try {
            instance.setMean(mean);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
        mean = new double[]{1.0, 2.0, 3.0};
        try {
            instance.setMean(mean);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    /**
     * Test of setMean method, of class RotatedBivariateNormalVector.
     */
    public void testSetMean_int_double() {
        System.out.println("setMean");
        int index = 0;
        double mean = 25.0;
        instance.setMean(index, mean);
        assertEquals(mean, instance.getMean(index));
        index = 1;
        mean = -10.0;
        instance.setMean(index, mean);
        assertEquals(mean, instance.getMean(index));
        index = -1;
        try {
            instance.setMean(index, mean);
            fail("Should throw ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        index = 2;
        try {
            instance.setMean(index, mean);
            fail("Should throw ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
        }
    }

    /**
     * Test of setStandardDeviation method, of class
     * RotatedBivariateNormalVector.
     */
    public void testSetStandardDeviation_doubleArr() {
        System.out.println("setStandardDeviation");
        double[] standardDeviation = new double[]{1.2, 3.4};
        instance.setStandardDeviation(standardDeviation);
        for (int i = 0; i < standardDeviation.length; ++i) {
            assertEquals(standardDeviation[i], instance.getStandardDeviation()[i]);
        }

        standardDeviation = new double[0];
        try {
            instance.setStandardDeviation(standardDeviation);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
        standardDeviation = new double[3];
        try {
            instance.setStandardDeviation(standardDeviation);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of setStandardDeviation method, of class
     * RotatedBivariateNormalVector.
     */
    public void testSetStandardDeviation_int_double() {
        System.out.println("setStandardDeviation");
        int index = 0;
        double standardDeviation = 2.0;
        instance.setStandardDeviation(index, standardDeviation);
        assertEquals(standardDeviation, instance.getStandardDeviation(index));

        index = 1;
        standardDeviation = 2.3;
        instance.setStandardDeviation(index, standardDeviation);
        assertEquals(standardDeviation, instance.getStandardDeviation(index));

        index = 2;
        try {
            instance.setStandardDeviation(index, standardDeviation);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
        index = -1;
        try {
            instance.setStandardDeviation(index, standardDeviation);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }

        standardDeviation = -1.0;
        try {
            instance.setStandardDeviation(index, standardDeviation);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }

        standardDeviation = 0.0;
        index = 1;
        instance.setStandardDeviation(index, standardDeviation);
        index = 0;
        instance.setStandardDeviation(index, standardDeviation);

    }

    /**
     * Test of setAngle method, of class RotatedBivariateNormalVector.
     */
    public void testSetAngle() {
        System.out.println("setAngle");
        double angle = Math.PI;
        instance.setAngle(angle);
        assertEquals(Math.PI, instance.getAngle());
        angle = -0.5 * Math.PI;
        instance.setAngle(angle);
        assertEquals(-0.5 * Math.PI, instance.getAngle());
    }

}
