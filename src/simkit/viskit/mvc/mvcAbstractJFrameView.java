package simkit.viskit.mvc;

import javax.swing.*;

/**
 * OPNAV N81 - NPS World Class Modeling (WCM) 2004 Projects
 * MOVES Institute
 * Naval Postgraduate School, Monterey CA
 * www.nps.navy.mil
 * By:   Mike Bailey
 * Date: Mar 2, 2004
 * Time: 11:33:58 AM
 */

/**
 * From an article at www.jaydeetechnology.co.uk
 */

public class mvcAbstractJFrameView extends JFrame implements mvcView, mvcModelListener
{
  private mvcModel model;
  private mvcController controller;

  public mvcAbstractJFrameView(String title)
  {
    super(title);
  }
  public void registerWithModel()
  {
    ((mvcAbstractModel)model).addModelListener(this);
  }

  public mvcController getController()
  {
    return controller;
  }

  public mvcModel getModel()
  {
    return model;
  }

  public void setController(mvcController controller)
  {
    this.controller = controller;
  }

  public void setModel(mvcModel model)
  {
    this.model = model;
    registerWithModel();
  }

  public void modelChanged(mvcModelEvent event)
  {
  }
}
