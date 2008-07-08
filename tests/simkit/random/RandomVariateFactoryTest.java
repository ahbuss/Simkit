/*
 */
package simkit.random;

import junit.framework.TestCase;

/**
 *
 * @author Kirk Stork (The MOVES Institute)
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
                new Object[]{new Double(1.0)});
        assertSame(rand.getClass(), simkit.random.ExponentialVariate.class);
        assertEquals(rand.getParameters()[0], 1.0);

        // unqualified
        
        rand = RandomVariateFactory.getInstance(
                "GammaVariate",
                new Object[]{new Double(1.0),new Double(2.0)});
        assertSame(rand.getClass(), simkit.random.GammaVariate.class);
        assertEquals(rand.getParameters()[0], 1.0);
        assertEquals(rand.getParameters()[1], 2.0);

        // non-existant fully qualified
        
        try {
            rand = RandomVariateFactory.getInstance(
                    "simkit.random.NonexistantVariate",
                    new Object[]{new Double(1.0),new Double(2.0)});
            fail("Failed to throw exception for non-existant variate class");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("RandomVariate class not found for simkit.random.NonexistantVariate"));
        }
    
        assertSame(rand.getClass(), simkit.random.GammaVariate.class);

        // non-existant unqualified
        
        try {
            rand = RandomVariateFactory.getInstance(
                    "NonexistantVariate",
                    new Object[]{new Double(1.0),new Double(2.0)});
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
        rand.setParameters(new Object[]{new Double(42.0)});

        RandomVariate randClone = RandomVariateFactory.getInstance(rand);
        
        assertSame(rand.getClass(), randClone.getClass());
        assertSame(rand.getRandomNumber(), randClone.getRandomNumber());
        assertEquals(rand.getParameters()[0], randClone.getParameters()[0]);

    }
}
