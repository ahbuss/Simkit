/*
 * MoverManager.java
 *
 * Created on July 29, 2002, 11:13 AM
 */

package simkit.smdx;

/**
 * Controls the movement of a Mover.
 * @author  Arnold Buss
 * @version $Id: MoverManager.java 441 2003-11-01 00:08:27Z jlruck $
 */
public interface MoverManager extends simkit.SimEntity {
    
/**
* Starts the Mover's motion. The path is determined by the
* implementation of the MoverManager.
**/
    public void start();
    
/**
* Stops the Mover at its current location.
**/
    public void stop();
    
/**
* Sets the Mover to be controlled by this MoverManager.
* The implementation is responsible for adding this MoverManger
* as a SimEventListner for the Mover so that the EndMove event
* can be heard. If this MoverManger is currently manager a Mover,
* then it should be removed as a SimEventListener from the old Mover.
**/
    public void setMover(Mover mover);
    
/**
* Gets the Mover that is controlled by this MoverManger.
**/
    public Mover getMover();
    
/**
* If true, after a reset, this MoverManager will re-start the
* motion of the controlled Mover.
**/
    public void setStartOnReset(boolean b);
    
/**
* If true, after a reset, this MoverManager will re-start the
* motion of the controlled Mover.
**/
    public boolean isStartOnReset();
    
/**
* Notifies this MoverManager that the Mover has reached the end
* of the current movement. This allows the implementation
* to determine where to move next.
**/
    public void doEndMove(Mover mover);
    
/**
* Returns true if the Mover controlled by this MoverManger is
* currently moving.
**/
    public boolean isMoving();
    
}
