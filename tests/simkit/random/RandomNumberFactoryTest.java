package simkit.random;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.TestCase;

/**
 * This test suite is incomplete.
 * 
 * @author Kirk Stork (The MOVES Institute)
 */
public class RandomNumberFactoryTest extends TestCase {
    
    public RandomNumberFactoryTest(String testName) {
        super(testName);
    }

    public void testGetCache() {
    }

    public void testSetDefaultClass() {
        
        RandomNumberFactory.setDefaultClass("simkit.random.MRG32k3a");
        RandomNumber r = RandomNumberFactory.getInstance();
        assertEquals(simkit.random.MRG32k3a.class, r.getClass());
        
        //put it back
        RandomNumberFactory.setDefaultClass(RandomNumberFactory.DEFAULT_CLASS);
    }

    public void testGetInstance_0args() {
        // if nothing else this documents the default rng
        assertEquals(simkit.random.MersenneTwister.class,
                RandomNumberFactory.getInstance().getClass());
    }

    public void testGetInstance_long() {
        // method is supposed to return an instance of the default
        // generator seeded with a long value.
        
        RandomNumber r = RandomNumberFactory.getInstance(42L);
        
        // should be the default rng
        assertEquals(simkit.random.MersenneTwister.class,
                r.getClass());
        
        // for the default Mersenne Twister, theis method just doesn't work
        try {
            r.getSeed();
            fail();
        } catch (UnsupportedOperationException e){}
        
        // for the default Mersenne Twister, theis method just doesn't work
        try {
            r.getSeeds();
            fail();
        } catch (UnsupportedOperationException e){}
        
        // TODO find a valid way to see if the seed was properly set
    }

    public void testGetInstance_longArr() {
        // method is supposed to return an instance of the default
        // generator seeded with a long value.
        
        RandomNumber r = RandomNumberFactory.getInstance(new long[]{42L});
        
        // should be the default rng
        assertEquals(simkit.random.MersenneTwister.class,
                r.getClass());
        
        // for the default Mersenne Twister, theis method just doesn't work
        try {
            r.getSeed();
            fail();
        } catch (UnsupportedOperationException e){}
        
        // for the default Mersenne Twister, theis method just doesn't work
        try {
            r.getSeeds();
            fail();
        } catch (UnsupportedOperationException e){}
        
        // TODO find a valid way to see if the seed was properly set
    }

    public void testGetInstance_String() {
        // other tests check proper instantiation of various generators by
        // string name.
    }

    public void testGetInstance_String_long() {
        RandomNumber r = RandomNumberFactory.getInstance("Congruential",42L);
        assertEquals(simkit.random.Congruential.class, r.getClass());
        assertEquals(42, r.getSeed());
    }

    public void testGetInstance_String_longArr() {
        RandomNumber r = RandomNumberFactory.getInstance("Congruential",
                new long[]{42L});
        assertEquals(simkit.random.Congruential.class, r.getClass());
        assertEquals(42, r.getSeed());
        
        r = RandomNumberFactory.getInstance("MRG32k3a",
                new long[]{1,2,3,4,5,6});
        assertEquals(simkit.random.MRG32k3a.class, r.getClass());
        assertTrue(Arrays.equals(new long[]{1,2,3,4,5,6}, r.getSeeds()));
        
    }

    public void testGetInstance_RandomNumber() {
    }

    public void testGetInstance_3args() {
    }

    

    public void testGetInstance_String_RandomNumberArr() {
    }

    public void testGetAntithetic() {
    }

    public void testAddSearchPackage() {
    }

    public void testRemoveSearchPackage() {
    }

    public void testGetSearchPackages() {
    }

    public void testGetClassFor() {
    }
    
    public void testBuildRNG_MT19937() {
        
    }
    public void testBuildRNG_RngStream() {
        // The defualt should be stream 0:0
        RngStream expected = new RngStream(0,0);
        RandomNumber r = RandomNumberFactory.getInstance("simkit.random.RngStream");
        assertTrue(Arrays.equals(expected.getSeeds(), r.getSeeds()));
        RngStream testStream = (RngStream)r;
        assertEquals(0, testStream.getStreamID());
        assertEquals(0, testStream.getSubstreamID());
    }

}
