package simkit.viskit.mvc;

import java.awt.event.ActionEvent;

/**
 * OPNAV N81 - NPS World Class Modeling (WCM) 2004 Projects
 * MOVES Institute
 * Naval Postgraduate School, Monterey CA
 * www.nps.navy.mil
 * By:   Mike Bailey
 * Date: Mar 2, 2004
 * Time: 11:01:04 AM
 */

/**
 * From an article at www.jaydeetechnology.co.uk
 */

/**
 * Used to notify interested objects of changes in the state of a model
 */

public class mvcModelEvent extends ActionEvent
{
  public mvcModelEvent(Object obj, int id, String message)
  {
    super(obj,id,message);
  }

}
