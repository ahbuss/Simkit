/*
 * CookieCutterSensor.java
 *
 * Created on March 6, 2002, 8:50 PM
 */

package simkit.smdx;
import simkit.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.*;

/**
 *
 * @author  Arnold Buss
 * @version 
 */
public class CookieCutterSensor extends SimEntityBase implements Sensor {

    protected Mover mover;
    protected Ellipse2D footprint;
    protected AffineTransform locationTransform;
    protected Set contacts;
    
    /** Creates new CookieCutterSensor */
    
    public CookieCutterSensor(double maxRange) {
        this(maxRange, null);
    }
    
    public CookieCutterSensor(double maxRange, Mover mover) {
        setMover(mover);
        locationTransform = new AffineTransform();
        footprint = new Ellipse2D.Double(0, 0, maxRange, maxRange);
        contacts = new HashSet();
    }

    public void doDetection(Moveable contact) {
        contacts.add(contact);
        firePropertyChange("detection", contact);
    }
    
    public void doUndetection(Moveable contact) {
        contacts.remove(contact);
        firePropertyChange("undetection", contact);
    }
    
    public Point2D getVelocity() {
        return mover != null ? mover.getVelocity() : null;
    }
    
    public double getMaxRange() {
        return footprint.getMaxX();
    }
    
    public Shape getFootprint() {
        Point2D loc = mover.getLocation();
        locationTransform.setToTranslation(loc.getX(), loc.getY());
        return locationTransform.createTransformedShape(footprint);
    }
    
    public Point2D getAcceleration() {
        return mover != null ? mover.getAcceleration() : null;
    }
    
    public void doEndMove(Mover mover) {
        waitDelay("EndMove", 0.0, new Object[] { this });
    }
    
    public void doStartMove(Mover mover) {
        waitDelay("StartMove", 0.0, new Object[] { this });
    }
    
    public Point2D getLocation() {
        return mover != null ? mover.getLocation() : null;
    }
    
    public Mover getMover() {
        return mover;
    }
    
    public void setMover(Mover mover) {
        if (this.mover != null) {
            mover.removeSimEventListener(this);
        }
        this.mover = mover;
        if (mover != null) {
            mover.addSimEventListener(this);
        }
    }    
    
    public void reset() {
        super.reset();
        contacts.clear();
        if (mover != null) {
            Point2D loc = mover.getLocation();
            locationTransform.setToTranslation(loc.getX(), loc.getY());
        }
    }
}
