package simkit;

import java.io.*;
import java.util.*;
import javax.swing.*;
import java.net.*;
import java.awt.*;
import simkit.util.*;

/**
* This class is used to display version and copyright information about SimKit. Constructing a 
* Version object will cause a dialog box to pop-up containing information about the program.
* @version $Id$
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
    
    static {
        InputStream is = null;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(
            Version.class.getResourceAsStream("version.txt")));
            StringBuffer buf = new StringBuffer();
            for (String nextLine = br.readLine(); nextLine != null; nextLine = br.readLine()) {
                buf.append(nextLine);
                buf.append(SimEntity.NL);
            }
            SIMKIT_VERSION = buf.toString().trim();
            br.close();
            
            br = new BufferedReader(new InputStreamReader(
            Version.class.getResourceAsStream("copyright.txt")));
            buf = new StringBuffer();
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
        } catch (IOException e) {}
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
        getCopyright() + SimEntity.NL + getMessage());
        
        JPanel panel = new JPanel();
        JButton button = new JButton(new GenericAction(this, "exit"));
        button.setText("OK");
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
