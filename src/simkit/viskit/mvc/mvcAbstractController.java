package simkit.viskit.mvc;

/**
 * OPNAV N81 - NPS World Class Modeling (WCM) 2004 Projects
 * MOVES Institute
 * Naval Postgraduate School, Monterey CA
 * www.nps.navy.mil
 * By:   Mike Bailey
 * Date: Mar 2, 2004
 * Time: 11:30:36 AM
 */

/**
 * From an article at www.jaydeetechnology.co.uk
 */

public abstract class mvcAbstractController implements mvcController
{
  private mvcView view;
  private mvcModel model;

  public mvcModel getModel()
  {
    return model;
  }

  public mvcView getView()
  {
    return view;
  }

  public void setModel(mvcModel model)
  {
    this.model = model;
  }

  public void setView(mvcView view)
  {
    this.view = view;
  }
}
