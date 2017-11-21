/*
 */
package simkit;

import java.util.SortedSet;
import junit.framework.TestCase;
import simkit.util.UnitTestEventList;

/**
 *
 * @author Kirk Stork (The MOVES Institute)
 */
public class PriorityTest extends TestCase {

    public PriorityTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }


    class EntityToStudy extends SimEntityBase {

    }
    
    public void testMe() {
        int id = Schedule.addNewEventList(UnitTestEventList.class);
        UnitTestEventList testEventList = (UnitTestEventList) Schedule.getEventList(id);
//        Schedule.setDefaultEventList(testEventList);
        
        SimEntity entity  = new EntityToStudy();
        entity.setEventListID(id);
        
        entity.waitDelay("FooDefault", 1.0);
        entity.waitDelay("FooHigh", 1.0, Priority.HIGH);
        entity.waitDelay("FooLow", 1.0, Priority.LOW);
        entity.waitDelay("FooLower", 1.0, Priority.LOWER);
        entity.waitDelay("FooHighest", 1.0, Priority.HIGHEST);
        entity.waitDelay("FooLowest", 1.0, Priority.LOWEST);
        entity.waitDelay("FooHumongous", 1.0, new Priority("Humongous", 100.0));
        entity.waitDelay("FooMiniscule", 1.0, new Priority("Miniscule", -100.0));

        SortedSet<SimEvent> events = testEventList.getScheduledEvents();
        
        SimEvent event;
        
        event = events.first();
        assertEquals("FooHighest", event.getName());
        events.remove(event);
        
        event = events.first();
        assertEquals("FooHumongous", event.getName());
        events.remove(event);
        
        event = events.first();
        assertEquals("FooHigh", event.getName());
        events.remove(event);
        
        event = events.first();
        assertEquals("FooDefault", event.getName());
        events.remove(event);
        
        event = events.first();
        assertEquals("FooLow", event.getName());
        events.remove(event);
        
        event = events.first();
        assertEquals("FooLower", event.getName());
        events.remove(event);
        
        event = events.first();
        assertEquals("FooMiniscule", event.getName());
        events.remove(event);
        
        event = events.first();
        assertEquals("FooLowest", event.getName());
        events.remove(event);
        
    }
}