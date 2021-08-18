package simkit;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.ScrollPane;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import simkit.actions.GenericAction;

/**
 * This class is used to display version and copyright information about SimKit.
 * Constructing a Version object will cause a dialog box to pop-up containing
 * information about the program.
 *
 * @author ahbuss
 *
 */
public class Version {

    private static final Logger LOGGER = Logger.getLogger(Version.class.getName());

    /**
     * Contains version information. Loaded during static initialization from
     * the file version.txt
     *
     */
    private static String SIMKIT_VERSION = "Not Found";

    /**
     * Contains the copyright statement. Loaded during static initialization
     * from the file copyright.txt
     *
     */
    private static String SIMKIT_COPYRIGHT = "";

    /**
     * Contains information on the GNU license. Loaded during static
     * initialization from the file gnu.txt
     *
     */
    private static String SIMKIT_MESSAGE = "";

    private static final String P = "<p>";

    private static final String BR = "<br>";

    private static String NOTE = "<HTML>"
            + "This is just information about the Simkit version and license.  "
            + P + BR
            + "Simkit is not an IDE or graphical environment.  "
            + P
            + "To create a Simkit model, you must create Java classes to "
            + BR
            + "represent the components of your model.  "
            + P
            + "See the documentation "
            + "or the simkit.examples for examples of this.  "
            + P
            + "Simkit's web site is: <a href=\"https://github.com/ahbuss/Simkit\">"
            + "https://github.com/ahbuss/Simkit</a>. "
            + P
            + "Contact: <a href=\"mailto:abuss@nps.edu\">abuss@nps.edu</a>";

    private static final int VERSION_NUMBER;

    private static final int SUBVERSION_NUMBER;

    private static final int SUBSUBVERSION_NUMBER;

    /**
     * The last character in a version string may be a number, optionally
     * followed by a single letter of either case.
     */
    static {
        InputStream is = null;
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(
                    Version.class.getResourceAsStream("version.txt"), "UTF-8"));

            String versonLine = bufferedReader.readLine().trim();
            String[] split = versonLine.split(":");
            SIMKIT_VERSION = split[1].trim();
            bufferedReader.close();

            bufferedReader = new BufferedReader(new InputStreamReader(
                    Version.class.getResourceAsStream("copyright.txt")));
            StringBuilder buf = new StringBuilder();
            for (String nextLine = bufferedReader.readLine(); nextLine != null; nextLine = bufferedReader.readLine()) {
                buf.append(nextLine);
                buf.append(BR);
            }
            SIMKIT_COPYRIGHT = buf.toString();
            bufferedReader.close();

            bufferedReader = new BufferedReader(new InputStreamReader(
                    Version.class.getResourceAsStream("gnu.txt")));
            buf = new StringBuilder();
            for (String nextLine = bufferedReader.readLine(); nextLine != null; nextLine = bufferedReader.readLine()) {
                buf.append(nextLine);
                buf.append(BR);
            }
            SIMKIT_MESSAGE = buf.toString();
        } catch (IOException e) {
            throw (new RuntimeException(e));
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    LOGGER.log(Level.SEVERE, null, ex);
                }
            }
        }
        String[] ver = getVersion().split("[[a-zA-Z]\\.]");
        if (ver.length < 3) {
            throw new RuntimeException("Bad Version: " + getVersion()
                    + " token length = " + ver.length);
        }
        VERSION_NUMBER = Integer.parseInt(ver[0]);
        SUBVERSION_NUMBER = Integer.parseInt(ver[1]);
        SUBSUBVERSION_NUMBER = Integer.parseInt(ver[2]);
    }

    public static int getVersionNumber() {
        return VERSION_NUMBER;
    }

    public static int getSubVersionNumber() {
        return SUBVERSION_NUMBER;
    }

    public static int getSubSubVersionNumber() {
        return SUBSUBVERSION_NUMBER;
    }

    public static boolean isAtLeastVersion(String otherVersion) {
        String[] split = otherVersion.split("[[a-zA-Z]\\.]");
        if (split.length < 3) {
            throw new IllegalArgumentException("Not legitimate version string: "
                    + otherVersion);
        }
        if (Integer.parseInt(split[0]) > Version.getVersionNumber()) {
            return false;
        } else if (Integer.parseInt(split[0]) < Version.getVersionNumber()) {
            return true;
        } else if (Integer.parseInt(split[1]) > Version.getSubVersionNumber()) {
            return false;
        } else if (Integer.parseInt(split[1]) < Version.getSubVersionNumber()) {
            return true;
        } else if (Integer.parseInt(split[2]) > Version.getSubSubVersionNumber()) {
            return false;
        }
        return true;
    }

    /**
     * Cause the program to exit, closing the information box.
     *
     */
    public void exit() {
        System.exit(0);
    }

    /**
     * Create and display the information window. The dialog box will close when
     * the user selects "OK"
     *
     */
    public Version() {
        JWindow frame = new JWindow();

        URL logo = Version.class.getResource("SimkitLogo.png");
        ImageIcon icon = new ImageIcon(logo);
        JLabel label = new JLabel(icon);
        label.setBackground(Color.cyan);
        label.setOpaque(true);
        label.setBorder(BorderFactory.createEtchedBorder());

        JEditorPane editorPane = new JEditorPane();
        editorPane.setContentType("text/html");
        editorPane.setText("<h1>Simkit version " + getVersion() + "</h1>"
                + NOTE + P
                + getCopyright() + P + getMessage() + "</HTML>");
        editorPane.addHyperlinkListener(new BrowseTo());
        editorPane.setEditable(false);
        JPanel panel = new JPanel();
        JButton button = new JButton(new GenericAction(this, "exit"));
        button.setText("Close");
        panel.add(button);
        panel.setBackground(Color.white);

        ScrollPane scrollPane = new ScrollPane(ScrollPane.SCROLLBARS_AS_NEEDED);
        scrollPane.add(editorPane);
        frame.getContentPane().add(label, BorderLayout.NORTH);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        frame.getContentPane().add(panel, BorderLayout.SOUTH);
        frame.setSize(500, 600);

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation((int) (0.5 * (screen.getWidth() - frame.getWidth())),
                (int) (0.5 * (screen.getHeight() - frame.getHeight())));
        frame.setVisible(true);
    }

    /**
     *
     * @return the copyright information.
     */
    public static final String getCopyright() {
        return SIMKIT_COPYRIGHT;
    }

    /**
     *
     * @return the GNU license information.
     */
    public static final String getMessage() {
        return SIMKIT_MESSAGE;
    }

    /**
     *
     * @return the version information.
     */
    public static final String getVersion() {
        return SIMKIT_VERSION;
    }

    /**
     * Create and display the information window. The dialog box will close when
     * the user selects "OK"
     *
     * @param args Given command line arguments (not used)
     * @throws java.lang.Throwable if something throws an Exception somewhere
     */
    public static void main(String[] args) throws Throwable {
        new Version();
    }

    private static class BrowseTo implements HyperlinkListener {

        @Override
        public void hyperlinkUpdate(HyperlinkEvent e) {
            if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                Desktop desktop = Desktop.getDesktop();
                try {
                    URI uri = e.getURL().toURI();
                    desktop.browse(uri);
                } catch (URISyntaxException | IOException ex) {
                    LOGGER.severe(ex.getMessage());
                }
            }
        }

    }

}
