package simkit.smdx;

import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.*;

import simkit.EventList;
import simkit.Priority;
import simkit.Schedule;
import simkit.Schedule;
import simkit.SimEntityBase;
import simkit.SimEvent;
import simkit.SimEventListener;
/**
* Causes its Mover to move relative to a FormationLeader which acts as
* a guide point (ZZ).
*
* @author John Ruck (Rolands and Associates Corporation 6/7/06)
* @version $Id$
**/
public class FormationMoverManager extends SimEntityBase implements MoverManager {

    public static final String _VERSION_ = "$Id$";

    public static Logger log = Logger.getLogger("simkit.smdx");

/**
* The data used to determine the location relative to the FormationLeader.
**/
    protected StationData station;

/**
* The station that the Mover to take at the start of the simulation.
**/
    protected StationData originalStation;

/**
* The Mover this manager controls.
**/
    protected Mover mover;

/**
* The FormationLeader that is the guide for the formation.
**/
    protected FormationLeader leader;

/**
* How close we need to be to our station.
**/
    protected double stationTolerance = 1.0;

/**
* Makes a new manager for the given Mover.
**/
    public FormationMoverManager(Mover m) {
        this.mover = m;
    }

/**
* How close we need to be to our station.
**/
    public void setStationTolerance(double value) {
        if (value < 0.0) {
            String msg = "StationTolerance must be non-negative, it was " + value;
            log.severe(msg);
            throw new IllegalArgumentException(msg);
        }
        this.stationTolerance = value;
    }

/**
* How close we need to be to our station.
**/
/**
* The FormationLeader that is the guide for the formation.
**/
    public void setFormationLeader(FormationLeader value) {
        if (leader != null) {
            leader.removeSimEventListener(this);
        } 
        this.leader = value;
        if (leader != null) {
            leader.addSimEventListener(this);
        }
    }

/**
* The FormationLeader that is the guide for the formation.
**/
    public FormationLeader getFormationLeader() {return leader;}

/**
* The data used to determine the location relative to the FormationLeader.
* Should only be used to initially set the station, use the ChangeStation
* event to change the station during a replication.
**/
    public void setOriginalStationData(StationData value) {this.originalStation = value;}

/**
* The speed that the Mover should initially try to move to regain station.
**/
    protected double cruisingSpeed = Double.POSITIVE_INFINITY;

/**
* The speed that the Mover should initially try to move to regain station.
**/
    public double getCruisingSpeed() {return cruisingSpeed;}

/**
* The speed that the Mover should initially try to move to regain station.
**/
    public void setCruisingSpeed(double value) {
        if (value <= 0.0) {
            String msg = "The Cruising speed must be positive, it was " + value;
            log.severe(msg);
            throw new IllegalArgumentException(msg);
        }
        this.cruisingSpeed = value;
    }

/**
* The Mover this manager controls.
**/
    public void setMover(Mover value) {
        if (mover != null) {
            mover.removeSimEventListener(this);
        }
        mover = value;
        if (value != null) {
            value.addSimEventListener(this);
        }
    }
        
/**
* The Mover this manager controls.
**/
    public Mover getMover() {return mover;}

/**
* Does nothing since the FormationMoverManager always starts on reset().
**/
    public void setStartOnReset(boolean value) {
        if (!value) {
            log.warning("Attempt to set startOnReset to false on the FormationMoverManager" 
                + this + " was ignored.");
        }
    }

/**
* Always returns true.
**/
    public boolean isStartOnReset() {return true;}

/**
* Stops the Mover.
**/
    public void stop() {
        mover.stop();
    }

/**
* Pick a new station point and start moving to it.
**/
    public void start() {
        changeStation();
    }

/**
* Pick a new station point and start moving to it.
**/
    public void doRun() {
        changeStation();
    }

/**
* If heard from the Mover, then pick a new station point and 
* start moving to it.
**/
    public void doEndMove(Mover m) {
        if (m == mover) {
            changeStation();
        }
    }

/**
* If heard from the FormationLeader, then pick a new station point and start
* moving to it.
**/
    public void doStartMove(Moveable m) {
        if (m == leader) {
            changeStation();
        }
    }

/**
* Resets to the initial station value.
**/
    public void reset() {
        super.reset();
        if (originalStation == null) {
            log.severe("The original station for " + getName() + " is null, this will "
                + " cause a NullPointerException later.");
        }
        station = originalStation;
    }

/**
* True if the controlled Mover is moving.
**/
    public boolean isMoving() {
        return mover.isMoving();
    }

/**
* Get the next station point, if close enough to it, then move with leader's 
* velocity, if not, intercept the point.
* The intercept is first attempted at cruising speed, then at max speed.
* If no intercept can be made, then move with the leader's velocity.
**/
    protected void changeStation() {
        stop();
        Point2D point = station.pickPoint(leader);
        if (point.distance(mover.getLocation()) <= stationTolerance) {
            mover.move(leader.getVelocity());
        } else {
            Mover surrogateTarget = new SurrogateTarget(point, leader);
            Point2D interceptPoint = Math2D.getIntercept(mover, cruisingSpeed, surrogateTarget);
            if (interceptPoint == null) {
                interceptPoint = Math2D.getIntercept(mover, surrogateTarget);
            }
            if (interceptPoint == null) {
                mover.move(leader.getVelocity());
                log.warning(Schedule.getSimTime() + ": " + this + " unable to intercept "
                    + " the current station, assuming leaders velocity.");
            } else {
                mover.moveTo(interceptPoint);
            }
        }
    }

/**
* A Mover with the minimum functionality to provide the data needed to be the 
* target Mover in Math2D.getIntercept().
* Only getLocation() and getVelocity() will work, everything else will throw
* an exception.
* TODO refactor Math2D and make this Class go away. (bug 1203)
**/
    protected static class SurrogateTarget implements Mover {

/**
* The time this surrogate was constructed.
**/
        protected double initialTime;

/**
* The initial location.
**/
        protected Point2D initialLocation;

/**
* The velocity of the leader and therefore the station point.
**/
        protected Point2D velocity;

/**
* Creates a new surrogate based on the given initial location and the velocity of
* the given Mover.
**/
        protected SurrogateTarget(Point2D point, Mover leader) {
            initialTime = Schedule.getSimTime();
            initialLocation = point;
            velocity = leader.getVelocity();
        }

/**
* The current location of the point.
**/
        public Point2D getLocation() {
            Point2D ret = Math2D.scalarMultiply(Schedule.getSimTime() - initialTime, velocity);
            ret = Math2D.add(initialLocation, ret);
            return ret;
        }

/**
* The current velocity of the point.
**/
        public Point2D getVelocity() {
            return velocity;
        }

        public static final String MESSAGE = "Not implemented. SurrogateTarget should only "
            + "be used by the FormationMoverManager when calling Math2D.getIntercept()";

        public void accelerate(Point2D p) {
            throw new RuntimeException(MESSAGE);
        }

        public void accelerate(Point2D a, double speed) {
            throw new RuntimeException(MESSAGE);
        }

        public void doEndMove(Moveable mover) {
            throw new RuntimeException(MESSAGE);
        }

        public void doStartMove(Moveable mover) {
            throw new RuntimeException(MESSAGE);
        }

        public double getMaxSpeed() {
            throw new RuntimeException(MESSAGE);
        }

        public MovementState getMovementState() {
            throw new RuntimeException(MESSAGE);
        }

        public boolean isMoving() {
            throw new RuntimeException(MESSAGE);
        }

        public void magicMove(Point2D loc) {
            throw new RuntimeException(MESSAGE);
        }

        public void move(Point2D vel) {
            throw new RuntimeException(MESSAGE);
        }

        public void moveTo(Point2D d) {
            throw new RuntimeException(MESSAGE);
        }

        public void moveTo(Point2D d, double t) {
            throw new RuntimeException(MESSAGE);
        }

        public String paramString() {
            throw new RuntimeException(MESSAGE);
        }

        public void pause() {
            throw new RuntimeException(MESSAGE);
        }

        public void stop() {
            throw new RuntimeException(MESSAGE);
        }

        public Point2D getAcceleration() {
            throw new RuntimeException(MESSAGE);
        }

        public EventList getEventList() {
            throw new RuntimeException(MESSAGE);
        }

        public int getEventListID() {
            throw new RuntimeException(MESSAGE);
        }

        public Priority getPriority() {
            throw new RuntimeException(MESSAGE);
        }

        public int getSerial() {
            throw new RuntimeException(MESSAGE);
        }
    
        public void handleSimEvent(SimEvent e) {
            throw new RuntimeException(MESSAGE);
        }

        public void interrupt(String n, Object... o) {
            throw new RuntimeException(MESSAGE);
        }

        public void interruptAll() {
            throw new RuntimeException(MESSAGE);
        }
        
        public void interruptAll(String n, Object... o) {
            throw new RuntimeException(MESSAGE);
        }

        public boolean isPersistant() {
            throw new RuntimeException(MESSAGE);
        }

        public boolean isReRunnable() {
            throw new RuntimeException(MESSAGE);
        }

        public void reset() {
            throw new RuntimeException(MESSAGE);
        }

        public void setEventListID(int id) {
            throw new RuntimeException(MESSAGE);
        }

        public void setPersistant(boolean p) {
            throw new RuntimeException(MESSAGE);
        }

        public void setPriority(Priority d) {
            throw new RuntimeException(MESSAGE);
        }

        public SimEvent waitDelay(String n, double d) {
            throw new RuntimeException(MESSAGE);
        }

        public SimEvent waitDelay(String n, double d, Object... o) {
            throw new RuntimeException(MESSAGE);
        }

//        public SimEvent waitDelay(String n, double d, Object[] o, double p) {
//            throw new RuntimeException(MESSAGE);
//        }
        
        public SimEvent waitDelay(String n, double d, Priority p, Object... o) {
            throw new RuntimeException(MESSAGE);
        }

        public String getName() {
            throw new RuntimeException(MESSAGE);
        }

        public void setName(String n) {
            throw new RuntimeException(MESSAGE);
        }

        public void addSimEventListener(SimEventListener l) {
            throw new RuntimeException(MESSAGE);
        }

        public SimEventListener[] getSimEventListeners() {
            throw new RuntimeException(MESSAGE);
        }

        public void notifyListeners(SimEvent e) {
            throw new RuntimeException(MESSAGE);
        }

        public void removeSimEventListener(SimEventListener l) {
            throw new RuntimeException(MESSAGE);
        }

        public void processSimEvent(SimEvent event) {
            throw new RuntimeException(MESSAGE);
        }

        public void setProperty(String name, Object value) {
            throw new RuntimeException(MESSAGE);
        }

        public Object getProperty(String name) {
            throw new RuntimeException(MESSAGE);
        }

        public Object getProperty(String name, Object defaultValue) {
            throw new RuntimeException(MESSAGE);
        }
    
        public void firePropertyChange(PropertyChangeEvent event) {
            throw new RuntimeException(MESSAGE);
        }
    
        public void addPropertyChangeListener(PropertyChangeListener listener) {
            throw new RuntimeException(MESSAGE);
        }
    
        public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
            throw new RuntimeException(MESSAGE);
        }

        public void removePropertyChangeListener(PropertyChangeListener listener) {
            throw new RuntimeException(MESSAGE);
        }
    
        public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
            throw new RuntimeException(MESSAGE);
        }

        public PropertyChangeListener[] getPropertyChangeListeners() {
            throw new RuntimeException(MESSAGE);
        }
    
        public String[] getAddedProperties() {
            throw new RuntimeException(MESSAGE);
        }
        
        public int compareTo(simkit.SimEntity other) {
            throw new RuntimeException(MESSAGE);
        }

        public boolean isClearAddedPropertiesOnReset() {
            return true;
        }

        public void setClearAddedPropertiesOnReset(boolean b) {
        }
    }
}
