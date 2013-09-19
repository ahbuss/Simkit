package simkit.smdx.test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.*;
import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import simkit.Schedule;
import simkit.SimEvent;
import simkit.SimEventListener;
import simkit.actions.AppCloser;
import simkit.actions.visual.ShapeIcon;
import simkit.animate.PingPanel;
import simkit.animate.PingThread;
import simkit.random.RandomVariate;
import simkit.random.RandomVariateFactory;
import simkit.smdx.Mover;
import simkit.smdx.RandomLocationMoverManager;
import simkit.smdx.UniformLinearMover;

/**
 *  <P> A more substantial test of the PingThread class.  Each instance of
 * <CODE>AnimationTest</CODE> has certain number of Movers in it, each
 *  controlled by a RandomLocationMoverManager that will simply send it to
 *  a uniform location on the screen.   The application keeps track of how
 *  many AnimationTests have been created and exits the program when the
 *  last one has been closed.
 **/

public class AnimationTest extends JFrame implements SimEventListener {
    
    private static int numberFrames = 0;   // Counts the # of AnimationFrame instances
    
    private List<Mover> entities;               // This instance's movers
    private Icon icon;                     // Everyone gets the same Icon (for expediency)
    private Image offscreen;               // For double-buffering the display
    private JPanel sandbox;
    
    public AnimationTest(String title) {
        super(title);
        
        numberFrames++;
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        this.addWindowListener(new AppCloser());
        
        sandbox = new JPanel();
        sandbox.setBackground(Color.white);
        
        getContentPane().add(sandbox, BorderLayout.CENTER);
        entities = new ArrayList<>();
//        icon = new ImageIcon(AnimationTest.class.getResource("images/mover.gif"));     // hard-wired -- I know, I know...
        Rectangle2D rect = new Rectangle(0, 0, 40, 30);
        icon = new ShapeIcon(rect, true);
        ((ShapeIcon) icon).setFillColor(Color.blue);
    }
    
    /**
     *  Redraw the screen based on the current position of the Movers using
     *  double-buffering.
     **/
    public void updateEntities() {
        Graphics g = sandbox.getGraphics();
        if (offscreen == null) {
            offscreen = sandbox.createImage(sandbox.getSize().width,
            sandbox.getSize().height);
        }
        Graphics dbuf = offscreen.getGraphics();
        dbuf.setColor(Color.white);
        dbuf.fillRect(0, 0, getContentPane().getSize().width, getContentPane().getSize().height);
        for (Mover nextMover : entities) {
            Point2D loc = nextMover.getLocation();
            icon.paintIcon(getContentPane(), dbuf, (int) loc.getX(), (int) loc.getY());
        }
        g.drawImage(offscreen, 0, 0, this);
        g.dispose();
        dbuf.dispose();
    }
    
    /**
     *  Adds a new mover.
     * 
     * @param mover = the new Mover added.
     */
    public void addMover(Mover mover) {
        if (!entities.contains(mover) ){
            entities.add(mover);
        }
    }
    
    /**
     *  Adds a new mover.
     * 
     * @param mover = the removed Mover.
     */
    public void removeMover(Mover mover) {
        entities.remove(mover);
    }
    
    /**
     *  Gets a copy of my Movers in a thread-safe manner.
     **/
    public List<Mover> getMovers() {
        return new ArrayList<>(entities);
    }
    
    /**
     *  Here's where I hear the Ping event and update my entities.
     **/
    
    @Override
    public void processSimEvent(SimEvent e) {
        if (e.getEventName().equals("Ping")) {
            this.updateEntities();
        }
    }
    public JPanel getSandbox() { return sandbox; }
    
    /**
     *  Read in the number of frames and entities in each frame, with defaults of
     *  1 frame with 10 entities.
     **/
    public static void main(String[] args) {
        //    Schedule.setReallyVerbose(true);
        
        BufferedWriter outputLog = null;
        PingThread pt = new PingThread(.1, 10);
        
        //        PropertyChangeFrame pcf = new PropertyChangeFrame();
        //        pt.addPropertyChangeListener(pcf);
        //        pcf.setVisible(true);
        PingPanel pp = new PingPanel(pt);
        pt.addSimEventListener(pp);
        
        
        int numberUnits = (args.length > 0) ? Integer.valueOf(args[0]).intValue() : 10;
        int numberFrames = (args.length > 1) ? Integer.valueOf(args[1]).intValue() : 1;
        
        for (int i = 0; i < numberFrames; i++ ) {
            AnimationTest at = new AnimationTest("Animation Test");
            pt.addSimEventListener(at);
            RandomVariate[] rv = new RandomVariate[2];
            rv[0] = RandomVariateFactory.getInstance("simkit.random.UniformVariate",
            new Object[] { new Double(0.0), new Double(500) });
            rv[1] = RandomVariateFactory.getInstance("simkit.random.UniformVariate",
            new Object[] { new Double(0.0), new Double(500) });
            
            for (int j = 0; j < numberUnits; j++) {
                Mover vm = new UniformLinearMover("Mover-" + j,new java.awt.geom.Point2D.Double(250, 250), 50);
                RandomLocationMoverManager rlmm = new RandomLocationMoverManager(vm,
                //                new Coordinate(0, 0), new Coordinate(500, 500));
                rv);
                rlmm.setStartOnReset(true);
                
                at.addMover(vm);
            }
            pp.addVerboseButton();
            
            JToolBar toolbar = new JToolBar();
            toolbar.add(pp);
            at.getContentPane().add(toolbar, BorderLayout.NORTH);
            
            at.setBounds(100 + 20 * i, 100 + 20 * i, 600, 600);
            at.setVisible(true);
            at.repaint();
        }
        Schedule.setVerbose(false);
    }
    
    
}
