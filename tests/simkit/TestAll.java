
package simkit;
import junit.framework.*;

public class TestAll extends TestCase {
    public static Test suite() {
        TestSuite suite = new TestSuite("TestAll");
        suite.addTest(new TestSuite(ListenerPatterns_IntegrationTest.class));
        suite.addTest(simkit.util.TestAll.suite());
        suite.addTest(simkit.smdx.TestAll.suite());
        suite.addTest(simkit.stat.TestAll.suite());
        suite.addTest(new TestSuite(VersionTest.class));
        suite.addTest(new TestSuite(SimEventTest.class));
        suite.addTest(new TestSuite(BasicSimEventSourceTest.class));
        suite.addTest(new TestSuite(EventListTest.class));
        suite.addTest(new TestSuite(PropertyChangeDispatcherTest.class));
        suite.addTest(new TestSuite(SimEntityBaseTest.class));
        suite.addTest(new TestSuite(JavaReflectionTest.class));
        suite.addTest(simkit.random.TestAll.suite());
        return suite;
    }
}
