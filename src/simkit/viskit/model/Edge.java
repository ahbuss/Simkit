package simkit.viskit.model;

import java.util.ArrayList;

/**
 * OPNAV N81 - NPS World Class Modeling (WCM) 2004 Projects
 * MOVES Institute
 * Naval Postgraduate School, Monterey CA
 * www.nps.navy.mil
 * By:   Mike Bailey
 * Date: Mar 8, 2004
 * Time: 2:57:37 PM
 */

abstract public class Edge extends ViskitElement
{
  public EventNode to;
  public EventNode from;
  public ArrayList parameters;
  public String    conditional;
  public String    delay;
  abstract Object  copyShallow();
}
