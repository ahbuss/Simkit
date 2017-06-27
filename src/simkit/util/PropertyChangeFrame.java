package simkit.util;

import java.awt.*;
import java.beans.*;
import javax.swing.*;
import simkit.*;

/**
 * <P>
 * A frame is displayed that logs the properties fired by whichever objects it
 * is listening to. For parsimony, this class has no bells or whistles. If you
 * want to save or print the contents, highlight the text with the mouse and
 * enter Ctrl-C (or command-C on Mac) to copy to the clipboard. Then open a text
 * editor and paste.
 *
 * <P>
 * Usage:
 * <PRE>
 *      PropertyChangeFrame pcf = new PropertyChangeFrame();
 *      &#60;object 1&#62;.addPropertyChangeListener(pcf);
 *      &#60;object 2&#62;.addPropertyChangeListener(pcf);
 *           ...
 *      pcf.setVisible(true);
 * </PRE>
 *
 * <P>
 * Note: since PropertyChangeFrame extends javax.swing.JFrame, you can add more
 * customization if desired.
 *
 * @author Arnold Buss, based on a class by CAPT Maximo Moore, USA.
 * @version $Id$
 *
 */
public class PropertyChangeFrame extends JFrame implements PropertyChangeListener {

    /**
     * The text containing the logs of property change events.
     *
     */
    protected JTextArea text;

    protected BasicEventList eventList;

    /**
     * Construct a PropertyChangeEvent
     *
     * @param bounds The java.awt.Rectangle determining the bounds of the
     * PropertyChangeEvent
     * @param eventListID Use this EventList id
     *
     */
    public PropertyChangeFrame(Rectangle bounds, int eventListID) {
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        text = new JTextArea();
        text.setEditable(false);
        this.getContentPane().add(new JScrollPane(text), BorderLayout.CENTER);
        this.setBounds(bounds);
        this.setTitle("Property Change Frame");
        this.setEventListID(eventListID);
    }

    /**
     * Instantiate PropertyChangeFrame with the EventList id 0
     * @param bounds Given bounding box for this PropertyChangeFrame
     */
    public PropertyChangeFrame(Rectangle bounds) {
        this(bounds, 0);
    }

    /**
     * Instantiate a PropertyChangeEvent with default bounds (200, 200, 300, 200).
     *
     */
    public PropertyChangeFrame() {
        this(new Rectangle(200, 200, 300, 200));
    }

    /**
     * Respond to a PropertyChangeEvent by appending a line to the text area.
     * The following data will be shown: Current time, current event, source
     * object of the event, the old value, the new value.
     *
     * @param e The PropertyChangeEvent "heard" by this PropertyChangeFrame.
     *
     */
    @Override
    public void propertyChange(PropertyChangeEvent e) {
        text.append("[" + Schedule.getCurrentEvent() + "] " + e.getSource()
                + " " + e.getPropertyName()
                + ": " + e.getOldValue() + " -> " + e.getNewValue() + '\n');
        text.setCaretPosition(text.getText().length());
    }

    /**
     * 
     * @param id Given EventList id
     */
    public void setEventListID(int id) {
        eventList = Schedule.getEventList(id);
    }

    /**
     * 
     * @return Current EventList id
     */
    public int getEventListID() {
        return this.eventList.getID();
    }
}
