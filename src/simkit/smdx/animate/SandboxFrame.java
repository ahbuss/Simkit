package simkit.smdx.animate;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import javax.swing.Icon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import simkit.actions.ActionUtilities;
import simkit.actions.visual.ShapeIcon;
import simkit.animate.PingPainter;
import simkit.animate.PingPanel;
import simkit.animate.PingThread;
import simkit.smdx.Mover;
import simkit.smdx.Sensor;

/**
 * @version $Id$
 * @author ahbuss
 */
public class SandboxFrame extends simkit.actions.MyFrame {

    private Sandbox sandbox;
    private Inspector inspector;
    private PingPanel vcrControlPanel;

    public SandboxFrame() {
        this("Simple Sandbox");
    }
    
    /**
     * Creates a new instance of SandboxFrame
     * @param title Given title
     */
    public SandboxFrame(String title) {
        super(title);
        sandbox = new Sandbox();
//        sandbox.setBackground(Color.white);
        sandbox.setOpaque(false);
        getContentPane().add(sandbox, BorderLayout.CENTER);

        JMenuBar menuBar = new JMenuBar();

        JMenu menu = ActionUtilities.createMenu("File", this.getAppCloser(), new String[]{"close"});
        menuBar.add(menu);
        setJMenuBar(menuBar);

        inspector = new Inspector();

        PingThread pingThread = new PingThread(0.075, 100);
        vcrControlPanel = new PingPanel(pingThread);
        vcrControlPanel.addVerboseButton();
        PingPainter painter = new PingPainter(sandbox);
        pingThread.addSimEventListener(painter);

        getContentPane().add(vcrControlPanel, BorderLayout.NORTH);

        setSize(600, 500);
    }

    public void addSensor(Sensor sensor) {
        SensorIcon icon = new SensorIcon(sensor, new ShapeIcon(new Rectangle2D.Double(0, 0, 0, 0)));
        icon.addMouseListener(inspector);
        sandbox.add(icon);
    }

    public void addSensor(Sensor sensor, Color color) {
        ShapeIcon footprint = new ShapeIcon(new Ellipse2D.Double(0, 0, 2.0 * sensor.getMaxRange(),
                2.0 * sensor.getMaxRange()));
        footprint.setFilled(false);
        SensorIcon icon = new SensorIcon(sensor, footprint);
        icon.setColor(color);
        icon.addMouseListener(inspector);
        sandbox.add(icon);
    }

    public void addMover(Mover mover, Color color) {
        Icon icon = new ShapeIcon(new Rectangle2D.Double(0, 0, 10, 10), Color.black, color, true);
        MoverIcon moverIcon = new MoverIcon(mover, icon);
        moverIcon.addMouseListener(inspector);
        sandbox.add(moverIcon);
    }

    public Sandbox getSandbox() {
        return sandbox;
    }

    public PingPanel getControlPanel() {
        return vcrControlPanel;
    }

    public void setDeltaT(double deltaT) {
        this.getControlPanel().getVcrController().setDeltaT(deltaT);
    }

    public void setMillisPerSimtime(double millisPerSimTime) {
        this.getControlPanel().getVcrController().setMillisPerSimtime(millisPerSimTime);
    }
}
