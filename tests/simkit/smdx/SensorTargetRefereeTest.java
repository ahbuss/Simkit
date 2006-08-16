
package simkit.smdx;

import java.awt.geom.Point2D;
import java.util.*;

import junit.framework.*;

import simkit.*;

public class SensorTargetRefereeTest extends TestCase {
    
    protected CookieCutterMediator mediator;
    protected SensorTargetReferee referee;




    public void setUp() {
        Schedule.coldReset();
        mediator = new CookieCutterMediator();
        referee = new SensorTargetReferee();
        SensorTargetMediatorFactory.addMediator(CookieCutterSensor.class, 
                                                SimpleTarget.class,
                                                mediator);
    }

    public void tearDown() {
        mediator = null;
        referee = null;
    }

/**
* SensorTargetReferee will only clear the HashMaps if clearOnReset is true.
**/
    public void testBug526() {
        CookieCutterSensor sensor = new CookieCutterSensor(999);
        Mover mover = new SimpleTarget(new java.awt.geom.Point2D.Double(0,0),999);
        referee.register(sensor);
        referee.register(mover);
        assertEquals(1, referee.sensors.size());
        assertEquals(1, referee.targets.size());
        Schedule.reset();

        assertEquals(1, referee.sensors.size());
        assertEquals(1, referee.targets.size());
        referee.setClearOnReset(true);
        Schedule.reset();
        assertEquals(0, referee.sensors.size());
        assertEquals(0, referee.targets.size());
    }

/**
* Mover starts outside, changes motion on line prior to EnterRange.
**/
    public void testBug490a() {
        Mover sensorMover = new SimpleTarget(new java.awt.geom.Point2D.Double(0,0),0.0);
        CookieCutterSensor sensor = new CookieCutterSensor(100.0, sensorMover);
        Mover mover = new SimpleTarget(new java.awt.geom.Point2D.Double(200.0,0.0),100.0);
        referee.register(sensor);
        referee.register(mover);
        sensorMover.reset();
        sensor.reset();
        mover.reset();
        mover.moveTo(new Point2D.Double(0.0, 0.0));
        SortedSet events = simkit.Helper.getEventSet();
        SimEvent event = (SimEvent)events.first();
        assertEquals("doStartMove", event.getMethodName());
        assertEquals(mover, event.getSource());
        assertEquals(0.0, event.getScheduledTime(), 0.0);
        events.remove(event);
        mover.doStartMove(mover);
        event = (SimEvent)events.first();
        assertEquals("doEnterRange", event.getMethodName());
        assertEquals(referee, event.getSource());
        assertEquals(1.0, event.getScheduledTime(), 0.0);
        simkit.Helper.setSimTime(1.0);
        mover.moveTo(new Point2D.Double(0.0, 0.0)); 
        event = (SimEvent)events.first();
        assertEquals("doEnterRange", event.getMethodName());
        assertEquals(referee, event.getSource());
        assertEquals(1.0, event.getScheduledTime(), 0.0);
        events.remove(event);
        referee.doEnterRange(sensor, mover);
        event = (SimEvent)events.first();
        assertEquals("doStartMove", event.getMethodName());
        assertEquals(mover, event.getSource());
        assertEquals(1.0, event.getScheduledTime(), 0.0);
        events.remove(event);
        mover.doStartMove(mover);
        event = (SimEvent)events.first();
        assertEquals("doEndMove", event.getMethodName());
        assertEquals(mover, event.getSource());
        assertEquals(2.0, event.getScheduledTime(), 0.0);
        events.remove(event);
        event = (SimEvent)events.first();
        assertEquals("doExitRange", event.getMethodName());
        assertEquals(referee, event.getSource());
        assertEquals(3.0, event.getScheduledTime(), 0.0);
        events.remove(event);
        assertTrue("Should have been empty events=" + events, events.isEmpty());
    }


/**
* Initializing out moving out.
* Should schedule no events.
**/
    public void testProcessTarget1() {
        Mover sensorMover = new SimpleTarget(new java.awt.geom.Point2D.Double(0,0),0.0);
        CookieCutterSensor sensor = new CookieCutterSensor(100.0, sensorMover);
        SimpleTarget mover = new SimpleTarget(new java.awt.geom.Point2D.Double(200.0,0.0),100.0);
        referee.register(sensor);
        referee.register(mover);
        sensorMover.reset();
        sensor.reset();
        mover.reset();
        mover.velocity = new Point2D.Double(100.0, 0.0);
        referee.processTarget(mover);
        SortedSet events = simkit.Helper.getEventSet();
        assertEquals(0, events.size());
    }

/**
* Initializing out moving in.
* Should schedule EnterRange.
**/
    public void testProcessTarget2() {
        Mover sensorMover = new SimpleTarget(new java.awt.geom.Point2D.Double(0,0),0.0);
        CookieCutterSensor sensor = new CookieCutterSensor(100.0, sensorMover);
        SimpleTarget mover = new SimpleTarget(new java.awt.geom.Point2D.Double(200.0,0.0),100.0);
        referee.register(sensor);
        referee.register(mover);
        sensorMover.reset();
        sensor.reset();
        mover.reset();
        mover.velocity = new Point2D.Double(-100.0, 0.0);
        referee.processTarget(mover);
        SortedSet events = simkit.Helper.getEventSet();
        assertEquals(1, events.size());
        SimEvent event = (SimEvent)events.first();
        assertEquals("doEnterRange", event.getMethodName());
    }

/**
* Initializing on line moving in.
* Should schedule EnterRange.
**/
    public void testProcessTarget3() {
        Mover sensorMover = new SimpleTarget(new java.awt.geom.Point2D.Double(0,0),0.0);
        CookieCutterSensor sensor = new CookieCutterSensor(100.0, sensorMover);
        SimpleTarget mover = new SimpleTarget(new java.awt.geom.Point2D.Double(100.0,0.0),100.0);
        referee.register(sensor);
        referee.register(mover);
        sensorMover.reset();
        sensor.reset();
        mover.reset();
        mover.velocity = new Point2D.Double(-100.0, 0.0);
        referee.processTarget(mover);
        SortedSet events = simkit.Helper.getEventSet();
        assertEquals(1, events.size());
        SimEvent event = (SimEvent)events.first();
        assertEquals("doEnterRange", event.getMethodName());
    }

/**
* Initializing on line moving out.
* Should schedule nothing.
**/
    public void testProcessTarget4() {
        Mover sensorMover = new SimpleTarget(new java.awt.geom.Point2D.Double(0,0),0.0);
        CookieCutterSensor sensor = new CookieCutterSensor(100.0, sensorMover);
        SimpleTarget mover = new SimpleTarget(new java.awt.geom.Point2D.Double(100.0,0.0),100.0);
        referee.register(sensor);
        referee.register(mover);
        sensorMover.reset();
        sensor.reset();
        mover.reset();
        mover.velocity = new Point2D.Double(100.0, 0.0);
        referee.processTarget(mover);
        SortedSet events = simkit.Helper.getEventSet();
        assertEquals("events=" + events, 0, events.size());
    }

/**
* Initializing inside moving out.
* Should schedule nothing.
**/
    public void testProcessTarget5() {
        Mover sensorMover = new SimpleTarget(new java.awt.geom.Point2D.Double(0,0),0.0);
        CookieCutterSensor sensor = new CookieCutterSensor(100.0, sensorMover);
        SimpleTarget mover = new SimpleTarget(new java.awt.geom.Point2D.Double(10.0,0.0),100.0);
        referee.register(sensor);
        referee.register(mover);
        sensorMover.reset();
        sensor.reset();
        mover.reset();
        mover.velocity = new Point2D.Double(-100.0, 0.0);
        referee.processTarget(mover);
        SortedSet events = simkit.Helper.getEventSet();
        assertEquals(1, events.size());
        SimEvent event = (SimEvent)events.first();
        assertEquals("doExitRange", event.getMethodName());
    }

/**
* doEnterRange then start outside moving in. Should schedule ExitRange since
* the state map overrides the actual location.
**/
    public void testProcessTarget6() {
        Mover sensorMover = new SimpleTarget(new java.awt.geom.Point2D.Double(0,0),0.0);
        CookieCutterSensor sensor = new CookieCutterSensor(100.0, sensorMover);
        SimpleTarget mover = new SimpleTarget(new java.awt.geom.Point2D.Double(1000.0,0.0),100.0);
        referee.register(sensor);
        referee.register(mover);
        sensorMover.reset();
        sensor.reset();
        mover.reset();
        mover.velocity = new Point2D.Double(-100.0, 0.0);
        referee.doEnterRange(sensor, mover);
        referee.processTarget(mover);
        SortedSet events = simkit.Helper.getEventSet();
        assertEquals(1, events.size());
        SimEvent event = (SimEvent)events.first();
        assertEquals("events=" + events, "doExitRange", event.getMethodName());
    }

/**
* doExitRange then start inside moving out. Should schedule EnterRange since
* the state map overrides the actual location.
**/
    public void testProcessTarget7() {
        Mover sensorMover = new SimpleTarget(new java.awt.geom.Point2D.Double(0,0),0.0);
        CookieCutterSensor sensor = new CookieCutterSensor(100.0, sensorMover);
        SimpleTarget mover = new SimpleTarget(new java.awt.geom.Point2D.Double(10.0,0.0),100.0);
        referee.register(sensor);
        referee.register(mover);
        sensorMover.reset();
        sensor.reset();
        mover.reset();
        mover.velocity = new Point2D.Double(100.0, 0.0);
        referee.doExitRange(sensor, mover);
        referee.processTarget(mover);
        SortedSet events = simkit.Helper.getEventSet();
        assertEquals(1, events.size());
        SimEvent event = (SimEvent)events.first();
        assertEquals("events=" + events, "doEnterRange", event.getMethodName());
    }


/**
* Initializing out moving out.
* Should schedule no events.
**/
    public void testProcessSensor1() {
        Mover sensorMover = new SimpleTarget(new java.awt.geom.Point2D.Double(0,0),0.0);
        CookieCutterSensor sensor = new CookieCutterSensor(100.0, sensorMover);
        SimpleTarget mover = new SimpleTarget(new java.awt.geom.Point2D.Double(200.0,0.0),100.0);
        referee.register(sensor);
        referee.register(mover);
        sensorMover.reset();
        sensor.reset();
        mover.reset();
        mover.velocity = new Point2D.Double(100.0, 0.0);
        referee.processSensor(sensor);
        SortedSet events = simkit.Helper.getEventSet();
        assertEquals(0, events.size());
    }

/**
* Initializing out moving in.
* Should schedule EnterRange.
**/
    public void testProcessSensor2() {
        Mover sensorMover = new SimpleTarget(new java.awt.geom.Point2D.Double(0,0),0.0);
        CookieCutterSensor sensor = new CookieCutterSensor(100.0, sensorMover);
        SimpleTarget mover = new SimpleTarget(new java.awt.geom.Point2D.Double(200.0,0.0),100.0);
        referee.register(sensor);
        referee.register(mover);
        sensorMover.reset();
        sensor.reset();
        mover.reset();
        mover.velocity = new Point2D.Double(-100.0, 0.0);
        referee.processSensor(sensor);
        SortedSet events = simkit.Helper.getEventSet();
        assertEquals(1, events.size());
        SimEvent event = (SimEvent)events.first();
        assertEquals("doEnterRange", event.getMethodName());
    }

/**
* Initializing on line moving in.
* Should schedule EnterRange.
**/
    public void testProcessSensor3() {
        Mover sensorMover = new SimpleTarget(new java.awt.geom.Point2D.Double(0,0),0.0);
        CookieCutterSensor sensor = new CookieCutterSensor(100.0, sensorMover);
        SimpleTarget mover = new SimpleTarget(new java.awt.geom.Point2D.Double(100.0,0.0),100.0);
        referee.register(sensor);
        referee.register(mover);
        sensorMover.reset();
        sensor.reset();
        mover.reset();
        mover.velocity = new Point2D.Double(-100.0, 0.0);
        referee.processSensor(sensor);
        SortedSet events = simkit.Helper.getEventSet();
        assertEquals(1, events.size());
        SimEvent event = (SimEvent)events.first();
        assertEquals("doEnterRange", event.getMethodName());
    }

/**
* Initializing on line moving out.
* Should schedule nothing.
**/
    public void testProcessSensor4() {
        Mover sensorMover = new SimpleTarget(new java.awt.geom.Point2D.Double(0,0),0.0);
        CookieCutterSensor sensor = new CookieCutterSensor(100.0, sensorMover);
        SimpleTarget mover = new SimpleTarget(new java.awt.geom.Point2D.Double(100.0,0.0),100.0);
        referee.register(sensor);
        referee.register(mover);
        sensorMover.reset();
        sensor.reset();
        mover.reset();
        mover.velocity = new Point2D.Double(100.0, 0.0);
        referee.processSensor(sensor);
        SortedSet events = simkit.Helper.getEventSet();
        assertEquals("events=" + events, 0, events.size());
    }

/**
* Initializing inside moving out.
* Should schedule nothing.
**/
    public void testProcessSensor5() {
        Mover sensorMover = new SimpleTarget(new java.awt.geom.Point2D.Double(0,0),0.0);
        CookieCutterSensor sensor = new CookieCutterSensor(100.0, sensorMover);
        SimpleTarget mover = new SimpleTarget(new java.awt.geom.Point2D.Double(10.0,0.0),100.0);
        referee.register(sensor);
        referee.register(mover);
        sensorMover.reset();
        sensor.reset();
        mover.reset();
        mover.velocity = new Point2D.Double(-100.0, 0.0);
        referee.processSensor(sensor);
        SortedSet events = simkit.Helper.getEventSet();
        assertEquals(1, events.size());
        SimEvent event = (SimEvent)events.first();
        assertEquals("doExitRange", event.getMethodName());
    }

/**
* doEnterRange then start outside moving in. Should schedule ExitRange since
* the state map overrides the actual location.
**/
    public void testProcessSensor6() {
        Mover sensorMover = new SimpleTarget(new java.awt.geom.Point2D.Double(0,0),0.0);
        CookieCutterSensor sensor = new CookieCutterSensor(100.0, sensorMover);
        SimpleTarget mover = new SimpleTarget(new java.awt.geom.Point2D.Double(1000.0,0.0),100.0);
        referee.register(sensor);
        referee.register(mover);
        sensorMover.reset();
        sensor.reset();
        mover.reset();
        mover.velocity = new Point2D.Double(-100.0, 0.0);
        referee.doEnterRange(sensor, mover);
        referee.processSensor(sensor);
        SortedSet events = simkit.Helper.getEventSet();
        assertEquals(1, events.size());
        SimEvent event = (SimEvent)events.first();
        assertEquals("events=" + events, "doExitRange", event.getMethodName());
    }

/**
* doExitRange then start inside moving out. Should schedule EnterRange since
* the state map overrides the actual location.
**/
    public void testProcessSensor7() {
        Mover sensorMover = new SimpleTarget(new java.awt.geom.Point2D.Double(0,0),0.0);
        CookieCutterSensor sensor = new CookieCutterSensor(100.0, sensorMover);
        SimpleTarget mover = new SimpleTarget(new java.awt.geom.Point2D.Double(10.0,0.0),100.0);
        referee.register(sensor);
        referee.register(mover);
        sensorMover.reset();
        sensor.reset();
        mover.reset();
        mover.velocity = new Point2D.Double(100.0, 0.0);
        referee.doExitRange(sensor, mover);
        referee.processSensor(sensor);
        SortedSet events = simkit.Helper.getEventSet();
        assertEquals(1, events.size());
        SimEvent event = (SimEvent)events.first();
        assertEquals("events=" + events, "doEnterRange", event.getMethodName());
    }
}
