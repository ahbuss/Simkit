
package simkit.util;
import junit.framework.*;

public class TestAll extends TestCase {
    public static Test suite() {
        TestSuite suite = new TestSuite("TestAll");
        suite.addTest(new TestSuite(Hashtable2Test.class));
        suite.addTest(new TestSuite(JackcessTableReadTest.class));
        return suite;
    }
}
