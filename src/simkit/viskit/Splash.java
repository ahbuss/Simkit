package simkit.viskit;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Method;

/**
 * OPNAV N81-NPS World-Class-Modeling (WCM) 2004 Projects
 * MOVES Institute
 * Naval Postgraduate School, Monterey, CA
 * User: mike
 * Date: Feb 23, 2004
 * Time: 8:26:50 AM
 */

/**
 * This class will put up a single image as a splash screen in the center of the default screen.
 * Waits for 2 seconds, then loads simkit.viskit.Main, handing off the arguments which the
 * entry point in the class was given.
 */

public class Splash extends JFrame
{
  JLabel label;

  public Splash()
  {
    super();
    this.setUndecorated(true);
    ImageIcon icon = new ImageIcon(ClassLoader.getSystemResource("images/ViskitSplash.png"));
    label = new JLabel(icon);
    this.getContentPane().add(label, BorderLayout.CENTER);
    this.pack();
  }

  public static void main(String[] args)
  {
    Splash spl = new Splash();
    Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
    spl.setLocation((d.width-spl.getWidth())/2,(d.height-spl.getHeight())/2);
    spl.setVisible(true);

    try{Thread.sleep(2000);}catch(Exception e){}  // this is used to give us some min splash viewing
    
    try {
      // Call the main() method of the application using reflection
      Object[] arguments = new Object[]{args};
      Class[] parameterTypes = new Class[]{args.getClass()};

      Class mainClass = Class.forName("simkit.viskit.Main");

      Method mainMethod = mainClass.getMethod("main",parameterTypes);
      mainMethod.invoke(null, arguments);
    }
    catch (Exception e) {
      e.printStackTrace();
    }

    spl.dispose();
  }
}
