package simkit.viskit.mvc;

/**
 * OPNAV N81 - NPS World Class Modeling (WCM) 2004 Projects
 * MOVES Institute
 * Naval Postgraduate School, Monterey CA
 * www.nps.navy.mil
 * By:   Mike Bailey
 * Date: Mar 2, 2004
 * Time: 10:53:21 AM
 */

/**
 * From an article at www.jaydeetechnology.co.uk
 */

/**
 *  Primary role of a model is to manage the underlying information
 */
public interface mvcModel
{
   void notifyChanged(mvcModelEvent ev);
}
