/*
 * Class.java
 *
 * Created on February 20, 2002, 12:00 PM
 */

package simkit.smdx;
import simkit.*;
import simkit.smd.*;
import java.util.*;

/**
 * @author  Arnold Buss
 */
public class WeaponTargetReferee extends SimEntityBase {
    
    public static final WeaponTargetReferee WEAPON_REFEREE;

    private ArrayList weapons;
    private ArrayList targets;
    
    private static WeaponMediatorFactory factory;
    static {
        WEAPON_REFEREE = new WeaponTargetReferee();
        factory = WeaponMediatorFactory.getFactoryInstance();
    }
    
    public static WeaponTargetReferee getDefaultWeaponTargetReferee() { return WEAPON_REFEREE; }
    
    public WeaponTargetReferee() {
        weapons = (ArrayList) Collections.synchronizedList(new ArrayList());
        targets = (ArrayList) Collections.synchronizedList(new ArrayList());
    }
    
    public void addWeapon(Weapon weapon) {
        if ( weapon != null && !weapons.contains(weapon)) {
            weapons.add(weapon);
            weapon.addSimEventListener(this);
        }
    }
    
    public void removeWeapon(Weapon weapon) {
        if (weapon != null) {
            weapons.remove(weapon);
            weapon.removeSimEventListener(this);
        }
        
    }
    
    public void addTarget(Target target) {
        if (target != null && !targets.contains(target)) {
            targets.add(target);
            target.addSimEventListener(this);
        }
    }
    
    public void removeTarget(Target target) {
        if (target != null) {
            targets.remove(target);
            target.removeSimEventListener(this);
        }
    }
    
    public void doShoot(Weapon weapon, Coordinate aimPoint) {
// Note: how to get the time to impact??        
        waitDelay("Impact", 0.0, new Object[] {weapon, aimPoint} );
    }
    
    public void doShoot(Weapon weapon, Target target) {
// Note: how to get the time to impact??        
        waitDelay("Impact", 0.0, new Object[] {weapon, target});
    }
    
    public void doImpact(Weapon weapon, Coordinate impactPoint) {
        if (weapons.contains(weapon)) {
            firePropertyChange("areaImpact", weapon, impactPoint);
        }
    }
    
    public void doImpact(Weapon weapon, Target target) {
        if (weapons.contains(weapon) && targets.contains(target)) {
            firePropertyChange("directImpact", weapon, target);
        }
    }
    
    public List getTargets() { return (List) targets.clone(); }
    
    public List getWeapons() { return (List) weapons.clone(); }
    
    
    public void reset() {
        super.reset();
    }
}
