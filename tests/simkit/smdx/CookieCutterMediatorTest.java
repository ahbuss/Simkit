
package simkit.smdx;

import junit.framework.*;

import simkit.Schedule;
public class CookieCutterMediatorTest extends TestCase {
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
                contact = new Contact((Mover)target);
                contacts.put(target, contact);
            }
            System.out.println("TestMediator providing contact " + 
                    contact.toString() + " for target " + target.toString());
            return contact;
        }
    }

    protected CookieCutterMediator mediator;




    public void setUp() {
        mediator = new TestMediator();
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
