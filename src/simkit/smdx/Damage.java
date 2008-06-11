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
 * @version $Id: Damage.java 439 2003-10-28 20:47:05Z jlruck $
 */
public interface Damage {
    
/**
* Gets the damage information.
**/
    public Object getDamage();

}

