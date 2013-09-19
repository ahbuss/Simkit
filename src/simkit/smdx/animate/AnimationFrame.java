package simkit.smdx.animate;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import simkit.SimEvent;
import simkit.SimEventListener;



/**
 *  <P> A more substantial test of the PingThread class.  Each instance of
 * <CODE>AnimationTest</CODE> has certain number of Movers in it, each
 *  controled by a RandomLocationMoverManager that will simply send it to
 *  a uniform location on the screen.   The application keeps track of how
 *  many AnimationTests have been created and exits the program when the
 *  last one has been closed.
**/

public class AnimationFrame extends JFrame implements SimEventListener {

  private ArrayList<Component> entities;               // This instance's movers
  private Icon icon;                     // Everyone gets the same Icon (for expediency)
  private Image offscreen;               // For double-buffering the display
  private JPanel sandbox;

  private Point2D upperLeft;
  private Point2D lowerRight;

  public AnimationFrame(String title) {
    super(title);
    this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

    this.addWindowListener(
      new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent e) {
            int userSelects =
                JOptionPane.showConfirmDialog(AnimationFrame.this,
                    "Do you really want to quit?");
                if (userSelects == JOptionPane.OK_OPTION) {
                    System.exit(0);
                }
        }
      }
    );

    sandbox = new JPanel();
    sandbox.setBackground(Color.white);

    getContentPane().add(sandbox, BorderLayout.CENTER);
    entities = new ArrayList<Component>();
  }

/**
 *  Redraw the screen based on the current position of the Movers using
 *  double-buffering.
**/
  protected void updateEntities() {
    Graphics g = sandbox.getGraphics();
    if (offscreen == null) {
      offscreen = sandbox.createImage(sandbox.getSize().width,
             sandbox.getSize().height);
    }
    Graphics dbuf = offscreen.getGraphics();
    dbuf.setColor(Color.white);
    dbuf.fillRect(0, 0, getContentPane().getSize().width, getContentPane().getSize().height);
    for (Iterator i = entities.iterator(); i.hasNext();) {
      Component nextComponent = (Component) i.next();
// convert component's coordinates here and draw to offscreen buffer
//      icon.paintIcon(getContentPane(), dbuf, (int) loc.getXCoord(), (int) loc.getYCoord());
    }
    g.drawImage(offscreen, 0, 0, this);
    g.dispose();
    dbuf.dispose();
  }

/**
     *  Adds a new component.
     * 
     * @param component = the new Component added.
     */
  public void addComponent(Component component) {
    if (!entities.contains(component) ){
      entities.add(component);
    }
  }

/**
     *  .
     * 
     * @param component = the removed component.
     */
  public void removeComponent(Component component) {
    entities.remove(component);
  }

/**
 *  Here's where I hear the Ping event and update my entities.
**/

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
/*
 public static void main(String[] args) {
//    Schedule.setReallyVerbose(true);

    BufferedWriter outputLog = null;
    PingThread2 pt = new PingThread2(.1, 10, true);

    int numberUnits = (args.length > 0) ? Integer.valueOf(args[0]).intValue() : 10;
    int numberFrames = (args.length > 1) ? Integer.valueOf(args[1]).intValue() : 1;

    for (int i = 0; i < numberFrames; i++ ) {
      AnimationFrame at = new AnimationFrame("Animation Frame Test");
      pt.addSimEventListener(at);

      for (int j = 0; j < numberUnits; j++) {
        BasicMover vm = new BasicMover(new Coordinate(250, 250), 50);
        RandomLocationMoverManager rlmm = new RandomLocationMoverManager(vm,
          new Coordinate(0, 0), new Coordinate(500, 500));
        at.addMover(vm);
        at.getContentPane().add(new PingPanel(pt), BorderLayout.SOUTH);
      }

      at.setBounds(100 + 20 * i, 100 + 20 * i, 600, 600);
      at.setVisible(true);
      at.updateEntities();
    }
    Schedule.setVerbose(false);
  }*/


} 
