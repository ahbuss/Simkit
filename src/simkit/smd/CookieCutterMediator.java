package simkit.smd;

import java.beans.*;    // II
import simkit.*;
import simkit.smd.*;

/**
 *  A mediator for implementing a "Cookie-Cutter" sensor. The detection
 *  occurs as soon as the target enters the range of the sensor.  When the
 *  detection occurs, a Contact is created and passed to the sensor.  When
 *  ExitRange happens, control passes back to the Referee.
 *
 *  @author Arnold Buss
 *  @version 21 Febraury 2001
**/

public class CookieCutterMediator extends SimEntityBase implements Mediator, PropertyChangeListener {

    private Sensor sensor;
    private Mover target;
    private Contact contact;

    private boolean connected;

/**
 *  <P> Construct a CookieCutterMediator to adjudicate the given Sensor and
 *  Mover instances.  Note that the constructor will not be called by the "user"
 *  via a "new" but rather by a Referee when an adjudication is required.
 *  The mediator is a SimeventListener to both the target and the sensor's
 *  mover delegate so it can hear all changes in movement status.
 *  It is also assumed that the mediator instance is a SimEventListener to the Referee.  
 *  @param sensor The sensor that may receive a detection.
 *  @param target The mover that may be detected.
**/

    public CookieCutterMediator(Sensor sensor, Mover target) {
        this.sensor = sensor;
        this.target = target;
        Schedule.removeRerun(this);
        contact = new Contact(target);
    }

    public CookieCutterMediator() {
        Schedule.removeRerun(this);
        contact = new Contact(target);
    }


/**
 *  The target enters the sensor's range.  This event is heard from the Referee.
 *  This event schedules a Detection immediately, providing the target and
 *  sensor are the correct ones, that is, the specific ones for which this
 *  mediator is adjudicating.
 *  @param rp The object containing the sensor, target, and undetection time for
 *  this interaction.
**/
    public void doEnterRange(RangeParameters rp) {
        if ( (rp.getSensor() == sensor) && (rp.getTarget() == target)){
            this.connect();
            waitDelay("Detection", 0.0, rp);
        }
    }

/**
 *  The detection event.  If the event is for this mediator's sensor/target
 *  pair, then the detection is explicitly scheduled in the sensor using the
 *  Contact instance that shields the true target.
 *  @param rp The object containing references to the sensor, target, and the
 *  undetection times.
**/
  public void doDetection(RangeParameters rp) {
    if ( (isConnected() && rp.getSensor() == sensor) && (rp.getTarget() == target)){
        sensor.waitDelay("Detection", 0.0, new Object[] {contact});
    }
  }

/**
 *  This event immediately schedules an UnDetection in the sensor.
 *  @param rp The object containing references to the sensor, target, and the
 *  undetection times.
**/
    public void doExitRange(RangeParameters rp) {
        if (isConnected() &&  (rp.getSensor() == sensor) && (rp.getTarget() == target)){
            sensor.waitDelay("Undetection", 0.0, new Object[] {contact});
            disconnect();
        }
    }

/**
 *  disconnect() the Mediator when the interaction is complete

**/
    public void disconnect() {
        target.removeSimEventListener(this);
        sensor.getMoverDelegate().removeSimEventListener(this);
        this.removeSimEventListener(sensor);
        ((SimEntityBase) target).removePropertyChangeListener( this);    //II
        ((SimEntityBase) sensor).removePropertyChangeListener( this);     //II
        connected = false;
        reset();
    }

/**
 *  connect() if/when a new interaction occured.
**/
    public void connect() {
        target.addSimEventListener(this);
        sensor.addSimEventListener(this);
        this.addSimEventListener(sensor);
        ((SimEntityBase) target).addPropertyChangeListener(this);      //II
        ((SimEntityBase) sensor).addPropertyChangeListener(this);       //II
        connected = true;
    }


/**
 * @return whether the current instance is connected or not.
**/
    public boolean isConnected() { return connected; }

    public void setTargetAndSensor(Mover target, Sensor sensor) {
    }

/**
 *  @return the target this Mediator is currently hooked up to.
**/
    public Mover getTarget() {return target;}
/**
 *  @return the sensor this Mediator is currently hooked up to.
**/
    public Sensor getSensor() {return sensor;}
/**
 *  @return whether the target is in sensor's range or not.
**/
    public boolean isTargetInRange() {
        return target.getCurrentLocation().distanceFrom(
                sensor.getCurrentLocation()) <= sensor.getMaxRange();
    }
/**
 *  @return detectionTime = 0.0.
**/
    public double getDetectionTime() {return 0.0;}
/**
 *  @return undetectionTime = 0.0.
**/
    public double getUndetectionTime() { return 0.0;}

/**
 *  Responds to PropertyChangeEvents - empty in this case.
**/
    public void propertyChange(PropertyChangeEvent event) {  }

} 
