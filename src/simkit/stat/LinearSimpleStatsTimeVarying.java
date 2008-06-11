package simkit.stat;

import java.util.logging.Logger;
import simkit.Schedule;

/**
 * <p>A SimpleStats that computes the basic statistics for piecewise linear 
 * functions.  An example is the inventory level.  However, you must make sure
 * that "jumps" (as in replenishments) have two property changes fired -
 * one at the "bottom" of the level just before replenishment, and one
 * at the "top", just after replenishment.  Also, make sure that a final
 * property change is fired at the very end so the last bit can be taken
 * into account.
 *
 * <p>The mean is computed using the "Kalman Filter" version that protects
 * against overflow.  However, the variance (the one most likely to overflow)
 * is done in the "sum-of-squares" manner, mostly because I havn't had a chance
 * to work out the Kalman Filter for the variance.
 *
 * @version $Id: LinearSimpleStatsTimeVarying.java 915 2006-09-14 22:46:24Z ahbuss $
 * @author ahbuss
 */
public class LinearSimpleStatsTimeVarying extends SimpleStatsTimeVarying {
    
    public static final Logger log = Logger.getLogger("stat");
    
    protected double lastObservation;
    
    protected double sumOfSquares;
    
    public LinearSimpleStatsTimeVarying() {
        this(DEFAULT_NAME);
    }
    
    public LinearSimpleStatsTimeVarying(String name) {
        super(name);
        setEventListID(Schedule.getDefaultEventList().getID());
        reset();
    }

    public simkit.stat.SamplingType getSamplingType() {
        return SamplingType.LINEAR;
    }

    public void reset() {
        super.reset();
        diff = 0.0;
        sumOfSquares = 0.0;
        lastObservation = Double.NaN;
        variance = Double.NaN;
        lastTime = eventList.getSimTime();
        mean = 0.0;
    }
    
    public void newObservation(double x) {
        if (eventList.getSimTime() < lastTime) {
            String message = "Observation in the past: " +
                    "last time = " + lastTime + " now: " + eventList.getSimTime();
            log.fine(message);
            throw new RuntimeException(message);
        }
        count++;
        if (x < minObs) { minObs = x; }
        if (x > maxObs) { maxObs = x; }
        if (Double.isNaN(lastObservation)) {
            mean = 0.0;
            variance = 0.0;
            diff = 0.0;
            sumOfSquares = 0.0;
            lastObservation = x;
            return;
        } else {
            double factor = 1.0 - lastTime / (eventList.getSimTime() - getStartTime());
            mean += factor * ( 0.5 * (x - lastObservation) + diff);
            diff = x - mean;
        }
        sumOfSquares +=  (eventList.getSimTime() - lastTime) *
                1.0/3.0 * (Math.pow(x, 2) + x * lastObservation + Math.pow(lastObservation, 2));
        variance = sumOfSquares / (eventList.getSimTime() - getStartTime()) - 
                Math.pow(mean, 2)  ;
        
        lastObservation = x;
        lastTime = eventList.getSimTime();
    }
    
    public double getMean() {
        if (eventList.getSimTime() > lastTime) {
            String message =  "Results not up-to-date; last observation at " +
                    lastObservation + ", current time is " + eventList.getSimTime();
            log.fine(message);
            throw new RuntimeException(message);
        }
        return mean; 
    }
    
    public double getVariance() {
        if (eventList.getSimTime() > lastTime) {
            String message =  "Results not up-to-date; last observation at " +
                    lastObservation + ", current time is " + eventList.getSimTime();
            log.fine(message);
            throw new RuntimeException(message);
        }
        return variance; 
    }
}