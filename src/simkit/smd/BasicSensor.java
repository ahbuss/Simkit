package simkit.smd;

import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.Set;
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

    protected Set<Mover> contacts;

    
    /**
     * Instantiate the contacts set.
     */
    public BasicSensor() {
        contacts = new HashSet<>();
    }
    
    /**
     *
     * @param mover The Mover instance this sensor is "on"
     * @param maxRange Maximum range of this Sensor.
     */
    public BasicSensor(Mover mover, double maxRange) {
        this();
        setMover(mover);
        setMaxRange(maxRange);
    }

    /**
     * Empty the contacts set.
     */
    @Override
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
        Set<Mover> oldContacts = getContacts();
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
        Set<Mover> oldContacts = getContacts();
        contacts.remove(contact);
        firePropertyChange("contacts", oldContacts, getContacts());

        firePropertyChange("undetection", contact);
    }

    /**
     * Delegated to Mover
     * @param mover This Sensor's Mover instance
     */
    public void doStartMove(Mover mover) {
        waitDelay("StartMove", 0.0, this);
    }

    @Override
    public void doStartMove(Sensor sensor) {
    }

    /**
     * Reschedule EndMove for this Sensor
     * @param mover This Sensor's Mover instance
     */
    public void doEndMove(Mover mover) {
        waitDelay("EndMove", 0.0, Priority.HIGH, this);
    }

    /**
     * Reschedule Stop for this Sensor
     * @param mover This Sensor's Mover instance
     */
    public void doStop(Mover mover) {
        waitDelay("Stop", 0.0, Priority.HIGH, this);
    }

    @Override
    public void doStop(Sensor sensor) {
    }
    
    @Override
    public Point2D getCurrentLocation() {
        return mover.getCurrentLocation();
    }

    @Override
    public Point2D getVelocity() {
        return mover.getVelocity();
    }

    /**
     * @return the maxRange
     */
    @Override
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
    @Override
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
    @Override
    public Set<Mover> getContacts() {
        return new HashSet<>(contacts);
    }

    @Override
    public String toString() {
        return "BasicSensor " + getMaxRange() + " " +  mover.toString();
    }
}
