package simkit.smd;

import java.util.List;

import simkit.PropertyChangeSource;
import simkit.SimEntity;
import simkit.SimEventSource;
/**
  *  <P>Basic Sensor interface.  The sensor is responsible for reporting its
  *  maximum range, as well as its current position and velocity.  Often the
  *  senor will get the last two pieces of information from a mover on which it is
  *  mounted.
  *
  * @author Arnold Buss
  * @version 0.6.3
**/

public interface Sensor extends SimEntity, SimEventSource, PropertyChangeSource {

   public double getMaxRange();
   public void addDetection(Mover target);
   public void removeDetection(Mover target);
   public List getDetectionList();
   public boolean isDetected(Mover target);

   public Coordinate getCurrentLocation();
   public Coordinate getVelocity();

   public Mover getMoverDelegate();

}



