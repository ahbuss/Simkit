package simkit.random;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.TestCase;

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
        RandomNumberFactory.setDefaultClass(RandomNumberFactory.DEFAULT_CLASS_NAME);
        r = RandomNumberFactory.getInstance();
        assertEquals(RandomNumberFactory.DEFAULT_CLASS.getName(), r.getClass().getName());
    }

    public void testGetInstance_0args() {
        // if nothing else this documents the default rng
        assertEquals(simkit.random.MersenneTwister.class,
                RandomNumberFactory.getInstance().getClass());
    }

//    public void testGetInstance_long_getSeed() {
//        // method is supposed to return an instance of the default
//        // generator seeded with a long value.
//
//        RandomNumber r = RandomNumberFactory.getInstance(42L);
//
//        // should be the default rng
//        assertEquals(simkit.random.MersenneTwister.class,
//                r.getClass());
//
//        // for the default Mersenne Twister, this method just doesn't work
//        try {
//            r.getSeed();
//            fail("Known bug - 1392");
//        } catch (UnsupportedOperationException e){}
//
//        // TODO find a valid way to see if the seed was properly set
//    }
//
//    public void testGetInstance_long_getSeeds() {
//        // method is supposed to return an instance of the default
//        // generator seeded with a long value.
//
//        RandomNumber r = RandomNumberFactory.getInstance(42L);
//
//        // should be the default rng
//        assertEquals(simkit.random.MersenneTwister.class,
//                r.getClass());
//
//        // for the default Mersenne Twister, this method just doesn't work
//        try {
//            r.getSeeds();
//            assertEquals(42L, r.getSeed());
//            fail("Known bug - 1392");
//        } catch (UnsupportedOperationException e){}
//
//        // TODO find a valid way to see if the seed was properly set
//    }
//
//    public void testGetInstance_longArr_getSeed() {
//        // method is supposed to return an instance of the default
//        // generator seeded with a long value.
//
//        RandomNumber r = RandomNumberFactory.getInstance(new long[]{42L});
//
//        // should be the default rng
//        assertEquals(simkit.random.MersenneTwister.class,
//                r.getClass());
//
//        // for the default Mersenne Twister, this method just doesn't work
//        try {
//            r.getSeed();
//            fail("Known bug - 1392");
//        } catch (UnsupportedOperationException e){}
//
//        // TODO find a valid way to see if the seed was properly set
//    }
//
//    public void testGetInstance_longArr_getSeeds() {
//        // method is supposed to return an instance of the default
//        // generator seeded with a long value.
//
//        RandomNumber r = RandomNumberFactory.getInstance(new long[]{42L});
//
//        // should be the default rng
//        assertEquals(simkit.random.MersenneTwister.class,
//                r.getClass());
//
//        // for the default Mersenne Twister, this method just doesn't work
//        try {
//            r.getSeeds();
//            fail("Known bug - 1392");
//        } catch (UnsupportedOperationException e){}
//
//        // TODO find a valid way to see if the seed was properly set
//    }

    public void testGetClassForString() {
        Class rngClass;
        Class theClass = RandomNumberFactory.getClassFor("Congruential");
//        System.out.println(theClass);
//        System.out.println(RandomNumberFactory.getCache());
        assertTrue(RandomNumberFactory.getCache().containsKey("Congruential"));
        rngClass = RandomNumberFactory.getCache().get("Congruential");
        assertSame(simkit.random.Congruential.class, theClass);
        assertSame(simkit.random.Congruential.class, rngClass);
        
        
        theClass = RandomNumberFactory.getClassFor("simkit.random.PooledGenerator");
//        System.out.println(theClass);
//        System.out.println(RandomNumberFactory.getCache());
        assertTrue(RandomNumberFactory.getCache().containsKey("simkit.random.PooledGenerator"));
        rngClass = RandomNumberFactory.getCache().get("simkit.random.PooledGenerator");
        assertSame(simkit.random.PooledGenerator.class, theClass);
        assertSame(simkit.random.PooledGenerator.class, rngClass);
        
        
        theClass = RandomNumberFactory.getClassFor("simkit.test.DummyGenerator");
//        System.out.println(theClass);
//        System.out.println(RandomNumberFactory.getCache());
        assertSame(simkit.test.DummyGenerator.class, theClass);
        assertTrue(RandomNumberFactory.getCache().containsKey("simkit.test.DummyGenerator"));
        rngClass = RandomNumberFactory.getCache().get("simkit.test.DummyGenerator");
        assertSame(simkit.test.DummyGenerator.class, rngClass);
        
        
        
        RandomNumberFactory.addSearchPackage("simkit.test");
        theClass = RandomNumberFactory.getClassFor("DummyGenerator");
//        System.out.println(theClass);
//        System.out.println(RandomNumberFactory.getCache());
        assertSame(simkit.test.DummyGenerator.class, theClass);
        assertTrue(RandomNumberFactory.getCache().containsKey("DummyGenerator"));
        rngClass = RandomNumberFactory.getCache().get("DummyGenerator");
        assertSame(simkit.test.DummyGenerator.class, rngClass);
        
        RandomNumberFactory.setDefaultClass("Tausworthe");
        RandomNumber rng = RandomNumberFactory.getInstance(CongruentialSeeds.SEED[0]);
        assertSame(simkit.random.Tausworthe.class, rng.getClass());
        
        RandomNumberFactory.setDefaultClass("DummyGenerator");
        rng = RandomNumberFactory.getInstance(CongruentialSeeds.SEED[0]);
        assertSame(simkit.test.DummyGenerator.class, rng.getClass());
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
