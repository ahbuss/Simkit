/*
 * BasicWeapon.java
 *
 * Created on March 5, 2002, 5:37 PM
 */

package simkit.smdx;

import java.awt.geom.*;

import simkit.smd.*;
import simkit.*;

/**
 *
 * @author  ahbuss
 * @version
 */
public class BasicWeapon extends SimEntityBase implements Weapon {
    
    private Moveable mover;
    private int numberRemainingRounds;
    
    private int maxRoundsCapacity;
    
    /** Creates new BasicWeapon */
    public BasicWeapon() {
    }
    
    public Point2D getVelocity() {
        return mover.getVelocity();
    }
    
    public Point2D getLocation() {
        return mover.getLocation();
    }
    
    public void setMover(Moveable mover) {
        this.mover = mover;
    }
    
    public void doShoot(Weapon weapon, Target target) {
        if (getRemainingRounds() > 0) {
            firePropertyChange("numberRemainingRounds", numberRemainingRounds,
            --numberRemainingRounds);
        }
    }
    
    public int getRemainingRounds() {
        return numberRemainingRounds;
    }
    
    public void reload(int numberRounds) {
        if (numberRounds > 0) {
            int oldValue = numberRemainingRounds;
            numberRemainingRounds += numberRounds;
            numberRemainingRounds = Math.min(maxRoundsCapacity, numberRemainingRounds);
            firePropertyChange("numberRemainingRounds", oldValue, numberRemainingRounds);
        }
    }
    
    public Point2D getAcceleration() {
        return null;
    }
    
    public WeaponFireType getFireType() {
        return WeaponFireType.aimed;
    }
    
    public Moveable getMover() {
        return mover;
    }
    
    public void fireAt(Target target) {
        waitDelay("Shoot", 0.0, new Object[] { this, target});
    }
    
    public void doShoot(Weapon weapon, Coordinate aimPoint) {
    }
       
}
