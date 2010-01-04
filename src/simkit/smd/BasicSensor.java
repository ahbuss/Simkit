package simkit.smd;

import java.awt.geom.Point2D;
import java.util.HashSet;
import simkit.Priority;
import simkit.SimEntityBase;

/**
 * Abstract base class with much of the common functionality. Location
 * and velocity are delegated to the Mover instance. Certain heard events
 * from the Mover are rescheduled with the Sensor as argument.
 *
 * @version $Id: BasicSensor.java 81 2009-11-16 22:28:39Z ahbuss $
 * @author ahbuss
 */
public abstract class BasicSensor extends SimEntityBase implements Sensor {

    private double maxRange;

    private Mover mover;

    protected HashSet<Mover> contacts;

    /**
     *
     * @param mover The Mover instance this sensor is "on"
     * @param maxRange Maximum range of this Sensor.
     */
    public BasicSensor(Mover mover, double maxRange) {
        setMover(mover);
        setMaxRange(maxRange);
        contacts = new HashSet<Mover>();
    }

    /**
     * Empty contacts list.
     */
    public void reset() {
        super.reset();
        contacts.clear();
    }

    /**
     * Schedule RegisterSensor(this)
     */
    public void doRun() {
        firePropertyChange("contacts", getContacts());

        waitDelay("RegisterSensor", 0.0, Priority.HIGH, this);
    }

    /**
     * Add parameter to list of contacts
     * @param contact The Mover that has been detected, or its surrogate
     */
    public void doDetection(Mover contact) {
        HashSet<Mover> oldContacts = getContacts();
        contacts.add(contact);
        firePropertyChange("contacts", oldContacts, getContacts());

        firePropertyChange("detection", contact);
    }

    /**
     * If a surrogate is used, care must be taken by the appropriate
     * Mediator 
     * @param contact The Mover that has been undetected, or its surrogate.
     */
    public void doUndetection(Mover contact) {
        HashSet<Mover> oldContacts = getContacts();
        contacts.remove(contact);
        firePropertyChange("contacts", oldContacts, getContacts());

        firePropertyChange("undetection", contact);
    }

    /**
     * Delegated to Mover
     * @param mover
     */
    public void doStartMove(Mover mover) {
        waitDelay("StartMove", 0.0, this);
    }

    public void doStartMove(Sensor sensor) {
    }

    /**
     * Reschedule EndMove for this Sensor
     * @param mover
     */
    public void doEndMove(Mover mover) {
        waitDelay("EndMove", 0.0, Priority.HIGH, this);
    }

    /**
     * Reschedule Stop for this Sensor
     * @param mover
     */
    public void doStop(Mover mover) {
        waitDelay("Stop", 0.0, Priority.HIGH, this);
    }

    public void doStop(Sensor sensor) {
    }
    
    public Point2D getCurrentLocation() {
        return mover.getCurrentLocation();
    }

    public Point2D getVelocity() {
        return mover.getVelocity();
    }

    /**
     * @return the maxRange
     */
    public double getMaxRange() {
        return maxRange;
    }

    /**
     * @param maxRange the maxRange to set
     */
    public void setMaxRange(double maxRange) {
        this.maxRange = maxRange;
    }

    /**
     * @return the mover
     */
    public Mover getMover() {
        return mover;
    }

    /**
     * @param mover the mover to set
     */
    public void setMover(Mover mover) {
        if (this.mover != null) {
            this.mover.removeSimEventListener(this);
            this.removeSimEventListener(mover);
        }
        this.mover = mover;
        this.addSimEventListener(mover);
        this.mover.addSimEventListener(this);
    }

    /**
     * @return the contacts
     */
    public HashSet<Mover> getContacts() {
        return new HashSet<Mover>(contacts);
    }

    /**
     * This will be 0.0 under simple linear motion
     * @return Mover's acceleration
     */
    public double getAcceleration() {
        return mover.getAcceleration();
    }

    public String toString() {
        return "BasicSensor " + getMaxRange() + " " +  mover.toString();
    }
}
