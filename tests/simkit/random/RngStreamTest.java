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
        
        testRng.resetNextSubstream();
        assertFalse(Arrays.equals(seed, testRng.getSeeds()));
        testRng.resetStartStream();
        assertTrue(Arrays.equals(seed, testRng.getSeeds()));
        assertEquals(a, testRng.drawLong());
        assertEquals(b, testRng.drawLong());
        assertEquals(c, testRng.drawLong());
        
    }

    /**
     * Test of resetStartSubstream method, of class RngStream.
     */
    public void testResetStartSubstream() {
        System.out.println("resetStartSubstream");
        RngStream testRng = new RngStream(1, 7);
        long seed[] = testRng.getSeeds();

        long a = testRng.drawLong();
        long b = testRng.drawLong();
        long c = testRng.drawLong();

        testRng.resetStartSubstream();

        assertTrue(Arrays.equals(seed, testRng.getSeeds()));
        assertEquals(a, testRng.drawLong());
        assertEquals(b, testRng.drawLong());
        assertEquals(c, testRng.drawLong());
    }

    /**
     * Test of resetNextSubstream method, of class RngStream.
     */
    public void testResetNextSubstream() {
        System.out.println("resetNextSubstream");
        RngStream testRng = new RngStream(1, 7);
        long seed[] = testRng.getSeeds();

        RngStream testRng2 = new RngStream(1, 0);

        for (int i = 0; i < 7; i++) {
            testRng2.resetNextSubstream();
        }

        assertTrue(Arrays.equals(seed, testRng2.getSeeds()));
    }

    /**
     * Test of setAntithetic method, of class RngStream.
     */
    public void testSetAntithetic() {
        System.out.println("setAntithetic");
        RngStream thetic = new RngStream(0,0);
        RngStream antithetic = new RngStream(0,0);
        antithetic.setAntithetic(true);
        
        double a = 1.0 - thetic.draw();
        double b = 1.0 - thetic.draw();
        double c = 1.0 - thetic.draw();
        
        assertEquals(a, antithetic.draw());
        assertEquals(b, antithetic.draw());
        assertEquals(c, antithetic.draw());

    }

    /**
     * Test of advanceState method, of class RngStream.
     */
    public void testAdvanceState() {
        //TODO implement
    }

    /**
     * Test of setSeed method, of class RngStream.
     */
    public void testSetSeed_longArr() {
        System.out.println("setSeed");
        long[] seed = {1, 2, 3, 4, 5, 6};
        boolean expResult = true;
        RngStream testRng = new RngStream(0, 0);
        boolean result = testRng.setSeed(seed);
        assertEquals(expResult, result);

        assertTrue(Arrays.equals(seed, testRng.getSeeds()));
    }

    /**
     * Test of randU01 method, of class RngStream.
     */
    public void testRandU01() {
//        TODO call out to die hard tests or something here?
//        System.out.println("randU01");
        assertTrue(true);
    }

    /**
     * Test of randInt method, of class RngStream.
     */
    public void testRandInt() {
//        TODO call out to die hard tests or something here?
//        System.out.println("randU01");
        assertTrue(true);
    }

    /**
     * Test of setSeed method, of class RngStream.
     */
    public void testSetSeed_long() {
        System.out.println("setSeed");
        long arg0 = 0L;
        RngStream instance = new RngStream();

        try {
            instance.setSeed(arg0);
            fail("Failed to throw UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertTrue(e.getMessage().contains("use setSeed("));
        }
    }


    /**
     * Test of setSeeds method, of class RngStream.
     */
    public void testSetAndCheckSeeds() {
        System.out.println("setSeeds");
        long[] seed = {1,2,3,4,5,6};
        RngStream instance = new RngStream();
        instance.setSeeds(seed);
        assertTrue(Arrays.equals(seed, instance.getSeeds()));
        
        seed =  new long[]{1,2,3,4,5};
        try {
            instance = new RngStream();
            instance.setSeeds(seed);
            fail("Failed to throw IllegalArgumentException");
        } catch (IllegalArgumentException e){}

        seed =  new long[]{1,2,3,4,5, -6};
        try {
            instance = new RngStream();
            instance.setSeeds(seed);
            fail("Failed to throw IllegalArgumentException");
        } catch (IllegalArgumentException e){}

        seed =  new long[]{-1,2,3,4,5,6};
        try {
            instance = new RngStream();
            instance.setSeeds(seed);
            fail("Failed to throw IllegalArgumentException");
        } catch (IllegalArgumentException e){}
        seed =  new long[]{0,0,0,4,5,6};
        try {
            instance = new RngStream();
            instance.setSeeds(seed);
            fail("Failed to throw IllegalArgumentException");
        } catch (IllegalArgumentException e){}
        seed =  new long[]{0,0,0,4,5,6};
        try {
            instance = new RngStream();
            instance.setSeeds(seed);
            fail("Failed to throw IllegalArgumentException");
        } catch (IllegalArgumentException e){}
        seed =  new long[]{4294967087L,0,0,4,5,6};
        try {
            instance = new RngStream();
            instance.setSeeds(seed);
            fail("Failed to throw IllegalArgumentException");
        } catch (IllegalArgumentException e){}
        seed =  new long[]{1,0,0,4,5,4294967087L};
        try {
            instance = new RngStream();
            instance.setSeeds(seed);
            fail("Failed to throw IllegalArgumentException");
        } catch (IllegalArgumentException e){}
        seed =  new long[]{4294944443L,0,0,4,5,6};
        try {
            instance = new RngStream();
            instance.setSeeds(seed);
        } catch (IllegalArgumentException e){
            fail("Improperly threw exception for good values");
        }
        seed =  new long[]{1,0,0,4,5,4294944443L};
        try {
            instance = new RngStream();
            instance.setSeeds(seed);
            fail("Failed to throw IllegalArgumentException");
        } catch (IllegalArgumentException e){}
   }


    /**
     * Test of getMultiplier method, of class RngStream.
     */
    public void testGetMultiplier() {
        System.out.println("getMultiplier");
        RngStream instance = new RngStream();
        try {
            instance.getMultiplier();
            fail("failed to throw exception");
        } catch (UnsupportedOperationException e) {
            
        } catch (Exception e) {
            fail("wrong exception thrown");
        }
    }
}
