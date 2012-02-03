package simkit;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import junit.framework.TestCase;
import utils.PropertyChangeAccumulator;

/**
 * These tests are currently incomplete. This is only to test to
 * see if DAFS Bug 1315 is really a simkit bug.
 **/
public class SimEntityBaseTest extends TestCase {

    public interface TheInterface {
    }

    public class GrandParent extends SimEntityBase implements TheInterface {

        protected List<Double> times = new ArrayList<Double>();

        public void doSomething(TheInterface i) {
            times.add(Schedule.getSimTime());
        }

        public String toString() {
            String ret = "GrandParent Processing Times: {";
            for (double time : times) {
                ret += time + " ";
            }
            ret += "}";
            return ret;
        }

        public int size() {
            return times.size();
        }
    }

    public class Parent extends GrandParent {

        protected int counter = 2;

        public void doRun() {
            schedule();
        }

        protected void schedule() {
        }

        public void doSomething(Parent p) {
            firePropertyChange("doSomething", counter, --counter);
            if (counter > 0) {
                schedule();
            }
        }
    }

    public class Child extends Parent {

        public void schedule() {
            waitDelay("Something", 1.0, new Object[]{this});
        }        //public void doSomething(Child c) {
        //   super.doSomething(c);
        //}
    }
    
    public static final Logger log = Logger.getLogger("simkit");

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
