/*
 * Mover.java
 *
 * Created on March 6, 2002, 6:05 PM
 */

package simkit.smdx;
import simkit.*;

import java.awt.geom.*;

/**
 *
 * @author  Arnold Buss
 * @version 
 */
public interface Mover extends Moveable, SimEntity, PropertyChangeSource {

    public void doStartMove(Moveable mover);
    
    public void doEndMove(Moveable mover);
    
    public void moveTo(Point2D destination);
    
    public void moveTo(Point2D destination, double cruisingSpeed);
    
    public void move(Point2D velocity);
    
    public void magicMove(Point2D location) throws MagicMoveException;
    
    public void accelerate(Point2D acceleration);
    
    public void accelerate(Point2D acceleration, double speed);
    
    public void stop();
    
    public void pause();
    
    public String paramString();
    
    public boolean isMoving();
    
    public MovementState getMovementState();
    
    public double getMaxSpeed();
}

