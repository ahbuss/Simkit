package simkit.smd;

import java.awt.geom.Point2D;
import simkit.SimEntity;

/**
 * Interface that specifies the minimum functionality for an entity
 * that has a 2-dimensional location and can move.
 *
 * @version $Id: Mover.java 81 2009-11-16 22:28:39Z ahbuss $
 * @author ahbuss
 */
public interface Mover extends SimEntity {
    /**
     * @return current location of the Mover
     */
    public Point2D getCurrentLocation();

    /**
     * @return current velocity of the Mover
     */
    public Point2D getVelocity();

    /**
     * Event that indicates the initiation of a maneuver
     * @param mover This Mover - for purposes of listeners
     */
    public void doStartMove(Mover mover);

    /**
     * Event that indicates a genuine stopped state.
     * @param mover This Mover - for purposes of listeners
     */
    public void doStop(Mover mover);

    /**
     * @return maximum speed mover can  travel
     */
    public double getMaxSpeed();
}