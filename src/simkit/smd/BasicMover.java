package simkit.smd;
/**
 *  The simplest mover that has uniform linear motion.
 *
 *  @author Arnold Buss
**/
import simkit.*;
import java.util.*;

public class BasicMover extends SimEntityBase implements Mover {

  protected Coordinate originalLocation;
  protected Coordinate destination;
  protected Coordinate lastStopLocation;
  protected double startMoveTime;
  protected double endMoveTime;
  protected double moveDuration;

  private double maxSpeed;
  private double currentSpeed;
  
  private Coordinate velocity;
/**
 *  Construct a BasicMover with initial location and maximum speed.
 *  @param location The BasicMover's initial location.
 *  @param maxSpeed The maximum speed of the BasicMover.
**/
  public BasicMover(Coordinate location, double maxSpeed) {
    if (location == null) {
       throw new IllegalArgumentException("BasicMover initial location null");
    }
    originalLocation = new Coordinate(location);
    lastStopLocation = new Coordinate(location);
    velocity = new Coordinate(0.0, 0.0);
    this.maxSpeed = maxSpeed;
    reset();
  }
/**
 *  Construct a BasicMover with name, location, and maxSpeed
 *  @param name The name of the BasicMover.
 *  @param location The initial location of the Basic Mover.
 *  @param maxSpeed The maximum speed of the BasicMover.
**/
  public BasicMover(String name, Coordinate location, double maxSpeed) {
    this(location, maxSpeed);
    setName(name);
  }
/**
 *  Construct a BasicMover with the parameters in String form.  If there are four
 *  tokens, the first is the name of the Mover.  If only three, the default name
 *  is given.  The next two tokens are the Coordinates of the initial location,
 *  the last is the maximum speed.  Ignored tokens are whitespace (space, tabs),
 *  commas, and parantheses.  Typical Strings might be "Hawk 3.0 4.0 5.0",
 *  "(3.0, 4.0) 5.0", etc.
 *  @params params The String form of the parameters.
**/
  public BasicMover(String params) {
    StringTokenizer tokens = new StringTokenizer(params, " \t(),");
    switch(tokens.countTokens()) {
      case 4:
        setName(tokens.nextToken());
      case 3:
        originalLocation = new Coordinate(tokens.nextToken() + " " + tokens.nextToken());
        maxSpeed = Double.valueOf(tokens.nextToken()).doubleValue();
        break;
      default:
        throw new IllegalArgumentException("Cannot construct BasicMover from String: " + params);
    }
    reset();
  }
/**
 *  Reset back to the initial state: located at the original location,
 *  no destination, and zero velocity.
**/
  public void reset() {
    super.reset();
    lastStopLocation = originalLocation;
    destination = null;
    velocity = new Coordinate(0.0, 0.0);
    currentSpeed = 0.0;
  }
/**
 *  To be called only from within BasicMover.
 *  @param dest The destination for the move.
**/
  protected void setDestination(Coordinate dest) {
     destination = (dest != null) ? new Coordinate(dest) : null;
  }
/**
 *  To be called only from with BasicMover.
 *  @param speed The speed of the mover; must be less than or equal to the
 *  maximum speed.
**/
  protected void setSpeed(double speed) {
     currentSpeed = Math.min(speed, maxSpeed);
  }
/**
 *  To be called only from with BasicMover.  Must have currentSpeed set first.
 *  @param direction The new direction of the mover; need not have unit length.r
**/
  protected void setVelocity(Coordinate direction) {
    if (direction != null) {
      velocity = direction.getDirection().scalarMultiply(getSpeed());
    }
    else {
      velocity = new Coordinate(0, 0);
      setSpeed(0.0);
    }
  }
/**
 *  Computes the time it would take to reach <CODE>destination</CODE> at speed
 *  <CODE>speed</CODE>.  No check is made to confirm that the Mover can in
 *  fact travel at this speed and no guarantee is made that this can in fact
 *  be done by this Mover.
 * @param destination The (hypothetical) destination
 * @param speed The (hypothetical) speed.
**/
  protected double getMoveTime(Coordinate dest, double speed) {
    if (destination != null) {
      Coordinate copy = new Coordinate(dest);
      return copy.decrementBy(getCurrentLocation()).getNorm() / speed;
    }
    return Double.POSITIVE_INFINITY;
  }
/**
 *   Returns the time of the unit's actual move.  Assumes that <CODE>destination</CODE>
 *   and <CODE>currentSpeed/CODE> have already been (correctly) set.
**/
  public double getMoveTime() {
    return getMoveTime(destination, currentSpeed);
  }

/**
 * @return the current destination - should be null if not moving.
**/
  public Coordinate getDestination() {return new Coordinate(destination);}
/**
 * @return the absolute time that move will end.
**/
  public double getEndMoveTime() {
    if (isMoving()) {
       return endMoveTime;
    }
    else {
      return Double.POSITIVE_INFINITY;
    }
  }
/**
 *  @return the current speed.
**/
  public double getSpeed() {return currentSpeed;}
/**
 *  @return the maximum possible speed.
**/
  public double getMaxSpeed() {return maxSpeed;}
/**
 *  @return the current velocity - (0.0,0.0) if not moving. 
**/
  public Coordinate getVelocity() {
    if (isMoving()) {
      return new Coordinate(velocity);
    }
    else {
      return new Coordinate(0, 0);
    }
  }
/**
 *  @return the whether or not we are moving. 
**/
  public boolean isMoving() {return velocity.getNorm() > 0.0;}
/**
 *  @return whether we are really good-to-go. 
**/
  public boolean isMoverReadyToGo() {
    return ((currentSpeed < Double.POSITIVE_INFINITY) && (velocity != null) );
  }
/**
 *  If not moving, last position; if moving, use dead reckoning.
 *  @return current location
**/
  public Coordinate getCurrentLocation() {
    if (isMoving()) {
      double timeMoved = (Schedule.simTime() - startMoveTime) ;
      Coordinate cp =  getVelocity().getDirection();
      return cp.scalarMultiply( getSpeed() * timeMoved).incrementBy(lastStopLocation);
    }
    else {
      return new Coordinate(lastStopLocation);
    }
  }
/**
 *  Order the BasicMover to begin moving to the given destination at the
 *  maximum speed.
 * @param destination The current destination of the BasicMover.
**/
  public void moveTo(Coordinate destination) {
    moveTo(destination, maxSpeed);
  }
/**
 *  Order the BasicMover to begin moving to the given destination at the given
 *  speed (or the maximum speed, whichever is smaller).
 *  @param destination The current destination of the BasicMover.
 *  @param atSpeed The speed at which the BasicMover is to travel or maxSpeed,
 *        whichever is smaller).
**/
  public void moveTo(Coordinate dest, double atSpeed) {
    setDestination(dest);
    setSpeed(atSpeed);
    waitDelay("StartMove", 0.0, this);
  }
/**
 *  Command the BasicMover to stop moving.  Its current position is set to wherever
 *  it happens to be.  <CODE>stop()</CODE> is intended to be an "abnormal" termination
 *  of a move, perhaps due to its platorm being damaged or destroyed, or
 *  perhaps a momentary interruption in response to information.
**/
  public void stop() {
    this.lastStopLocation = this.getCurrentLocation();
    this.setSpeed(0.0);
    this.setVelocity(new Coordinate(0.0, 0.0));
    interruptAll("EndMove", this);
    waitDelay("EndMove", 0.0, this);
  }
/**
 *  Event method signaling the start of a move.
 *  @param mover The mover who started moving: me.
**/
  public void doStartMove(Mover mover) {
    Coordinate destCopy = new Coordinate(destination);
    setVelocity(destCopy.decrementBy(getCurrentLocation()));
    startMoveTime = Schedule.simTime();
    moveDuration = getMoveTime();
    endMoveTime = Schedule.simTime() + moveDuration;
    if (!isMoverReadyToGo()) {
      throw new MovingException("Move started without proper initialization");
    }
    waitDelay("EndMove", getMoveTime(), this);
  }
/**
 *  Event method signaling the end of a move.
 *  @param mover The mover who stopped moving: me.
**/
  public void doEndMove(Mover mover) {
    lastStopLocation = getCurrentLocation();
     setVelocity(new Coordinate(0,0));
     startMoveTime = Schedule.simTime();
     endMoveTime = Schedule.simTime();
     moveDuration = 0.0;
  }
/**
 *  Responds to a "Ping" event by printing out itself to the console; probably
 *  not as useful as once thought.
**/
  public void doPing() {System.out.println(this);}
/**
 * @return String representing current state.
**/
  public String paramString() {
    return getCurrentLocation() + " " + getSpeed();
  }
/**
 *  @return String representing original location and max speed.
**/
  public String originalParameters() {
    return originalLocation + " " + maxSpeed;
  }
/**
 * @return String representing current state.
**/
  public String toString() {
    return getName() + " " + paramString();
  }
}
