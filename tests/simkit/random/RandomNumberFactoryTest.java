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

    
    public void testGetInstance_String_Map() {
        String shortName;
        Map<String, Object> pmap;
        Logger log = Logger.getLogger("simkit.random");
        Level oldLevel = log.getLevel();
        log.setLevel(Level.OFF);
        
        // get a default one passing null as the map
        
        shortName = "Exponential";
        pmap = null;
        RandomVariate v = RandomVariateFactory.getInstance("Exponential", pmap);
//        System.out.println(v);
        
        assertSame(simkit.random.ExponentialVariate.class, v.getClass());
        assertEquals(1.0, v.getParameters()[0]);
        assertSame(simkit.random.MersenneTwister.class, v.getRandomNumber().getClass());

        // an empty, but not null parameter map
        pmap = new HashMap();
        v = RandomVariateFactory.getInstance("Exponential", pmap);
//        System.out.println(v);
        
        assertSame(simkit.random.ExponentialVariate.class, v.getClass());
        assertEquals(1.0, v.getParameters()[0]);
        assertSame(simkit.random.MersenneTwister.class, v.getRandomNumber().getClass());

        // partial map
        pmap = new HashMap();
        pmap.put("mean", 2.0);
        v = RandomVariateFactory.getInstance("Exponential", pmap);
//        System.out.println(v);
        
        assertSame(simkit.random.ExponentialVariate.class, v.getClass());
        assertEquals(2.0, v.getParameters()[0]);
        assertSame(simkit.random.MersenneTwister.class, v.getRandomNumber().getClass());

        // override the default class
        pmap = new HashMap();
        pmap.put("className", "simkit.random.Normal03Variate");
        v = RandomVariateFactory.getInstance("Normal", pmap);
//        System.out.println(v);
        
        assertSame(simkit.random.Normal03Variate.class, v.getClass());
        assertEquals(0.0, v.getParameters()[0]);
        assertEquals(1.0, v.getParameters()[1]);
        assertSame(simkit.random.MersenneTwister.class, v.getRandomNumber().getClass());
        
        
        // ok, one test of defaults for each entry in VariateDefaults.yaml
        // that we haven't used yet
        
        pmap = null;

        shortName = "Exponential";
        v = RandomVariateFactory.getInstance(shortName, pmap);
        assertSame(simkit.random.ExponentialVariate.class, v.getClass());
        assertEquals(1.0, v.getParameters()[0]);
        assertSame(simkit.random.MersenneTwister.class, v.getRandomNumber().getClass());

        shortName = "Uniform";
        v = RandomVariateFactory.getInstance(shortName, pmap);
        assertSame(simkit.random.UniformVariate.class, v.getClass());
        assertEquals(0.0, v.getParameters()[0]);
        assertEquals(1.0, v.getParameters()[1]);
        assertSame(simkit.random.MersenneTwister.class, v.getRandomNumber().getClass());

        shortName = "Weibull";
        v = RandomVariateFactory.getInstance(shortName, pmap);
        assertSame(simkit.random.WeibullVariate.class, v.getClass());
        assertEquals(1.0, v.getParameters()[0]);
        assertEquals(1.0, v.getParameters()[1]);
        assertSame(simkit.random.MersenneTwister.class, v.getRandomNumber().getClass());

        shortName = "Poisson";
        v = RandomVariateFactory.getInstance(shortName, pmap);
        assertSame(simkit.random.PoissonVariate.class, v.getClass());
        assertEquals(1.0, v.getParameters()[0]);
        assertSame(simkit.random.MersenneTwister.class, v.getRandomNumber().getClass());

        shortName = "Normal";
        v = RandomVariateFactory.getInstance(shortName, pmap);
        assertSame(simkit.random.NormalVariate.class, v.getClass());
        assertEquals(0.0, v.getParameters()[0]);
        assertEquals(1.0, v.getParameters()[1]);
        assertSame(simkit.random.MersenneTwister.class, v.getRandomNumber().getClass());

        shortName = "Lognormal";
        v = RandomVariateFactory.getInstance(shortName, pmap);
        assertSame(simkit.random.LogNormalVariate.class, v.getClass());
        assertEquals(0.0, v.getParameters()[0]);
        assertEquals(1.0, v.getParameters()[1]);
        assertSame(simkit.random.MersenneTwister.class, v.getRandomNumber().getClass());

        shortName = "Gamma";
        v = RandomVariateFactory.getInstance(shortName, pmap);
        assertSame(simkit.random.GammaVariate.class, v.getClass());
        assertEquals(1.0, v.getParameters()[0]);
        assertEquals(1.0, v.getParameters()[1]);
        assertSame(simkit.random.MersenneTwister.class, v.getRandomNumber().getClass());

        shortName = "Beta";
        v = RandomVariateFactory.getInstance(shortName, pmap);
        assertSame(simkit.random.BetaVariate.class, v.getClass());
        assertEquals(1.0, v.getParameters()[0]);
        assertEquals(1.0, v.getParameters()[1]);
        assertSame(simkit.random.MersenneTwister.class, v.getRandomNumber().getClass());

        shortName = "Geometric";
        v = RandomVariateFactory.getInstance(shortName, pmap);
        assertSame(simkit.random.GeometricVariate.class, v.getClass());
        assertEquals(1.0, v.getParameters()[0]);
        assertSame(simkit.random.MersenneTwister.class, v.getRandomNumber().getClass());

        shortName = "Constant";
        v = RandomVariateFactory.getInstance(shortName, pmap);
        assertSame(simkit.random.ConstantVariate.class, v.getClass());
        assertEquals(0.0, v.getParameters()[0]);
        assertSame(simkit.random.MersenneTwister.class, v.getRandomNumber().getClass());

        shortName = "OscillatingExponential";
        v = RandomVariateFactory.getInstance(shortName, pmap);
        assertSame(simkit.random.OscillatingExponentialVariate.class, v.getClass());
        assertEquals(1.0, v.getParameters()[0]);
        assertEquals(1.0, v.getParameters()[1]);
        assertEquals(1.0, v.getParameters()[2]);
        assertEquals(1.0, v.getParameters()[3]);
        assertSame(simkit.random.MersenneTwister.class, v.getRandomNumber().getClass());

        shortName = "Triangle";
        v = RandomVariateFactory.getInstance(shortName, pmap);
        assertSame(simkit.random.TriangleVariate.class, v.getClass());
        System.out.println(v);
        // the array is returned [left, right, center]
        assertEquals(-1.0, v.getParameters()[0]);
        assertEquals(1.0, v.getParameters()[1]);
        assertEquals(0.0, v.getParameters()[2]);
        
        assertSame(simkit.random.MersenneTwister.class, v.getRandomNumber().getClass());

        log.setLevel(oldLevel);

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
