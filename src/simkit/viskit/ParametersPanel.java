package simkit.viskit;

import simkit.viskit.model.vParameter;

/**
 * OPNAV N81 - NPS World Class Modeling (WCM) 2004 Projects
 * MOVES Institute
 * Naval Postgraduate School, Monterey CA
 * www.nps.navy.mil
 * By:   Mike Bailey
 * Date: Apr 8, 2004
 * Time: 8:49:21 AM
 */

public class ParametersPanel extends ViskitTablePanel
{
  private String[] mytitles = {"name","type","comment"};

  private String plusToolTip = "Add a simulation parameter";
  private String minusToolTip = "Removed the selected parameter";

  ParametersPanel(int wid)
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
    sa[0] = ((vParameter)o).getName();
    sa[1] = ((vParameter)o).getType();
    sa[2] = ((vParameter)o).getComment();
    return sa;
  }

  public Object newRowObject()
  {
    vParameter ea = new vParameter("name","int","comment");
    return ea;
  }

  public int getNumVisibleRows()
  {
    return 3;
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