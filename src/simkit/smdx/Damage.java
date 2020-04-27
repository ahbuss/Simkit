/*
 * Damage.java
 *
 * Created on February 20, 2002, 5:25 PM
 */

package simkit.smdx;

/**
 * Contains information about damage to a Target by a Munition.
 * The type of Object returned by <CODE>getDamage()</CODE> is implementation
 * dependent as is how the Object affects the Target.
 * @author  Arnold Buss
 * 
 */
public interface Damage {
    
    /**
     * 
     * @return the damage information
     */
    public Object getDamage();

}

