package simkit.smdx;

/**
 * An interface for holding the data needed to define a movement location
 * relative to another MoverManager.
 *
 * 
 * @author John Ruck (Rolands and Associates Corporation - 6/6/06)
*
 */
public interface StationData {

    public static final String _VERSION_ = "$Id:";

    /**
     * Returns the next point that a Mover in the formation should attempt to
     * intercept. Implementations should give the point relative to the current
     * guide position and leave it up to the FormationMoverManager to deal with
     * the fact that the guide is moving.
     *
     * @param mover The Mover that represents the guide (ZZ)
     * @return the next point that a Mover in the formation should attempt to
     * intercept.
     */
    public java.awt.geom.Point2D pickPoint(Mover mover);
}
