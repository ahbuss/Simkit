package simkit.viskit;

import java.util.ArrayList;

/**
 * OPNAV N81 - NPS World Class Modeling (WCM) 2004 Projects
 * MOVES Institute
 * Naval Postgraduate School, Monterey CA
 * www.nps.navy.mil
 * By:   Mike Bailey
 * Date: Mar 8, 2004
 * Time: 9:04:09 AM
 */

public class CancellingEdge extends Edge
{
  public CancellingEdge()
  {
    parameters = new ArrayList();
    try {
      parameters.add(new EdgeParameter("canparam1_St",Class.forName("java.lang.String")));
      parameters.add(new EdgeParameter("canparam2_Int",Class.forName("java.lang.Integer")));
      parameters.add(new EdgeParameter("canparam3_HM",Class.forName("java.util.HashMap")));
    }
    catch(Exception e) {
      System.err.println("bad Class.forName, CancellingEdge");
    }
  }
  Object copyShallow()
  {
    CancellingEdge ce = new CancellingEdge();
    ce.opaqueViewObject = opaqueViewObject;
    ce.to = to;
    ce.from = from;
    ce.parameters = parameters;
    ce.conditional = conditional;
    ce.delay = delay;
    return ce;
  }
}
