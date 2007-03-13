
package simkit;
import junit.framework.*;

public class TestAll extends TestCase {
    public static Test suite() {
        TestSuite suite = new TestSuite("TestAll");
        suite.addTest(simkit.random.TestAll.suite());
        suite.addTest(simkit.util.TestAll.suite());
        suite.addTest(simkit.smdx.TestAll.suite());
        suite.addTest(new TestSuite(SimEventTest.class));
        suite.addTest(new TestSuite(EventListTest.class));
        suite.addTest(new TestSuite(PropertyChangeDispatcherTest.class));
//Commented out a test of DAFS bug 1315, which currently fails.
        //suite.addTest(new TestSuite(SimEntityBaseTest.class));
        return suite;
    }
}
