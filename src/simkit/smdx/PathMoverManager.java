package simkit.smdx;

import java.util.*;
import simkit.*;
import java.awt.geom.*;

/**
 * @author Arnold Buss
 */
public class PathMoverManager extends SimEntityBase implements MoverManager {
    
    private static final double EPSILON = 1.0E-10;
    
    protected List wayPoints;
    private Mover mover;
    protected Iterator nextWayPoint;
    private boolean startOnReset;
    
    /**
     * @param m Mover this instance is managing
     * @param path Array of WayPoints for mover to follow
     */
    public PathMoverManager(Mover m, WayPoint[] path) {
        this(m);
        for (int i = 0; i < path.length; i++) {
            addWayPoint(path[i]);
        }
    }
    
    /**
     * @param m Mover this instance is managing
     * @param path Array of waypoints
     */
    public PathMoverManager(Mover m, Point2D[] path) {
        this(m);
        for (int i = 0; i < path.length; i++) {
            addWayPoint(path[i]);
        }
    }
    
    /**
     * @param m Mover this instance is managing
     * @param path List of WayPoints
     */
    public PathMoverManager(Mover m, List path) {
        this(m, (WayPoint[]) path.toArray(new WayPoint[path.size()]));
    }
    
    /**
     * @param m Mover this instance is managing
     */
    public PathMoverManager(Mover m) {
        setMover(m);
        mover.addSimEventListener(this);
        wayPoints = new ArrayList();
    }
    
    /** Start by moving to first WayPoint
     */
    public void start() {
        nextWayPoint = wayPoints.iterator();
        if (nextWayPoint.hasNext()) {
            WayPoint nextWP = (WayPoint) nextWayPoint.next();
            mover.moveTo(nextWP.getWayPoint(), nextWP.getSpeed());
        }
    }
    
    /** Stop moving
     */
    public void stop() {
        mover.stop();
    }
    
    /** When an EndMove event is heard, move to next WayPoint, if there
     * are any remaning; else, stop.
     * @param m Should be this instance's Mover
     */
    public void doEndMove(Mover m) {
        if (mover == m) {
            if (nextWayPoint.hasNext()) {
                WayPoint nextWP = (WayPoint) nextWayPoint.next();
                mover.moveTo(nextWP.getWayPoint(), nextWP.getSpeed());
            }
            else if (mover.getMovementState() != MovementState.STOPPED) {
                stop();
            }
        }
    }
    
    /**
     * @return Whether Mover is moving along its path
     */
    public boolean isMoving() {return mover.isMoving();}
    
    /** Note: make a deep copy and check for bad data
     * @param path List of WayPoints
     */
    public void setWayPoints(List path) {
        if (path == null) {
            throw new IllegalArgumentException("PathMoverManager needs a non-null path.");
        }
        if (isMoving()) { stop(); }
        this.wayPoints = new Vector(path.size());
        // Check that every element is a Coordinate.
        for (Iterator e = path.iterator(); e.hasNext();) {
            Object nextPoint = e.next() ;
            if (nextPoint instanceof Point2D) {
                addWayPoint((Point2D) nextPoint);
            }
            else if (nextPoint instanceof WayPoint) {
                addWayPoint( (WayPoint) nextPoint);
            }
        }
    }
    
    /** Clear waypoint list
     */
    public void clearPath() {
        if (isMoving()) { stop(); }
        wayPoints.clear();
    }
    
    /**
     * @param wayPoint The WayPoint added to end of path - WayPoint includes
     * the Point2D and the desired speed
     */
    public void addWayPoint(WayPoint wayPoint) {
        wayPoints.add(wayPoint.clone());
    }
    
    /**
     * @param point The next destination, added to end of current path
     * @param speed The desired speed to move to this point
     */
    public void addWayPoint(Point2D point, double speed) {
        addWayPoint(new WayPoint((Point2D) point.clone(), speed));
    }
    
    /**
     * @param point Location added to end of path - speed is assumed to be maximun speed.
     */
    public void addWayPoint(Point2D point) {
        addWayPoint(point, mover.getMaxSpeed());
    }
    
    /**
     * @param point Point to be removed
     */
    public void removeWayPoint(Point2D point) {
        for (Iterator i = wayPoints.iterator(); i.hasNext();) {
            Point2D nextPoint = ((WayPoint) i.next()).getWayPoint();
            if (nextPoint.distance(point) < EPSILON) {
                wayPoints.remove(nextPoint);
                break;
            }
        }
    }
    
    /**
     * @return Shallow copy of WayPoints
     */
    public List getWayPoints() {
        return new ArrayList(wayPoints);
    }
    
    /**
     * @return My Mover
     */
    public Mover getMover() {return mover;}
    
    /**
     * @param b Whether to invoke start() in reset().
     */
    public void setStartOnReset(boolean b) { startOnReset = b; }
    
    /**
     * @return Whether start() is invoked in reset().
     */
    public boolean isStartOnReset() { return startOnReset; }
    
    /** If startOnReset is true, schedule Start event
     */
    public void reset() {
        super.reset();
        if (isStartOnReset()) {
            waitDelay("Start", 0.0);
        }
    }
    
    /** Starts Mover on path
     */
    public void doStart() {
        start();
    }
    
    /** Must remove as SimEventListener from old Mover (if it exists)
     * and add self to new one.
     * @param newMover The new Mover to manage
     */
    public void setMover(Mover newMover) {
        if (mover != null) {
            mover.removeSimEventListener(this);
        }
        mover = newMover;
        mover.addSimEventListener(this);
    }
    
}
