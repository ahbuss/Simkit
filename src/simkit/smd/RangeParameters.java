package simkit.smd;

/**
  * Class to hold information for detections.  Each instance represents a 
  * potential entry of a target (Mover) into a Sensor's range.  For 
  * interruption purposes,  each instance matches either the
  * sensor or the target via "equals()" for eventual cancelation of detections.
  *
  * @author Arnold Buss
  * @version 0.2
**/

import java.text.*;

public class RangeParameters{ 
// instance variables
   private Mover target;
   private Sensor sensor;
   private double exitRangeTime ;
   private DecimalFormat form;
/**
 *  Construct a RangePrameters instance with the given Mover, sensor, and exit time.
 *  @param t The target
 *  @param s The sensor
 *  @param time The Exit time
**/
   public RangeParameters(Mover t, Sensor s, double time) {
      target = t;
      sensor = s;
      exitRangeTime = time;
      setFormat("0.000");
   }
/**
 *  @return the target associated with this instance
**/
   public Mover getTarget() {return target;}
/**
 *  @return the sensor associated with this instance
**/
   public Sensor getSensor() {return sensor;}
/**
 *  @return the exit time
**/
   public double getExitRangeTime() {return exitRangeTime ;}
/**
 *  @return true if same object or if it is this Sensor or Mover (target)
**/
   public boolean equals(Object o) {
      if (o instanceof RangeParameters) {
        return ((RangeParameters) o).equals(this);
      }
      else {
        return o.equals(target) || o.equals(sensor);
      }
   }
/**
 *  @return true if it is this object, false otherwise
**/
   public boolean equals(RangeParameters o) {
      return o.getTarget().equals(target) && o.getSensor().equals(sensor);
   }
/**
 *  @param newFormatString java.text.DecimalFormat String
**/
   public void setFormat(String newFormatString) {
      form = new DecimalFormat(newFormatString);
   }
/**
 * @return String representation of this instance
**/
   public String toString() {
      return sensor.getName() + ", " + target.getName() +
        ", exitRangeTime=" +
          form.format(exitRangeTime);
   }
}
