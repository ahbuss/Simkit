/*
 * CookieCutterSensor.java
 *
 * Created on March 6, 2002, 8:50 PM
 */

package simkit.smdx;
import simkit.*;
import java.awt.*;
import java.awt.geom.*;
import java.beans.*;
import java.util.*;

/** Models a Cookie Cutter Sensor.  Instances of this class delegate
 * their movement state to a Mover.  Responibilities are to maintain
 * a range (so the "cookie" is always a circle) and a list of contacts.
 * The Detection and Undetection events should also fire PropertyChangeEvents
 * signaling these state changes.
 *
 * @author Arnold Buss
 */
public class CookieCutterSensor extends SimEntityBase implements Sensor {
    
    protected Mover mover;
    protected Ellipse2D footprint;
    protected AffineTransform locationTransform;
    protected Set contacts;
    protected double maxRange;
    
    /** Creates new CookieCutterSensor not associated with a Mover.
     * @param maxRange Radius of "cookie"
     */
    
    public CookieCutterSensor(double maxRange) {
        this(maxRange, null);
    }
    
    /**
     * @param maxRange Radius of "cookie"
     * @param mover The Mover this sensor delegates its position to
     */    
    public CookieCutterSensor(double maxRange, Mover mover) {
        setMover(mover);
        locationTransform = new AffineTransform();
        footprint = new Ellipse2D.Double();
        this.maxRange = maxRange;
        contacts = new HashSet();
    }

    /** Add contact to list.  Note that in general the Contact will
     * not be a reference to the actual target.
     * @param contact Mover that is passed by the Mediator
     */    
    public void doDetection(Moveable contact) {
        contacts.add(contact);
        firePropertyChange("detection", contact);
    }
    
    /** Remove from contacts list
     * @param contact The contact that was lost
     */    
    public void doUndetection(Moveable contact) {
        contacts.remove(contact);
        firePropertyChange("undetection", contact);
    }
    
    /**
     * @return Velocity of Mover delegate
     */    
    public Point2D getVelocity() {
        return mover != null ? mover.getVelocity() : (Point2D) Math2D.ZERO.clone();
    }
    
    /**
     * @return Maximum rand of sensor
     */    
    public double getMaxRange() {
        return maxRange;
    }
    
    /**
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
     * @return Acceleration of mover delegate
     */    
    public Point2D getAcceleration() {
        return mover != null ? mover.getAcceleration() : null;
    }
    
    /** Schedule EndMove for this Sensor
     * @param mover Mover sensor is on
     */    
    public void doEndMove(Mover mover) {
        waitDelay("EndMove", 0.0, new Object[] { this }, 1.0);
    }
    
    /** Schedule StartMove for this sensor
     * @param mover Mover this Sensor is on
     */    
    public void doStartMove(Mover mover) {
        waitDelay("StartMove", 0.0, new Object[] { this });
    }
    
    /**
     * @return Loaction of Mover delegate
     */    
    public Point2D getLocation() {
        return mover != null ? mover.getLocation() : (Point2D) Math2D.ZERO.clone();
    }
    
    /**
     * @return The Mover this Sensor is "on" (i.e. its location delegate)
     */    
    public Mover getMover() {
        return mover;
    }
    
    /** Setting the Mover delegate involves unregistering as a
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
    
    /** Need to clear contact list for next run.
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
     * @param point Point to test
     * @return true if point is inside "cookie"
     */    
    public boolean isInRangeOf(Point2D point) {
        return getFootprint().contains(point);
    }
    
    /**
     * @return String form of Sensor
     */    
    public String toString() {
        return "CookieCutterSensor (" +  getMaxRange() + ") [" + getMover() +"]";
    }
    
    /** If from this Sensor's mover, re-braodcast it.
     * @param e Heard PropertyChangeEvent
     */    
    public void propertyChange(PropertyChangeEvent e) {
        if (e.getSource() == mover) {
            firePropertyChange(e.getPropertyName(), e.getOldValue(), e.getNewValue());
        }
    }
}