package simkit.viskit;

import simkit.viskit.model.vStateVariable;
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

public class VariablesPanel extends ViskitTablePanel
{
  private String[] mytitles = {"name","type","comment"};

  private String plusToolTip = "Add a state variable";
  private String minusToolTip = "Removed the selected state variable";

  VariablesPanel(int wid, int height)
  {
    super(wid,height);            // separate constructor from initialization
    init();
  }

  public String[] getColumnTitles()
  {
    return mytitles;
  }

  public String[] getFields(Object o)
  {
    String[] sa = new String[3];
    sa[0] = ((vStateVariable)o).getName();
    sa[1] = ((vStateVariable)o).getType();
    sa[2] = (String)((vStateVariable)o).getComment();
    return sa;
  }

  public Object newRowObject()
  {
    vStateVariable ea = new vStateVariable("name","int","comment");
    return ea;
  }

  public int getNumVisibleRows()
  {
    return 3;  // not used if we init super with a height
  }

  // Custom tooltips
  protected String getMinusToolTip()
  {
    return minusToolTip;
  }

  // Protected methods
  protected String getPlusToolTip()
  {
    return plusToolTip;
  }
}