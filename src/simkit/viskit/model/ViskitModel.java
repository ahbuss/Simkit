package simkit.viskit.model;

import java.io.File;
import java.awt.*;
import java.util.Vector;
import java.util.ArrayList;

/**
 * OPNAV N81 - NPS World Class Modeling (WCM) 2004 Projects
 * MOVES Institute
 * Naval Postgraduate School, Monterey CA
 * www.nps.navy.mil
 * By:   Mike Bailey
 * Date: Mar 18, 2004
 * Time: 1:43:07 PM
 */

public interface ViskitModel
{
  /**
   * Separate initialization from object construction.
   */
  public void init();

  /**
   * Messaged by controller when a new Model should be loaded.
   * @param f File representing persistent model representation.  If null, model resets itself to 0 nodes, 0 edges, etc.
   */
  public void newModel      (File f);

  /**
   * Save existing model to specified file.  If null, save to last file.  If no last file, error.
   * @param f File to save to.
   */
  public void saveModel     (File f);

  /**
   *  Reports saved state of model.  Becomes "clean" after a save.
   */
  public boolean isDirty       ();

  /**
   * This is messaged by the controller, typically after a newModel(f) message.  It is used to get a vector of all the
   * nodes in the graph.  Since the EventNode object has src and target members, it also serves to get all the edges.
   * @return Vector of EventNodes.
   */
  public Vector    getAllNodes ();

  /**
   * Messaged by controller to get all defined StateVariables.
   * @return Vector of StateVariables.
   */
  public Vector    getStateVariables();
  
  /**
   * Messaged by controller to get all defined simulation parameters.  Order (may be) important (?), ergo ArrayList container.
   * @return ArrayList of vParameter objects.
   */
  public ArrayList getSimParameters();


  // todo further comments...


  public void newEvent      (String nodeName, Point p);
  public void newEdge       (EventNode src, EventNode target);
  public void newCancelEdge (EventNode src, EventNode target);

  public void deleteEvent      (EventNode node);
  public void deleteEdge       (SchedulingEdge edge);
  public void deleteCancelEdge (CancellingEdge edge);

  public void changeEdge       (Edge e);
  public void changeCancelEdge (Edge e);
  public void changeEvent      (EventNode ev);

  public void newStateVariable    (String name, String type, String initVal, String comment);
  public void newSimParameter     (String name, String type, String initVal, String comment);
  public void changeStateVariable (vStateVariable st);
  public void changeSimParameter  (vParameter p);
  public void deleteStateVariable (String sv);
  public void deleteStateVariable (vStateVariable sv);
  public void deleteSimParameter  (vParameter p);
  public void deleteSimParameter  (String p);
}
