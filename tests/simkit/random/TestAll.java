
package simkit.random;
import junit.framework.*;

public class TestAll extends TestCase {
    public static Test suite() {
        TestSuite suite = new TestSuite("TestAll");
        suite.addTest(new TestSuite(MersenneTwisterFactoryTest.class));
        suite.addTest(new TestSuite(LogNormalTest.class));
        return suite;
    }
}
