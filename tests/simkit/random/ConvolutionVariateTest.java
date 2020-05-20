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

    public void testGenerate() {
        conv.setParameters(new Object[]{rvs});
        assertEquals(3.3, conv.generate(), StrictMath.ulp(3.3));
    }

    public void testSetGetParameters() {
        System.out.println("setParameters");
        conv.setParameters(new Object[]{rvs});
        Object[] params = conv.getParameters();
        assertEquals(1, params.length);
        assertTrue(Arrays.equals(rvs, (Object[]) params[0]));
        assertTrue(Arrays.equals(rvs, (RandomVariate[]) conv.getRandomVariates()));
        params = conv.getParameters();
        assertEquals(1, params.length);
        assertTrue(Arrays.equals(rvs, (Object[]) params[0]));

        conv.setParameters(conv.getParameters());

        params = new Object[]{
            new String[]{
                "Exponential (1.5)",
                "Gamma (1.2, 3.4)",
                "Normal (1.5, 2.7"}
        };
        conv.setParameters(params);
        params = conv.getParameters();

        assertTrue(params[0] instanceof RandomVariate[]);
        for (RandomVariate rv : (RandomVariate[]) params[0]) {
            assertNotNull(rv);
        }
        assertTrue(((RandomVariate[])params[0])[0] instanceof ExponentialVariate);
        assertTrue(((RandomVariate[])params[0])[1] instanceof GammaVariate);
        assertTrue(((RandomVariate[])params[0])[2] instanceof NormalVariate);
    }

}
