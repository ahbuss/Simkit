package simkit.viskit.model;

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
  SchedulingEdge()       // package-limited
  {
    parameters = new ArrayList();

/*
    parameters.add(new vParameter("schparam1_St" ,"java.lang.String"));
    parameters.add(new vParameter("schparam2_Int","java.lang.Integer"));
    parameters.add(new vParameter("schparam3_HM" ,"java.util.HashMap"));
*/

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
