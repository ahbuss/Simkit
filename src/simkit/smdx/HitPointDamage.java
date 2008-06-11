/*
 * HitPointDamage.java
 *
 * Created on February 20, 2002, 5:26 PM
 */

package simkit.smdx;

/**
 * This Damage type holds a Number to indicate the amount of damage to the Target
 * from a hit.
 * The meaning of the Number depends on the implementation of the <CODE>hit</CODE>
 * method in the Target.
 * @author  Arnold Buss
 * @version $Id: HitPointDamage.java 441 2003-11-01 00:08:27Z jlruck $ 
 */
public class HitPointDamage implements Damage {

/**
* Contructs a new HitPointDamage with the damage Object set to null.
**/
   public HitPointDamage() {
   }

/**
* Contructs a new HitPointDamage with the given damage Object.
**/
   public HitPointDamage(Object damage) {
      setDamage(damage);
   }

/**
*  A numerical value representing the amount that a hit
* damages a Target.
**/
    private Number damage;
    
/**
* Sets the numerical value representing the amount that a hit
* damages a Target. 
* @param damage The Number representing the damage. If not a Number, then
* the value of <CODE>damage</CODE> is left unchanged.
**/
    public void setDamage(Object damage) { 
        if (damage instanceof Number) { 
            this.damage = (Number) damage;
        }
    }
    
/**
* A numerical value representing the amount that a hit
* damages a Target.
**/
    public Object getDamage() { return damage; }
    
}
