package simkit;

import java.io.*;
import java.util.*;
import javax.swing.*;
import java.net.*;
import java.awt.*;
import simkit.util.*;

public class Version {
    
    private static String SIMKIT_VERSION = "Not Found";
    private static Properties SIMKIT_PROPERTIES;
    private static String SIMKIT_COPYRIGHT = "";
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
    
    public void exit() { System.exit(0); }
    
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
    
    public static final String getCopyright() {
        return SIMKIT_COPYRIGHT;
    }
    
    public static final String getMessage() {
        return SIMKIT_MESSAGE;
    }
    
    public static final String getVersion() {return SIMKIT_VERSION;}
    
    public static void main(String[] args) throws Throwable {
        new Version();
    }
}
