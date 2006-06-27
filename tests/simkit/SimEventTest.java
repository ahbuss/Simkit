
package simkit;

import junit.framework.*;

public class SimEventTest extends TestCase {
    
    
    public void setUp() {
    }

    public void tearDown() {
    }

    public void testGetEventHash() {
        SimEntity entity2 = new simkit.smdx.CookieCutterMediator();
        SimEntity entity = new Adapter("heard", "passed");
        SimEvent e1 = new SimEvent(entity, "event",  new Object[] {"A string", entity2, new Integer(999)}, 1234.7);
        SimEvent e2 = new SimEvent(entity, "event",  new Object[] {"A string", entity2, new Integer(999)}, 223.4);
        assertEquals(e1.getEventHash(), e2.getEventHash());
        assertEquals(e1.getEventHash(), e2.getEventHash());//twice for the cache
    }
}
