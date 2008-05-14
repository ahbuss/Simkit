
package simkit.smdx;

import java.awt.geom.Point2D;
/**
* The guide point (ZZ) for a formation.
*
* @author John Ruck (Rolands and Associates Corporation 6/7/06)
* @version $Id: FormationLeader.java 1147 2006-06-07 23:20:30Z jlruck $
**/
public class FormationLeader extends UniformLinearMover {

/**
* Creates a FormationLeader at the given point that cannot move.
**/
    public FormationLeader(Point2D location) {
        super(location);
    }

/**
* Creates a FormationLeader at the given point at the given max speed.
**/
    public FormationLeader(Point2D location, double maxSpeed) {
        super(location, maxSpeed);
    }

/**
* Creates a FormationLeader at the given point that cannot move.
**/
    public FormationLeader(String name, Point2D location) {
        super(name, location);
    }

/**
* Creates a FormationLeader at the given point at the given max speed.
**/
    public FormationLeader(String name, Point2D location, double maxSpeed) {
        super(name, location, maxSpeed);
    }

}
