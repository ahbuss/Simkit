package simkit.viskit;

import simkit.viskit.mvc.mvcAbstractModel;

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
 * Date: Mar 2, 2004
 * Time: 1:09:38 PM
 */

/**
 * This is the "master" model of an event graph.  It should hold the node, edge and assembly
 * information.  What hasn't been done is to put in accessor methods for the view to
 * read pieces that it needs, say after it receives a "new model" event.
 */
public class Model extends mvcAbstractModel implements ViskitModel
{
  private boolean modelDirty = false;

  /**
   * Boolean to signify whether the model has been changed since last disk save.
   *
   * @return true means changes have been made and it needs to be flushed.
   */
  public boolean isDirty()
  {
    return modelDirty;
  }

  /**
   * Replace existing model with one contained in the existing file.
   *
   * @param f
   */
  public void newModel(File f)
  {
    // put code to do it here

    this.newModel(new EventGraphXMLReader(/*f*/), true);   // something like
    this.notifyChanged(new ModelEvent(this, ModelEvent.NEWMODEL, "New model loaded from file"));
  }

  /**
   * Replace existing model with one contained in the existing reader source.
   *
   * @param reader
   */
  public void newModel(EventGraphXMLReader reader, boolean pIsEditable)
  {
    // put code to do it here

    modelDirty = false;
    this.notifyChanged(new ModelEvent(this, ModelEvent.NEWMODEL, "New model loaded from file"));
  }

  public Vector     getAllNodes ()
  {
    //todo
    return null;
  }

  public Vector    getStateVariables()
  {
    //todo
    return null;
  }
  public ArrayList getSimParameters()
  {
    //todo
    return null;
  }

  // parameter mods
  // --------------
  public void newSimParameter(Parameter p)
  {
    // put code to do it here

    modelDirty = true;
    this.notifyChanged(new ModelEvent(p, ModelEvent.SIMPARAMETERADDED, "Parameter added"));
  }

  public void deleteSimParameter(Parameter p)
  {
    // put code to do it here

    modelDirty = true;
    this.notifyChanged(new ModelEvent(p, ModelEvent.SIMPARAMETERDELETED, "Parameter deleted"));
  }

  public void changeSimParameter(Parameter p)
  {
    // put code to do it here

    modelDirty = true;
    this.notifyChanged(new ModelEvent(p, ModelEvent.SIMPARAMETERCHANGED, "Parameter changed"));
  }

  // State variable mods
  // -------------------
  public void newStateVariable(StateVariable sv)
  {
    // put code to do it here

    modelDirty = true;
    this.notifyChanged(new ModelEvent(sv, ModelEvent.STATEVARIABLEADDED, "State variable added"));
  }

  public void deleteStateVariable(StateVariable sv)
  {
    // put code to do it here

    modelDirty = true;
    this.notifyChanged(new ModelEvent(sv, ModelEvent.STATEVARIABLEDELETED, "State variable deleted"));
  }

  public void changeStateVariable(StateVariable sv)
  {
    // put code to do it here

    modelDirty = true;
    this.notifyChanged(new ModelEvent(sv, ModelEvent.STATEVARIABLEDELETED, "State variable changed"));
  }

  // Event (node) mods
  // -----------------
   /**
   * Add a new event to the graph with the given label, at the given point
   *
   */
  public void newEvent(EventNode node, Point p)
  {
    if(p == null) p = new Point(100,100);
    // put code to do it here

    modelDirty = true;
    Object[] oa = new Object[]{node, p};

    ModelEvent mev = new ModelEvent(oa, ModelEvent.EVENTADDED, "Event added");
    this.notifyChanged(mev);
  }

  /**
   * Delete the referenced event, also deleting attached edges.
   *
   * @param node
   */
  public void deleteEvent(EventNode node)
  {

    // todo instert code to do it
    modelDirty = true;
    this.notifyChanged(new ModelEvent(node, ModelEvent.EVENTDELETED, "Event deleted"));
  }

  public void changeEvent(EventNode ev)
  {
    // todo insert code to do it
    modelDirty = true;
    this.notifyChanged(new ModelEvent(ev, ModelEvent.EVENTCHANGED, "Event changed"));
  }

  // Edge mods
  // ---------
  public void newEdge(EventNode src, EventNode target)
  {
    SchedulingEdge se = new SchedulingEdge();
    se.from = src;
    se.to = target;
    src.connections.add(se);
    target.connections.add(se);
    // put code to do it here

    modelDirty = true;

    this.notifyChanged(new ModelEvent(se, ModelEvent.EDGEADDED, "Edge added"));
  }

  public void newCancelEdge(EventNode src, EventNode target)
  {
    CancellingEdge ce = new CancellingEdge();
    ce.from = src;
    ce.to = target;
    src.connections.add(ce);
    target.connections.add(ce);
    // put code to do it here

    modelDirty = true;

    this.notifyChanged(new ModelEvent(ce, ModelEvent.CANCELLINGEDGEADDED, "Edge added"));
  }

  public void deleteEdge(SchedulingEdge edge)
  {
    // todo put code to do it here

    modelDirty = true;
    this.notifyChanged(new ModelEvent(edge, ModelEvent.EDGEDELETED, "Edge deleted"));
  }

  public void deleteCancelEdge(CancellingEdge edge)
  {
    // todo put code to do it here

    modelDirty = true;
    this.notifyChanged(new ModelEvent(edge, ModelEvent.CANCELLINGEDGEDELETED, "Cancelling edge deleted"));
  }

  public void changeEdge(Edge e)
  {
    // put code to do it here

    modelDirty = true;
    this.notifyChanged(new ModelEvent(e, ModelEvent.EDGECHANGED, "Edge changed"));
  }

  public void changeCancelEdge(Edge e)
  {
    // todo put code to do it here

    modelDirty = true;
    this.notifyChanged(new ModelEvent(e, ModelEvent.CANCELLINGEDGECHANGED, "Cancelling edge changed"));
  }


  public void saveModel(File f)
  {
    // if f == null, use same name
    // else, this is a "save as"
    if(f != null)
      System.out.println("Model.saveModel(" + f.getName() + ")");
    modelDirty = false;
  }
}
