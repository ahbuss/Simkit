package simkit.viskit;

import simkit.viskit.model.vEdgeParameter;
import java.util.ArrayList;

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
  private String[] mytitles = {"value","type"};

  EdgeParametersPanel(int wid)
  {
    super(wid);            // separate constructor from initialization
    init();
  }

  public String[] getColumnTitles()
  {
    return mytitles;
  }

  public String[] getFields(Object o)
  {
    String[] sa = new String[3];
    sa[0] = ((vEdgeParameter)o).getValue();
    sa[1] = ((vEdgeParameter)o).getType();
    return sa;
  }

  public Object newRowObject()
  {
    vEdgeParameter ea = new vEdgeParameter("value","type");
    return ea;
  }

  public int getNumVisibleRows()
  {
    return 3;
  }
}