/*
 * MouseDisplay.java
 *
 * Created on April 1, 2002, 3:58 PM
 */

package simkit.test;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JTextArea;
/**
 *
 * @author  Arnold Buss
 * 
 */
public class MouseDisplay extends JTextArea implements MouseMotionListener {

    /** Creates new MouseDisplay */
    public MouseDisplay() {
        this.setEditable(false);
        this.setOpaque(false);
        this.setBorder(null);
    }

    public void mouseDragged(java.awt.event.MouseEvent mouseEvent) {
    }
    
    public void mouseMoved(MouseEvent e) {
        setText("[" + e.getX() + "," + e.getY() + "]");
    }
    
}
