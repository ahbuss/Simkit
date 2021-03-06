package simkit.smd;

import simkit.SimEntityBase;

/**
 * Simplest Mediator. Schedules Detections and Undetections immediately.
 *
 * @author ahbuss
 */
public class CookieCutterMediator extends SimEntityBase implements
        SensorMoverMediator<Mover, Sensor> {

    /**
     * Schedule Detection(mover) on sensor with delay of 0.0 if the sensor
     * hasn't already detected the target.
     *
     * @param mover The target
     * @param sensor The Sensor
     */
    @Override
    public void doEnterRange(Mover mover, Sensor sensor) {
        if (!sensor.getContacts().contains(mover)) {
            sensor.waitDelay("Detection", 0.0, mover);
        }
    }

    /**
     * Schedule Undetection(mover) with delay of 0.0.
     *
     * @param mover The target
     * @param sensor The Sensor
     */
    @Override
    public void doExitRange(Mover mover, Sensor sensor) {
        sensor.waitDelay("Undetection", 0.0, mover);
    }
}
