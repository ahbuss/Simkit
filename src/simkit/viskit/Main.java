package simkit.viskit;

import javax.swing.*;
import java.util.Enumeration;
import java.awt.*;

/**
 * OPNAV N81 - NPS World Class Modeling (WCM) 2004 Projects
 * MOVES Institute
 * Naval Postgraduate School, Monterey CA
 * www.nps.navy.mil
 * By:   Mike Bailey
 * Date: Mar 3, 2004
 * Time: 11:29:05 AM
 */

/**
 * This is a simple class which does the MVC initting for Viskit.  Since the JFrame is
 * really "just" a view into the data, it is not really the lord of all the rings.  That's
 * what this class is.
 */
public class Main
{
  public static void main(String[] args)
  {


    try {
      //String GTK = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
      String GTK = "javax.swing.plaf.metal.MetalLookAndFeel";
      UIManager.setLookAndFeel(GTK);
      UIManager.installLookAndFeel("GTK",GTK);
      UIDefaults def = UIManager.getDefaults();
      def.put("Tree.font", new Font("Verdana",Font.PLAIN,12));
    }
    catch (Exception e) {
      System.err.println("Could not install GTK");
    }

    Controller controller = new Controller();
    Model model = new Model();
    EventGraphViewFrame view = new EventGraphViewFrame(model,controller);
    controller.setModel(model);   // registers cntl as model listener
    controller.setView(view);

    view.setVisible(true);
  }
}
