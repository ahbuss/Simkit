package simkit.viskit;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.lang.reflect.Method;

/**
 * OPNAV N81 - NPS World Class Modeling (WCM)  2004 Projects
 * MOVES Institute
 * Naval Postgraduate School, Monterey, CA
 * www.nps.edu
 * By:   Mike Bailey
 * Date: Apr 13, 2004
 * Time: 9:19:25 AM
 */

/**
 * Base on code posted by "Stanislav Lapitsky, ghost_s@mail.ru, posted on the Sun developer forum.  Feb 9, 2004.
 */

class Splash2 extends JFrame
{
  Robot robot;
  BufferedImage screenImg;
  Rectangle screenRect;
  MyPanel contentPanel = new MyPanel();
  boolean userActivate = false;

  public Splash2()
  {
    super();
    setUndecorated(true);

    ImageIcon icon = new ImageIcon(ClassLoader.getSystemResource("simkit/viskit/images/ViskitSplash.png"));
    JLabel label = new JLabel(icon);
    label.setOpaque(false);
    contentPanel.add(label,BorderLayout.CENTER);

contentPanel.setPreferredSize(label.getPreferredSize());

    createScreenImage();
    setContentPane(contentPanel);
    this.pack();

    this.addComponentListener(new ComponentAdapter()
    {
       public void componentMoved(ComponentEvent e)
      {
        resetUnderImg();
        repaint();
      }

      public void componentResized(ComponentEvent e)
      {
        resetUnderImg();
        repaint();
      }
    });

    this.addWindowListener(new WindowAdapter()
    {
      public void windowActivated(WindowEvent e)
      {
        if (userActivate) {
          userActivate = false;
          Splash2.this.setVisible(false);
          createScreenImage();
          resetUnderImg();
          Splash2.this.setVisible(true);
        }
        else {
          userActivate = true;
        }
      }
    });
  }

  protected void createScreenImage()
  {
    try {
      if (robot == null)
        robot = new Robot();
    }
    catch (AWTException ex) {
      ex.printStackTrace();
    }

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    screenRect = new Rectangle(0, 0, screenSize.width, screenSize.height);
    screenImg = robot.createScreenCapture(screenRect);
  }

  public void resetUnderImg()
  {
    if (robot != null && screenImg != null) {
      Rectangle frameRect = getBounds();
      int x = frameRect.x; // + 4;
      contentPanel.paintX = 0;
      contentPanel.paintY = 0;
      if (x < 0) {
        contentPanel.paintX = -x;
        x = 0;
      }
      int y = frameRect.y; // + 23;
      if (y < 0) {
        contentPanel.paintY = -y;
        y = 0;
      }
      int w = frameRect.width; // - 10;
      if (x + w > screenImg.getWidth())
        w = screenImg.getWidth() - x;
      int h = frameRect.height; // - 23 - 5;
      if (y + h > screenImg.getHeight())
        h = screenImg.getHeight() - y;

      contentPanel.underFrameImg = screenImg.getSubimage(x, y, w, h);
    }
  }
  public static void main(String[] args)
  {
    Splash2 spl = new Splash2();
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

class MyPanel extends JPanel
{
  BufferedImage underFrameImg;
  int paintX = 0;
  int paintY = 0;

  public MyPanel()
  {
    super();
    setOpaque(true);
  }

  public void paint(Graphics g)
  {
    super.paint(g);
  }

  protected void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    g.drawImage(underFrameImg, paintX, paintY, null);
  }
}
