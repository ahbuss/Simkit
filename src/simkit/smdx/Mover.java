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
public interface Mover extends Moveable, SimEntity {

    public void doStartMove(Moveable mover);
    
    public void doEndMove(Moveable mover);
    
    public void moveTo(Point2D destination);
    
    public void stop();
}

