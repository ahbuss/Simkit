package simkit.viskit;

import java.util.Vector;

/**
 * OPNAV N81 - NPS World Class Modeling (WCM) 2004 Projects
 * MOVES Institute
 * Naval Postgraduate School, Monterey CA
 * www.nps.navy.mil
 * By:   Mike Bailey
 * Date: Mar 8, 2004
 * Time: 9:08:08 AM
 */

/**
 * An event as seen by the model (not the view)
 */
public class EventNode extends ViskitElement
{
  public String name;
  //public Object opaqueUserObject;
  public String stateTrans;
  public EventNode(String name)
  {
    this.name = name;
  }
  public String toString()
  {
    return name;
  }
  public Vector connections = new Vector();
  public Object clone()
  {
    EventNode en = new EventNode(name+"-copy");
    en.stateTrans = stateTrans;
    return en;
  }
}
