/*
 */
package simkit.random;

import java.util.HashMap;
import java.util.Map;
import junit.framework.TestCase;

/**
 * TODO: add some more stress tests and ones expected to fail
 *
 * @author Kirk Stork (The MOVES Institute)
 * @author ahbuss
 */
public class RandomVariateFactoryTest extends TestCase {

    public RandomVariateFactoryTest(String testName) {
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

    public void testGetInstance_String_Array() {
        RandomVariate rand = null;

        // fully qualified
        rand = RandomVariateFactory.getInstance(
                "simkit.random.ExponentialVariate",
                new Object[]{1.0});
        assertSame(rand.getClass(), simkit.random.ExponentialVariate.class);
        assertEquals(rand.getParameters()[0], 1.0);

        // unqualified
        rand = RandomVariateFactory.getInstance(
                "GammaVariate",
                new Object[]{1.0, 2.0});
        assertSame(rand.getClass(), simkit.random.GammaVariate.class);
        assertEquals(rand.getParameters()[0], 1.0);
        assertEquals(rand.getParameters()[1], 2.0);

        // non-existant fully qualified
        try {
            rand = RandomVariateFactory.getInstance(
                    "simkit.random.NonexistantVariate",
                    new Object[]{1.0, 2.0});
            fail("Failed to throw exception for non-existant variate class");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("RandomVariate class not found for simkit.random.NonexistantVariate"));
        }

        assertSame(rand.getClass(), simkit.random.GammaVariate.class);

        // non-existant unqualified
        try {
            rand = RandomVariateFactory.getInstance(
                    "NonexistantVariate",
                    new Object[]{1.0, 2.0});
            fail("Failed to throw exception for non-existant variate class");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("RandomVariate class not found for NonexistantVariate"));
        }

        assertSame(rand.getClass(), simkit.random.GammaVariate.class);
    }

    public void testGetInstance_String_Args() {
        RandomVariate rand = null;

        // fully qualified
        rand = RandomVariateFactory.getInstance("simkit.random.ExponentialVariate", 1.0);
        assertSame(rand.getClass(), simkit.random.ExponentialVariate.class);
        assertEquals(rand.getParameters()[0], 1.0);

        // unqualified
        rand = RandomVariateFactory.getInstance("GammaVariate", 1.0, 2.0);
        assertSame(rand.getClass(), simkit.random.GammaVariate.class);
        assertEquals(rand.getParameters()[0], 1.0);
        assertEquals(rand.getParameters()[1], 2.0);

        // non-existant fully qualified
        try {
            rand = RandomVariateFactory.getInstance("simkit.random.NonexistantVariate", 1.0, 2.0);
            fail("Failed to throw exception for non-existant variate class");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("RandomVariate class not found for simkit.random.NonexistantVariate"));
        }

        assertSame(rand.getClass(), simkit.random.GammaVariate.class);

        // non-existant unqualified
        try {
            rand = RandomVariateFactory.getInstance("NonexistantVariate", 1.0, 2.0);
            fail("Failed to throw exception for non-existant variate class");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("RandomVariate class not found for NonexistantVariate"));
        }

        assertSame(rand.getClass(), simkit.random.GammaVariate.class);
    }

    public void testGetInstance_Cloner() {
        RandomVariate rand = new simkit.random.ExponentialVariate();
        RandomNumber rng = new Tausworthe();
        rand.setParameters(new Object[]{42.0});

        RandomVariate randClone = RandomVariateFactory.getInstance(rand);

        assertSame(rand.getClass(), randClone.getClass());
        assertSame(rand.getRandomNumber(), randClone.getRandomNumber());
        assertEquals(rand.getParameters()[0], randClone.getParameters()[0]);

    }

    /**
     * Test of getInstance method, of class RandomVariateFactory.
     */
    public void testGetInstance_String_ObjectArr() {
        System.out.println("getInstance");
        String className = "Triangle";
        Object[] parameters = new Object[]{1.2, 4.5, 3.1};
        RandomVariate expResult = new TriangleVariate();
        expResult.setParameters(parameters);
        RandomVariate result = RandomVariateFactory.getInstance(className, parameters);
        assertEquals(expResult.getClass(), result.getClass());

        Object[] resultParams = result.getParameters();
        assertEquals(parameters.length, resultParams.length);
        for (int i = 0; i < parameters.length; ++i) {
            assertEquals(parameters[i], resultParams[i]);
        }

        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of getInstance method, of class RandomVariateFactory.
     */
    public void testGetInstance_3args() {
        System.out.println("getInstance");
        String className = "Beta";
        RandomNumber rng = new Mother();
        Object[] parameters = new Object[]{4.1, 2.3, 1.0, 3.5};
        RandomVariate expResult = new BetaVariate();
        expResult.setParameters(parameters);
        expResult.setRandomNumber(rng);

        RandomVariate result = RandomVariateFactory.getInstance(className, rng, parameters);
        assertEquals(expResult.getClass(), result.getClass());
        assertEquals(rng, result.getRandomNumber());
        Object[] resultParams = result.getParameters();
        assertEquals(parameters.length, resultParams.length);
        for (int i = 0; i < parameters.length; ++i) {
            assertEquals(parameters[i], resultParams[i]);
        }
    }

    /**
     * Test of getInstance method, of class RandomVariateFactory.
     */
    public void testGetInstance_RandomVariate() {
        System.out.println("getInstance");
        RandomVariate rv = new GammaVariate();
        Object[] params = new Object[]{3.4, 5.6};
        rv.setParameters(params);
        RandomVariate expResult = rv;
        RandomVariate result = RandomVariateFactory.getInstance(rv);
        assertEquals(expResult.getClass(), result.getClass());
        Object[] resultParams = result.getParameters();
        assertEquals(params.length, resultParams.length);
        for (int i = 0; i < params.length; ++i) {
            assertEquals(params[i], resultParams[i]);
        }
    }

    /**
     * Test of getInstance method, of class RandomVariateFactory.
     */
    public void testGetInstance_String() {
        System.out.println("getInstance");
        String toString = "Normal(2.3, 4.5)";
        RandomVariate expResult = RandomVariateFactory.getInstance("Normal", 2.3, 4.5);
        RandomVariate result = RandomVariateFactory.getInstance(toString);
        assertEquals(expResult.getClass(), result.getClass());
        Object[] expParams = expResult.getParameters();
        Object[] resultParams = result.getParameters();
        assertEquals(expParams.length, resultParams.length);
        for (int i = 0; i < expParams.length; ++i) {
            assertEquals(expParams[i], resultParams[i]);
        }
    }

    /**
     * Test of findFullyQualifiedNameFor method, of class RandomVariateFactory.
     */
    public void testFindFullyQualifiedNameFor() {
        System.out.println("findFullyQualifiedNameFor");
        String className = "NormalVariate";
        Class<? extends RandomVariate> expResult = NormalVariate.class;
        Class<?> result = RandomVariateFactory.findFullyQualifiedNameFor(className);
        assertEquals(expResult, result);

        className = "ConstantVariate";
        expResult = ConstantVariate.class;
        result = RandomVariateFactory.findFullyQualifiedNameFor(className);
        assertEquals(expResult, result);
        
        className = "FoobarVariate";
        result = RandomVariateFactory.findFullyQualifiedNameFor(className);
        assertNull(result);
    }

    /**
     * Test of getDiscreteRandomVariateInstance method, of class
     * RandomVariateFactory.
     */
    public void testGetDiscreteRandomVariateInstance_String_ObjectArr() {
        System.out.println("getDiscreteRandomVariateInstance");
        String className = "Binomial";
        Object[] params = new Object[]{4, 0.7};
        DiscreteRandomVariate expResult = new BinomialVariate();
        expResult.setParameters(params);
        DiscreteRandomVariate result = RandomVariateFactory.getDiscreteRandomVariateInstance(className, params);
        assertEquals(expResult.getClass(), result.getClass());
        Object[] resultParams = result.getParameters();
        assertEquals(params.length, resultParams.length);
        for (int i = 0; i < params.length; ++i) {
            assertEquals(params[i], resultParams[i]);
        }
    }

    /**
     * Test of getDiscreteRandomVariateInstance method, of class
     * RandomVariateFactory.
     */
    public void testGetDiscreteRandomVariateInstance_3args() {
        System.out.println("getDiscreteRandomVariateInstance");
        String className = "Bernoulli";
        RandomNumber rng = new MRG32k3a();
        Object[] params = new Object[]{0.6};
        DiscreteRandomVariate expResult = new BernoulliVariate();
        expResult.setParameters(params);
        expResult.setRandomNumber(rng);
        DiscreteRandomVariate result = RandomVariateFactory.getDiscreteRandomVariateInstance(className, rng, params);
        assertEquals(expResult.getClass(), result.getClass());
        assertEquals(expResult.getRandomNumber(), result.getRandomNumber());
        Object[] resultParams = result.getParameters();
        assertEquals(params.length, resultParams.length);
        for (int i = 0; i < params.length; ++i) {
            assertEquals(params[i], resultParams[i]);
        }
    }

    /**
     * Test of getInstance method, of class RandomVariateFactory.
     */
    public void testGetInstance_String_Map() {
        System.out.println("getInstance");
        String className = "Gamma";
        double alpha = 1.2;
        double beta = 3.4;
        Map<String, Object> params = new HashMap<>();
        params.put("alpha", alpha);
        params.put("beta", beta);
        RandomVariate expResult = new GammaVariate();

        expResult.setParameters(alpha, beta);
        RandomVariate result = RandomVariateFactory.getInstance(className, params);
        assertEquals(expResult.getClass(), result.getClass());

        Object[] expParams = expResult.getParameters();
        Object[] resultParams = result.getParameters();
        assertEquals(expParams.length, resultParams.length);
        for (int i = 0; i < expParams.length; ++i) {
            assertEquals(expParams[i], resultParams[i]);
        }

    }

    /**
     * Test of getUnconfiguredInstance method, of class RandomVariateFactory.
     */
    public void testGetUnconfiguredInstance() {
        System.out.println("getUnconfiguredInstance");
        String className = "Binomial";
        RandomVariate expResult = new BinomialVariate();
        RandomVariate result = RandomVariateFactory.getUnconfiguredInstance(className);
        assertEquals(expResult.getClass(), result.getClass());
    }
    
    public void testIsRandomVariateString() {
        System.out.println("isRandomVariateString");
        
        String testString = "Constant (1.0) ";
        boolean result = RandomVariateFactory.isRandomVariateString(testString);
        assertTrue(result);
        
        testString = "Foobar";
        result = RandomVariateFactory.isRandomVariateString(testString);
        assertFalse(result);
        
        
    }
}
