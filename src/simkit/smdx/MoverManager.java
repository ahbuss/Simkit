/*
 * MoverManager.java
 *
 * Created on July 29, 2002, 11:13 AM
 */

package simkit.smdx;

/**
 *
 * @author  Arnold Buss
 */
public interface MoverManager extends simkit.SimEntity {
    
    public void start();
    
    public void stop();
    
    public Mover getMover();
    
    public void setStartOnReset(boolean b);
    
    public boolean isStartOnReset();
    
    public void doEndMove(Mover mover);
    
    public boolean isMoving();
    
}
