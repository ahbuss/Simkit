/*
 * WeaponMediator.java
 *
 * Created on February 20, 2002, 1:14 AM
 */
 
package simkit.smdx;

import java.beans.*;
import simkit.*;
import simkit.smd.*;

/**
 *
 * @author  Arnold Buss
 */
public interface WeaponMediator extends PropertyChangeListener {
    
    public void setWeapon(Weapon weapon);
    
    public Weapon getWeapon();
    
    public void setTarget(Target target);
    
    public Target getTarget();
    
    public void setEnabled(boolean enabled);
    
    public boolean isEnabled();
    
    public WeaponFireType getWeaponFireType();
    
}
