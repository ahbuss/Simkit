package simkit.data;

import java.awt.*;
import java.util.*;

/**
* @deprecated Use simkit.stat.TablePanel instead.
**/
public class   TablePanel
       extends Panel
{   
   private Vector       labels,
                        values,
                        valueBoxes;
   private GridLayout   layout;
   private Font         labelFont,
                        valueFont;
   private FontMetrics  labelMetrics,
                        valueMetrics;
   private int          labelWidth,
                        valueWidth,
                        maxWidth,
                        winWidth,
                        winHeight;
   
   public TablePanel() {
//      super(title);
      labels     = new Vector();
      values     = new Vector();
      valueBoxes = new Vector();
      layout     = new GridLayout(0,2,4, 2);
      this.setLayout(layout);
      labelFont  = new Font("Helvetica", Font.BOLD, 10);
      valueFont  = new Font("Courier", Font.PLAIN, 12);
      valueWidth = 15;
      maxWidth   = 10;
   }
   
   
   public void addItem( String s ) {
      Label     newLabel = new Label(s, Label.RIGHT);
      TextField newField = new TextField(valueWidth);
      
      if ( labelMetrics == null ) {
         labelMetrics = newLabel.getFontMetrics(labelFont);
         valueMetrics = newField.getFontMetrics(valueFont);
      }
      
      if ( labelMetrics.stringWidth(s) + 5 > maxWidth ) {
         maxWidth = labelMetrics.stringWidth(s) + 5;
      }
      newLabel.setFont(labelFont);
      newField.setFont(valueFont);
      newField.setEditable(false);
      
      labels.addElement( newLabel );
      valueBoxes.addElement( newField );

   }
   
   public void init() {
      for ( int i = 0; i < labels.size(); i++ ) {
         this.add((Label)(labels.elementAt(i)));
         this.add((TextField)(valueBoxes.elementAt(i)));
      }
      
      winWidth  = maxWidth * 2 + 5;
      winHeight = (7 + labelMetrics.getHeight() ) * labels.size();
   }
      
   public void updateValues( double[] vals ) {
      if ( vals.length != valueBoxes.size() ) {
         System.err.println("Wrong number of values passed to TableWindow");
         return;
      }
      if ( valueBoxes.isEmpty() ) {
         System.err.println("empty table");
         return;
      }
      
      for ( int i = 0; i < vals.length; i++) {
         ((TextField)(valueBoxes.elementAt(i))).setText(
                              Double.toString(vals[i]));
      }
      (this.getParent()).repaint();
//      try {
//         Thread.sleep(5);
//      } catch (Exception e) {}
   }
   
/*
	public boolean handleEvent(Event evt) {             //AB
		return super.dispatchEvent(evt);
	}
*/
}
