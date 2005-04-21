package simkit.smdx;
import java.awt.geom.*;
import java.util.*;
/**
 * Similar to PathMoverManager.  Follows a list of way points.  When the
 * last one is reached, instead of stopping, the Mover is sent to the 
 * first waypoint and starts all over.
 *
 * @version $Id$
 * @author  ahbuss
 */
public class PatrolMoverManager extends PathMoverManager {
    
    /**
     *
     * @param mover Mover to be managed
     */    
    public PatrolMoverManager(Mover mover) {
        super(mover);
    }
    
    /**
     *
     * @param mover Mover to be managed
     * @param wayPoints List containing Waypoints to be visited
     */    
    public PatrolMoverManager(Mover mover, List wayPoints) {
        super(mover, wayPoints);
    }
    
    /**
     *
     * @param mover Mover to be managed
     * @param wayPoints Array of Waypoints to be visited
     */    
    public PatrolMoverManager(Mover mover, WayPoint[] wayPoints) {
        super(mover, wayPoints);
    }
    
    /**
     * The speed on each leg is the max speed of the Mover
     * @param mover Mover to be managed
     * @param wayPoints Array of Point2D's to be visited
     */    
    public PatrolMoverManager(Mover mover, Point2D[] wayPoints) {
        super(mover, wayPoints);
    }
    
    /**
     * If this manager's Mover, go to next waypoint if there are any.
     * If not, start over.
     * @param m Mover whose EndMove event has occured
     */    
    public void doEndMove(Mover m) {
        if (getMover() == m) {
            if (nextWayPoint.hasNext()) {
                WayPoint nextWP = (WayPoint) nextWayPoint.next();
                getMover().moveTo(nextWP.getWayPoint(), nextWP.getSpeed());
            }
            else {
                start();
            }
        }
    }
}
