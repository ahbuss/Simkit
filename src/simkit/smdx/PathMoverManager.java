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
    
    public PathMoverManager(Mover m, List path) {
        this.mover = m;
        mover.addSimEventListener(this);
        this.setWayPoints(path);
    }
    
    public PathMoverManager(Mover m) {
        this(m, new ArrayList());
    }
    
    public void start() {
        nextWayPoint = wayPoints.iterator();
        if (nextWayPoint.hasNext()) {
            mover.moveTo(  (Point2D) nextWayPoint.next());
        }
    }
    
    public void stop() {
        mover.stop();
    }
    
    public void doEndMove(Mover m) {
        if ((mover == m) && nextWayPoint.hasNext()) {
            mover.moveTo((Point2D) nextWayPoint.next());
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
        }
    }
    
    public void clearPath() {
        if (isMoving()) { stop(); }
        wayPoints.clear();
    }
    
    public void addWayPoint(Point2D point) {
        wayPoints.add((Point2D) point.clone());
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
}
