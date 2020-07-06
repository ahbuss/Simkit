/*
 * SensorIcon.java
 *
 * Created on June 13, 2002, 6:46 PM
 */
package simkit.animate;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import javax.swing.Icon;
import simkit.actions.visual.ShapeIcon;
import simkit.smd.Sensor;

/**
 * @author ahbuss
 */
public class SensorIcon extends MoverIcon {

    private Sensor sensor;

    /**
     * Creates a new instance of SensorIcon
     *
     * @param sensor Given Sensor
     */
    public SensorIcon(Sensor sensor) {
        this(sensor, new ShapeIcon(new Ellipse2D.Double(0, 0, 2 * sensor.getMaxRange(),
                2 * sensor.getMaxRange())));
    }

    public SensorIcon(Sensor sensor, Icon icon) {
        super(sensor.getMover(), icon);
        this.sensor = sensor;
        setRangeWidth(2.0f);
        setSize(2 * (int) sensor.getMaxRange(), 2 * (int) sensor.getMaxRange());
    }

    public void setColor(Color c) {
        Icon icon = getIcon();
        if (icon instanceof ShapeIcon) {
            ((ShapeIcon) icon).setOutlineColor(c);
        }
    }

    public void setRangeWidth(float w) {
        Icon icon = getIcon();
        if (icon instanceof ShapeIcon) {
            ((ShapeIcon) icon).setStroke(new BasicStroke(w));
        }
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor s) {
        sensor = s;
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Point2D loc = getScreenLocation();
        Icon icon = getIcon();
        setBounds((int) (loc.getX() - icon.getIconWidth() * 0.5),
                (int) (loc.getY() - icon.getIconHeight() * 0.5),
                icon.getIconWidth(), icon.getIconHeight());
        icon.paintIcon(this, g, 0, 0);
    }

    @Override
    public String toString() {
        return sensor.toString();
    }
}
