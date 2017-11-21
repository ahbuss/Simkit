
package simkit.smdx;

import java.awt.geom.Point2D;
/**
* The guide point (ZZ) for a formation.
*
* @author John Ruck (Rolands and Associates Corporation 6/7/06)
* @version $Id$
**/
public class FormationLeader extends UniformLinearMover {

    /**
     * Creates a FormationLeader at the given point that cannot move.
     * @param location given point
     */
    public FormationLeader(Point2D location) {
        super(location);
    }

    /**
     * Creates a FormationLeader at the given point at the given max speed.
     * @param location given point
     * @param maxSpeed given max speed
     */
    public FormationLeader(Point2D location, double maxSpeed) {
        super(location, maxSpeed);
    }

    /**
     * Creates a FormationLeader at the given point that cannot move.
     * @param name Given name
     * @param location Given point
     */
    public FormationLeader(String name, Point2D location) {
        super(name, location);
    }

    /**
     * Creates a FormationLeader at the given point at the given max speed.
     * @param name Name of FormationLeader
     * @param location given point
     * @param maxSpeed given max speed
     */
    public FormationLeader(String name, Point2D location, double maxSpeed) {
        super(name, location, maxSpeed);
    }

}
