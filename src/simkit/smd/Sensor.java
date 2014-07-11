package simkit.smd;

import java.awt.geom.Point2D;
import java.util.Set;
import simkit.SimEntity;

/**
 *
 * Interface for Sensor. This has all the methods that can be used
 * for linear movement as well as a placeholder for implementing
 * accelerated movement.
 * @version $Id: Sensor.java 81 2009-11-16 22:28:39Z ahbuss $
 * @author ahbuss
 */
public interface Sensor extends SimEntity {

    /**
     * Typically delegated to Mover instance
     * @return current location of Sensor
     */
    public Point2D getCurrentLocation();

    /**
     * Typically delegated to Mover instance
     * @return current velocity of Sensor
     */
    public Point2D getVelocity();

    /**
     * Typically delegated to Mover instance. This is always 0.0
     * under constant velocity motion.
     * @return current acceleration of Sensor
     */
    public double getAcceleration();

    /**
     * @return maximim range of this Sensor
     */
    public double getMaxRange();

    /**
     * Parameter is for listeners to know which Sensor has started moving
     * @param sensor Reference to this Sensor
     */
    public void doStartMove(Sensor sensor);

    /**
     * Parameter is for listeners to know which Sensor has stopped
     * @param sensor Reference to this Sensor
     */
    public void doStop(Sensor sensor);

    /**
     * @return Mover this Sensor is "on"
     */
    public Mover getMover();
    
    public Set<Mover> getContacts();
}
