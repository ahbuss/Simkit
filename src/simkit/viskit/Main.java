package simkit.viskit;

import simkit.viskit.model.Model;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.util.Set;
import java.util.Iterator;
import java.util.Map;

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
    String laf = "javax.swing.plaf.metal.MetalLookAndFeel";          //default

    String os = System.getProperty("os.name").toLowerCase();
    if (os.indexOf("windows") != -1)
      laf = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
    else if (os.indexOf("mac") != -1) {
      //laf = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
      //laf = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
      //laf = "apple.laf.AquaLookAndFeel";
      laf = "javax.swing.plaf.metal.MetalLookAndFeel";
      //System.setProperty("apple.awt.brushMetalLook","true");
      System.setProperty("apple.awt.antialiasing", "true");
      //System.setProperty("apple.awt.showGrowBox","true");     // only for aqua
    }

    try {
      UIManager.setLookAndFeel(laf);
      UIDefaults def = UIManager.getDefaults();
      def.put("Tree.font", new Font("Verdana", Font.PLAIN, 12));
    }
    catch (Exception e) {
      System.err.println("Could not enable " + laf);
    }

    Controller controller = new Controller();
    Model model = new Model();
    EventGraphViewFrame view = new EventGraphViewFrame(model, controller);
    controller.setModel(model);   // registers cntl as model listener
    controller.setView(view);

    model.init();

    //setAllFonts(view);
    view.setVisible(true);
  }

  private static void setAllFonts(JFrame f)
  {
    try {

      UIManager.put("ToolTip.background",new ColorUIResource(204,204,255));


      FontUIResource fontPlain12 = new FontUIResource("Verdana", Font.PLAIN, 12);
      FontUIResource fontBold12 = new FontUIResource("Verdana", Font.BOLD, 12);
      FontUIResource fontPlain10 = new FontUIResource("Verdana", Font.PLAIN, 10);

      UIManager.put("InternalFrame.titleFont", fontBold12);

      UIManager.put("ToolTip.font", fontPlain12);

      //UIManager.put("Button.font", fontBold12);
      UIManager.put("CheckBox.font", fontBold12);
      UIManager.put("ComboBox.font", fontPlain12); //fontBold12);
      UIManager.put("DesktopIcon.font", fontBold12);
      UIManager.put("Label.font", fontPlain12); //fontBold12);
      UIManager.put("List.font", fontPlain12); //fontBold12);
      UIManager.put("ProgressBar.font", fontBold12);
      UIManager.put("RadioButton.font", fontBold12);
      UIManager.put("Spinner.font", fontBold12);
      UIManager.put("TabbedPane.font", fontBold12);
      UIManager.put("TitledBorder.font", fontBold12);
      UIManager.put("ToggleButton.font", fontBold12);

      UIManager.put("EditorPane.font", fontPlain12);
      UIManager.put("FormattedTextField.font", fontPlain12);
      UIManager.put("PasswordField.font", fontPlain12);
      UIManager.put("Table.font", fontPlain12);        // no effect
      UIManager.put("TableHeader.font", fontPlain12);  // no effect
      UIManager.put("TextArea.font", fontPlain12);
      UIManager.put("TextField.font", fontPlain12);
      UIManager.put("TextPane.font", fontPlain12);
      UIManager.put("Tree.font", fontPlain12);

      UIManager.put("CheckBoxMenuItem.acceleratorFont", fontPlain10);
      UIManager.put("Menu.acceleratorFont", fontPlain10);
      UIManager.put("MenuItem.acceleratorFont", fontPlain10);
      UIManager.put("RadioButtonMenuItem.acceleratorFont", fontPlain10);

      UIManager.put("CheckBoxMenuItem.font", fontBold12);
      UIManager.put("Menu.font", fontBold12);
      UIManager.put("MenuBar.font", fontBold12);
      UIManager.put("MenuItem.font", fontBold12);
      UIManager.put("PopupMenu.font", fontBold12);
      UIManager.put("RadioButtonMenuItem.font", fontBold12);
      UIManager.put("ToolBar.font", fontBold12);

      SwingUtilities.updateComponentTreeUI(f);
    }
    catch (Exception e) {
      System.err.println("error setting UI fonts " + e.getMessage());
    }

  }
}
