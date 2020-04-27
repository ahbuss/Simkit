/*
 * Mover.java
 *
 * Created on March 6, 2002, 6:05 PM
 */

package simkit.smdx;
import java.awt.geom.Point2D;

import simkit.PropertyChangeSource;
import simkit.SimEntity;

/**
 * A SimEntity whose movement can be controlled and that can interact
 * with Sensors.
 * @see Sensor
 * @see MoverManager
 * @author  Arnold Buss
 * 
 */
public interface Mover extends Moveable, SimEntity, PropertyChangeSource {

    /**
     * Event that signals the start of a move to a previously set destination.
     * Should set the MovementState to "CRUISING." Movers should not
     * normally respond to other entities StartMove events.
     * @param mover Should be reference to this Mover.
     */
    public void doStartMove(Moveable mover);
    
    /**
     * Event that signals that this Mover has reached its desired location.
     * Should set the MovementState to "PAUSED." Movers should not
     * normally respond to other entities EndMove events.
     * @param mover Should be reference to this Mover
     */
    public void doEndMove(Moveable mover);
    

    /**
     * Causes this Mover to move to the given destination at 
     * its maximum speed. Normally schedules the StartMove event after
     * setting the destination.
     * 
     * @param destination given destination
     */
    public void moveTo(Point2D destination);
    
    /**
     * Causes this Mover to move to the given destination at the 
     * given speed. Normally schedules the StartMove event.
     * @param destination given destination
     * @param cruisingSpeed given speed
     */
    public void moveTo(Point2D destination, double cruisingSpeed);
    
    /**
     * Move with the given velocity.
     * @param velocity given velocity
     */
    public void move(Point2D velocity);
    
    /**
     * Instantly move to the given location.
     * @param location given location
     * @throws MagicMoveException If this Mover is not allowed to magic move.
     */
    public void magicMove(Point2D location) throws MagicMoveException;
    
    /**
     * Accelerate with the given acceleration vector.
     * @param acceleration given acceleration vector
     */
    public void accelerate(Point2D acceleration);
    
    /**
     * Accelerate in the direction of the given acceleration vector
     * to the given speed.
     * @param acceleration given acceleration
     * @param speed given speed
     */
    public void accelerate(Point2D acceleration, double speed);
    
/**
* Stops at the current location. Should set the MovementState to "STOPPED"
**/
    public void stop();
    
/**
* Pause at the current location. Should set the MovementState to "PAUSED"
**/
    public void pause();
    
    /**
     * 
     * @return information about the Mover.
     */
    public String paramString();
    
    /**
     * 
     * @return true if the Mover is currently moving.
     */
    public boolean isMoving();
    
    /**
     * 
     * @return the current MovementState of this Mover.
     */
    public MovementState getMovementState();
    
    /**
     * 
     * @return speed that this Mover should never exceed.
     */
    public double getMaxSpeed();

    /**
     * Added to support zero argument constructors and the ObjectMaker system.
     * @param maxSpeed speed that this Mover should never exceed.
     * @throws MagicMoveException if this Mover doesn't support "magic moves
     */
    public void setMaxSpeed(double maxSpeed) throws MagicMoveException;
    
    /**
     * Added to support zero argument constructors and the ObjectMaker system.
     * @param location Given location
     * @throws MagicMoveException if Mover doesn't support magic moves
     */
    public void setLocation(Point2D location) throws MagicMoveException;
}

