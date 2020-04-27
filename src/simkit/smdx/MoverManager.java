/*
 * MoverManager.java
 *
 * Created on July 29, 2002, 11:13 AM
 */
package simkit.smdx;

/**
 * Controls the movement of a Mover.
 *
 * @author Arnold Buss
 * 
 */
public interface MoverManager extends simkit.SimEntity {

    /**
     * Starts the Mover's motion. The path is determined by the implementation
     * of the MoverManager.
     *
     */
    public void start();

    /**
     * Stops the Mover at its current location.
     *
     */
    public void stop();

    /**
     * Sets the Mover to be controlled by this MoverManager. The implementation
     * is responsible for adding this MoverManger as a SimEventListner for the
     * Mover so that the EndMove event can be heard. If this MoverManger is
     * currently manager a Mover, then it should be removed as a
     * SimEventListener from the old Mover.
     *
     * @param mover the Mover to be controlled by this MoverManager
     */
    public void setMover(Mover mover);

    /**
     * 
     * @return the Mover that is controlled by this MoverManger.
     */
    public Mover getMover();

    /**
     *
     * @param b true if after a reset, this MoverManager will re-start the
     * motion of the controlled Mover.
     *
     */
    public void setStartOnReset(boolean b);

    /**
     * @return true if, after a reset, this MoverManager will re-start the
     * motion of the controlled Mover.
     */
    public boolean isStartOnReset();

    /**
     * Notifies this MoverManager that the Mover has reached the end of the
     * current movement. This allows the implementation to determine where to
     * move next.
     *
     * @param mover Given Mover
     */
    public void doEndMove(Mover mover);

    /**
     *
     * @return true if the Mover controlled by this MoverManger is currently
     * moving.
     */
    public boolean isMoving();

}
