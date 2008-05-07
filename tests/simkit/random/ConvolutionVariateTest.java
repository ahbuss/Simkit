package simkit.random;

import java.util.Arrays;
import junit.framework.TestCase;


public class ConvolutionVariateTest extends TestCase {

    public static final double EPSILON = 1.0E-10;

    protected ConvolutionVariate conv;
    protected RandomVariate[] rvs;

    public void setUp() {
        conv = new ConvolutionVariate();
        rvs = new RandomVariate[2];
        rvs[0] = RandomVariateFactory.getInstance("Constant", 1.1);
        rvs[1] = RandomVariateFactory.getInstance("Constant", 2.2);
    }

    public void tearDown() {
        conv = null;
        rvs = null;
    }

    public void testGenerateDefault() {
        assertEquals(0.0, conv.generate(), 0.0);
    }

    public void testSetGetParameters() {
        conv.setParameters(new Object[] {rvs}); 
        Object[] params = conv.getParameters();
        assertEquals(1, params.length);
        assertTrue(Arrays.equals(rvs, (Object[])params[0]));
        assertTrue(Arrays.equals(rvs, (RandomVariate[])conv.getRandomVariates()));
        params = conv.getParameters();
        assertEquals(1, params.length);
        assertTrue(Arrays.equals(rvs, (Object[])params[0]));

        conv.setParameters(conv.getParameters());
    }
}
        
