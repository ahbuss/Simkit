/*
 * Platform.java
 *
 * Created on June 28, 2002, 4:15 PM
 */

package simkit.smdx;

/**
 *
 * @author  Arnold Buss
 */
public interface Platform extends Moveable, simkit.SimEntity {
    
    public boolean isAlive();
    
    public void kill();
    
    
}
