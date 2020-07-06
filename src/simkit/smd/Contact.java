package simkit.smd;

import java.awt.geom.Point2D;

/**
 * @author ahbuss
 */
public class Contact {

    private BasicLinearMover myMover;

    public Contact() {
    }

    public Contact(BasicLinearMover myMover) {
        this.myMover = myMover;
    }

    public Point2D getCurrentLocation() {
        return myMover.getCurrentLocation();
    }

    @Override
    public String toString() {
        Point2D location = getCurrentLocation();
        return "Contact [" + BasicLinearMover.FORM.format(location.getX())
                + ", " + BasicLinearMover.FORM.format(location.getY()) + "]";
    }
}
