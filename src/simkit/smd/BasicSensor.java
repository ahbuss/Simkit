package simkit.smd;

import java.util.List;
import java.util.Vector;

import simkit.SimEntityBase;

/**
  *  <P>The basic sensor class.  The primary purpose is to hold properties of the
  *  sensor, especially the maximum range, the location, and velocity of the
  *  sensor, and to manage a list of detections.  Note that the only way a BasicSensor 
  *  can move is to be mounted on a Mover.  If so mounted, the BasicSensor reports its 
  *  position, speed, and velocity from the Mover on which it is mounted.
  *
  *  <P>The only kind of target is an object that implements the Mover interface.
  *
  *  @author Arnold Buss
  *  @version 1.1.2
**/

public class BasicSensor extends SimEntityBase implements Sensor {

// instance variables
   private Vector detectionList;
   private double maxRange;
   private Mover locationDelegate;

/**
 *  Construct a BasicSensor
 *  @param delegate The Mover from which the BasicSensor will get its location
 *  @param range The maximum range of the sensor 
**/   public BasicSensor(Mover delegate, double range) {
      maxRange = range;
      detectionList = new Vector();
      if (delegate == null) {
        throw new IllegalArgumentException("Mover Delegate null");
      }
      locationDelegate = delegate;
      locationDelegate.addSimEventListener(this);
   }

   public double getMaxRange() { return maxRange; }

/**
 *  @return A list of the current contacts of the sensor.  
**/
   public List getDetectionList() {
      return (List) detectionList.clone(); 
   }
   
/**
 *  @return The current location as dicated by the Mover delegate.
**/
   public Coordinate getCurrentLocation() {
     return locationDelegate.getCurrentLocation();
   }
   
/**
 *  @return The current velocity as dicated by the Mover delegate.
**/
   public Coordinate getVelocity() {
      return locationDelegate.getVelocity();
   }

/**
  *  Adds a Mover to the detection list
  *  @param target The contact that is added to the list.
**/
   public void addDetection(Mover target) {
     if (!detectionList.contains(target)) {
       detectionList.addElement(target);
       if (this.isVerbose()) {
        System.out.println(this + " acquired " + target);
       }
     }
   }

/**
  *  Removes the specified Mover to the detection list
  *  @param target The contact that is removed from the list.
**/
   public void removeDetection(Mover target) {
      detectionList.removeElement(target);
      if (this.isVerbose()) {
        System.out.println(this + " lost " + target);
      }
   }

/**
 *  If the BasicSensor hears the StartMove event for its location delegate, then
 *  it puts the corresponding event on the event list for itself.  This is
 *  primarily so that Referees and Mediators can hear the change of movement
 *  status of the Sensor.
 *  @param mover The heard Mover instance.  Must match the BasicSensor's locationDelegate
 *               for the event to be scheduled.
**/
    public void doStartMove(Mover mover) {
        if (mover == locationDelegate) {
            waitDelay("StartMove", 0.0, this);
        }
    }

/**
 *  If the BasicSensor hears the EndMove event for its location delegate, then
 *  it puts the corresponding event on the event list for itself.  This is
 *  primarily so that Referees and Mediators can hear the change of movement
 *  status of the Sensor.
 *  @param mover The heard Mover instance.  Must match the BasicSensor's locationDelegate
 *               for the event to be scheduled.
**/
    public void doEndMove(Mover mover) {
        if (mover == locationDelegate) {
            waitDelay("EndMove", 0.0, this);
        }
    }


/**
 *  The event that a target is detected.
 *  @param target The target that this detection event detected
**/
   public void doDetection(Mover target) {
     this.addDetection(target);
   }

/**
 *  The event that a target is undetected (i.e. lost).
 *  @param target The target that this undetection event detected
**/
   public void doUndetection(Mover target) {
     this.removeDetection(target);
   }

/**
  *  Is the specified Mover detected?
**/
   public boolean isDetected(Mover target) {
      return (detectionList.contains(target)); 
   }

/**
 *  Convenience method.
 * @return Vector of contacts.
**/
   public Vector getContacts() { return (Vector) detectionList.clone();}

/**
 *  Returns a reference to the mover delegate for this sensor.  This should
 *  normally only be used internally by Referees and/or Mediators.
 *  @return The mover delegate that gives the sensor its location and velocity.
**/
   public Mover getMoverDelegate() {return locationDelegate;}


/**
 * @return A String representation of the BasicSensor object -- its name, maximum
 *         range, and location delegate.
**/
   public String toString() {
      return getName() + " " + getMaxRange() +
         " locationDelegate: " + locationDelegate.toString();
    }

/**
 *  Resetting removes all elements from the contact list.
**/
   public void reset() {
      super.reset();
      if (detectionList != null) {
        detectionList.removeAllElements();
      }
   }
}
