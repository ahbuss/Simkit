package simkit.smd;

/**
 *  Computes the time at which a uniform linear mover enters the range of a sensor.
**/
public class EnterRange {

/**
 *  Computes the enter and exit times for a uniform linear mover and a sensor.
 *  This applies whether or not the sensor is a "cookie cutter" since it is used
 *  to schedule the EnterRange and ExitRange events, which are orthogonal to the
 *  detection alogorithm of the sensor.  
 *  @param target The target in the interaction
 *  @param sensor The sensor in the interaction.
 *  @return an array of doubles -- the first value is the time until enter range
 *          (possibly 0); the second is the time to when range is exited.
**/
  public static double[] getRangeTimes(Mover target, Sensor sensor) {
    double[] times = null;
    if (target instanceof BasicMover && sensor.getMoverDelegate() instanceof BasicMover) {
// get target location relative to sensor
      Coordinate relativeLocation = target.getCurrentLocation().decrementBy(sensor.getCurrentLocation());
// get target velocity relative to sensor
      Coordinate relativeVelocity = target.getVelocity().decrementBy(sensor.getVelocity());
      double speedSquared = relativeVelocity.dotBy(relativeVelocity);
      double distanceSquared = relativeLocation.dotBy(relativeLocation);
      double middleTerm = relativeVelocity.dotBy(relativeLocation);

      times = Quadratic.solve(speedSquared, 2 * middleTerm, distanceSquared -
              sensor.getMaxRange() * sensor.getMaxRange());
    }
    else {
      throw new IllegalArgumentException ("Only BasicMovers supported for Range Calculations");
    }
    return times;
  }

}
