package simkit.smdx;

import java.util.*;
import simkit.*;
import simkit.smd.*;
import java.awt.geom.*;

public class PathMoverManager extends SimEntityBase {
    
    private static final double EPSILON = 1.0E-14;
    
    private List wayPoints;
    private Mover mover;
    private Iterator nextWayPoint;
    private boolean startOnReset;
    
    public PathMoverManager(Mover m, Point2D[] path) {
        this.mover = m;
        mover.addSimEventListener(this);
        wayPoints = new ArrayList();
        for (int i = 0; i < path.length; i++) {
            addWayPoint(path[i]);
        }
    }
    
    public PathMoverManager(Mover m, List path) {
        this(m, (Point2D[]) path.toArray(new Point2D[path.size()]));
    }
    
    public PathMoverManager(Mover m) {
        this(m, new ArrayList());
    }
    
    public void start() {
        nextWayPoint = wayPoints.iterator();
        if (nextWayPoint.hasNext()) {
            WayPoint nextWP = (WayPoint) nextWayPoint.next();
            mover.moveTo(nextWP.getWayPoint(), nextWP.getSpeed());
        }
    }
    
    public void stop() {
        mover.stop();
    }
    
    public void doEndMove(Mover m) {
        if ((mover == m) && nextWayPoint.hasNext()) {
            WayPoint nextWP = (WayPoint) nextWayPoint.next();
            mover.moveTo(nextWP.getWayPoint(), nextWP.getSpeed());
        }
        else if (mover.getMovementState() != MovementState.STOPPED) {
            stop();
        }
    }
    
    public boolean isMoving() {return mover.isMoving();}
    
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
    
    public void clearPath() {
        if (isMoving()) { stop(); }
        wayPoints.clear();
    }
    
    public void addWayPoint(WayPoint wayPoint) {
        wayPoints.add(wayPoint);
    }
    
    public void addWayPoint(Point2D point, double speed) {
        addWayPoint(new WayPoint((Point2D) point.clone(), speed));
    }
    
    public void addWayPoint(Point2D point) {
        addWayPoint(point, mover.getMaxSpeed());
    }
    
    public void removeWayPoint(Point2D point) {
        for (Iterator i = wayPoints.iterator(); i.hasNext();) {
            Point2D nextPoint = (Point2D) i.next();
            if (nextPoint.distance(point) < EPSILON) {
                wayPoints.remove(nextPoint);
                break;
            }
        }
    }
    
    public List getWayPoints() {
        return new ArrayList(wayPoints);
    }
    
    public Mover getMover() {return mover;}
    
    public void setStartOnReset(boolean b) { startOnReset = b; }
    
    public boolean isStartOnReset() { return startOnReset; }
    
    public void reset() {
        super.reset();
        if (isStartOnReset()) {
            start();
        }
    }
}
