/*
 * WeaponMediator.java
 *
 * Created on February 20, 2002, 1:14 AM
 */
 
package simkit.smdx;

import java.beans.*;
import simkit.*;
import simkit.smd.*;
import java.awt.geom.*;

/**
 *
 * @author  Arnold Buss
 */
public interface WeaponAdjudicator extends PropertyChangeListener {
    
    public void doImpact(Weapon weapon, Target target);
    
    public void doImpact(Weapon weapon, Point2D coordinate);
    
}
