package simkit.smd;

/**
  * Class to hold information for detections.  Each instance represents a 
  * potential detection
  * of a target (Mover) by a sensor (Sensor).  Each instance matches either the
  * sensor or the target via "equals()" for eventual cancelation of detections.
**/
import java.text.DecimalFormat;

public class DetectionParameters{ 
// instance variables
   private Mover target;
   private Sensor sensor;
   private double undetectTime ;
   private DecimalFormat form;

// constructor
   public DetectionParameters(Mover t, Sensor s, double time) {
      target = t;
      sensor = s;
      undetectTime = time;
      setFormat("0.###");
   }

// accessors
   public Mover getTarget() {return target;}
   public Sensor getSensor() {return sensor;}
   public double getUndetectTime() {return undetectTime ;}

// for comparing, matches target or sensor
   public boolean equals(Object o) {
      return o.equals(target) || o.equals(sensor);
   }

   public void setFormat(String newFormatString) {
      form = new DecimalFormat(newFormatString);
   }

   public String toString() {
      return "sensor=" + Integer.toHexString(sensor.hashCode()) + ", target=" + 
         Integer.toHexString(target.hashCode()) + 
        ", undetectTime=" +
          form.format(undetectTime);
   }

// unit test
   public static void main(String[] args) {
      BasicMover firstMover = new BasicMover(new Coordinate(), 0.0);
      BasicMover secondMover = new BasicMover(new Coordinate(), 0.0);

      BasicSensor firstSensor = new BasicSensor(firstMover, 10.0);
      BasicSensor secondSensor = new BasicSensor(secondMover, 5.0);

      DetectionParameters first = 
         new DetectionParameters(firstMover, firstSensor, 3.0);
      System.out.println(first);
      System.out.println("Compare with first mover: " + 
         first.equals(firstMover) );
      System.out.println("Compare with second mover: " + 
         first.equals(secondMover) );
      System.out.println("Compare with first sensor: " + 
         first.equals(firstSensor ) );
      System.out.println("Compare with second sensor: " + 
         first.equals(secondSensor ) );
   }


}
