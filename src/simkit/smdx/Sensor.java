/*
 * Sensor.java
 *
 * Created on March 6, 2002, 6:26 PM
 */

package simkit.smdx;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeListener;
import java.util.Set;

import simkit.SimEntity;
/**
 * Represents an object that can detect a Mover. The {@link SensorTargetReferee}
 * is responsible for determining when a Mover enters the footprint of this
 * Sensor. When a Mover enters the footprint, then a {@link SensorTargetMediator}
 * is responsible for determining if and when a detection occurs and
 * schedules the Detection and Undetection events for this Sensor as
 * appropriate.
 *
 * @author  Arnold Buss
 * @version $Id$
 */
public interface Sensor extends Moveable, SimEntity, PropertyChangeListener {
    
    /**
     * The largest range that this Sensor is capable of making detections,
     * independent of the shape of the footprint.
     * <p>
     * This may be called by mediators in determining whether detections are
     * possible, particularly in simplified implementations.  However, the
     * Footprint should be considered the primary means for that determination.
     * Mediators designed for sensors with complex Footprint will expect this
     * value to be a cheaply-computed best-case sensor range, and will use it
     * to minimize calls to {@code getFootprint}.
     * <p>
     * This method may be called frequently.
     * <p>
     * Treat as simulation state. 
     * Note: treating maxRange as simulation state instead of as a simulation
     * parameter is new after 2008-06-06.  The conceptual change comes from
     * a need for sensor performance to be (potentially) sensitive to conditions
     * in the simulation.  Also, the presence of a setter is convenient when
     * constructing sensors using a builder pattern.
     **/
    public double getMaxRange();
    public void setMaxRange(double r);

    /**
     * Two-dimensional area within which detections are possible for this Sensor.
     * <p>
     * Implementors - This method may be called frequently.
     * Clients - this method may be expensive.
     * <p>
     * Treat as simulation state, since the returned value may change
     * based on the movement of the Mover upon which the sensor is installed.
     */
    public Shape getFootprint();
    
    /**
     * The Mover on which this Sensor resides.
     * <p>
     * Treat as a simulation parameter (should not change after simulation
     * start)
     **/
    public Mover getMover();
    
    /**
     * The Mover on which this Sensor resides.
     * <p>
     * Treat as a simulation parameter (should not change after simulation
     * start)
     **/
    public void setMover(Mover mover);

    /**
     * Sensor Mediators schedule this event with the sensor as the 
     * scheduling entity. (by calling {@code someSensor.waitDelay("Detection"...})
     * <p>
     * If this sensor is on, then the contact should be added to the contact
     * list and a property change on contact fired.
     **/
    public void doDetection(Moveable contact);
    
    /**
     * Sensor Mediators schedule this event with the sensor as the 
     * scheduling entity. (by calling {@code someSensor.waitDelay("Undetection"...})
     * <p>
     * If this sensor is on, then the contact should be removed from the contact
     * list and a property change on contact fired.
     **/
    public void doUndetection(Moveable contact);

    /**
     * An event that indicates this Sensor has started moving. Fired by the
     * associated Mover.
     * <p>
     * Note.  Some previous implementations used this event to re-schedule
     * the same event with no delay, but this behavior is not part of
     * the contract, and should not be relied upon.
     **/
    public void doStartMove(Mover mover);
    
    /**
     * An event that indicates this Sensor has stopped moving.  Fired by
     * the associated Mover.
     * <p>
     * Note.  Some historical implementations used this event to re-schedule
     * the same event with no delay, but this behavior is not part of
     * the contract, and should not be relied upon.
     **/
    public void doEndMove(Mover mover);

    /**
     * True if the point is inside the footprint of this Sensor.
     * <p>
     * Implementors - This method may be called frequently.
     * Clients - this method may be expensive.
     **/
 public boolean isInRangeOf(Point2D point);
    
/**
* A set containing the contacts currently
* being detected by this Sensor.
**/
    public Set getContacts();
    
}

