
package simkit;
import junit.framework.*;

public class TestAll extends TestCase {
    public static Test suite() {
        TestSuite suite = new TestSuite("TestAll");
        suite.addTest(simkit.random.TestAll.suite());
        suite.addTest(simkit.util.TestAll.suite());
        return suite;
    }
}
