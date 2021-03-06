package simkit.smd;

import simkit.SimEntityBase;

/**
 * @author ahbuss
 */
public class ConstantTimeMediator extends SimEntityBase
        implements SensorMoverMediator<BasicLinearMover, ConstantTimeSensor> {

    /**
     * Schedule Detection event on the sensor with a delay given by that
     * sensor's timeToDetect parameter. Do this only if the sensor instance has
     * not yet detected the target. Note that a reference to the actual Mover
     * instance is passed to the Sensor instance. Also, the waitDelay() is
     * invoked on the sensor, thus making it appear that their scheduled its own
     * Detection event. This is for the purpose of components who wish to listen
     * to the sensor for its Detection events.
     *
     * @param mover the Mover that just entered the range of the Sensor
     * @param sensor the Sensor whose range was just entered
     */
    @Override
    public void doEnterRange(BasicLinearMover mover, ConstantTimeSensor sensor) {
        if (!sensor.getContacts().contains(mover)) {
            sensor.waitDelay("Detection", sensor.getTimeToDetect(), mover);
        }
    }

    /**
     * If mover not yet detected, cancel Detection event of sensor.<br>
     * If already detected, schedule Undetection on sensor.
     *
     * @param mover Given BasicLinearMover
     * @param sensor Given ConstantTimeSensor
     */
    @Override
    public void doExitRange(BasicLinearMover mover, ConstantTimeSensor sensor) {
        if (!sensor.getContacts().contains(mover)) {
            sensor.interrupt("Detection", mover);
        }
        if (sensor.getContacts().contains(mover)) {
            sensor.waitDelay("Undetection", 0.0, mover);
        }
    }
}
