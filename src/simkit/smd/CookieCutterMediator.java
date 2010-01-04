package simkit.smd;

import simkit.SimEntityBase;

/**
 * Simplest Mediator. Schedules Detections and Undetections immediately.
 * @version $Id: CookieCutterMediator.java 81 2009-11-16 22:28:39Z ahbuss $
 * @author ahbuss
 */
public class CookieCutterMediator extends SimEntityBase implements
        SensorMoverMediator<BasicLinearMover, CookieCutterSensor>{

    /**
     * Schedule Detection(mover) on sensor with delay of 0.0 if the
     * sensor hasn't already detected the target.
     * @param mover The target
     * @param sensor The Sensor
     */
    public void doEnterRange(BasicLinearMover mover, CookieCutterSensor sensor) {
        if (!sensor.getContacts().contains(mover)) {
            sensor.waitDelay("Detection", 0.0, mover);
        }
    }

    /**
     * Schedule Undetection(mover) with delay of 0.0.
     * @param mover The target
     * @param sensor The Sensor
     */
    public void doExitRange(BasicLinearMover mover, CookieCutterSensor sensor) {
        sensor.waitDelay("Undetection", 0.0, mover);
    }
}