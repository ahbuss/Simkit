package simkit.actions;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
/**
 * Basic JFrame with some convenient additions.  Not necessarily for 
 * prime time.
 *
 * @version $Id$
 * @author ahbuss
 */
public class MyFrame extends JFrame implements PropertyChangeListener {
    
    private JLabel status;
    private JComponent statusArea;
    private WindowListener closer;
    
    private static Point DEFAULT_WINDOW_LOCATION;
    private static Dimension DEFAULT_WINDOW_DIMENSION;
    
    static {
        DEFAULT_WINDOW_LOCATION = new Point(100, 100);
        DEFAULT_WINDOW_DIMENSION = new Dimension(300, 350);
    }
    
    public MyFrame(String title, WindowListener closer,
    int x, int y, int w, int h) {
        this(title, x, y, w, h);
        this.closer = closer;
    }
    
    public MyFrame(String title, int x, int y, int w, int h) {
        super(title);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setAppCloser(new AppCloser());
        JPanel statusArea = new JPanel();
        status = new JLabel(" ");
        status.setForeground(Color.black);
        statusArea.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        //      statusArea.setBorder(BorderFactory.createLoweredBevelBorder());
        statusArea.add(status);
        getContentPane().add(statusArea, BorderLayout.SOUTH);
        
        setBounds(x, y, w, h);
    }
    
    public MyFrame(String title, WindowListener closer, Rectangle r) {
        this(title, closer, r.x, r.y, r.width, r.height);
    }
    
    public MyFrame(String title, Rectangle r) {
        this(title, r.x, r.y, r.width, r.height);
    }
    
    public MyFrame(String title, Point p, Dimension d) {
        this(title,  new Rectangle(p, d));
    }
    
    public MyFrame(String title) {
        this(title, DEFAULT_WINDOW_LOCATION, DEFAULT_WINDOW_DIMENSION);
    }
    
    public MyFrame() {
        this("");
    }
    
    /**
     * @param newStatus New status to write in status bar
     */
    public void setStatus(String newStatus) {
        status.setText(newStatus);
    }
    
    /** @param newStatus the toString() will be written in status bar
     */
    public void setStatus(Object newStatus) {
        setStatus(newStatus.toString());
    }
    
    /** @return current status text
     */
    public String getStatus() {return status.getText();}
    
    /** @return current status component
     */
    public JComponent getStatusArea() {return statusArea;}
    
    /**
     * @param newStatusArea replaces existing status component
     */
    public void setStatusArea(JComponent newStatusArea) {
        statusArea = newStatusArea;
    }
    
    /** @return current AppCloser
     */
    public WindowListener getAppCloser() {return closer;}
    
    /** @param closer new "closer"
     */
    public void setAppCloser(WindowListener closer) {
        this.closer = closer;
        this.addWindowListener(closer);
    }
    
    /** 
     * If a property called "status" is heard, set the status bar to the 
     * new value.
     * @param e Heard PropertyChangeEvent
     */
    public void propertyChange(PropertyChangeEvent e) {
        if (e.getPropertyName().equals("status")) {
            setStatus(e.getNewValue());
        }
    }    
}