/*
 * PingPainter.java
 *
 * Created on February 5, 2002, 4:54 PM
 */

package simkit.animate;

import java.awt.Component;
import simkit.BasicSimEntity;

/**
 * @version $Id$
 * @author ahbuss
 */
public class PingPainter extends BasicSimEntity {
    
    private Component myPanel;
    private boolean fullSpeed;
    
    /** Creates new PingPainter */
    public PingPainter() {
        setFullSpeed(false);
    }

    @Override
    public void handleSimEvent(simkit.SimEvent simEvent) {
    }
    
    @Override
    public void processSimEvent(simkit.SimEvent simEvent) {
        if (isFullSpeed()) {
            doPing();
        }
        else if (simEvent.getEventName().equals("Ping")) {
            doPing();
        }
    }
    
    public PingPainter(Component panel) {
        setPanel(panel);
    }
    
    public void doPing() {
        if (myPanel != null) {
            myPanel.repaint();
        }
    }
    
    public void setPanel(Component c) { myPanel = c; }
    
    public Component getPanel() { return myPanel; }
    
    @Override
    public void reset() {
        super.reset();
        if (myPanel !=null) {
            myPanel.repaint();
        }
    }
    
    public void setFullSpeed(boolean b) { fullSpeed = b; }
    
    public boolean isFullSpeed() { return fullSpeed; }

}
