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
/**
 *
 * @author  ahbuss
 */
public class Munition extends SimEntityBase implements Moveable {
    
    private Mover propulsion;
    private Point2D aimPoint;
    private double range;
    
    public Munition(Mover prop, double range) {
        propulsion = prop;
        propulsion.addSimEventListener(this);
        this.range = range;
    }
    
    public Point2D getAcceleration() {
        return propulsion.getAcceleration();
    }
    
    public Point2D getLocation() {
        return propulsion.getLocation();
    }
    
    public Point2D getVelocity() {
        return propulsion.getVelocity();
    }
    
    public void doFire(Point2D aimPoint) {
        this.aimPoint = aimPoint;
        propulsion.moveTo(aimPoint);
    }
    
    public void doEndMove(Mover m) {
        if (m == propulsion) {
            firePropertyChange("impact", this);
            propulsion.removeSimEventListener(this);
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
}
