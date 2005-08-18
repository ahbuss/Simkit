
package simkit.smdx;

import junit.framework.*;

import simkit.Schedule;
public class SensorTargetRefereeTest extends TestCase {
    
    protected CookieCutterMediator mediator;
    protected SensorTargetReferee referee;




    public void setUp() {
        mediator = new CookieCutterMediator();
        referee = new SensorTargetReferee();
    }

    public void tearDown() {
        mediator = null;
        referee = null;
    }

/**
* SensorTargetReferee will only clear the HashMaps if clearOnReset is true.
**/
    public void testBug526() {
        SensorTargetMediatorFactory.addMediator(CookieCutterSensor.class, 
                                                SimpleTarget.class,
                                                mediator);
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
}
