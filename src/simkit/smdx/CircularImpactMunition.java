/*
 * Munition.java
 *
 * Created on June 18, 2002, 5:26 PM
 */

package simkit.smdx;

import simkit.*;
import java.awt.*;
import java.awt.geom.*;
import java.beans.*;
import java.text.*;
/**
 *
 * @author  ahbuss
 */
public class CircularImpactMunition extends UniformLinearMover implements Munition {
    
    private Point2D aimPoint;
    private double range;
    private boolean expended;
    
    private static DecimalFormat df;
    static {
        df = new DecimalFormat("-0.000; 0.000");
    }
    
    public CircularImpactMunition(String name, Point2D location, double speed, double range) {
        this(location, speed, range);
        setName(name);
    }
    
    public CircularImpactMunition(Point2D location, double speed, double range) {
        super(location, speed);
        this.range = range;
    }
    
    public void reset() {
        super.reset();
        expended = false;
    }
    
    public void doFire(Point2D aimPoint) {
        setAimPoint(aimPoint);
        expended = true;
        firePropertyChange("expended", Boolean.FALSE, Boolean.TRUE);
        moveTo(aimPoint);
    }
    
    public void doEndMove(CircularImpactMunition m) {
        if (this == m) {
            stop();
            waitDelay("Impact", 0.0, this);
        }
    }
    
    public Point2D getAimPoint() { return (Point2D) aimPoint.clone(); }
    
    public double getImpactRange() { return range; }
    
    public Shape getImpact() {
        Ellipse2D impact =  new Ellipse2D.Double();
        Point2D center = getLocation();
        Point2D corner = Math2D.add(center, new Point2D.Double(range, range));
        impact.setFrameFromCenter(center, corner);
        return impact;
    }
    
    public boolean isExpended() { return expended; }
    
    public String toString() {
        return super.toString() + (!isExpended() ? " Not Expended" : " Expended");
    }
    
    public void doImpact(Munition munition) {
        firePropertyChange("impact", getImpact());
    }
    
    public void setAimPoint(Point2D aimPoint) {
        this.aimPoint = aimPoint;
        firePropertyChange("aimPoint", aimPoint);
    }
    
}
