package simkit.animate;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.geom.Point2D;
import java.net.URL;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * 
 * @author  ahbuss
 */
public class Sandbox extends JPanel {

    private Image backgroundImage;
    protected Point2D origin;
    private boolean drawAxes;

    /** Creates new Sandbox */
    public Sandbox() {
        this.setLayout(null);
        this.setOpaque(false);
        origin = new Point2D.Double();
        setOrigin(new Point2D.Double());
    }

    public void setOrigin(Point2D o) {
        origin.setLocation(o);
    }

    public Point2D getOrigin() {
        return (Point2D) origin.clone();
    }

    public void setDrawAxes(boolean b) {
        drawAxes = b;
    }

    public boolean isDrawAxes() {
        return drawAxes;
    }

    public void setBackroundImage(Image bkgdImage) {
        int width = bkgdImage.getWidth(this);
        int height = bkgdImage.getHeight(this);
        setSize(width, height);
        backgroundImage = bkgdImage;
    }

    public void setBackroundImage(String file) {
        URL url = getClass().getResource(file);
        Image bkgdImage = Toolkit.getDefaultToolkit().getImage(url);

        int width = bkgdImage.getWidth(this);
        int height = bkgdImage.getHeight(this);
        setSize(width, height);
        backgroundImage = bkgdImage;
    }

    public void removeBackgroundImage() {
        backgroundImage = null;
    }

    @Override
    public Component add(Component c) {
        if (c instanceof MoverIcon) {
            ((MoverIcon) c).setOrigin(getOrigin());
        }
        return super.add(c);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, this);
        if (isDrawAxes()) {
            g.setColor(Color.lightGray);
            g.drawLine(0, (int) getOrigin().getY(), getWidth(),
                    (int) getOrigin().getY());
            g.drawLine((int) getOrigin().getX(), 0,
                    (int) getOrigin().getX(), getHeight());
        }
    }

    public void addAnnotation(String text, Point2D location) {
        JLabel newAnnotation = new JLabel(text);
        newAnnotation.setBounds((int) (origin.getX() + location.getX()),
                (int) (origin.getY() - location.getY()), 50, 20);
        add(newAnnotation);
    }
}
