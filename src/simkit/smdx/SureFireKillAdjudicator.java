/*
 * SureFireKillMediator.java
 *
 * Created on February 20, 2002, 5:01 PM
 */

package simkit.smdx;

import simkit.*;
import java.beans.*;
import java.awt.geom.*;

/**
 *
 * @author  Arnold Buss
 * @version
 */
public class SureFireKillAdjudicator implements WeaponAdjudicator {
    
    private Weapon weapon;
    private Target target;
    
    /** Creates new SureFireKillMediator */
    public SureFireKillAdjudicator() {
    }
    
    public void doImpact(Weapon weapon, Target target) {
        target.kill();
    }    
    
    public void doImpact(Weapon weapon, Point2D coordinate) {
        
    }
    
    public void propertyChange(java.beans.PropertyChangeEvent propertyChangeEvent) {
    }
    
}
