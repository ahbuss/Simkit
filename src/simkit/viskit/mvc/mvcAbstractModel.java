package simkit.viskit.mvc;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * OPNAV N81 - NPS World Class Modeling (WCM) 2004 Projects
 * MOVES Institute
 * Naval Postgraduate School, Monterey CA
 * www.nps.navy.mil
 * By:   Mike Bailey
 * Date: Mar 2, 2004
 * Time: 11:04:25 AM
 */

/**
 * Abstract root class of the model hierarchy.  Provides basic notification behavior.
 */

/**
 * From an article at www.jaydeetechnology.co.uk
 */

public abstract class mvcAbstractModel implements mvcModel
{
  private ArrayList listeners = new ArrayList(4);

  public void notifyChanged(mvcModelEvent event)
  {
    ArrayList list = (ArrayList)listeners.clone();
    for(Iterator it = list.iterator(); it.hasNext();) {
      mvcModelListener ml = (mvcModelListener)it.next();
      ml.modelChanged(event);
    }
  }

  public void addModelListener(mvcModelListener l)
  {
    listeners.add(l);
  }

  public void removeModelListener(mvcModelListener l)
  {
    listeners.remove(l);
  }
}
