// FILE: ChartWindow.java

package simkit.data;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
* @deprecated Use simkit.stat.DataWindow instead.
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
	
	public void show() {
	   super.show();
	}
}
