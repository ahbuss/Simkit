package simkit.random;

import java.util.Arrays;
import junit.framework.TestCase;

/**
 *
 * @author ahbuss
 */
public class BivariateNormalVectorTest extends TestCase {

    public BivariateNormalVectorTest(String testName) {
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
     * Test of generate method, of class BivariateNormalVector.
     */
    public void testGenerate() {
        System.out.println("generate");
        RandomVector instance = 
                RandomVectorFactory.getInstance("BivariateNormalVector",
                        1.0, 1.5, 0.5, 1.1, 0.9);
        double[] expResult = null;
        double[] result = instance.generate();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setMean method, of class BivariateNormalVector.
     */
    public void testSetMean() {
        System.out.println("setMean");
        double[] mean = {0.0};
        BivariateNormalVector instance = new BivariateNormalVector();
        try {
            instance.setMean(mean);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }

        mean = new double[]{-1.0, 2.0};
        instance.setMean(mean);
        assertEquals(mean[0], instance.getMean()[0]);
        assertEquals(mean[1], instance.getMean()[1]);
    }

    /**
     * Test of setStandardDeviation method, of class BivariateNormalVector.
     */
    public void testSetStandardDeviation() {
        System.out.println("setStandardDeviation");
        double[] standardDeviation = {1.0, 1.0, 1.0};
        BivariateNormalVector instance = new BivariateNormalVector();
        try {
            instance.setStandardDeviation(standardDeviation);
            fail("Should have thrown exception for std dev "
                    + Arrays.toString(standardDeviation));
        } catch (IllegalArgumentException e) {
        }
        standardDeviation = new double[]{1.0, 0.0};
        instance.setStandardDeviation(standardDeviation);
        assertEquals(standardDeviation[0], instance.getStandardDeviation()[0]);
        assertEquals(standardDeviation[1], instance.getStandardDeviation()[1]);

        standardDeviation[0] = -0.1;
        try {
            instance.setStandardDeviation(standardDeviation);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    /**
     * Test of setCorrelation method, of class BivariateNormalVector.
     */
    public void testSetCorrelation() {
        System.out.println("setCorrelation");
        double correlation = 1.01;
        BivariateNormalVector instance = new BivariateNormalVector();
        try {
            instance.setCorrelation(correlation);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
        correlation = 0.5;
        instance.setCorrelation(correlation);
        assertEquals(correlation, instance.getCorrelation());

        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of setRandomNumber method, of class BivariateNormalVector.
     */
    public void testSetRandomNumber() {
        System.out.println("setRandomNumber");
        RandomNumber randomNumber = RandomNumberFactory.getInstance();
        BivariateNormalVector instance = new BivariateNormalVector();
        instance.setRandomNumber(randomNumber);
        assertSame(randomNumber, instance.getRandomNumber());
    }

    /**
     * Test of setParameters method, of class BivariateNormalVector.
     */
    public void testSetParameters() {
        System.out.println("setParameters");
        Object[] parameters = null;
        BivariateNormalVector instance = new BivariateNormalVector();

        parameters = new Object[0];
        try {
            instance.setParameters(parameters);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
        parameters = new Object[]{1.0, 2.0, 3.0, 4.0};
        try {
            instance.setParameters(parameters);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
        try {
            instance.setParameters(1.0, 2.0, 3.0, 4.0);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
        instance.setParameters(1.0, 2.0, 3.0, 4.0, 0.75);

        double[] mean = new double[3];
        double[] stdDev = new double[2];
        double corr = 0.9;

        try {
            instance.setParameters(mean, stdDev, corr);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
        mean = new double[]{1.0, 2.0};
        instance.setParameters(mean, stdDev, corr);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");

    }

    /**
     * Test of setStandardNormalsGenerator method, of class
     * BivariateNormalVector.
     */
    public void testSetStandardNormalsGenerator() {
        System.out.println("setStandardNormalsGenerator");
        RandomVariate[] standardNormalsGenerator = new RandomVariate[]{
            new NormalVariate(), new NormalVariate(), new NormalVariate(),};
        BivariateNormalVector instance = new BivariateNormalVector();
        try {
            instance.setStandardNormalsGenerator(standardNormalsGenerator);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
        standardNormalsGenerator = new RandomVariate[]{
            new NormalVariate(), new NormalVariate()};
        instance.setStandardNormalsGenerator(standardNormalsGenerator);
    }

}
