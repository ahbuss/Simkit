package simkit.viskit.model;

/**
 * OPNAV N81 - NPS World Class Modeling (WCM) 2004 Projects
 * MOVES Institute
 * Naval Postgraduate School, Monterey CA
 * www.nps.navy.mil
 * By:   Mike Bailey
 * Date: Mar 19, 2004
 * Time: 2:10:07 PM
 */

/**
 *  Base class for the objects that get passed around between M, V and C.
 */
abstract public class ViskitElement
{
  public Object opaqueViewObject;       // for private use of V
  public Object opaqueModelObject;      // for private use of M
  public Object opaqueControllerObject; // for private use of C

  protected ViskitElement shallowCopy(ViskitElement newVe)      // shallow copy
  {
    newVe.opaqueControllerObject = this.opaqueControllerObject;
    newVe.opaqueViewObject       = this.opaqueViewObject;
    newVe.opaqueModelObject      = this.opaqueModelObject;
    newVe.modelKey               = this.modelKey;
    return newVe;
  }

  // every node or edge has a unique key
  private static int seqID = 0;
  private Object modelKey = ""+seqID++;

  public Object getModelKey()   // package access
  {
    return modelKey;
  }

}
