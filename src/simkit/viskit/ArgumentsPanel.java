package simkit.viskit;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: mike
 * Date: Mar 15, 2004
 * Time: 2:35:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class ArgumentsPanel extends JPanel implements ActionListener
{
  JComboBox typeCB;
  myTableModel mod;
  JTable tab;
  JButton minusButt;

  public ArgumentsPanel()
  {
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    tab = new JTable(mod = new myTableModel());
    typeCB = new JComboBox();
    typeCB.addItem("int");
    typeCB.addItem("float");
    typeCB.addItem("double");
    typeCB.addItem("boolean");
    typeCB.addItem("String");
    typeCB.addItem("other java object");

    TableColumn tCol = tab.getColumnModel().getColumn(2);  // type column
    tCol.setCellEditor(new DefaultCellEditor(typeCB));

    DefaultTableCellRenderer rend = new DefaultTableCellRenderer();
    rend.setToolTipText("Click for argument type");
    tCol.setCellRenderer(rend);

    initColumnSizes(tab);

    tab.setPreferredScrollableViewportSize(new Dimension(300, 85));
    JScrollPane jsp = new JScrollPane(tab);
    add(jsp);


    add(Box.createVerticalStrut(5));

    JPanel buttPan = new JPanel();
    buttPan.setLayout(new BoxLayout(buttPan, BoxLayout.X_AXIS));
    buttPan.add(Box.createHorizontalGlue());
    JButton plusButt = new JButton(new ImageIcon(ClassLoader.getSystemResource("simkit/viskit/images/plus.png")));
    Dimension d = plusButt.getPreferredSize();

    plusButt.setFont(plusButt.getFont().deriveFont(plusButt.getFont().getSize2D() + 5f));
    plusButt.setPreferredSize(d);plusButt.setMinimumSize(d);plusButt.setMaximumSize(d);
    plusButt.setActionCommand("p");
    buttPan.add(plusButt);
    minusButt = new JButton(new ImageIcon(ClassLoader.getSystemResource("simkit/viskit/images/minus.png")));
    minusButt.setFont(plusButt.getFont()); //.deriveFont(plusButt.getFont().getSize2D()+5f));
    minusButt.setPreferredSize(d);minusButt.setMinimumSize(d);minusButt.setMaximumSize(d);

    minusButt.setActionCommand("m");
    minusButt.setEnabled(false);
    buttPan.add(minusButt);
    buttPan.add(Box.createHorizontalGlue());
    add(buttPan);

    plusButt.addActionListener(this);
    minusButt.addActionListener(this);

    tab.getSelectionModel().addListSelectionListener(new myListSelectionListener());
  }

  public void actionPerformed(ActionEvent event)
  {
    if(event.getActionCommand() == "p") {
      mod.insertRow();
    }
    else {
      mod.removeRows(tab.getSelectedRows());
    }
  }

  /*
   * This method picks good column sizes.
   * If all column heads are wider than the column's cells'
   * contents, then you can just use column.sizeWidthToFit().
   20,40,90,40,140
   */
  private int[] stColWidths = {20, 40, 90, 40, 140};

  private void initColumnSizes(JTable table)
  {

    //myTableModel model = (myTableModel) table.getModel();
    TableColumn column = null;
    //Component comp = null;
    //int headerWidth = 0;
    //int cellWidth = 0;

    //TableCellRenderer headerRenderer =
    //    table.getTableHeader().getDefaultRenderer();

    for (int i = 0; i < 5; i++) {
      column = table.getColumnModel().getColumn(i);
/*
          comp = headerRenderer.getTableCellRendererComponent(
                               null, column.getHeaderValue(),
                               false, false, 0, 0);
          headerWidth = comp.getPreferredSize().width;

          comp = table.getDefaultRenderer(model.getColumnClass(i)).
                           getTableCellRendererComponent(
                               table, longValues[i],
                               false, false, 0, i);
          cellWidth = comp.getPreferredSize().width;

          column.setPreferredWidth(Math.max(headerWidth, cellWidth));
*/
      column.setPreferredWidth(stColWidths[i]);
    }
  }

  class Argument
  {
    public String nm;
    public String typ;
    public Boolean array;
    public String coment;
    Argument(String nm,String typ,Boolean array,String coment)
    {
      this.nm = nm;
      this.typ = typ;
      this.array = array;
      this.coment = coment;
    }
  }
  String[] columnNames = {" ", "name", "type", "array", "comment"};

  ArrayList initArrLis;
  Object[] longValues = {"8", "Longest",
                         "java.lang.String",
                         Boolean.TRUE, "A comment"}; // field about this long"};

  class myTableModel extends AbstractTableModel
  {
    myTableModel()
    {
      initArrLis = new ArrayList(5);
      initArrLis.add(new Argument("arg1", "int", new Boolean(false), "first arg comment"));
      initArrLis.add(new Argument("arg2", "int", new Boolean(false), "2nd arg comment here"));
      initArrLis.add(new Argument("arg3", "int", new Boolean(false), "3rd arg comment here"));
      initArrLis.add(new Argument("arg4", "java.lang.String", new Boolean(true), "4th arg: string array"));
      initArrLis.add(new Argument("arg5", "boolean", new Boolean(false), "5th arg"));
    }
    public String getColumnName(int i)
    {
      return columnNames[i];
    }

    public int getColumnCount()
    {
      return 5;
    }

    public int getRowCount()
    {
      return initArrLis.size();
    }

    public Object getValueAt(int row, int col)
    {
      Argument arg = (Argument)initArrLis.get(row);
      switch(col) {
        case 0:
          return ""+(row+1);
        case 1:
          return arg.nm;
        case 2:
          return arg.typ;
        case 3:
          return arg.array;
        case 4:
          return arg.coment;
        default:
          System.err.println("Bad row number");
      }
      return null;
    }

    public void setValueAt(Object o, int row, int col)
    {
      Argument arg = (Argument)initArrLis.get(row);
      switch(col) {
        //case 0:
        //         shouldn't be editting
        case 1:
          arg.nm = (String)o;
          break;
        case 2:
          arg.typ = (String)o;
          break;
        case 3:
          arg.array = (Boolean)o;
          break;
        case 4:
          arg.coment = (String)o;
          break;
        default:
          System.err.println("Bad row number");
      }

      this.fireTableCellUpdated(row, col);
    }

    public boolean isCellEditable(int row, int col)
    {
      if (col == 0) {
        return false;
      }
      else {
        return true;
      }
    }

    public Class getColumnClass(int c)
    {
      return getValueAt(0, c).getClass();
    }

    public void insertRow()
    {
      int count = initArrLis.size();
      initArrLis.add(new Argument("unnamed","int",new Boolean(false),""));
      this.fireTableRowsInserted(count,count);
    }

    public void removeRows(int[] selectedRows)
    {
      if(selectedRows.length > 0) {
        for(int i=0;i<selectedRows.length;i++) {
          initArrLis.remove(selectedRows[i]-i);  // ArrayList shifts with each delete
        }
      }
      this.fireTableDataChanged();
    }
  }

  class myListSelectionListener implements ListSelectionListener
  {
    public void valueChanged(ListSelectionEvent event)
    {
      if(!event.getValueIsAdjusting()) {
        minusButt.setEnabled(tab.getSelectedRowCount() > 0);
      }
    }
  }
}
