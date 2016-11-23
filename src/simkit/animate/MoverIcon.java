package simkit.animate;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.text.DecimalFormat;
import javax.swing.Icon;
import javax.swing.JComponent;
import simkit.smd.Mover;

/**
 *
 * @version $Id$
 * @author ahbuss
 */
public class MoverIcon extends JComponent {

    private Icon icon;
    private Mover myMover;
    private DecimalFormat df;
    private Point location;
    private Point2D origin;
    private Point2D screenLocation;
    private double scale;

    /**
     * Creates new MoverImage
     * @param m Given Mover
     * @param icon Given Icon
     */
    public MoverIcon(Mover m, Icon icon) {
        this(m, icon, new Point2D.Double());
    }

    public MoverIcon(Mover m, Icon icon, Point2D origin) {
        this.setIcon(icon);
        this.setMover(m);
        this.setSize(icon.getIconWidth(), icon.getIconHeight());
        this.setPreferredSize(this.getSize());
        this.setMinimumSize(this.getSize());
        this.setOrigin(origin);
        this.location = new Point();
        this.screenLocation = new Point2D.Double();
        this.setScale(1.0);
    }

    public void setMover(Mover m) {
        myMover = m;
        this.setToolTipText(m.getName());
    }

    public void setIcon(Icon i) {
        icon = i;
    }

    public Icon getIcon() {
        return icon;
    }

    public Mover getMover() {
        return myMover;
    }

    @Override
    public Point getLocation() {
        location.setLocation(myMover.getCurrentLocation());
        return location;
    }

    public void setOrigin(Point2D origin) {
        if (this.origin == null) {
            this.origin = new Point2D.Double(origin.getX(), origin.getY());
        } else {
            this.origin.setLocation(origin);
        }
    }

    public Point2D getScreenLocation() {
        screenLocation = myMover.getCurrentLocation();
        screenLocation.setLocation(
                origin.getX() + scale * screenLocation.getX(),
                origin.getY() - scale * screenLocation.getY());
        return screenLocation;
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Point2D loc = getScreenLocation();
        setBounds((int) (loc.getX() - icon.getIconWidth() * 0.5),
                (int) (loc.getY() - icon.getIconHeight() * 0.5),
                icon.getIconWidth(), icon.getIconHeight());
        icon.paintIcon(this, g, 0, 0);
    }

    @Override
    public String toString() {
        return myMover.toString();
    }

    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        if (scale <= 0.0) {
            throw new IllegalArgumentException(
                    "scale must be > 0.0: " + scale);
        }
        this.scale = scale;
    }

    public Point2D getOrigin() {
        return origin;
    }

}
