package simkit.viskit;

import simkit.viskit.model.EventLocalVariable;

/**
 * OPNAV N81 - NPS World Class Modeling (WCM) 2004 Projects
 * MOVES Institute
 * Naval Postgraduate School, Monterey CA
 * www.nps.navy.mil
 * By:   Mike Bailey
 * Date: Apr 8, 2004
 * Time: 8:49:21 AM
 */

public class LocalVariablesPanel extends ViskitTablePanel
{
  private String[] mytitles = {"name","type","value"};

  LocalVariablesPanel(int wid)
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
    sa[0] = ((EventLocalVariable)o).getName();
    sa[1] = ((EventLocalVariable)o).getType();
    sa[2] = ((EventLocalVariable)o).getValue();
    return sa;
  }

  public Object newRowObject()
  {
    return new EventLocalVariable("locvar name","int","0");
  }

  public int getNumVisibleRows()
  {
    return 3;
  }
}