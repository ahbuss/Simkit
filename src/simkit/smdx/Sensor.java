/*
 * Sensor.java
 *
 * Created on March 6, 2002, 6:26 PM
 */

package simkit.smdx;
import java.awt.*;
import java.awt.geom.*;
import java.beans.*;
import java.util.*;
import simkit.*;
/**
 *
 * @author  Arnold Buss
 * @version 
 */
public interface Sensor extends Moveable, SimEntity, PropertyChangeListener {
    
    public double getMaxRange();
    
    public Shape getFootprint();
    
    public Mover getMover();
    
    public void setMover(Mover mover);

/**
* Scheduled by a Mediator when this Sensor detects a Moveable.
**/
    public void doDetection(Moveable contact);
    
/**
* Scheduled by a Mediator when this Sensor stops detecting a Moveable.
**/
    public void doUndetection(Moveable contact);
    
    public void doStartMove(Mover mover);
    
    public void doEndMove(Mover mover);
    
    public boolean isInRangeOf(Point2D point);
    
    public Set getContacts();
    
}

