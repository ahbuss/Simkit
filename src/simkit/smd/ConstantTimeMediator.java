package simkit.smd;

import simkit.SimEntityBase;

/**
 * @version $Id: ConstantTimeMediator.java 81 2009-11-16 22:28:39Z ahbuss $
 * @author ahbuss
 */
public class ConstantTimeMediator extends SimEntityBase
        implements SensorMoverMediator<BasicLinearMover, ConstantTimeSensor> {

    public void doEnterRange(BasicLinearMover mover, ConstantTimeSensor sensor) {
        if (!sensor.getContacts().contains(mover)) {
            sensor.waitDelay("Detection", sensor.getTimeToDetect(), mover);
        }
    }

    public void doExitRange(BasicLinearMover mover, ConstantTimeSensor sensor) {
        if (!sensor.getContacts().contains(mover)) {
            sensor.interrupt("Detection", mover);
        }
        if (sensor.getContacts().contains(mover)) {
            sensor.waitDelay("Undetection", 0.0, mover);
        }
    }
}