
package simkit;

import junit.framework.*;

import java.beans.*;
import java.util.*;
import java.util.logging.*;

import utils.*;
/**
* These tests are currently incomplete. This is only to test to
* see if DAFS Bug 1315 is really a simkit bug.
**/
public class SimEntityBaseTest extends TestCase {

    public static Logger log = Logger.getLogger("simkit");

    public void setUp() {
        Schedule.coldReset();
    }

    public void tearDown() {
    }

    public void testDAFS1315() {
        Child child = new Child();
        PropertyChangeAccumulator acc = new PropertyChangeAccumulator();
        acc.addExpected(new PropertyChangeEvent(child, "doSomething", 2, 1));
        acc.addExpected(new PropertyChangeEvent(child, "doSomething", 1, 0));
        child.addPropertyChangeListener(acc);

        System.out.println("Start testDAFS1315");
        Schedule.setReallyVerbose(true);
        Schedule.reset();
        Schedule.setReallyVerbose(true);
        Schedule.startSimulation();

        assertEquals(child.toString(), 0, child.size());
        acc.done();
    }
}
