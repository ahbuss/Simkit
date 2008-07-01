package simkit.random;

import java.util.Arrays;
import junit.framework.TestCase;

/**
 *
 * @author Kirk Stork (The MOVES Institute)
 */
public class RngStreamTest extends TestCase {
    
    public RngStreamTest(String testName) {
        super(testName);
    }            

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testSetPackageSeed() {
        System.out.println("setPackageSeed");
        long[] seed = {42, 42, 42, 42, 42, 42};
        boolean expResult = true;
        boolean result = RngStream.setPackageSeed(seed);
        assertEquals(expResult, result);
        
        RngStream testRng = new RngStream(0, 0);
        assertTrue(Arrays.equals(seed, testRng.getSeeds()));
        
        long a = testRng.drawLong();
        long b = testRng.drawLong();
        long c = testRng.drawLong();
        assertFalse(Arrays.equals(seed, testRng.getSeeds()));
        
        testRng.resetSeed();
        assertTrue(Arrays.equals(seed, testRng.getSeeds()));
        assertEquals(a, testRng.drawLong());
        assertEquals(b, testRng.drawLong());
        assertEquals(c, testRng.drawLong());
        
}

    /**
     * Test of resetStartStream method, of class RngStream.
     */
    public void testResetStartStream() {
        System.out.println("resetStartStream");
        long[] seed = {42, 42, 42, 42, 42, 42};
        boolean expResult = true;
        boolean result = RngStream.setPackageSeed(seed);
        assertEquals(expResult, result);
        
        RngStream testRng = new RngStream(0, 0);
        assertTrue(Arrays.equals(seed, testRng.getSeeds()));
        
        long a = testRng.drawLong();
        long b = testRng.drawLong();
        long c = testRng.drawLong();
        assertFalse(Arrays.equals(seed, testRng.getSeeds()));
        testRng.resetStartStream();
        assertTrue(Arrays.equals(seed, testRng.getSeeds()));
        assertEquals(a, testRng.drawLong());
        assertEquals(b, testRng.drawLong());
        assertEquals(c, testRng.drawLong());
    }
//
//    /**
//     * Test of resetStartSubstream method, of class RngStream.
//     */
//    public void testResetStartSubstream() {
//        System.out.println("resetStartSubstream");
//        RngStream instance = new RngStream();
//        instance.resetStartSubstream();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of resetNextSubstream method, of class RngStream.
//     */
//    public void testResetNextSubstream() {
//        System.out.println("resetNextSubstream");
//        RngStream instance = new RngStream();
//        instance.resetNextSubstream();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of setAntithetic method, of class RngStream.
//     */
//    public void testSetAntithetic() {
//        System.out.println("setAntithetic");
//        boolean a = false;
//        RngStream instance = new RngStream();
//        instance.setAntithetic(a);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of increasedPrecis method, of class RngStream.
//     */
//    public void testIncreasedPrecis() {
//        System.out.println("increasedPrecis");
//        boolean incp = false;
//        RngStream instance = new RngStream();
//        instance.increasedPrecis(incp);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of advanceState method, of class RngStream.
//     */
//    public void testAdvanceState() {
//        System.out.println("advanceState");
//        int e = 0;
//        int c = 0;
//        RngStream instance = new RngStream();
//        instance.advanceState(e, c);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of setSeed method, of class RngStream.
//     */
//    public void testSetSeed_longArr() {
//        System.out.println("setSeed");
//        long[] seed = null;
//        RngStream instance = new RngStream();
//        boolean expResult = false;
//        boolean result = instance.setSeed(seed);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getState method, of class RngStream.
//     */
//    public void testGetState() {
//        System.out.println("getState");
//        RngStream instance = new RngStream();
//        double[] expResult = null;
//        double[] result = instance.getState();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of writeState method, of class RngStream.
//     */
//    public void testWriteState() {
//        System.out.println("writeState");
//        RngStream instance = new RngStream();
//        instance.writeState();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of writeStateFull method, of class RngStream.
//     */
//    public void testWriteStateFull() {
//        System.out.println("writeStateFull");
//        RngStream instance = new RngStream();
//        instance.writeStateFull();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of randU01 method, of class RngStream.
//     */
//    public void testRandU01() {
//        System.out.println("randU01");
//        RngStream instance = new RngStream();
//        double expResult = 0.0;
//        double result = instance.randU01();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of randInt method, of class RngStream.
//     */
//    public void testRandInt() {
//        System.out.println("randInt");
//        int i = 0;
//        int j = 0;
//        RngStream instance = new RngStream();
//        int expResult = 0;
//        int result = instance.randInt(i, j);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of setSeed method, of class RngStream.
//     */
//    public void testSetSeed_long() {
//        System.out.println("setSeed");
//        long arg0 = 0L;
//        RngStream instance = new RngStream();
//        instance.setSeed(arg0);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getSeed method, of class RngStream.
//     */
//    public void testGetSeed() {
//        System.out.println("getSeed");
//        RngStream instance = new RngStream();
//        long expResult = 0L;
//        long result = instance.getSeed();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of resetSeed method, of class RngStream.
//     */
    public void testResetSeed() {
        System.out.println("resetSeed");
        long[] seed = {12345, 12345, 12345, 12345, 12345, 12345};
        boolean result = RngStream.setPackageSeed(seed);
        RngStream testRng = new RngStream(0, 0);
        
        long a = testRng.drawLong();
        long b = testRng.drawLong();
        long c = testRng.drawLong();
        
        testRng.resetSeed();
        System.out.println(Arrays.toString(testRng.getSeeds()));
        assertTrue(Arrays.equals(seed, testRng.getSeeds()));
        assertEquals(a, testRng.drawLong());
        assertEquals(b, testRng.drawLong());
        assertEquals(c, testRng.drawLong());
    }
//
//    /**
//     * Test of setSeeds method, of class RngStream.
//     */
//    public void testSetSeeds() {
//        System.out.println("setSeeds");
//        long[] seed = null;
//        RngStream instance = new RngStream();
//        instance.setSeeds(seed);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getSeeds method, of class RngStream.
//     */
//    public void testGetSeeds() {
//        System.out.println("getSeeds");
//        RngStream instance = new RngStream();
//        long[] expResult = null;
//        long[] result = instance.getSeeds();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of draw method, of class RngStream.
//     */
//    public void testDraw() {
//        System.out.println("draw");
//        RngStream instance = new RngStream();
//        double expResult = 0.0;
//        double result = instance.draw();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of drawLong method, of class RngStream.
//     */
//    public void testDrawLong() {
//        System.out.println("drawLong");
//        RngStream instance = new RngStream();
//        long expResult = 0L;
//        long result = instance.drawLong();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getMultiplier method, of class RngStream.
//     */
//    public void testGetMultiplier() {
//        System.out.println("getMultiplier");
//        RngStream instance = new RngStream();
//        double expResult = 0.0;
//        double result = instance.getMultiplier();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of toString method, of class RngStream.
//     */
//    public void testToString() {
//        System.out.println("toString");
//        RngStream instance = new RngStream();
//        String expResult = "";
//        String result = instance.toString();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

}
