package simkit.viskit;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: mike
 * Date: Mar 15, 2004
 * Time: 2:35:01 PM
 */
public class ParametersPanel extends JPanel implements ActionListener
{
  JComboBox typeCB;
  myTableModel mod;
  JTable tab;
  JButton minusButt;

  public ParametersPanel()
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
  private int[] stColWidths = {20, 110, 80, 195};

  private void initColumnSizes(JTable table)
  {

    //myTableModel model = (myTableModel) table.getModel();
    TableColumn column = null;
    //Component comp = null;
    //int headerWidth = 0;
    //int cellWidth = 0;

    //TableCellRenderer headerRenderer =
    //    table.getTableHeader().getDefaultRenderer();

    for (int i = 0; i < stColWidths.length; i++) {
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

/*
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
*/
  class Parameter
  {
    public String expression;
    public String typ;
    public String coment;
    Parameter(String expression, String typ, String coment)
    {
      this.expression = expression;
      this.typ = typ;
      this.coment = coment;
    }
  }
  String[] columnNames = {" ", "expression", "type", "comment"};

  ArrayList initArrLis;
  Object[] longValues = {"8", "Longest",
                         "java.lang.String",
                         "A comment"}; // field about this long"};

  class myTableModel extends AbstractTableModel
  {
    myTableModel()
    {
      initArrLis = new ArrayList(4);
      initArrLis.add(new Parameter("<html>t<sub>A</sub></html>", "ranVariate", "part interarrival sequence"));
      initArrLis.add(new Parameter("<html>t<sub>S</sub></html>", "ranVariate", "service times sequence"));
      initArrLis.add(new Parameter("<html>t<sub>F</sub></html>", "ranVariate", "machine time-to-failure"));
      initArrLis.add(new Parameter("<html>t<sub>R</sub></html>", "ranVariate", "repair times"));
    }
    public String getColumnName(int i)
    {
      return columnNames[i];
    }

    public int getColumnCount()
    {
      return columnNames.length;
    }

    public int getRowCount()
    {
      return initArrLis.size();
    }

    public Object getValueAt(int row, int col)
    {
      Parameter param = (Parameter)initArrLis.get(row);
      switch(col) {
        case 0:
          return ""+(row+1);
        case 1:
          return param.expression;
        case 2:
          return param.typ;
        case 3:
          return param.coment;
        default:
          System.err.println("Bad row number");
      }
      return null;
    }

    public void setValueAt(Object o, int row, int col)
    {
      Parameter param = (Parameter)initArrLis.get(row);
      switch(col) {
        //case 0:
        //         shouldn't be editting
        case 1:
          param.expression = (String)o;
          break;
        case 2:
          param.typ = (String)o;
          break;
        case 3:
          param.coment = (String)o;
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
      initArrLis.add(new Parameter("expr. here","int",""));
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
