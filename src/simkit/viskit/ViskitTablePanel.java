package simkit.viskit;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Vector;

/**
 * OPNAV N81 - NPS World Class Modeling (WCM) 2004 Projects
 * MOVES Institute
 * Naval Postgraduate School, Monterey CA
 * www.nps.navy.mil
 * By:   Mike Bailey
 * Date: Apr 8, 2004
 * Time: 8:49:21 AM
 */

public abstract class ViskitTablePanel extends JPanel
/***************************************************/
{
  private JTable tab;
  private JButton plusButt, minusButt, edButt;
  private ThisTableModel mod;

  private int defaultWidth=0, defaultHeight=0;
  private ArrayList shadow = new ArrayList();

  private ActionListener myEditLis,myPlusLis,myMinusLis;

  private String plusToolTip  = "Add a row to this table";
  private String minusToolTip = "Delete the selected row from this table;";

  public ViskitTablePanel(int defaultWidth)
  //=======================================
  {
    this.defaultWidth = defaultWidth;
  }

  public ViskitTablePanel(int defaultWidth, int defaultHeight)
  //==========================================================
  {
    this.defaultWidth = defaultWidth;
    this.defaultHeight = defaultHeight;
  }

  public void init()
  //----------------
  {
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    // edit instructions line
     JPanel p = new JPanel();
     p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
     p.add(Box.createHorizontalGlue());
      JLabel instructions = new JLabel("Double click a row to ");
      int bigSz = instructions.getFont().getSize();
      instructions.setFont(getFont().deriveFont(Font.ITALIC, (float) (bigSz - 2)));
     p.add(instructions);
      edButt = new JButton("edit.");
      edButt.setFont(instructions.getFont()); //.deriveFont(Font.ITALIC, (float) (bigSz - 2)));
      edButt.setBorder(null);
      edButt.setEnabled(false);
      edButt.setActionCommand("e");
     p.add(edButt);
     p.add(Box.createHorizontalGlue());
    add(p);

    // the table
     tab = new ThisToolTipTable(mod = new ThisTableModel(getColumnTitles()));
     adjustColumnWidths();
     if(defaultHeight == 0) {
       defaultHeight = tab.getRowHeight();
       defaultHeight *= getNumVisibleRows()+1 + 1; // want 3 rows + header and one more for extra
     }
     tab.setPreferredScrollableViewportSize(new Dimension(defaultWidth, defaultHeight));
     JScrollPane jsp = new JScrollPane(tab);
    add(jsp);
    add(Box.createVerticalStrut(5));

    // plus, minus and edit buttons
     JPanel buttPan = new JPanel();
     buttPan.setLayout(new BoxLayout(buttPan, BoxLayout.X_AXIS));
     buttPan.add(Box.createHorizontalGlue());
      // add button
      plusButt = new JButton(new ImageIcon(ClassLoader.getSystemResource("simkit/viskit/images/plus.png")));
      plusButt.setBorder(null);
      plusButt.setText(null);
      plusButt.setToolTipText(getPlusToolTip());
      Dimension d = plusButt.getPreferredSize();
      plusButt.setMinimumSize(d);
      plusButt.setMaximumSize(d);
      plusButt.setActionCommand("p");
     buttPan.add(plusButt);
      // delete button
      minusButt = new JButton(new ImageIcon(ClassLoader.getSystemResource("simkit/viskit/images/minus.png")));
      minusButt.setDisabledIcon(new ImageIcon(ClassLoader.getSystemResource("simkit/viskit/images/minusGrey.png")));
      minusButt.setBorder(null);
      minusButt.setText(null);
      minusButt.setToolTipText(getMinusToolTip());
      d = minusButt.getPreferredSize();
      minusButt.setMinimumSize(d);
      minusButt.setMaximumSize(d);
      minusButt.setActionCommand("m");
      minusButt.setEnabled(false);
     buttPan.add(minusButt);
     buttPan.add(Box.createHorizontalGlue());
    add(buttPan);
    add(Box.createVerticalStrut(5));

    // don't let the whole panel get squeezed smaller that what we start out with
    d = getPreferredSize();
    setMinimumSize(d);

    // install local add, delete and edit handlers
    ActionListener lis = new MyAddDelEditHandler();
    plusButt .addActionListener(lis);
    minusButt.addActionListener(lis);
    edButt   .addActionListener(lis);

    // install the handler to enable delete and edit buttons only on row-select
    tab.getSelectionModel().addListSelectionListener(new ListSelectionListener()
    {
      public void valueChanged(ListSelectionEvent event)
      {
        if (!event.getValueIsAdjusting()) {
          boolean yn = tab.getSelectedRowCount() > 0;
          minusButt.setEnabled(yn);
          edButt.setEnabled(yn);
        }
      }
    });

    // install the double-clicked handler to duplicate action of edit button
    tab.addMouseListener(new MouseAdapter()
    {
      public void mouseClicked(MouseEvent event)
      {
        if (event.getClickCount() == 2)
          doEdit();
      }
    });
  }

  // public methods for working with this class

  /**
   * Install external handler for row edit requests.  The row object can be retrieved by
   * ActionEvent.getSource().
   * @param edLis
   */
  public void addDoubleClickedListener(ActionListener edLis)
  //--------------------------------------------------------
  {
    myEditLis = edLis;
  }

  /**
   * Install external handler for row-add requests.
   * @param addLis
   */
  public void addPlusListener(ActionListener addLis)
  //------------------------------------------------
  {
    myPlusLis = addLis;
  }

  /**
   * Install external handler for row-delete requests.  The row object can be retrieved by
   * ActionEvent.getSource().
   * @param delLis
   */
  public void addMinusListener(ActionListener delLis)
  {
    myMinusLis = delLis;
  }

  /**
   * Add a row defined by the argument to the end of the table.  The table data will be retrieve through
   * the abstract method, getFields(o).
   * @param o
   */
  public void addRow(Object o)
  //--------------------------
  {
    shadow.add(o);
    Vector rowData = new Vector();
    String[] fields = getFields(o);
    rowData.addAll(Arrays.asList(fields));
    mod.addRow(rowData);

    adjustColumnWidths();
  }

  /**
   * Add a row to the end of the table.  The row object will be build through the abstract method, newRowObject().
   */
  public void addRow()
  //-----------------
  {
    addRow(newRowObject());
  }

  /**
   * Remove the row representing the argument from the table.
   * @param o
   */
  public void removeRow(Object o)
  //-----------------------------
  {
    removeRow(findObjectRow(o));
  }

  /**
   * Remove the row identified by the passed zero-based row number from the table.
   * @param r
   */
  public void removeRow(int r)
  //--------------------------
  {
    shadow.remove(r);
    mod.removeRow(r);
  }

  /**
   * Initialize the table with the passed data.
   * @param data
   */
  public void setData(java.util.List data)
  //--------------------------------------
  {
    shadow.clear();
    mod.setRowCount(0);

    if(data != null) {
      for (Iterator itr = data.iterator(); itr.hasNext();) {
        Object o = itr.next();
        putARow(o);
      }
    }
    adjustColumnWidths();
  }

  /**
   * Get all the current table data in the form of an array of row objects.
   * @return ArrayList of row objects
   */
  public ArrayList getData()
  //------------------------
  {
   // todo jmb get the cloning working
     return (ArrayList)shadow.clone();
    //return shadow;
  }

  /**
   * Update the table row (typ. after editing) represented the passed rowObject.
   * @param rowObject
   */
  public void updateRow(Object rowObject)
  //-------------------------------------
  {
    int row = findObjectRow(rowObject);

    String[] fields = getFields(rowObject);
    for (int i = 0; i < mod.getColumnCount(); i++) {
      mod.setValueAt(fields[i], row, i);
    }
    adjustColumnWidths();
  }

  // Protected methods
  protected String getPlusToolTip()
  //-------------------------------
  {
    return plusToolTip;
  }
  protected String getMinusToolTip()
  //--------------------------------
  {
    return minusToolTip;
  }

  // Abstract methods
  /**
   * Return the column titles.  This defines the number of columns in the display.
   * @return String array of titles
   */
  abstract public String[] getColumnTitles();
  //-----------------------------------------

  /**
   * Return the fields to be displayed in the table.
   * @param o row object
   * @return  String array of fields
   */
  abstract public String[] getFields(Object o);
  //-------------------------------------------

  /**
   * Build a new row object
   * @return Row object
   */
  abstract public Object newRowObject();
  //--------------------------------------

  /**
   * Specify how many rows the table should display at a minimum
   * @return number of rows
   */
  abstract public int getNumVisibleRows();
  //--------------------------------------

  // private methods

  /**
   * If a double-clicked listener has been installed, message it with the row object to be edited.
   */
  private void doEdit()
  //-------------------
  {
    if (myEditLis != null) {
      Object o = shadow.get(tab.getSelectedRow());
      ActionEvent ae = new ActionEvent(o,0,"");
      myEditLis.actionPerformed(ae);
    }
  }

  /**
   * Given a row object, find its row number.
   * @param o row object
   * @return row index
   */
  private int findObjectRow(Object o)
  //---------------------------------
  {
    int row = 0;

    // the most probable case
    if (o == shadow.get(tab.getSelectedRow()))
      row = tab.getSelectedRow();

    // else look at all
    else {
      int r = 0;
      for (r = 0; r < shadow.size(); r++) {
        if (o == shadow.get(r))
          row = r;
        break;
      }
      if (r >= mod.getRowCount())
      //assert false: "Bad table processing, ViskitTablePanel.updateRow)
        System.err.println("Bad table processing, ViskitTablePanel.updateRow");  // will die here
    }
    return row;
  }

  /**
   * Set table column widths to the widest element, including header.  Let last column float.
   */
  private void adjustColumnWidths()
  {
    String[] titles = getColumnTitles();
    FontMetrics fm = tab.getFontMetrics(tab.getFont());

    for (int c = 0; c < tab.getColumnCount(); c++) {
      TableColumn col = tab.getColumnModel().getColumn(c);
      int maxWidth = 0;
      int w = fm.stringWidth(titles[c]);
      col.setMinWidth(w);
      if (w > maxWidth) maxWidth = w;
      for (int r = 0; r < tab.getRowCount(); r++) {
        String s = (String) mod.getValueAt(r, c);
        w = fm.stringWidth(s);
        if (w > maxWidth)
          maxWidth = w;
      }
      if (c != tab.getColumnCount() - 1) {    // leave the last one alone
        // its important to set maxwidth before preferred with because the latter
        // gets clamped by the former.
        col.setMaxWidth(maxWidth + 5);       // why the fudge?
        col.setPreferredWidth(maxWidth + 5); // why the fudge?
      }
    }
    tab.invalidate();
  }

  /**
   * Build a table row based on the passed row object.
   * @param o
   */
  private void putARow(Object o)
  {
    shadow.add(o);
    Vector rowData = new Vector();
    String[] fields = getFields(o);
    rowData.addAll(Arrays.asList(fields));
    mod.addRow(rowData);
  }

  /**
   * The local listener for plus, minus and edit clicks
   */
  class MyAddDelEditHandler implements ActionListener
  /*************************************************/
  {
    public void actionPerformed(ActionEvent event)
    {
      if (event.getActionCommand() == "p") {
        if (myPlusLis != null)
          myPlusLis.actionPerformed(event);
        else
          addRow();
      }
      else if (event.getActionCommand() == "m") {
        if (myMinusLis != null) {
          event.setSource(shadow.get(tab.getSelectedRow()));
          myMinusLis.actionPerformed(event);
        }
        else
          removeRow(tab.getSelectedRow());
      }
      else {// if(event.getActionCommand() == "e")
        doEdit();
      }
      adjustColumnWidths();
    }
  }

  /**
   * Our table model.  Sub class done only to mark all as read-only.
   */
  class ThisTableModel extends DefaultTableModel
  /********************************************/
  {
    ThisTableModel(String[] columnNames)
    {
      super(columnNames, 0);
    }
    public boolean isCellEditable(int row, int col)
    {
      return false;
    }
  }

  class ThisToolTipTable extends JTable
  {
    ThisToolTipTable(TableModel tm)
    {
      super(tm);
    }
    public String getToolTipText(MouseEvent e)
    {
      String tip = null;
      java.awt.Point p = e.getPoint();
      int rowIndex = rowAtPoint(p);
      int colIndex = columnAtPoint(p);
      int realColumnIndex = convertColumnIndexToModel(colIndex);

      tip = getValueAt(rowIndex, colIndex).toString();  // tool tip is contents (for long contents)
      if(tip == null || tip.length() == 0)
        return null;
      return tip;
    }
  }
}
