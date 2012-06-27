package simkit.smd.animate;

import simkit.actions.ActionUtilities;
import simkit.actions.visual.ShapeIcon;
import simkit.animate.PingPainter;
import simkit.animate.PingThread;
import simkit.animate.VCRControlPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import javax.swing.Icon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import simkit.actions.MyFrame;
import simkit.smd.Mover;
import simkit.smd.Sensor;

/**
 * @version $Id: SandboxFrame.java 81 2009-11-16 22:28:39Z ahbuss $
 * @author ahbuss
 */
public class SandboxFrame extends MyFrame {
    
    private Sandbox2 sandbox;
    private Inspector inspector;
    private VCRControlPanel vcrControlPanel;
    
    /** Creates a new instance of SandboxFrame */
    public SandboxFrame(String title) {
        super(title);
        sandbox = new Sandbox2();
        sandbox.setBackground(Color.white);
        getContentPane().setBackground(Color.white);
//        sandbox.setOpaque(false);
        getContentPane().add(sandbox, BorderLayout.CENTER);
        
        JMenuBar menuBar = new JMenuBar();
        
        JMenu menu = ActionUtilities.createMenu("File", this.getAppCloser(), new String[] { "close" });
        menuBar.add(menu);
        setJMenuBar(menuBar);
        
        inspector = new Inspector();
        
        PingThread pingThread = new PingThread(0.075, 100);
        vcrControlPanel = new VCRControlPanel(pingThread);
        vcrControlPanel.addVerboseButton();
        PingPainter painter = new PingPainter(sandbox);
        pingThread.addSimEventListener(painter);
        
        getContentPane().add(vcrControlPanel, BorderLayout.NORTH);
        
        setSize(600, 500);
    }
    
    public SandboxFrame() {
        this("");
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
    
    public Sandbox2 getSandbox() { return sandbox; }
    
    public VCRControlPanel getControlPanel() { return vcrControlPanel; }
    
}
