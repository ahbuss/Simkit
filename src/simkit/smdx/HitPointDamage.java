/*
 * HitPointDamage.java
 *
 * Created on February 20, 2002, 5:26 PM
 */

package simkit.smdx;

/**
 *
 * @author  Arnold Buss
 * @version 
 */
public class HitPointDamage implements Damage {

    private Number damage;
    
    public void setDamage(Object damage) { 
        if (damage instanceof Number) { 
            this.damage = (Number) damage;
        }
    }
    
    public Object getDamage() { return damage; }
    
}
