package simkit.viskit;

import simkit.viskit.model.vEdgeParameter;
import simkit.viskit.model.EventArgument;

import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.DefaultTableCellRenderer;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * OPNAV N81 - NPS World Class Modeling (WCM) 2004 Projects
 * MOVES Institute
 * Naval Postgraduate School, Monterey CA
 * www.nps.navy.mil
 * By:   Mike Bailey
 * Date: Apr 8, 2004
 * Time: 8:49:21 AM
 */

public class EdgeParametersPanel extends ViskitTablePanel
{
  private String[] mytitles = {"event argument","value"};

  EdgeParametersPanel(int wid)
  {
    super(wid);            // separate constructor from initialization
    init(false);

    // Set the first column background to be the color of the panel (indicating r/o)
    TableColumn tc = tab.getColumnModel().getColumn(0);
    tc.setCellRenderer(new DifferentTableColumnBackgroundRenderer(getBackground()));

    super.addDoubleClickedListener(new myEditListener());
  }

  public String[] getColumnTitles()
  {
    return mytitles;
  }
  public String[] xgetFields(Object o)
  {  return null;
  }

  public String[] getFields(Object o, int rowNum)
  {
    String[] sa = new String[2];
    EventArgument ea = (EventArgument)argList.get(rowNum);
    sa[0] = ea.getName()+" ("+ea.getType()+")";
    sa[1] = ((vEdgeParameter)o).getValue();
    return sa;
  }

  public Object newRowObject()
  {
    vEdgeParameter ea = new vEdgeParameter("value"); //,"type");
    return ea;
  }

  public int getNumVisibleRows()
  {
    return 3;
  }

  ArrayList argList;
  public void setArgumentList(ArrayList lis)
  {
    argList = lis;
  }

  class DifferentTableColumnBackgroundRenderer extends DefaultTableCellRenderer
  {
    Color c;
    public DifferentTableColumnBackgroundRenderer(Color c)
    {
      super();
      setBackground(c);
    }
   }

  private ActionListener alis;

  /**
    * Install external handler for row edit requests.  The row object can be retrieved by
    * ActionEvent.getSource().
    * @param edLis
    */
   public void addDoubleClickedListener(ActionListener edLis)
   //--------------------------------------------------------
   {
     alis = edLis;
   }

  class myEditListener implements ActionListener
  {
    public void actionPerformed(ActionEvent e)
    {
      if (alis != null) {
        Object o = e.getSource();
        int row = findObjectRow(o);
        EventArgument ea = (EventArgument)argList.get(row);
        ((vEdgeParameter)o).bogus = ea.getType();
        ActionEvent ae = new ActionEvent(o,0,"");
        alis.actionPerformed(ae);
      }

    }
  }

}