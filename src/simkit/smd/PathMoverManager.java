package simkit.smd;

import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.ListIterator;
import simkit.SimEntityBase;

/**
 * Basic MoverManager.  Maintains list of WayPoints for managing a single
 * Mover. When started, directs Mover to move to each WayPoint in succession,
 * and when last WayPoint is reached, stop.
 * @version $Id: PathMoverManager.java 81 2009-11-16 22:28:39Z ahbuss $
 * @author ahbuss
 */
public class PathMoverManager extends SimEntityBase {

    /**
     * List of desired WayPoints the Mover is to traverse
     */
    private LinkedList<WayPoint> wayPoint;

    /**
     * Points to next WayPoint if hasNext() is true.
     */
    protected ListIterator<WayPoint> nextWayPointIter;

    /**
     * If true, then start Mover from Run event
     */
    private boolean startOnRun;

    /**
     * The one Mover this instance is managing
     */
    private Mover mover;

    /**
     * Instantiate a PathMoverManager with the given Mover, WayPoints, and
     * whether to start immediately or wait.
     * @param mover My Mover
     * @param waypoint List of WayPoints to traverse
     * @param startOnRun If true, start from Run event
     */
    public PathMoverManager(Mover mover, 
            LinkedList<WayPoint> waypoint,
            boolean startOnRun) {
        setMover(mover);
        setWaypoint(waypoint);
        setStartOnRun(startOnRun);
    }

    /**
     * Set nextWayPointIter to beginning of waypoint
     */
    public void reset() {
        super.reset();
        nextWayPointIter = wayPoint.listIterator();
    }

    /**
     * If startOnRun is true, schedule Start.
     */
    public void doRun() {
        if (isStartOnRun()) {
            waitDelay("Start", 0.0);
        }
    }

    /**
     * If there is a WayPoint, schedule StartMove(d, s), where d is the
     * location and s is the speed specified by the WayPoint objst.
     */
    public void doStart() {
        nextWayPointIter = wayPoint.listIterator();
        
        WayPoint next = nextWayPointIter.hasNext() ? nextWayPointIter.next() : null;
        firePropertyChange("nextWayPoint", next);

        if (next != null) {
            waitDelay("MoveTo", 0.0, next.getPoint(), next.getSpeed());
        }
    }

    /**
     * Empty - to be heard.
     * @param destination desired destination
     * @param speed desired speed
     */
    public void doMoveTo(Point2D destination, double speed) {
    }

    /**
     * Heard from mover. If there is another WayPoint, schedule MoveTo(d,s)
     * for it; otherwise, schedule OrderStop(mover).
     * @param mover My mover
     */
    public void doEndMove(Mover mover) {
        WayPoint next = nextWayPointIter.hasNext() ? nextWayPointIter.next() : null;
        firePropertyChange("nextWayPoint", next);

        if (next != null) {
            waitDelay("MoveTo", 0.0, next.getPoint(), next.getSpeed());
        }

        if (next == null) {
            waitDelay("OrderStop", 0.0, mover);
        }
    }

    /**
     * Schedule OrderStop(mover).
     */
    public void doStop() {
        waitDelay("OrderStop", 0.0, mover);
    }

    /**
     * @return the list of WayPoints (shallow copy)
     */
    public LinkedList<WayPoint> getWaypoint() {
        return new LinkedList<WayPoint>(wayPoint);
    }

    /**
     * @param waypoint the wayPoint to set
     */
    public void setWaypoint(LinkedList<WayPoint> waypoint) {
        this.wayPoint = new LinkedList<WayPoint>(waypoint);
    }

    /**
     * If true, start moving at beginning of simulation
     * @return the startOnRun
     */
    public boolean isStartOnRun() {
        return startOnRun;
    }

    /**
     * @param startOnRun the startOnRun to set
     */
    public void setStartOnRun(boolean startOnRun) {
        this.startOnRun = startOnRun;
    }

    /**
     * @return the mover
     */
    public Mover getMover() {
        return mover;
    }

    /**
     * @param mover the mover to set
     */
    public void setMover(Mover mover) {
        if (this.mover != null) {
            this.mover.removeSimEventListener(this);
            this.removeSimEventListener(this.mover);
        }
        this.mover = mover;
        this.mover.addSimEventListener(this);
        this.addSimEventListener(this.mover);
    }

    /**
     * This is for purety and consistency since this is a state variable.
     * There is no legitimate reason for any component to have a reference
     * to this.
     * @return the nextWayPointIter
     */
    public ListIterator<WayPoint> getNextWayPointIter() {
        return nextWayPointIter;
    }

    /**
     * This is a derived state from waypoint and nextWayPointIter
     * @return next WayPoint or null if none
     */
    public WayPoint getNextWayPoint() {
        int index = nextWayPointIter.nextIndex();
        if (index < wayPoint.size()) {
            return wayPoint.get(index);
        } else {
            return null;
        }
    }

}