package simkit.smd;

/**
  * The basic interface for a moving object.  These are the minimal set of
  * methods that anything that moves must provide.  The implementation is 
  * responsible for ensuring that requested information is correctly provided
  * and that the motion is carried out correctly.  
  *
  * <P>The two move methods provided different information regarding the
  * entity's destination.  <CODE>moveTo(Coordinate)</CODE> has the destination
  * as its argument, whereas <CODE>moveInDirection(Coordinate)</CODE> has the
  * direction as the argument.  It is the implementation's responsibility to
  * convert the given direction into an appropriate velocity vector.
  *
  * @author Arnold H. Buss
  * @version 0.2
  * 
**/

import simkit.SimEntity;

public interface Mover extends SimEntity {

/**
  * Move to the destination provided.
  *
  * @param Coordinate destination
  * @exception smd.MovingException if the entity cannot move toward the destination
**/
   public void moveTo(Coordinate destination) ;
   
/**
  * Move to the destination provided at the given speed.
  *
  * @param Coordinate destination
  * @param atSpeed The desired speed; should be overridden if this exceeds the
  *        Mover's capabilities 
  * @exception smd.MovingException if the entity cannot move toward the destination
**/
   public void moveTo(Coordinate destination, double atSpeed) ;
   

/**
  * Stops the entity
**/
   public void stop();

/**
  * Returns the current position of the entity.  Typically, some kind of 
  * dead reckoning algorithm is best suited.
**/
   public Coordinate getCurrentLocation();

/**
  * Returns the velocity of the entity.  Note that velocity has both direction 
  * and magnitude.
**/
   public Coordinate getVelocity();

/**
  * Returns the speed of the entity, a scalar quantity without direction.  
**/
   public double getSpeed();

   public double getMoveTime();

   public void doStartMove(Mover m);
   public void doEndMove(Mover m);
}
