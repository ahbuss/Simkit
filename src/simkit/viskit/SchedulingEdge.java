package simkit.viskit;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * OPNAV N81 - NPS World Class Modeling (WCM) 2004 Projects
 * MOVES Institute
 * Naval Postgraduate School, Monterey CA
 * www.nps.navy.mil
 * By:   Mike Bailey
 * Date: Mar 8, 2004
 * Time: 9:04:09 AM
 */

public class SchedulingEdge extends Edge
{
  public SchedulingEdge()
  {
    parameters = new ArrayList();
    try {
      parameters.add(new EdgeParameter("schparam1_St" ,new String("")));
      parameters.add(new EdgeParameter("schparam2_Int",new Integer(0)));
      parameters.add(new EdgeParameter("schparam3_HM" ,new HashMap()));
    }
    catch(Exception e) {
      System.err.println("bad Class.forName, SchlingEdge");
    }
  }
  Object copyShallow()
  {
    SchedulingEdge ce = new SchedulingEdge();
    ce.opaqueViewObject = opaqueViewObject;
    ce.to = to;
    ce.from = from;
    ce.parameters = parameters;
    ce.delay = delay;
    ce.conditional = conditional;
    return ce;
  }
}
