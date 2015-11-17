/*
 * Munition.java
 *
 * Created on June 18, 2002, 5:26 PM
 */
package simkit.smdx;

import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.text.DecimalFormat;

/**
 * A Munition that has an area of effect that is a circle centered around its
 * aim point.
 *
 * @author ahbuss
 * @version $Id$
 */
public class CircularImpactMunition extends UniformLinearMover implements Munition {

    /**
     * The center of the circle affected by this Munition.
*
     */
    private Point2D aimPoint;

    /**
     * The radius of the circle affected by the Munition.
*
     */
    private double range;

    /**
     * True if this Munition has been fired.
*
     */
    private boolean expended;

    /**
     * A default format for displaying numbers.
*
     */
    private static DecimalFormat df;

    static {
        df = new DecimalFormat("-0.000; 0.000");
    }

    /**
     * Constructs a new CircularImpactMunition.
     *     
* @param name The name of this Munition.
     * @param location The initial location of this Munition.
     * @param speed The speed of this Munition.
     * @param range The radius of the circular area of effect for this Munition.
*
     */
    public CircularImpactMunition(String name, Point2D location, double speed, double range) {
        this(location, speed, range);
        setName(name);
    }

    /**
     * Contructs a new CircularImpactMunition.
     *     
* @param location The initial location of this Munition.
     * @param speed The speed of this Munition.
     * @param range The radius of the circular area of effect for this Munition.
*
     */
    public CircularImpactMunition(Point2D location, double speed, double range) {
        super(location, speed);
        this.range = range;
    }

    /**
     * Cancels all pending events and returns the Munition to its original
     * location in an un-expended state.
*
     */
    public void reset() {
        super.reset();
        expended = false;
    }

    /**
     * Causes this Munition to move to the given aimpoint.
     *
     * @param aimPoint given aimpoint
     */
    public void doFire(Point2D aimPoint) {
        setAimPoint(aimPoint);
        expended = true;
        firePropertyChange("expended", Boolean.FALSE, Boolean.TRUE);
        moveTo(aimPoint);
    }

    /**
     * Stops this Munition at the aimpoint and schedules the Impact event for
     * now.
     *
     * @param munition this Munition
     */
    public void doEndMove(CircularImpactMunition munition) {
        if (this == munition) {
            stop();
            waitDelay("Impact", 0.0, this);
        }
    }

    /**
     * Returns a copy of the aimpoint.
*
     */
    public Point2D getAimPoint() {
        return (Point2D) aimPoint.clone();
    }

    /**
     * The radius of the circular area that this Munition effects.
*
     */
    public double getImpactRange() {
        return range;
    }

    /**
     * Returns a Shape that is a circle with radius equal to the impactRange
     * centered on the aimpoint. This is the area that this Munition effects.
*
     */
    public Shape getImpact() {
        Ellipse2D impact = new Ellipse2D.Double();
        Point2D center = getLocation();
        Point2D corner = Math2D.add(center, new Point2D.Double(range, range));
        impact.setFrameFromCenter(center, corner);
        return impact;
    }

    /**
     * Returns true if this Munition has been expended.
*
     */
    public boolean isExpended() {
        return expended;
    }

    /**
     * Returns a String containing the name, current location, current velocity,
     * and whether or not expended of this Munition.
*
     */
    public String toString() {
        return super.toString() + (!isExpended() ? " Not Expended" : " Expended");
    }

    /**
     * It is up to a MunitionTargetReferee to determine the effect of the Impact
     * event. Simply fires a property change for the impact area.
*
     */
    public void doImpact(Munition munition) {
        firePropertyChange("impact", getImpact());
    }

    /**
     * Sets the aimpoint of this Munition and fires a property change.
*
     */
    public void setAimPoint(Point2D aimPoint) {
        this.aimPoint = aimPoint;
        firePropertyChange("aimPoint", aimPoint);
    }

}
