package simkit.actions;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.Icon;
import javax.swing.JOptionPane;

/**
 * A bare bones "closer" that either exits or "closes" a Window to which it is
 * listening. Additionally, an Action can be tied to the two public methods.
 *
 *
 * @author ahbuss
 */
public class AppCloser extends WindowAdapter implements ActionListener {

    private boolean notifyOnClose;
    private boolean exitOnClose;
    private Window window;
    private Icon icon;

    public AppCloser(boolean noc, boolean eoc) {
        notifyOnClose = noc;
        exitOnClose = eoc;
    }

    public AppCloser() {
        this(true, true);
    }

    /**
     * Invoke close() method when window closes.
     *
     * @param e Given WindowEvent
     */
    @Override
    public void windowClosing(WindowEvent e) {
        window = (Window) e.getSource();
        close();
    }

    /**
     * Close window -- in this case, the application also closes.
     */
    public void close() {
        if (exitOnClose) {
            if (notifyOnClose) {
                int result = JOptionPane.showConfirmDialog(window,
                        "Exit the program? ",
                        "Exit?",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        icon);
                if (result == JOptionPane.YES_OPTION) {
                    exit();
                } else if (result == JOptionPane.NO_OPTION && window != null) {
                    window.dispose();
                }
            } else {
                exit();
            }
        } else {
            window.dispose();
        }
    }

    public void setNotifyOnClose(boolean noc) {
        notifyOnClose = noc;
    }

    public boolean isNotifyOnClose() {
        return notifyOnClose;
    }

    public void setExitOnClose(boolean eoc) {
        exitOnClose = eoc;
    }

    public boolean isExitOnClose() {
        return exitOnClose;
    }

    /**
     * Exit the application, wherever it is.
     */
    public void exit() {
        System.exit(0);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equalsIgnoreCase("exit")
                || e.getActionCommand().equalsIgnoreCase("close")) {
            close();
        }
    }

    /**
     * @return the icon
     */
    public Icon getIcon() {
        return icon;
    }

    /**
     * @param icon the icon to set
     */
    public void setIcon(Icon icon) {
        this.icon = icon;
    }

}
