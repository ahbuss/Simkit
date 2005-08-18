
package simkit.smdx;
import junit.framework.*;

public class TestAll extends TestCase {
    public static Test suite() {
        TestSuite suite = new TestSuite("TestAll");
        suite.addTest(new TestSuite(CookieCutterMediatorTest.class));
        suite.addTest(new TestSuite(SensorTargetRefereeTest.class));
        return suite;
    }
}
