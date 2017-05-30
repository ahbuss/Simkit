package simkit;

import java.util.List;
import junit.framework.TestCase;
import simkit.examples.ArrivalProcess;
import simkit.random.RandomVariateFactory;

/**
 *
 * @author ahbuss
 */
public class SimEntityFactoryTest extends TestCase {

    private SimEntity original;

    private SimEntity copy;

    public SimEntityFactoryTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        original = null;
        copy = null;
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        original = null;
        copy = null;
    }

    /**
     * Test of createCopies method, of class SimEntityFactory.
     */
    public void testCreateCopies() {
        System.out.println("createCopies");
        SimEntity original = null;
        int quantity = 0;
        List<SimEntity> expResult = null;
        List<SimEntity> result = SimEntityFactory.createCopies(original, quantity);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of createCopy method, of class SimEntityFactory.
     */
    public void testCreateCopy() {
        System.out.println("createCopy");
//        SimEntity original = null;
//        SimEntity expResult = null;
//        SimEntity result = SimEntityFactory.createCopy(original);
        original = new ArrivalProcess(RandomVariateFactory.getInstance("Exponential", 1.9));
        copy = SimEntityFactory.createCopy(original);

        assertEquals(original.getClass(), copy.getClass());
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    private static boolean areParametersEqual(Object[] originalParams, Object[] copyParams) {
        boolean equal = true;
        if (originalParams.length != copyParams.length) {
            equal = false;
        } else {
            for (int i = 0; i < originalParams.length; ++i) {
                if (originalParams[i] == null ^ copyParams[i] == null) {
                    continue;
                } else if (originalParams[i] != null) {
                    if (!originalParams[i].equals(copyParams[i])) {
                        equal = false;
                        break;
                    }
                }
            }

        }
        return equal;

    }
    
}
