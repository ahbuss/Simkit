package simkit;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JWindow;

import simkit.actions.GenericAction;

/**
* This class is used to display version and copyright information about SimKit. Constructing a 
* Version object will cause a dialog box to pop-up containing information about the program.
* @version $Id$
 *@author ahbuss
**/
public class Version {
    
/**
* Contains version information. Loaded during static initialization from the file
* version.txt
**/
    private static String SIMKIT_VERSION = "Not Found";

/**
* Contains the copyright statement. Loaded during static initialization from the file
* copyright.txt
**/
    private static String SIMKIT_COPYRIGHT = "";

/**
* Contains information on the GNU license. Loaded during static initialization from the
* file gnu.txt
**/
    private static String SIMKIT_MESSAGE = "";
    
    private static String NOTE = "This is just information about the Simkit version and license.  " +
                SimEntity.NL +
                "Simkit is not an IDE or graphical environment.  " +
                SimEntity.NL +
                "To create a Simkit model, you must create Java classes to " +
                SimEntity.NL +
                "represent the components of your model.  See the documentation " +
                SimEntity.NL +
                "or the simkit.examples for examples of this.  " +
                SimEntity.NL +
                "Simkit's web site is: http://diana.nps.edu/Simkit.  " +
                SimEntity.NL +
                "You can post bugs here: https://diana.nps.edu/bugzilla";
    
    private static final int VERSION_NUMBER;
    
    private static final int SUBVERSION_NUMBER;
    
    private static final int SUBSUBVERSION_NUMBER;
    
    /**
     * The last character in a version string may be a number, optionally
     * followed by a single letter of either case.
     */
    static {
        InputStream is = null;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(
            Version.class.getResourceAsStream("version.txt")));

            SIMKIT_VERSION = br.readLine().trim();
            br.close();
            
            br = new BufferedReader(new InputStreamReader(
            Version.class.getResourceAsStream("copyright.txt")));
            StringBuffer buf = new StringBuffer();
            for (String nextLine = br.readLine(); nextLine != null; nextLine = br.readLine()) {
                buf.append(nextLine);
                buf.append(SimEntity.NL);
            }
            SIMKIT_COPYRIGHT = buf.toString();
            br.close();
            
            br = new BufferedReader(new InputStreamReader(
            Version.class.getResourceAsStream("gnu.txt") ) );
            buf = new StringBuffer();
            for (String nextLine = br.readLine(); nextLine != null; nextLine = br.readLine()) {
                buf.append(nextLine);
                buf.append(SimEntity.NL);
            }
            SIMKIT_MESSAGE = buf.toString();
            br.close();
        } catch (IOException e) {throw(new RuntimeException(e));}
        
        String[] ver = getVersion().split("[[a-zA-Z]\\.]");
        if (ver.length < 3) {
            throw new RuntimeException("Bad Version: " + getVersion() + 
                " token length = " + ver.length);
        }
        VERSION_NUMBER = Integer.parseInt(ver[0]);
        SUBVERSION_NUMBER = Integer.parseInt(ver[1]);
        SUBSUBVERSION_NUMBER = Integer.parseInt(ver[2]);
    }
   
    public static int getVersionNumber() { return VERSION_NUMBER; }
    
    public static int getSubVersionNumber() { return SUBVERSION_NUMBER; }
    
    public static int getSubSubVersionNumber() { return SUBSUBVERSION_NUMBER; }
    
    public static boolean isAtLeastVersion(String otherVersion) {
        String[] split = otherVersion.split("[[a-zA-Z]\\.]");
        if (split.length < 3) {
            throw new IllegalArgumentException("Not legitimate version string: " +
                otherVersion);
        }
        if (Integer.parseInt(split[0]) > Version.getVersionNumber()) { return false; }
        else if (Integer.parseInt(split[0]) < Version.getVersionNumber()) { return true; }
        else if ( Integer.parseInt(split[1]) > Version.getSubVersionNumber()) {
            return false;
        }
        else if ( Integer.parseInt(split[1]) < Version.getSubVersionNumber()) {
            return true;
        }
        else if ( Integer.parseInt(split[2]) > Version.getSubSubVersionNumber()) {
            return false;
        }
        return true;
    }
    
/**
* Cause the program to exit, closing the information box.
**/ 
    public void exit() { System.exit(0); }
    
/**
* Create and display the information window. The dialog box will
* close when the user selects "OK"
**/
    public Version() {
        JWindow frame = new JWindow();
        
        URL logo = Version.class.getResource("SimkitLogo.png");
        ImageIcon icon = new ImageIcon(logo);
        JLabel label = new JLabel(icon);
        label.setBackground(Color.cyan);
        label.setOpaque(true);
        label.setBorder(BorderFactory.createEtchedBorder());
        
        JTextArea text = new JTextArea();
        text.setText("Simkit version " + getVersion() + SimEntity.NL +
            SimEntity.NL + NOTE + SimEntity.NL + SimEntity.NL + 
            getCopyright() + getMessage());
        
        JPanel panel = new JPanel();
        JButton button = new JButton(new GenericAction(this, "exit"));
        button.setText("Close");
        panel.add(button);
        panel.setBackground(Color.white);
        
        frame.getContentPane().add(label, BorderLayout.NORTH);
        frame.getContentPane().add(text, BorderLayout.CENTER);
        frame.getContentPane().add(panel, BorderLayout.SOUTH);
        frame.pack();
        
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation((int) (0.5 *(screen.getWidth() - frame.getWidth())) ,
        (int) (0.5 * (screen.getHeight() - frame.getHeight())));
        frame.setVisible(true);
    }
    
/**
* Return the copyright information.
**/
    public static final String getCopyright() {
        return SIMKIT_COPYRIGHT;
    }
    
/**
* Return the GNU license information.
**/
    public static final String getMessage() {
        return SIMKIT_MESSAGE;
    }
    
/**
* Return the version information.
**/
    public static final String getVersion() {return SIMKIT_VERSION;}
    
/**
* Create and display the information window. The dialog box will
* close when the user selects "OK"
**/
    public static void main(String[] args) throws Throwable {
        new Version();
    }
}
