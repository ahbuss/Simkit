/*
 * Target.java
 *
 * Created on February 20, 2002, 12:03 PM
 */

package simkit.smdx;

/**
 * A Mover that can be acted on by a Munition and is subject to being
 * killed or damaged.
 * @author  Arnold Buss
 * @version $Id: Target.java 643 2004-04-25 22:59:33Z kastork $
 */
public interface Target extends Mover {
    
/**
* Kills this Target.
**/
    public void kill();
    
/**
* Causes this Target to be damaged.
* @param damage Defines how this Target is damaged.
**/
    public void hit(Damage damage);
    
/**
* Returns true if this Target has not been killed.
**/
    public boolean isAlive();
}

