
package simkit.smdx;

import junit.framework.*;

import simkit.Schedule;
public class CookieCutterMediatorTest extends TestCase {
    
    protected CookieCutterMediator mediator;




    public void setUp() {
        mediator = new CookieCutterMediator();
    }

    public void tearDown() {
        mediator = null;
    }

/**
* Prior to fixing the bug, the contacts HashMap will accumulate entries even after resets;
* After fixing the bug is should be cleared after a reset.
**/
    public void testBug526() {
        SensorTargetMediatorFactory.addMediator(CookieCutterSensor.class, 
                                                SimpleTarget.class,
                                                mediator);
        CookieCutterSensor sensor = new CookieCutterSensor(999);
        Mover mover = new SimpleTarget(new java.awt.geom.Point2D.Double(0,0),999);
        mediator.doEnterRange(sensor, mover);
        assertEquals(1, mediator.contacts.size());
        sensor = new CookieCutterSensor(999);
        mover = new SimpleTarget(new java.awt.geom.Point2D.Double(0,0),999);
        mediator.doEnterRange(sensor, mover);
        assertEquals(2, mediator.contacts.size());
        Schedule.reset();
        assertEquals(0, mediator.contacts.size());
    }
}
