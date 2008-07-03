/*
 */

package simkit.random;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.TestCase;

/**
 *
 * @author Kirk Stork (The MOVES Institute)
 */
public class RandomVariateFactoryTest extends TestCase {
    
    public RandomVariateFactoryTest(String testName) {
        super(testName);
    }

    public void testSetVerbose() {
    }

    public void testIsVerbose() {
    }

    public void testGetCache() {
    }

    public void testSetDefaultRandomNumber() {
    }

    public void testGetDefaultRandomNumber() {
    }

    public void testGetInstance_String_ObjectArr() {
    }

    public void testGetInstance_3args() {
    }

    public void testGetInstance_RandomVariate() {
    }

    public void testAddSearchPackage() {
    }

    public void testSetSearchPackages() {
    }

    public void testGetSearchPackages() {
    }

    public void testFindFullyQualifiedNameFor() {
    }

    public void testGetDiscreteRandomVariateInstance_String_ObjectArr() {
    }

    public void testGetDiscreteRandomVariateInstance_3args() {
    }

    public void testGetDefaultParameterTypes() {
    }

    public void testGetDefaultParameterValues() {
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

        shortName = "Triangle";
        pmap = new HashMap();
        pmap.put("left", -1.0);
        pmap.put("center", 0.0);
        pmap.put("right", 1.0);
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

}
