
package simkit;

import junit.framework.*;

import java.util.*;

public class SimEventTest extends TestCase {

    // A concrete entity class to test with
    class SimEventTest_TestEntity extends SimEntityBase {}
    
    protected SimEvent[] se;
    
    
    @Override
    public void setUp() {
        Schedule.coldReset();
    }

    @Override
    public void tearDown() {
        se = null;
    }

    protected void makeSimEvents() {
        SimEntityBase highPri = new SimEventTest_TestEntity();
        highPri.setPriority(Priority.HIGHER);
        SimEntityBase loPri = new SimEventTest_TestEntity();
        loPri.setPriority(Priority.DEFAULT);
        SimkitTestingHelper.setSimTime(0.0);
        
        se = new SimEvent[14];

        se[1] = loPri.waitDelay("1", 1.0, Priority.DEFAULT);
        se[2] = loPri.waitDelay("2", 1.0, Priority.HIGH);
        se[3] = highPri.waitDelay("3", 0.0, Priority.HIGH);
        se[4] = highPri.waitDelay("4", 0.0, Priority.DEFAULT);
        se[5] = loPri.waitDelay("5", 0.0, Priority.DEFAULT);
        se[6] = loPri.waitDelay("6", 0.0, Priority.HIGH);
        se[7] = highPri.waitDelay("7", 1.0, Priority.HIGH);
        se[8] = highPri.waitDelay("8", 1.0, Priority.DEFAULT);

        SimkitTestingHelper.setSimTime(1.0);

        se[9] = highPri.waitDelay("9", 0.0, Priority.HIGH);
        se[10] = loPri.waitDelay("10", 0.0, Priority.DEFAULT);
        se[11] = highPri.waitDelay("11", 0.0, Priority.DEFAULT);
        se[12] = loPri.waitDelay("12", 0.0, Priority.HIGH);
        se[13] = loPri.waitDelay("13", 0.0, Priority.HIGH);
    }

    public void testGetEventHash() {
        SimEntity entity2 = new SimEventTest_TestEntity();
        SimEntity entity = new Adapter("heard", "passed");
        SimEvent e1 = new SimEvent(entity, "event",  new Object[] {"A string", entity2, 999}, 1234.7);
        SimEvent e2 = new SimEvent(entity, "event",  new Object[] {"A string", entity2, 999}, 223.4);
        assertEquals(e1.getEventHash(), e2.getEventHash());
        assertEquals(e1.getEventHash(), e2.getEventHash());//twice for the cache
    }

/**
* This tests the sort order of SimEvents by putting them in the event list.
* It serves as a regression test for making SimEvents comparable.
**/
    public void testCompareToRegression() {
        makeSimEvents();
// The sort order should be 3, 6, 4, 5, 7, 9, 2, 12, 13, 8, 11, 1, 10

        int[] order = new int[] {999, 3, 6, 4, 5, 7, 9, 2, 12, 13, 8, 11, 1, 10}; 

        SortedSet<SimEvent> events = SimkitTestingHelper.getEventSet();

        int i = 1;
        for (SimEvent event : events) {
            assertSame("i=" + i + " ans index=" + order[i], se[order[i]], event);
            i++;
        }
        assertEquals(14, i);
    } 
}
