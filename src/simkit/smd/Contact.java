package simkit.smd;

import simkit.*;
/**
 *  This class shields a sensor from having access to the actual target.
**/

public class Contact extends SimEntityBase implements Mover {

  private Mover mover;

  public Contact(Mover mover) {
    this.mover = mover;
    this.addSimEventListener(mover);
  }

  protected Mover getTarget() {return mover;}

   public void moveTo(Coordinate destination){}
   
   public void moveTo(Coordinate destination, double speed){}
   
   public void stop() {}

   public Coordinate getCurrentLocation() {return mover.getCurrentLocation();}

   public Coordinate getVelocity() {return mover.getVelocity();}

   public double getSpeed() {return mover.getSpeed();}

   public double getMoveTime() {return Double.NaN;}

   public String toString() {return mover.toString() + " [Contact]";}

   public void doStartMove(Mover m) {}

   public void doEndMove(Mover m) {}

}

