/*
 * Sensor.java
 *
 * Created on March 6, 2002, 6:26 PM
 */

package simkit.smdx;
import java.awt.*;
import java.awt.geom.*;
import java.beans.*;
import java.util.*;
import simkit.*;
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
* The largest range that this Sensor is capable of detecting. A
* circle centered on this Sensor with this radius should enclose the
* footprint.
**/
    public double getMaxRange();
    
/**
* The area in which this Sensor can potentially detect Movers.
**/
    public Shape getFootprint();
    
/**
* The Mover on which this Sensor resides.
**/
    public Mover getMover();
    
/**
* Sets the Mover on which this Sensor resides.
**/
    public void setMover(Mover mover);

/**
* Scheduled by a Mediator when this Sensor detects a Moveable.
**/
    public void doDetection(Moveable contact);
    
/**
* Scheduled by a Mediator when this Sensor stops detecting a Moveable.
**/
    public void doUndetection(Moveable contact);

/**
* An event that indicates this Sensor has started moving.
**/
    public void doStartMove(Mover mover);
    
/**
* An event that indicates that this Sensor has completed is current
* move.
    public void doEndMove(Mover mover);
    
/**
* True if the point is inside the footprint of this Sensor.
**/
    public boolean isInRangeOf(Point2D point);
    
/**
* A set containing the contacts currently
* being detected by this Sensor.
**/
    public Set getContacts();
    
}

