package simkit.viskit.mvc;

/**
 * OPNAV N81 - NPS World Class Modeling (WCM) 2004 Projects
 * MOVES Institute
 * Naval Postgraduate School, Monterey CA
 * www.nps.navy.mil
 * By:   Mike Bailey
 * Date: Mar 2, 2004
 * Time: 10:55:20 AM
 */

/**
 * From an article at www.jaydeetechnology.co.uk
 */

/**
 * Primary role of controller is to determine what should happen in response to user input.
 */

public interface mvcController
{
  void setModel(mvcModel model);
  void setView (mvcView  view);
  
  mvcModel getModel();
  mvcView  getView();
}
