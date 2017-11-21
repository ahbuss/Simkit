/*
 * Moveable.java
 *
 * Created on February 20, 2002, 12:57 AM
 */
package simkit.smdx;

import java.awt.geom.Point2D;

/**
 * An Object that can move in 2 dimensions.
 *
 * @author Arnold Buss
 * @version $Id$
 */
public interface Moveable {

    /**
     * Implementations should take care that simulation events and state changes
     * are not fired by this method.
     *
     * @return the current location of this Object.
     */
    public Point2D getLocation();

    /**
     *
     * @return the current velocity of this Object.
     */
    public Point2D getVelocity();

    /**
     *
     * @return the current acceleration of this Object.
     */
    public Point2D getAcceleration();

}
