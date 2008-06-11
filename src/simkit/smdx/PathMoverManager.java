package simkit.smdx;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import simkit.SimEntityBase;

/**
 * A manager that causes a Mover to move along a specified path.
 * @author Arnold Buss
 * @version $Id$
 */
public class PathMoverManager extends SimEntityBase implements MoverManager {
    
/**
* When comparing the location of waypoints, distances
* less than EPSILON are considered zero.
**/
    private static final double EPSILON = 1.0E-10;
    
/**
* The path for the Mover to follow.
**/
    protected List<WayPoint> wayPoints;

/**
* The Movers controlled by this MoverManager.
**/
    private Mover mover;

/**
* Iterates over the wayPoints.
**/
    protected Iterator<WayPoint> nextWayPoint;

/**
* If true, then when reset is called on this MoverManager,
* the Mover will be restarted on the path.
**/
    private boolean startOnReset;
    
    /**
     * Constructs a new PathMoverManager.
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
     * Constructs a new PathMoverManager.
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
     * Constructs a new PathMoverManager.
     * @param m Mover this instance is managing
     * @param path List of WayPoints
     */
    public PathMoverManager(Mover m, List<WayPoint> path) {
        this(m, path.toArray(new WayPoint[path.size()]));
    }
    
    /**
     * Constructs a new PathMoverManager with no path specified.
     * @param m Mover this instance is managing
     */
    public PathMoverManager(Mover m) {
        setMover(m);
        mover.addSimEventListener(this);
        wayPoints = new ArrayList<WayPoint>();
    }
    
    /** 
     * Start by moving to first WayPoint. When the Mover reaches the
     * first waypoint, an EndMove event will occur causing the Mover
     * to continue to the next WayPoint.
     */
    public void start() {
        nextWayPoint = wayPoints.iterator();
        if (nextWayPoint.hasNext()) {
            WayPoint nextWP = nextWayPoint.next();
            mover.moveTo(nextWP.getWayPoint(), nextWP.getSpeed());
        }
    }
    
    /** 
     * Stops the Mover at its current location.
     */
    public void stop() {
        mover.stop();
    }
    
    /** When an EndMove event is heard, move to next WayPoint, if there
     * are any remaining; else, stop.
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
     * Returns true if the Mover is currently moving.
     */
    public boolean isMoving() {return mover.isMoving();}
    
    /** 
     * Takes the given list of waypoints and makes them the path
     * for this MoverManager. The Mover is first stopped at its current
     * location. Any of the points that are not either 
     * a <CODE>Point2D</CODE> or a <CODE>WayPoint</CODE> are
     * ignored and not added as part of the new path.
     * If a point in the path is a <CODE>Point2D</CODE>, then the 
     * speed for that segment is set to the maximum speed of the Mover.
     * <CODE>WayPoints</CODE> contain speed information.
     * @param path List of WayPoints
     * @throws IllegalArgumentException If the given path is <CODE>null</CODE>
     * @see WayPoint
     * @see Point2D
     */
    public void setWayPoints(List<WayPoint> path) {
        if (path == null) {
            throw new IllegalArgumentException("PathMoverManager needs a non-null path.");
        }
        if (isMoving()) { 
            throw new RuntimeException("Cannot set path while moving");
        }
        this.wayPoints = new ArrayList<WayPoint>(path);
    }
    
    /** 
     * Stops the Mover and clears the waypoint list.
     */
    public void clearPath() {
        if (isMoving()) { stop(); }
        wayPoints.clear();
    }
    
    /**
     * Adds the given WayPoint to the end of the current path.
     * @param wayPoint The WayPoint added to end of path - WayPoint includes
     * the Point2D and the desired speed
     */
    public void addWayPoint(WayPoint wayPoint) {
        wayPoints.add(new WayPoint(wayPoint));
    }
    
    /**
     * Adds the given Point2D to the end of the current path with the
     * given speed.
     * @param point The next destination, added to end of current path
     * @param speed The desired speed to move to this point
     */
    public void addWayPoint(Point2D point, double speed) {
        addWayPoint(new WayPoint((Point2D) point.clone(), speed));
    }
    
    /**
     * Adds the Point2D to the end of the path with the Mover's maximum speed.
     * @param point Location added to end of path - speed is assumed to be maximum speed.
     */
    public void addWayPoint(Point2D point) {
        addWayPoint(point, mover.getMaxSpeed());
    }
    
    /**
     * Removes the given point from the path. Removes the
     * first point in the path that has the same location.
     * If there are no points in the path with the same location
     * as the point to remove, then does nothing.
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
     * Creates a shallow copy of the current path
     * @return Shallow copy of WayPoints
     */
    public List<WayPoint> getWayPoints() {
        return new ArrayList<WayPoint>(wayPoints);
    }
    
    /**
     * The Mover controlled by this PathMoverManager
     * @return My Mover
     */
    public Mover getMover() {return mover;}
    
    /**
     * If true, when reset, the PathMoverManger will start the Mover
     * on the current path.
     * @param b Whether to invoke start() in reset().
     */
    public void setStartOnReset(boolean b) { startOnReset = b; }
    
    /**
     * If true, when reset, the PathMoverManger will start the Mover
     * on the current path.
     * @return Whether start() is invoked in reset().
     */
    public boolean isStartOnReset() { return startOnReset; }
    
    /**  
     * Cancels all pending events for this MoverManager and
     * if startOnReset is true, schedule Start event.
     */
    public void reset() {
        super.reset();
        if (isStartOnReset()) {
            waitDelay("Start", 0.0);
        }
    }
    
    /** 
     * Starts Mover on the current path.
     */
    public void doStart() {
        start();
    }
    
    /** 
     * Sets the Mover that this MoverManager controls. 
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
