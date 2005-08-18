/*
 * CookieCutterSensor.java
 *
 * Created on March 6, 2002, 8:50 PM
 */

package simkit.smdx;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.util.LinkedHashSet;
import java.util.Set;

import simkit.SimEntityBase;

/** Models a Cookie Cutter Sensor.  Instances of this class delegate
 * their movement state to a Mover.  Responsibilities are to maintain
 * a range (so the "cookie" is always a circle) and a list of contacts.
 * The Detection and Undetection events should also fire PropertyChangeEvents
 * signaling these state changes.
 *
 * @author Arnold Buss
 * @version $Id$
 */
public class CookieCutterSensor extends SimEntityBase implements Sensor {
    
/**
* The Mover on which this Sensor resides.
**/
    protected Mover mover;

/**
* The circular coverage area for this Sensor.
**/
    protected Ellipse2D footprint;

    protected AffineTransform locationTransform;

/**
* The Contacts currently being tracked by this Sensor.
**/
    protected Set contacts;

/**
* The range of this Sensor.
**/
    protected double maxRange;
    
    /** 
     * Creates a new CookieCutterSensor not associated with a Mover.
     * @param maxRange Radius of "cookie"
     */
    
    public CookieCutterSensor(double maxRange) {
        this(maxRange, null);
    }
    
    /**
     * Creates a new CookieCutterSensor.
     * @param maxRange Radius of "cookie"
     * @param mover The Mover this sensor delegates its position to
     */    
    public CookieCutterSensor(double maxRange, Mover mover) {
        setMover(mover);
        locationTransform = new AffineTransform();
        footprint = new Ellipse2D.Double();
        this.maxRange = maxRange;
        contacts = new LinkedHashSet();
    }

    /**
     * Creates a new CookieCutterSensor.
     * @param maxRange Radius of "cookie"
     * @param mover The Mover this sensor delegates its position to
     */    
    public CookieCutterSensor(Mover mover, double maxRange) {
        this(maxRange, mover);
    }
    
    /** 
     * Adds the contact to the list of currently sensed contacts.
     * Note that in general the Contact will
     * not be a reference to the actual target.
     * @param contact Mover that is passed by the Mediator
     */    
    public void doDetection(Moveable contact) {
        contacts.add(contact);
        firePropertyChange("detection", contact);
    }
    
    /** 
     * Removes the given contact from the contacts list.
     * @param contact The contact that was lost
     */    
    public void doUndetection(Moveable contact) {
        contacts.remove(contact);
        firePropertyChange("undetection", contact);
    }
    
    /**
     * Returns the velocity of this Sensor.
     * @return Velocity of Mover delegate
     */    
    public Point2D getVelocity() {
        return mover != null ? mover.getVelocity() : (Point2D) Math2D.ZERO.clone();
    }
    
    /**
     * Returns the radius of detection for this Sensor.
     * @return Maximum radius of sensor
     */    
    public double getMaxRange() {
        return maxRange;
    }
    
    /**
     * Returns the detection area for this Sensor. The Shape is
     * a circle centered on the Sensor with a radius equal to the maximum
     * range.
     * @return Shape that is the circle of the "cookie" centered
     * at its location
     */    
    public Shape getFootprint() {
        Point2D loc = mover.getLocation();
        footprint.setFrameFromCenter(loc.getX(), loc.getY(), loc.getX() + maxRange,
            loc.getY() + maxRange);
        return footprint;
    }
    
    /**
     * Returns the acceleration vector for this Sensor.
     * @return Acceleration of mover delegate
     */    
    public Point2D getAcceleration() {
        return mover != null ? mover.getAcceleration() : null;
    }
    
    /** 
     * Schedules EndMove for this Sensor.
     * @param mover Mover sensor is on
     */    
    public void doEndMove(Mover mover) {
        waitDelay("EndMove", 0.0, new Object[] { this }, 1.0);
    }
    
    /** 
     * Schedules StartMove for this sensor
     * @param mover Mover this Sensor is on
     */    
    public void doStartMove(Mover mover) {
        waitDelay("StartMove", 0.0, new Object[] { this });
    }
    
    /**
     * The current location of this Sensor.
     * @return Location of Mover delegate
     */    
    public Point2D getLocation() {
        return mover != null ? mover.getLocation() : (Point2D) Math2D.ZERO.clone();
    }
    
    /**
     * The Mover on which this Sensor is located.
     * @return The Mover this Sensor is "on" (i.e. its location delegate)
     */    
    public Mover getMover() {
        return mover;
    }
    
    /**
     * Places this Sensor on the given Mover. 
     * Setting the Mover delegate involves unregistering as a
     * SimEventListener and PropertyChangeListener to the old Mover (if necessary);
     * then listening to the new Mover.
     * @param mover The new Mover this sensor is on
     */    
    public void setMover(Mover mover) {
        if (this.mover != null) {
            mover.removeSimEventListener(this);
            mover.removePropertyChangeListener(this);
        }
        this.mover = mover;
        if (mover != null) {
            mover.addSimEventListener(this);
            mover.addPropertyChangeListener(this);
        }
    }    
    
    /** 
     * Clears all pending events for this Sensor and clears the contact list.
     */    
    public void reset() {
        super.reset();
        contacts.clear();
        if (mover != null) {
            Point2D loc = mover.getLocation();
            locationTransform.setToTranslation(loc.getX(), loc.getY());
        }
    }
    
    /**
     * Determine if the given point is inside the detection area for 
     * this Sensor.
     * @param point Point to test
     * @return true if point is inside "cookie"
     */    
    public boolean isInRangeOf(Point2D point) {
        return getFootprint().contains(point);
    }
    
    /**
     * Returns a String containing the range of this Sensor and the information on the Mover
     * for this Sensor.
     */    
    public String toString() {
        return "CookieCutterSensor (" +  getMaxRange() + ") [" + getMover() +"]";
    }
    
    /** If from this Sensor's mover, re-broadcast the event.
     * @param e Heard PropertyChangeEvent
     */    
    public void propertyChange(PropertyChangeEvent e) {
        if (e.getSource() == mover) {
            firePropertyChange(e.getPropertyName(), e.getOldValue(), e.getNewValue());
        }
    }
    
/**
* Returns this Set of contacts currently held by this Sensor.
**/
    public Set getContacts() {
        return new LinkedHashSet(contacts);
    }
    
}
