/*
 * Target.java
 *
 * Created on February 20, 2002, 12:03 PM
 */

package simkit.smdx;
import simkit.*;
import simkit.smd.*;

/**
 *
 * @author  Arnold Buss
 * @version 
 */
public interface Target extends Moveable, SimEntity {
    
    public void kill();
    
    public void hit(Damage damage);
    
    public boolean isAlive();
    
}

