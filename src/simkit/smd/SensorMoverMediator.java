package simkit.smd;

import simkit.SimEntity;

/**
 * Interface with the two key methods for scheduling Detection and
 * Undetection events. A concrete implementation will specify
 * the type of Mover and Sensor on which it will be used.
 * @version $Id: SensorMoverMediator.java 81 2009-11-16 22:28:39Z ahbuss $
 * @author ahbuss
 */
public interface SensorMoverMediator <M extends Mover, S extends Sensor> 
        extends SimEntity {

    /**
     * Schedules Detection event for sensor. This may involve a
     * delegate to hide the Mover's actual values.
     * @param mover The target
     * @param sensor The Sensor
     */
    public void doEnterRange(M mover, S sensor);

    /**
     * Typically cancels a pending Detection, if any, and schedules
     * Undetection, if necessary.
     * @param mover The target
     * @param sensor The Sensor
     */
    public void doExitRange(M mover, S sensor);
}
