// FILE: ChartWindow.java

package simkit.stat;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
* A simple class for displaying a window. The window created is 
* at position (5,5) and is 275 by 300 pixels. When the
* window is closed, the program will exit.
* <p/>{@link simkit.util.CloseableDataWindow} is a more flexible version of this Class.
* @version $Id$
**/
public class   DataWindow
       extends Frame
{
   protected static int          winPosX      = 5;
   protected static int          winPosY      = 5;
   protected static final int    WIN_X_OFFSET = 15;
   protected static final int    WIN_Y_OFFSET = 15;
   protected BorderLayout layout;      
   
   private int myx, myy, myw, myh;
   
/**
* Creates a new DataWindow. Located at position (5,5)
* that is 275 by 300 pixels.
* @param title The title for the window.
**/
   public DataWindow(String title) {
      super(title);
      myx = winPosX;
      myy = winPosY;
      myw = 275;
      myh = 300;
      layout = new BorderLayout();
      this.setLayout(layout);
      this.setBounds(myx, myy, myw, myh);     //AB
      winPosX += WIN_X_OFFSET;
      winPosY += WIN_Y_OFFSET;

      this.setVisible(true);

      this.addWindowListener(
        new WindowAdapter() {
          public void windowClosing(WindowEvent e) {
            dispose();
            setVisible(false);
            System.exit(0);
          }
        }
      );
   }
   
/*
   public boolean handleEvent(Event evt) {
	   if ( evt.id == Event.WINDOW_DESTROY) {
         this.dispose();            // AB
         this.setVisible(false);    // AB
         System.exit(0);            // AB
		}
		return super.handleEvent(evt);
	}
*/
	
/**
* Simply calls super.show().
**/
	public void show() {
	   super.show();
	}
}
