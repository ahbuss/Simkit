package simkit.viskit;

import java.io.File;
import java.awt.*;
import java.util.Vector;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: mike
 * Date: Mar 18, 2004
 * Time: 1:43:07 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ViskitModel
{
  /**
   * Messaged by controller when a new Model should be loaded.
   * @param f File representing persistent model representation.  If null, model resets itself to 0 nodes, 0 edges, etc.
   */
  public void newModel      (File f);

  /**
   * Save existing model to specified file.  If null, save to last file.  If no list file, error.
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
   * @return ArrayList of Parameter objects.
   */
  public ArrayList getSimParameters();


  // todo further comments...


  public void newEvent      (EventNode node, Point p);
  public void newEdge       (EventNode src, EventNode target);
  public void newCancelEdge (EventNode src, EventNode target);

  public void deleteEvent      (EventNode node);
  public void deleteEdge       (SchedulingEdge edge);
  public void deleteCancelEdge (CancellingEdge edge);

  public void changeEdge       (Edge e);
  public void changeCancelEdge (Edge e);
  public void changeEvent      (EventNode ev);

  public void newStateVariable    (StateVariable sv);
  public void newSimParameter     (Parameter p);
  public void changeStateVariable (StateVariable st);
  public void changeSimParameter  (Parameter p);
  public void deleteStateVariable (StateVariable sv);
  public void deleteSimParameter  (Parameter p);
}
