package simkit.smd;

import java.awt.geom.Point2D;
import simkit.SimEntityBase;
import simkit.random.RandomVariate;

/**
 * MoverManager that generates a random Point2D and directs its Mover
 * to go there. When the Mover arrives, another destination is
 * generated and it is directed to go there. Continue until
 *
 * @author ahbuss
 */
public class RandomMoverManager extends SimEntityBase {

    private RandomVariate[] coordinate;
    private boolean startOnRun;
    private Mover mover;

    public RandomMoverManager() { }
    
    /**
     * Instantiate a RandomMoverManager with the given Mover,
     * Random coordinate generators and whether to start immediately.
     * @param mover My Mover
     * @param coordinate RandomVariate array to generate coordinates
     * @param startOnRun If true, start from Run event
     */
    public RandomMoverManager(Mover mover,
            RandomVariate[] coordinate,
            boolean startOnRun) {
        setMover(mover);
        setCoordinate(coordinate);
        setStartOnRun(startOnRun);
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
     * Schedule MoveTo(d) where d is a Point2D with randomly generated
     * coordinates
     */
    public void doStart() {
        waitDelay("MoveTo", 0.0,
                new Point2D.Double(coordinate[0].generate(),
                coordinate[1].generate()));
    }

    /**
     * Empty - to be heard by Mover
     * @param destination Given destination
     */
    public void doMoveTo(Point2D destination) {
    }

    /**
     * Generate another random destination d and schedule MoveTo(d)
     * @param mover My Mover
     */
    public void doEndMove(BasicLinearMover mover) {
        waitDelay("MoveTo", 0.0,
                new Point2D.Double(coordinate[0].generate(),
                coordinate[1].generate()));
    }

    /**
     * Schedule OrderStop(mover). This will scheduled by another
     * component and heard by this one.
     */
    public void doStop() {
        waitDelay("OrderStop", 0.0, mover);
    }

    /**
     * Empty - heard by Mover
     * @param mover My Mover
     */
    public void doOrderStop(Mover mover) {
    }

    /**
     * @return the coordinate
     */
    public RandomVariate[] getCoordinate() {
        return coordinate.clone();
    }

    /**
     * @param coordinate the coordinate to set
     */
    public void setCoordinate(RandomVariate[] coordinate) {
        this.coordinate = coordinate.clone();
    }

    /**
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

    public String paramString() {
        return super.toString();
    }

    public String toString() {
        return "RandomMoverManager " + getMover() + " ["
                + coordinate[0] + ", " + coordinate[1] + "]";
    }
}
