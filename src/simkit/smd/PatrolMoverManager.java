package simkit.smd;

import java.util.LinkedList;

/**
 * Identical functionality to PathMoverManager, except at last WayPoint,
 * start over from the beginning.
 * 
 * @version $Id: PatrolMoverManager.java 81 2009-11-16 22:28:39Z ahbuss $
 * @author ahbuss
 */
public class PatrolMoverManager extends PathMoverManager {

    /**
     * Instantiate a PatrolMoverManager with the indicated Mover
     * instance, list of WayPoints, and whether to start immediately.
     * @param mover My mover
     * @param waypoint List of WayPoints to patrol
     * @param startOnRun if true, starts on Run event.
     */
    public PatrolMoverManager(Mover mover, 
            LinkedList<WayPoint> waypoint,
            boolean startOnRun) {
        super(mover, waypoint, startOnRun);
    }

    /**
     * If another WayPoint, start moving there. If not, then start from the
     * beginning.
     * @param mover My Mover
     */
    @Override
    public void doEndMove(Mover mover) {
        WayPoint next = nextWayPointIter.hasNext() ? nextWayPointIter.next() : null;
        firePropertyChange("nextWayPoint", next);

        if (next != null) {
            waitDelay("MoveTo", 0.0, next.getPoint(), next.getSpeed());
        }

        if (next == null) {
            waitDelay("Start", 0.0);
        }
    }
}