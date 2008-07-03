
package simkit.random;
import junit.framework.*;

public class TestAll extends TestCase {
    public static Test suite() {
        TestSuite suite = new TestSuite("TestAll");
        suite.addTest(new TestSuite(RandomNumberFactoryTest.class));
        suite.addTest(new TestSuite(RandomVariateFactoryTest.class));
        suite.addTest(new TestSuite(MersenneTwisterFactoryTest.class));
        suite.addTest(new TestSuite(LogNormalTest.class));
        suite.addTest(new TestSuite(ConvolutionVariateTest.class));
        suite.addTest(new TestSuite(RngStreamTest.class));
        return suite;
    }
}
