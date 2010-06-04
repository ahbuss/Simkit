
package simkit;

import junit.framework.*;

import java.util.*;
import java.util.logging.*;
import simkit.smdx.Contact;
import simkit.smdx.CookieCutterMediator;
import simkit.smdx.Mover;
import simkit.smdx.Sensor;
import simkit.smdx.SensorContact;


/**
* These tests are currently incomplete. 
**/
public class EventListTest extends TestCase {

    public class EventListTestException extends RuntimeException {
    }

    public static Logger log = Logger.getLogger("simkit");

    protected EventList eventList;

    protected SimEntity entity1;
    protected SimEntity entity2;
    protected SimEntity entity3;
    protected SimEntity entity4;
    protected SimEntity entity5;

    protected SimEvent event1;
    protected SimEvent event2;
    protected SimEvent event3;
    protected SimEvent event4;
    protected SimEvent event5;

    protected SimEvent event1a;
    protected SimEvent event2a;
    protected SimEvent event3a;
    protected SimEvent event4a;
    protected SimEvent event5a;

    protected SimpleEventListener  listener;

        static class TestMediator extends CookieCutterMediator {

        @Override
        protected void targetIsEnteringSensorRange(Sensor sensor, Mover target){
            System.out.println(target.toString() + " is entering range of " + sensor.toString());
            System.out.println("TestMediator does no special processing before scheduling detections");
        }
        
        @Override
        protected void targetIsExitingSensorRange(Sensor sensor, Mover target){
            System.out.println(target.toString() + " is exitng range of " + sensor.toString());
            System.out.println("TestMediator does no special processing before scheduling undetections");
        }
        
        @Override
        protected Contact getContactForEnterRangeEvent(Sensor sensor, Mover target) {
           Contact contact = contacts.get(target);
            if (contact == null) {
                contact = new SensorContact((Mover)target);
                contacts.put(target, contact);
            }
            System.out.println("TestMediator providing contact " + 
                    contact.toString() + " for target " + target.toString());
            return contact;
        }
    }

    public void setUp() {
        Schedule.coldReset();
        // This test assumes the BasicEventList implementation
        // is simkit.EventList
        eventList = (EventList)Schedule.getDefaultEventList();
        entity1 = new Adapter("heard", "passed");
        entity2 = new Bridge("heard", "sent");
        entity3 = new TestMediator();
        entity4 = new EventCounter();
        entity5 = new SimEventFilter();

        event1 = new SimEvent(entity1, "This", 1.0);
        event2 = new SimEvent(entity1, "This", new Object[] {entity2}, 1.0);
        event3 = new SimEvent(entity3, "That", 0.5);
        event4 = new SimEvent(entity3, "TheOther", new Object[] {new Integer(1), null}, 1.0);
        event5 = new SimEvent(entity5, "Another", 2.0);

        event1a = new SimEvent(entity1, "This", 11.0);
        event2a = new SimEvent(entity1, "This", new Object[] {entity2}, 11.0);
        event3a = new SimEvent(entity3, "That", 10.5);
        event4a = new SimEvent(entity3, "TheOther", new Object[] {new Integer(1), null}, 11.0);
        event5a = new SimEvent(entity5, "Another", 12.0);

        listener = new SimpleEventListener();
        entity1.addSimEventListener(listener);
        entity2.addSimEventListener(listener);
        entity3.addSimEventListener(listener);
        entity4.addSimEventListener(listener);
        entity5.addSimEventListener(listener);
    }

    public void tearDown() {
        eventList = null;
        entity1 = null;
        entity2 = null;
        entity3 = null;
        entity4 = null;
        entity5 = null;

        event1 = null;
        event2 = null;
        event3 = null;
        event4 = null;
        event5 = null;

        event1a = null;
        event2a = null;
        event3a = null;
        event4a = null;
        event5a = null;

        listener = null;
    }

    public void testEntityEventMap() {
        log.info("Starting testEntityEventMap");
        eventList.setFastInterrupts(false);
        try {
            eventList.addToEntityEventMap(event1);
            fail("Should have thrown InvalidSchedulingException");
        } catch (InvalidSchedulingException e) {
        }
        eventList.setFastInterrupts(true);
        assertTrue(eventList.addToEntityEventMap(event1));
        Set events = (Set)eventList.entityEventMap.get(entity1);
        assertEquals(1, events.size());
        assertTrue(events.contains(event1));
        assertFalse(events.contains(event2));
        assertFalse(events.contains(event3));
        assertFalse(events.contains(event4));
        assertFalse(events.contains(event5));
        events = (Set)eventList.entityEventMap.get(entity2);
        assertNull(events);
        events = (Set)eventList.entityEventMap.get(entity3);
        assertNull(events);
        events = (Set)eventList.entityEventMap.get(entity4);
        assertNull(events);
        events = (Set)eventList.entityEventMap.get(entity5);
        assertNull(events);

        assertTrue(eventList.addToEntityEventMap(event2));
        events = (Set)eventList.entityEventMap.get(entity1);
        assertEquals(2, events.size());
        assertTrue(events.contains(event1));
        assertTrue(events.contains(event2));
        assertFalse(events.contains(event3));
        assertFalse(events.contains(event4));
        assertFalse(events.contains(event5));
        events = (Set)eventList.entityEventMap.get(entity2);
        assertNull(events);
        events = (Set)eventList.entityEventMap.get(entity3);
        assertNull(events);
        events = (Set)eventList.entityEventMap.get(entity4);
        assertNull(events);
        events = (Set)eventList.entityEventMap.get(entity5);
        assertNull(events);

        assertTrue(eventList.addToEntityEventMap(event3));
        events = (Set)eventList.entityEventMap.get(entity1);
        assertEquals(2, events.size());
        assertTrue(events.contains(event1));
        assertTrue(events.contains(event2));
        assertFalse(events.contains(event3));
        assertFalse(events.contains(event4));
        assertFalse(events.contains(event5));
        events = (Set)eventList.entityEventMap.get(entity2);
        assertNull(events);
        events = (Set)eventList.entityEventMap.get(entity3);
        assertEquals(1, events.size());
        assertFalse(events.contains(event1));
        assertFalse(events.contains(event2));
        assertTrue(events.contains(event3));
        assertFalse(events.contains(event4));
        assertFalse(events.contains(event5));
        events = (Set)eventList.entityEventMap.get(entity4);
        assertNull(events);
        events = (Set)eventList.entityEventMap.get(entity5);
        assertNull(events);

        assertTrue(eventList.addToEntityEventMap(event4));
        events = (Set)eventList.entityEventMap.get(entity1);
        assertEquals(2, events.size());
        assertTrue(events.contains(event1));
        assertTrue(events.contains(event2));
        assertFalse(events.contains(event3));
        assertFalse(events.contains(event4));
        assertFalse(events.contains(event5));
        events = (Set)eventList.entityEventMap.get(entity2);
        assertNull(events);
        events = (Set)eventList.entityEventMap.get(entity3);
        assertEquals(2, events.size());
        assertFalse(events.contains(event1));
        assertFalse(events.contains(event2));
        assertTrue(events.contains(event3));
        assertTrue(events.contains(event4));
        assertFalse(events.contains(event5));
        events = (Set)eventList.entityEventMap.get(entity4);
        assertNull(events);
        events = (Set)eventList.entityEventMap.get(entity5);
        assertNull(events);

        assertTrue(eventList.addToEntityEventMap(event5));
        events = (Set)eventList.entityEventMap.get(entity1);
        assertEquals(2, events.size());
        assertTrue(events.contains(event1));
        assertTrue(events.contains(event2));
        assertFalse(events.contains(event3));
        assertFalse(events.contains(event4));
        assertFalse(events.contains(event5));
        events = (Set)eventList.entityEventMap.get(entity2);
        assertNull(events);
        events = (Set)eventList.entityEventMap.get(entity3);
        assertEquals(2, events.size());
        assertFalse(events.contains(event1));
        assertFalse(events.contains(event2));
        assertTrue(events.contains(event3));
        assertTrue(events.contains(event4));
        assertFalse(events.contains(event5));
        events = (Set)eventList.entityEventMap.get(entity4);
        assertNull(events);
        events = (Set)eventList.entityEventMap.get(entity5);
        assertEquals(1, events.size());
        assertFalse(events.contains(event1));
        assertFalse(events.contains(event2));
        assertFalse(events.contains(event3));
        assertFalse(events.contains(event4));
        assertTrue(events.contains(event5));
//Try to put in a duplicate event.
        assertFalse(eventList.addToEntityEventMap(event5));
        events = (Set)eventList.entityEventMap.get(entity1);
        assertEquals(2, events.size());
        assertTrue(events.contains(event1));
        assertTrue(events.contains(event2));
        assertFalse(events.contains(event3));
        assertFalse(events.contains(event4));
        assertFalse(events.contains(event5));
        events = (Set)eventList.entityEventMap.get(entity2);
        assertNull(events);
        events = (Set)eventList.entityEventMap.get(entity3);
        assertEquals(2, events.size());
        assertFalse(events.contains(event1));
        assertFalse(events.contains(event2));
        assertTrue(events.contains(event3));
        assertTrue(events.contains(event4));
        assertFalse(events.contains(event5));
        events = (Set)eventList.entityEventMap.get(entity4);
        assertNull(events);
        events = (Set)eventList.entityEventMap.get(entity5);
        assertEquals(1, events.size());
        assertFalse(events.contains(event1));
        assertFalse(events.contains(event2));
        assertFalse(events.contains(event3));
        assertFalse(events.contains(event4));
        assertTrue(events.contains(event5));

        assertTrue(eventList.removeFromEntityEventMap(event3));
        events = (Set)eventList.entityEventMap.get(entity1);
        assertEquals(2, events.size());
        assertTrue(events.contains(event1));
        assertTrue(events.contains(event2));
        assertFalse(events.contains(event3));
        assertFalse(events.contains(event4));
        assertFalse(events.contains(event5));
        events = (Set)eventList.entityEventMap.get(entity2);
        assertNull(events);
        events = (Set)eventList.entityEventMap.get(entity3);
        assertEquals(1, events.size());
        assertFalse(events.contains(event1));
        assertFalse(events.contains(event2));
        assertFalse(events.contains(event3));
        assertTrue(events.contains(event4));
        assertFalse(events.contains(event5));
        events = (Set)eventList.entityEventMap.get(entity4);
        assertNull(events);
        events = (Set)eventList.entityEventMap.get(entity5);
        assertEquals(1, events.size());
        assertFalse(events.contains(event1));
        assertFalse(events.contains(event2));
        assertFalse(events.contains(event3));
        assertFalse(events.contains(event4));
        assertTrue(events.contains(event5));

        assertTrue(eventList.removeFromEntityEventMap(event5));
        events = (Set)eventList.entityEventMap.get(entity1);
        assertEquals(2, events.size());
        assertTrue(events.contains(event1));
        assertTrue(events.contains(event2));
        assertFalse(events.contains(event3));
        assertFalse(events.contains(event4));
        assertFalse(events.contains(event5));
        events = (Set)eventList.entityEventMap.get(entity2);
        assertNull(events);
        events = (Set)eventList.entityEventMap.get(entity3);
        assertEquals(1, events.size());
        assertFalse(events.contains(event1));
        assertFalse(events.contains(event2));
        assertFalse(events.contains(event3));
        assertTrue(events.contains(event4));
        assertFalse(events.contains(event5));
        events = (Set)eventList.entityEventMap.get(entity4);
        assertNull(events);
        events = (Set)eventList.entityEventMap.get(entity5);
        assertEquals(0, events.size());

//Try to remove it again.
        assertFalse(eventList.removeFromEntityEventMap(event5));
        events = (Set)eventList.entityEventMap.get(entity1);
        assertEquals(2, events.size());
        assertTrue(events.contains(event1));
        assertTrue(events.contains(event2));
        assertFalse(events.contains(event3));
        assertFalse(events.contains(event4));
        assertFalse(events.contains(event5));
        events = (Set)eventList.entityEventMap.get(entity2);
        assertNull(events);
        events = (Set)eventList.entityEventMap.get(entity3);
        assertEquals(1, events.size());
        assertFalse(events.contains(event1));
        assertFalse(events.contains(event2));
        assertFalse(events.contains(event3));
        assertTrue(events.contains(event4));
        assertFalse(events.contains(event5));
        events = (Set)eventList.entityEventMap.get(entity4);
        assertNull(events);
        events = (Set)eventList.entityEventMap.get(entity5);
        assertEquals(0, events.size());

        assertTrue(eventList.removeFromEntityEventMap(event1));
        events = (Set)eventList.entityEventMap.get(entity1);
        assertEquals(1, events.size());
        assertFalse(events.contains(event1));
        assertTrue(events.contains(event2));
        assertFalse(events.contains(event3));
        assertFalse(events.contains(event4));
        assertFalse(events.contains(event5));
        events = (Set)eventList.entityEventMap.get(entity2);
        assertNull(events);
        events = (Set)eventList.entityEventMap.get(entity3);
        assertEquals(1, events.size());
        assertFalse(events.contains(event1));
        assertFalse(events.contains(event2));
        assertFalse(events.contains(event3));
        assertTrue(events.contains(event4));
        assertFalse(events.contains(event5));
        events = (Set)eventList.entityEventMap.get(entity4);
        assertNull(events);
        events = (Set)eventList.entityEventMap.get(entity5);
        assertEquals(0, events.size());

        assertTrue(eventList.removeFromEntityEventMap(event2));
        events = (Set)eventList.entityEventMap.get(entity1);
        assertEquals(0, events.size());
        events = (Set)eventList.entityEventMap.get(entity2);
        assertNull(events);
        events = (Set)eventList.entityEventMap.get(entity3);
        assertEquals(1, events.size());
        assertFalse(events.contains(event1));
        assertFalse(events.contains(event2));
        assertFalse(events.contains(event3));
        assertTrue(events.contains(event4));
        assertFalse(events.contains(event5));
        events = (Set)eventList.entityEventMap.get(entity4);
        assertNull(events);
        events = (Set)eventList.entityEventMap.get(entity5);
        assertEquals(0, events.size());

        assertTrue(eventList.removeFromEntityEventMap(event4));
        events = (Set)eventList.entityEventMap.get(entity1);
        assertEquals(0, events.size());
        events = (Set)eventList.entityEventMap.get(entity2);
        assertNull(events);
        events = (Set)eventList.entityEventMap.get(entity3);
        assertEquals(0, events.size());
        events = (Set)eventList.entityEventMap.get(entity4);
        assertNull(events);
        events = (Set)eventList.entityEventMap.get(entity5);
        assertEquals(0, events.size());
    }

    public void testHashEventMap() {
        log.info("Starting testHashEventMap");
        eventList.setFastInterrupts(false);
        try {
            eventList.addToHashEventMap(event1);
            fail("Should have thrown InvalidSchedulingException");
        } catch (InvalidSchedulingException e) {
        }
        eventList.setFastInterrupts(true);
        assertTrue(eventList.addToHashEventMap(event1));
        Collection eventSets = (Collection)eventList.hashEventMap.values();
        Set events = new TreeSet();
        for (Iterator itt = eventSets.iterator(); itt.hasNext();) {
            boolean addAll = events.addAll((Set) itt.next());
        }
        assertEquals("events=" + events, 1, events.size());
        assertTrue(events.contains(event1));
        assertFalse(events.contains(event2));
        assertFalse(events.contains(event3));
        assertFalse(events.contains(event4));
        assertFalse(events.contains(event5));

//Try to add duplicate event.
        assertFalse(eventList.addToHashEventMap(event1));
        eventSets = (Collection)eventList.hashEventMap.values();
        events = new TreeSet();
        for (Iterator itt = eventSets.iterator(); itt.hasNext();) {
            boolean addAll = events.addAll((Set) itt.next());
        }
        assertEquals(1, events.size());
        assertTrue(events.contains(event1));
        assertFalse(events.contains(event2));
        assertFalse(events.contains(event3));
        assertFalse(events.contains(event4));
        assertFalse(events.contains(event5));

//Try to remove one that has never been added
        assertFalse(eventList.removeFromHashEventMap(event5));
        eventSets = (Collection)eventList.hashEventMap.values();
        events = new TreeSet();
        for (Iterator itt = eventSets.iterator(); itt.hasNext();) {
            events.addAll((Set)itt.next());
        }
        assertEquals(1, events.size());
        assertTrue(events.contains(event1));
        assertFalse(events.contains(event2));
        assertFalse(events.contains(event3));
        assertFalse(events.contains(event4));
        assertFalse(events.contains(event5));

        assertTrue(eventList.addToHashEventMap(event2));
        eventSets = (Collection)eventList.hashEventMap.values();
        events = new TreeSet();
        for (Iterator itt = eventSets.iterator(); itt.hasNext();) {
            events.addAll((Set)itt.next());
        }
        assertEquals(2, events.size());
        assertTrue(events.contains(event1));
        assertTrue(events.contains(event2));
        assertFalse(events.contains(event3));
        assertFalse(events.contains(event4));
        assertFalse(events.contains(event5));

        assertTrue(eventList.addToHashEventMap(event3));
        eventSets = (Collection)eventList.hashEventMap.values();
        events = new TreeSet();
        for (Iterator itt = eventSets.iterator(); itt.hasNext();) {
            events.addAll((Set)itt.next());
        }
        assertEquals(3, events.size());
        assertTrue(events.contains(event1));
        assertTrue(events.contains(event2));
        assertTrue(events.contains(event3));
        assertFalse(events.contains(event4));
        assertFalse(events.contains(event5));

        assertTrue(eventList.addToHashEventMap(event4));
        eventSets = (Collection)eventList.hashEventMap.values();
        events = new TreeSet();
        for (Iterator itt = eventSets.iterator(); itt.hasNext();) {
            events.addAll((Set)itt.next());
        }
        assertEquals(4, events.size());
        assertTrue(events.contains(event1));
        assertTrue(events.contains(event2));
        assertTrue(events.contains(event3));
        assertTrue(events.contains(event4));
        assertFalse(events.contains(event5));

        assertTrue(eventList.addToHashEventMap(event5));
        eventSets = (Collection)eventList.hashEventMap.values();
        events = new TreeSet();
        for (Iterator itt = eventSets.iterator(); itt.hasNext();) {
            events.addAll((Set)itt.next());
        }
        assertEquals(5, events.size());
        assertTrue(events.contains(event1));
        assertTrue(events.contains(event2));
        assertTrue(events.contains(event3));
        assertTrue(events.contains(event4));
        assertTrue(events.contains(event5));

        assertTrue(eventList.removeFromHashEventMap(event5));
        eventSets = (Collection)eventList.hashEventMap.values();
        events = new TreeSet();
        for (Iterator itt = eventSets.iterator(); itt.hasNext();) {
            events.addAll((Set)itt.next());
        }
        assertEquals(4, events.size());
        assertTrue(events.contains(event1));
        assertTrue(events.contains(event2));
        assertTrue(events.contains(event3));
        assertTrue(events.contains(event4));
        assertFalse(events.contains(event5));
//Try to remove again.
        assertFalse(eventList.removeFromHashEventMap(event5));
        eventSets = (Collection)eventList.hashEventMap.values();
        events = new TreeSet();
        for (Iterator itt = eventSets.iterator(); itt.hasNext();) {
            events.addAll((Set)itt.next());
        }
        assertEquals(4, events.size());
        assertTrue(events.contains(event1));
        assertTrue(events.contains(event2));
        assertTrue(events.contains(event3));
        assertTrue(events.contains(event4));
        assertFalse(events.contains(event5));

        assertTrue(eventList.removeFromHashEventMap(event1));
        eventSets = (Collection)eventList.hashEventMap.values();
        events = new TreeSet();
        for (Iterator itt = eventSets.iterator(); itt.hasNext();) {
            events.addAll((Set)itt.next());
        }
        assertEquals(3, events.size());
        assertFalse(events.contains(event1));
        assertTrue(events.contains(event2));
        assertTrue(events.contains(event3));
        assertTrue(events.contains(event4));
        assertFalse(events.contains(event5));

        assertTrue(eventList.removeFromHashEventMap(event2));
        eventSets = (Collection)eventList.hashEventMap.values();
        events = new TreeSet();
        for (Iterator itt = eventSets.iterator(); itt.hasNext();) {
            events.addAll((Set)itt.next());
        }
        assertEquals(2, events.size());
        assertFalse(events.contains(event1));
        assertFalse(events.contains(event2));
        assertTrue(events.contains(event3));
        assertTrue(events.contains(event4));
        assertFalse(events.contains(event5));

        assertTrue(eventList.removeFromHashEventMap(event3));
        eventSets = (Collection)eventList.hashEventMap.values();
        events = new TreeSet();
        for (Iterator itt = eventSets.iterator(); itt.hasNext();) {
            events.addAll((Set)itt.next());
        }
        assertEquals(1, events.size());
        assertFalse(events.contains(event1));
        assertFalse(events.contains(event2));
        assertFalse(events.contains(event3));
        assertTrue(events.contains(event4));
        assertFalse(events.contains(event5));

        assertTrue(eventList.removeFromHashEventMap(event4));
        eventSets = (Collection)eventList.hashEventMap.values();
        events = new TreeSet();
        for (Iterator itt = eventSets.iterator(); itt.hasNext();) {
            events.addAll((Set)itt.next());
        }
        assertEquals(0, events.size());
        assertFalse(events.contains(event1));
        assertFalse(events.contains(event2));
        assertFalse(events.contains(event3));
        assertFalse(events.contains(event4));
        assertFalse(events.contains(event5));
    }

    public void testSetFastInterrupts() {
        log.info("Starting testSetFastInterrupts");
        eventList.setFastInterrupts(false);
        eventList.scheduleEvent(event1);
        eventList.scheduleEvent(event2);
        eventList.scheduleEvent(event3);
        eventList.scheduleEvent(event4);
        eventList.scheduleEvent(event5);
        assertNull(eventList.hashEventMap);
        assertNull(eventList.entityEventMap);
        eventList.setFastInterrupts(true);
        Collection eventSets = (Collection)eventList.hashEventMap.values();
        Set events = new TreeSet();
        for (Iterator itt = eventSets.iterator(); itt.hasNext();) {
            events.addAll((Set)itt.next());
        }
        assertEquals(5, events.size());
        assertTrue(events.contains(event1));
        assertTrue(events.contains(event2));
        assertTrue(events.contains(event3));
        assertTrue(events.contains(event4));
        assertTrue(events.contains(event5));

        events = (Set)eventList.entityEventMap.get(entity1);
        assertEquals(2, events.size());
        assertTrue(events.contains(event1));
        assertTrue(events.contains(event2));
        assertFalse(events.contains(event3));
        assertFalse(events.contains(event4));
        assertFalse(events.contains(event5));
        events = (Set)eventList.entityEventMap.get(entity2);
        assertNull(events);
        events = (Set)eventList.entityEventMap.get(entity3);
        assertEquals(2, events.size());
        assertFalse(events.contains(event1));
        assertFalse(events.contains(event2));
        assertTrue(events.contains(event3));
        assertTrue(events.contains(event4));
        assertFalse(events.contains(event5));
        events = (Set)eventList.entityEventMap.get(entity4);
        assertNull(events);
        events = (Set)eventList.entityEventMap.get(entity5);
        assertEquals(1, events.size());
        assertFalse(events.contains(event1));
        assertFalse(events.contains(event2));
        assertFalse(events.contains(event3));
        assertFalse(events.contains(event4));
        assertTrue(events.contains(event5));
        
        eventList.setFastInterrupts(false);
        assertNull(eventList.hashEventMap);
        assertNull(eventList.entityEventMap);
    }

    public void testScheduleEvent() {
        log.info("Starting testScheduleEvents");
        eventList.setFastInterrupts(true);

        eventList.scheduleEvent(event1);
        assertEquals(1, eventList.eventList.size());
        assertTrue(eventList.eventList.contains(event1));
        assertFalse(eventList.eventList.contains(event2));
        assertFalse(eventList.eventList.contains(event3));
        assertFalse(eventList.eventList.contains(event4));
        assertFalse(eventList.eventList.contains(event5));
        Collection eventSets = (Collection)eventList.hashEventMap.values();
        Set events = new TreeSet();
        for (Iterator itt = eventSets.iterator(); itt.hasNext();) {
            events.addAll((Set)itt.next());
        }
        assertEquals(1, events.size());
        assertTrue(events.contains(event1));
        assertFalse(events.contains(event2));
        assertFalse(events.contains(event3));
        assertFalse(events.contains(event4));
        assertFalse(events.contains(event5));

        events = (Set)eventList.entityEventMap.get(entity1);
        assertEquals(1, events.size());
        assertTrue(events.contains(event1));
        assertFalse(events.contains(event2));
        assertFalse(events.contains(event3));
        assertFalse(events.contains(event4));
        assertFalse(events.contains(event5));
        events = (Set)eventList.entityEventMap.get(entity2);
        assertNull(events);
        events = (Set)eventList.entityEventMap.get(entity3);
        assertNull(events);
        events = (Set)eventList.entityEventMap.get(entity4);
        assertNull(events);
        events = (Set)eventList.entityEventMap.get(entity5);
        assertNull(events);

        eventList.scheduleEvent(event2);
        assertEquals(2, eventList.eventList.size());
        assertTrue(eventList.eventList.contains(event1));
        assertTrue(eventList.eventList.contains(event2));
        assertFalse(eventList.eventList.contains(event3));
        assertFalse(eventList.eventList.contains(event4));
        assertFalse(eventList.eventList.contains(event5));
        eventSets = (Collection)eventList.hashEventMap.values();
        events = new TreeSet();
        for (Iterator itt = eventSets.iterator(); itt.hasNext();) {
            events.addAll((Set)itt.next());
        }
        assertEquals(2, events.size());
        assertTrue(events.contains(event1));
        assertTrue(events.contains(event2));
        assertFalse(events.contains(event3));
        assertFalse(events.contains(event4));
        assertFalse(events.contains(event5));

        events = (Set)eventList.entityEventMap.get(entity1);
        assertEquals(2, events.size());
        assertTrue(events.contains(event1));
        assertTrue(events.contains(event2));
        assertFalse(events.contains(event3));
        assertFalse(events.contains(event4));
        assertFalse(events.contains(event5));
        events = (Set)eventList.entityEventMap.get(entity2);
        assertNull(events);
        events = (Set)eventList.entityEventMap.get(entity3);
        assertNull(events);
        events = (Set)eventList.entityEventMap.get(entity4);
        assertNull(events);
        events = (Set)eventList.entityEventMap.get(entity5);
        assertNull(events);

        eventList.scheduleEvent(event3);
        assertEquals(3, eventList.eventList.size());
        assertTrue(eventList.eventList.contains(event1));
        assertTrue(eventList.eventList.contains(event2));
        assertTrue(eventList.eventList.contains(event3));
        assertFalse(eventList.eventList.contains(event4));
        assertFalse(eventList.eventList.contains(event5));
        eventSets = (Collection)eventList.hashEventMap.values();
        events = new TreeSet();
        for (Iterator itt = eventSets.iterator(); itt.hasNext();) {
            events.addAll((Set)itt.next());
        }
        assertEquals(3, events.size());
        assertTrue(events.contains(event1));
        assertTrue(events.contains(event2));
        assertTrue(events.contains(event3));
        assertFalse(events.contains(event4));
        assertFalse(events.contains(event5));

        events = (Set)eventList.entityEventMap.get(entity1);
        assertEquals(2, events.size());
        assertTrue(events.contains(event1));
        assertTrue(events.contains(event2));
        assertFalse(events.contains(event3));
        assertFalse(events.contains(event4));
        assertFalse(events.contains(event5));
        events = (Set)eventList.entityEventMap.get(entity2);
        assertNull(events);
        events = (Set)eventList.entityEventMap.get(entity3);
        assertEquals(1, events.size());
        assertFalse(events.contains(event1));
        assertFalse(events.contains(event2));
        assertTrue(events.contains(event3));
        assertFalse(events.contains(event4));
        assertFalse(events.contains(event5));
        events = (Set)eventList.entityEventMap.get(entity4);
        assertNull(events);
        events = (Set)eventList.entityEventMap.get(entity5);
        assertNull(events);

        eventList.scheduleEvent(event4);
        assertEquals(4, eventList.eventList.size());
        assertTrue(eventList.eventList.contains(event1));
        assertTrue(eventList.eventList.contains(event2));
        assertTrue(eventList.eventList.contains(event3));
        assertTrue(eventList.eventList.contains(event4));
        assertFalse(eventList.eventList.contains(event5));
        eventSets = (Collection)eventList.hashEventMap.values();
        events = new TreeSet();
        for (Iterator itt = eventSets.iterator(); itt.hasNext();) {
            events.addAll((Set)itt.next());
        }
        assertEquals(4, events.size());
        assertTrue(events.contains(event1));
        assertTrue(events.contains(event2));
        assertTrue(events.contains(event3));
        assertTrue(events.contains(event4));
        assertFalse(events.contains(event5));

        events = (Set)eventList.entityEventMap.get(entity1);
        assertEquals(2, events.size());
        assertTrue(events.contains(event1));
        assertTrue(events.contains(event2));
        assertFalse(events.contains(event3));
        assertFalse(events.contains(event4));
        assertFalse(events.contains(event5));
        events = (Set)eventList.entityEventMap.get(entity2);
        assertNull(events);
        events = (Set)eventList.entityEventMap.get(entity3);
        assertEquals(2, events.size());
        assertFalse(events.contains(event1));
        assertFalse(events.contains(event2));
        assertTrue(events.contains(event3));
        assertTrue(events.contains(event4));
        assertFalse(events.contains(event5));
        events = (Set)eventList.entityEventMap.get(entity4);
        assertNull(events);
        events = (Set)eventList.entityEventMap.get(entity5);
        assertNull(events);

        eventList.scheduleEvent(event5);
        assertEquals(5, eventList.eventList.size());
        assertTrue(eventList.eventList.contains(event1));
        assertTrue(eventList.eventList.contains(event2));
        assertTrue(eventList.eventList.contains(event3));
        assertTrue(eventList.eventList.contains(event4));
        assertTrue(eventList.eventList.contains(event5));
        eventSets = (Collection)eventList.hashEventMap.values();
        events = new TreeSet();
        for (Iterator itt = eventSets.iterator(); itt.hasNext();) {
            events.addAll((Set)itt.next());
        }
        assertEquals(5, events.size());
        assertTrue(events.contains(event1));
        assertTrue(events.contains(event2));
        assertTrue(events.contains(event3));
        assertTrue(events.contains(event4));
        assertTrue(events.contains(event5));

        events = (Set)eventList.entityEventMap.get(entity1);
        assertEquals(2, events.size());
        assertTrue(events.contains(event1));
        assertTrue(events.contains(event2));
        assertFalse(events.contains(event3));
        assertFalse(events.contains(event4));
        assertFalse(events.contains(event5));
        events = (Set)eventList.entityEventMap.get(entity2);
        assertNull(events);
        events = (Set)eventList.entityEventMap.get(entity3);
        assertEquals(2, events.size());
        assertFalse(events.contains(event1));
        assertFalse(events.contains(event2));
        assertTrue(events.contains(event3));
        assertTrue(events.contains(event4));
        assertFalse(events.contains(event5));
        events = (Set)eventList.entityEventMap.get(entity4);
        assertNull(events);
        events = (Set)eventList.entityEventMap.get(entity5);
        assertEquals(1, events.size());
        assertFalse(events.contains(event1));
        assertFalse(events.contains(event2));
        assertFalse(events.contains(event3));
        assertFalse(events.contains(event4));
        assertTrue(events.contains(event5));
    }

    public void testRun() {
        log.info("Starting testRun");
        eventList.setFastInterrupts(true);
        eventList.scheduleEvent(event1);
        eventList.scheduleEvent(event2);
        eventList.scheduleEvent(event3);
        eventList.scheduleEvent(event4);
        eventList.scheduleEvent(event5);

        eventList.startSimulation();

        assertEquals(5, listener.events.size());
        assertEquals(event3, listener.events.get(0));
        assertEquals(event1, listener.events.get(1));
        assertEquals(event2, listener.events.get(2));
        assertEquals(event4, listener.events.get(3));
        assertEquals(event5, listener.events.get(4));
    }

    public void testInterruptEntityName() {
        log.info("Starting testInterruptEntityName");
        eventList.setFastInterrupts(true);
        eventList.scheduleEvent(event1);
        eventList.scheduleEvent(event2);
        eventList.scheduleEvent(event3);
        eventList.scheduleEvent(event4);
        eventList.scheduleEvent(event5);
        eventList.scheduleEvent(event1a);
        eventList.scheduleEvent(event2a);
        eventList.scheduleEvent(event3a);
        eventList.scheduleEvent(event4a);
        eventList.scheduleEvent(event5a);

        eventList.interrupt(entity1, "This");

        eventList.startSimulation();

        assertEquals(9, listener.events.size());
        assertEquals(event3, listener.events.get(0));
        assertEquals(event2, listener.events.get(1));
        assertEquals(event4, listener.events.get(2));
        assertEquals(event5, listener.events.get(3));
        assertEquals(event3a, listener.events.get(4));
        assertEquals(event1a, listener.events.get(5));
        assertEquals(event2a, listener.events.get(6));
        assertEquals(event4a, listener.events.get(7));
        assertEquals(event5a, listener.events.get(8));
    }

    public void testInterruptFull() {
        log.info("Starting testInterruptFull");
        eventList.setFastInterrupts(true);
        eventList.scheduleEvent(event1);
        eventList.scheduleEvent(event2);
        eventList.scheduleEvent(event3);
        eventList.scheduleEvent(event4);
        eventList.scheduleEvent(event5);
        eventList.scheduleEvent(event1a);
        eventList.scheduleEvent(event2a);
        eventList.scheduleEvent(event3a);
        eventList.scheduleEvent(event4a);
        eventList.scheduleEvent(event5a);

        eventList.interrupt(entity3, "TheOther", new Object[] {new Integer(1), null});

        eventList.startSimulation();
        assertEquals(9, listener.events.size());
        assertEquals(event3, listener.events.get(0));
        assertEquals(event1, listener.events.get(1));
        assertEquals(event2, listener.events.get(2));
        assertEquals(event5, listener.events.get(3));
        assertEquals(event3a, listener.events.get(4));
        assertEquals(event1a, listener.events.get(5));
        assertEquals(event2a, listener.events.get(6));
        assertEquals(event4a, listener.events.get(7));
        assertEquals(event5a, listener.events.get(8));
    }

    public void testInterruptAllEntityName() {
        log.info("Starting testInterruptAllEntityName");
        eventList.setFastInterrupts(true);
        eventList.scheduleEvent(event1);
        eventList.scheduleEvent(event2);
        eventList.scheduleEvent(event3);
        eventList.scheduleEvent(event4);
        eventList.scheduleEvent(event5);
        eventList.scheduleEvent(event1a);
        eventList.scheduleEvent(event2a);
        eventList.scheduleEvent(event3a);
        eventList.scheduleEvent(event4a);
        eventList.scheduleEvent(event5a);

        eventList.interruptAll(entity1, "This");

        eventList.startSimulation();

        assertEquals(6, listener.events.size());
        assertEquals(event3, listener.events.get(0));
        assertEquals(event4, listener.events.get(1));
        assertEquals(event5, listener.events.get(2));
        assertEquals(event3a, listener.events.get(3));
        assertEquals(event4a, listener.events.get(4));
        assertEquals(event5a, listener.events.get(5));
    }

    public void testInterruptAllEntityName2() {
        log.info("Starting testInterruptAllEntityName2");
        eventList.setFastInterrupts(true);
        eventList.scheduleEvent(event1);
        eventList.scheduleEvent(event2);
        eventList.scheduleEvent(event3);
        eventList.scheduleEvent(event4);
        eventList.scheduleEvent(event5);
        eventList.scheduleEvent(event1a);
        eventList.scheduleEvent(event2a);
        eventList.scheduleEvent(event3a);
        eventList.scheduleEvent(event4a);
        eventList.scheduleEvent(event5a);

        eventList.interruptAll(entity3, "That");

        eventList.startSimulation();

        assertEquals(8, listener.events.size());
        assertEquals(event1, listener.events.get(0));
        assertEquals(event2, listener.events.get(1));
        assertEquals(event4, listener.events.get(2));
        assertEquals(event5, listener.events.get(3));
        assertEquals(event1a, listener.events.get(4));
        assertEquals(event2a, listener.events.get(5));
        assertEquals(event4a, listener.events.get(6));
        assertEquals(event5a, listener.events.get(7));
    }

    public void testInterruptAllFull() {
        log.info("Starting testInterruptAllFull");
        eventList.setFastInterrupts(true);
        eventList.scheduleEvent(event1);
        eventList.scheduleEvent(event2);
        eventList.scheduleEvent(event3);
        eventList.scheduleEvent(event4);
        eventList.scheduleEvent(event5);
        eventList.scheduleEvent(event1a);
        eventList.scheduleEvent(event2a);
        eventList.scheduleEvent(event3a);
        eventList.scheduleEvent(event4a);
        eventList.scheduleEvent(event5a);

        eventList.interruptAll(entity3, "TheOther", new Object[] {new Integer(1), null});

        eventList.startSimulation();
        assertEquals(8, listener.events.size());
        assertEquals(event3, listener.events.get(0));
        assertEquals(event1, listener.events.get(1));
        assertEquals(event2, listener.events.get(2));
        assertEquals(event5, listener.events.get(3));
        assertEquals(event3a, listener.events.get(4));
        assertEquals(event1a, listener.events.get(5));
        assertEquals(event2a, listener.events.get(6));
        assertEquals(event5a, listener.events.get(7));
    }

    public void testInterruptAllFull2() {
        log.info("Starting testInterruptAllFull2");
        eventList.setFastInterrupts(true);
        eventList.scheduleEvent(event1);
        eventList.scheduleEvent(event2);
        eventList.scheduleEvent(event3);
        eventList.scheduleEvent(event4);
        eventList.scheduleEvent(event5);
        eventList.scheduleEvent(event1a);
        eventList.scheduleEvent(event2a);
        eventList.scheduleEvent(event3a);
        eventList.scheduleEvent(event4a);
        eventList.scheduleEvent(event5a);

        eventList.interruptAll(entity1, "This", new Object[] {entity2});

        eventList.startSimulation();
        assertEquals(8, listener.events.size());
        assertEquals(event3, listener.events.get(0));
        assertEquals(event1, listener.events.get(1));
        assertEquals(event4, listener.events.get(2));
        assertEquals(event5, listener.events.get(3));
        assertEquals(event3a, listener.events.get(4));
        assertEquals(event1a, listener.events.get(5));
        assertEquals(event4a, listener.events.get(6));
        assertEquals(event5a, listener.events.get(7));
    }

    public void testInterruptAllEntity() {
        log.info("Starting testInterruptAllEntity");

        eventList.setFastInterrupts(true);
        eventList.scheduleEvent(event1);
        eventList.scheduleEvent(event2);
        eventList.scheduleEvent(event3);
        eventList.scheduleEvent(event4);
        eventList.scheduleEvent(event5);
        eventList.scheduleEvent(event1a);
        eventList.scheduleEvent(event2a);
        eventList.scheduleEvent(event3a);
        eventList.scheduleEvent(event4a);
        eventList.scheduleEvent(event5a);

        eventList.interruptAll(entity3);
        eventList.startSimulation();
        assertEquals(6, listener.events.size());
        assertEquals(event1, listener.events.get(0));
        assertEquals(event2, listener.events.get(1));
        assertEquals(event5, listener.events.get(2));
        assertEquals(event1a, listener.events.get(3));
        assertEquals(event2a, listener.events.get(4));
        assertEquals(event5a, listener.events.get(5));
    }

    public void testInterruptEntityNameNotFound() {
        log.info("Starting testInterruptEntityNameNotFound");
        eventList.setFastInterrupts(true);
        eventList.scheduleEvent(event1);
        eventList.scheduleEvent(event2);
        eventList.scheduleEvent(event3);
        eventList.scheduleEvent(event4);
        eventList.scheduleEvent(event5);
        eventList.scheduleEvent(event1a);
        eventList.scheduleEvent(event2a);
        eventList.scheduleEvent(event3a);
        eventList.scheduleEvent(event4a);
        eventList.scheduleEvent(event5a);

        eventList.interrupt(entity1, "NoName");
        eventList.interrupt(entity2, "NoName");

        eventList.startSimulation();

        assertEquals(10, listener.events.size());
        assertEquals(event3, listener.events.get(0));
        assertEquals(event1, listener.events.get(1));
        assertEquals(event2, listener.events.get(2));
        assertEquals(event4, listener.events.get(3));
        assertEquals(event5, listener.events.get(4));
        assertEquals(event3a, listener.events.get(5));
        assertEquals(event1a, listener.events.get(6));
        assertEquals(event2a, listener.events.get(7));
        assertEquals(event4a, listener.events.get(8));
        assertEquals(event5a, listener.events.get(9));
    }

    public void testInterruptFullNotFound() {
        log.info("Starting testInterruptFullNotFound");
        eventList.setFastInterrupts(true);
        eventList.scheduleEvent(event1);
        eventList.scheduleEvent(event2);
        eventList.scheduleEvent(event3);
        eventList.scheduleEvent(event4);
        eventList.scheduleEvent(event5);
        eventList.scheduleEvent(event1a);
        eventList.scheduleEvent(event2a);
        eventList.scheduleEvent(event3a);
        eventList.scheduleEvent(event4a);
        eventList.scheduleEvent(event5a);

        eventList.interrupt(entity4, "TheOther", new Object[] {new Integer(1), null});
        eventList.interrupt(entity3, "NotFound", new Object[] {new Integer(1), null});
        eventList.interrupt(entity3, "TheOther", new Object[] {new Integer(2), null});
        eventList.interrupt(entity3, "TheOther", new Object[] {new Integer(1), entity1});
        eventList.interrupt(entity3, "TheOther", new Object[] {new Integer(1), null, entity2});

        eventList.startSimulation();

        assertEquals(10, listener.events.size());
        assertEquals(event3, listener.events.get(0));
        assertEquals(event1, listener.events.get(1));
        assertEquals(event2, listener.events.get(2));
        assertEquals(event4, listener.events.get(3));
        assertEquals(event5, listener.events.get(4));
        assertEquals(event3a, listener.events.get(5));
        assertEquals(event1a, listener.events.get(6));
        assertEquals(event2a, listener.events.get(7));
        assertEquals(event4a, listener.events.get(8));
        assertEquals(event5a, listener.events.get(9));
    }

    public void testInterruptAllEntityNameNotFound() {
        log.info("Starting testInterruptAllEntityNameNotFound");
        eventList.setFastInterrupts(true);
        eventList.scheduleEvent(event1);
        eventList.scheduleEvent(event2);
        eventList.scheduleEvent(event3);
        eventList.scheduleEvent(event4);
        eventList.scheduleEvent(event5);
        eventList.scheduleEvent(event1a);
        eventList.scheduleEvent(event2a);
        eventList.scheduleEvent(event3a);
        eventList.scheduleEvent(event4a);
        eventList.scheduleEvent(event5a);

        eventList.interruptAll(entity4, "This");
        eventList.interruptAll(entity1, "NotFound");

        eventList.startSimulation();

        assertEquals(10, listener.events.size());
        assertEquals(event3, listener.events.get(0));
        assertEquals(event1, listener.events.get(1));
        assertEquals(event2, listener.events.get(2));
        assertEquals(event4, listener.events.get(3));
        assertEquals(event5, listener.events.get(4));
        assertEquals(event3a, listener.events.get(5));
        assertEquals(event1a, listener.events.get(6));
        assertEquals(event2a, listener.events.get(7));
        assertEquals(event4a, listener.events.get(8));
        assertEquals(event5a, listener.events.get(9));
    }

    public void testInterruptAllEntityName2NotFound() {
        log.info("Starting testInterruptAllEntityName2NotFound");
        eventList.setFastInterrupts(true);
        eventList.scheduleEvent(event1);
        eventList.scheduleEvent(event2);
        eventList.scheduleEvent(event3);
        eventList.scheduleEvent(event4);
        eventList.scheduleEvent(event5);
        eventList.scheduleEvent(event1a);
        eventList.scheduleEvent(event2a);
        eventList.scheduleEvent(event3a);
        eventList.scheduleEvent(event4a);
        eventList.scheduleEvent(event5a);

        eventList.interruptAll(entity2, "That");
        eventList.interruptAll(entity3, "This");

        eventList.startSimulation();

        assertEquals(10, listener.events.size());
        assertEquals(event3, listener.events.get(0));
        assertEquals(event1, listener.events.get(1));
        assertEquals(event2, listener.events.get(2));
        assertEquals(event4, listener.events.get(3));
        assertEquals(event5, listener.events.get(4));
        assertEquals(event3a, listener.events.get(5));
        assertEquals(event1a, listener.events.get(6));
        assertEquals(event2a, listener.events.get(7));
        assertEquals(event4a, listener.events.get(8));
        assertEquals(event5a, listener.events.get(9));
    }

    public void testInterruptAllFullNotFound() {
        log.info("Starting testInterruptAllFullNotFound");
        eventList.setFastInterrupts(true);
        eventList.scheduleEvent(event1);
        eventList.scheduleEvent(event2);
        eventList.scheduleEvent(event3);
        eventList.scheduleEvent(event4);
        eventList.scheduleEvent(event5);
        eventList.scheduleEvent(event1a);
        eventList.scheduleEvent(event2a);
        eventList.scheduleEvent(event3a);
        eventList.scheduleEvent(event4a);
        eventList.scheduleEvent(event5a);

        eventList.interruptAll(entity2, "TheOther", new Object[] {new Integer(1), null});
        eventList.interruptAll(entity1, "TheOther", new Object[] {new Integer(1), null});
        eventList.interruptAll(entity3, "TheOther", new Object[] {new Integer(2), null});
        eventList.interruptAll(entity3, "TheOther", new Object[] {new Integer(1), null, entity2});

        eventList.startSimulation();
        assertEquals(10, listener.events.size());
        assertEquals(event3, listener.events.get(0));
        assertEquals(event1, listener.events.get(1));
        assertEquals(event2, listener.events.get(2));
        assertEquals(event4, listener.events.get(3));
        assertEquals(event5, listener.events.get(4));
        assertEquals(event3a, listener.events.get(5));
        assertEquals(event1a, listener.events.get(6));
        assertEquals(event2a, listener.events.get(7));
        assertEquals(event4a, listener.events.get(8));
        assertEquals(event5a, listener.events.get(9));
    }

    public void testInterruptAllFull2NotFound() {
        log.info("Starting testInterruptAllFull2NotFound");
        eventList.setFastInterrupts(true);
        eventList.scheduleEvent(event1);
        eventList.scheduleEvent(event2);
        eventList.scheduleEvent(event3);
        eventList.scheduleEvent(event4);
        eventList.scheduleEvent(event5);
        eventList.scheduleEvent(event1a);
        eventList.scheduleEvent(event2a);
        eventList.scheduleEvent(event3a);
        eventList.scheduleEvent(event4a);
        eventList.scheduleEvent(event5a);

        eventList.interruptAll(entity2, "This", new Object[] {entity2});
        eventList.interruptAll(entity1, "That", new Object[] {entity2});
        eventList.interruptAll(entity1, "This", new Object[] {entity3});
        eventList.interruptAll(entity1, "This", new Object[] {null});

        eventList.startSimulation();
        assertEquals(10, listener.events.size());
        assertEquals(event3, listener.events.get(0));
        assertEquals(event1, listener.events.get(1));
        assertEquals(event2, listener.events.get(2));
        assertEquals(event4, listener.events.get(3));
        assertEquals(event5, listener.events.get(4));
        assertEquals(event3a, listener.events.get(5));
        assertEquals(event1a, listener.events.get(6));
        assertEquals(event2a, listener.events.get(7));
        assertEquals(event4a, listener.events.get(8));
        assertEquals(event5a, listener.events.get(9));
    }

    public void testInterruptAllEntityNotFound() {
        log.info("Starting testInterruptAllEntityNotFound");

        eventList.setFastInterrupts(true);
        eventList.scheduleEvent(event1);
        eventList.scheduleEvent(event2);
        eventList.scheduleEvent(event3);
        eventList.scheduleEvent(event4);
        eventList.scheduleEvent(event5);
        eventList.scheduleEvent(event1a);
        eventList.scheduleEvent(event2a);
        eventList.scheduleEvent(event3a);
        eventList.scheduleEvent(event4a);
        eventList.scheduleEvent(event5a);

        eventList.interruptAll(entity2);
        eventList.startSimulation();
        assertEquals(10, listener.events.size());
        assertEquals(event3, listener.events.get(0));
        assertEquals(event1, listener.events.get(1));
        assertEquals(event2, listener.events.get(2));
        assertEquals(event4, listener.events.get(3));
        assertEquals(event5, listener.events.get(4));
        assertEquals(event3a, listener.events.get(5));
        assertEquals(event1a, listener.events.get(6));
        assertEquals(event2a, listener.events.get(7));
        assertEquals(event4a, listener.events.get(8));
        assertEquals(event5a, listener.events.get(9));
    }


//Start of "Old" tests

    public void testRunOld() {
        log.info("Starting testRunOld");
        eventList.setFastInterrupts(false);
        eventList.scheduleEvent(event1);
        eventList.scheduleEvent(event2);
        eventList.scheduleEvent(event3);
        eventList.scheduleEvent(event4);
        eventList.scheduleEvent(event5);

        eventList.startSimulation();

        assertEquals(5, listener.events.size());
        assertEquals(event3, listener.events.get(0));
        assertEquals(event1, listener.events.get(1));
        assertEquals(event2, listener.events.get(2));
        assertEquals(event4, listener.events.get(3));
        assertEquals(event5, listener.events.get(4));
    }

    public void testInterruptEntityNameOld() {
        log.info("Starting testInterruptEntityNameOld");
        eventList.setFastInterrupts(false);
        eventList.scheduleEvent(event1);
        eventList.scheduleEvent(event2);
        eventList.scheduleEvent(event3);
        eventList.scheduleEvent(event4);
        eventList.scheduleEvent(event5);
        eventList.scheduleEvent(event1a);
        eventList.scheduleEvent(event2a);
        eventList.scheduleEvent(event3a);
        eventList.scheduleEvent(event4a);
        eventList.scheduleEvent(event5a);

        eventList.interrupt(entity1, "This");

        eventList.startSimulation();

        assertEquals(9, listener.events.size());
        assertEquals(event3, listener.events.get(0));
        assertEquals(event2, listener.events.get(1));
        assertEquals(event4, listener.events.get(2));
        assertEquals(event5, listener.events.get(3));
        assertEquals(event3a, listener.events.get(4));
        assertEquals(event1a, listener.events.get(5));
        assertEquals(event2a, listener.events.get(6));
        assertEquals(event4a, listener.events.get(7));
        assertEquals(event5a, listener.events.get(8));
    }

    public void testInterruptFullOld() {
        log.info("Starting testInterruptFullOld");
        eventList.setFastInterrupts(false);
        eventList.scheduleEvent(event1);
        eventList.scheduleEvent(event2);
        eventList.scheduleEvent(event3);
        eventList.scheduleEvent(event4);
        eventList.scheduleEvent(event5);
        eventList.scheduleEvent(event1a);
        eventList.scheduleEvent(event2a);
        eventList.scheduleEvent(event3a);
        eventList.scheduleEvent(event4a);
        eventList.scheduleEvent(event5a);

        eventList.interrupt(entity3, "TheOther", new Object[] {new Integer(1), null});

        eventList.startSimulation();
        assertEquals(9, listener.events.size());
        assertEquals(event3, listener.events.get(0));
        assertEquals(event1, listener.events.get(1));
        assertEquals(event2, listener.events.get(2));
        assertEquals(event5, listener.events.get(3));
        assertEquals(event3a, listener.events.get(4));
        assertEquals(event1a, listener.events.get(5));
        assertEquals(event2a, listener.events.get(6));
        assertEquals(event4a, listener.events.get(7));
        assertEquals(event5a, listener.events.get(8));
    }

    public void testInterruptAllEntityNameOld() {
        log.info("Starting testInterruptAllEntityNameOld");
        eventList.setFastInterrupts(false);
        eventList.scheduleEvent(event1);
        eventList.scheduleEvent(event2);
        eventList.scheduleEvent(event3);
        eventList.scheduleEvent(event4);
        eventList.scheduleEvent(event5);
        eventList.scheduleEvent(event1a);
        eventList.scheduleEvent(event2a);
        eventList.scheduleEvent(event3a);
        eventList.scheduleEvent(event4a);
        eventList.scheduleEvent(event5a);

        eventList.interruptAll(entity1, "This");

        eventList.startSimulation();

        assertEquals(6, listener.events.size());
        assertEquals(event3, listener.events.get(0));
        assertEquals(event4, listener.events.get(1));
        assertEquals(event5, listener.events.get(2));
        assertEquals(event3a, listener.events.get(3));
        assertEquals(event4a, listener.events.get(4));
        assertEquals(event5a, listener.events.get(5));
    }

    public void testInterruptAllEntityName2Old() {
        log.info("Starting testInterruptAllEntityName2Old");
        eventList.setFastInterrupts(false);
        eventList.scheduleEvent(event1);
        eventList.scheduleEvent(event2);
        eventList.scheduleEvent(event3);
        eventList.scheduleEvent(event4);
        eventList.scheduleEvent(event5);
        eventList.scheduleEvent(event1a);
        eventList.scheduleEvent(event2a);
        eventList.scheduleEvent(event3a);
        eventList.scheduleEvent(event4a);
        eventList.scheduleEvent(event5a);

        eventList.interruptAll(entity3, "That");

        eventList.startSimulation();

        assertEquals(8, listener.events.size());
        assertEquals(event1, listener.events.get(0));
        assertEquals(event2, listener.events.get(1));
        assertEquals(event4, listener.events.get(2));
        assertEquals(event5, listener.events.get(3));
        assertEquals(event1a, listener.events.get(4));
        assertEquals(event2a, listener.events.get(5));
        assertEquals(event4a, listener.events.get(6));
        assertEquals(event5a, listener.events.get(7));
    }

    public void testInterruptAllFullOld() {
        log.info("Starting testInterruptAllFullOld");
        eventList.setFastInterrupts(false);
        eventList.scheduleEvent(event1);
        eventList.scheduleEvent(event2);
        eventList.scheduleEvent(event3);
        eventList.scheduleEvent(event4);
        eventList.scheduleEvent(event5);
        eventList.scheduleEvent(event1a);
        eventList.scheduleEvent(event2a);
        eventList.scheduleEvent(event3a);
        eventList.scheduleEvent(event4a);
        eventList.scheduleEvent(event5a);

        eventList.interruptAll(entity3, "TheOther", new Object[] {new Integer(1), null});

        eventList.startSimulation();
        assertEquals(8, listener.events.size());
        assertEquals(event3, listener.events.get(0));
        assertEquals(event1, listener.events.get(1));
        assertEquals(event2, listener.events.get(2));
        assertEquals(event5, listener.events.get(3));
        assertEquals(event3a, listener.events.get(4));
        assertEquals(event1a, listener.events.get(5));
        assertEquals(event2a, listener.events.get(6));
        assertEquals(event5a, listener.events.get(7));
    }

    public void testInterruptAllFull2Old() {
        log.info("Starting testInterruptAllFull2Old");
        eventList.setFastInterrupts(false);
        eventList.scheduleEvent(event1);
        eventList.scheduleEvent(event2);
        eventList.scheduleEvent(event3);
        eventList.scheduleEvent(event4);
        eventList.scheduleEvent(event5);
        eventList.scheduleEvent(event1a);
        eventList.scheduleEvent(event2a);
        eventList.scheduleEvent(event3a);
        eventList.scheduleEvent(event4a);
        eventList.scheduleEvent(event5a);

        eventList.interruptAll(entity1, "This", new Object[] {entity2});

        eventList.startSimulation();
        assertEquals(8, listener.events.size());
        assertEquals(event3, listener.events.get(0));
        assertEquals(event1, listener.events.get(1));
        assertEquals(event4, listener.events.get(2));
        assertEquals(event5, listener.events.get(3));
        assertEquals(event3a, listener.events.get(4));
        assertEquals(event1a, listener.events.get(5));
        assertEquals(event4a, listener.events.get(6));
        assertEquals(event5a, listener.events.get(7));
    }

    public void testInterruptAllEntityOld() {
        log.info("Starting testInterruptAllEntityOld");
        eventList.setFastInterrupts(false);

        eventList.scheduleEvent(event1);
        eventList.scheduleEvent(event2);
        eventList.scheduleEvent(event3);
        eventList.scheduleEvent(event4);
        eventList.scheduleEvent(event5);
        eventList.scheduleEvent(event1a);
        eventList.scheduleEvent(event2a);
        eventList.scheduleEvent(event3a);
        eventList.scheduleEvent(event4a);
        eventList.scheduleEvent(event5a);

        eventList.interruptAll(entity3);
        eventList.startSimulation();
        assertEquals(6, listener.events.size());
        assertEquals(event1, listener.events.get(0));
        assertEquals(event2, listener.events.get(1));
        assertEquals(event5, listener.events.get(2));
        assertEquals(event1a, listener.events.get(3));
        assertEquals(event2a, listener.events.get(4));
        assertEquals(event5a, listener.events.get(5));
    }

    public void testInterruptEntityNameNotFoundOld() {
        log.info("Starting testInterruptEntityNameNotFoundOld");
        eventList.setFastInterrupts(false);
        eventList.scheduleEvent(event1);
        eventList.scheduleEvent(event2);
        eventList.scheduleEvent(event3);
        eventList.scheduleEvent(event4);
        eventList.scheduleEvent(event5);
        eventList.scheduleEvent(event1a);
        eventList.scheduleEvent(event2a);
        eventList.scheduleEvent(event3a);
        eventList.scheduleEvent(event4a);
        eventList.scheduleEvent(event5a);

        eventList.interrupt(entity1, "NoName");
        eventList.interrupt(entity2, "NoName");

        eventList.startSimulation();

        assertEquals(10, listener.events.size());
        assertEquals(event3, listener.events.get(0));
        assertEquals(event1, listener.events.get(1));
        assertEquals(event2, listener.events.get(2));
        assertEquals(event4, listener.events.get(3));
        assertEquals(event5, listener.events.get(4));
        assertEquals(event3a, listener.events.get(5));
        assertEquals(event1a, listener.events.get(6));
        assertEquals(event2a, listener.events.get(7));
        assertEquals(event4a, listener.events.get(8));
        assertEquals(event5a, listener.events.get(9));
    }

    public void testInterruptFullNotFoundOld() {
        log.info("Starting testInterruptFullNotFoundOld");
        eventList.setFastInterrupts(false);
        eventList.scheduleEvent(event1);
        eventList.scheduleEvent(event2);
        eventList.scheduleEvent(event3);
        eventList.scheduleEvent(event4);
        eventList.scheduleEvent(event5);
        eventList.scheduleEvent(event1a);
        eventList.scheduleEvent(event2a);
        eventList.scheduleEvent(event3a);
        eventList.scheduleEvent(event4a);
        eventList.scheduleEvent(event5a);

        eventList.interrupt(entity4, "TheOther", new Object[] {new Integer(1), null});
        eventList.interrupt(entity3, "NotFound", new Object[] {new Integer(1), null});
        eventList.interrupt(entity3, "TheOther", new Object[] {new Integer(2), null});
        eventList.interrupt(entity3, "TheOther", new Object[] {new Integer(1), entity1});
        eventList.interrupt(entity3, "TheOther", new Object[] {new Integer(1), null, entity2});

        eventList.startSimulation();

        assertEquals(10, listener.events.size());
        assertEquals(event3, listener.events.get(0));
        assertEquals(event1, listener.events.get(1));
        assertEquals(event2, listener.events.get(2));
        assertEquals(event4, listener.events.get(3));
        assertEquals(event5, listener.events.get(4));
        assertEquals(event3a, listener.events.get(5));
        assertEquals(event1a, listener.events.get(6));
        assertEquals(event2a, listener.events.get(7));
        assertEquals(event4a, listener.events.get(8));
        assertEquals(event5a, listener.events.get(9));
    }

    public void testInterruptAllEntityNameNotFoundOld() {
        log.info("Starting testInterruptAllEntityNameNotFoundOld");
        eventList.setFastInterrupts(false);
        eventList.scheduleEvent(event1);
        eventList.scheduleEvent(event2);
        eventList.scheduleEvent(event3);
        eventList.scheduleEvent(event4);
        eventList.scheduleEvent(event5);
        eventList.scheduleEvent(event1a);
        eventList.scheduleEvent(event2a);
        eventList.scheduleEvent(event3a);
        eventList.scheduleEvent(event4a);
        eventList.scheduleEvent(event5a);

        eventList.interruptAll(entity4, "This");
        eventList.interruptAll(entity1, "NotFound");

        eventList.startSimulation();

        assertEquals(10, listener.events.size());
        assertEquals(event3, listener.events.get(0));
        assertEquals(event1, listener.events.get(1));
        assertEquals(event2, listener.events.get(2));
        assertEquals(event4, listener.events.get(3));
        assertEquals(event5, listener.events.get(4));
        assertEquals(event3a, listener.events.get(5));
        assertEquals(event1a, listener.events.get(6));
        assertEquals(event2a, listener.events.get(7));
        assertEquals(event4a, listener.events.get(8));
        assertEquals(event5a, listener.events.get(9));
    }

    public void testInterruptAllEntityName2NotFoundOld() {
        log.info("Starting testInterruptAllEntityName2NotFoundOld");
        eventList.setFastInterrupts(false);
        eventList.scheduleEvent(event1);
        eventList.scheduleEvent(event2);
        eventList.scheduleEvent(event3);
        eventList.scheduleEvent(event4);
        eventList.scheduleEvent(event5);
        eventList.scheduleEvent(event1a);
        eventList.scheduleEvent(event2a);
        eventList.scheduleEvent(event3a);
        eventList.scheduleEvent(event4a);
        eventList.scheduleEvent(event5a);

        eventList.interruptAll(entity2, "That");
        eventList.interruptAll(entity3, "This");

        eventList.startSimulation();

        assertEquals(10, listener.events.size());
        assertEquals(event3, listener.events.get(0));
        assertEquals(event1, listener.events.get(1));
        assertEquals(event2, listener.events.get(2));
        assertEquals(event4, listener.events.get(3));
        assertEquals(event5, listener.events.get(4));
        assertEquals(event3a, listener.events.get(5));
        assertEquals(event1a, listener.events.get(6));
        assertEquals(event2a, listener.events.get(7));
        assertEquals(event4a, listener.events.get(8));
        assertEquals(event5a, listener.events.get(9));
    }

    public void testInterruptAllFullNotFoundOld() {
        log.info("Starting testInterruptAllFullNotFoundOld");
        eventList.setFastInterrupts(false);
        eventList.scheduleEvent(event1);
        eventList.scheduleEvent(event2);
        eventList.scheduleEvent(event3);
        eventList.scheduleEvent(event4);
        eventList.scheduleEvent(event5);
        eventList.scheduleEvent(event1a);
        eventList.scheduleEvent(event2a);
        eventList.scheduleEvent(event3a);
        eventList.scheduleEvent(event4a);
        eventList.scheduleEvent(event5a);

        eventList.interruptAll(entity2, "TheOther", new Object[] {new Integer(1), null});
        eventList.interruptAll(entity1, "TheOther", new Object[] {new Integer(1), null});
        eventList.interruptAll(entity3, "TheOther", new Object[] {new Integer(2), null});
        eventList.interruptAll(entity3, "TheOther", new Object[] {new Integer(1), null, entity2});

        eventList.startSimulation();
        assertEquals(10, listener.events.size());
        assertEquals(event3, listener.events.get(0));
        assertEquals(event1, listener.events.get(1));
        assertEquals(event2, listener.events.get(2));
        assertEquals(event4, listener.events.get(3));
        assertEquals(event5, listener.events.get(4));
        assertEquals(event3a, listener.events.get(5));
        assertEquals(event1a, listener.events.get(6));
        assertEquals(event2a, listener.events.get(7));
        assertEquals(event4a, listener.events.get(8));
        assertEquals(event5a, listener.events.get(9));
    }

    public void testInterruptAllFull2NotFoundOld() {
        log.info("Starting testInterruptAllFull2NotFoundOld");
        eventList.setFastInterrupts(false);
        eventList.scheduleEvent(event1);
        eventList.scheduleEvent(event2);
        eventList.scheduleEvent(event3);
        eventList.scheduleEvent(event4);
        eventList.scheduleEvent(event5);
        eventList.scheduleEvent(event1a);
        eventList.scheduleEvent(event2a);
        eventList.scheduleEvent(event3a);
        eventList.scheduleEvent(event4a);
        eventList.scheduleEvent(event5a);

        eventList.interruptAll(entity2, "This", new Object[] {entity2});
        eventList.interruptAll(entity1, "That", new Object[] {entity2});
        eventList.interruptAll(entity1, "This", new Object[] {entity3});
        eventList.interruptAll(entity1, "This", new Object[] {null});

        eventList.startSimulation();
        assertEquals(10, listener.events.size());
        assertEquals(event3, listener.events.get(0));
        assertEquals(event1, listener.events.get(1));
        assertEquals(event2, listener.events.get(2));
        assertEquals(event4, listener.events.get(3));
        assertEquals(event5, listener.events.get(4));
        assertEquals(event3a, listener.events.get(5));
        assertEquals(event1a, listener.events.get(6));
        assertEquals(event2a, listener.events.get(7));
        assertEquals(event4a, listener.events.get(8));
        assertEquals(event5a, listener.events.get(9));
    }

    public void testInterruptAllEntityNotFoundOld() {
        log.info("Starting testInterruptAllEntityNotFoundOld");
        eventList.setFastInterrupts(false);

        eventList.scheduleEvent(event1);
        eventList.scheduleEvent(event2);
        eventList.scheduleEvent(event3);
        eventList.scheduleEvent(event4);
        eventList.scheduleEvent(event5);
        eventList.scheduleEvent(event1a);
        eventList.scheduleEvent(event2a);
        eventList.scheduleEvent(event3a);
        eventList.scheduleEvent(event4a);
        eventList.scheduleEvent(event5a);

        eventList.interruptAll(entity2);
        eventList.startSimulation();
        assertEquals(10, listener.events.size());
        assertEquals(event3, listener.events.get(0));
        assertEquals(event1, listener.events.get(1));
        assertEquals(event2, listener.events.get(2));
        assertEquals(event4, listener.events.get(3));
        assertEquals(event5, listener.events.get(4));
        assertEquals(event3a, listener.events.get(5));
        assertEquals(event1a, listener.events.get(6));
        assertEquals(event2a, listener.events.get(7));
        assertEquals(event4a, listener.events.get(8));
        assertEquals(event5a, listener.events.get(9));
    }

    public void testClearDeadEvents() {
        eventList.scheduleEvent(event1);
        eventList.scheduleEvent(event2);
        eventList.scheduleEvent(event3);
        eventList.scheduleEvent(event4);
        eventList.scheduleEvent(event5);
        eventList.scheduleEvent(event1a);
        eventList.scheduleEvent(event2a);
        eventList.scheduleEvent(event3a);
        eventList.scheduleEvent(event4a);
        eventList.scheduleEvent(event5a);

        assertEquals(10, eventList.eventList.size());
        assertTrue(eventList.eventList.contains(event1));
        assertTrue(eventList.eventList.contains(event2));
        assertTrue(eventList.eventList.contains(event3));
        assertTrue(eventList.eventList.contains(event4));
        assertTrue(eventList.eventList.contains(event5));
        assertTrue(eventList.eventList.contains(event1a));
        assertTrue(eventList.eventList.contains(event2a));
        assertTrue(eventList.eventList.contains(event3a));
        assertTrue(eventList.eventList.contains(event4a));
        assertTrue(eventList.eventList.contains(event5a));

        Set set = (Set)eventList.entityEventMap.get(entity1);
        assertEquals(4, set.size());
        assertTrue(set.contains(event1));
        assertTrue(set.contains(event2));
        assertTrue(set.contains(event1a));
        assertTrue(set.contains(event2a));
        set = (Set)eventList.entityEventMap.get(entity3);
        assertEquals(4, set.size());
        assertTrue(set.contains(event3));
        assertTrue(set.contains(event4));
        assertTrue(set.contains(event3a));
        assertTrue(set.contains(event4a));
        set = (Set)eventList.entityEventMap.get(entity5);
        assertEquals(2, set.size());
        assertTrue(set.contains(event5));
        assertTrue(set.contains(event5a));

        set = (Set)eventList.hashEventMap.get(event1.getEventHash());
        assertTrue(set.contains(event1));
        set = (Set)eventList.hashEventMap.get(event2.getEventHash());
        assertTrue(set.contains(event2));
        set = (Set)eventList.hashEventMap.get(event3.getEventHash());
        assertTrue(set.contains(event3));
        set = (Set)eventList.hashEventMap.get(event4.getEventHash());
        assertTrue(set.contains(event4));
        set = (Set)eventList.hashEventMap.get(event5.getEventHash());
        assertTrue(set.contains(event5));
        set = (Set)eventList.hashEventMap.get(event1a.getEventHash());
        assertTrue(set.contains(event1a));
        set = (Set)eventList.hashEventMap.get(event2a.getEventHash());
        assertTrue(set.contains(event2a));
        set = (Set)eventList.hashEventMap.get(event3a.getEventHash());
        assertTrue(set.contains(event3a));
        set = (Set)eventList.hashEventMap.get(event4a.getEventHash());
        assertTrue(set.contains(event4a));
        set = (Set)eventList.hashEventMap.get(event5a.getEventHash());
        assertTrue(set.contains(event5a));

        event3.interrupt();
        event1.interrupt();
        event5.interrupt();

        eventList.clearDeadEvents();

        assertEquals(8, eventList.eventList.size());
        assertFalse(eventList.eventList.contains(event1));
        assertTrue(eventList.eventList.contains(event2));
        assertFalse(eventList.eventList.contains(event3));
        assertTrue(eventList.eventList.contains(event4));
        assertTrue(eventList.eventList.contains(event5));
        assertTrue(eventList.eventList.contains(event1a));
        assertTrue(eventList.eventList.contains(event2a));
        assertTrue(eventList.eventList.contains(event3a));
        assertTrue(eventList.eventList.contains(event4a));
        assertTrue(eventList.eventList.contains(event5a));

        set = (Set)eventList.entityEventMap.get(entity1);
        assertEquals(3, set.size());
        assertFalse(set.contains(event1));
        assertTrue(set.contains(event2));
        assertTrue(set.contains(event1a));
        assertTrue(set.contains(event2a));
        set = (Set)eventList.entityEventMap.get(entity3);
        assertEquals(3, set.size());
        assertFalse(set.contains(event3));
        assertTrue(set.contains(event4));
        assertTrue(set.contains(event3a));
        assertTrue(set.contains(event4a));
        set = (Set)eventList.entityEventMap.get(entity5);
        assertEquals(2, set.size());
        assertTrue(set.contains(event5));
        assertTrue(set.contains(event5a));

        set = (Set)eventList.hashEventMap.get(event1.getEventHash());
        assertFalse(set != null && set.contains(event1));
        set = (Set)eventList.hashEventMap.get(event2.getEventHash());
        assertTrue(set.contains(event2));
        set = (Set)eventList.hashEventMap.get(event3.getEventHash());
        assertFalse(set != null && set.contains(event3));
        set = (Set)eventList.hashEventMap.get(event4.getEventHash());
        assertTrue(set.contains(event4));
        set = (Set)eventList.hashEventMap.get(event5.getEventHash());
        assertTrue(set.contains(event5));
        set = (Set)eventList.hashEventMap.get(event1a.getEventHash());
        assertTrue(set.contains(event1a));
        set = (Set)eventList.hashEventMap.get(event2a.getEventHash());
        assertTrue(set.contains(event2a));
        set = (Set)eventList.hashEventMap.get(event3a.getEventHash());
        assertTrue(set.contains(event3a));
        set = (Set)eventList.hashEventMap.get(event4a.getEventHash());
        assertTrue(set.contains(event4a));
        set = (Set)eventList.hashEventMap.get(event5a.getEventHash());
        assertTrue(set.contains(event5a));
    }

    public void testClearDeadEventsOld() {
        eventList.setFastInterrupts(false);
        eventList.scheduleEvent(event1);
        eventList.scheduleEvent(event2);
        eventList.scheduleEvent(event3);
        eventList.scheduleEvent(event4);
        eventList.scheduleEvent(event5);
        eventList.scheduleEvent(event1a);
        eventList.scheduleEvent(event2a);
        eventList.scheduleEvent(event3a);
        eventList.scheduleEvent(event4a);
        eventList.scheduleEvent(event5a);

        assertEquals(10, eventList.eventList.size());
        assertTrue(eventList.eventList.contains(event1));
        assertTrue(eventList.eventList.contains(event2));
        assertTrue(eventList.eventList.contains(event3));
        assertTrue(eventList.eventList.contains(event4));
        assertTrue(eventList.eventList.contains(event5));
        assertTrue(eventList.eventList.contains(event1a));
        assertTrue(eventList.eventList.contains(event2a));
        assertTrue(eventList.eventList.contains(event3a));
        assertTrue(eventList.eventList.contains(event4a));
        assertTrue(eventList.eventList.contains(event5a));


        event3.interrupt();
        event1.interrupt();
        event5.interrupt();

        eventList.clearDeadEvents();

        assertEquals(8, eventList.eventList.size());
        assertFalse(eventList.eventList.contains(event1));
        assertTrue(eventList.eventList.contains(event2));
        assertFalse(eventList.eventList.contains(event3));
        assertTrue(eventList.eventList.contains(event4));
        assertTrue(eventList.eventList.contains(event5));
        assertTrue(eventList.eventList.contains(event1a));
        assertTrue(eventList.eventList.contains(event2a));
        assertTrue(eventList.eventList.contains(event3a));
        assertTrue(eventList.eventList.contains(event4a));
        assertTrue(eventList.eventList.contains(event5a));

    }

    public void testBadReset() {
        EvilResetter resetter = new EvilResetter();
        Schedule.reset();
        resetter.waitDelay("Reset", 1.0);
        try {
            Schedule.startSimulation();
            fail("Should have thrown a SimkitConcurrencyException");
        } catch (EventListTestException e) {
            assertEquals(1.0, Schedule.getSimTime(), 0.0);
        }
    }

    public void testBadColdReset() {
        EvilResetter resetter = new EvilResetter();
        Schedule.reset();
        resetter.waitDelay("ColdReset", 1.0);
        try {
            Schedule.startSimulation();
            fail("Should have thrown a SimkitConcurrencyException");
        } catch (EventListTestException e) {
            assertEquals(1.0, Schedule.getSimTime(), 0.0);
        }
    }

    public void testBadStartSimulation() {
        EvilResetter resetter = new EvilResetter();
        Schedule.reset();
        resetter.waitDelay("StartAgain", 1.0);
        try {
            Schedule.startSimulation();
            fail("Should have thrown a SimkitConcurrencyException");
        } catch (EventListTestException e) {
            assertEquals(1.0, Schedule.getSimTime(), 0.0);
        }
    }

    public void testBug1205() {
        eventList.scheduleEvent(event1);
        eventList.scheduleEvent(event2);
        eventList.scheduleEvent(event3);
        eventList.scheduleEvent(event4);
        eventList.scheduleEvent(event5);
        eventList.scheduleEvent(event1a);
        eventList.scheduleEvent(event2a);
        eventList.scheduleEvent(event3a);
        eventList.scheduleEvent(event4a);
        eventList.scheduleEvent(event5a);
        assertFalse(eventList.eventList.isEmpty());
        assertFalse(eventList.entityEventMap.isEmpty());
        assertFalse(eventList.hashEventMap.isEmpty());
        eventList.clearEventList();
        assertTrue(eventList.eventList.isEmpty());
        assertTrue(eventList.entityEventMap.isEmpty());
        assertTrue(eventList.hashEventMap.isEmpty());
    }

    public void testBug1205a() {
        eventList.scheduleEvent(event1);
        eventList.scheduleEvent(event2);
        eventList.scheduleEvent(event3);
        eventList.scheduleEvent(event4);
        eventList.scheduleEvent(event5);
        eventList.scheduleEvent(event1a);
        eventList.scheduleEvent(event2a);
        eventList.scheduleEvent(event3a);
        eventList.scheduleEvent(event4a);
        eventList.scheduleEvent(event5a);
        assertFalse(eventList.eventList.isEmpty());
        assertFalse(eventList.entityEventMap.isEmpty());
        assertFalse(eventList.hashEventMap.isEmpty());
        eventList.reset();
        assertTrue(eventList.eventList.isEmpty());
        assertTrue(eventList.entityEventMap.isEmpty());
        assertTrue(eventList.hashEventMap.isEmpty());
    }

    public void testBug1205b() {
        eventList.scheduleEvent(event1);
        eventList.scheduleEvent(event2);
        eventList.scheduleEvent(event3);
        eventList.scheduleEvent(event4);
        eventList.scheduleEvent(event5);
        eventList.scheduleEvent(event1a);
        eventList.scheduleEvent(event2a);
        eventList.scheduleEvent(event3a);
        eventList.scheduleEvent(event4a);
        eventList.scheduleEvent(event5a);
        assertFalse(eventList.eventList.isEmpty());
        assertFalse(eventList.entityEventMap.isEmpty());
        assertFalse(eventList.hashEventMap.isEmpty());
        eventList.coldReset();
        assertTrue(eventList.eventList.isEmpty());
        assertTrue(eventList.entityEventMap.isEmpty());
        assertTrue(eventList.hashEventMap.isEmpty());
    }

/**
* Makes sure not NullPointerException when calling removeRerun
* with fast interrupts false.
**/
    public void testRemoveRerun1() {
        eventList.setFastInterrupts(false);
        SimEntity entity = new SimEntityBase() {};
        Set<ReRunnable> reRuns = eventList.getRerun();
        assertTrue(reRuns.contains(entity));
        eventList.removeRerun(entity);
        reRuns = eventList.getRerun();
        assertFalse(reRuns.contains(entity));
    }

/**
* Stores the events it hears in a List in the order it heard them.
**/
    public class SimpleEventListener implements SimEventListener {

        protected List events = new ArrayList();

        public void processSimEvent(SimEvent event) {
            events.add(event);
        }
    }

/**
* Attempts to call startSimulation(), reset() and coldReset() from a events.
**/
    public class EvilResetter extends SimEntityBase {

        public void doReset() {
            try {
                Schedule.reset();
            }
            catch (SimkitConcurrencyException e) {
                throw (new EventListTestException());
            }
        }

        public void doColdReset() {
            try {
                Schedule.coldReset();
            }
            catch (SimkitConcurrencyException e) {
                throw (new EventListTestException());
            }

        }

        public void doStartAgain() {
            try {
                Schedule.startSimulation();
            }
            catch (SimkitConcurrencyException e) {
                throw (new EventListTestException());
            }

        }
    }
    
}

