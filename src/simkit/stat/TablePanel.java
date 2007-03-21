package simkit.stat;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.util.Vector;

/**
* A Panel to contain labels and data fields. <p/>
* Usage:<ul>
* <li>Construct the TablePanel.</li>
* <li>Add it to an AWT Container.</li>
* <li>Call addItem for each data field</li>
* <li>Call init</li>
* <li>Display the parent Container</li>
* <li>Call updateValues to change the data values displayed.</li>
*
* @version $Id$
*/
public class   TablePanel
       extends Panel
{   
   private Vector<Label>       labels;
   private Vector       values;
   private Vector<TextField>  valueBoxes;
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
   
/**
* Constructs a new TablePanel.
**/
   public TablePanel() {
//      super(title);
      labels     = new Vector<Label>();
      values     = new Vector();
      valueBoxes = new Vector<TextField>();
      layout     = new GridLayout(0,2,4, 2);
      this.setLayout(layout);
      labelFont  = new Font("Helvetica", Font.BOLD, 10);
      valueFont  = new Font("Courier", Font.PLAIN, 12);
      valueWidth = 15;
      maxWidth   = 10;
   }
   
   
/**
* Adds a label/data field pair to this TablePanel.
* @param s The contents of the label.
**/
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
   
/**
* Initializes this TablePanel. Call after all calls to addItem.
**/
   public void init() {
      for ( int i = 0; i < labels.size(); i++ ) {
         this.add((Label)(labels.elementAt(i)));
         this.add((TextField)(valueBoxes.elementAt(i)));
      }
      
      winWidth  = maxWidth * 2 + 5;
      winHeight = (7 + labelMetrics.getHeight() ) * labels.size();
   }
      
/**
* Updates the contents of the data fields with new values.
* Prints a warning and returns if the given array does not contain the
* same number of elements as there are fields.
* @param vals An array containing the values to put in the boxes.
**/
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
