package simkit.viskit;

import simkit.viskit.model.EventArgument;
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

public class ArgumentsPanel extends ViskitTablePanel
{
  private String[] mytitles = {"name","type","comment"};

  ArgumentsPanel(int wid)
  {
    super(wid);            // separate constructor from initialization
    init(true);
  }

  public String[] getColumnTitles()
  {
    return mytitles;
  }

  public String[] getFields(Object o, int rowNum)
  {
    String[] sa = new String[3];
    sa[0] = ((EventArgument)o).getName();
    sa[1] = ((EventArgument)o).getType();
    ArrayList  ar = ((EventArgument)o).getComments();
    if(ar.size() > 0)
      sa[2] = (String)((EventArgument)o).getComments().get(0);
    else
      sa[2] = "comment";
    return sa;
  }

  public Object newRowObject()
  {
    EventArgument ea = new EventArgument();
    ea.setName("name");
    ea.setType("int");
    return ea;
  }

  public int getNumVisibleRows()
  {
    return 3;
  }
}